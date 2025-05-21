package taotaomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.UserDao;
import taotaomall.model.User;
import taotaomall.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    UserDao userDao;

    @Override
    public User registerService(User user) {
        User tmp = userDao.findByUsername(user.getUsername());
        if(tmp != null)
        {return null;}
        else
        {
            userDao.register(user);
            return user;
        }
    }

    @Override
    public User loginService(String username,String password) {
        return userDao.login(username,password);
    }
}
