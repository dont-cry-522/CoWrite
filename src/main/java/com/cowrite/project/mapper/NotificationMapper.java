package com.cowrite.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowrite.project.model.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
