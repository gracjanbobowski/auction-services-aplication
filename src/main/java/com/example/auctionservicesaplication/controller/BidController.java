package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.BidService;
import com.example.auctionservicesaplication.service.EmailService;
import com.example.auctionservicesaplication.service.UserService;
import com.example.auctionservicesaplication.util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Controller that manages bid-related operations in the application.
@Controller
@RequestMapping("/bids")
public class BidController {

    // Dependency injection through the constructor for required services.
    private final AuctionService auctionService;
    private final BidService bidService;
    private final UserService userService;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(BidController.class);


    @Autowired
    public BidController(AuctionService auctionService, BidService bidService, UserService userService, EmailService emailService) {
        this.auctionService = auctionService;
        this.bidService = bidService;
        this.userService = userService;
        this.emailService = emailService;
    }

    // Retrieves and displays a list of all auctions for bidding.
    @GetMapping
    public String getAllBids(Model model, Authentication authentication) {
        List<Auction> auctions = auctionService.getAllAuction();
        model.addAttribute("auctions", auctions);

        // Determine the home redirect URL based on the user's role
        model.addAttribute("homeRedirectUrl", ControllerUtil.determineHomeRedirectUrl(authentication));

        return "bids";
    }

    // Fetches and displays the bids for a specific auction.
    @GetMapping("/{auctionId}/bids")
    public String getAuctionBids(@PathVariable Long auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        List<Bid> bids = bidService.getBidsByAuction(auction);
        model.addAttribute("auction", auction);
        model.addAttribute("bids", bids);
        return "bids";
    }

    // Fetches and displays details for a specific auction.
    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable Long auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);
        return "auctionDetails";
    }

    // Fetches and displays the bids for a specific auction along with the current highest bid.
    @GetMapping("/{auctionId}/bidsForAuction")
    public String getBidsForAuction(@PathVariable Long auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        List<Bid> bids = bidService.getBidsByAuction(auction);
        BigDecimal currentBidAmount = auction.getCurrentPrice();
        model.addAttribute("auction", auction);
        model.addAttribute("bids", bids);
        model.addAttribute("currentBidAmount", currentBidAmount);
        return "bidsForAuction";
    }

    // Fetches the form for placing a new bid on an auction.
    @GetMapping("/{auctionId}/details/placeBid")
    public String getPlaceBidForm(@PathVariable Long auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);

        // Create a new bid instance and pass it to the form.
        Bid newBid = new Bid();
        model.addAttribute("newBid", newBid);

        // Retrieve the current auction price.
        BigDecimal currentAuctionPrice = auctionService.getHighestBidAmount(auction);
        model.addAttribute("currentAuctionPrice", currentAuctionPrice);

        return "placeBidForm";
    }

    // Handles the process of placing a new bid on an auction.
    @PostMapping("/{auctionId}/placeBid")
    public String placeBid(
            @PathVariable Long auctionId,
            @ModelAttribute("newBid") @Valid Bid newBid,
            BindingResult bindingResult,
            Model model) {

        // Retrieve auction details.
        Auction auction = auctionService.getAuctionById(auctionId);

        // Retrieve bidder information.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User bidder = userService.getUserByUsername(authentication.getName());

        // Set details for the new bid.
        newBid.setAuction(auction);
        newBid.setBidder(bidder);
        newBid.setBidTime(LocalDateTime.now());

        // Validate the bid for correctness.
        if (bindingResult.hasErrors() || !validateBid(newBid, auction, bindingResult)) {
            model.addAttribute("auction", auction);
            model.addAttribute("newBid", newBid);
            return "placeBidForm";
        }

        // Save the bid to the database.
        bidService.createBid(newBid);

        // Update auction price if the new bid is higher and send confirmation email.
        updateAuctionPriceAndSendConfirmationIfNeeded(auction, newBid, bindingResult);

        // Redirect to the bids page for the auction.
        return "redirect:/bids/" + auctionId + "/bidsForAuction";
    }

    // Helper method to update auction price if the new bid is higher and send confirmation email.
    private void updateAuctionPriceAndSendConfirmationIfNeeded(Auction auction, Bid newBid, BindingResult bindingResult) {
        if (validateBid(newBid, auction, bindingResult)) {
            auction.setCurrentPrice(newBid.getBidAmount());
            auctionService.updateAuction(auction);
            sendBidConfirmationEmail(auction, newBid);
        }
    }

    // Helper method to send bid confirmation email.
    private void sendBidConfirmationEmail(Auction auction, Bid newBid) {
        try {
            bidService.sendBidConfirmation(auction.getSeller().getEmail(), newBid.getBidder().getUsername(), auction.getTitle());
        } catch (Exception e) {
            logger.error("Error sending email: " + e.getMessage(), e);
        }
    }

    // Helper method for bid validation.
    private boolean validateBid(Bid newBid, Auction auction, BindingResult bindingResult) {
        BigDecimal currentPrice = auction.getCurrentPrice();
        BigDecimal highestBidAmount = auctionService.getHighestBidAmount(auction);

        // Check if the new bid is higher than both the current auction price and the current highest bid.
        if (newBid.getBidAmount().compareTo(currentPrice) <= 0 || newBid.getBidAmount().compareTo(highestBidAmount) <= 0) {
            bindingResult.rejectValue("bidAmount", "error.bid", "Bid amount must be higher than the current price.");
            return false;
        }
        return true;
    }

}
