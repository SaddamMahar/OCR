package com.primeid.daoImpl;

import com.primeid.dao.DocumentTypeDao;
import com.primeid.model.DocumentType;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository("documentTypeDao")
public class DocumentTypeDaoImpl implements DocumentTypeDao{
     
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public DocumentType findByDocumentTypeID(long documentTypeID) {

        List<DocumentType> audits;

        audits = sessionFactory.getCurrentSession()
                .createQuery("from DocumentType where documentTypeID=?")
                .setParameter(0, documentTypeID)
                .list();

        if (audits.size() > 0) {
            return audits.get(0);
        } else {
            return null;
        }

    }
    
   
}
