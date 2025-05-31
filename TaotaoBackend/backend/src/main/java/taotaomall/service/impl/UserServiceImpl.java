package taotaomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.UserDao;
import taotaomall.model.User;
import taotaomall.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public int register(User user) {
        return userDao.register(user);
    }

    @Override
    public User findByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    @Override
    public User loginService(String username, String password) {
        return userDao.login(username, password);
    }

    public User registerService(User user) {
        User tmp = userDao.findByUsername(user.getUsername());
        if (tmp != null) {
            return null;
        } else {
            userDao.register(user);
            return user;
        }
    }
}
