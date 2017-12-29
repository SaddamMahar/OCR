package com.primeid.daoImpl;

import com.primeid.dao.CaseRecordDao;
import com.primeid.model.CaseRecord;
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
@Repository("caseRecordDao")
public class CaseRecordDaoImpl implements CaseRecordDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public CaseRecord findByTokenCode(String tokenCode) {

        List<CaseRecord> tokens;

        tokens = sessionFactory.getCurrentSession()
                .createQuery("from CaseRecord where tokenCode=?")
                .setParameter(0, tokenCode)
                .list();

        if (tokens.size() > 0) {
            return tokens.get(0);
        } else {
            return null;
        }

    }
    
    @Transactional
    public CaseRecord findByCaseID(String caseID) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from CaseRecord where caseID=?")
                .setParameter(0, caseID)
                .list();
        if(list != null && list.size() > 0){
            return (CaseRecord) list.get(0);
        }
		return null;
    }
    
    @Transactional(readOnly = false)
    public CaseRecord save(CaseRecord caseRecord) {
        sessionFactory.getCurrentSession().save(caseRecord);
        return findByTokenCode(caseRecord.getTokenCode());
    }
    
    @Transactional(readOnly = false)
    public CaseRecord update(CaseRecord caseRecord) {
        sessionFactory.getCurrentSession().update(caseRecord);
        return findByTokenCode(caseRecord.getTokenCode());
    }
}
