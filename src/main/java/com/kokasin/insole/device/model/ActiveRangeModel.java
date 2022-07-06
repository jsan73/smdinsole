package com.kokasin.insole.device.model;

import com.kokasin.insole.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "활동 반경 모델")
public class ActiveRangeModel extends BaseVO {
    @ApiModelProperty(value = "Sequence", hidden = true)
    private long rangeNo;

    @ApiModelProperty(value = "활동범위 이름", example = "범위 1", required = true)
    private String rangeName;

    @ApiModelProperty(value = "단말 No", example = "25", required = true)
    private long deviceNo;

    @ApiModelProperty(value = "활동범위 주소", example = "서울시 서대문구", required = true)
    private String rangeAddress;

    @ApiModelProperty(value = "위도", example = "37.5709535", required = true)
    private double lat;

    @ApiModelProperty(value = "경도", example = "126.9611967", required = true)
    private double lng;

    @ApiModelProperty(value = "활동범위 반경(m)", example = "1500", required = true)
    private int radius;
}
