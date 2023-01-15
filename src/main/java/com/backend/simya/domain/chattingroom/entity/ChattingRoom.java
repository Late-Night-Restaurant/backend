package com.backend.simya.domain.chattingroom.entity;

import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "chatting_room")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoom extends BaseTimeEntity {

    @Id
    @Column(name = "chatting_room_id")
    private Long chattingRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_id")
    private Category categoryId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "signboard_image_url")
    private String signboardImageUrl;

    @Column(name = "activated")
    private boolean activated;
}
