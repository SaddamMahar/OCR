/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.dao;

import com.primeid.model.NameThreshold;

/**
 *
 * @author hashim
 */
public interface NameThresholdDao {
    
     NameThreshold findByAccountID(long AccountID);
     void save (NameThreshold nameThreshold);
}
