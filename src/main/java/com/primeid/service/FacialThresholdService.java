package com.primeid.service;

import com.primeid.dao.FacialThresholdDao;
import com.primeid.model.FacialThreshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saddam Hussain
 */

@Service("facialThresholdService")
public class FacialThresholdService {
    @Autowired
    private FacialThresholdDao facialThresholdDao;
    
    public FacialThreshold loadByFacialThresholdID(long id){
        return facialThresholdDao.findByFacialThresID(id);
    }
    
    public FacialThreshold loadByAccountID(long id){
        return facialThresholdDao.findByAccountID(id);
    }
    
    public FacialThreshold save(FacialThreshold facialThreshold){
        return facialThresholdDao.save(facialThreshold);
    }
}
