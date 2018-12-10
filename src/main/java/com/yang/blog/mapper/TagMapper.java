package com.yang.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.blog.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * <p>
 * 文章标签 Mapper 接口
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface TagMapper extends BaseMapper<Tag> {

    Collection<Tag> listArticleRelateTagAsc(@Param("articleId") String articleId, @Param("orderColumns") String[] orderColumns);
}
