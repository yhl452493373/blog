package com.yang.blog.service.impl;

import com.yang.blog.entity.ArticleTag;
import com.yang.blog.mapper.ArticleTagMapper;
import com.yang.blog.service.ArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博文与标签的对应关系 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
