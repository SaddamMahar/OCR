package com.primeid.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="namematchresults")
public class NameMatchResult implements Serializable{
    
    private long id;
    private long caseID;
    private String nameMatchResult;
    private String connectionData;
    private String responseData;
    private String created;
    
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    @Column(name = "caseID")
    public long getCaseID() {
        return caseID;
    }

    
    public void setCaseID(long caseID) {
        this.caseID = caseID;
    }
    @Column (name ="nameMatchResult")
    public String getNameMatchResult() {
        return nameMatchResult;
    }

    public void setNameMatchResult(String nameMatchResult) {
        this.nameMatchResult = nameMatchResult;
    }
   @Column (name ="connectionData")
    public String getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(String connectionData) {
        this.connectionData = connectionData;
    }
   @Column (name ="responseData")
    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
    @Column (name ="created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
}
