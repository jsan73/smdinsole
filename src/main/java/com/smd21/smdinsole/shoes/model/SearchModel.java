package com.smd21.smdinsole.shoes.model;

import lombok.Data;

@Data
public class SearchModel {
    private long shoesNo;
    private long guardNo;
    private String startDt;
    private String endDt;
}
