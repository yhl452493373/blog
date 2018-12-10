package com.yang.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.entity.Tag;

import java.util.Collection;

/**
 * <p>
 * 文章标签 服务类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface TagService extends IService<Tag> {

    Collection<Tag> listArticleRelateTagAsc(String articleId, String... orderColumns);
}
