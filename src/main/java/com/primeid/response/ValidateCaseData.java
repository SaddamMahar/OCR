package com.primeid.response;


/**
 *
 * @author Saddam Hussain
 */
public class ValidateCaseData {
    private String nameMatchResult;
    private String nameMatchConnection;
    private String nameMatchResponse;
    private String hundredPointResult;
    private String hundredPointScore;
    private String created;

    public String getNameMatchResult() {
        return nameMatchResult;
    }

    public void setNameMatchResult(String nameMatchResult) {
        if(nameMatchResult == null)
            nameMatchResult = "";
        this.nameMatchResult = nameMatchResult;
    }

    public String getNameMatchConnection() {
        return nameMatchConnection;
    }

    public void setNameMatchConnection(String nameMatchConnection) {
        if(nameMatchConnection == null)
            nameMatchConnection = "";
        this.nameMatchConnection = nameMatchConnection;
    }

    public String getNameMatchResponse() {
        return nameMatchResponse;
    }

    public void setNameMatchResponse(String nameMatchResponse) {
        if(nameMatchResponse == null)
            nameMatchResponse = "";
        this.nameMatchResponse = nameMatchResponse;
    }

    public String getHundredPointResult() {
        return hundredPointResult;
    }

    public void setHundredPointResult(String hundredPointResult) {
        if(hundredPointResult == null)
            hundredPointResult = "0.0";
        this.hundredPointResult = hundredPointResult;
    }

    public String getHundredPointScore() {
        return hundredPointScore;
    }

    public void setHundredPointScore(String hundredPointScore) {
        if(hundredPointScore == null)
            hundredPointScore = "";
        this.hundredPointScore = hundredPointScore;
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
