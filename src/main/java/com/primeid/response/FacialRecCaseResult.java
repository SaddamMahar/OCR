package com.primeid.response;

/**
 *
 * @author Saddam Hussain
 */
public class FacialRecCaseResult {
    private String artifactID;
    private long facialRecResultsID;
    private String facialRec_results;
    private String match_result;

    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifactID) {
        this.artifactID = artifactID;
    }

    public long getFacialRecResultsID() {
        return facialRecResultsID;
    }

    public void setFacialRecResultsID(long facialRecResultsID) {
        this.facialRecResultsID = facialRecResultsID;
    }

    public String getFacialRec_results() {
        return facialRec_results;
    }

    public void setFacialRec_results(String facialRec_results) {
        this.facialRec_results = facialRec_results;
    }

    public String getMatch_result() {
        return match_result;
    }

    public void setMatch_result(String match_result) {
        this.match_result = match_result;
    }
    
}
