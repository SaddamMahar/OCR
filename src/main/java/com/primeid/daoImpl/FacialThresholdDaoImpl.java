package com.primeid.daoImpl;

import com.google.gson.Gson;
import com.primeid.dao.FacialThresholdDao;
import com.primeid.model.Case;
import com.primeid.model.FacialThreshold;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */
@Repository("facialThresholdDao")
public class FacialThresholdDaoImpl implements FacialThresholdDao{

    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public FacialThreshold findByFacialThresID(long id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from FacialThreshold where id=? ORDER BY created DESC")
                .setParameter(0, id)
                .list();
        if (list != null && list.size() > 0) {
            return (FacialThreshold) list.get(0);
        }
        return null;
    }

    @Transactional
    public FacialThreshold findByAccountID(long accountID) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from FacialThreshold where accountID=? ORDER BY created DESC")
                .setParameter(0, accountID)
                .list();
        if (list != null && list.size() > 0) {
            return (FacialThreshold) list.get(0);
        }
        return null;
    }
    
    @Transactional(readOnly = false)
    @Override
    public FacialThreshold save(FacialThreshold facialThreshold) {
        try {
            sessionFactory.getCurrentSession().save(facialThreshold);
        return facialThreshold;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
