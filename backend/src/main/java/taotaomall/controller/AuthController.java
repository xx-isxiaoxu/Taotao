package taotaomall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.Userorder;
import taotaomall.service.SmsService;
import taotaomall.service.UserService;
import taotaomall.model.User;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;
import taotaomall.utils.JWTUtils;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplateString;

    // 发送验证码
    @PostMapping("/sendCode")
    public Result<?> sendCode(@RequestBody Map<String, String> map) {
        String phone = map.get("phone");
        if (phone == null || phone.isEmpty()) {
            return Result.failure(ResultCodeEnum.PARAMS_ERROR, "手机号不能为空");
        }
        // 生成验证码
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        // 存入redis
        redisTemplateString.opsForValue().set("login:code:" + phone, code, 5, TimeUnit.MINUTES);
        // Java 1.8 兼容写法
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("success", true);
        data.put("message", "验证码已发送");
        return Result.success(data);
    }

    // 验证码登录
    @PostMapping("/loginByPhone")
    public Map<String, Object> loginByPhone(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String code = body.get("code");
        String cacheCode = redisTemplate.opsForValue().get("login:code:" + phone);
        Map<String, Object> result = new HashMap<>();
        if (cacheCode == null || !cacheCode.equals(code)) {
            result.put("success", false);
            result.put("message", "验证码错误或已过期");
            return result;
        }
        User user = userService.findByPhone(phone);
        if (user != null) {
            // 已注册，自动登录
            String accessToken = JWTUtils.getToken(String.valueOf(user.getId()), user.getUsername());
            String refreshToken = JWTUtils.getRefreshToken(String.valueOf(user.getId()), user.getUsername());
            Map<String, Object> data = new HashMap<>();
            data.put("token", accessToken);
            data.put("refreshToken", refreshToken);
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            return Result.success(data).getData();
        } else {
            // 未注册，前端跳转到注册页
            result.put("success", false);
            result.put("needRegister", true);
            result.put("message", "手机号未注册，请先注册");
            return result;
        }
    }

    @PostMapping("/registerByPhone")
    public Map<String, Object> registerByPhone(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String code = body.get("code");
        String username = body.get("username");
        String password = body.get("password");
        String cacheCode = redisTemplate.opsForValue().get("login:code:" + phone);
        Map<String, Object> result = new HashMap<>();
        if (cacheCode == null || !cacheCode.equals(code)) {
            result.put("success", false);
            result.put("message", "验证码错误或已过期");
            return result;
        }
        User exist = userService.findByPhone(phone);
        if (exist != null) {
            result.put("success", false);
            result.put("message", "手机号已注册");
            return result;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        user.setCreateTime(new Date());
        userService.register(user);
        String accessToken = JWTUtils.getToken(String.valueOf(user.getId()), user.getUsername());
        String refreshToken = JWTUtils.getRefreshToken(String.valueOf(user.getId()), user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("message", "注册成功");
        data.put("userId", user.getId());
        return Result.success(data).getData();
    }

   @PostMapping("/pay")
   public Result<String> alipay(@RequestHeader("Authorization") String token, @RequestBody Userorder orderModel) {
       System.out.println("收到的token: " + token);
       return null;
   }

   @PostMapping("/refreshToken")
   public Result<?> refreshToken(@RequestBody Map<String, String> map) {
       String refreshToken = map.get("refreshToken");
       // 校验 refreshToken 合法性
       // 如果合法，生成新的 access token
       // 返回新 token
       return null;
   }
}
