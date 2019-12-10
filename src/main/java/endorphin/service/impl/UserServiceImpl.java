package endorphin.service.impl;

import endorphin.dao.UserDao;
import endorphin.domain.User;
import endorphin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserServiceImpl
 *
 * @author igaozp
 * @version 1.0
 * @since 2016
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        if (user != null) {
            userDao.addUser(user);
        }
    }

    @Override
    public void updateUserByUserName(User user) {
        if (user != null) {
            userDao.updateUserByUserName(user);
        }
    }
    @Override
    public void updateUserByOpenId(User user) {
        if (user != null) {
            userDao.updateUserByOpenId(user);
        }
    }
    @Override
    public void updateUserByPhone(User user) {
        if (user != null) {
            userDao.updateUserByPhone(user);
        }
    }

    @Override
    public User getUserByUserName(String userName) {
        if (userName == null) {
            return null;
        }
        return userDao.findUserByUserName(userName);
    }
    @Override
    public User getUserByOpenId(String openId) {
        if (openId == null) {
            return null;
        }
        return userDao.findUserByOpenID(openId);
    }

    @Override
    public User getUserByPhone(String phone) {
        if (phone.equals("0") ) {
            return null;
        }
        return userDao.findUserByPhone(phone);
    }

    @Override
    public String getPassword(String userName) {
        if (userName == null) {
            return null;
        }
        return userDao.getUserPasswordByUserName(userName);
    }

    @Override
    public void deleteUserByUserName(String userName) { }

    @Override
    public void deleteUserByOpenId(String openId) { }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUserInfo();
    }
}
