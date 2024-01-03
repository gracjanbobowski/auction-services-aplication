package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;

    }

    @GetMapping
    public String getAllbids(Model model) {
        List<Bid> bids = bidService.getAllBids();
        model.addAttribute("bids", bids);
        return "bidsUser";
    }

    @GetMapping("/{bidId}")
    public String getBidDetails(@PathVariable Long bidId, Model model) {
        Bid bid = bidService.getBidById(BigDecimal.valueOf(bidId));
        model.addAttribute("bid", bid);
        return "bidDetails";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("bid", new Bid());
        return "createForm";
    }

    @PostMapping("/create")
    public String createbid(@ModelAttribute Bid bid) {
        bidService.createBids(bid);
        return "redirect:/bids";
    }

    @GetMapping("/{bidId}/edit")
    public String getEditForm(@PathVariable Long bidId, Model model) {
        Bid bid = bidService.getBidById(BigDecimal.valueOf(bidId));
        model.addAttribute("bid", bid);
        return "editForm";
    }

    @PostMapping("/{bidId}/edit")
    public String editBid(@PathVariable Long bidId, @ModelAttribute Bid editedBid) {
        bidService.editBid(BigDecimal.valueOf(bidId), editedBid);
        return "redirect:/bids";
    }

    @GetMapping("/{bidId}/delete")
    public String deleteBid(@PathVariable Long bidId) {
        bidService.deleteBid(BigDecimal.valueOf(bidId));
        return "redirect:/bids";
    }
}
