package com.backend.simya.domain.house.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Category {
    LOVE("사랑"),
    FAMILY("가족"),
    PEOPLE("인간관계"),
    STRESS("스트레스"),
    HOBBY("취미"),
    CULTURE("문화생활");

    private String name;

    private Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 한글명을 파라미터로 받아서 enum 형식으로 반환
    public static Category nameOf(String name) {
        for (Category category : Category.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

}
