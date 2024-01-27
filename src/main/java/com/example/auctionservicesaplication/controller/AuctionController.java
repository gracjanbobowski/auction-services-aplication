package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.message.AuctionNotFoundException;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final CategoryService categoryService;

    @Autowired
    public AuctionController(AuctionService auctionService, CategoryService categoryService) {
        this.auctionService = auctionService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllAuctions(Model model) {
        model.addAttribute("auctions", auctionService.getAllAuction());
        return "auctions";
    }

    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable BigDecimal auctionId, Model model) {
        model.addAttribute("auction", getAuctionOrThrow(auctionId));
        return "auctionDetails";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("auction", new Auction());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "createdForm";
    }

    @PostMapping("/create")
    public String createAuction(@ModelAttribute Auction auction, Model model) {
        if (isAuctionValid(auction)) {
            auctionService.createAuction(auction);
            return "redirect:/auctions";
        }
        model.addAttribute("error", "Invalid auction data");
        return "errorView";
    }

    @GetMapping("/{auctionId}/edit")
    public String getEditForm(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = getAuctionOrThrow(auctionId);
        model.addAttribute("auction", auction);
        model.addAttribute("categories", categoryService.getAllCategories());
        if (auction.getStartTime() != null) {
            model.addAttribute("formattedStartTime", auction.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        }
        if (auction.getEndTime() != null) {
            model.addAttribute("formattedEndTime", auction.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        }
        return "editFormAuction";
    }

    @PostMapping("/{auctionId}/edit")
    public String editAuction(@ModelAttribute Auction auction, @PathVariable BigDecimal auctionId) {
        getAuctionOrThrow(auctionId); // Throws if not found
        auctionService.editAuction(auctionId, auction);
        return "redirect:/auctions";
    }

    @GetMapping("/{auctionId}/delete")
    public String deleteAuction(@PathVariable BigDecimal auctionId) {
        getAuctionOrThrow(auctionId); // Throws if not found
        auctionService.deleteAuction(auctionId);
        return "redirect:/auctions";
    }

    @ExceptionHandler(AuctionNotFoundException.class)
    public String handleAuctionNotFoundException(AuctionNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "auctionNotFound";
    }

    private Auction getAuctionOrThrow(BigDecimal auctionId) {
        return auctionService.getAuctionById(auctionId);
    }

    private boolean isAuctionValid(Auction auction) {
        return auction.getTitle() != null && auction.getDescription() != null;
    }
}
