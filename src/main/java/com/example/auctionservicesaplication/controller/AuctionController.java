package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.CategoryService;
import com.example.auctionservicesaplication.model.Category;
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
    private final CategoryService categoryService;

    @Autowired
    public AuctionController(AuctionService auctionService, CategoryService categoryService) {
        this.auctionService = auctionService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllAuctions(Model model) {
        List<Auction> auctions = auctionService.getAllAuction();
        model.addAttribute("auctions", auctions);
        return "auctions";
    }


    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);
        return "auctionDetails";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("auction", new Auction());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "createdForm"; // Corrected template name
    }

    @PostMapping("/create")
    public String createAuction(@ModelAttribute Auction auction, Model model) {
        auctionService.createAuction(auction);
        return "redirect:/auctions";
    }

    @GetMapping("/{auctionId}/edit")
    public String getEditForm(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("auction", auction);
        model.addAttribute("categories", categories);
        return "editFormAuction";
    }

    @PostMapping("/{auctionId}/edit")
    public String editAuction(@ModelAttribute Auction auction, @PathVariable BigDecimal auctionId) {
        if (auctionService.getAuctionById(auctionId) == null) {
            return "auctionNotFound";
        }
        auctionService.editAuction(auctionId, auction);
        return "redirect:/auctions";
    }

    @GetMapping("/{auctionId}/delete")
    public String deleteAuction(@PathVariable BigDecimal auctionId) {
        auctionService.deleteAuction(auctionId);
        return "redirect:/auctions";
    }
}