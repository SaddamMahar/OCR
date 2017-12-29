package com.primeid.response;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Saddam Hussain
 */
public class ProcessCaseResponse {
    private List<FacialRecCaseResult> facial_rec_results;
    private List<Map> validation_results;
    private List<ArtifactValidationResults> dvs_results;

    public List<FacialRecCaseResult> getFacial_rec_results() {
        return facial_rec_results;
    }

    public void setFacial_rec_results(List<FacialRecCaseResult> facial_rec_results) {
        this.facial_rec_results = facial_rec_results;
    }

    public List<Map> getValidation_results() {
        return validation_results;
    }

    public void setValidation_results(List<Map> validation_results) {
        this.validation_results = validation_results;
    }

    public List<ArtifactValidationResults> getDvs_results() {
        return dvs_results;
    }

    public void setDvs_results(List<ArtifactValidationResults> dvs_results) {
        this.dvs_results = dvs_results;
    }
    
}
