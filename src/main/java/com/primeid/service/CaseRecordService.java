/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.service;

import com.primeid.dao.CaseRecordDao;
import com.primeid.model.CaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */
@Service("caseRecordService")
public class CaseRecordService {

    @Autowired
    private CaseRecordDao caseRecordDao;

    @Transactional(readOnly = true)
    public CaseRecord loadTokenByTokenCode(String tokenCode){
        CaseRecord caseRecord = null;
        caseRecord = caseRecordDao.findByTokenCode(tokenCode);
        return caseRecord;
    }
    
    public CaseRecord loadTokenByCaseID(String caseID){
        CaseRecord tokenObj = null;
        tokenObj = caseRecordDao.findByCaseID(caseID);
        return tokenObj;
    }
    
    public CaseRecord save(CaseRecord token){
        CaseRecord tokenObj = null;
        tokenObj = caseRecordDao.save(token);
        return tokenObj;
    }
    
    public CaseRecord update(CaseRecord token){
        CaseRecord tokenObj = null;
        tokenObj = caseRecordDao.update(token);
        return tokenObj;
    }

}