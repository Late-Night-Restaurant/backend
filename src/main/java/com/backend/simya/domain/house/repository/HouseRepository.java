package com.backend.simya.domain.house.repository;

import com.backend.simya.domain.house.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {

    @Query(value = "select house " +
            "from House house " +
            "where house.profile.user.userId = :userId")
    List<House> findMyHousesByUserId(Long userId);
}
