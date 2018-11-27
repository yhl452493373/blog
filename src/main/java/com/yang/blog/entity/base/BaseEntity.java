package com.yang.blog.entity.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yang.blog.annotation.FieldUpdate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;

public abstract class BaseEntity<Entity> implements BaseConstant {
    /**
     * 记录的id
     */
    private String id;

    /**
     * 记录创建时间
     */
    private LocalDateTime createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @SuppressWarnings("unchecked")
    public void update(Entity entity) {
        Class clazz = entity.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getModifiers() == Modifier.PRIVATE) {
                FieldUpdate fieldUpdate = field.getAnnotation(FieldUpdate.class);
                //设置了调用update方法时跳过,则不更新
                if (fieldUpdate != null && fieldUpdate.exclude())
                    continue;
                try {
                    String fieldName = field.getName();
                    fieldName = StringUtils.capitalize(fieldName);
                    String get = "get" + fieldName;
                    String set = "set" + fieldName;
                    Method getMethod = clazz.getDeclaredMethod(get);
                    Method setMethod = clazz.getDeclaredMethod(set, field.getType());
                    setMethod.invoke(this, getMethod.invoke(entity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
