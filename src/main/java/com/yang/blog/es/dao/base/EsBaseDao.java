package com.yang.blog.es.dao.base;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface EsBaseDao<EsDoc, ID extends Serializable> extends ElasticsearchRepository<EsDoc, ID> {
}
