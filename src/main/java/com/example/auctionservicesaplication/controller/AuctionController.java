package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//AuctionController: Obsługuje żądania związane z aukcjami.
@Controller
@RequestMapping("/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping
    public String getAllAuctions(Model model) {
        List<Auction> auctions = auctionService.getAllAuction();
        model.addAttribute("auctions", auctions);
        return "auctions";
    }

    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable Long auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(BigDecimal.valueOf(auctionId));
        model.addAttribute("auction", auction);
        return "auctionDetails";
    }

    @GetMapping("/create")
    public String getCreatedForm(Model model) {
        model.addAttribute("auction", new Auction());
        return "createdForm";
    }

    @PostMapping("/craete")
    public String createAuction(@ModelAttribute Auction auction, Model model) {
        auctionService.createAuction(auction);
        return "redirect:/auctions";
    }

    @GetMapping("/{auctionId}/edit")
    public String getEditForm(@PathVariable Long auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(BigDecimal.valueOf(auctionId));
        model.addAttribute("auction", auction);
        return "editForm";
    }

    @PostMapping("/{auctionId}/edit")
    public String editAuction(@PathVariable Long auctionId, @ModelAttribute Auction editedAuction) {
        auctionService.editAuction(BigDecimal.valueOf(auctionId), editedAuction);
        return "redirect:/auctions";
    }

    @GetMapping("/{auctionId}/delete")
    public String deleteAuction(@PathVariable Long auctionId) {
        auctionService.deleteAuction(BigDecimal.valueOf(auctionId));
        return "redirect:/auctions";
    }

}
