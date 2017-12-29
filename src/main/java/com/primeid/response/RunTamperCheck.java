package com.primeid.response;

/**
 *
 * @author Saddam Hussain
 */
public class RunTamperCheck {
    private String artifactID;
    private String tamperScore;
    private String tamperConclusion;
    private String tamperStatus;
    private String created;

    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifactID) {
        if(artifactID == null)
            artifactID = "0.0"; 
        this.artifactID = artifactID;
    }

    public String getTamperScore() {
        return tamperScore;
    }

    public void setTamperScore(String tamperScore) {
        if(tamperScore == null)
            tamperScore = "";
        this.tamperScore = tamperScore;
    }

    public String getTamperConclusion() {
        return tamperConclusion;
    }

    public void setTamperConclusion(String tamperConclusion) {
        if(tamperConclusion == null)
            tamperConclusion = "";
        this.tamperConclusion = tamperConclusion;
    }

    public String getTamperStatus() {
        return tamperStatus;
    }

    public void setTamperStatus(String tamperStatus) {
        if(tamperStatus == null)
            tamperStatus = "";
        this.tamperStatus = tamperStatus;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        if(created == null)
            created = "";
        this.created = created;
    }
    
}
