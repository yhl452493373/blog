package com.yang.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.entity.AboutFile;
import com.yang.blog.mapper.AboutFileMapper;
import com.yang.blog.service.AboutFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关于对应的文件，主要用于删除关于时同步删除文件 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-26
 */
@Service
public class AboutFileServiceImpl extends ServiceImpl<AboutFileMapper, AboutFile> implements AboutFileService {

}
