package com.kokasin.insole.device.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "단말 위치 및 활동 반경 정보")
@Data
public class DashboardModel extends DeviceInfoModel {
    @ApiModelProperty(value = "현위치 위도", example = "37.5709535", required = true)
    private double lat;

    @ApiModelProperty(value = "현위치 경도", example = "126.9611967", required = true)
    private double lng;

    // 위치정보 획득수단 (GPS:4, CELL:5, SAVE-WIFI:6,없음:7)
    private int status;

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
