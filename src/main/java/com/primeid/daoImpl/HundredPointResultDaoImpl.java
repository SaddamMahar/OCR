
package com.primeid.daoImpl;

import com.primeid.dao.HundredPointResultDao;
import com.primeid.model.HundredPointResult;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository(value = "hundredPointResultDao")
public class HundredPointResultDaoImpl implements HundredPointResultDao{
    
     @Autowired
 private SessionFactory sessionFactory;
       @Transactional(readOnly = false)
    @Override
    public void save(HundredPointResult hundredPointResult) {
        sessionFactory.getCurrentSession().save(hundredPointResult);
    }
}
