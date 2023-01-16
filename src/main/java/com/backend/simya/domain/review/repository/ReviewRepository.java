package com.backend.simya.domain.review.repository;

import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

     @Query(value = "select review " +
             "from Review review " +
             "where review.profile.user.userId = :userId")
     List<Review> findReviewsByUserId(Long userId);

     @Query(value = "select review " +
             "from Review review " +
             "where review.profile.profileId = :profileId")
     List<Review> findReviewsByProfileId(Long profileId);

     List<Review> findReviewsByHouse(House house);
}
