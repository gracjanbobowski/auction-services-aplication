package com.example.auctionservicesaplication.repository;


import com.example.auctionservicesaplication.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, BigDecimal> {

    Bid save(Bid bid);

    Optional<Bid> findById(BigDecimal id);

    List<Bid> findAll();

    void deleteById(BigDecimal id);

    @Query("SELECT b FROM Bids b WHERE b.auction.id = :auctionId")
    List<Bid> findByAuctionId(@Param("auctionId") BigDecimal auctionId);

    @Query("SELECT b FROM Bids b WHERE b.bidder.id = :bidderId")
    List<Bid> findAllByBidderId(@Param("bidderId") BigDecimal bidderId);

    @Query("SELECT b FROM Bids b WHERE b.auction.id = :auctionId AND b.bidAmount = (SELECT MAX(b2.bidAmount) FROM Bids b2 WHERE b2.auction.id = :auctionId)")
    Bid getHighestBidForAuction(BigDecimal auctionId);

    @Query("DELETE FROM Bids b WHERE b.auction.id = :auctionId")
    void deleteByAuctionId(BigDecimal auctionId);
}
