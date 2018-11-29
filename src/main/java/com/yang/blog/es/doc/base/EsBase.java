package com.yang.blog.es.doc.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yang.blog.annotation.FieldUpdate;
import com.yang.blog.entity.base.Base;
import com.yang.blog.entity.base.BaseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"Duplicates", "unchecked"})
public interface EsBase<Doc> extends Base<Doc> {
    //从传统数据库的对象获取数据并set到es数据路的对象
    default Doc update(Boolean ignoreNull, BaseEntity base) {
        Class superClass = this.getClass().getSuperclass();
        Field[] superFields = superClass.getDeclaredFields();
        Field[] fields = this.getClass().getDeclaredFields();
        Map<String, Field[]> maps = new HashMap<>();
        maps.put("super", superFields);
        maps.put("this", fields);
        maps.keySet().forEach(key -> {
            Field[] tempField = maps.get(key);
            for (Field field : tempField) {
                //如果是父类,只有protected的方法执行
                if ((key.equalsIgnoreCase("super") && field.getModifiers() == Modifier.PROTECTED) ||
                        (key.equalsIgnoreCase("this") && field.getModifiers() == Modifier.PRIVATE)) {
                    FieldUpdate fieldUpdate = field.getAnnotation(FieldUpdate.class);
                    //设置了调用update方法时跳过,则不更新
                    if (fieldUpdate != null && fieldUpdate.exclude())
                        continue;
                    try {
                        String fieldName = field.getName();
                        executeGetterAndSetter(key.equalsIgnoreCase("super"), base, field, fieldName, ignoreNull);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return (Doc) this;
    }

    default void executeGetterAndSetter(Boolean isSuper, BaseEntity base, Field field, String fieldName, boolean ignoreNull) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class getterClazz;
        if (isSuper)
            getterClazz = base.getClass().getSuperclass();
        else
            getterClazz = base.getClass();
        Class setterClazz;
        if (isSuper)
            setterClazz = this.getClass().getSuperclass();
        else
            setterClazz = this.getClass();
        fieldName = StringUtils.capitalize(fieldName);
        String get = "get" + fieldName;
        String set = "set" + fieldName;
        Method getMethod = getterClazz.getDeclaredMethod(get);
        Method setMethod = setterClazz.getDeclaredMethod(set, field.getType());
        Object getResult = getMethod.invoke(base);
        if (getResult != null || !ignoreNull)
            setMethod.invoke(this, getResult);
    }
}
