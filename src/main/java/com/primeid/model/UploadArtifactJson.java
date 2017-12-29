package com.primeid.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public class UploadArtifactJson {
    private String doc_type;
    private String file_type;
    private String location;
    
    private Meta meta;
    private List<OcrMap> ocr_map = new ArrayList<>();

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

  

    public List<OcrMap> getOcr_map() {
        return ocr_map;
    }

    public void setOcr_map(List<OcrMap> ocr_map) {
        this.ocr_map = ocr_map;
    }
    
    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String type) {
        this.file_type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
    
}
