package com.primeid.daoImpl;

import com.primeid.dao.ArtifactDao;
import com.primeid.model.Artifact;
import com.primeid.model.DocumentType;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */
@Repository("artifactDao")
public class ArtifactDaoImpl implements ArtifactDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public Artifact findByArtifactID(long artifactID) {

        List<Artifact> audits ;

        audits = sessionFactory.getCurrentSession()
                .createQuery("from Artifact where artifactID=?")
                .setParameter(0, artifactID)
                .list();

        if (audits.size() > 0) {
            return audits.get(0);
        } else {
            return null;
        }

    }

    @Transactional
    @Override
    public List<Artifact> findByArtifactTypeID(long artifactTypeID) {

        List<Artifact> audits ;

        audits = sessionFactory.getCurrentSession()
                .createQuery("from Artifact where artifactTypeID=?")
                .setParameter(0, artifactTypeID)
                .list();

        if (audits.size() > 0) {
            return audits;
        } else {
            return null;
        }

    }
    @Transactional
    @Override
    public List<DocumentType> findByDocumentTypeID(long documentTypeID) {

        List<DocumentType> audits;

        audits = sessionFactory.getCurrentSession()
                .createQuery("from Artifact where documentTypeID=?")
                .setParameter(0, documentTypeID)
                .list();

        if (audits.size() > 0) {
            return audits;
        } else {
            return null;
        }

    }
    
    @Transactional
    @Override
    public List<Artifact> findByArtifactCaseID(long caseID) {

        List<Artifact> audits;

        audits = sessionFactory.getCurrentSession()
                .createQuery("from Artifact where caseID=?")
                .setParameter(0, caseID)
                .list();

        if (audits.size() > 0) {
            return audits;
        } else {
            return null;
        }

    }
     @Transactional
    
    @Override
    public List<Artifact> findByCaseIDAndDocumentID(long caseID,long documentTypeID) {
        
        List<Artifact> audits;

      
        audits = sessionFactory.getCurrentSession()
                .createQuery("from Artifact where caseID=? AND documentTypeID=?")
                .setParameter(0, caseID)
                .setParameter(1, documentTypeID)               
                .list();

        if (audits.size() > 0) {
            return audits;
        } else {
            return null;
        }

    }
    
    @Transactional(readOnly = false)
    @Override
    public Artifact save(Artifact artifact) {
        sessionFactory.getCurrentSession().save(artifact);
        return artifact;
    }
}
