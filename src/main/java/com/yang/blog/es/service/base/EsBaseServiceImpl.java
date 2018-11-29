package com.yang.blog.es.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yang.blog.es.dao.base.EsBaseDao;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class EsBaseServiceImpl<EsDoc, ID extends Serializable, EsDao extends EsBaseDao<EsDoc, ID>> implements EsBaseService<EsDoc, ID> {
    @Autowired
    private EsDao esBaseDao;

    @Override
    public EsDoc save(EsDoc esDoc) {
        return esBaseDao.save(esDoc);
    }

    @Override
    public IPage<EsDoc> search(IPage<EsDoc> page, QueryBuilder queryBuilder) {
        Pageable pageable = getPageable(page);
        Page<EsDoc> esArticlePage = esBaseDao.search(queryBuilder, pageable);
        page.setTotal(esArticlePage.getTotalElements());
        page.setRecords(esArticlePage.getContent());
        return page;
    }

    @Override
    public IPage<EsDoc> searchSimilar(IPage<EsDoc> page, EsDoc esArticle, String[] fields) {
        Pageable pageable = getPageable(page);
        Page<EsDoc> esArticlePage = esBaseDao.searchSimilar(esArticle, fields, pageable);
        page.setTotal(esArticlePage.getTotalElements());
        page.setRecords(esArticlePage.getContent());
        return page;
    }

    @Override
    public IPage<EsDoc> findAll(IPage<EsDoc> page) {
        Pageable pageable = getPageable(page);
        Page<EsDoc> esArticlePage = esBaseDao.findAll(pageable);
        page.setTotal(esArticlePage.getTotalElements());
        page.setRecords(esArticlePage.getContent());
        return page;
    }

    @Override
    public Iterable<EsDoc> findAll(Sort sort) {
        return esBaseDao.findAll(sort);
    }

    @Override
    public EsDoc findById(ID id) {
        return esBaseDao.findById(id).orElse(null);
    }

    /**
     * 根据mybatis plus的分页生成spring data的分页
     *
     * @param page mybatis plus分页
     * @return spring data分页
     */
    private Pageable getPageable(IPage<EsDoc> page) {
        Sort sort = null;
        if (page.ascs() != null) {
            sort = Sort.by(Sort.Direction.ASC, page.ascs());
        }
        if (page.descs() != null) {
            if (sort == null) {
                sort = Sort.by(Sort.Direction.DESC, page.descs());
            } else {
                sort.and(Sort.by(Sort.Direction.DESC, page.descs()));
            }
        }
        return PageRequest.of((int) page.getCurrent() - 1, (int) page.getSize(), sort);
    }
}
