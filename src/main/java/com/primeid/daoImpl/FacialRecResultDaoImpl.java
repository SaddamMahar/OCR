package com.primeid.daoImpl;

import com.primeid.dao.FacialRecResultDao;
import com.primeid.model.FacialRecResult;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */
@Repository("facialRecResultDao")
public class FacialRecResultDaoImpl implements FacialRecResultDao{
    @Autowired
    private SessionFactory sessionFactory;
    
    @Transactional
    public FacialRecResult findByFacialID(long facialRecResultID) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from FacialRecResult where facialRecResultID=? ORDER BY created DESC")
                .setParameter(0, facialRecResultID)
                .list();
        if (list != null && list.size() > 0) {
            return (FacialRecResult) list.get(0);
        }
        return null;
    }
    
    @Transactional(readOnly = false)
    public FacialRecResult save(FacialRecResult facialRecResult) {
        sessionFactory.getCurrentSession().save(facialRecResult);
        return facialRecResult;
    }
    
}
