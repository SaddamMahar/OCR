package com.primeid.response;

import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public class DVSResult {
    private String artifactValidationResultsID;
    private List<DVSOutput> details;

    public String getArtifactValidationResultsID() {
        return artifactValidationResultsID;
    }

    public void setArtifactValidationResultsID(String artifactValidationResultsID) {
        this.artifactValidationResultsID = artifactValidationResultsID;
    }

    public List<DVSOutput> getDetails() {
        return details;
    }

    public void setDetails(List<DVSOutput> details) {
        this.details = details;
    }
}
