package com.yang.blog.es.doc.base;

import com.yang.blog.annotation.FieldNotUpdate;
import com.yang.blog.entity.base.Base;

import java.lang.reflect.Field;

@SuppressWarnings({"Duplicates", "unchecked"})
public interface EsBase<Doc, Entity> extends Base<Entity> {
    /**
     * 从传统数据库的对象获取数据并set到es数据库的对象.注意,默认使用的反射,字段过多时,此法效率较低,建议按照需要在每个实体中重写该方法
     *
     * @param ignoreNull 是否忽略空字段
     * @param entity     传统数据库对象
     * @param docClazz   es对象类,此参数不能删除,否则会使用此接口继承父类的方法
     * @return 更新后的对象
     */
    @SuppressWarnings("unused")
    default Doc update(Boolean ignoreNull, Entity entity, Class<Doc> docClazz) {
        Field[] fields = getAllFields(null, null);
        for (Field field : fields) {
            FieldNotUpdate fieldNotUpdate = field.getAnnotation(FieldNotUpdate.class);
            //设置了调用update方法时跳过,则不更新
            if (fieldNotUpdate != null)
                continue;
            try {
                executeGetterAndSetter(entity, entity.getClass(), field, ignoreNull);
            } catch (Exception ignored) {
                //由于夸实体更新,忽略一些不必要的异常
            }
        }
        return (Doc) this;
    }
}
