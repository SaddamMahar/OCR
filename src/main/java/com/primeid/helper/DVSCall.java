package com.primeid.helper;

import java.io.BufferedInputStream;
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
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.commons.codec.binary.Base64;
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

public class DVSCall {

    public String getHttpResponseWithSSL() {
        System.out.println("---");
        String httpsURL = "https://sandpit.dvshub.com.au:19443/Bus/VerificationServiceBus.svc/Https";
        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSSSSS Z");
            String localDateTimeWithTimeZone = ZonedDateTime.now().format(formatter);

            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
//            System.out.println("data time local now: "+LocalDateTime.now().atZone(ZoneId.systemDefault()).toString());
            System.out.println("data time local now: "+LocalDateTime.now());
            System.out.println("data time local now+5 : "+LocalDateTime.now().plusMinutes(5));
            System.out.println("data time in utc now : "+LocalDateTime.now(Clock.systemUTC()));
            System.out.println("data time in utc now  +5: "+LocalDateTime.now(Clock.systemUTC()).plusMinutes(5));
            System.out.println("data time in zoned now  : "+ZonedDateTime.now());
            System.out.println("data time in zoned now  : "+ZonedDateTime.now());
            System.out.println("data time in nowUTC with zone offset : "+nowUTC);
            System.out.println("data time in nowUTC +5 : "+nowUTC.plusMinutes(5));

            String CA_FILE = "C:\\Users\\Administrator\\Desktop\\dvs\\original\\sandpit.dvshub.com.au-CA.cer";

            FileInputStream fis = new FileInputStream(CA_FILE);
            X509Certificate ca = (X509Certificate) CertificateFactory.getInstance(
                    "X.509").generateCertificate(new BufferedInputStream(fis));

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry(Integer.toString(1), ca);

            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            SSLContext ctx = SSLContext.getInstance("tls");
//            TrustManager[] trustManagers = getTrustManagers("pkcs12", new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\Novigi\\dvs\\sandpit.dvshub.com.au-PIO1.p12")), "AztGEx4NpTScKFFs");
            TrustManager[] trustManagers = tmf.getTrustManagers();

            KeyManager[] keyManagers = getKeyManagers("pkcs12", new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\dvs\\sandpit.dvshub.com.au-PIO1.pfx")), "AztGEx4NpTScKFFs");
            ctx.init(keyManagers, trustManagers, new SecureRandom());
            SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());

            ClientConnectionManager manager = httpClient.getConnectionManager();
            manager.getSchemeRegistry().register(new Scheme("https", 443, factory));

            InputStream pfxInputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\dvs\\sandpit.dvshub.com.au-PIO1.pfx");
            KeyStore ksa = KeyStore.getInstance("PKCS12");
            ksa.load(pfxInputStream, "AztGEx4NpTScKFFs".toCharArray());
            Enumeration<String> aliases = ksa.aliases();
            String aliaz = "";
            while (aliases.hasMoreElements()) {
                aliaz = aliases.nextElement();
                if (ksa.isKeyEntry(aliaz)) {
                    break;
                }
            }
            X509Certificate certificate = (X509Certificate) ksa.getCertificate(aliaz);
            Base64 base64 = new Base64();
            String tokena = base64.encodeToString(certificate.getEncoded());
            System.out.println("token |: "+tokena);
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
            con.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            con.setRequestProperty("Host", "sandpit.dvshub.com.au:19443");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            con.setSSLSocketFactory(ctx.getSocketFactory());

            String urlParameters = "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                    + " <s:Header>\n"
                    + " <a:Action s:mustUnderstand=\"1\">http://DVS/Common/2014/06/Contract/Service/Manager/IVerification/VerifyDocument</a:Action>\n"
                    + " <a:MessageID>urn:uuid:0ae2eb09-b382-4faa-955f-cc74abd8aa8f</a:MessageID>\n"
                    + " <ActivityId CorrelationId=\"87865644-b239-4f2d-8b80-c4ccde6af1d5\" xmlns=\"http://schemas.microsoft.com/2004/09/ServiceModel/Diagnostics\">1216bf69-fae4-40fc-8108-38a9a19bd371</ActivityId>\n"
                    + " <a:ReplyTo>\n"
                    + " <a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n"
                    + " </a:ReplyTo>\n"
                    + " <a:To s:mustUnderstand=\"1\" u:Id=\"_1\">https://sandpit.dvshub.com.au:19443/Bus/VerificationServiceBus.svc/Https</a:To>\n"
                    + " <o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n"
                    + " <u:Timestamp u:Id=\"_0\">\n"
                    + //" <u:Created>"+nowUTC+"</u:Created>\n" +
                    " <u:Created>" + LocalDateTime.now(Clock.systemUTC()) + "</u:Created>\n"
                    + " <u:Expires>" + LocalDateTime.now(Clock.systemUTC()).plusMinutes(5) + "</u:Expires>\n"
                    + //" <u:Expires>"+nowUTC.plusMinutes(5)+"</u:Expires>\n" +
                    " </u:Timestamp>\n"
                    + " <o:BinarySecurityToken>"+tokena+"</o:BinarySecurityToken>\n"
                    + " <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n"
                    + " <SignedInfo>\n"
                    + " <CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod>\n"
                    + " <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>\n"
                    + " <Reference URI=\"#_0\">\n"
                    + " <Transforms>\n"
                    + " <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>\n"
                    + " </Transforms>\n"
                    + " <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>\n"
                    + " <DigestValue>TIOFtxNKYxXWzoNnil71FYHhnjE=</DigestValue>\n"
                    + " </Reference>\n"
                    + " <Reference URI=\"#_1\">\n"
                    + " <Transforms>\n"
                    + " <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>\n"
                    + " </Transforms>\n"
                    + " <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>\n"
                    + " <DigestValue>L0svi1leABSauQO5jBfjIZplMCU=</DigestValue>\n"
                    + " </Reference>\n"
                    + " </SignedInfo>\n"
                    + " <SignatureValue>ltEdyh1Y0iIl58wu+qJMkkRJOaw0lLYFd607a9WToA5d6uIHEohhhVFpDooO2JB6AYHuvxs3/Fh83PQFKhN9TVFjvzg3cfL6fzKOyB+RIAW2XFw0GaV+ZuB+fP7KV8DWTHQJGtcqulZkwdLe9R9L2PyX0p1+hoHNcvwy7OdxgfrWJDcqGXBWc0oVjlOlQLjXC06cZwAjBYUpmin7EImBD8o1gz0D45uzmoJ8Q11ay7p0wErmeXvAAbuny4zek6UTaucWMvlEfxjml3m5OaDWocEne7P1xFHNTr4o9fkNCw/t75+awsF64IcqwPSsPozAWCnT/dhOVvrgp3M9R5VR0w==</SignatureValue>\n"
                    + " <KeyInfo>\n"
                    + " <o:SecurityTokenReference>\n"
                    + " <o:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#uuid-2759e946-14c1-469c-8ec7-35d5ea450f12-1\"></o:Reference>\n"
                    + " </o:SecurityTokenReference>\n"
                    + " </KeyInfo>\n"
                    + " </Signature>\n"
                    + " </o:Security>\n"
                    + " </s:Header>\n"
                    + " <s:Body>\n"
                    + " <VerifyDocument xmlns=\"http://DVS/Common/2014/06/Contract/Service/Manager\">\n"
                    + " <request i:type=\"b:MedicareRequest\" xmlns:b=\"http://DVS/Common/2014/06/Contract/Data/Manager\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                    + " <b:BirthDate i:nil=\"true\"></b:BirthDate>\n"
                    + " <b:DocumentTypeCode>MD</b:DocumentTypeCode>\n"
                    + " <b:FamilyName i:nil=\"true\"></b:FamilyName>\n"
                    + " <b:GivenName i:nil=\"true\"></b:GivenName>\n"
                    + " <b:OriginatingAgencyCode>PIO1</b:OriginatingAgencyCode>\n"
                    + " <b:RequestDateTime>" + localDateTimeWithTimeZone + "</b:RequestDateTime>\n"
                    + //" <b:RequestDateTime>"+LocalDateTime.now(Clock.systemUTC())+"</b:RequestDateTime>\n" +
                    " <b:VerificationRequestNumber>0123456789</b:VerificationRequestNumber>\n"
                    + " <b:VersionNumber>1</b:VersionNumber>\n"
                    + " <b:CardExpiry>2017-07</b:CardExpiry>\n"
                    + " <b:CardNumber>1234567890</b:CardNumber>\n"
                    + " <b:CardType>G</b:CardType>\n"
                    + " <b:FullName1>SAMPLE FULLNAME</b:FullName1>\n"
                    + " <b:FullName2 i:nil=\"true\"></b:FullName2>\n"
                    + " <b:FullName3 i:nil=\"true\"></b:FullName3>\n"
                    + " <b:FullName4 i:nil=\"true\"></b:FullName4>\n"
                    + " <b:IndividualRefNumber>1</b:IndividualRefNumber>\n"
                    + " </request>\n"
                    + " </VerifyDocument>\n"
                    + " </s:Body>\n"
                    + " </s:Envelope>";

            con.setDoInput(true);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + httpsURL);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Message : " + con.getResponseMessage());
            BufferedReader innn = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String line = "";
            while ((line = innn.readLine()) != null) {
                System.out.println("Error Stream=>" + line);
            }
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
