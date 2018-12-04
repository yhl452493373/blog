package com.yang.blog.entity.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yang.blog.annotation.FieldNotUpdate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 基类的公共接口.给每个实现此方法的对象赋予公共的update方法.
 *
 * @param <Entity> 实体对象
 */
@SuppressWarnings({"unused", "Duplicates", "unchecked"})
public interface Base<Entity> {
    String getPrefix = "get";
    String setPrefix = "set";

    /**
     * 获取所有的get/set字段.
     *
     * @param clazz     实体类,为空则为调用的实体
     * @param fieldList 字段列表
     * @return 字段数组
     */
    default Field[] getAllFields(Class clazz, List<Field> fieldList) {
        if (fieldList == null)
            fieldList = new ArrayList<>();
        if (clazz == null)
            clazz = this.getClass();
        //取当前类所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //只处理public,protected,private类别的字段
            if (field.getModifiers() == Modifier.PUBLIC || field.getModifiers() == Modifier.PROTECTED || field.getModifiers() == Modifier.PRIVATE) {
                String fieldName = StringUtils.capitalize(field.getName());
                String getMethodName = getPrefix + fieldName;
                String setMethodName = setPrefix + fieldName;
                try {
                    //既有get,又有set,则为可以自动取值更新的字段,放到列表
                    if (this.getClass().getMethod(getMethodName) != null && this.getClass().getMethod(setMethodName, field.getType()) != null) {
                        fieldList.add(field);
                    }
                } catch (NoSuchMethodException ignored) {
                    //如果get或set其中之一不存在,则会出异常,但是这个异常不影响,故不处理
                }
            }
        }
        clazz = clazz.getSuperclass();
        //如果有父类,则再从父类获取字段
        if (clazz != null)
            getAllFields(clazz, fieldList);
        return fieldList.toArray(new Field[]{});
    }

    /**
     * 根据名字获取字段
     *
     * @param clazz     实体类
     * @param fieldName 字段名
     * @return 获取到的字段, 没有则为null
     */
    default Field getField(Class clazz, String fieldName) {
        if (clazz == null)
            clazz = this.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            String getMethodName = getPrefix + StringUtils.capitalize(field.getName());
            String setMethodName = setPrefix + StringUtils.capitalize(field.getName());
            try {
                //既有get,又有set,则为可以自动取值更新的字段,返回
                if (this.getClass().getMethod(getMethodName) != null && this.getClass().getMethod(setMethodName, field.getType()) != null) {
                    return field;
                }
            } catch (NoSuchMethodException ignored) {
                //如果get或set其中之一不存在,则会出异常,但是这个异常不影响,故不处理
            }
        } catch (NoSuchFieldException e) {
            //当前类中没有,则从父类找
            clazz = clazz.getSuperclass();
            if (clazz != null)
                getField(clazz, fieldName);
            //父类不存在,又报异常,则说明没有这个字段
        }
        return null;
    }

    /**
     * 默认的update方法.注意,默认使用的反射,字段过多时,此法效率较低,建议按照需要在每个实体中重写该方法
     *
     * @param ignoreNull 是否忽略新实体值为null的字段
     * @param entity     新实体对象
     * @return 是否将当前实体更新为传入的实体成功
     */
    default Entity update(boolean ignoreNull, Entity entity) {
        Field[] fields = getAllFields(null, null);
        for (Field field : fields) {
            FieldNotUpdate fieldNotUpdate = field.getAnnotation(FieldNotUpdate.class);
            //设置了调用update方法时跳过,则不更新
            if (fieldNotUpdate != null)
                continue;
            try {
                executeGetterAndSetter(entity, this.getClass(), field, ignoreNull);
            } catch (Exception e) {
                e.printStackTrace();
                return (Entity) this;
            }
        }
        return (Entity) this;
    }

    /**
     * 默认的update方法.注意,默认使用的反射,字段过多时,此法效率较低,建议按照需要在每个实体中重写该方法
     *
     * @param ignoreNull 是否忽略新实体值为null的字段
     * @param entity     新的实体对象
     * @param fieldNames 要更新的属性名
     * @return 是否将当前实体更新为传入的实体成功
     */
    default Entity update(boolean ignoreNull, Entity entity, String... fieldNames) {
        Class clazz = this.getClass();
        for (String fieldName : fieldNames) {
            try {
                Field field = getField(clazz, fieldName);
                if (field != null) {
                    FieldNotUpdate fieldNotUpdate = field.getAnnotation(FieldNotUpdate.class);
                    //设置了调用update方法时跳过,则不更新
                    if (fieldNotUpdate != null)
                        continue;
                    executeGetterAndSetter(entity, clazz, field, ignoreNull);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return (Entity) this;
            }
        }
        return (Entity) this;
    }

    /**
     * 执行对象中的get方法,把值通过当前对象的set方法更新到当前对象
     *
     * @param entity     新的对象实例
     * @param clazz      新的对象的类
     * @param field      需要执行的字段
     * @param ignoreNull 是否忽略空字段
     * @throws NoSuchMethodException     方法未找到
     * @throws IllegalAccessException    权限异常
     * @throws InvocationTargetException 被更新对象异常
     */
    default void executeGetterAndSetter(Entity entity, Class clazz, Field field, boolean ignoreNull) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String fieldName = field.getName();
        fieldName = StringUtils.capitalize(fieldName);
        String get = getPrefix + fieldName;
        String set = setPrefix + fieldName;
        Method getMethod = clazz.getMethod(get);
        Method setMethod = this.getClass().getMethod(set, field.getType());
        Object getResult = getMethod.invoke(entity);
        if (getResult != null || !ignoreNull)
            setMethod.invoke(this, getResult);
    }
}
