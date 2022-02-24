package com.kyt.demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页请求基础类
 *
 * @author ZhaoLiang
 * @date 2017/11/22
 * @description
 */
@Data
public abstract class PageRequest {
    /**
     * 页码，从1开始
     */
    @ApiModelProperty(value = "页码，从1开始")
    private int pageNum;
    /**
     * 页面大小
     */
    @ApiModelProperty(value = "页面大小")
    private int pageSize;
}
