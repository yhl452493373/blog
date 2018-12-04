package com.yang.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.entity.Message;
import com.yang.blog.mapper.MessageMapper;
import com.yang.blog.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 留言 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
