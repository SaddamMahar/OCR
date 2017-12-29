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
 * @author hashim
 */
@Entity
@Table(name="tamperresults")
public class TamperResult implements Serializable
{
    private long id;
    private String tamperScore;
    private String tamperConclusion;
    private String tamperStatus;
    private String created;
    
    private Artifact artifacts;
    
    
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tamperResultsID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artifactID",referencedColumnName = "artifactID")
    public Artifact getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Artifact artifacts) {
        this.artifacts = artifacts;
    }
    
    @Column (name ="tamperScore")
    public String getTamperScore() {
        return tamperScore;
    }

    public void setTamperScore(String tamperScore) {
        this.tamperScore = tamperScore;
    }
   @Column (name ="tamperConclusion")
    public String getTamperConclusion() {
        return tamperConclusion;
    }

    public void setTamperConclusion(String tamperConclusion) {
        this.tamperConclusion = tamperConclusion;
    }
   @Column (name ="tamperStatus")
    public String getTamperStatus() {
        return tamperStatus;
    }

    public void setTamperStatus(String tamperStatus) {
        this.tamperStatus = tamperStatus;
    }
    @Column (name ="created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
    
}
