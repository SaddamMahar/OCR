package com.primeid.response;

import com.primeid.model.OcrResult;
import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public class RunOCR {
    private String artifactID;
    private String connectionData;
    private String responseData;
    private String created;

    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifactID) {
        if(artifactID == null)
            artifactID = "0.0";
        this.artifactID = artifactID;
    }

    public String getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(String connectionData) {
        if(connectionData == null)
            connectionData = "";
        this.connectionData = connectionData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        if(responseData == null)
            responseData = "";
        this.responseData = responseData;
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
