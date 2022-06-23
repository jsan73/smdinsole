package com.smd21.smdinsole.shoes.model;

import com.smd21.smdinsole.common.vo.BaseVO;
import lombok.Data;

@Data
public class NoticeModel extends BaseVO {
    private long shoesNo;
    private long guardNo;
    private String notiCd;
    private String nextNotiTime;
    private long rsvSms;
    private String useYn;

}
