package com.primeid.dao;

import com.primeid.model.FacialThreshold;

/**
 *
 * @author Saddam Hussain
 */
public interface FacialThresholdDao {
    FacialThreshold findByFacialThresID(long id);
    FacialThreshold findByAccountID(long id);
    FacialThreshold save(FacialThreshold facialThreshold) ;
}
