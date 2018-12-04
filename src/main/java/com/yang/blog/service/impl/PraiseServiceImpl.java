package com.yang.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.entity.Praise;
import com.yang.blog.mapper.PraiseMapper;
import com.yang.blog.service.PraiseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章、评论的点赞记录表 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-23
 */
@Service
public class PraiseServiceImpl extends ServiceImpl<PraiseMapper, Praise> implements PraiseService {

}
