package com.backend.simya.domain.review.entity;

import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.entity.BaseTimeEntity;
import com.backend.simya.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "REVIEW")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long reviewId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @Column(name = "rate")
    private int rate;

    @Column(name = "content")
    private String content;

    @Column(name = "activated")
    private boolean activated;

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void changeStatus(boolean activated) {
        this.activated = activated;
    }

    public Review updateReview(int rate, String content) {
        this.setRate(rate);
        this.setContent(content);
        return this;
    }
}
