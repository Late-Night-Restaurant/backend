package com.backend.simya.domain.house.dto.request;

import com.backend.simya.domain.house.entity.Category;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.entity.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseRequestDto {

    private Long profileId;  // 방장 프로필_id
    private String category;
    private String signboardImageUrl;
    private String houseName;
    private String comment;

    public House toEntity(Profile profile) {
        return House.builder()
                .profile(profile)
                .category(Category.nameOf(category))
                .houseName(houseName)
                .comment(comment)
                .capacity(0)
                .signboardImageUrl(signboardImageUrl)
                .open(false)
                .activated(true)
                .build();
    }

}
