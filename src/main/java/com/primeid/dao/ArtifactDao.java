package com.primeid.dao;

import com.primeid.model.Artifact;
import com.primeid.model.DocumentType;
import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public interface ArtifactDao {
    Artifact findByArtifactID (long artifactID);
    List<Artifact> findByArtifactTypeID (long artifactTypeID);
    List<DocumentType> findByDocumentTypeID (long documentTypeID);
    List<Artifact> findByArtifactCaseID (long caseID);
    List<Artifact> findByCaseIDAndDocumentID(long caseID, long documentTypeID);
    Artifact save (Artifact artifact);
}
