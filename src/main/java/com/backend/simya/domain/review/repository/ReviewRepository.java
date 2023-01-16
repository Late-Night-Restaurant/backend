package com.backend.simya.domain.review.repository;

import com.backend.simya.domain.chattingroom.entity.ChattingRoom;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
     List<Review> findReviewsByProfile(Profile profile);
     List<Review> findReviewsByChattingRoom(ChattingRoom chattingRoom);
}
