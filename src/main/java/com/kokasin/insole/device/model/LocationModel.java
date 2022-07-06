package com.kokasin.insole.device.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "단말 위치 정보 수신 모델")
@Data
public class LocationModel {
    @ApiModelProperty(value = "Sequence", hidden = true)
    private long locationNo;

    @ApiModelProperty(value = "단말 No",  hidden = true,  example = "25", required = false)
    private long deviceNo;

//    @ApiModelProperty(value = "단말 번호", example = "01112345678", required = true)
//    private String deviceNumber;

    @ApiModelProperty(value = "위도", example = "37.5709535", required = true)
    private double lat;

    @ApiModelProperty(value = "경도", example = "126.9611967", required = true)
    private double lng;

    @ApiModelProperty(value = "위치정보 획득수단 (GPS:4, CELL:5, SAVE-WIFI:6,없음:7)", example = "4", required = true)
    private int status;

    @ApiModelProperty(value = "배터리 정보(0~3)", example = "0", required = true)
    private int battery;

    @ApiModelProperty(value = "수신일자", required = true)
    private String reportDate;
}
