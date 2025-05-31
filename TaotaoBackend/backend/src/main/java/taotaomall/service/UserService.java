package taotaomall.service;


import org.springframework.stereotype.Service;
import taotaomall.model.User;

@Service
public interface UserService {
    //注册业务
    int register(User user);
    //登录业务
    User loginService(String username, String password);
    User findByPhone(String phone);
}
