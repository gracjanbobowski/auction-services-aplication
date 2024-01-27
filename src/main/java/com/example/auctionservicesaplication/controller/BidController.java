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

@Controller
@RequestMapping("/bids")
public class BidController {

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

    @GetMapping
    public String getAllBids(Model model) {
        List<Auction> auctions = auctionService.getAllAuction();
        model.addAttribute("auctions", auctions);
        return "bids";
    }

    @GetMapping("/{auctionId}/bids")
    public String getAuctionBids(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        List<Bid> bids = bidService.getBidsByAuction(auction);
        model.addAttribute("auction", auction);
        model.addAttribute("bids", bids);
        return "bids";
    }

    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);
        return "auctionDetails";
    }

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

    @GetMapping("/{auctionId}/details/placeBid")
    public String getPlaceBidForm(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);

        Bid newBid = new Bid();
        model.addAttribute("newBid", newBid);

        BigDecimal currentAuctionPrice = auctionService.getHighestBidAmount(auction);
        model.addAttribute("currentAuctionPrice", currentAuctionPrice);

        return "placeBidForm";
    }

    @PostMapping("/{auctionId}/placeBid")
    public String placeBid(
            @PathVariable BigDecimal auctionId,
            @ModelAttribute("newBid") @Valid Bid newBid,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        Auction auction = auctionService.getAuctionById(auctionId);

        if (bindingResult.hasErrors()) {
            return "placeBidForm";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String bidderUsername = authentication.getName();
        User bidder = userService.getUserByUsername(bidderUsername);

        newBid.setAuction(auction);
        newBid.setBidder(bidder);
        newBid.setBidTime(LocalDateTime.now());

        validateBid(newBid, auction, bindingResult);

        if (bindingResult.hasErrors()) {
            return "placeBidForm";
        }

        bidService.createBid(newBid);

        BigDecimal highestBidAmount = auctionService.getHighestBidAmount(auction);
        if (newBid.getBidAmount().compareTo(highestBidAmount) > 0) {
            auction.setCurrentPrice(newBid.getBidAmount());
            auctionService.updateAuction(auction);

            // Wysyłanie potwierdzenia licytacji
            bidService.sendBidConfirmation(auction.getSeller().getEmail(), newBid.getBidder().getUsername(), auction.getTitle());
        }

        return "redirect:/bids/" + auctionId + "/bidsForAuction";
    }

    private void validateBid(Bid newBid, Auction auction, BindingResult bindingResult) {
        List<Bid> bids = bidService.getBidsByAuction(auction);

        if (bids.stream().anyMatch(bid -> bid.getBidAmount().compareTo(newBid.getBidAmount()) >= 0)) {
            bindingResult.rejectValue("bidAmount", "error.bid", "Bid amount must be higher than the current highest bid.");
        }
    }
}