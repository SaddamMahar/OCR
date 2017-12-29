package com.primeid.service;

import com.primeid.dao.ArtifactDao;
import com.primeid.model.Artifact;
import com.primeid.model.DocumentType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */
@Service("artifactService")
public class ArtifactService {

    @Autowired
    private ArtifactDao artifactDao;

    @Transactional(readOnly = true)
    public List<Artifact> loadArtifactByArtifactCaseID(long artifactCaseID) {
        return artifactDao.findByArtifactCaseID(artifactCaseID);
    }
    @Transactional(readOnly = true)
    public List<Artifact> loadArtifactByArtifactTypeID(long artifactTypeID) {
        return artifactDao.findByArtifactTypeID(artifactTypeID);
    }
     @Transactional(readOnly = true)
    public List<DocumentType> loadArtifactByDocumentTypeID(long documentTypeID) {
        return artifactDao.findByDocumentTypeID(documentTypeID);
    }
    @Transactional(readOnly = true)
    public Artifact loadArtifactByArtifactID(long artifactID) {
        return artifactDao.findByArtifactID(artifactID);
    }
     @Transactional(readOnly = true)
    public List<Artifact> loadByCaseIDAndDocumentTypeID(long caseID,long documentTypeID){
        return artifactDao.findByCaseIDAndDocumentID(caseID, documentTypeID);
    }

    public void save(Artifact artifact) {
        artifactDao.save(artifact);
    }
}
