/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.dao;

import com.primeid.model.TransactionAudit;
import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public interface TransactionAuditDao {
    List<TransactionAudit> findByAccountCode (String accountCode);
    List<TransactionAudit> findByTokenCode (String tokenCode);
    List<TransactionAudit> findByIP (String ip);
    public void save (TransactionAudit transactionAudit);
}
