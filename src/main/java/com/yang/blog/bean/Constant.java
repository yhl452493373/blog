package com.yang.blog.bean;

import org.springframework.ui.ModelMap;

@SuppressWarnings("unused")
public class Constant {
    /**
     * 以下为公用错误代码常量
     */
    public static final Integer CODE_OK = 0;//正常
    public static final Integer CODE_ID_EMPTY = 10;//ID为空
    public static final Integer CODE_CONTENT_EMPTY = 20;//内容为空
    public static final Integer CODE_NO_PERMISSION = 30;//无权限
    public static final Integer CODE_NOT_EXIST = 40;//不存在

    private static final ModelMap codeMap = new ModelMap();

    public static ModelMap getCodeMap() {
        codeMap.addAttribute("CODE_OK", CODE_OK);
        codeMap.addAttribute("CODE_ID_EMPTY", CODE_ID_EMPTY);
        codeMap.addAttribute("CODE_CONTENT_EMPTY", CODE_CONTENT_EMPTY);
        codeMap.addAttribute("CODE_NO_PERMISSION", CODE_NO_PERMISSION);
        codeMap.addAttribute("CODE_NOT_EXIST", CODE_NOT_EXIST);
        return codeMap;
    }
}
