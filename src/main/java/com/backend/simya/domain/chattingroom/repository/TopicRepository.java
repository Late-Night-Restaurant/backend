package com.backend.simya.domain.chattingroom.repository;

import com.backend.simya.domain.chattingroom.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
