package com.smd21.smdinsole.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TokenUserModel {
    @ApiModelProperty(value = "보호자 no")
    private long   guardNo;

    @ApiModelProperty(value = "shose no")
    private List<Long>  shoseNoList;

    @ApiModelProperty(value = "보호자 폰 넘버")
    private String guardPhone;

    @ApiModelProperty(value = "마스터 보호자 No")
    private long masterGuardNo;

    @JsonIgnore
    private List<String> roles; // 보유권한
}
