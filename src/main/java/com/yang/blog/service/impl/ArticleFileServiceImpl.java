package com.yang.blog.service.impl;

import com.yang.blog.entity.ArticleFile;
import com.yang.blog.mapper.ArticleFileMapper;
import com.yang.blog.service.ArticleFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章对应的文件，主要用于删除文章时同步删除文件 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
@Service
public class ArticleFileServiceImpl extends ServiceImpl<ArticleFileMapper, ArticleFile> implements ArticleFileService {

}
