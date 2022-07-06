package com.kokasin.insole.device.model;

import lombok.Data;

@Data
public class SearchModel {
    private long deviceNo;
    private long guardNo;
    private String startDt;
    private String endDt;
}
