package com.yang.blog.mapper;

import com.yang.blog.entity.ArticleFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 文章对应的文件，主要用于删除文章时同步删除文件 Mapper 接口
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
public interface ArticleFileMapper extends BaseMapper<ArticleFile> {

}
