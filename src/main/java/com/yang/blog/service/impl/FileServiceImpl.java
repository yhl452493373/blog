package com.yang.blog.service.impl;

import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.entity.File;
import com.yang.blog.mapper.FileMapper;
import com.yang.blog.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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

    @Override
    public boolean setAvailable(List<String> fileIdList, Integer available) {
        Collection<File> fileList = listByIds(fileIdList);
        fileList.forEach(file -> file.setAvailable(available));
        return updateBatchById(fileList);
    }
}
