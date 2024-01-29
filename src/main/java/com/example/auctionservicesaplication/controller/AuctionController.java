package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.message.AuctionNotFoundException;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.BidService;
import com.example.auctionservicesaplication.service.CategoryService;
import com.example.auctionservicesaplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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
        model.addAttribute("auctions", auctionService.getAllAuction());
        return "auctions";
    }
    // Pobiera i wyświetla szczegóły danej aukcji.
    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable BigDecimal auctionId, Model model) {
        model.addAttribute("auction", getAuctionOrThrow(auctionId));
        return "auctionDetails";
    }
    // Pobiera formularz do utworzenia nowej aukcji.
    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("auction", new Auction());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "createdForm";
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
    // Obsługuje proces edycji danej aukcji.
    @PostMapping("/{auctionId}/edit")
    public String editAuction(@ModelAttribute Auction auction, @PathVariable BigDecimal auctionId) {
        getAuctionOrThrow(auctionId); // Throws if not found
        auctionService.editAuction(auctionId, auction);
        return "redirect:/auctions";
    }
    // Obsługuje proces usunięcia danej aukcji.
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

}