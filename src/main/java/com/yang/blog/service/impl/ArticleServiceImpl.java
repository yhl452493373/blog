package com.yang.blog.service.impl;

import com.yang.blog.entity.Article;
import com.yang.blog.mapper.ArticleMapper;
import com.yang.blog.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博文内容 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
