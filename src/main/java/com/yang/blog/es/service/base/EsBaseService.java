package com.yang.blog.es.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.io.Serializable;

@SuppressWarnings("unused")
public interface EsBaseService<Doc, ID extends Serializable> {
    /**
     * 保存或修改文档
     *
     * @param doc es文档(即传统数据库实体)
     * @return 保存后的文档
     */
    Doc save(Doc doc);

    /**
     * 根据条件分页查询.此法采用原生方法查询,可以用过滤
     *
     * @param page        mybatis plus的分页对象
     * @param searchQuery 查询条件
     * @return 分页对象
     */
    IPage<Doc> search(IPage<Doc> page, SearchQuery searchQuery);

    /**
     * 根据条件分页查询.此法采用spring data elasticsearch方法查询
     *
     * @param page         mybatis plus的分页对象
     * @param queryBuilder 查询条件
     * @return 分页对象
     */
    IPage<Doc> search(IPage<Doc> page, QueryBuilder queryBuilder);

    /**
     * 根据文档字段分页查询相似文档对象
     *
     * @param page   mybatis plus的分页对象
     * @param doc    要查询的文档
     * @param fields 对比字段
     * @return 分页对象
     */
    IPage<Doc> searchSimilar(IPage<Doc> page, Doc doc, String[] fields);

    /**
     * 分页查询所有文档
     *
     * @param page mybatis plus的分页对象
     * @return 分页对象
     */
    IPage<Doc> findAll(IPage<Doc> page);

    /**
     * 排序查询所有文档
     *
     * @param sort 排序属性
     * @return 文档列表
     */
    Iterable<Doc> findAll(Sort sort);

    /**
     * 根据条件查询记录的id
     *
     * @param searchQuery 查询条件
     * @return id集合
     */
    Iterable<String> findIds(SearchQuery searchQuery);

    /**
     * 根据id查询文档
     *
     * @param id 文档id
     * @return 文档
     */
    Doc findById(ID id);
}
