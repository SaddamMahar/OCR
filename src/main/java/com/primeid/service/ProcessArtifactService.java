package com.primeid.service;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Saddam Hussain
 */
public class ProcessArtifactService {

    public String sendUploadArtifactRequest(HttpHeaders headers, HttpServletRequest requestt, MultipartFile file, String json) {
        synchronized (this) {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(20, TimeUnit.MINUTES);
            String contentType = requestt.getContentType();
            String boundary = "--" + contentType.split("boundary=")[1];
            String endboundary = boundary + "--";
            String data = "";            
            String filedata = "";
            boolean hasFile = Boolean.valueOf(headers.get("hasFile").toString().replaceAll("[^A-Za-z0-9 ]", ""));
            if (headers.get("hasFile").contains("True")) {
                byte[] imageByteArray = null;
                try {
                    imageByteArray = file.getBytes();
//                byte[] imageByteArray = baos.toByteArray();
                } catch (IOException ex) {
                    Logger.getLogger(ProcessArtifactService.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                String base64ByteArray = Base64.encode(imageByteArray);
                data = boundary + "\r\nContent-Disposition: form-data; name=\"data\"\r\n\r\n" + json + "\r\n";
                filedata = boundary + "\r\nContent-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getOriginalFilename() + "\"\r\nContent-Type: image/jpeg\r\n\r\n\r\n" + base64ByteArray + "\r\n" + endboundary;
            }else{
                data = boundary + "\r\nContent-Disposition: form-data; name=\"data\"\r\n\r\n" + json + "\r\n"+endboundary;
            }

            MediaType mediaType = MediaType.parse(contentType);
            
            RequestBody body = RequestBody.create(mediaType, data + filedata);

            Request request = new Request.Builder()
                    .url("http://localhost:8085/PrimeID/uploadArtifact")
                    .post(body)
                    .addHeader("token", headers.get("token").toString().replaceAll("[^A-Za-z0-9]", ""))
                    .addHeader("caseID", headers.get("caseID").toString().replaceAll("[^A-Za-z0-9]", ""))
                    .addHeader("hasFile", hasFile+"")
                    .addHeader("content-type", contentType)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", headers.get("postman-token").toString())
                    .build();

            String responseRequest = "";
            try {
                Response response = client.newCall(request).execute();
                responseRequest = response.body().string();

            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProcessArtifactService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return responseRequest;
        }
    }

    public String sendTemperCheckRequest(String artifactID, String token) {
        synchronized (this) {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(20, TimeUnit.MINUTES);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, artifactID);
            Request request = new Request.Builder()
                    .url("http://localhost:8085/PrimeID/runTamperCheck")
                    .post(body)
                    .addHeader("token", token)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "98c3ef19-76bb-6581-c7a1-51f58c7ac1ae")
                    .build();

            String responseRequest = "";
            try {
                Response response = client.newCall(request).execute();
                responseRequest = response.body().string();
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProcessArtifactService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return responseRequest;
        }
    }

    public String sendOCRRequest(String artifactID, String token) {
        synchronized (this) {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(20, TimeUnit.MINUTES);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, artifactID);
            Request request = new Request.Builder()
                    .url("http://localhost:8085/PrimeID/runOCR")
                    .post(body)
                    .addHeader("token", token)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "98c3ef19-76bb-6581-c7a1-51f58c7ac1ae")
                    .build();

            String responseRequest = "";
            try {
                Response response = client.newCall(request).execute();
                responseRequest = response.body().string();
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProcessArtifactService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return responseRequest;
        }
    }

}
