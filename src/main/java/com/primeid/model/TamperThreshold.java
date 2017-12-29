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
@Table(name="tamper_threshold")
public class TamperThreshold implements Serializable{
    private long id;
    private Account accounts;
    private double tamperThresh;
    private String lastModified;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tamperThreshID", nullable = false)
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

    public void setAccounts(Account accounts) {
        this.accounts = accounts;
    }

    @Column (name ="tamperThresh")
    public double getTamperThresh() {
        return tamperThresh;
    }

    public void setTamperThresh(double tamperThresh) {
        this.tamperThresh = tamperThresh;
    }

    @Column (name ="lastModified")
    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
    

    
    
}

