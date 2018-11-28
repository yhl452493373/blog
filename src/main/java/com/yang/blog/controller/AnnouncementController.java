package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yhl452493373.bean.JSONResult;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.Announcement;
import com.yang.blog.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author User
 * @since 2018-11-27
 */
@RestController
@RequestMapping("/data/announcement")
public class AnnouncementController implements BaseController {
    private final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 添加数据
     *
     * @param announcement 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(Announcement announcement) {
        JSONResult jsonResult = JSONResult.init();
        String userId = ShiroUtils.getLoginUser().getId();
        announcement.setUserId(userId);
        announcement.setCreatedTime(LocalDateTime.now());
        announcement.setAvailable(Announcement.AVAILABLE);
        service.announcementService.setOtherAvailable(userId, Announcement.BLOCK);
        boolean result = service.announcementService.save(announcement);
        if (result)
            jsonResult.success(ADD_SUCCESS).data(announcement);
        else
            jsonResult.error(ADD_FAILED);
        return jsonResult;
    }

    /**
     * 获取用户最新公告
     *
     * @return 最新公告信息
     */
    @RequestMapping("/newest")
    public JSONResult newest() {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<Announcement> announcementQueryWrapper = new QueryWrapper<>();
        announcementQueryWrapper.eq("available", Announcement.AVAILABLE);
        announcementQueryWrapper.orderByDesc("created_time");
        Announcement announcement = service.announcementService.getOne(announcementQueryWrapper);
        if (announcement != null)
            return jsonResult.success(QUERY_SUCCESS).data(announcement).count(1);
        return jsonResult.success(QUERY_SUCCESS).count(0);
    }
}