/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.daoImpl;

import com.primeid.dao.UserAuditDao;
import com.primeid.model.UserAudit;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */

@Repository("userAuditDao")
public class UserAuditDaoImpl implements UserAuditDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public List<UserAudit> findByAccountCode(String accountCode) {

        List<UserAudit> users = new ArrayList<UserAudit>();

        users = sessionFactory.getCurrentSession()
                .createQuery("from User where accountCode=?")
                .setParameter(0, accountCode)
                .list();

        if (users.size() > 0) {
            return users;
        } else {
            return null;
        }

    }
    
    @Transactional(readOnly = false)
    @Override
    public void save(UserAudit userAudit) {
        sessionFactory.getCurrentSession().save(userAudit);
    }
}
