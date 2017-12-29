/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.daoImpl;

import com.primeid.dao.NameThresholdDao;
import com.primeid.model.NameThreshold;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author hashim
 */
@Repository(value = "nameThresholdDao")
public class NameThresholdDaoImpl implements NameThresholdDao{
    
    @Autowired
 private SessionFactory sessionFactory;


     @Transactional(readOnly = false)
     
    @Override
    public void save(NameThreshold nameThreshold) {
        sessionFactory.getCurrentSession().save(nameThreshold);
    }
     @Transactional
     @Override
    public NameThreshold findByAccountID(long accountID) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from NameThreshold where accountID=? ")
                .setParameter(0, accountID)
                .list();
        if (list != null && list.size() > 0) {
            return (NameThreshold) list.get(0);
        }
        return null;
    }
    
    
    
}
