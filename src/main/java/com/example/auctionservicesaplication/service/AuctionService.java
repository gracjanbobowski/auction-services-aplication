package com.example.auctionservicesaplication.service;


import com.example.auctionservicesaplication.message.AuctionNotFoundException;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//AuctionService: Logika biznesowa związana z aukcjami.
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> getAllAuction() {
        return auctionRepository.findAll();
    }

    public Auction getAuctionById(BigDecimal auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));
    }

    public Auction createAuction(Auction auction) {
        // Upewnij się, że kategoria nie jest null
        if (auction.getCategory() == null) {
            // Obsłuż błąd, np. rzucenie wyjątku lub zwrócenie odpowiedzi błędu
            throw new IllegalArgumentException("Kategoria aukcji nie może być pusta.");
        }

        return auctionRepository.save(auction);
    }

    public void editAuction(BigDecimal auctionId, Auction editedAuction) {
        Auction existingAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));

        // Only update if it's not null in the editedAuction
        if (editedAuction.getCategory() != null) {
            existingAuction.setCategory(editedAuction.getCategory());
        }
        if (editedAuction.getTitle() != null) {
            existingAuction.setTitle(editedAuction.getTitle());
        }
        if (editedAuction.getDescription() != null) {
            existingAuction.setDescription(editedAuction.getDescription());
        }
        if (editedAuction.getStartingPrice() != null) {
            existingAuction.setStartingPrice(editedAuction.getStartingPrice());
        }
        if (editedAuction.getCurrentPrice() != null) {
            existingAuction.setCurrentPrice(editedAuction.getCurrentPrice());
        }
        if (editedAuction.getStartTime() != null) {
            existingAuction.setStartTime(editedAuction.getStartTime());
        }
        if (editedAuction.getEndTime() != null) {
            existingAuction.setEndTime(editedAuction.getEndTime());
        }

        auctionRepository.save(existingAuction);
    }


    public void deleteAuction(BigDecimal auctionId) {
        Auction auctionToDelete = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));

        auctionRepository.delete(auctionToDelete);
    }

    public void addBidToAuction(Auction auction, Bid newBid) {
        Set<Bid> bids = auction.getBids();
        if (bids == null) {
            bids = new HashSet<>();
            auction.setBids(bids);
        }
        bids.add(newBid);
        auctionRepository.save(auction);
    }
}

