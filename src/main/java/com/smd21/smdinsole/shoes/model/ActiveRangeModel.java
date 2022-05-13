package com.smd21.smdinsole.shoes.model;

import lombok.Data;

@Data
public class ActiveRangeModel {
    private long rangeNo;
    private String rangeName;
    private long shoesNo;
    private String rangeAddress;
    private double latitude;
    private double longitude;
    private int radius;
    private String regDate;
    private String upDate;
    private long regUser;
}
