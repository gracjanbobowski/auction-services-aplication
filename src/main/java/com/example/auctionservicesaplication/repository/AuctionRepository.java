package com.example.auctionservicesaplication.repository;


import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, BigDecimal> {

    Auction save(Auction auction);

    void deleteById(BigDecimal AuctionId);

    Optional<Auction> findById(BigDecimal auctionId);

    List<Auction> findAll();

    @Query("SELECT a FROM Auctions a WHERE LOWER(a.title) LIKE LOWER(concat('%', :keyword, '%'))")
    List<Auction> findAuctionByTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT a FROM Auctions a WHERE a.startTime > :startTime")
    List<Auction> findAuctionByStartTimeAfter(@Param("startTime") LocalDateTime startTime);

    @Query("SELECT a FROM Auctions a WHERE a.endTime < :endTime")
    List<Auction> findAuctionByEndTimeBefore(@Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Auctions a WHERE a.category = :category")
    List<Auction> findAuctionByCategory(@Param("category") Category category);

    @Query("SELECT a FROM Auctions a ORDER BY a.startTime DESC")
    List<Auction> findTop5AuctionByOrderByStartTimeDesc();

    @Query("SELECT a FROM Auctions a WHERE a.seller.id = :sellerId")
    List<Auction> findAuctionBySellerId(@Param("sellerId") BigDecimal sellerId);

}
