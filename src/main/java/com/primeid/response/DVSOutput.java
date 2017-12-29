package com.primeid.response;

/**
 *
 * @author Saddam Hussain
 */
public class DVSOutput {
    private String artifactID;
    private String documentTypeID;
    private String artifactValidationResults;

    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifactID) {
        this.artifactID = artifactID;
    }

    public String getDocumentTypeID() {
        return documentTypeID;
    }

    public void setDocumentTypeID(String documentTypeID) {
        this.documentTypeID = documentTypeID;
    }

    public String getArtifactValidationResults() {
        return artifactValidationResults;
    }

    public void setArtifactValidationResults(String artifactValidationResults) {
        this.artifactValidationResults = artifactValidationResults;
    }
    
}
