package com.yang.blog.annotation;

import java.lang.annotation.*;

/**
 * 调用默认的update方法时,是否跳过更新某个字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldNotUpdate {

}
