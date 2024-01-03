package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//AuctionController: Obsługuje żądania związane z aukcjami.
@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    AuctionService auctionService;

    @GetMapping("")
    public List<Auction> getAll() {
        return auctionService.getAll();
    }

    @GetMapping("/{id}")
    public Auction getById(@PathVariable("id") int id) {
        return auctionService.getById(id);
    }

//    @PostMapping("")
//    public int add(@RequestBody List<Auction> auctions) {
//        return auctionService.save(auctions);
//    }
}
