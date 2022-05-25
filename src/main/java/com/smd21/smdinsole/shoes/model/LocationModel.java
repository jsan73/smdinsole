package com.smd21.smdinsole.shoes.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "단말 위치 정보 수신 모델")
@Data
public class LocationModel {
    @ApiModelProperty(value = "Sequence", hidden = true)
    private long locationNo;

    @ApiModelProperty(value = "단말 No",  hidden = true,  example = "25", required = false)
    private long shoesNo;

    @ApiModelProperty(value = "단말 번호", example = "01112345678", required = true)
    private String shoesNumber;

    @ApiModelProperty(value = "위도", example = "37.5709535", required = true)
    private double latitude;

    @ApiModelProperty(value = "경도", example = "126.9611967", required = true)
    private double longitude;

    @ApiModelProperty(value = "배터리 정보", example = "80", required = true)
    private int status;

    @ApiModelProperty(value = "수신일자", required = true)
    private String reportDate;
}
