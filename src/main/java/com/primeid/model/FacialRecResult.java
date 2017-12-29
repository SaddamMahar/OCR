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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Saddam Hussain
 */
@Entity
@Table(name = "facialrecresults")
public class FacialRecResult implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private long facialRecResultsID;
    private Artifact artifact;
    private String facialRecResult;
    private String matchResult;
    private String created;
    private String connectionData;
    
    private Set<FacialRecArtifact> facialRecArtifact = new HashSet<FacialRecArtifact>(0);
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facialRecResultsID", nullable = false)
    public long getFacialRecResultsID() {
        return facialRecResultsID;
    }

    public void setFacialRecResultsID(long facialRecResultsID) {
        this.facialRecResultsID = facialRecResultsID;
    }
    
    @Column(name = "created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artifactID", referencedColumnName = "artifactID")
    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    @Column(name = "facialRecResult")
    public String getFacialRecResult() {
        return facialRecResult;
    }

    public void setFacialRecResult(String facialRecResult) {
        this.facialRecResult = facialRecResult;
    }

    @Column(name = "matchResult")
    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    @OneToMany(targetEntity = FacialRecArtifact.class, mappedBy = "facialRecResults", fetch = FetchType.EAGER)
    public Set<FacialRecArtifact> getFacialRecArtifact() {
        return facialRecArtifact;
    }

    public void setFacialRecArtifact(Set<FacialRecArtifact> facialRecArtifact) {
        this.facialRecArtifact = facialRecArtifact;
    }

    @Column(name = "connectionData")
    public String getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(String connectionData) {
        this.connectionData = connectionData;
    }
    
}
