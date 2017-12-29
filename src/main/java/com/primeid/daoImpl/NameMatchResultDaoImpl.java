
package com.primeid.daoImpl;

import com.primeid.dao.NameMatchResultDao;
import com.primeid.model.NameMatchResult;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "nameMatchResultDao")
public class NameMatchResultDaoImpl implements NameMatchResultDao {
      @Autowired
 private SessionFactory sessionFactory;
       @Transactional(readOnly = false)
    @Override
    public void save(NameMatchResult nameMatchResult) {
        sessionFactory.getCurrentSession().save(nameMatchResult);
    }
    
}
