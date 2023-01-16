package com.backend.simya.domain.house.entity;

import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.user.entity.BaseTimeEntity;
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

@Entity
@Table(name = "house")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class House extends BaseTimeEntity {

    @Id
    @Column(name = "house_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "house", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviewList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "house_name")
    private String houseName;

    @Column(name = "comment")
    private String comment;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "signboard_image_url")
    private String signboardImageUrl;

    @Column(name = "open")
    private boolean open;  // 오픈 상태인지

    @Column(name = "activated")
    private boolean activated;

    public void openHouse() {
        this.open = true;
    }

    public House update(HouseUpdateRequestDto houseUpdateRequestDto) {
        this.signboardImageUrl = houseUpdateRequestDto.getSignboardImageUrl();
        this.houseName = houseUpdateRequestDto.getHouseName();
        this.comment = houseUpdateRequestDto.getComment();

        return this;
    }

    public House delete() {
        this.activated = false;
        return this;
    }

    public void addReview(Review review) {
        reviewList.add(review);
        review.setReviewedHouse(this);
    }
}
