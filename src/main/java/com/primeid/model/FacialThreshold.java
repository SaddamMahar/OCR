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
@Table(name="facial_threshold")
public class FacialThreshold implements Serializable{
    private static final long serialVersionUID = 1L;
    
    
    private long id;
    private Account accounts;
    private long facialThresh;
    private String created;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facialThreshID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID", nullable = false,referencedColumnName = "accountID")
    public Account getAccounts() {
        return accounts;
    }

    @Column(name = "facialThresh")
    public long getFacialThresh() {
        return facialThresh;
    }

    public void setFacialThresh(long facialThresh) {
        this.facialThresh = facialThresh;
    }

    @Column(name = "lastModified")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setAccounts(Account accounts) {
        this.accounts = accounts;
    }
    
}