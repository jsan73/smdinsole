package com.kokasin.insole.common.sms.model;

import lombok.Data;

@Data
public class SMSModel {
    private String receiver;
    private String msg;
    private String rdate;
    private String rtime;
}
