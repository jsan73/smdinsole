package com.kokasin.insole.device.model;

import com.kokasin.insole.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "단말 모델")
@Data
public class DeviceInfoModel extends BaseVO {
    @ApiModelProperty(value = "Sequence", hidden = true)
    private long deviceNo;

    @ApiModelProperty(value = "단말 ID", example = "A0001", required = true)
    private String deviceIMEI;

    @ApiModelProperty(value = "단말 번호", example = "0170023456", required = true)
    private String deviceNumber;

    @ApiModelProperty(value = "단말 별칭", example = "꼬까신", required = true)
    private String nickName;

    @ApiModelProperty(value = "보호자 할당 유무", example = "Y", required = true)
    private char isAssign;

    private int locCycle1;  // 위치전송 주기 주간
    private int locCycle2;  // 위치전송 주기 야간
    private int locCycle3;  // 위치 전송 주기 응급

    @ApiModelProperty(value = "단말기 배터리 정보 (%)", example = "70", required = false)
    private int battery;

    private String expDate; // 만료일
}
