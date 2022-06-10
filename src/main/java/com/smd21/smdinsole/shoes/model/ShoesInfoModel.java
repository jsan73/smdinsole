package com.smd21.smdinsole.shoes.model;

import com.smd21.smdinsole.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "단말 모델")
@Data
public class ShoesInfoModel extends BaseVO {
    @ApiModelProperty(value = "Sequence", hidden = true)
    private long shoesNo;

    @ApiModelProperty(value = "단말 ID", example = "A0001", required = true)
    private String shoesId;

    @ApiModelProperty(value = "단말 번호", example = "0170023456", required = true)
    private String shoesNumber;

    @ApiModelProperty(value = "단말 별칭", example = "꼬까신", required = true)
    private String nickName;

    @ApiModelProperty(value = "보호자 할당 유무", example = "Y", required = true)
    private char isAssign;

    @ApiModelProperty(value = "단말기 배터리 정보 (%)", example = "70", required = false)
    private int battery;
}
