package com.primeid.utils;

import com.google.gson.Gson;
import com.primeid.model.Artifact;
import com.primeid.model.Audit;
import com.primeid.model.Case;
import com.primeid.service.ArtifactService;
import com.primeid.service.ArtifactTypeService;
import com.primeid.service.AuditService;
import com.primeid.service.CaseService;
import com.primeid.service.SessionTableService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Saddam Hussain
 */
public class CustomFunctions {

    @Autowired
    private CaseService caseService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private SessionTableService sessionTableService;

    @Autowired
    private ArtifactTypeService artifactTypeService;

    Gson gson = new Gson();
    Audit audit = new Audit();
    HashMap<String, String> response = new HashMap<String, String>();

    public HashMap<String, String> validateCaseID(String caseID, long accountID) {
        try {
            if (caseID == null) {
                response = updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
                return response;
            }
            long caseid = Long.parseLong(caseID);
            if (caseService.loadCaseByCaseID(caseid) != null) {
                Case caseTabel = caseService.loadCaseByCaseID(caseid);
                if (caseTabel.getAccounts().getAccountID() == accountID) {
                    return null;
                }
            }
        } catch (Exception e) {
            response = updateTokenResponse("408", Constants.Errors.EIGHT.toString(), false);
            return response;
        }

        response = updateTokenResponse("408", Constants.Errors.EIGHT.toString(), false);
        return response;
    }

    public HashMap<String, String> validateFileInformation(String fileExtension) {
        try {
            if (fileExtension == null) {
                response = updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
                return response;
            }
            if ( artifactTypeService.loadByArtifactCode(fileExtension) != null) {
                    return null;
            }
        } catch (Exception e) {
            response = updateTokenResponse("410", Constants.Errors.TEN.toString(), false);
            return response;
        }

        response = updateTokenResponse("410", Constants.Errors.TEN.toString(), false);
        return response;
    }

    public HashMap<String, String> validateArtifactKeyID(String artifactKey) {
        if (artifactKey == null) {
            response = updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        Artifact artifact = artifactService.loadArtifactByArtifactID(Long.parseLong(artifactKey));
        if (null == artifactKey || artifact.getArtifactID() == 0) {
            response = updateTokenResponse("412", Constants.Errors.TWELIVE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        return null;
    }

    public HashMap<String, String> validateToken(String token) {
        Gson gson = new Gson();
        HashMap<String, String> response = new HashMap<String, String>();

        try {
            String requestToken = gson.fromJson(token, String.class);
            if (null == requestToken) {
                response = updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
                return response;
            }
            if (null == sessionTableService.loadSessionTableByToken(requestToken)
                    || (!sessionTableService.loadSessionTableByToken(requestToken).getToken().equals(requestToken))) {

                response = updateTokenResponse("406", Constants.Errors.SIX.toString(), false);
                return response;
            } else if (null == sessionTableService.loadSessionTableByTokenExpiry(requestToken)) {
                response = updateTokenResponse("407", Constants.Errors.SEVEN.toString(), false);
                return response;
            }
        } catch (Exception e) {
            response = updateTokenResponse("406", Constants.Errors.SIX.toString(), false);
            return response;
        }

        return null;
    }


    public HashMap<String, String> updateTokenResponse(String status, String value, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            response.put("caseID", status);
            response.put("expiry", value);
        } else {
            response.put("status", status);
            response.put("details", value);
        }

        return response;
    }

    public HashMap<String, String> updateFacialRecognitionResponse(String status, String value, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            response.put("facialRecResultsID", status);
            response.put("facialRec_results", value);
        } else {
            response.put("status", status);
            response.put("details", value);
        }

        return response;
    }

    public HashMap<String, String> updateFaceRecongitionResponse(String status, String value, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            response.put("status", status);
            response.put("expiry", value);
        } else {
            response.put("status", status);
            response.put("details", value);
        }

        return response;
    }

    public HashMap<String, String> updateAccountCodeResponse(String status, String value, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            response.put("token", status);
            response.put("expiry", value);
        } else {
            response.put("status", status);
            response.put("details", value);
        }

        return response;
    }

    public HashMap<String, String> updateArtifactResponse(String status, String value, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            response.put("artifactKey", status);
            response.put("expiry", value);
        } else {
            response.put("status", status);
            response.put("details", value);
        }

        return response;
    }

    public HashMap<String, String> updateCaseResponse(String status, String value, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            response.put("caseID", status);
            response.put("expiry", value);
        } else {
            response.put("status", status);
            response.put("details", value);
        }

        return response;
    }
    
    public HashMap<String, String> updateValidateCaseData(String nameMatchResult, double nameMatchPoints,String hundredPointResult,long hundredPointScore, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            
            String responseData="[{nameMatchResult:\""+nameMatchResult+"\",nameMatchPoints:\""+nameMatchPoints+"\",hundredPointResult:\""+hundredPointResult+"\",hundredPointScore:\""+hundredPointScore+"\"}]";
            response.put("total_results",responseData); 
      
        } else {
            response.put("status", "425");
            response.put("deatail", "Invalid Output");
        }

        return response;
    }
    

    public HashMap<String, String> tamperCheckResponse(String tamperResultsID,String status, String value, String conclusionValue, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            
            String responseData="[{result:\""+status+"\",tamperScore:\""+value+"\",Conclusion:\""+conclusionValue+"\"}]";
            String bar = responseData.replaceAll("\\\\", "");
            response.put("tamperResultsID",tamperResultsID); 
            response.put("details",bar);
        } else {
            response.put("status", status);
            response.put("details", value);
        }

        return response;
    }

    public HashMap<String, String> updateProcessArtifactResponse(String status, String value, boolean check) {
        HashMap<String, String> response = new LinkedHashMap<>();

        if (check) {
            String[] values = status.split(",");
            for (int i = 0; i < values.length; i++) {
                String key = values[i].split(":")[0];
                String val = values[i].split(":")[1];

                response.put(key.replaceAll("[^A-Za-z0-9]", ""), val.replaceAll("[^A-Za-z0-9 ]", ""));
            }
            response.put("expiry", value);

        } else {
            String[] statusArray = status.split(",");           
            response.put("status", statusArray[0].split(":")[1].replaceAll("[^A-Za-z0-9 ]", ""));
            response.put("details", statusArray[1].split(":")[1].replaceAll("[^A-Za-z0-9 ]", ""));
        }

        return response;
    }


    public Audit setAuditDetails(String headers, String params, HttpServletRequest request) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        audit.setCreated(ldt.format(dtf));
        audit.setHeaderParams(headers);
        audit.setRequestParams(params);
        audit.setIp(request.getRemoteAddr());
        audit.setRequestURL(request.getRequestURI());
        return audit;
    }

    public String[] getHeaders(HttpServletRequest request) {

        String[] headers = new String[5];
        if (request.getHeader("token") != null) {
            headers[0] = request.getHeader("token");
        }
        if (request.getHeader("caseID") != null) {
            headers[1] = request.getHeader("caseID");
        }
        if (request.getHeader("hasFile") != null) {
            headers[2] = request.getHeader("hasFile");
        }
        return headers;
    }

    public static boolean validateHTTP_URI(String uri) {
//        String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator();
        if (urlValidator.isValid(uri)) {
            return true;
        } else {
            return false;
        }
    }

}
