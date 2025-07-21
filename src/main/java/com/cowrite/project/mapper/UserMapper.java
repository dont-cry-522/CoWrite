package com.cowrite.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowrite.project.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
