package com.primeid.daoImpl;

import com.primeid.dao.TransactionAuditDao;
import com.primeid.model.TransactionAudit;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */

@Repository("TransactionAuditDao")
public class TransactionAuditDaoImpl implements TransactionAuditDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public List<TransactionAudit> findByAccountCode(String accountCode) {

        List<TransactionAudit> transactions;

        transactions = sessionFactory.getCurrentSession()
                .createQuery("from TransactionAudit where accountCode=?")
                .setParameter(0, accountCode)
                .list();

        if (transactions.size() > 0) {
            return transactions;
        } else {
            return null;
        }

    }
    @Transactional
    @Override
    public List<TransactionAudit> findByTokenCode(String tokenCode) {

        List<TransactionAudit> transactions;

        transactions = sessionFactory.getCurrentSession()
                .createQuery("from TransactionAudit where tokenCode=?")
                .setParameter(0, tokenCode)
                .list();

        if (transactions.size() > 0) {
            return transactions;
        } else {
            return null;
        }

    }
    @Transactional
    @Override
    public List<TransactionAudit> findByIP(String ip) {

        List<TransactionAudit> transactions;

        transactions = sessionFactory.getCurrentSession()
                .createQuery("from TransactionAudit where ip=?")
                .setParameter(0, ip)
                .list();

        if (transactions.size() > 0) {
            return transactions;
        } else {
            return null;
        }

    }
    
    @Transactional(readOnly = false)
    @Override
    public void save(TransactionAudit transactionAudit) {
        sessionFactory.getCurrentSession().save(transactionAudit);
    }
}
