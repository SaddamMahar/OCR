package com.primeid.model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="documenttypes")
public class DocumentType implements Serializable {
    
    private long documentTypeID;
    private String documentType;
    private Set<Artifact> artifacts = new HashSet<Artifact>(0);
    private Set<ArtifactValidationResult> artifactValidationResults = new HashSet<ArtifactValidationResult>(0);
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentTypeID", nullable = false)

    public long getDocumentTypeID() {
        return documentTypeID;
    }

    public void setDocumentTypeID(long documentTypeID) {
        this.documentTypeID = documentTypeID;
    }

    @Column(name="documentType")
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    @OneToMany(targetEntity = Artifact.class, mappedBy = "documentType", fetch = FetchType.EAGER)
    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
    
    @OneToMany(targetEntity = ArtifactValidationResult.class, mappedBy = "documentTypes", fetch = FetchType.EAGER)
    public Set<ArtifactValidationResult> getArtifactValidationResults() {
        return artifactValidationResults;
    }

    public void setArtifactValidationResults(Set<ArtifactValidationResult> artifactValidationResults) {
        this.artifactValidationResults = artifactValidationResults;
    }
    
    
}
