package com.primeid.service;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.springframework.stereotype.Service;
import java.io.StringReader;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 *
 * @author Saddam Hussain
 */
@Service("imageForgeryService")
public class ImageForgeryService {

    public String submitImage(String image) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://91.134.247.230/ImageForgeryDetector/ImageForgeryDetector.svc";
        client.setConnectTimeout(5, TimeUnit.MINUTES); // connect timeout
        client.setWriteTimeout(5, TimeUnit.MINUTES);
        client.setReadTimeout(5, TimeUnit.MINUTES);    // socket timeout

        MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
        String st = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:sco=\"http://schemas.datacontract.org/2004/07/Scorto.DocAnalysis.WebService.Models\">\r\n\r\n    <x:Header/>\r\n\r\n    <x:Body>\r\n\r\n        <tem:SubmitImage>\r\n\r\n            <tem:data>\r\n\r\n<sco:Data>";
        String st4 = "</sco:Data>\r\n\r\n                <sco:Description>Other</sco:Description>\r\n\r\n                <sco:SendProcessedImage>true</sco:SendProcessedImage>\r\n\r\n                <sco:UseDemo>false</sco:UseDemo>\r\n\r\n            </tem:data>\r\n\r\n        </tem:SubmitImage>\r\n\r\n    </x:Body>\r\n\r\n</x:Envelope>";
        RequestBody body = RequestBody.create(mediaType, st + image + st4);
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "text/xml; charset=utf-8")
                    .addHeader("soapaction", "http://tempuri.org/IImageForgeryDetector/SubmitImage")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String responseRequest = response.body().string();
            String[] responseRequestTemp = responseRequest.split("</SubmitImageResult>");
            String res = responseRequestTemp[0].split("<SubmitImageResult>")[1];
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public void checkStatus(String id) {
        OkHttpClient client = new OkHttpClient();

        String rBodyStart = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\r\n    <x:Header/>\r\n    <x:Body>\r\n        <tem:CheckStatus>\r\n            <tem:id>";
        String rBodyEnd = "</tem:id>\r\n        </tem:CheckStatus>\r\n    </x:Body>\r\n</x:Envelope>";
        MediaType mediaType = MediaType.parse("text/xml");
        RequestBody body = RequestBody.create(mediaType, rBodyStart + id + rBodyEnd);
        Request request = new Request.Builder()
                .url("http://91.134.247.230/ImageForgeryDetector/ImageForgeryDetector.svc")
                .post(body)
                .addHeader("content-type", "text/xml")
                .addHeader("soapaction", "http://tempuri.org/IImageForgeryDetector/CheckStatus")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            String res = "";
            do {
                if (res != "") {
                    Thread.sleep(2000);
                }
                Response response = client.newCall(request).execute();

                String responseRequest = response.body().string();
                String[] responseRequestTemp = responseRequest.split("</CheckStatusResult>");
                res = responseRequestTemp[0].split("<CheckStatusResult>")[1];
                if (res.equalsIgnoreCase("NotFound")) {
                    break;
                }
            } while (!res.equalsIgnoreCase("done"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getResults(String id) {
        OkHttpClient client = new OkHttpClient();

        String rBodyStart = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\r\n    <x:Header/>\r\n    <x:Body>\r\n        <tem:GetResults>\r\n            <tem:id>";
        String rBodyEnd = "</tem:id>\r\n        </tem:GetResults>\r\n    </x:Body>\r\n</x:Envelope>";
        MediaType mediaType = MediaType.parse("text/xml");
        RequestBody body = RequestBody.create(mediaType, rBodyStart + id + rBodyEnd);
        Request request = new Request.Builder()
                .url("http://91.134.247.230/ImageForgeryDetector/ImageForgeryDetector.svc")
                .post(body)
                .addHeader("content-type", "text/xml")
                .addHeader("soapaction", "http://tempuri.org/IImageForgeryDetector/GetResults")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(
                    xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node getNodeValue(String nodeName, NodeList nodeList) {

        for (int x = 0; x < nodeList.getLength(); x++) {

            Node node = nodeList.item(x);
            if (node.getNodeName().equalsIgnoreCase(nodeName)) {
                return node;
            }
        }

        return null;
    }

    public HashMap<String, String> getNodeValues(NodeList nodeList) {
        HashMap<String, String> result = new HashMap<String, String>();
        if (nodeList != null) {
            int length = nodeList.getLength();
            for (int i = 0; i < length; i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nodeList.item(i);
                    if (el.getNodeName().contains("AnalysisResultsResponse")) {
                        String score = el.getElementsByTagName("Score").item(0).getTextContent();
                        String conclusion = el.getElementsByTagName("Conclusion").item(0).getTextContent();
                        result.put("score", score);
                        result.put("conclusion", conclusion);

                    }
                }
            }

            return result;
        } else {
            return null;
        }
    }

}
