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
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.ItemOCR;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.ItemInventoryManager;
import com.example.seniorproject.smartshopping.model.manager.ItemOCRManager;
import com.example.seniorproject.smartshopping.model.ocrtools.LevenshteinDistance;
import com.example.seniorproject.smartshopping.model.util.PermissionUtils;
import com.example.seniorproject.smartshopping.view.adapter.ItemOCRAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private ItemOCR itemOCR;
    private ItemOCRManager itemOCRManager;
    private ItemOCRAdapter itemOCRAdapter;
    private MutableInteger lastPositionInteger;
    private ListView listView;
    private TextView tvTotalPrice;
    private Button btnSaveOCR;
    private ProgressBar progressBarOCR;

    private DatabaseReference mDatabaseRef;


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
        mMainImage.setVisibility(View.GONE);

        init();

    }

    private void init() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        lastPositionInteger = new MutableInteger(-1);
        itemOCRManager = new ItemOCRManager();
        itemOCRAdapter = new ItemOCRAdapter(lastPositionInteger);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        btnSaveOCR = (Button) findViewById(R.id.btnSaveOCR);
        progressBarOCR = (ProgressBar) findViewById(R.id.progressBarOCR);

        tvTotalPrice.setVisibility(View.GONE);
        btnSaveOCR.setOnClickListener(saveInfoListener);


        listView = (ListView) findViewById(R.id.listViewItemOCR);

        /*Log.d("Size: ", "" + ItemInventoryManager.getInstance().getSize());
        for (int i = 0; i < ItemInventoryManager.getInstance().getSize(); i++) {
            ItemInventoryMap itemInventoryMap = ItemInventoryManager.getInstance().getItemInventory(i);
            itemOCRManager.addItemOCR(new ItemOCR(itemInventoryMap, 0, 1));
        }

        Log.d("Size: ", "" + itemOCRManager.getSize()); */
        itemOCRAdapter.setItemOCRs(itemOCRManager.getItemOCRs());


        listView.setAdapter(itemOCRAdapter);

        itemOCRAdapter.notifyDataSetChanged();
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
                mMainImage.setVisibility(View.VISIBLE);
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
                //mImageDetails.setText(result);
                String[] myResult = result.split("\n");
                for(int i = 0 ; i < myResult.length ; i++){
                    Log.d(i+ "", myResult[i]);
                }

                ArrayList<String> words = new ArrayList<String>();
                ArrayList<String> amount = new ArrayList<String>();

                for (int i = 3 ; i < myResult.length ; i++) {
                    String line = myResult[i];
                    if (isNumeric(line)) {
                        amount.add(line);
                    } else if (isWord(line)) {
                        words.add(line);
                    }
                }

                Log.d("Word: " , words.size() + "");
                Log.d("Amount: " , amount.size() + "");
                double totalPrice = 0;
                int position = 0;
                for(int i = 0 ; i < amount.size() ; i++){
                    double price = Double.parseDouble(amount.get(i));
                    if(price > totalPrice){
                        totalPrice = price;
                        position = i;
                    }
                }
                amount.remove(position);

                LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

                ArrayList<ItemInventoryMap> itemInventoryMaps =
                        levenshteinDistance.doLevenshteinDistance(words);

                for (int i = 0; i < itemInventoryMaps.size(); i++) {
                    ItemInventoryMap itemInventoryMap = itemInventoryMaps.get(i);
                    double price = Double.parseDouble(amount.get(i));
                    itemOCRManager.addItemOCR(new ItemOCR(itemInventoryMap, price, 1));
                }

                itemOCRAdapter.setItemOCRs(itemOCRManager.getItemOCRs());
                itemOCRAdapter.notifyDataSetChanged();

                tvTotalPrice.setVisibility(View.VISIBLE);
                tvTotalPrice.setText("Total:    " + totalPrice + "   บาท");


                mImageDetails.setVisibility(View.GONE);




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
            for (int i = 0; i < labels.size(); i++) {
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
            for (String line : listLabel) {
                for (String myword : line.split("\\s+")) {
                    if (isNumeric(myword)) {
                        amount.add(line.split("V")[0].split("N")[0].split("v")[0]);
                        break;
                    } else if (isWord(myword)) {
                        words.add(myword);
                        break;
                    }
                }
            }

            //LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
            //ArrayList<String> mywords = new ArrayList<String>();
            //mywords = levenshteinDistance.doLevenshteinDistance(words);
            Log.i("Amount: ", "" + amount.size());
            Log.i("Words: ", "" + words.size());

            /*int length = (mywords.size() < amount.size())? mywords.size() : amount.size();

            for(int i = 0 ; i < length ; i++){
                if(i == length - 1 && mywords.size() > amount.size()){
                    builder.append(mywords.get(words.size()-1));
                }else  builder.append(mywords.get(i));
                builder.append("\n");
                builder.append(amount.get(i));
                builder.append("\n");
            }*/

            for (int i = 0; i < amount.size(); i++) {
                builder.append(amount.get(i));
                builder.append("\n");
            }
            for (int i = 0; i < words.size(); i++) {
                builder.append(words.get(i));
                builder.append("\n");

            }


        } else {
            builder.append("nothing");
        }

        return builder.toString();
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?N?V?v?\\s?N?V?v?");  //match a number with optional '-' and decimal.
    }

    private static boolean isWord(String str) {
        return str.matches("[\\u0E01-\\u0E5B][\\u0E01-\\u0E5B0-9]*");  //match a number with optional '-' and decimal.
    }


    /******************************************************************************************
     * ****************************** Listener variable *********************************************
     *******************************************************************************************/

    final View.OnClickListener saveInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressBarOCR.setVisibility(View.VISIBLE);
            for (int i = 0; i < itemOCRManager.getItemOCRs().size(); i++) {
                ItemOCR itemOCR = itemOCRManager.getItemOCRs().get(i);
                ItemInventory itemInventory = itemOCR.getItemInventoryMap().getItemInventory();
                String itemInventoryID = itemOCR.getItemInventoryMap().getId();
                //double price = itemOCR.getPrice();
                long amount = itemOCR.getAmount();

                long remainAmount = itemInventory.getAmount() + amount;

                itemInventory.setAmount(remainAmount);

                mDatabaseRef.child("iteminventory").child(itemInventoryID)
                        .setValue(itemInventory).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBarOCR.setVisibility(View.GONE);
                    }
                });
            }
            Toast.makeText(OCRActivity.this, "Update Item Inventory Success", Toast.LENGTH_SHORT).show();
            finish();
        }
    };


}