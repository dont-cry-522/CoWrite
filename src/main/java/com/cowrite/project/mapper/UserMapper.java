package com.cowrite.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowrite.project.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * User Mapper 接口
 * @author Hibiscus-code-generate
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名检查用户是否存在
     */
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(@Param("email") String email);

    /**
     * 根据邮箱查询用户
     */
    User selectByEmail(String email);

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);
}
