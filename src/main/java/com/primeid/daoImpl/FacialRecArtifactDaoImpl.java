package com.primeid.daoImpl;

import com.primeid.dao.FacialRecArtifactDao;
import com.primeid.model.FacialRecArtifact;
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
@Repository("facialRecArtifactDao")
public class FacialRecArtifactDaoImpl implements FacialRecArtifactDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public FacialRecArtifact findByArtifactID(long artifactID) {
        List<FacialRecArtifact> facialRecArtifact;

        facialRecArtifact = sessionFactory.getCurrentSession()
                .createQuery("from FacialRecArtifact where artifactID=?")
                .setParameter(0, artifactID)
                .list();

        if (facialRecArtifact.size() > 0) {
            return facialRecArtifact.get(0);
        } else {
            return null;
        }
    }

    @Override
    public FacialRecArtifact findByFacialRecResultID(long facialRecResultID) {
        List<FacialRecArtifact> facialRecArtifact;

        facialRecArtifact = sessionFactory.getCurrentSession()
                .createQuery("from FacialRecArtifact where facialRecResultsID=?")
                .setParameter(0, facialRecResultID)
                .list();

        if (facialRecArtifact.size() > 0) {
            return facialRecArtifact.get(0);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = false)
    @Override
    public FacialRecArtifact save(FacialRecArtifact facialRecArtifact) {
        sessionFactory.getCurrentSession().save(facialRecArtifact);
        return facialRecArtifact;
    }
}
