package taotaomall.service;


import org.springframework.stereotype.Service;
import taotaomall.model.User;

@Service
public interface UserService {
    //注册业务
    User registerService(User user);
    //登录业务
    User loginService(String username,String password);
}
