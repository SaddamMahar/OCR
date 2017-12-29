package com.primeid.dao;

import com.primeid.model.TamperThreshold;

/**
 *
 * @author hashim
 */
public interface TamperThresholdDao {

    void save (TamperThreshold tamperThresh);

    TamperThreshold findByAccountID(long AccountID);
    
}
