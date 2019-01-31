package com.yang.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.blog.entity.Announcement;

/**
 * <p>
 * 公告信息 Mapper 接口
 * </p>
 *
 * @author User
 * @since 2018-11-27
 */
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    void setOtherAvailable(Integer available);

    Announcement getNewest();
}
