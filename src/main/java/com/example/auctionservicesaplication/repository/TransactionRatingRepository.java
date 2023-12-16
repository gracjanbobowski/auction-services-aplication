package com.example.auctionservicesaplication.repository;


import com.example.auctionservicesaplication.model.TransactionRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRatingRepository extends JpaRepository<TransactionRating, BigDecimal> {

    TransactionRating save(TransactionRating transactionRating);

    Optional<TransactionRating> findById(BigDecimal transactionRatingId);

    List<TransactionRating> findAll();

    void deleteById(BigDecimal transactionRatingId);

    @Query("Select tr FROM TransactionRatings tr WHERE tr.rating >= : minRating")
    List<TransactionRating> findByRatingGreaterOrEqual(@Param("minRating") int minRating);

    @Query("SELECT tr FROM TransactionRatings tr WHERE tr.user.id = :reviewerId")
    List<TransactionRating> findAllByReviewerId(@Param("reviewerId") BigDecimal reviewerId);

    @Query("SELECT COUNT(tr) FROM TransactionRatings tr WHERE tr.user.id = :userId")
    Long countRatingsForUser(@Param("userId") BigDecimal userId);

    @Query("DELETE FROM TransactionRatings tr WHERE tr.timestamp < :timestamp")
    void deleteByTimestampBefore(@Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT AVG(tr.rating) FROM TransactionRatings tr WHERE tr.user.id = :userId")
    Double findAverageRatingForUser(@Param("userId") BigDecimal userId);
}
