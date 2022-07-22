package com.kokasin.insole.device.model;

import lombok.Data;

@Data
public class RequestLocModel {
    private long reqNo;
    private long deviceNo;
    private long reqGuardNo;
    private String reqDate;
    private String rcvDate;
    private String rcvYn;
}
