package com.backend.simya.domain.house.repository;

import com.backend.simya.domain.house.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
