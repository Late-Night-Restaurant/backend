package com.backend.simya.domain.chattingroom.repository;

import com.backend.simya.domain.chattingroom.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
