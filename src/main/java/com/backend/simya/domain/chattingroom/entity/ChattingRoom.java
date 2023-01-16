package com.backend.simya.domain.chattingroom.entity;

import com.backend.simya.domain.chattingroom.dto.request.ChattingUpdateRequestDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.user.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "chatting_room")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoom extends BaseTimeEntity {

    @Id
    @Column(name = "chatting_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviewList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "chatting_room_name")
    private String chattingRoomName;

    @Column(name = "comment")
    private String comment;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "signboard_image_url")
    private String signboardImageUrl;

    @Column(name = "open")
    private boolean open;  // 오픈 상태인지

    @Column(name = "activated")
    private boolean activated;

    public void openChatting() {
        this.open = true;
    }

    public ChattingRoom update(ChattingUpdateRequestDto chattingUpdateRequestDto) {
        this.signboardImageUrl = chattingUpdateRequestDto.getSignboardImageUrl();
        this.chattingRoomName = chattingUpdateRequestDto.getChattingRoomName();
        this.comment = chattingUpdateRequestDto.getComment();

        return this;
    }

    public ChattingRoom delete() {
        this.activated = false;
        return this;
    }

    public void addReview(Review review) {
        reviewList.add(review);
        review.setChattingRoomToReview(this);
    }
}
