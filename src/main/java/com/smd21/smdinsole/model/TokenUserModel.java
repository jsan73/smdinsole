package com.smd21.smdinsole.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TokenUserModel {
    @ApiModelProperty(value = "user no")
    private long   userNo;

    @ApiModelProperty(value = "shose no")
    private long   shoseNo;

    @ApiModelProperty(value = "보호자 no")
    private long   guardNo;

    private String guardPhone;

    @ApiModelProperty(value = "마스터 보호자 No")
    private long masterGuardNo;

    @JsonIgnore
    private List<String> roles; // 보유권한
}
