package com.primeid.response;

import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public class ArtifactValidationDetails {
    private List<ArtifactValidationResults> details;

    public List<ArtifactValidationResults> getDetails() {
        return details;
    }

    public void setDetails(List<ArtifactValidationResults> details) {
        this.details = details;
    }
    
}
