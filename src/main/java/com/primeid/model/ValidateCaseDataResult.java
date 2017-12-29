
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
@Table(name="validatecasedataresults")
public class ValidateCaseDataResult implements Serializable {
    
    private long id;
    private Case cases;
    private String nameMatchResult;
    private String hundredPointResult;
    private long hundredPointScore;
    private String connectionData;
    private String responseData;
    private String created;

    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "validateCaseDataResultsID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caseID", nullable = false,referencedColumnName = "caseID")
    public Case getCases() {
        return cases;
    }

    public void setCases(Case cases) {
        this.cases = cases;
    }

    
    @Column(name="nameMatchResult")
    public String getNameMatchResult() {
        return nameMatchResult;
        
    }

    public void setNameMatchResult(String nameMatchResult) {
        this.nameMatchResult = nameMatchResult;
    }

    @Column(name="hundredPointScore")
    public long getHundredPointScore() {
        return hundredPointScore;
    }

    public void setHundredPointScore(long hundredPointScore) {
        this.hundredPointScore = hundredPointScore;
    }

    @Column(name="connectionData") 
    public String getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(String connectionData) {
        this.connectionData = connectionData;
    }
    
    @Column(name="responseData") 
    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    @Column(name="created") 
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

     @Column(name="hundredPointResult")
    public String getHundredPointResult() {
        return hundredPointResult;
    }

    public void setHundredPointResult(String hundredPointResult) {
        this.hundredPointResult = hundredPointResult;
    }
    
    
    
}
