package com.primeid.response;

import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public class AllCaseRelatedResult {
    private String caseID;
    private List<UploadAritfact> uploadAritfact;
    private List<ValidateCaseData> validateCaseData;
    private List<RunTamperCheck> runTamperCheck;
    private List<RunOCR> runOCR;
    private List<ArtifactValidationResults> artifactValidationResults;

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public List<UploadAritfact> getUploadAritfact() {
        return uploadAritfact;
    }

    public void setUploadAritfact(List<UploadAritfact> uploadAritfact) {
        this.uploadAritfact = uploadAritfact;
    }

    public List<ValidateCaseData> getValidateCaseData() {
        return validateCaseData;
    }

    public void setValidateCaseData(List<ValidateCaseData> validateCaseData) {
        this.validateCaseData = validateCaseData;
    }

    public List<RunTamperCheck> getRunTamperCheck() {
        return runTamperCheck;
    }

    public void setRunTamperCheck(List<RunTamperCheck> runTamperCheck) {
        this.runTamperCheck = runTamperCheck;
    }

    public List<RunOCR> getRunOCR() {
        return runOCR;
    }

    public void setRunOCR(List<RunOCR> runOCR) {
        this.runOCR = runOCR;
    }

    public List<ArtifactValidationResults> getArtifactValidationResults() {
        return artifactValidationResults;
    }

    public void setArtifactValidationResults(List<ArtifactValidationResults> artifactValidationResults) {
        this.artifactValidationResults = artifactValidationResults;
    }

}
