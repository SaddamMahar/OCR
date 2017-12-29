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
@Table(name="cases")
public class Case implements Serializable{
    private static final long serialVersionUID = 1L;

    private long caseID;
    private Account accounts;
    private Jurisdiction jurisdictions;
    private String created;
    
    private Set<Artifact> artifacts = new HashSet<Artifact>(0);
    private Set<ValidateCaseDataResult> validateCaseDataResults = new HashSet<ValidateCaseDataResult>(0);
    private Set<ArtifactValidationResult> artifactValidationResults = new HashSet<ArtifactValidationResult>(0);
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caseID", nullable = false)
    public long getCaseID() {
        return caseID;
    }

    public void setCaseID(long caseID) {
        this.caseID = caseID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID", nullable = false,referencedColumnName = "accountID")
    public Account getAccounts() {
        return accounts;
    }

    public void setAccounts(Account accounts) {
        this.accounts = accounts;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jurisdictionID", nullable = false,referencedColumnName = "jurisdictionID")
    public Jurisdiction getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(Jurisdiction jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

    @Column (name ="created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
    @OneToMany(targetEntity = Artifact.class, mappedBy = "cases", fetch = FetchType.EAGER)
    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
    
    @OneToMany(targetEntity = ValidateCaseDataResult.class, mappedBy = "cases", fetch = FetchType.EAGER)
    public Set<ValidateCaseDataResult> getValidateCaseDataResults() {
        return validateCaseDataResults;
    }

    public void setValidateCaseDataResults(Set<ValidateCaseDataResult> validateCaseDataResults) {
        this.validateCaseDataResults = validateCaseDataResults;
    }

    @OneToMany(targetEntity = ArtifactValidationResult.class, mappedBy = "cases", fetch = FetchType.EAGER)
    public Set<ArtifactValidationResult> getArtifactValidationResults() {
        return artifactValidationResults;
    }

    public void setArtifactValidationResults(Set<ArtifactValidationResult> artifactValidationResults) {
        this.artifactValidationResults = artifactValidationResults;
    }
}
