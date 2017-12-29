/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.service;

import com.primeid.dao.NameThresholdDao;
import com.primeid.model.NameThreshold;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author hashim
 */
@Service("nameThresholdService")
public class NameThresholdService {
     @Resource(name = "nameThresholdDao")
   private NameThresholdDao nameThresholdDao;
    public void save(NameThreshold nameThreshold) {
        nameThresholdDao.save(nameThreshold);
    }
    public NameThreshold loadByAccountID(long accountID)
    {
        return nameThresholdDao.findByAccountID(accountID);
        
    }
}
