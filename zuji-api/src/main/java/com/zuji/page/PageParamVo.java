package com.zuji.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页参数
 *
 * @author Ink足迹
 * @create 2020-06-10 19:26
 **/
@Getter
@Setter
public class PageParamVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "请选择第几页")
    @ApiModelProperty("第几页")
    private int pageNo = 1;

    @Min(value = 1, message = "请选择一页显示的行数")
    @ApiModelProperty("一页几行")
    private int pageSize = 5;
}
