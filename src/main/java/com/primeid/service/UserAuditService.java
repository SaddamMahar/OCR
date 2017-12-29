package com.primeid.service;

import com.primeid.dao.UserAuditDao;
import com.primeid.dao.UserDao;
import com.primeid.model.User;
import com.primeid.model.UserAudit;
import java.util.List;
import javax.security.auth.login.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */

@Service("userAuditService")
public class UserAuditService {

    @Autowired
    private UserAuditDao userAuditDao;

    @Transactional(readOnly = true)
    public List<UserAudit> loadUserByUsername(String user){
        return userAuditDao.findByAccountCode(user);
    }
    
    public void save(UserAudit userAudit) {
        userAuditDao.save(userAudit);
    }

}
