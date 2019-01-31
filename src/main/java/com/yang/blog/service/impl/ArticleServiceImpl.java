package com.yang.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.entity.Article;
import com.yang.blog.mapper.ArticleMapper;
import com.yang.blog.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Article> getNewest(int size) {
        return baseMapper.getNewest(size);
    }

    @Override
    public List<Article> getHottest(int size) {
        return baseMapper.getHottest(size);
    }

    @Override
    public List<Article> findByTagId(String tagId, int size) {
        return baseMapper.findByTagId(tagId,size);
    }
}
