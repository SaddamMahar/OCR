package com.primeid.dao;

import com.primeid.model.DocumentType;

public interface DocumentTypeDao {
    
       DocumentType findByDocumentTypeID(long artifactTypeID);  
 //   DocumentType findByArtifactCode(String artifactCode);
}
