package com.smd21.smdinsole.shoes.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "단말 위치 및 활동 반경 정보")
@Data
public class DashboardModel extends ShoesInfoModel {
    @ApiModelProperty(value = "위도", example = "37.5709535", required = true)
    private double lat;

    @ApiModelProperty(value = "경도", example = "126.9611967", required = true)
    private double lng;

    @ApiModelProperty(value = "활동범위 이름", example = "범위 1", required = true)
    private String rangeName;

    @ApiModelProperty(value = "활동 정보 위도", example = "37.5709535", required = true)
    private double activeLat;

    @ApiModelProperty(value = "활동 정보 경도", example = "126.9611967", required = true)
    private double activeLng;

    @ApiModelProperty(value = "활동범위 주소", example = "서울시 서대문구", required = true)
    private String rangeAddress;

    @ApiModelProperty(value = "활동범위 반경(m)", example = "1500", required = true)
    private int radius;
}
