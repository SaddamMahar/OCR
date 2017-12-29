package com.primeid.helper;

import com.primeid.service.ProcessArtifactService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Saddam Hussain
 */
public class ProcessCaseHelper {
    public String sendValidateCaseDataRequest(String json, String token, HttpServletRequest request) {
        synchronized (this) {
            String req = request.getRequestURL().toString().replaceAll(request.getRequestURI().toString(), request.getContextPath().toString().concat("/validateCaseData"));
            System.out.println(" req : "+req);
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(20, TimeUnit.SECONDS);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json);
            Request nRequest = new Request.Builder()
                    .url(req)
                    .post(body)
                    .addHeader("token", token)
                    .build();

            String responseRequest = "";
            try {
                Response response = client.newCall(nRequest).execute();
                responseRequest = response.body().string();
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProcessArtifactService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return responseRequest;
        }
    }

    public String sendFacialRecognitionRequest(String json, String token, HttpServletRequest request) {
        synchronized (this) {
            String req = request.getRequestURL().toString().replaceAll(request.getRequestURI().toString(), request.getContextPath().toString().concat("/runFacialRecognition"));
            System.out.println(" req : "+req);
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(20, TimeUnit.SECONDS);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json);
            Request nRequest = new Request.Builder()
                    .url(req)
                    .post(body)
                    .addHeader("token", token)
                    .build();

            String responseRequest = "";
            try {
                Response response = client.newCall(nRequest).execute();
                responseRequest = response.body().string();
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProcessArtifactService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return responseRequest;
        }
    }
    public String sendArtifactValidationRequest(String json, String token, HttpServletRequest request) {
        synchronized (this) {
            String req = request.getRequestURL().toString().replaceAll(request.getRequestURI().toString(), request.getContextPath().toString().concat("/runArtifactValidation"));
            System.out.println(" req : "+req);
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(20, TimeUnit.SECONDS);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json);
            Request nRequest = new Request.Builder()
                    .url(req)
                    .post(body)
                    .addHeader("token", token)
                    .build();

            String responseRequest = "";
            try {
                Response response = client.newCall(nRequest).execute();
                responseRequest = response.body().string();
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProcessArtifactService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return responseRequest;
        }
    }
}
