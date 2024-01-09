package com.example.auctionservicesaplication.service;


import com.example.auctionservicesaplication.message.AuctionNotFoundException;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


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
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found whit ID: " + auctionId));
    }

    public void createAuction(Auction auction) {
        auctionRepository.save(auction);
    }
    public void editAuction(BigDecimal auctionId, Auction editedAuction) {
        Auction existingAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found with ID: " + auctionId));

        existingAuction.setCategory(editedAuction.getCategory());
        existingAuction.setId(editedAuction.getId());
        existingAuction.setTitle(editedAuction.getTitle());
        existingAuction.setDescription(editedAuction.getDescription());
        existingAuction.setStartingPrice(editedAuction.getStartingPrice());
        existingAuction.setCurrentPrice(editedAuction.getCurrentPrice());
        existingAuction.setStartTime(editedAuction.getStartTime());
        existingAuction.setEndTime(editedAuction.getEndTime());
        auctionRepository.save(editedAuction);
    }

    public void deleteAuction(BigDecimal auctionId) {
        Auction auctionToDelete = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found with ID: " + auctionId));

                auctionRepository.delete(auctionToDelete);
    }

}

