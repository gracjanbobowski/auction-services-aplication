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

    // Iniekcja zależności poprzez konstruktor.
    @Autowired
    public BidController(AuctionService auctionService, BidService bidService, UserService userService, EmailService emailService) {
        this.auctionService = auctionService;
        this.bidService = bidService;
        this.userService = userService;
        this.emailService = emailService;
    }
    // Pobiera i wyświetla listę wszystkich aukcji.
    @GetMapping
    public String getAllBids(Model model) {
        List<Auction> auctions = auctionService.getAllAuction();
        model.addAttribute("auctions", auctions);
        return "bids";
    }
    // Pobiera i wyświetla oferty dla konkretnej aukcji.
    @GetMapping("/{auctionId}/bids")
    public String getAuctionBids(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        List<Bid> bids = bidService.getBidsByAuction(auction);
        model.addAttribute("auction", auction);
        model.addAttribute("bids", bids);
        return "bids";
    }
    // Pobiera i wyświetla szczegóły danej aukcji.
    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);
        return "auctionDetails";
    }
    // Pobiera i wyświetla oferty dla danej aukcji wraz z aktualną ceną.
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
    // Pobiera formularz do złożenia nowej oferty na aukcji.
    @GetMapping("/{auctionId}/details/placeBid")
    public String getPlaceBidForm(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);
        // Tworzy nową ofertę i przekazuje ją do formularza.
        Bid newBid = new Bid();
        model.addAttribute("newBid", newBid);
        // Pobiera aktualną cenę aukcji.
        BigDecimal currentAuctionPrice = auctionService.getHighestBidAmount(auction);
        model.addAttribute("currentAuctionPrice", currentAuctionPrice);

        return "placeBidForm";
    }
    // Obsługuje proces składania nowej oferty na aukcji.
    @PostMapping("/{auctionId}/placeBid")
    public String placeBid(
            @PathVariable BigDecimal auctionId,
            @ModelAttribute("newBid") @Valid Bid newBid,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        // Pobiera aukcję i bieżące dane autentykacji.
        Auction auction = auctionService.getAuctionById(auctionId);
        // Waliduje formularz oferty.
        if (bindingResult.hasErrors()) {
            return "placeBidForm";
        }
        // Pobiera informacje o licytującym.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String bidderUsername = authentication.getName();
        User bidder = userService.getUserByUsername(bidderUsername);
        // Ustawia dane nowej oferty.
        newBid.setAuction(auction);
        newBid.setBidder(bidder);
        newBid.setBidTime(LocalDateTime.now());
        // Waliduje ofertę pod kątem poprawności.
        validateBid(newBid, auction, bindingResult);
        // Jeśli są błędy, zwraca formularz.
        if (bindingResult.hasErrors()) {
            return "placeBidForm";
        }
        // Zapisuje ofertę w bazie danych.
        bidService.createBid(newBid);
        // Aktualizuje cenę aukcji, jeśli nowa oferta jest wyższa.
        BigDecimal highestBidAmount = auctionService.getHighestBidAmount(auction);
        if (newBid.getBidAmount().compareTo(highestBidAmount) > 0) {
            auction.setCurrentPrice(newBid.getBidAmount());
            auctionService.updateAuction(auction);

            // Wysyła potwierdzenie licytacji na adres e-mail sprzedawcy.
            bidService.sendBidConfirmation(auction.getSeller().getEmail(), newBid.getBidder().getUsername(), auction.getTitle());
        }
        // Przekierowuje na stronę z ofertami dla danej aukcji.
        return "redirect:/bids/" + auctionId + "/bidsForAuction";
    }
    // Prywatna metoda do walidacji oferty.
    private void validateBid(Bid newBid, Auction auction, BindingResult bindingResult) {
        List<Bid> bids = bidService.getBidsByAuction(auction);
    // Sprawdza, czy nowa oferta jest wyższa niż aktualnie najwyższa oferta.
        if (bids.stream().anyMatch(bid -> bid.getBidAmount().compareTo(newBid.getBidAmount()) >= 0)) {
            bindingResult.rejectValue("bidAmount", "error.bid", "Bid amount must be higher than the current highest bid.");
        }
    }
}