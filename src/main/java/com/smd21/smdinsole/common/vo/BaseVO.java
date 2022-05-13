package com.smd21.smdinsole.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseVO {
	@ApiModelProperty(value="등록일")
	private String regDate;

	@ApiModelProperty(value="등록자")
	private long regUserNo;

	@ApiModelProperty(value="등록자명")
	private String regUserNm;

	@ApiModelProperty(value="수정일")
	private String updDate;
}
