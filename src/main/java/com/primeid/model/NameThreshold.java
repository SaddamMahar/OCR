/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name="name_threshold")
public class NameThreshold implements Serializable{
    private long id;
    private Account accounts;
    private double nameThresh;
    private String lastModified;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nameThresholdID", nullable = false)
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

    @Column (name ="nameThresh")
    public double getTamperThresh() {
        return nameThresh;
    }

    public void setTamperThresh(double tamperThresh) {
        this.nameThresh = tamperThresh;
    }

    @Column (name ="lastModified")
    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
    

    
    
}
