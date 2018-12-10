package com.yang.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.blog.entity.File;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 上传的文件 Mapper 接口
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface FileMapper extends BaseMapper<File> {

    List<File> listAboutRelateFile(@Param("id") String id, @Param("fileAvailable") Integer fileAvailable);
}
