package com.backend.simya.domain.house.repository;

import com.backend.simya.domain.house.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query(value = "select p from Topic p WHERE p.house.houseId = :houseId  ")
    List<Topic> findAllByHouseId(@Param("houseId") Long houseId);
}
