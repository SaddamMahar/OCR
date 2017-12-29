package com.primeid.daoImpl;

import com.primeid.model.TamperThreshold;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.primeid.dao.TamperThresholdDao;

/** 
 *
 * @author hashim
 */
@Repository(value = "tamperThreshDao")
public class TamperThresholdDaoImpl implements TamperThresholdDao {
    
 @Autowired
 private SessionFactory sessionFactory;


     @Transactional(readOnly = false)
    @Override
    public void save(TamperThreshold tamperThresh) {
        sessionFactory.getCurrentSession().save(tamperThresh);
    }
     @Transactional
     @Override
    public TamperThreshold findByAccountID(long accountID) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from TamperThreshold where accountID=? ORDER BY lastModified DESC")
                .setParameter(0, accountID)
                .list();
        if (list != null && list.size() > 0) {
            return (TamperThreshold) list.get(0);
        }
        return null;
    }
 
    
}
