package com.yang.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yang.blog.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 博文内容 Mapper 接口
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> getNewest(int size);

    List<Article> getHottest(int size);

    List<Article> findByTagId(@Param("tagId") String tagId, @Param("size") int size);

    IPage<Article> selectUnionPage(IPage<Article> page);
}
