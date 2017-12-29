package com.primeid.service;

import com.primeid.dao.TransactionAuditDao;
import com.primeid.model.TransactionAudit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */

@Service("transactionAuditService")
public class TransactionAuditService {

    @Autowired
    private TransactionAuditDao transactionAuditDao;

    @Transactional(readOnly = true)
    public List<TransactionAudit> loadTransactionByAccountCode(String accountCode){
        return transactionAuditDao.findByAccountCode(accountCode);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionAudit> loadTransactionByTokenCode(String tokenCode){
        return transactionAuditDao.findByAccountCode(tokenCode);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionAudit> loadTransactionByIP(String ip){
        return transactionAuditDao.findByAccountCode(ip);
    }
    
    public void save(TransactionAudit userAudit) {
        transactionAuditDao.save(userAudit);
    }

}
