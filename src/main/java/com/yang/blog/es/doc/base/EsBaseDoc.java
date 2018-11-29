package com.yang.blog.es.doc.base;

@SuppressWarnings("unchecked")
public abstract class EsBaseDoc<Doc> implements EsBase<Doc>, Cloneable {
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Doc clone() {
        try {
            return (Doc) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
