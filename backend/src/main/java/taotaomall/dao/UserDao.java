package taotaomall.dao;

import org.apache.ibatis.annotations.Mapper;
import taotaomall.model.User;

@Mapper
public interface UserDao {
    // 注册用户
    int register(User user);
    
    // 根据用户名查找用户,判断有没有重名
    User findByUsername(String username);
    
    // 用户登录
    User login(String username, String password);
}
