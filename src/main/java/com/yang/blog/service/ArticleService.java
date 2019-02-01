package com.yang.blog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.entity.Article;

import java.util.List;

/**
 * <p>
 * 博文内容 服务类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface ArticleService extends IService<Article> {

    List<Article> getNewest(int size);

    List<Article> getHottest(int size);

    List<Article> findByTagId(String tagId, int size);

    List<Article> unionPage(IPage<Article> page);
}
