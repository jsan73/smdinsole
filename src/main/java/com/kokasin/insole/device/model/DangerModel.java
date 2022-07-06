package com.kokasin.insole.device.model;

import com.kokasin.insole.common.vo.BaseVO;
import lombok.Data;

@Data
public class DangerModel extends BaseVO {
    private long deviceNo;
    private long locNo;
    private double lat;
    private double lng;
    private String dangerYn;
}
