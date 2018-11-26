package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.*;
import com.yang.blog.shiro.ShiroUtils;
import com.yang.blog.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/about")
public class AboutController implements BaseController {
    private final Logger logger = LoggerFactory.getLogger(AboutController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 添加数据
     *
     * @param about 添加对象
     * @return 添加结果
     */
    @SuppressWarnings("Duplicates")
    @RequestMapping("/add")
    public JSONResult add(About about) {
        JSONResult jsonResult = JSONResult.init();
        if (StringUtils.isEmpty(about.getContent()))
            return jsonResult.error(ADD_FAILED + "内容不能为空");
        User user = ShiroUtils.getLoginUser();
        about.setId(CommonUtils.uuid());
        about.setUserId(user.getId());
        about.setCreatedTime(LocalDateTime.now());
        about.setAvailable(Article.TEMP);
        boolean aboutResult, aboutFileResult = true;
        aboutResult = service.aboutService.saveOrUpdate(about);
        String fileIds = about.getFileIds();
        if (StringUtils.isNotEmpty(fileIds)) {
            aboutFileResult = relateAboutAndFile(about);
        }
        if (aboutResult && aboutFileResult) {
            about.setAvailable(Article.AVAILABLE);
            service.aboutService.updateById(about);
            //添加博客成功,返回其id作为data的值,通过id跳转
            jsonResult.success(ADD_SUCCESS).data(about.getId());
        } else {
            jsonResult.error(ADD_FAILED);
        }
        return jsonResult;
    }

    /**
     * 修改数据
     *
     * @param about 修改对象
     * @return 修改结果
     */
    @SuppressWarnings("Duplicates")
    @RequestMapping("/update")
    public JSONResult update(About about) {
        JSONResult jsonResult = JSONResult.init();
        if (StringUtils.isEmpty(about.getId()))
            return jsonResult.error(UPDATE_FAILED + "id参数异常");
        if (StringUtils.isEmpty(about.getContent()))
            return jsonResult.error(UPDATE_FAILED + "内容不能为空");
        boolean aboutResult = service.aboutService.updateById(about), aboutFileResult;
        aboutFileResult = relateAboutAndFile(about);
        if (aboutResult && aboutFileResult)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 将指定id的文件和about对象关联起来
     *
     * @param about 实体对象
     * @return 关联结果
     */
    @SuppressWarnings("Duplicates")
    private boolean relateAboutAndFile(About about) {
        String fileIds = about.getFileIds();
        boolean fileResult = true, aboutFileResult = true;
        List<String> fileIdList = CommonUtils.splitIds(fileIds);
        if (!fileIdList.isEmpty()) {
            User user = ShiroUtils.getLoginUser();
            fileResult = service.fileService.setAvailable(fileIdList, File.AVAILABLE);
            if (fileResult) {
                //将文件和文章关联
                List<AboutFile> aboutFileList = new ArrayList<>();
                fileIdList.forEach(fileId -> {
                    AboutFile articleFile = new AboutFile();
                    articleFile.setAboutId(about.getId());
                    articleFile.setFileId(fileId);
                    articleFile.setCreatedTime(LocalDateTime.now());
                    articleFile.setUserId(user.getId());
                    aboutFileList.add(articleFile);
                });
                aboutFileResult = service.aboutFileService.saveBatch(aboutFileList);
            }
        }

        if (fileResult) {
            //清理临时状态的文件.此一步操作稍作修改可作为定时任务一部分
            QueryWrapper<AboutFile> aboutQueryWrapper = new QueryWrapper<>();
            aboutQueryWrapper.eq("about_id", about.getId());
            List<AboutFile> aboutFileList = service.aboutFileService.list(aboutQueryWrapper);
            List<String> aboutFileIdList = CommonUtils.convertToFieldList(aboutFileList, "getFileId");
            QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
            fileQueryWrapper.in("id", aboutFileIdList);
            fileQueryWrapper.eq("available", File.TEMP);
            List<File> tempFileList = service.fileService.list(fileQueryWrapper);
            aboutFileResult = FileUtils.delete(CommonUtils.convertToIdList(tempFileList));
        }
        return fileResult && aboutFileResult;
    }
}