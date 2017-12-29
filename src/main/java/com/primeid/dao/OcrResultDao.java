package com.primeid.dao;

import com.primeid.model.OcrResult;

public interface OcrResultDao {
    public OcrResult save (OcrResult audit);
    public OcrResult findByArtifactID(long artifactID);
}
