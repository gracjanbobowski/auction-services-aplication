package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.BidService;
import com.example.auctionservicesaplication.service.EmailService;
import com.example.auctionservicesaplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    public BidController(AuctionService auctionService, BidService bidService, UserService userService, EmailService emailService) {
        this.auctionService = auctionService;
        this.bidService = bidService;
        this.userService = userService;
        this.emailService = emailService;
    }

    // Retrieves and displays a list of all auctions for bidding.
    @GetMapping
    public String getAllBids(Model model) {
        List<Auction> auctions = auctionService.getAllAuction();
        model.addAttribute("auctions", auctions);
        return "bids";
    }

    // Fetches and displays the bids for a specific auction.
    @GetMapping("/{auctionId}/bids")
    public String getAuctionBids(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        List<Bid> bids = bidService.getBidsByAuction(auction);
        model.addAttribute("auction", auction);
        model.addAttribute("bids", bids);
        return "bids";
    }

    // Fetches and displays details for a specific auction.
    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);
        return "auctionDetails";
    }

    // Fetches and displays the bids for a specific auction along with the current highest bid.
    @GetMapping("/{auctionId}/bidsForAuction")
    public String getBidsForAuction(@PathVariable BigDecimal auctionId, Model model) {
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
    public String getPlaceBidForm(@PathVariable BigDecimal auctionId, Model model) {
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
            @PathVariable BigDecimal auctionId,
            @ModelAttribute("newBid") @Valid Bid newBid,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Retrieve auction and current authentication details.
        Auction auction = auctionService.getAuctionById(auctionId);

        // Validate the bid form.
        if (bindingResult.hasErrors()) {
            return "placeBidForm";
        }

        // Retrieve bidder information.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String bidderUsername = authentication.getName();
        User bidder = userService.getUserByUsername(bidderUsername);

        // Set details for the new bid.
        newBid.setAuction(auction);
        newBid.setBidder(bidder);
        newBid.setBidTime(LocalDateTime.now());

        // Validate the bid for correctness.
        validateBid(newBid, auction, bindingResult);

        // If there are errors, return to the form.
        if (bindingResult.hasErrors()) {
            return "placeBidForm";
        }

        // Save the bid to the database.
        bidService.createBid(newBid);

        // Update auction price if the new bid is higher.
        BigDecimal highestBidAmount = auctionService.getHighestBidAmount(auction);
        if (newBid.getBidAmount().compareTo(highestBidAmount) > 0) {
            auction.setCurrentPrice(newBid.getBidAmount());
            auctionService.updateAuction(auction);

            // Send a bid confirmation to the seller's email.
            bidService.sendBidConfirmation(auction.getSeller().getEmail(), newBid.getBidder().getUsername(), auction.getTitle());
        }

        // Redirect to the bids page for the auction.
        return "redirect:/bids/" + auctionId + "/bidsForAuction";
    }

    // Private method for bid validation.
    private void validateBid(Bid newBid, Auction auction, BindingResult bindingResult) {
        List<Bid> bids = bidService.getBidsByAuction(auction);

        // Check if the new bid is higher than the current highest bid.
        if (bids.stream().anyMatch(bid -> bid.getBidAmount().compareTo(newBid.getBidAmount()) >= 0)) {
            bindingResult.rejectValue("bidAmount", "error.bid", "Bid amount must be higher than the current highest bid.");
        }
    }
}
