package com.backend.simya.domain.house.repository;

import com.backend.simya.domain.house.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {
}
