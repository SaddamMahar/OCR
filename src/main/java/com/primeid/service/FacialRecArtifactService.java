package com.primeid.service;

import com.primeid.dao.FacialRecArtifactDao;
import com.primeid.model.FacialRecArtifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saddam Hussain
 */
@Service("facialRecArtifactService")
public class FacialRecArtifactService {

    @Autowired
    private FacialRecArtifactDao facialRecArtifactDao;

    public FacialRecArtifact loadByArtifactID(long artifactID) {
        return facialRecArtifactDao.findByArtifactID(artifactID);
    }

    public FacialRecArtifact loadByFacialRecResultsID(long facialRecResultsID) {
        return facialRecArtifactDao.findByFacialRecResultID(facialRecResultsID);
    }

    public FacialRecArtifact save(FacialRecArtifact facialRecArtifact) {
        return facialRecArtifactDao.save(facialRecArtifact);
    }
}
