package com.yang.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.entity.Announcement;

/**
 * <p>
 * 公告信息 服务类
 * </p>
 *
 * @author User
 * @since 2018-11-27
 */
public interface AnnouncementService extends IService<Announcement> {

    void setOtherAvailable(Integer available);
}
