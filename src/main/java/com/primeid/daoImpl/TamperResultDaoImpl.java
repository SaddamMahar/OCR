package com.primeid.daoImpl;

import com.primeid.model.TamperResult;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.primeid.dao.TamperResultDao;
import java.util.List;

/**
 *
 * @author hashim
 */
@Repository(value = "tamperResultDao")
public class TamperResultDaoImpl implements TamperResultDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(TamperResult tamperResults) {
        sessionFactory.getCurrentSession().save(tamperResults);
    }
    @Transactional
    public TamperResult findByArtifactID(long artifactID) {
        try {
            List list = sessionFactory.getCurrentSession()
                    .createQuery("from TamperResult where artifactID=? ORDER BY created DESC")
                    .setParameter(0, artifactID)
                    .list();
            if (list != null && list.size() > 0) {
                return (TamperResult) list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
