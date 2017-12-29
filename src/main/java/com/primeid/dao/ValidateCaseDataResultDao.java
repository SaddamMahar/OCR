package com.primeid.dao;

import com.primeid.model.ValidateCaseDataResult;
import java.util.List;

public interface ValidateCaseDataResultDao {
    
    List<ValidateCaseDataResult> findByCaseID (long caseID);

    void save(ValidateCaseDataResult validateCaseDataResult);
    
}
