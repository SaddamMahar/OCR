package com.primeid.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.optimaize.anythingworks.common.host.Host;
import com.optimaize.command4j.CommandExecutor;
import com.optimaize.command4j.Mode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import com.primeid.config.JwtTokenUtil;
import com.primeid.helper.ProcessCaseHelper;
import com.primeid.model.Account;
import com.primeid.model.Artifact;
import com.primeid.model.ArtifactKey;
import com.primeid.model.Audit;
import com.primeid.model.Case;
import com.primeid.model.IP;
import com.primeid.model.Jurisdiction;
import com.primeid.model.SessionTable;
import com.primeid.utils.Constants;
import com.primeid.utils.CustomFunctions;
import com.primeid.model.FacialRecArtifact;
import com.primeid.model.FacialRecCase;
import com.primeid.model.FacialRecResult;
import com.primeid.model.FacialThreshold;
import com.primeid.model.HundredPointResult;
import com.primeid.model.NameMatchResult;
import com.primeid.model.NameThreshold;
import com.primeid.model.OcrOutput;
import com.primeid.model.OcrResult;
import com.primeid.model.Score;
import com.primeid.model.TamperResult;
import com.primeid.model.TamperThreshold;
import com.primeid.model.ValidateCaseDataResult;
import com.primeid.response.AllCaseRelatedResult;
import com.primeid.response.ArtifactValidationDetails;
import com.primeid.response.ArtifactValidationResults;
import com.primeid.response.DVSResult;
import com.primeid.response.FacialRecCaseResult;
import com.primeid.response.FacialRecognitionResponse;
import com.primeid.response.ProcessCaseResponse;
import com.primeid.response.RunOCR;
import com.primeid.response.RunTamperCheck;
import com.primeid.response.UploadAritfact;
import com.primeid.response.ValidateCaseData;
import com.primeid.response.ValidateCaseDataResponse;
import com.primeid.response.ValidateListMap;
import com.primeid.utils.FaceRecognition;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.nameapi.client.lib.NameApiModeFactory;
import org.nameapi.client.lib.NameApiPortUrlFactory;
import org.nameapi.client.lib.NameApiRemoteExecutors;
import org.nameapi.client.services.matcher.personmatcher.PersonMatcherArgument;
import org.nameapi.client.services.matcher.personmatcher.PersonMatcherCommand;
import org.nameapi.ontology5.input.context.Context;
import org.nameapi.ontology5.input.context.ContextBuilder;
import org.nameapi.ontology5.input.context.Priority;
import org.nameapi.ontology5.input.entities.person.NaturalInputPerson;
import org.nameapi.ontology5.input.entities.person.NaturalInputPersonBuilder;
import org.nameapi.ontology5.input.entities.person.name.builder.NameBuilders;
import org.nameapi.ontology5.services.matcher.personmatcher.PersonMatcherResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *
 * @author Saddam Hussain
 */
@Service("customAuthenticationService")
public class CustomAuthenticationService {

    private static final long EXPIRY_DURATION = 1;
    @Autowired
    private IPService ipService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SessionTableService sessionTableService;
    @Autowired
    private CaseService caseService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private JurisdictionService jurisdictionService;
    @Autowired
    private CustomFunctions customFunctions;
    @Autowired
    private ArtifactService artifactService;
    @Autowired
    private FacialRecResultService facialRecResultService;
    @Autowired
    private FacialRecCaseService facialRecCaseService;
    @Autowired
    private FacialRecArtifactService facialRecArtifactService;
    @Autowired
    private ImageForgeryService imageForgeryService;
    @Autowired
    private FacialThresholdService facialThresholdService;
    @Autowired
    private TamperThresholdService tamperThreshService;
    @Autowired
    private TamperResultService tamperResultService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private OcrResultService ocrResultService;
    @Autowired
    private NameThresholdService nameThresholdService;
    @Autowired
    private NameMatchResultService nameMatchResultService;
    @Autowired
    private HundredPointResultService hundredPointResultService;
    @Autowired
    private ValidateCaseDataResultService validateCaseDataResultService;

    Audit audit = new Audit();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Map response = new HashMap<String, String>();
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    String[] headerKeys = {"token", "caseID", "hasFile"};

    Document resultDoc;

    public Map ipAuthentication(String params, HttpServletRequest request, String headers) {
        try {

            IP ipObj = ipService.loadIPByIP(request.getRemoteAddr());
            if (ipObj.isActive()) {
                return null;
            }
            audit = customFunctions.setAuditDetails(headers, params, request);
            audit.setAccounts(ipObj.getAccounts());
            response = customFunctions.updateAccountCodeResponse("402", Constants.Errors.TWO.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        } catch (Exception e) {
            audit = customFunctions.setAuditDetails(headers, params, request);
            response = customFunctions.updateAccountCodeResponse("402", Constants.Errors.TWO.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
    }

    public Map userAuthentication(String json, HttpServletRequest request) {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime expiry = ldt.plusHours(EXPIRY_DURATION);
        audit = customFunctions.setAuditDetails(json, "", request);
        try {
            Account account = gson.fromJson(json, Account.class);
            if (account.getAccountCode() != null && account.getAccountKey() != null) {
                Account accountDetail = accountService.loadAccountByAccountCode(account.getAccountCode());
                if (accountDetail != null) {
                    audit.setAccounts(accountDetail);
                    if (accountDetail.getAccountKey().equals(account.getAccountKey())) {
                        if (accountDetail.isAccessProduction()) {
                            SessionTable tokenObj = new SessionTable();
                            String token = null;

                            if (sessionTableService.loadSessionTableByAccountID(accountDetail.getAccountID()) == null) {
                                token = JwtTokenUtil.generateToken(accountDetail);
                                tokenObj.setAccounts(accountDetail);
                                tokenObj.setToken(token);
                                tokenObj.setExpiry(expiry.format(dtf));
                                tokenObj.setLastConnection(ldt.format(dtf));
                                sessionTableService.save(tokenObj);
                                response = customFunctions.updateAccountCodeResponse(tokenObj.getToken(), ldt.format(dtf), true);
                                audit.setResponse(gson.toJson(response));
                                auditService.save(audit);
                                return response;
                            }
                            tokenObj = sessionTableService.loadSessionTableByAccountID(accountDetail.getAccountID());
                            tokenObj.setExpiry(expiry.format(dtf));
                            tokenObj.setLastConnection(ldt.format(dtf));
                            sessionTableService.update(tokenObj);
                            response = customFunctions.updateAccountCodeResponse(tokenObj.getToken(), ldt.format(dtf), true);
                            audit.setResponse(gson.toJson(response));
                            auditService.save(audit);
                            return response;
                        } else {
                            response = customFunctions.updateAccountCodeResponse("405", Constants.Errors.FIVE.toString(), false);
                            audit.setResponse(gson.toJson(response));
                            auditService.save(audit);
                            return response;
                        }
                    } else {
                        response = customFunctions.updateAccountCodeResponse("404", Constants.Errors.FOUR.toString(), false);
                        audit.setResponse(gson.toJson(response));
                        auditService.save(audit);
                        return response;
                    }
                }
                response = customFunctions.updateAccountCodeResponse("404", Constants.Errors.FOUR.toString(), false);
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
            } else {
                response = customFunctions.updateAccountCodeResponse("403", Constants.Errors.THREE.toString(), false);
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
            }

        } catch (Exception e) {
            response = customFunctions.updateAccountCodeResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
    }

    public Map tokenAuthentication(String tokenCode, String json, String jurisdictionID, HttpServletRequest request) {
        LocalDateTime ldtNow = LocalDateTime.now();
        Map response;
        Audit transactionAudit;
        Gson gson = new Gson();
        transactionAudit = customFunctions.setAuditDetails("token : " + tokenCode, json.replaceAll("\\s", ""), request);
        if (!jurisdictionID.isEmpty()) {

            Jurisdiction jurisdiction = jurisdictionService.loadJurisdictionByJurisdictionID(Long.parseLong(jurisdictionID));
            try {
                String requestToken = gson.fromJson(tokenCode, String.class);

                if (jurisdiction != null) {

                    if (sessionTableService.loadSessionTableByTokenExpiry(requestToken) == null
                            || (!sessionTableService.loadSessionTableByTokenExpiry(requestToken).getToken().equals(requestToken))) {

                        response = customFunctions.updateTokenResponse("406", Constants.Errors.SIX.toString(), false);
                        transactionAudit.setResponse(gson.toJson(response));
                        auditService.save(transactionAudit);
                        return response;
                    }

                    SessionTable token = sessionTableService.loadSessionTableByToken(requestToken);
                    if (token != null) {

                        Case caseRecord = new Case();
                        caseRecord.setAccounts(token.getAccounts());
                        caseRecord.setJurisdictions(jurisdiction);
                        caseRecord.setCreated(LocalDateTime.now().format(dtf));
                        caseRecord = caseService.save(caseRecord);
                        ldtNow = ldtNow.plusHours(EXPIRY_DURATION);
                        token.setExpiry(ldtNow.format(dtf));
                        token.setLastConnection(LocalDateTime.now().format(dtf));
                        sessionTableService.update(token);
                        response = customFunctions.updateTokenResponse(Long.toString(caseRecord.getCaseID()), ldtNow.format(dtf), true);
                        transactionAudit.setResponse(gson.toJson(response));
                        auditService.save(transactionAudit);
                        return response;
                    } else {
                        response = customFunctions.updateTokenResponse("407", Constants.Errors.SEVEN.toString(), false);
                        transactionAudit.setResponse(gson.toJson(response));
                        auditService.save(transactionAudit);
                        return response;
                    }

                } else {
                    response = customFunctions.updateAccountCodeResponse("413", Constants.Errors.THIRTEEN.toString(), false);
                    transactionAudit.setResponse(gson.toJson(response));
                    auditService.save(transactionAudit);
                    return response;
                }
            } catch (Exception e) {
                response = customFunctions.updateAccountCodeResponse("406", Constants.Errors.SIX.toString(), false);
                transactionAudit.setResponse(gson.toJson(response));
                auditService.save(transactionAudit);
                return response;
            }

        } else {
            response = customFunctions.updateAccountCodeResponse("403", Constants.Errors.THREE.toString(), false);
            transactionAudit.setResponse(gson.toJson(response));
            auditService.save(transactionAudit);
            return response;
        }
    }

    public Map facialRecognition(String json, HttpServletRequest request,long caseID) {
        Map response;
        FacialRecCaseResult facialRecCaseResult = new FacialRecCaseResult();
        StringBuilder result = new StringBuilder();
        List<Artifact> documentList = new ArrayList<Artifact>();
        FacialRecognitionResponse facialRecognitionResponse = new FacialRecognitionResponse();
        List<FacialRecognitionResponse> facialRecognitionResponseList = new ArrayList<>();
        List<FacialRecCaseResult> facialRecCaseResultList = new ArrayList<>();
        LocalDateTime ldtNow = LocalDateTime.now();
        Gson gson = new Gson();
        Audit audit = new Audit();
        String artifactID1 = "";
        String artifactID2 = "";
        String pathOne="";
        String pathTwo="";
        HashMap<Integer, Artifact> artifactHashMap = new HashMap<>();
        String[] headerKeys = {"token", "caseID", "hasFile"};
        String[] headerValues = customFunctions.getHeaders(request);
        String headerParams = "";
        int count = 0;
        for (String value : headerValues) {
            if (value != null) {
                if (count == 0) {
                    headerParams = headerKeys[count] + " : " + value;
                } else {
                    headerParams += ", " + headerKeys[count] + " : " + value;
                }
                count++;
            }

        }
        SessionTable tokenTable = new SessionTable();

        audit.setCreated(ldtNow.format(dtf));
        audit.setHeaderParams(headerParams);
        audit.setIp(request.getRemoteAddr());
        audit.setRequestURL(request.getRequestURI().toString());

        response = ipAuthentication(json, request, headerParams);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = customFunctions.validateToken(headerValues[0]);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (json == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(headerValues[0]);
        LocalDateTime expiry = ldtNow.plusHours(EXPIRY_DURATION);
        tokenTable.setExpiry(expiry.format(dtf));
        sessionTableService.update(tokenTable);

        List<Artifact> artifactListByCaseID = artifactService.loadByCaseIDAndDocumentTypeID(caseID, 7);
        {
            if (artifactListByCaseID.isEmpty()) {
              response = customFunctions.updateArtifactResponse("426", Constants.Errors.TWENTYSIX.toString(), false);
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
                
            }
        }    
       
        Case caseTable = caseService.loadCaseByCaseID(caseID);
        try {
            List<Artifact> artifactList = artifactService.loadArtifactByArtifactCaseID(caseID);
            if (artifactList.size() < 2) {
           response = customFunctions.updateArtifactResponse("423", Constants.Errors.TWENTYTHREE.toString(), false);
                    audit.setResponse(gson.toJson(response));
                    auditService.save(audit);
                    return response;
        }

                    FaceRecognition faceRecognition = new FaceRecognition();
                    try {
                        FacialRecResult facialRecResult = new FacialRecResult();
                HashMap<String, String> responseDB = new HashMap<String, String>();
                        FacialThreshold facialThreshold = facialThresholdService.loadByAccountID(caseTable.getAccounts().getAccountID());
                        if (facialThreshold == null) {
                            facialThreshold = new FacialThreshold();
                            facialThreshold.setAccounts(caseTable.getAccounts());
                            facialThreshold.setFacialThresh(Double.valueOf(Constants.System.FACIAL_THRESHOLD.toString()).longValue());
                            facialThreshold.setCreated(dtf.format(LocalDateTime.now()));
                            facialThresholdService.save(facialThreshold);
                        }

                        Double facialThresh = Double.valueOf(facialThreshold.getFacialThresh());
                int index = 0;
                for (Artifact artifact : artifactList) {

                    if (artifact.getDocumentType().getDocumentTypeID() == 7) {
                        artifactHashMap.put(1, artifact);
                        pathOne = artifact.getRepositoryRef();
                        artifactID1 = Long.toString(artifact.getArtifactID());
                    } else {

                        documentList.add(index, artifact);
                        index++;

                        }
                        }
                for (Artifact artifact : documentList) {

                        artifactHashMap.put(2, artifact);
                        pathTwo = artifact.getRepositoryRef();
                        artifactID2 = Long.toString(artifact.getArtifactID());
                    responseDB = faceRecognition.compareFaces(facialThresh, artifactHashMap,pathOne,pathTwo,caseID);

                        facialRecResult.setCreated(dtf.format(LocalDateTime.now()));
                    facialRecResult.setConnectionData(gson.toJson(responseDB));
                    facialRecResult.setArtifact(artifact);
                    facialRecResult.setFacialRecResult(responseDB.get("CONFIDENCE"));
                    facialRecResult.setMatchResult(responseDB.get("MATCHED"));

                        facialRecResult = facialRecResultService.save(facialRecResult);

//                    FacialRecArtifact facialRecArtifact = new FacialRecArtifact();
//
//                    facialRecArtifact.setArtifacts(artifact);
//                    facialRecArtifact.setFacialRecResults(facialRecResult);
//                    facialRecArtifactService.save(facialRecArtifact);

                   FacialRecCase facialRecCase = new FacialRecCase();
                   facialRecCase.setCaseID(caseTable);
                   facialRecCase.setFacialRecResult(facialRecResult);
                   facialRecCase.setCreated(dtf.format(LocalDateTime.now()));
                   facialRecCase=facialRecCaseService.save(facialRecCase);


                    facialRecCaseResult.setArtifactID(artifactID2);
                    facialRecCaseResult.setFacialRecResultsID(facialRecResult.getFacialRecResultsID());
                    facialRecCaseResult.setFacialRec_results(responseDB.get("CONFIDENCE"));
                    facialRecCaseResult.setMatch_result(responseDB.get("MATCHED"));

                    facialRecCaseResultList = new ArrayList<>();
                    facialRecCaseResultList.add(facialRecCaseResult);
                    
                    facialRecognitionResponse.setDetails(facialRecCaseResultList);
                    facialRecognitionResponseList.add(facialRecognitionResponse);

                }

                response = new HashMap<String, String>();
                response.put("results", gson.toJson(facialRecognitionResponseList));
                        audit.setResponse(gson.toJson(response));
                        auditService.save(audit);

                    } catch (Exception e) {
                        e.printStackTrace();
                        response = customFunctions.updateTokenResponse("420", Constants.Errors.TWENTY.toString(), false);
                        audit.setResponse(gson.toJson(response));
                        auditService.save(audit);
                        return response;
                    }

                    return response;

        } catch (Exception e) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

    }

    public Map TamperCheck(String json, HttpServletRequest request) {
        TamperThreshold tamperThresh = new TamperThreshold();
        TamperResult tamperResults = new TamperResult();

        String scoreResult;

        String artifactKey;
        String tamperThreshold = Constants.System.TAMPER_THRESHOLD.toString();
        double score = Double.parseDouble(tamperThreshold);

        LocalDateTime ldtNow = LocalDateTime.now();
        Gson gson = new Gson();
        Audit audit;
        String[] headerValues = customFunctions.getHeaders(request);
        String headerParams = "";
        int count = 0;
        for (String value : headerValues) {
            if (value != null) {
                if (count == 0) {
                    headerParams = headerKeys[count] + " : " + value;
                } else {
                    headerParams += ", " + headerKeys[count] + " : " + value;
                }
                count++;
            }

        }
        try {

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            artifactKey = jsonObject.get("artifactKey").getAsString();

        } catch (Exception e) {
            artifactKey = "";
        }

        SessionTable tokenTable;

        audit = customFunctions.setAuditDetails(headerParams, json.replaceAll("\\s", ""), request);

        response = ipAuthentication(json, request, headerParams);

        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        response = customFunctions.validateToken(headerValues[0]);
        if (artifactKey == "") {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (json == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        long artifactid = Long.parseLong(artifactKey);

        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(headerValues[0]);
        LocalDateTime expiry = ldtNow.plusHours(EXPIRY_DURATION);
        tokenTable.setExpiry(expiry.format(dtf));
        sessionTableService.update(tokenTable);
        audit.setAccounts(tokenTable.getAccounts());
        try {
            tamperThresh = tamperThreshService.loadByAccountID(tokenTable.getAccounts().getAccountID());
            if (tamperThresh == null) {
                tamperThresh = new TamperThreshold();
                tamperThresh.setAccounts(tokenTable.getAccounts());
                tamperThresh.setLastModified("");
                tamperThresh.setTamperThresh(score);
                tamperThreshService.save(tamperThresh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Artifact artifactOne = artifactService.loadArtifactByArtifactID(artifactid);
            Case caseTabel = caseService.loadCaseByCaseID(artifactOne.getCases().getCaseID());
            audit.setAccounts(caseTabel.getAccounts());
            if (caseTabel.getAccounts().getAccountID() != tokenTable.getAccounts().getAccountID()) {
                response = customFunctions.updateTokenResponse("412", Constants.Errors.TWELIVE.toString(), false);
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
            }

        } catch (Exception e) {
            response = customFunctions.updateTokenResponse("412", Constants.Errors.TWELIVE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        try {
            ArtifactKey artifactkey = gson.fromJson(json, ArtifactKey.class);
            Artifact artifact = artifactService.loadArtifactByArtifactID(Long.parseLong(artifactkey.getArtifactKey()));
            File imageFile = new File(artifact.getRepositoryRef());
            if (imageFile.exists()) {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.flush();
                ImageIO.write(bufferedImage, imageFile.getName().split("\\.")[1], baos);
                byte[] imageByteArray = baos.toByteArray();
                String base64ByteArray = Base64.encode(imageByteArray);
                String id = imageForgeryService.submitImage(base64ByteArray);
                imageForgeryService.checkStatus(id);
                String xml = imageForgeryService.getResults(id);
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                Document doc = db.parse(is);
                Node envelope = imageForgeryService.getNodeValue("s:Envelope", doc.getChildNodes());
                Node body = imageForgeryService.getNodeValue("s:Body", envelope.getChildNodes());
                Node resultsResponse = imageForgeryService.getNodeValue("GetResultsResponse", body.getChildNodes());
                Node resultsResult = imageForgeryService.getNodeValue("GetResultsResult", resultsResponse.getChildNodes());
                String analysisResultsResponse = resultsResult.getFirstChild().getNodeValue();
                resultDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(analysisResultsResponse)));
                HashMap<String, String> resultNode = imageForgeryService.getNodeValues(resultDoc.getChildNodes());
                double finalscore = Double.parseDouble(resultNode.get("score"));
                Boolean validScore = false;
                if (tamperThresh != null && finalscore <= tamperThresh.getTamperThresh()) {
                    validScore = true;

                }
                if (validScore) {
                    scoreResult = "Pass";
                } else {
                    scoreResult = "Fail";
                }

                tamperResults.setArtifacts(artifact);
                tamperResults.setTamperConclusion(resultNode.get("conclusion"));
                tamperResults.setTamperScore(resultNode.get("score"));
                tamperResults.setTamperStatus(scoreResult);
                tamperResults.setCreated(LocalDateTime.now().format(dtf));
                tamperResultService.save(tamperResults);
                Long tamperResultsID = tamperResults.getId();
                String tamperResult = Long.toString(tamperResultsID);

                response = customFunctions.tamperCheckResponse(tamperResult, scoreResult, resultNode.get("score"), resultNode.get("conclusion"), true);
                //   String responsedata="tamperResultsID:"+tamperResultsID+",details:[ result:"+scoreResult+"tamperScore:"+resultNode.get("score")+"Conclusion"+resultNode.get("conclusion");

                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map processArtifact(HttpHeaders headers, HttpServletRequest request, MultipartFile file, String json) {
        LocalDateTime ldtNow = LocalDateTime.now();
        Gson gson = new Gson();
        Audit audit = new Audit();
        String[] headerValues = customFunctions.getHeaders(request);
        String headerParams = "";
        int count = 0;
        for (String value : headerValues) {
            if (value != null) {
                if (count == 0) {
                    headerParams = headerKeys[count] + " : " + value;
                } else {
                    headerParams += ", " + headerKeys[count] + " : " + value;
                }
                count++;
            }
        }
        SessionTable tokenTable;

        audit.setCreated(ldtNow.format(dtf));
        audit.setHeaderParams(headerParams);
        audit.setIp(request.getRemoteAddr());
        audit.setRequestURL(request.getRequestURI().toString());

        if (headerValues.length < 3) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = ipAuthentication(json, request, headerParams);
        if (!(response == null)) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = customFunctions.validateToken(headerValues[0]);
        if (!(response == null)) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (json == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(headerValues[0]);
        response = customFunctions.validateCaseID(headerValues[1], tokenTable.getAccounts().getAccountID());
        if (!(response == null)) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = customFunctions.validateFileInformation(file.getOriginalFilename().split("\\.")[1]);
        if (!(response == null)) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        ProcessArtifactService pas = new ProcessArtifactService();

        String uploadArtifactResponse = pas.sendUploadArtifactRequest(headers, request, file, json);
        String artifactID = "";
        if (uploadArtifactResponse.contains("artifactKey")) {
            String[] uploadArtifactResponseArray = uploadArtifactResponse.split(",");
            for (int i = 0; i < uploadArtifactResponseArray.length; i++) {
                if (uploadArtifactResponseArray[i].contains("artifactKey")) {
                    artifactID = uploadArtifactResponseArray[i] + "}";
                }
            }
        } else {
            response = customFunctions.updateProcessArtifactResponse(uploadArtifactResponse, "", false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        String temperCheckResponse = pas.sendTemperCheckRequest(artifactID, headerValues[0]);
        String ocrResponse = pas.sendOCRRequest(artifactID, headerValues[0]);

        String ocrResultID = "";
        if (ocrResponse.contains("ocrResultsID")) {
            String[] ocrResultArray = ocrResponse.split(",");
            for (int i = 0; i < ocrResultArray.length; i++) {
                if (ocrResultArray[i].contains("ocrResultsID")) {
                    ocrResultID = ocrResultArray[i] + "}";
                }
            }
        } else {
            response = customFunctions.updateProcessArtifactResponse(ocrResponse, "", false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        String tamperResultsID = "";
        if (temperCheckResponse.contains("tamperResultsID")) {
            String[] temperCheckResponseArray = temperCheckResponse.split(",");
            for (int i = 0; i < temperCheckResponseArray.length; i++) {
                if (temperCheckResponseArray[i].contains("tamperResultsID")) {
                    tamperResultsID = temperCheckResponseArray[i] + "}";
                }
            }
        } else {
            response = customFunctions.updateProcessArtifactResponse(temperCheckResponse, "", false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        LocalDateTime expiry = ldtNow.plusHours(EXPIRY_DURATION);
        tokenTable.setExpiry(expiry.format(dtf));
        sessionTableService.update(tokenTable);

        response = customFunctions.updateProcessArtifactResponse(artifactID + "," + ocrResultID + "," + tamperResultsID, expiry.format(dtf), true);

        audit.setResponse(gson.toJson(response));
        auditService.save(audit);
        return response;
    }

    public Map validateCaseData(String token, String json, HttpServletRequest request) {
        long totalScore = 0;
        long documenttype;
        HashMap<Integer, String> responseData = new HashMap();
        String nameResult = "";
        String responseString;
        long scores;
        long jurisdiction;
        double namePointResult = 0;
        String caseID;
        String hundredPointResult = "";
        String nameMatchResult = "";
        String responseResult = "";
        Score score;
        Case caseTable = null;
        OcrResult ocrResult;
        NameThreshold nameThreshold;
        NameMatchResult nameMatchResultTable = new NameMatchResult();
        HundredPointResult hundredPointResultTable = new HundredPointResult();
        ValidateCaseDataResult validateCaseDataResult = new ValidateCaseDataResult();
        LocalDateTime ldtNow = LocalDateTime.now();
        Gson gson = new Gson();
        Audit audit;
        String[] headerValues = customFunctions.getHeaders(request);
        String headerParams = "";
        int count = 0;
        for (String value : headerValues) {
            if (value != null) {
                if (count == 0) {
                    headerParams = headerKeys[count] + " : " + value;
                } else {
                    headerParams += ", " + headerKeys[count] + " : " + value;
                }
                count++;
            }

        }
        SessionTable tokenTable;
        audit = customFunctions.setAuditDetails(headerParams, json.replaceAll("\\s", ""), request);

        if (headerValues.length < 3) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = ipAuthentication(json, request, headerParams);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = new HashMap<String, String>();
        response = customFunctions.validateToken(headerValues[0]);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (json == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(headerValues[0]);
        LocalDateTime expiry = ldtNow.plusHours(EXPIRY_DURATION);
        tokenTable.setExpiry(expiry.format(dtf));
        sessionTableService.update(tokenTable);
        audit.setAccounts(tokenTable.getAccounts());
        try {

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            caseID = jsonObject.get("caseID").getAsString();

        } catch (Exception e) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        try {

            long caseNumber = Long.parseLong(caseID);
            caseTable = caseService.loadCaseByCaseID(caseNumber);

            List<Artifact> artifactList = artifactService.loadArtifactByArtifactCaseID(caseNumber);

            if (artifactList.size() < 2) {
                response = customFunctions.updateArtifactResponse("423", Constants.Errors.TWENTYTHREE.toString(), false);
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
            }
            int index = 1;
            for (Artifact artifact : artifactList) {
                documenttype = artifact.getDocumentType().getDocumentTypeID();
                score = scoreService.findByDocumentID(documenttype);

                try {

                    ocrResult = ocrResultService.findByArtifactID(artifact.getArtifactID());
                    if (ocrResult == null) {

                        response = customFunctions.updateArtifactResponse("425", Constants.Errors.TWENTYFIVE.toString(), false);
                        audit.setResponse(gson.toJson(response));
                        auditService.save(audit);
                        return response;

                    }
                    responseString = ocrResult.getResponseData();
                    responseData.put(index, responseString);
                    scores = score.getScore();
                    totalScore += scores;
                    index++;
                } catch (Exception e) {
                    response = customFunctions.updateArtifactResponse("424", Constants.Errors.TWENTYFOUR.toString(), false);
                    audit.setResponse(gson.toJson(response));
                    auditService.save(audit);
                    return response;
                }
            }

            jurisdiction = caseService.loadCaseByCaseID(caseNumber).getJurisdictions().getJurisdictionID();
            long jurisdictionscore = jurisdictionService.loadJurisdictionByJurisdictionID(jurisdiction).getScoreReq();
            if (totalScore > jurisdictionscore) {

                hundredPointResult = "Pass";

            } else {
                hundredPointResult = "Fail";
            }

            HashMap<Integer, String> responseHashMap = new HashMap<Integer, String>();
            int indexx = 1;
            for (Integer res : responseData.keySet()) {
                Pattern pattern = Pattern.compile("result([\\\\:\"]*)([^\\\\]*)");
                try {
                    Matcher matcher = pattern.matcher(responseData.get(res));
                    if (matcher.find()) {

                        responseHashMap.put(indexx, matcher.group(2));

                    }
                    indexx++;
                } catch (Exception e) {
                    response = customFunctions.updateTokenResponse("422", Constants.Errors.TWENTYTWO.toString(), false);
                    audit.setResponse(gson.toJson(response));
                    auditService.save(audit);
                    return response;

                }
            }

            if (responseHashMap.size() < 2) {
                response = customFunctions.updateTokenResponse("422", Constants.Errors.TWENTYTWO.toString(), false);
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;

            }

            Context context = new ContextBuilder()
                    .priority(Priority.REALTIME)
                    .build();
            CommandExecutor executor = NameApiRemoteExecutors.get();
            Mode mode = NameApiModeFactory.withContext(
                    "cf64e55cf9786dc606be894bd33d0c92-user1",
                    context,
                    new Host("rc50-api.nameapi.org", 80), NameApiPortUrlFactory.versionLatestStable()
            );
            int countt = 0;
            PersonMatcherCommand command = new PersonMatcherCommand();
            PersonMatcherResult result;
            if (responseHashMap.size() >= 2) {
                for (int i = 0; i < responseHashMap.size(); i++) {

                    if (responseResult == "") {

                        NaturalInputPerson person1 = new NaturalInputPersonBuilder().name(NameBuilders.western().fullname(responseHashMap.get(1)).build()).build();
                        NaturalInputPerson person2 = new NaturalInputPersonBuilder().name(NameBuilders.western().fullname(responseHashMap.get(2)).build()).build();
                        PersonMatcherArgument argument = new PersonMatcherArgument(person1, person2);
                        result = executor.execute(command, mode, argument).get();

                        Pattern pattern = Pattern.compile("points([\\=]*)([^\\,]*)");
                        responseResult = result.toString();
                        Matcher matcher = pattern.matcher(responseResult);
                        if (matcher.find()) {
                            nameResult = matcher.group(2);

                        }
                        namePointResult += Double.parseDouble(nameResult);
                        countt++;

                    } else if (responseResult != "" && responseHashMap.size() > 2) {
                        NaturalInputPerson person1 = new NaturalInputPersonBuilder().name(NameBuilders.western().fullname(responseHashMap.get(1)).build()).build();
                        NaturalInputPerson person2 = new NaturalInputPersonBuilder().name(NameBuilders.western().fullname(responseHashMap.get(3)).build()).build();
                        PersonMatcherArgument argument = new PersonMatcherArgument(person1, person2);
                        result = executor.execute(command, mode, argument).get();

                        Pattern pattern = Pattern.compile("points([\\=]*)([^\\,]*)");
                        responseResult = result.toString();
                        Matcher matcher = pattern.matcher(responseResult);
                        if (matcher.find()) {
                            nameResult = matcher.group(2);

                        }
                        namePointResult += Double.parseDouble(nameResult);
                        countt++;

                    }

                }
            }

            namePointResult *= 100;
            namePointResult = namePointResult / countt;

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            nameThreshold = nameThresholdService.loadByAccountID(caseTable.getAccounts().getAccountID());
            if (namePointResult > nameThreshold.getTamperThresh()) {
                nameMatchResult = "Pass";
            } else {
                nameMatchResult = "Fail";
            }
            nameMatchResultTable.setCaseID(caseTable.getCaseID());
            nameMatchResultTable.setConnectionData(responseResult);
            nameMatchResultTable.setResponseData(responseResult);
            nameMatchResultTable.setNameMatchResult(nameMatchResult);
            nameMatchResultTable.setCreated(LocalDateTime.now().format(dtf));

            nameMatchResultService.save(nameMatchResultTable);

            hundredPointResultTable.setCases(caseTable);
            hundredPointResultTable.setHundredPointResult(hundredPointResult);
            hundredPointResultTable.setHundredPointScore(totalScore);
            hundredPointResultTable.setCreated(LocalDateTime.now().format(dtf));

            hundredPointResultService.save(hundredPointResultTable);

            response = customFunctions.updateValidateCaseData(nameMatchResult, namePointResult, hundredPointResult, totalScore, true);

            validateCaseDataResult.setCases(caseTable);
            validateCaseDataResult.setNameMatchResult(nameMatchResult);
            validateCaseDataResult.setHundredPointScore(totalScore);
            validateCaseDataResult.setHundredPointResult(hundredPointResult);
            validateCaseDataResult.setConnectionData(json);
            validateCaseDataResult.setCreated(LocalDateTime.now().format(dtf));
            validateCaseDataResult.setResponseData(response.toString());

            validateCaseDataResultService.save(validateCaseDataResult);

            audit.setResponse(gson.toJson(response));
            auditService.save(audit);

        } catch (Exception e) {
            response = customFunctions.updateTokenResponse("408", Constants.Errors.EIGHT.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        return response;
    }

    public Map caseData(String token, String json, HttpServletRequest request) {
        Map response;
        LocalDateTime ldtNow = LocalDateTime.now();
        Gson gson = new Gson();
        Audit audit;
        Case caseTable;
        AllCaseRelatedResult allCaseRelatedResult = new AllCaseRelatedResult();
        UploadAritfact uploadArtifact;
        ValidateCaseData validateCaseData;
        RunTamperCheck runTamperCheck;
        RunOCR runOCR;
//        ArtifactValidationResults artifactValidationResults;// remaining
        
        
        List<UploadAritfact> uploadAritfactList = new ArrayList<>();
        List<ValidateCaseData> validateCaseDataList = new ArrayList<>();
        List<RunTamperCheck> runTamperCheckList = new ArrayList<>();
        List<RunOCR> runOCRList = new ArrayList<>();
//        List<ArtifactValidationResults> artifactValidationResultsList = new ArrayList<>(); // remaining
        List<Artifact> artifacts;
        List<TamperResult> tamperResults = new ArrayList<>();
        List<OcrResult> ocrResults = new ArrayList<>();
        List<ValidateCaseDataResult> validateCaseDataResults;

        String[] headerValues = customFunctions.getHeaders(request);
        String headerParams = "";
        String caseID = "";
        int count = 0;

        for (String value : headerValues) {
            if (value != null) {
                if (count == 0) {
                    headerParams = headerKeys[count] + " : " + value;
                } else {
                    headerParams += ", " + headerKeys[count] + " : " + value;
                }
                count++;
            }

        }
        SessionTable tokenTable;

        audit = customFunctions.setAuditDetails(headerParams, json.replaceAll("\\s", ""), request);
        response = ipAuthentication(json, request, headerParams);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (headerValues.length < 1) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (json == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        try {

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            caseID = jsonObject.get("caseID").getAsString();

        } catch (Exception e) {
            caseID = "";
        }
        if (caseID.equals("") || caseID == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        response = customFunctions.validateToken(headerValues[0]);
        if (!(response == null)) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(headerValues[0]);

        if(tokenTable != null && tokenTable.getAccounts() != null)
            audit.setAccounts(tokenTable.getAccounts());
        long caseNumber = Long.parseLong(caseID);
        try {
            caseTable = caseService.loadCaseByCaseID(caseNumber);
            response = customFunctions.validateCaseID(Long.toString(caseTable.getCaseID()), tokenTable.getAccounts().getAccountID());
            if (!(response == null)) {
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
            }
        } catch (Exception e) {        
            response = customFunctions.updateCaseResponse("408", Constants.Errors.EIGHT.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        try {
            artifacts = artifactService.loadArtifactByArtifactCaseID(caseTable.getCaseID());
        } catch (Exception e) {
            response = customFunctions.updateCaseResponse("426", Constants.Errors.TWENTYSIX.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        try {
            validateCaseDataResults = validateCaseDataResultService.loadByCaseID(caseTable.getCaseID());
        } catch (Exception e) {
            response = customFunctions.updateCaseResponse("427", Constants.Errors.TWENTYSEVEN.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        for (Artifact ar : artifacts) {
            uploadArtifact = new UploadAritfact();
            
            uploadArtifact.setArtifactID(Long.toString(ar.getArtifactID()));
            uploadArtifact.setArtifactTypeID(ar.getArtifactTypes() == null ? "0.0" :Long.toString(ar.getArtifactTypes().getArtifactTypeID()));
            uploadArtifact.setCreated(ar.getCreated());
            uploadArtifact.setDocumentTypeID((ar.getDocumentType() == null ? "0.0" : Long.toString(ar.getDocumentType().getDocumentTypeID())));
            uploadArtifact.setFeature_map(ar.getFeature_map());
            uploadArtifact.setMeta(ar.getMeta());
            
            uploadAritfactList.add(uploadArtifact);
            
            if (tamperResultService.findTamperResultByArtifactID(ar.getArtifactID()) != null) {
                tamperResults.add(tamperResultService.findTamperResultByArtifactID(ar.getArtifactID()));
            }
            if (ocrResultService.findByArtifactID(ar.getArtifactID()) != null) {
                ocrResults.add(ocrResultService.findByArtifactID(ar.getArtifactID()));
            }
        }

        if (tamperResults.isEmpty()) {
            response = customFunctions.updateCaseResponse("428", Constants.Errors.TWENTYEIGHT.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }

        if (ocrResults.isEmpty()) {
            response = customFunctions.updateCaseResponse("425", Constants.Errors.TWENTYFIVE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        for(TamperResult tamp:tamperResults){
            runTamperCheck = new RunTamperCheck();
            runTamperCheck.setArtifactID(tamp.getArtifacts() == null ? "0.0" : Long.toString(tamp.getArtifacts().getArtifactID()));
            runTamperCheck.setCreated(tamp.getCreated());
            runTamperCheck.setTamperConclusion(tamp.getTamperConclusion());
            runTamperCheck.setTamperScore(tamp.getTamperScore());
            runTamperCheck.setTamperStatus(tamp.getTamperStatus());
            runTamperCheckList.add(runTamperCheck);
        }
        for(OcrResult ocr:ocrResults){
            runOCR = new RunOCR();
            runOCR.setArtifactID(ocr.getArtifacts() == null ? "0.0" :Long.toString(ocr.getArtifacts().getArtifactID()));
            runOCR.setConnectionData(ocr.getConnectionData());
            runOCR.setCreated(ocr.getCreated());
            runOCR.setResponseData(ocr.getResponseData());
            runOCRList.add(runOCR);
        }
        for(ValidateCaseDataResult vcdr:validateCaseDataResults){
            validateCaseData = new ValidateCaseData();
            validateCaseData.setCreated(vcdr.getCreated());
            validateCaseData.setHundredPointResult(vcdr.getHundredPointResult());
            validateCaseData.setHundredPointScore(Long.toString(vcdr.getHundredPointScore()));
            validateCaseData.setNameMatchConnection(vcdr.getConnectionData());
            validateCaseData.setNameMatchResponse(vcdr.getResponseData());
            validateCaseData.setNameMatchResult(vcdr.getNameMatchResult());
            validateCaseDataList.add(validateCaseData);
        }
        
//        Extract runArtifactValidation connection d

        allCaseRelatedResult.setCaseID(caseID);
//        allCaseRelatedResult.setArtifactValidationResults(artifactValidationResultsList); 
        allCaseRelatedResult.setRunOCR(runOCRList);
        allCaseRelatedResult.setRunTamperCheck(runTamperCheckList);
        allCaseRelatedResult.setUploadAritfact(uploadAritfactList);
        allCaseRelatedResult.setValidateCaseData(validateCaseDataList);
                
        LocalDateTime expiry = ldtNow.plusHours(EXPIRY_DURATION);
        tokenTable.setExpiry(expiry.format(dtf));
        sessionTableService.update(tokenTable);
        audit.setAccounts(tokenTable.getAccounts());
        
        response = new HashMap<String, String>();
        response.put("allCaseRelatedResult", gson.toJson(allCaseRelatedResult));
        
        audit.setResponse(gson.toJson(response));
        auditService.save(audit);
        
        return response;
    }

    public Map getProcessCases(String token, String json, HttpServletRequest request) {
        LocalDateTime ldtNow = LocalDateTime.now();
        Gson gson = new Gson();
        Audit audit;
        Case caseTable;
        String[] headerValues = customFunctions.getHeaders(request);
        String headerParams = "";
        String caseID = "";
        int count = 0;
        for (String value : headerValues) {
            if (value != null) {
                if (count == 0) {
                    headerParams = headerKeys[count] + " : " + value;
                } else {
                    headerParams += ", " + headerKeys[count] + " : " + value;
                }
                count++;
            }

        }
        SessionTable tokenTable;
        audit = customFunctions.setAuditDetails(headerParams, json.replaceAll("\\s", ""), request);
        response = ipAuthentication(json, request, headerParams);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (headerValues.length < 1) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (json == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        try {

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            caseID = jsonObject.get("caseID").getAsString();

        } catch (Exception e) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = customFunctions.validateToken(headerValues[0]);
        if (!(response == null)) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(headerValues[0]);
        if(tokenTable != null && tokenTable.getAccounts() != null)
            audit.setAccounts(tokenTable.getAccounts());
        long caseNumber = Long.parseLong(caseID);
        try {
            caseTable = caseService.loadCaseByCaseID(caseNumber);
            response = customFunctions.validateCaseID(Long.toString(caseTable.getCaseID()), tokenTable.getAccounts().getAccountID());
            if (!(response == null)) {
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
            }
        } catch (Exception e) {        
            response = customFunctions.updateCaseResponse("408", Constants.Errors.EIGHT.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        ProcessCaseHelper processCaseHelper = new ProcessCaseHelper(); 
        
        
        try {
            ProcessCaseResponse processCaseResponse = new ProcessCaseResponse();
            
//            String validateCaseData = processCaseHelper.sendValidateCaseDataRequest(json, token, request);
//            String facialRecognition = processCaseHelper.sendFacialRecognitionRequest(json, token, request);
//            String artifactValidation = processCaseHelper.sendArtifactValidationRequest(json, token, request);
            
            String validateCaseData = "{\"total_results\":[{\"nameMatchResult\": \"Pass/Fail\",\"nameMatchPoints\": \"\"},{\"hundredPointResult\": \"Fail\",\"hundredPointScore\": \"70\"}]}";
            String facialRecognition = "{\"results\":[{\"artifactID\" : \"\", \"facialRecResultsID\": \"db.facialRecResults.facialRecResultsID\", facialRec_results\":\"numerical output from openFace\",\"match_result\" : \"Pass/Fail\"},{\"artifactID\" : \"\",\"facialRecResultsID\": \"db.facialRecResults.facialRecResultsID\",\"facialRec_results\": \"numerical output from openFace\",\"match_result\" : \"Pass/Fail\" } ]}";
            String artifactValidation = "{\"details\":[{\"artifactID\" : \"\",\"documentTypeID\" : \"\",\"artifactValidationResults\" : \"Pass or Fail\"},{\"artifactID\" : \"\", \"documentTypeID\" : \"\", \"artifactValidationResults\" : \"Pass or Fail\"},{\"artifactID\" : \"\", \"documentTypeID\" : \"\", \"artifactValidationResults\" : \"Pass or Fail\"}]}";
            
            ValidateListMap total_results = gson.fromJson(validateCaseData, new TypeToken<ValidateListMap>(){}.getType());
            FacialRecognitionResponse facialRecognitionResponse = gson.fromJson(facialRecognition, new TypeToken<FacialRecognitionResponse>(){}.getType());
            ArtifactValidationDetails artifactValidationDetails = gson.fromJson(artifactValidation, new TypeToken<ArtifactValidationDetails>(){}.getType());
            
            List<Map> listMap = new ArrayList<>();
            for(Map map:total_results.getTotal_results()){
                listMap.add(map);
            }
            
            List<ArtifactValidationResults> dvs_results = artifactValidationDetails.getDetails();
            List<FacialRecCaseResult> facial_rec_results = facialRecognitionResponse.getDetails();
            
            processCaseResponse.setDvs_results(dvs_results);
            processCaseResponse.setFacial_rec_results(facial_rec_results);
            processCaseResponse.setValidation_results(listMap);
            
            response = new HashMap<String, String>();
            response.put("result", gson.toJson(processCaseResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        LocalDateTime expiry = ldtNow.plusHours(EXPIRY_DURATION);
        tokenTable.setExpiry(expiry.format(dtf));
        sessionTableService.update(tokenTable);
        
        audit.setResponse(response.get("result").toString());
        auditService.save(audit);
        return response;
    }

    public Map getArtifactGetValidation(String token, String json, HttpServletRequest request) {
        LocalDateTime ldtNow = LocalDateTime.now();
        Gson gson = new Gson();
        Audit audit;
        Case caseTable;
        String[] headerValues = customFunctions.getHeaders(request);
        String headerParams = "";
        String caseID = "";
        int count = 0;
        for (String value : headerValues) {
            if (value != null) {
                if (count == 0) {
                    headerParams = headerKeys[count] + " : " + value;
                } else {
                    headerParams += ", " + headerKeys[count] + " : " + value;
                }
                count++;
            }

        }
        SessionTable tokenTable;
        audit = customFunctions.setAuditDetails(headerParams, json.replaceAll("\\s", ""), request);
        response = ipAuthentication(json, request, headerParams);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (headerValues.length < 1) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        if (json == null) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        try {

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            caseID = jsonObject.get("caseID").getAsString();

        } catch (Exception e) {
            response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        response = customFunctions.validateToken(headerValues[0]);
        if (!(response == null)) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(headerValues[0]);
        if(tokenTable != null && tokenTable.getAccounts() != null)
            audit.setAccounts(tokenTable.getAccounts());
        long caseNumber = Long.parseLong(caseID);
        try {
            caseTable = caseService.loadCaseByCaseID(caseNumber);
            response = customFunctions.validateCaseID(Long.toString(caseTable.getCaseID()), tokenTable.getAccounts().getAccountID());
            if (!(response == null)) {
                audit.setResponse(gson.toJson(response));
                auditService.save(audit);
                return response;
            }            
        } catch (Exception e) {        
            response = customFunctions.updateCaseResponse("408", Constants.Errors.EIGHT.toString(), false);
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        List<Artifact> artifacts = artifactService.loadArtifactByArtifactCaseID(caseNumber);
        String name = "";
        String dob = "";
        String number = "";
        for(Artifact art: artifacts){
            if(ocrResultService.findByArtifactID(art.getArtifactID()) != null){
                OcrResult ocr = ocrResultService.findByArtifactID(art.getArtifactID());
                String result = ocr.getResponseData();
                List<OcrOutput> outputOcr = gson.fromJson(result, new TypeToken<List<OcrOutput>>(){}.getType());
                for(OcrOutput output :outputOcr){
                    if(output.getRegion_name().equals("name")){
                        name = output.getResult();
                    }
                    if(output.getRegion_name().equals("dob")){
                        dob = output.getResult();
                    }
                    if(output.getRegion_name().equals("licNum")){
                        number = output.getResult();
                    }
                }
                name = name.replaceAll("\\\\n", "");
                dob = dob.replaceAll("\\\\n", "").replaceAll("[^0-9\\-]", "");
                number = number.replaceAll("\\\\n", "").replaceAll("[^0-9]", "");
                
            }                
        }
        
        LocalDateTime expiry = ldtNow.plusHours(EXPIRY_DURATION);
        tokenTable.setExpiry(expiry.format(dtf));
        sessionTableService.update(tokenTable);
        
        audit.setResponse(gson.toJson(response));
        auditService.save(audit);
        return response;
    }
}
