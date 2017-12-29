package com.primeid.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.primeid.service.CustomAuthenticationService;
import com.primeid.service.OCRService;
import com.primeid.service.UploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;

/**
 *
 * @author Saddam Hussain
 */
@Controller
public class LoginController {

    @Autowired
    private CustomAuthenticationService customAuthenticationService;

    @Autowired
    private OCRService ocrService;

    @Autowired
    private UploadService uploadService;

    @ResponseBody
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> authentication(@RequestBody(required = false) String json, HttpServletRequest request) {
        Map response;
        Gson gson = new Gson();
        response = customAuthenticationService.ipAuthentication(json, request, "");

        if (response != null) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }
        response = customAuthenticationService.userAuthentication(json, request);
        if (response.containsKey("token")) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @RequestMapping(value = "/initiateCase", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> initiateCase(@RequestHeader(value = "token") String token, @RequestBody String json, HttpServletRequest request) {
        Map response;
        Gson gson = new Gson();

        String jurisdictionID = null;
        try {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            jurisdictionID = jsonObject.get("jurisdiction").getAsString();
        } catch (Exception e) {
            jurisdictionID = "";
        }

        response = customAuthenticationService.ipAuthentication(token, request, "");
        if (response != null) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }
        response = customAuthenticationService.tokenAuthentication(token, json, jurisdictionID, request);
        if (response.containsKey("caseID")) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @RequestMapping(value = "/uploadArtifact", headers = "content-type=multipart/*", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> uploadArtifactTwo(@RequestHeader(value = "token", required = false) String token, @RequestHeader(value = "caseID", required = false) String caseID,
            @RequestHeader(value = "hasFile", required = false, defaultValue = "false") boolean hasFile,
            @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "data", required = false) String data, HttpServletRequest request)
            throws IOException, ServletException, FileUploadException {

        Gson gson = new Gson();
        Map response;
        response = uploadService.uploadArtifact(token, caseID, hasFile, request, file, data);

        if (response.containsKey("status")) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/runOCR", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runOCR(@RequestBody(required = false) String json, HttpServletRequest request) {

        Gson gson = new Gson();
        Map response;
        response = ocrService.OCR(json, request);
        if (response.containsKey("ocrResultsID")) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

       @ResponseBody
    @RequestMapping(value = "/runFacialRecognition", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runFacialRecognition(@RequestBody(required = false) String json, HttpServletRequest request) {
        Gson gson = new Gson();
        Map response;
         String caseID = null;
        try {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            caseID = jsonObject.get("caseID").getAsString();
        } catch (Exception e) {
            caseID = "";
        }
        response = customAuthenticationService.facialRecognition(json, request,Long.parseLong(caseID));
        if (response.containsKey("ocrResultsID")) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @RequestMapping(value = "/runTamperCheck", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runTamperCheck(@RequestBody(required = false) String json, HttpServletRequest request) {
        Gson gson = new Gson();
        Map response;
        response = customAuthenticationService.TamperCheck(json, request);
        try {
            if (response.containsKey("doc")) {
                return new ResponseEntity<>((response.get("doc")), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/processArtifact", headers = "content-type=multipart/*", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runProcessArtifact(@RequestHeader HttpHeaders headers, @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "data", required = false) String data, HttpServletRequest request)
            throws IOException, ServletException, FileUploadException {
        Gson gson = new Gson();
        Map response;
        response = customAuthenticationService.processArtifact(headers, request, file, data);

        if (response.containsKey("status")) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/validateCaseData", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> getValidationCaseData(@RequestHeader(value = "token", required = false) String token, @RequestBody String json, HttpServletRequest request)
            throws IOException, ServletException, FileUploadException {
        Gson gson = new Gson();
        Map response;
        
        response = customAuthenticationService.validateCaseData(token, json, request);

        if (response.containsKey("status")) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        
    }

    @ResponseBody
    @RequestMapping(value = "/getCaseData", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> getCaseData(@RequestHeader(value = "token", required = false) String token, @RequestBody String json, HttpServletRequest request)
            throws IOException, ServletException, FileUploadException {
        Gson gson = new Gson();
        Map response;
        
        response = customAuthenticationService.caseData(token, json, request);
        if (response.containsKey("allCaseRelatedResult")) {
            return new ResponseEntity<>(response.get("allCaseRelatedResult"), HttpStatus.OK);
        }

        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @RequestMapping(value = "/processCase", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> getProcessCase(@RequestHeader(value = "token", required = false) String token, @RequestBody String json, HttpServletRequest request)
            throws IOException, ServletException, FileUploadException {
        Gson gson = new Gson();
        Map response;
        response = customAuthenticationService.getProcessCases(token, json, request);
        if (response.containsKey("result")) {
            return new ResponseEntity<>(response.get("result"), HttpStatus.OK);
        }

        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @RequestMapping(value = "/runArtifactValidation", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> getArtifactValidation(@RequestHeader(value = "token", required = false) String token, @RequestBody String json, HttpServletRequest request)
            throws IOException, ServletException, FileUploadException {
        Gson gson = new Gson();
        Map response;
        response = customAuthenticationService.getArtifactGetValidation(token, json, request);
        if (response.containsKey("processCase")) {
            return new ResponseEntity<>(response.get("processCase"), HttpStatus.OK);
        }

        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

}
