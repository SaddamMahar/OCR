/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.daoImpl;

import com.primeid.dao.ScoreDao;
import com.primeid.model.Score;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author hashim
 */
@Repository("scoreDao")
public class ScoreDaoImpl implements ScoreDao {
         
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public Score findByDocumentID(long documentID)
    {
        
             List<Score> audits;

        audits = sessionFactory.getCurrentSession()
                .createQuery("from Score where documentTypeID=?")
                .setParameter(0, documentID)
                .list();

        
        if (audits.size() > 0) {
            return audits.get(0);
        } else {
            return null;
        }
        
    }
    
}
