package com.primeid.dao;

import com.primeid.model.User;

/**
 *
 * @author Saddam Hussain
 */
public interface UserDao {
    User findByAccountCode (String accountCode);
}
