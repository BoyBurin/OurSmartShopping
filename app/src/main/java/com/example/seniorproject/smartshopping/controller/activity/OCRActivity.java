/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.seniorproject.smartshopping.controller.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.model.ocrtools.LevenshteinDistance;
import com.example.seniorproject.smartshopping.model.util.PermissionUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.seniorproject.smartshopping.R;

public class OCRActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCFC2KKqZoQ5lIWyNp00bh3wDoO4p_z7xY";
    public static final String FILE_NAME = "temp.jpg";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;
    private ImageView mMainImage;
    private long startTimeMS;
    private float uploadDurationSec;

    private EditText tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15,tv16,tv17,tv18,tv19,tv20,tv21,tv22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OCRActivity.this);
                builder
                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGalleryChooser();
                            }
                        })
                        .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                            }
                        });
                builder.create().show();
            }
        });

        mImageDetails = (TextView) findViewById(R.id.image_details);
        mMainImage = (ImageView) findViewById(R.id.main_image);

        tv1 = (EditText) findViewById(R.id.tv1);
        tv2 = (EditText) findViewById(R.id.tv2);
        tv3 = (EditText) findViewById(R.id.tv3);
        tv4 = (EditText) findViewById(R.id.tv4);
        tv5 = (EditText) findViewById(R.id.tv5);
        tv6 = (EditText) findViewById(R.id.tv6);
        tv7 = (EditText) findViewById(R.id.tv7);
        tv8 = (EditText) findViewById(R.id.tv8);
        tv9 = (EditText) findViewById(R.id.tv9);
        tv10 = (EditText) findViewById(R.id.tv10);
        tv11 = (EditText) findViewById(R.id.tv11);
        tv12 = (EditText) findViewById(R.id.tv12);
        tv13 = (EditText) findViewById(R.id.tv13);
        tv14 = (EditText) findViewById(R.id.tv14);
        tv15 = (EditText) findViewById(R.id.tv15);
        tv16 = (EditText) findViewById(R.id.tv16);
        tv17 = (EditText) findViewById(R.id.tv17);
        tv18 = (EditText) findViewById(R.id.tv18);
        tv19 = (EditText) findViewById(R.id.tv19);
        tv20 = (EditText) findViewById(R.id.tv20);
        tv21 = (EditText) findViewById(R.id.tv21);
        tv22 = (EditText) findViewById(R.id.tv22);

        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        tv4.setVisibility(View.INVISIBLE);
        tv5.setVisibility(View.INVISIBLE);
        tv6.setVisibility(View.INVISIBLE);
        tv7.setVisibility(View.INVISIBLE);
        tv8.setVisibility(View.INVISIBLE);
        tv9.setVisibility(View.INVISIBLE);
        tv10.setVisibility(View.INVISIBLE);
        tv11.setVisibility(View.INVISIBLE);
        tv12.setVisibility(View.INVISIBLE);
        tv13.setVisibility(View.INVISIBLE);
        tv14.setVisibility(View.INVISIBLE);
        tv15.setVisibility(View.INVISIBLE);
        tv16.setVisibility(View.INVISIBLE);
        tv17.setVisibility(View.INVISIBLE);
        tv18.setVisibility(View.INVISIBLE);
        tv19.setVisibility(View.INVISIBLE);
        tv20.setVisibility(View.INVISIBLE);
        tv21.setVisibility(View.INVISIBLE);
        tv22.setVisibility(View.INVISIBLE);
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            uploadImage(Uri.fromFile(getCameraFile()));
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        startTimeMS = System.currentTimeMillis();
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {

                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new
                            VisionRequestInitializer(CLOUD_VISION_API_KEY));
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        ArrayList<String> language = new ArrayList<String>();
                        language.add("th");
                        ImageContext imc = new ImageContext();
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImageContext(imc.setLanguageHints(language));
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("TEXT_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    long sendMS = System.currentTimeMillis();
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    uploadDurationSec = (System.currentTimeMillis() - sendMS) / 1000f;
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                mImageDetails.setText("");
                String[] resultList = result.split("\n");
                for(int i = 2 ; i < resultList.length ; i++){
                    if(i == 3){
                        tv1.setVisibility(View.VISIBLE);
                        tv1.setText(resultList[3]);
                    }

                    if(i == 4){
                        tv2.setVisibility(View.VISIBLE);
                        tv2.setText(resultList[4]);
                    }

                    if(i == 5){
                        tv3.setVisibility(View.VISIBLE);
                        tv3.setText(resultList[5]);
                    }

                    if(i == 6){
                        tv4.setVisibility(View.VISIBLE);
                        tv4.setText(resultList[6]);
                    }

                    if(i == 7){
                        tv5.setVisibility(View.VISIBLE);
                        tv5.setText(resultList[7]);
                    }

                    if(i == 8){
                        tv6.setVisibility(View.VISIBLE);
                        tv6.setText(resultList[8]);
                    }

                    if(i == 9){
                        tv7.setVisibility(View.VISIBLE);
                        tv7.setText(resultList[9]);
                    }

                    if(i == 10){
                        tv8.setVisibility(View.VISIBLE);
                        tv8.setText(resultList[10]);
                    }

                    if(i == 11){
                        tv9.setVisibility(View.VISIBLE);
                        tv9.setText(resultList[11]);
                    }

                    if(i == 12){
                        tv10.setVisibility(View.VISIBLE);
                        tv10.setText(resultList[12]);
                    }

                    if(i == 13){
                        tv11.setVisibility(View.VISIBLE);
                        tv11.setText(resultList[13]);
                    }

                    if(i == 14){
                        tv12.setVisibility(View.VISIBLE);
                        tv12.setText(resultList[14]);
                    }

                    if(i == 15){
                        tv13.setVisibility(View.VISIBLE);
                        tv13.setText(resultList[15]);
                    }

                    if(i == 16){
                        tv14.setVisibility(View.VISIBLE);
                        tv14.setText(resultList[16]);
                    }

                    if(i == 17){
                        tv15.setVisibility(View.VISIBLE);
                        tv15.setText(resultList[17]);
                    }

                    if(i == 18){
                        tv16.setVisibility(View.VISIBLE);
                        tv16.setText(resultList[18]);
                    }

                    if(i == 19){
                        tv17.setVisibility(View.VISIBLE);
                        tv17.setText(resultList[19]);
                    }

                    if(i == 20){
                        tv18.setVisibility(View.VISIBLE);
                        tv18.setText(resultList[20]);
                    }

                    if(i == 21){
                        tv19.setVisibility(View.VISIBLE);
                        tv19.setText(resultList[21]);
                    }

                    if(i == 22){
                        tv20.setVisibility(View.VISIBLE);
                        tv20.setText(resultList[22]);
                    }

                    if(i == 23){
                        tv21.setVisibility(View.VISIBLE);
                        tv21.setText(resultList[23]);
                    }

                    if(i == 24){
                        tv22.setVisibility(View.VISIBLE);
                        tv22.setText(resultList[24]);
                    }
                }
            }
        }.execute();
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        long spentMS = System.currentTimeMillis() - startTimeMS;
        String message = "Recognition results:\n";
        StringBuilder builder = new StringBuilder(message);
        String s = "";
        builder.append(String.format("(Total spent %.2f secs, including %.2f secs for upload)\n\n", spentMS / 1000f, uploadDurationSec));

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        Log.i("JackTest", "total labels:" + labels.size());
        if (labels != null) {
            for (int i = 0; i < labels.size(); i++ ) {
                EntityAnnotation label = labels.get(i);
                if (i == 0) {
                    //builder.append("Locale: ");
                    //builder.append(label.getLocale());
                }
                s = label.getDescription();
                //builder.append(label.getDescription());
                //builder.append("\n");
                //TODO: Draw rectangles later
                break;
            }
            ArrayList<String> words = new ArrayList<String>();
            ArrayList<String> amount = new ArrayList<String>();

            String[] listLabel = s.split("\n");
            for(String line : listLabel){
                if(isNumeric(line)){
                    amount.add(line.split("V")[0].split("N")[0].split("v")[0].split("\\.")[0]);
                } else
                if(!line.contains("X") && !line.contains("14") && !line.contains("x")){
                    words.add(line);
                }
            }

            LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
            ArrayList<String> mywords = new ArrayList<String>();
            mywords = levenshteinDistance.doLevenshteinDistance(words);
            Log.i("Amount: " , "" +amount.size());
            Log.i("Words: " , "" +words.size());

            int length = (mywords.size() < amount.size())? mywords.size() : amount.size();

            for(int i = 0 ; i < length ; i++){
                if(i == length - 1 && mywords.size() > amount.size()){
                    builder.append(mywords.get(words.size()-1));
                }else  builder.append(mywords.get(i));
                builder.append("\n");
                builder.append(amount.get(i));
                builder.append("\n");
            }

            /*for(int i = 0 ; i < amount.size() ; i++){
                builder.append(amount.get(i));
                builder.append("\n");
            }
            for(int i = 0 ; i < words.size() ; i++){
              builder.append(words.get(i));
                builder.append("\n");

            } */


        } else {
            builder.append("nothing");
        }

        return builder.toString();
    }

    private static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?N?V?v?\\s?N?V?v?");  //match a number with optional '-' and decimal.
    }
}