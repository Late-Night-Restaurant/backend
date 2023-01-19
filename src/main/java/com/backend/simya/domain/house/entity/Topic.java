package com.backend.simya.domain.house.entity;

import com.backend.simya.domain.user.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "topic")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends BaseTimeEntity {

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @Column(name = "is_today_topic")
    private boolean isTodayTopic;

    public boolean isTodayTopic() {
        return this.isTodayTopic;
    }

    public void setHouseToRegisterTopic(House house) {
        this.house = house;
    }
}
