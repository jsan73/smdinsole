package com.smd21.smdinsole.shoes.model;

import com.smd21.smdinsole.common.vo.BaseVO;
import lombok.Data;

@Data
public class DangerModel extends BaseVO {
    private long shoesNo;
    private long locNo;
    private double lat;
    private double lon;
    private String dangerYn;
}
