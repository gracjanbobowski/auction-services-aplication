package com.example.auctionservicesaplication.repository;


import com.example.auctionservicesaplication.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, BigDecimal> {

    Category save(Category category);

    Optional<Category> findById(BigDecimal categoryId);

    List<Category> findAll();

    void deleteById(BigDecimal categoryId);

    @Query("SELECT c FROM Categories c WHERE c.description LIKE concat('%', :keyword, '%')")
    List<Category> findCategoryByDescriptionsContains(@Param("keyword") String keyword);

    @Query("SELECT COUNT(a) FROM Auctions a WHERE a.category.id = :categoryId  ")
    Long countAuctionsInCategory(@Param("categoryId") BigDecimal categoryId);

    @Query("UPDATE Categories c SET c.description = :newDescription WHERE c.id = :categoryId")
    void updateCategoryDescription(@Param("categoryId") BigDecimal categoryId, @Param("newDescription") String newDescription);
//    @Query("SELECT c FROM Category c WHERE c.id IN SELECT(a.category.id FROM Auction a GROUP BY a.category HAVING AVG(a.rating) > minAverageRating)")
//    List<Category> findCategoriesWithAverageRatingAbove(@Param("minAverageRating") double minAverageRating);
}
