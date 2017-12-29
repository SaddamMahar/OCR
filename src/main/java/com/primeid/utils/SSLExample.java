/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;


/**
 *
 * @author Saddam Hussain
 */
public class SSLExample {

     public String getHttpResponseWithSSL() {
        String httpsURL = "https://sandpit.dvshub.com.au:19443/Bus/VerificationServiceBus.svc/Https";
        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {
                        SSLContext ctx = SSLContext.getInstance("tls");
                        TrustManager[] trustManagers = getTrustManagers("pkcs12", new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\Novigi\\dvs\\sandpit.dvshub.com.au-PIO1.p12")), "AztGEx4NpTScKFFs");
                        KeyManager[] keyManagers = getKeyManagers("pkcs12", new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\Novigi\\dvs\\sandpit.dvshub.com.au-PIO1.pfx")), "AztGEx4NpTScKFFs");
                        ctx.init(keyManagers, trustManagers, new SecureRandom());
                        SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());
            
                        ClientConnectionManager manager = httpClient.getConnectionManager();
                        manager.getSchemeRegistry().register(new Scheme("https", 443, factory));
//                        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, null).

//            String keyPassphrase = "AztGEx4NpTScKFFs";
//
//            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//            keyStore.load(new FileInputStream("C:\\Users\\Administrator\\Desktop\\Novigi\\dvs\\sandpit\\sandpit.dvshub.com.au-PIO1.pfx"), keyPassphrase.toCharArray());
//
//            SSLContext sslContext = SSLContexts.custom()
//                    .loadKeyMaterial(keyStore, null)
//                    .build();

            HttpClient httpClientt = HttpClients.custom().setSslcontext(ctx).build();
            
            URL obj = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setSSLSocketFactory(ctx.getSocketFactory());

            String urlParameters = "Accept-Encoding=gzip, deflate&Expect=100-continue&Content-Type=application/soap+xml; charset=utf-8&Host=sandpit.dvshub.com.au:19443";

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + httpsURL);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
//            String    responseRequest = response.body().string();

            //as before
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected static KeyManager[] getKeyManagers(String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyStorePassword.toCharArray());
        return kmf.getKeyManagers();
    }

    protected static TrustManager[] getTrustManagers(String trustStoreType, InputStream trustStoreFile, String trustStorePassword) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(trustStoreType);
        trustStore.load(trustStoreFile, trustStorePassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        return tmf.getTrustManagers();
    }
}
