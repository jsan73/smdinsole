package com.smd21.smdinsole.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseVO {
	@ApiModelProperty(value="등록일", hidden = true)
	private String regDate;

	@ApiModelProperty(value="등록자", hidden = true)
	private long regUser;

	@ApiModelProperty(value="마스터 보호자", hidden = true)
	private long guardNo;

	@ApiModelProperty(value="수정일", hidden = true)
	private String updDate;
}
