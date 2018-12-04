package com.yang.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.entity.ArticleFile;

/**
 * <p>
 * 文章对应的文件，主要用于删除文章时同步删除文件 服务类
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
public interface ArticleFileService extends IService<ArticleFile> {

}
