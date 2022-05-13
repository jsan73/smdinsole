package com.smd21.smdinsole.shoes.model;

import lombok.Data;

@Data
public class LocationModel {
    private long locationNo;
    private long shoesNo;
    private double latitude;
    private double longitude;
    private int status;
    private String reportDate;
}
