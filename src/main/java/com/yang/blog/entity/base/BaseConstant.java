package com.yang.blog.entity.base;

@SuppressWarnings("unused")
public interface BaseConstant {
    /**
     * 临时状态
     */
    Integer TEMP = 2;
    /**
     * 可用状态
     */
    Integer AVAILABLE = 1;
    /**
     * 禁用状态
     */
    Integer BLOCK = 0;
    /**
     * 删除状态
     */
    Integer DELETE = -1;
}
