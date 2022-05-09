package com.smd21.smdinsole.api.model;

import lombok.Data;

@Data
public class ShoesInfoModel {
    private long shoesNo;
    private String shoesNumber;
    private char isAssign;
    private int battery;
    private String regDate;
    private String updDate;
}
