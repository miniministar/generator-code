package com.kyt.demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页响应基础类
 *
 * @author ZhaoLiang
 * @date 2017/11/22
 * @description
 */
@Data
public abstract class PageResponse {
    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private long total;
    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private int pages;
}
