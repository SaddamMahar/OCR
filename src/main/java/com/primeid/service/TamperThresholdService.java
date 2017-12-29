package com.primeid.service;

import com.primeid.model.TamperThreshold;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.primeid.dao.TamperThresholdDao;

/**
 *
 * @author hashim
 */
@Service("tamperThreshService") 
public class TamperThresholdService {

   @Resource(name = "tamperThreshDao")
   private TamperThresholdDao tamperThreshDao;

    public void save(TamperThreshold tamperThresh) {
        tamperThreshDao.save(tamperThresh);
    }
    public TamperThreshold loadByAccountID(long accountID)
    {
        return tamperThreshDao.findByAccountID(accountID);
        
    }
    
}
