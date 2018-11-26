package com.yang.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.entity.File;

import java.util.List;

/**
 * <p>
 * 上传的文件 服务类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface FileService extends IService<File> {
    boolean setAvailable(List<String> fileIdList, Integer available);
}
