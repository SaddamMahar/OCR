package com.primeid.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Saddam Hussain
 */

@Entity
@Table(name="artifactValidationResults")
public class ArtifactValidationResult implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private long artifactValidationResultID;
    private String artifactValidationResult;
    private String created;
    
    private Case cases;
    private Artifact artifacts;
    private DocumentType documentTypes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artifactValidationResultID", nullable = false)
    public long getArtifactValidationResultID() {
        return artifactValidationResultID;
    }

    public void setArtifactValidationResultID(long artifactValidationResultID) {
        this.artifactValidationResultID = artifactValidationResultID;
    }

    @Column(name = "artifactValidationResult")
    public String getArtifactValidationResult() {
        return artifactValidationResult;
    }

    public void setArtifactValidationResult(String artifactValidationResult) {
        this.artifactValidationResult = artifactValidationResult;
    }

    @Column(name = "created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artifactID",referencedColumnName = "artifactID")
    public Artifact getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Artifact artifacts) {
        this.artifacts = artifacts;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caseID",referencedColumnName = "caseID")
    public Case getCases() {
        return cases;
    }

    public void setCases(Case cases) {
        this.cases = cases;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentTypeID",referencedColumnName = "documentTypeID")
    public DocumentType getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(DocumentType documentTypes) {
        this.documentTypes = documentTypes;
    }
    
    
}
