package com.kokasin.insole.guard.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "로그인 모델")
@Data
public class GuardLoginModel {
    @ApiModelProperty(value = "폰 번호", example = "01052490965", required = true)
    private String guardPhone;

    @ApiModelProperty(value = "비밀번호", example = "1234", required = true)
    private String guardPwd;

    private String autoLogin;
}
