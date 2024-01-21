package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.BidService;
import com.example.auctionservicesaplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bids")
public class BidController {

    private final AuctionService auctionService;
    private final BidService bidService;
    private final UserService userService;

    @Autowired
    public BidController(AuctionService auctionService, BidService bidService, UserService userService) {
        this.auctionService = auctionService;
        this.bidService = bidService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllBids(Model model) {
        List<Auction> auctions = auctionService.getAllAuction();  // Corrected method name
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

    @GetMapping("/{auctionId}/details")
    public String getBidDetails(@PathVariable BigDecimal auctionId, @PathVariable BigDecimal bidId, Model model) {
        Bid bid = bidService.getBidById(bidId);
        model.addAttribute("bid", bid);
        return "bidDetails";
    }

    //    @PostMapping("/{auctionId}/bids/{bidId}/placeBid")
//    public String placeBid(
//            @PathVariable Long auctionId,
//            @PathVariable Long bidId,
//            @RequestParam BigDecimal newBidAmount) {
//        // Pobierz aukcję i licytantów, a następnie utwórz nową ofertę i dodaj ją do listy ofert aukcji
//        Auction auction = auctionService.getAuctionById(BigDecimal.valueOf(auctionId));
//
//        // Pobierz zalogowanego użytkownika przy użyciu Spring Security
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User bidder = userService.getUserById(BigDecimal.valueOf(Long.valueOf(authentication.getName())));
//
//        Bid newBid = new Bid();
//        newBid.setAuction(auction);
//        newBid.setBidder(bidder);
//        newBid.setBidAmount(newBidAmount.doubleValue());
//        newBid.setBidTime(LocalDateTime.now());
//
//
//        // Umieść logikę walidacji, czy oferta jest wyższa niż aktualna najwyższa oferta itp.
//
//        auctionService.addBidToAuction(auction, newBid);
//        auctionService.editAuction(BigDecimal.valueOf(auctionId), auction);
//
//        return "redirect:/auctions/{auctionId}";
//    }
    @PostMapping("/{auctionId}/details/placeBid")
    public String placeBid(
            @PathVariable BigDecimal auctionId,
            @RequestParam Double newBidAmount) {
        // Fetch auction, bidder, and create a new bid
        Auction auction = auctionService.getAuctionById(auctionId);

        // Fetch the authenticated user using Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User bidder = userService.getUserById(BigDecimal.valueOf(Long.parseLong(authentication.getName())));

        Bid newBid = new Bid();
        newBid.setAuction(auction);
        newBid.setBidder(bidder);
        newBid.setBidAmount(newBidAmount.doubleValue());
        newBid.setBidTime(LocalDateTime.now());

        // Add validation logic here, e.g., check if the bid is higher than the current highest bid, etc.
        validateBid(newBid, auction);

        bidService.createBid(newBid);

        return "redirect:/bids/" + auctionId + "/details";
    }

    private void validateBid(Bid newBid, Auction auction) {
        List<Bid> bids = bidService.getBidsByAuction(auction);

        // Add your validation logic here, for example, check if the new bid is higher than the current highest bid

        // Example: Check if the new bid amount is higher than the highest bid amount
        if (bids.stream().anyMatch(bid -> bid.getBidAmount() >= newBid.getBidAmount())) {
            // Throw an exception or handle the validation error
            throw new IllegalArgumentException("Bid amount must be higher than the current highest bid.");
        }
    }
}
