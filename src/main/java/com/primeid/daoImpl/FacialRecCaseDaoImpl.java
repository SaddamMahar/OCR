
package com.primeid.daoImpl;
import com.primeid.dao.FacialRecCaseDao;
import com.primeid.model.FacialRecCase;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("facialRecCaseDao")
public class FacialRecCaseDaoImpl implements FacialRecCaseDao{
    
    @Autowired
    private SessionFactory sessionFactory;
    @Transactional(readOnly = false)
    @Override
    
    public FacialRecCase save(FacialRecCase facialRecCase) {
        sessionFactory.getCurrentSession().save(facialRecCase);
        return facialRecCase;
    }
    
    
}
