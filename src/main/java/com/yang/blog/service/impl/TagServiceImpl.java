package com.yang.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.entity.Tag;
import com.yang.blog.mapper.TagMapper;
import com.yang.blog.service.TagService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <p>
 * 文章标签 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public Collection<Tag> listArticleRelateTagAsc(String articleId, String... orderColumns) {
        return baseMapper.listArticleRelateTagAsc(articleId, orderColumns);
    }
}
