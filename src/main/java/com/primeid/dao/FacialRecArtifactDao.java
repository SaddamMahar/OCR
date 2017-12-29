package com.primeid.dao;

import com.primeid.model.FacialRecArtifact;

/**
 *
 * @author Saddam Hussain
 */
public interface FacialRecArtifactDao {
    FacialRecArtifact findByArtifactID(long artifactID);
    FacialRecArtifact findByFacialRecResultID(long facialRecResultID);
    FacialRecArtifact save(FacialRecArtifact facialRecArtifact);
    
}
