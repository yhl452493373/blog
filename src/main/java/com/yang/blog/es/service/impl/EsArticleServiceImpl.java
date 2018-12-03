package com.yang.blog.es.service.impl;

import com.yang.blog.es.dao.EsArticleDao;
import com.yang.blog.es.doc.EsArticle;
import com.yang.blog.es.service.EsArticleService;
import com.yang.blog.es.service.base.EsBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EsArticleServiceImpl extends EsBaseServiceImpl<EsArticle,String, EsArticleDao> implements EsArticleService {
}
