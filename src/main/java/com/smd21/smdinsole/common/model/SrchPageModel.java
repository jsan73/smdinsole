package com.smd21.smdinsole.common.model;

import com.smd21.smdinsole.common.vo.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SrchPageModel extends PageVO {
    @ApiModelProperty(value = "paging용 limit 시작")
    private int pageStart;

    public int getPageStart() {
        return (getPageNum() - 1) * getPageSize();
    }
}
