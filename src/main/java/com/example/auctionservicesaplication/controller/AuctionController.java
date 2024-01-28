package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.BidService;
import com.example.auctionservicesaplication.service.CategoryService;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final BidService bidService;
    private final UserService userService;
    // Iniekcja zależności poprzez konstruktor.
    @Autowired
    public AuctionController(AuctionService auctionService, CategoryService categoryService, BidService bidService, UserService userService) {
        this.auctionService = auctionService;
        this.categoryService = categoryService;
        this.bidService = bidService;
        this.userService = userService;
    }
    // Pobiera i wyświetla listę wszystkich aukcji.
    @GetMapping
    public String getAllAuctions(Model model) {
        List<Auction> auctions = auctionService.getAllAuction();
        model.addAttribute("auctions", auctions);
        return "auctions";
    }
    // Pobiera i wyświetla szczegóły danej aukcji.
    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        model.addAttribute("auction", auction);
        return "auctionDetails";
    }
    // Pobiera formularz do utworzenia nowej aukcji.
    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("auction", new Auction());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "createdForm"; // Corrected template name
    }
    // Obsługuje proces utworzenia nowej aukcji.
    @PostMapping("/create")
    public String createAuction(@ModelAttribute Auction auction, Model model, Authentication authentication) {
        // Sprawdź, czy użytkownik jest zalogowany
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String loggedInUsername = userDetails.getUsername();

            // Pobierz obiekt User na podstawie nazwy użytkownika
            User loggedInUser = userService.getUserByUsername(loggedInUsername);

            // Utwórz aukcję, przekazując obiekt User
            auctionService.createAuction(auction, loggedInUser);

            // Dodaj informacje o zalogowanym użytkowniku do modelu
            model.addAttribute("loggedInUser", loggedInUsername);

            return "redirect:/auctions";
        } else {
            // Obsłuż przypadki, gdy użytkownik nie jest zalogowany
            return "redirect:/login"; // Przekieruj na stronę logowania lub obsłuż inaczej
        }
    }
    // Pobiera formularz do edycji danej aukcji.
    @GetMapping("/{auctionId}/edit")
    public String getEditForm(@PathVariable BigDecimal auctionId, Model model) {
        Auction auction = auctionService.getAuctionById(auctionId);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("auction", auction);
        model.addAttribute("categories", categories);
        return "editFormAuction";
    }
    // Obsługuje proces edycji danej aukcji.
    @PostMapping("/{auctionId}/edit")
    public String editAuction(@ModelAttribute Auction auction, @PathVariable BigDecimal auctionId) {
        if (auctionService.getAuctionById(auctionId) == null) {
            return "auctionNotFound";
        }
        auctionService.editAuction(auctionId, auction);
        return "redirect:/auctions";
    }
    // Obsługuje proces usunięcia danej aukcji.
    @GetMapping("/{auctionId}/delete")
    public String deleteAuction(@PathVariable BigDecimal auctionId) {
        auctionService.deleteAuction(auctionId);
        return "redirect:/auctions";
    }
}