package com.backend.simya.domain.favorite.repository;

import com.backend.simya.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query(value = "select favorite " +
            "from Favorite favorite " +
            "where favorite.profile.user.userId = :userId")
    List<Favorite> findFavoriteListByUserId(Long userId);

    @Query(value = "select favorite " +
            "from Favorite favorite " +
            "where favorite.profile.profileId = :profileId")
    List<Favorite> findFavoriteListByProfileId(Long profileId);

    @Query(value = "select favorite " +
            "from Favorite favorite " +
            "where favorite.house.houseId = :houseId")
    List<Favorite> findFavoriteListByHouseId(Long houseId);
}
