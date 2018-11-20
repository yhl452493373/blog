package com.yang.blog.service.impl;

import com.yang.blog.entity.Comment;
import com.yang.blog.mapper.CommentMapper;
import com.yang.blog.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
