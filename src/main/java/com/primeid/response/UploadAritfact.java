package com.primeid.response;


/**
 *
 * @author Saddam Hussain
 */
public class UploadAritfact{
    private String artifactID;
    private String artifactTypeID;
    private String documentTypeID;
    private String meta;
    private String feature_map;
    private String created;

    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifactID) {
        if(artifactID == null)
            artifactID = "0.0";
        this.artifactID = artifactID;
    }

    public String getArtifactTypeID() {
        return artifactTypeID;
    }

    public void setArtifactTypeID(String artifactTypeID) {
        if(artifactTypeID == null)
            artifactTypeID = "0.0";
        this.artifactTypeID = artifactTypeID;
    }

    public String getDocumentTypeID() {
        return documentTypeID;
    }

    public void setDocumentTypeID(String documentTypeID) {
        if(documentTypeID == null)
            documentTypeID = "0.0";
        this.documentTypeID = documentTypeID;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        if(meta == null)
            meta = "";
        this.meta = meta;
    }

    public String getFeature_map() {
        return feature_map;
    }

    public void setFeature_map(String feature_map) {
        if(feature_map == null)
            feature_map = "";
        this.feature_map = feature_map;
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
