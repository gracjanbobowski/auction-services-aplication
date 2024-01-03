package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.message.BidNotFoundException;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public Bid getBidById(BigDecimal bidId) {
        return bidRepository.findById(bidId)
                .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + bidId));
    }

    public void createBids(Bid bid) {
        bidRepository.save(bid);
    }
    public void editBid(BigDecimal bidId, Bid editedBid) {
        Bid existingBid = bidRepository.findById(bidId)
                .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + bidId));

        existingBid.setId(editedBid.getId());
        existingBid.setBidTime(editedBid.getBidTime());
        existingBid.setBidAmount(editedBid.getBidAmount());
        bidRepository.save(existingBid);
    }

    public void deleteBid(BigDecimal bidId) {
        Bid bidToDelete = bidRepository.findById(bidId)
                .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + bidId));

        bidRepository.delete(bidToDelete);
    }
}
