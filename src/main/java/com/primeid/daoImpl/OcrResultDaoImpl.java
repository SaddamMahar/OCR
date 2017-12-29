package com.primeid.daoImpl;

import com.primeid.dao.AuditDao;
import com.primeid.dao.OcrResultDao;
import com.primeid.model.Audit;
import com.primeid.model.OcrResult;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("ocrResultDao")
public class OcrResultDaoImpl implements OcrResultDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = false)
    @Override
    public OcrResult save(OcrResult ocrResult) {
        try {
            sessionFactory.getCurrentSession().save(ocrResult);
            return ocrResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    @Transactional
    @Override
     public OcrResult findByArtifactID(long artifactID)
     {
         List list = sessionFactory.getCurrentSession()
                .createQuery("from OcrResult where artifactID=? ORDER BY created DESC")
                .setParameter(0, artifactID)
                .list();
        if (list != null && list.size() > 0) {
            return (OcrResult) list.get(0);
        }
        return null;
     }
    
}
