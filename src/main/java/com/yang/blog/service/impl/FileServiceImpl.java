package com.yang.blog.service.impl;

import com.yang.blog.entity.File;
import com.yang.blog.mapper.FileMapper;
import com.yang.blog.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 上传的文件 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}
