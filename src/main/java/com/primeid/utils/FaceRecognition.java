package com.primeid.utils;
import com.google.gson.Gson;
import com.primeid.model.Artifact;
import com.primeid.model.OcrMap;
import com.primeid.model.OcrMapJson;
import com.primeid.model.UploadArtifactJson;
import com.primeid.service.ArtifactService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

/**
 *
 * @author Saddam Hussain
 */
public class FaceRecognition {

    String testImagePath;
    String artifactID1 = "";
    String artifactID2 = "";
    String destination = null;

    public HashMap<String, String> compareFaces(double facialThresh, HashMap<Integer, Artifact> artifactHashMap, String pathOne, String pathTwo, long caseID) throws IOException {

        try {
            System.load("C:\\java\\tomcat\\bin\\opencv_libs\\" + Core.NATIVE_LIBRARY_NAME + ".dll");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        HashMap<String, String> response = new HashMap<>();
        HashMap<String, String> finalResponse = new HashMap<>();
        String trainingDir = System.getProperty("catalina.base") + "/kycengine_artifacts/facialRec_conversions/";
        File fileFirstPath = new File(trainingDir + "caseID-" + caseID + "/selfie");
        if (!fileFirstPath.exists()) {
            fileFirstPath.mkdirs();
        }


        try {
            artifactID1 = Long.toString(artifactHashMap.get(1).getArtifactID());
            artifactID2 = Long.toString(artifactHashMap.get(2).getArtifactID());
            response = detectFaces(artifactHashMap, pathOne, artifactID1, pathTwo, artifactID2, caseID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        File root = new File(fileFirstPath.getAbsolutePath());
        opencv_core.Mat testImage = imread(testImagePath, CV_LOAD_IMAGE_GRAYSCALE);
        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };
        File[] imageFiles = root.listFiles(imgFilter);
        MatVector images = new MatVector(imageFiles.length);
        opencv_core.Mat labels = new opencv_core.Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
        int counter = 0;
        try {
          
            for (File image : imageFiles) {
                opencv_core.Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
                int imagelabel = Integer.parseInt(image.getName().split("\\-")[0]);
                images.put(counter, img);
                labelsBuf.put(counter, imagelabel);
                counter++;
            }
            FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();

            faceRecognizer.train(images, labels);

            IntPointer label = new IntPointer(1);
            DoublePointer confidence = new DoublePointer(1);
            faceRecognizer.predict(testImage, label, confidence);
            int predictedLabel = label.get(0);
            System.out.println(" compare : " + (confidence.get() > facialThresh));
            System.out.println(" compare : " + (confidence.get() > facialThresh));
            System.out.println(" compare : " + (confidence.get() > facialThresh));
            if (confidence.get() > facialThresh) {
                response.put("MATCHED", "FALSE");
            } else {
                response.put("MATCHED", "TRUE");
            }
            response.put("CONFIDENCE", Double.toString(confidence.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return response;
    }

    public HashMap<String, String> detectFaces(HashMap<Integer, Artifact> artifactHashMap, String pathOne, String labelOne, String pathTwo, String labelTwo, long caseID) {
        HashMap<String, String> response = new HashMap<String, String>();
        File first = new File(pathOne);
        File second = new File(pathTwo);
        Gson gson = new Gson();
        CascadeClassifier faceDetector = new CascadeClassifier(System.getProperty("catalina.base") + "/bin/opencv_libs/haarcascade_frontalface_default.xml");

        MatOfInt moi = new MatOfInt(2);

        String fileName = null;
       
        Mat image_roi = null;
        for (int artifactID : artifactHashMap.keySet()) {
            Mat image = null;
             String destination = null;
            if (artifactID == 1) {
                fileName = pathOne;
                destination = System.getProperty("catalina.base") + "/kycengine_artifacts/facialRec_conversions/caseID-" + caseID + "/selfie/" + labelOne + "-" + first.getName();

                
            } else {
                fileName = pathTwo;
                destination = System.getProperty("catalina.base") + "/kycengine_artifacts/facialRec_conversions/caseID-" + caseID + "/" + labelTwo + "-" + second.getName();
               testImagePath = destination;
            }
            
                Rect rectangle = null;
            
            if (artifactHashMap.get(artifactID).getDocumentType().getDocumentTypeID() != 7 && artifactHashMap.get(artifactID).getFeature_map() != null && artifactHashMap.get(artifactID).getFeature_map() != "") {
                
                String feature_map = "{\"ocr_map\":"+artifactHashMap.get(artifactID).getFeature_map()+"}".replaceAll("\"\"", "\"").replaceAll("\\\\", "/");
               OcrMapJson ocrJson = gson.fromJson(feature_map.replaceAll("\\\\", "/"), OcrMapJson.class);

               image = Imgcodecs.imread(fileName);
                
                List<OcrMap> ocrMapList = ocrJson.getOcr_map();
                for (OcrMap ocrMap : ocrMapList) {
                    if (ocrMap.getTitle().contentEquals("photo")) {
                        int x, y, w, h;

                        x = Integer.parseInt(ocrMap.getX());
                        y = Integer.parseInt(ocrMap.getY());
                        w = Integer.parseInt(ocrMap.getW());
                        h = Integer.parseInt(ocrMap.getH());
                       Imgproc.rectangle(image, new Point(x, y), new Point(x + w, y + h), new Scalar(0, 255, 0));
                        rectangle = new Rect(x, y, w, h);

                    }
                }
            
                try {
                   
                    image_roi = new Mat(image, rectangle);
                    Imgcodecs.imwrite(destination, image_roi);

                    resize(destination, destination, 200, 200);
                    image = Imgcodecs.imread(destination);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            if (image == null) {
                image = Imgcodecs.imread(fileName);
            }

            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(image, faceDetections, 1.1, 5, Objdetect.CASCADE_SCALE_IMAGE,
                    new Size(1, 1), new Size());
            System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
            // Draw a bounding box around each face.
            Rect rectCrop = null;
            for (Rect rect : faceDetections.toArray()) {
                if (artifactID == 1) {
                    response.put(first.getName().split("\\.")[0], rect.toString());
                } else {
                    response.put(second.getName().split("\\.")[0], rect.toString());
                }
                Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
                rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);

            }
            try {
                // Save the visualized detection.
                System.out.println("image= " + image + ", rect= " + rectCrop);
                image_roi = new Mat(image, rectCrop);
//		    Imgcodecs.imwrite(fileName, image);
                Imgcodecs.imwrite(destination, image_roi);

                resize(destination, destination, 200, 200);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        
        return response;
    }

    public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
}
