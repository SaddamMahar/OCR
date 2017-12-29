/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.service;

import com.primeid.dao.ScoreDao;
import com.primeid.model.DocumentType;
import com.primeid.model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author hashim
 */
public class ScoreService {
        @Autowired
    private ScoreDao scoreDao; 
        @Transactional(readOnly = true)
        
    public Score findByDocumentID(long documentTypeID){
        return scoreDao.findByDocumentID(documentTypeID);
    }

   
}
