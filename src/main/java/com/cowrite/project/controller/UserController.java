package com.cowrite.project.controller;

import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.model.entity.Register;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.UserService;
import com.cowrite.project.utils.CodeCreateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Collection;

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
    private final CodeCreateUtils codeCreateUtils;

    public UserController(UserService userService, CodeCreateUtils codeCreateUtils) {
        this.userService = userService;
        this.codeCreateUtils = codeCreateUtils;
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

    @PostMapping("/create")
    @ApiOperation("创建用户")
    public ApiResponse<Boolean> createUser(@RequestBody Register register) {
        Boolean codeVerified = codeCreateUtils.verifyCode(register.getEmail(), register.getCode());
        User user = new User(register.getName(),
                register.getEmail(),
                register.getPassword(),
                register.getAvatarUrl(),
               "ACTIVE");

        if (!codeVerified) {
            return ApiResponse.error("验证码错误或已过期");
        }

        Boolean isSuccess = userService.save(user);
        if (isSuccess) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("创建失败");
        }
    }

    @GetMapping("/delete/{id}")
    @ApiOperation("删除用户")
    public ApiResponse<Boolean> deleteUser(@PathVariable Long id){
        Boolean isSuccess  = userService.removeById(id);
        if(isSuccess) {
            return ApiResponse.success(true);
        }else {
            return ApiResponse.error(SYSTEM_ERROR.code(),"删除失败");
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新用户信息")
    public ApiResponse<Boolean> updateUser(@RequestBody User user){
        Boolean isSuccess =userService.updateUser(user);
        if(isSuccess) {
            return ApiResponse.success(true);
        }else {
            return ApiResponse.error(SYSTEM_ERROR.code(),"更新失败");
        }
    }

    @GetMapping("/find_user/{id}")
    @ApiOperation("查找用户")
    public ApiResponse<User> findUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user!= null){
            return ApiResponse.success(user);
        }else{
            return ApiResponse.error(SYSTEM_ERROR.code(),"查找失败");
        }

    }
}