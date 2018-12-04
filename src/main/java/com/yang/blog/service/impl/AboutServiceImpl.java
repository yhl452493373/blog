package com.yang.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.entity.About;
import com.yang.blog.mapper.AboutMapper;
import com.yang.blog.service.AboutService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关于 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
@Service
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About> implements AboutService {

}
