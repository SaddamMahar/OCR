/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.dao;

import com.primeid.model.Score;

/**
 *
 * @author hashim
 */

public interface ScoreDao {
    Score findByDocumentID(long documentID);
    
}
