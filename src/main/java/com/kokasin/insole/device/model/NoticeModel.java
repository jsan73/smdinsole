package com.kokasin.insole.device.model;

import com.kokasin.insole.common.vo.BaseVO;
import lombok.Data;

@Data
public class NoticeModel extends BaseVO {
    private long noticeNo;
    private long deviceNo;
    private long guardNo;
    private String notiCd;
    private String nextNotiTime;
    private long rsvSms;
    private String useYn;

}
