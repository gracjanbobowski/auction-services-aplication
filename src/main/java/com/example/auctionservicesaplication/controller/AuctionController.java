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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

// The AuctionController handles all auction-related requests.
@Controller
@RequestMapping("/auctions")
public class AuctionController {

    // Service dependencies are injected here through the constructor.
    private final AuctionService auctionService;
    private final CategoryService categoryService;
    private final BidService bidService;
    private final UserService userService;

    @Autowired
    public AuctionController(AuctionService auctionService, CategoryService categoryService, BidService bidService, UserService userService) {
        this.auctionService = auctionService;
        this.categoryService = categoryService;
        this.bidService = bidService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllAuctions(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            User loggedInUser = userService.getUserByUsername(username);

            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("auctions", auctionService.getAllAuction());
            } else {
                model.addAttribute("auctions", auctionService.getAuctionsBySeller(loggedInUser));
            }
        }
        return "auctions";
    }

    // Fetches and displays details for a specific auction.
    @GetMapping("/{auctionId}")
    public String getAuctionDetails(@PathVariable Long auctionId, Model model) {
        model.addAttribute("auction", getAuctionOrThrow(auctionId));
        return "auctionDetails";
    }

    // Retrieves the form to create a new auction.
    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("auction", new Auction());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "createdForm";
    }

    // Handles the process of creating a new auction.
    @PostMapping("/create")
    public String createAuction(@ModelAttribute Auction auction, BindingResult bindingResult, Model model, Authentication authentication) {
        // Validate auction dates
        if (!validateAuctionDates(auction, bindingResult)) {
            // Return to form with error if validation fails
            model.addAttribute("categories", categoryService.getAllCategories());
            return "createForm";
        }
        // Check if the user is authenticated
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String loggedInUsername = userDetails.getUsername();

            // Retrieve the User object based on the username
            User loggedInUser = userService.getUserByUsername(loggedInUsername);

            // Create an auction, passing the User object
            auctionService.createAuction(auction, loggedInUser);

            // Add information about the logged-in user to the model
            model.addAttribute("loggedInUser", loggedInUsername);

            return "redirect:/auctions";
        } else {
            // Handle cases when the user is not authenticated
            return "redirect:/login"; // Redirect to the login page or handle otherwise
        }
    }

    // Retrieves the form for editing an existing auction.
    @GetMapping("/{auctionId}/edit")
    public String getEditForm(@PathVariable Long auctionId, Model model) {
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

    // Handles the process of updating an existing auction.
    @PostMapping("/{auctionId}/edit")
    public String editAuction(@ModelAttribute Auction auction, BindingResult bindingResult, @PathVariable Long auctionId, Model model) {
        // Validate auction dates
        if (!validateAuctionDates(auction, bindingResult)) {
            // Return to form with error if validation fails
            model.addAttribute("auction", auction);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "editFormAuction";
        }

        getAuctionOrThrow(auctionId); // Ensure the auction exists, or throw an exception
        auctionService.editAuction(auctionId, auction);
        return "redirect:/auctions";
    }

    // Handles the process of deleting an existing auction.
    @GetMapping("/{auctionId}/delete")
    public String deleteAuction(@PathVariable Long auctionId) {
        getAuctionOrThrow(auctionId); // Ensure the auction exists, or throw an exception
        auctionService.deleteAuction(auctionId);
        return "redirect:/auctions";
    }

    // Handles validation of the auction's start and end dates to ensure that the start date is before the end date
    private boolean validateAuctionDates(Auction auction, BindingResult bindingResult) {
        if (auction.getStartTime() != null && auction.getEndTime() != null && auction.getStartTime().isAfter(auction.getEndTime())) {
            bindingResult.rejectValue("startTime", "error.startTime", "Start time must be before end time.");
            return false;
        }
        return true;
    }

    // Handles exceptions when an auction is not found, displaying an error message.
    @ExceptionHandler(AuctionNotFoundException.class)
    public String handleAuctionNotFoundException(AuctionNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "auctionNotFound";
    }

    // Helper method to fetch an auction by its ID or throw an exception if not found.
    private Auction getAuctionOrThrow(Long auctionId) {
        return auctionService.getAuctionById(auctionId);
    }
}