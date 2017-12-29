
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

@Entity
@Table(name="facialreccases")
public class FacialRecCase implements Serializable{
    private long facialRecCaseID;
    private Case caseID;
    private FacialRecResult facialRecResult;
    private String results;
    private String created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facialRecCaseID", nullable = false)
    public long getFacialRecCaseID() {
        return facialRecCaseID;
    }

    public void setFacialRecCaseID(long facialRecCaseID) {
        this.facialRecCaseID = facialRecCaseID;
    }

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caseID",referencedColumnName = "caseID")
    public Case getCaseID() {
        return caseID;
    }

    
    public void setCaseID(Case caseID) {
        this.caseID = caseID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facialRecResultID",referencedColumnName = "facialRecResultsID")
 
        public FacialRecResult getFacialRecResult() {
        return facialRecResult;
    }

    public void setFacialRecResult(FacialRecResult facialRecResult) {
        this.facialRecResult = facialRecResult;
    }
    

    @Column(name="results")
    public String getResults() {
        return results;
    }



    public void setResults(String results) {
        this.results = results;
    }

    @Column(name="created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
    
    
}
