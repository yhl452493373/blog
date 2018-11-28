package com.yang.blog.entity.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yang.blog.annotation.FieldUpdate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public interface Base<Entity> {
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

    /**
     * 默认的update方法
     *
     * @param ignoreNull 是否忽略新实体值为null的字段
     * @param entity     新实体对象
     * @return 是否将当前实体更新为传入的实体成功
     */
    default boolean update(boolean ignoreNull, Entity entity) {
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
                    executeSetter(entity, clazz, field, fieldName, ignoreNull);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 默认的update方法
     *
     * @param ignoreNull 是否忽略新实体值为null的字段
     * @param entity     新的实体对象
     * @param fieldNames 要更新的属性名
     * @return 是否将当前实体更新为传入的实体成功
     */
    default boolean update(boolean ignoreNull, Entity entity, String... fieldNames) {
        Class clazz = entity.getClass();
        for (String fieldName : fieldNames) {
            try {
                Field field = clazz.getField(fieldName);
                if (field.getModifiers() == Modifier.PRIVATE) {
                    FieldUpdate fieldUpdate = field.getAnnotation(FieldUpdate.class);
                    //设置了调用update方法时跳过,则不更新
                    if (fieldUpdate != null && fieldUpdate.exclude())
                        continue;
                    executeSetter(entity, clazz, field, fieldName, ignoreNull);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    default void executeSetter(Entity entity, Class clazz, Field field, String fieldName, boolean ignoreNull) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        fieldName = StringUtils.capitalize(fieldName);
        String get = "get" + fieldName;
        String set = "set" + fieldName;
        Method getMethod = clazz.getDeclaredMethod(get);
        Method setMethod = clazz.getDeclaredMethod(set, field.getType());
        Object getResult = getMethod.invoke(entity);
        if (getResult != null || ignoreNull)
            setMethod.invoke(this, getResult);
    }
}
