package com.cowrite.project.controller;

import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * UserService
     */
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 发送验证码
     */
    @GetMapping("/send-code/{email}")
    @ApiOperation("发送验证码")
    public ApiResponse<Boolean> sendVerificationCode(@PathVariable String email) {
        Boolean isSuccess = userService.sendVerificationCode(email);
        if (isSuccess) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error(SYSTEM_ERROR.code(), "发送失败");
        }
    }
}
