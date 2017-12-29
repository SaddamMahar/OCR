package com.primeid.dao;

import com.primeid.model.TamperResult;

/**
 *
 * @author hashim
 */

public interface TamperResultDao {
    public TamperResult findByArtifactID(long artifactID);
    void save (TamperResult tamperResults);
    
}
