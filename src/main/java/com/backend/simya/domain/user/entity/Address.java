package com.backend.simya.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Address {

    @Column(name = "region_1depth_name")
    private String region1depthName;

    @Column(name = "region_2depth_name")
    private String region2depthName;

    @Column(name = "region_3depth_name")
    private String region3depthName;

}
