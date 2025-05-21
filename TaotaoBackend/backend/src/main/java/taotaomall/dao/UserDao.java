package taotaomall.dao;

import org.apache.ibatis.annotations.Mapper;
import taotaomall.model.User;

@Mapper
public interface UserDao {
    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 根据ID查询用户
     */
    User findById(Integer id);

    /**
     * 添加用户
     */
    int insert(User user);

    /**
     * 更新用户信息
     */
    int update(User user);
} 