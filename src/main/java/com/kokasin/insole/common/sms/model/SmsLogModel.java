package com.kokasin.insole.common.sms.model;

import lombok.Data;

@Data
public class SmsLogModel {
    private long smsNo;
    private String receiver;
    private long msgId;
    private String sendMsg;
    private int resultCode;
    private String resultMsg;
    private int successCnt;
    private int errorCnt;
    private String sendTo;
    private String regDate;
    private int resCode;
}
