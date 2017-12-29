package com.primeid.service;

import com.primeid.dao.ValidateCaseDataResultDao;
import com.primeid.model.ValidateCaseDataResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("validateCaseDataResultService")
public class ValidateCaseDataResultService {

    @Autowired
    private ValidateCaseDataResultDao validateCaseDataResultDao;

    
//    @Transactional(readOnly = true)
//    @Transactional
    public List<ValidateCaseDataResult> loadByCaseID(long caseID) {
        return validateCaseDataResultDao.findByCaseID(caseID);
    }
    
    public void save(ValidateCaseDataResult validateCaseDataResult) {
        validateCaseDataResultDao.save(validateCaseDataResult);
    }
}
