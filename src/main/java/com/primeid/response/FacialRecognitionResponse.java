package com.primeid.response;

import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public class FacialRecognitionResponse {
    private List<FacialRecCaseResult> results;

    public List<FacialRecCaseResult> getDetails() {
        return results;
    }

    public void setDetails(List<FacialRecCaseResult> details) {
        this.results = details;
    }
    
}
