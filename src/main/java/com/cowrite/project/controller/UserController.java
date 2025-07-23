package com.cowrite.project.controller;

import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.context.AuthContext;
import com.cowrite.project.model.dto.user.UserEmailRequest;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.model.vo.UserVO;
import com.cowrite.project.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static com.cowrite.project.common.enums.ResponseCodeEnum.SYSTEM_ERROR;

/**
 * User 控制器，提供基础增删改查接口
 *
 * @author Hibiscus-code-generate
 */
@Api(tags = "User 控制器")
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 发送验证码
     */
    @GetMapping("/send-code/{email}")
    @ApiOperation("发送验证码")
    public ApiResponse<Boolean> sendVerificationCode(@PathVariable String email) throws Exception {
        Boolean isSuccess = userService.sendVerificationCode(email);
        if (isSuccess) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error(SYSTEM_ERROR.code(), "发送失败");
        }
    }

    /**
     * 邮箱注册账号
     */
    @PostMapping("/register/email")
    @ApiOperation("邮箱注册账号")
    public ApiResponse<UserVO> registerEmail(@RequestBody UserEmailRequest userEmailRequest, HttpServletRequest request) {
        return ApiResponse.success(userService.registerByEmail(userEmailRequest, request));
    }

    /**
     * 根据token获取信息
     */
    @GetMapping("/info")
    @ApiOperation("根据token获取角色信息")
    public ApiResponse<UserVO> getUserInfoByToken() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null){
            currentUser.setPassword(null);
            return ApiResponse.success(UserVO.convertToUserVO(currentUser));
        } else {
            return ApiResponse.error(SYSTEM_ERROR.code(), "用户不存在");
        }
    }

    /**
     * 邮箱登录
     */
    @PostMapping("/login/email")
    @ApiOperation("邮箱登录账号")
    public ApiResponse<String> loginByEmail(@RequestBody UserEmailRequest userEmailRequest, HttpServletRequest request) {
        return ApiResponse.success(userService.loginByEmail(userEmailRequest, request));
    }

    /**
     * 用户退出登录
     */
    @PostMapping("/logout")
    @ApiOperation("用户退出登录")
    public ApiResponse<String> logout() {
        String currentToken = AuthContext.getCurrentToken();
        userService.logoutUser(currentToken);
        return ApiResponse.success("退出登录成功");
    }

    /**
     * 上传头像
     */
    @PostMapping("/upload-avatar")
    @ApiOperation("上传头像")
    public ApiResponse<String> uploadAvatar(
            @RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        return ApiResponse.success(avatarUrl);
    }

    /**
     * 普通用户修改自己的信息
     */
    @PutMapping("/update")
    @ApiOperation("普通用户修改自己的信息")
    public ApiResponse<String> updateUser(@RequestBody User user) {
        try {
            return ApiResponse.success(userService.updateUserInfo(user));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(SYSTEM_ERROR.code(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user info", e);
            return ApiResponse.error(SYSTEM_ERROR.code(), "服务器内部错误");
        }
    }

    /**
     * 注销用户
     */
    @PostMapping("/delete-account")
    @ApiOperation("注销用户")
    public ApiResponse<Void> deleteAccount() {
        userService.requestAccountDeletion();
        return ApiResponse.success(null);
    }
}