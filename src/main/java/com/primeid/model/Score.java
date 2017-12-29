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
@Table(name="scores")
public class Score implements Serializable{
    
    private long scoreID;
    private DocumentType documentType;
    private Jurisdiction jurisdication;
    private long score;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scoreID", nullable = false)
    public long getScoreID() {
        return scoreID;
    }

    
    public void setScoreID(long scoreID) {
        this.scoreID = scoreID;
    }

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentTypeID", nullable = false,referencedColumnName = "documentTypeID")
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jurisdictionID", nullable = false,referencedColumnName = "jurisdictionID")
    public Jurisdiction getJurisdication() {
        return jurisdication;
    }

    public void setJurisdication(Jurisdiction jurisdication) {
        this.jurisdication = jurisdication;
    }

    @Column(name="score")
    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
    
    
    
}
