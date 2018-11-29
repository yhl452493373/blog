package com.yang.blog.es.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

public interface EsBaseService<Doc, ID extends Serializable> {
    Doc save(Doc doc);

    IPage<Doc> search(IPage<Doc> page, QueryBuilder queryBuilder);

    IPage<Doc> searchSimilar(IPage<Doc> page, Doc doc, String[] fields);

    IPage<Doc> findAll(IPage<Doc> page);

    Iterable<Doc> findAll(Sort sort);

    Doc findById(ID id);
}
