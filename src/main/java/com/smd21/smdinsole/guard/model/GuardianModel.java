package com.smd21.smdinsole.guard.model;

import com.smd21.smdinsole.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "단말 위치 정보 수신 모델")
@Data
public class GuardianModel extends BaseVO {
    @ApiModelProperty(value = "Sequence", hidden = true)
    private long guardNo;

    @ApiModelProperty(value = "보호자 명", example = "홍길동", required = true)
    private String guardName;

    @ApiModelProperty(value = "보호자 핸드폰 No", example = "01034567890", required = true)
    private String guardPhone;

    @ApiModelProperty(value = "마스터 보호자 No", example = "25", required = false)
    private long masterGuardNo;

    @ApiModelProperty(value = "보호자 비밀번호", example = "12345", required = false)
    private String guardPwd;

    @ApiModelProperty(value = "마케팅 동의 여부", example = "Y", required = false)
    private String maketingAgreeYn;

}
