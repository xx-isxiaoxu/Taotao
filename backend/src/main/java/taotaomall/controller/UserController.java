package taotaomall.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taotaomall.model.User;
import taotaomall.service.UserService;
import taotaomall.utils.JWTUtils;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")   //127.0.0.1:8080/user/
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping(value = "/register")
    public Result<User> register(@RequestBody User user)
    {
        user.setCreateTime(new Date());
        if(userService.registerService(user) != null)
        {
            return Result.success(user);
        }
        else
        {
            return Result.failure(ResultCodeEnum.USER_IS_EXITES);
        }
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User userformjdbc = userService.loginService(user.getUsername(),user.getPassword());
        if(userformjdbc == null)
        {
            return Result.failure(ResultCodeEnum.UNAUTHORIZED,"用户名或密码错误！");
        }
        else
        {
            String token = JWTUtils.getToken(String.valueOf(userformjdbc.getId()),userformjdbc.getUsername());
            Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("userId", String.valueOf(userformjdbc.getId()));
            userMap.put("userName",userformjdbc.getUsername());
            userMap.put("token",token);
            return Result.success(userMap);
        }
    }
}

