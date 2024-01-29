package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.message.BidNotFoundException;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final EmailService emailService;

    @Autowired
    public BidService(BidRepository bidRepository, EmailService emailService) {
        this.bidRepository = bidRepository;
        this.emailService = emailService;
    }

    public void createBid(Bid bid) {
        bidRepository.save(bid);
    }

    public List<Bid> getBidsByAuction(Auction auction) {
        return bidRepository.findByAuction(auction);
    }

    public void sendBidConfirmation(String to, String username, String auctionTitle) {
        String subject = "Potwierdzenie licytacji";
        String body = "Cześć " + username + "!\n" +
                "Dziękujemy za złożenie oferty licytacyjnej na aukcję o tytule: " + auctionTitle + ".\n" +
                "Życzymy powodzenia!";
        emailService.sendEmail(to, subject, body);
    }
}
