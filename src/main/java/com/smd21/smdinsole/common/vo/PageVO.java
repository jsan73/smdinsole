package com.smd21.smdinsole.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageVO extends BaseVO {

	@ApiModelProperty(example="페이지번호")
	private int pageNum;

	@ApiModelProperty(example="페이지사이즈")
    private int pageSize;

}
