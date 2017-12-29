/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.dao;

import com.primeid.model.CaseRecord;

/**
 *
 * @author Saddam Hussain
 */

public interface CaseRecordDao {
    CaseRecord findByTokenCode (String tokenCode);
    CaseRecord findByCaseID (String caseID);
    CaseRecord save (CaseRecord caseRecord);
    CaseRecord update (CaseRecord caseRecord);
}
