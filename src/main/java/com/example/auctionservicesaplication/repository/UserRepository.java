package com.example.auctionservicesaplication.repository;


import com.example.auctionservicesaplication.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, BigDecimal> {

    User save(User user);

    Optional<User> findById(BigDecimal userId);

    List<User> findAll();

    void deleteById(BigDecimal userId);

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Users u WHERE u.tr.rating >= :minRating")
    List<User> findUsersByTransactionRatingsGreaterOrEqual(@Param("minRating") int minRating);

    @Query("SELECT u FROM Users u WHERE u.transactionRatings IS NOT EMPTY ORDER BY SIZE(u.transactionRatings) DESC")
    List<User> findUsersWithMostTransactions(@Param("pageable") Pageable pageable);

    @Query("UPDATE Users u SET u.password = :newPassword WHERE u.email = :email")
    void updateUserPasswordByEmail(@Param("email") String email, @Param("newPassword") String newPassword);

    @Query("SELECT u FROM Users u WHERE u.tr.reviewerId = :reviewerId")
    List<User> findUsersByTransactionRatingsReviewerId(@Param("reviewerId") BigDecimal reviewerId);

    @Query("SELECT COUNT(u) FROM Users u WHERE u.transactionRatings IS EMPTY")
    Long countUsersByTransactionRatingsIsEmpty();
}
