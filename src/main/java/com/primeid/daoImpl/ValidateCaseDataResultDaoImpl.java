package com.primeid.daoImpl;

import com.primeid.dao.ValidateCaseDataResultDao;
import com.primeid.model.Artifact;
import com.primeid.model.ValidateCaseDataResult;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "validateCaseDataResultDao")
public class ValidateCaseDataResultDaoImpl implements ValidateCaseDataResultDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public List<ValidateCaseDataResult> findByCaseID(long caseID) {

        try {
            List<ValidateCaseDataResult> validateCaseDataResults;

        validateCaseDataResults = sessionFactory.getCurrentSession()
                .createQuery("from ValidateCaseDataResult where caseID=?")
                .setParameter(0, caseID)
                .list();

        if (validateCaseDataResults.size() > 0) {
            return validateCaseDataResults;
        } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        

    }
    
    @Transactional(readOnly = false)
    @Override
    public void save(ValidateCaseDataResult validateCaseDataResult) {
        sessionFactory.getCurrentSession().save(validateCaseDataResult);
    }
}
