package com.primeid.model;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Saddam Hussain
 */

@Entity
@Table(name="facialrec_artifacts")
public class FacialRecArtifact implements Serializable{

    @Id
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facialRecResultsID", nullable = false,referencedColumnName = "facialRecResultsID")
    private FacialRecResult facialRecResults;
    public FacialRecResult getFacialRecResults() {
        return facialRecResults;
    }

    public void setFacialRecResults(FacialRecResult facialRecResults) {
        this.facialRecResults = facialRecResults;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artifactID", nullable = false,referencedColumnName = "artifactID")
    private Artifact artifacts;
    public Artifact getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Artifact artifacts) {
        this.artifacts = artifacts;
    }
}
