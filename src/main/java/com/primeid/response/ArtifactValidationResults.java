package com.primeid.response;

import com.primeid.model.ArtifactValidationResult;
import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public class ArtifactValidationResults {
    private String artifactID;
    private String documentTypeID;
    private String artifactValidationResults;

    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifactID) {
        if(artifactID == null)
            artifactID = "0.0";
        this.artifactID = artifactID;
    }

    public String getDocumentTypeID() {
        return documentTypeID;
    }

    public void setDocumentTypeID(String documentTypeID) {
        if(documentTypeID == null)
            documentTypeID = "0.0";
        this.documentTypeID = documentTypeID;
    }

    public String getArtifactValidationResults() {
        return artifactValidationResults;
    }

    public void setArtifactValidationResults(String artifactValidationResults) {
        if(artifactValidationResults == null)
            artifactValidationResults = "";
        this.artifactValidationResults = artifactValidationResults;
    }
}
