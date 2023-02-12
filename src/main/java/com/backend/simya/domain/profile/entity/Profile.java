package com.backend.simya.domain.profile.entity;


import com.backend.simya.domain.favorite.entity.Favorite;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.dto.request.ProfileUpdateDto;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.user.entity.BaseTimeEntity;
import com.backend.simya.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "PROFILE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile extends BaseTimeEntity {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Favorite> favoriteList = new ArrayList<>();

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "comment", length = 100)
    private String comment;

    @Column(name = "picture")
    private String pictureUrl;

    @Column(name = "is_represent")
    private boolean isRepresent;

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<House> houseList = new ArrayList<>();

    public void selectMainProfile() {
        this.isRepresent = true;
    }

    public void setUserProfile(User user) {
        this.user = user;
    }

    public void autoSetMainProfile() {
        this.getUser().getProfileList().get(0).isRepresent = true;
    }

    public void cancelMainProfile() {
        this.isRepresent = false;
    }

    public Profile update(ProfileUpdateDto updateDto) {
        this.nickname = updateDto.getNickname();
        this.comment = updateDto.getComment();
        this.isRepresent = updateDto.isRepresent();
        return this;
    }

    public void addReview(Review review) {
        reviewList.add(review);
        review.setReviewersProfile(this);
    }

    public void addFavorite(Favorite favorite) {
        favoriteList.add(favorite);
        favorite.setProfile(this);
    }

    public void removeFavorite(Favorite favorite) {
        favoriteList.remove(favorite);
    }

    public void removeReview(Review review) {
        reviewList.remove(review);
    }
}
