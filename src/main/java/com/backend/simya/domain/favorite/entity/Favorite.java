package com.backend.simya.domain.favorite.entity;

import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "FAVORITE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite extends BaseTimeEntity {

    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long favoriteId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "house_id")
    @JsonBackReference
    private House house;

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
