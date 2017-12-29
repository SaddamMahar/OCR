package com.primeid.service;

import com.google.gson.Gson;
import com.primeid.model.Artifact;
import com.primeid.model.ArtifactType;
import com.primeid.model.Audit;
import com.primeid.model.Case;
import com.primeid.model.DocumentType;
import com.primeid.model.SessionTable;
import com.primeid.model.UploadArtifactJson;
import com.primeid.utils.Constants;
import com.primeid.utils.CustomFunctions;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.image.FileImageSource;
import sun.awt.image.JPEGImageDecoder;

/**
 *
 * @author hashim
 */
@Service("uploadService")
public class UploadService {

    private static final long EXPIRY_DURATION = 1;

    @Autowired
    CustomAuthenticationService customAuthenticationService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private SessionTableService sessionTableService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private ArtifactTypeService artifactTypeService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private CustomFunctions customFunctions;

    Audit audit = new Audit();
    ArtifactType artifactType = new ArtifactType();
    DocumentType documentType = new DocumentType();
    Map response = new HashMap<String, String>();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map uploadArtifact(String token, String caseID, boolean hasFile, HttpServletRequest request, MultipartFile file, String json) {

        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime expiry = ldt.plusHours(EXPIRY_DURATION);
        Gson gson = new Gson();
        String headers = "token : " + token + " , caseID: " + caseID + " , hasFile : " + hasFile;
        response = customAuthenticationService.ipAuthentication("", request, headers);
        SessionTable tokenTable = new SessionTable();

        audit = customFunctions.setAuditDetails("", headers, request);
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
        response = customFunctions.validateToken(token);
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        String requestToken = gson.fromJson(token, String.class);
        tokenTable = sessionTableService.loadSessionTableByTokenExpiry(requestToken);
        tokenTable.setExpiry(expiry.format(dtf));
        tokenTable.setLastConnection(ldt.format(dtf));
        sessionTableService.update(tokenTable);
        audit.setAccounts(tokenTable.getAccounts());
        response = customFunctions.validateCaseID(caseID, tokenTable.getAccounts().getAccountID());
        if (response != null) {
            audit.setResponse(gson.toJson(response));
            auditService.save(audit);
            return response;
        }
        Case caseTable = caseService.loadCaseByCaseID(Long.parseLong(caseID));

        Artifact artifact = new Artifact();

        response = uploadFile(file, json, artifact, hasFile, caseTable, tokenTable);
        audit.setResponse(gson.toJson(response));
        auditService.save(audit);
        return response;

    }

    public Map uploadFile(MultipartFile file, String json, Artifact artifact, boolean hasFile, Case caseTable, SessionTable tokenTable) {
        LocalDateTime ldt = LocalDateTime.now();
        Gson gson = new Gson();

        try {
            UploadArtifactJson artifactJson = gson.fromJson(json.replaceAll("\\\\", "/"), UploadArtifactJson.class);

            artifact.setMeta(gson.toJson(artifactJson.getMeta()));
            if (!artifactJson.getDoc_type().contains("7")) {

                artifact.setFeature_map(gson.toJson(artifactJson.getOcr_map()));

            }
            try {
                List<Artifact> artifactList = artifactService.loadByCaseIDAndDocumentTypeID(caseTable.getCaseID(), Long.parseLong(artifactJson.getDoc_type()));
                if (!artifactList.isEmpty()) {
                    return response = customFunctions.updateTokenResponse("421", Constants.Errors.TWENTYONE.toString(), false);
                }
            } catch (Exception e) {
                return response = customFunctions.updateTokenResponse("421", Constants.Errors.TWENTYONE.toString(), false);

            }
            ldt = ldt.plusHours(EXPIRY_DURATION);
            tokenTable.setExpiry(ldt.format(dtf));
            sessionTableService.update(tokenTable);

            if (artifactJson.getFile_type() != null) {
                try {
                    artifact.setArtifactTypes(artifactTypeService.loadByArtifactTypeID(Long.parseLong(artifactJson.getFile_type())));
                } catch (Exception e) {
                    return response = customFunctions.updateTokenResponse("417", Constants.Errors.SEVENTEEN.toString(), false);
                }
            }
            if (artifactJson.getDoc_type() != null) {
                try {
                    artifact.setDocumentType(documentTypeService.loadByDocumentTypeID(Long.parseLong(artifactJson.getDoc_type())));
                } catch (Exception e) {
                    return response = customFunctions.updateTokenResponse("421", Constants.Errors.TWENTYONE.toString(), false);
                }
            }

            if (hasFile) {

                if (artifactJson == null || artifactJson.getFile_type() == null || artifactJson.getDoc_type() == null || artifactJson.getMeta().getName() == null
                        || artifactJson.getMeta().getSize() == null) {

                    return response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
                } else if (!(artifactJson.getDoc_type().contains("7")) && artifactJson.getOcr_map().isEmpty()) {
                    return response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
                }
                if (file.isEmpty()) {
                    return response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
                } else if (!file.getOriginalFilename().split("\\.")[0].equalsIgnoreCase(artifactJson.getMeta().getName())) {
                    return response = customFunctions.updateTokenResponse("416", Constants.Errors.SIXTEEN.toString(), false);
                } else if (file.getSize() != Long.parseLong(artifactJson.getMeta().getSize())) {
                    return response = customFunctions.updateTokenResponse("411", Constants.Errors.ELEVEN.toString(), false);
                }

                String path = System.getProperty("catalina.base") + "\\kycengine_artifacts\\uploads\\" + file.getOriginalFilename();
                path = path.replaceAll("\\\\", "/");

                artifact.setRepositoryRef(path);
                File nfile = new File(path);
                try {
                    JPEGImageDecoder decoder = new JPEGImageDecoder(new FileImageSource(nfile.getAbsolutePath()), new FileInputStream(nfile.getAbsolutePath()));
                    decoder.produceImage();

                } catch (Exception e) {
                    nfile.exists();
                    nfile.delete();
                    OutputStream os = new FileOutputStream(path);
                    os.write(Base64.decode(new String(file.getBytes(), "ISO-8859-1")));
                    os.flush();
                    os.close();
                }

                artifact.setCases(caseTable);
                artifact.setCreated(ldt.now().format(dtf));

                artifactService.save(artifact);
                return response = customFunctions.updateArtifactResponse(artifact.getArtifactID() + "", tokenTable.getExpiry(), true);

//            } else if (customFunctions.validateHTTP_URI(artifactJson.getLocation())) {
            } else if (true) {
                artifact.setRepositoryRef(artifactJson.getLocation());
            } else {
                return response = customFunctions.updateTokenResponse("414", Constants.Errors.FOURTEEN.toString(), false);
            }

            artifact.setCases(caseTable);
            artifact.setCreated(ldt.now().format(dtf));
            artifactService.save(artifact);

        } catch (Exception e) {
            return response = customFunctions.updateTokenResponse("403", Constants.Errors.THREE.toString(), false);
        }
        return response = customFunctions.updateArtifactResponse(artifact.getArtifactID() + "", tokenTable.getExpiry(), true);
    }
}
