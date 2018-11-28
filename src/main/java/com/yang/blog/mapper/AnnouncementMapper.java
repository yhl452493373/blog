package com.yang.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.blog.entity.Announcement;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 公告信息 Mapper 接口
 * </p>
 *
 * @author User
 * @since 2018-11-27
 */
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    void setOtherAvailable(@Param("userId") String userId, @Param("available") Integer available);
}
