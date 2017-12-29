/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.service;

import com.primeid.dao.DocumentTypeDao;
import com.primeid.model.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 *
 * @author hashim
 */
@Service("documentTypeService")
public class DocumentTypeService {
       @Autowired
    private DocumentTypeDao documentTypeDao;
    
     @Transactional(readOnly = true)
    public DocumentType loadByDocumentTypeID(long documentTypeID){
        return documentTypeDao.findByDocumentTypeID(documentTypeID);
    }

    
}
