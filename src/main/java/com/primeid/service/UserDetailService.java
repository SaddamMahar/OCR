package com.primeid.service;

import com.primeid.model.User;
import com.primeid.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */

@Service("userDetailsService")
public class UserDetailService {

    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = true)
    public User loadUserByAccountCode(String accountCode) {

        User userObj = null;
        userObj = userDao.findByAccountCode(accountCode);
        return (User) userObj;

    }

}
