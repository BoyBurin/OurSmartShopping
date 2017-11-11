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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.controller.fragment.purchaseitem.PurchaseItemAddFragment;
import com.example.seniorproject.smartshopping.controller.fragment.purchaseitem.PurchaseItemOCRFragment;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.model.manager.itemocr.ItemOCRManager;
import com.example.seniorproject.smartshopping.model.ocrtools.LevenshteinDistance;
import com.example.seniorproject.smartshopping.model.util.PermissionUtils;
import com.example.seniorproject.smartshopping.view.adapter.itemocr.ItemOCRAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.seniorproject.smartshopping.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

public class OCRActivity extends AppCompatActivity implements PurchaseItemAddFragment.MainFragmentTag {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCFC2KKqZoQ5lIWyNp00bh3wDoO4p_z7xY";
    public static final String FILE_NAME = "temp.jpg";

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    private static Uri tmpUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final int PIC_CROP = 4;

    private static final String PURCHASE_ITEM__FRAGMENT_TAG = "purchaseItemFragmentTag";

    private TextView mImageDetails;
    private RelativeLayout activityLayout;
    private FrameLayout containerOCR;
    private RelativeLayout loadingOCR;
    //private ImageView mMainImage;
    private Spinner spinner;

    private long startTimeMS;
    private float uploadDurationSec;

    private ItemOCR itemOCR;
    private ItemOCRManager itemOCRManager;
    private ItemInventoryManager itemInventoryManager;
    /*private ItemOCRAdapter itemOCRAdapter;
    private MutableInteger lastPositionInteger;*/

    private FloatingActionButton fab;
    //private ListView listView;
    //private TextView tvTotalPrice;
    //private Button btnSaveOCR;
    //private TextView storeName;
    private ProgressBar progressBarOCR;

    /*private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db;*/

    private ArrayAdapter<String> adapter;
    private String currentStore;

    private FirebaseFirestore db;
    private CollectionReference cItems;
    private ListenerRegistration cItemsListener;

    private Uri picUri;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        fab = (FloatingActionButton) findViewById(R.id.fab);
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


        init();

    }


    private void init() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(GroupManager.getInstance().getCurrentGroup().getGroup().getName());

        itemInventoryManager = new ItemInventoryManager();
        db = FirebaseFirestore.getInstance();
        cItems = db.collection("groups")
                .document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items");

        cItemsListener = cItems.addSnapshotListener(itemListener);

        progressBarOCR = (ProgressBar) findViewById(R.id.progressBarOCR);
        loadingOCR = (RelativeLayout) findViewById(R.id.loadingOCR);
        spinner = (Spinner) findViewById(R.id.spinner);
        activityLayout = (RelativeLayout) findViewById(R.id.activityLayout);
        containerOCR = (FrameLayout) findViewById(R.id.containerOCR);
        itemOCRManager = new ItemOCRManager();

        currentStore = "Big C Bangpakok";
        String[] stores = {"Big C Bangpakok", "Max Value Pracha Uthit", "Tesco Lotus Bangpakok", "Tesco Lotus ตลาดโลตัสประชาอุทิศ"};
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stores);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(storeSelectedLister);
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

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
            Log.d("boy", "No");

        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            //uploadImage(Uri.fromFile(getCameraFile()));
            picUri = Uri.fromFile(getCameraFile());
            performCrop();
        }
        else if(requestCode == PIC_CROP && resultCode == RESULT_OK){
            Uri uri = data.getData();
            uploadImage(uri);
            Log.d("boy", "Yeah");
        }
    }


    private void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
//set crop properties
            cropIntent.putExtra("crop", "true");
//indicate aspect of desired crop
            //cropIntent.putExtra("aspectX", 1);
            //cropIntent.putExtra("aspectY", 1);
//indicate output X and Y
            //cropIntent.putExtra("outputX", 256);
            //cropIntent.putExtra("outputY", 256);
//retrieve data on return
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpUri);
//start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
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
                /*mMainImage.setVisibility(View.VISIBLE);
                mMainImage.setImageBitmap(bitmap);*/

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
        activityLayout.setVisibility(View.GONE);
        loadingOCR.setVisibility(View.VISIBLE);

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
                if(result == null){
                    toast();
                    activityLayout.setVisibility(View.VISIBLE);
                    loadingOCR.setVisibility(View.GONE);
                    return;
                }
                String[] myResult = result.split("\n");
                for (int i = 0; i < myResult.length; i++) {
                    Log.d(i + "", myResult[i]);
                }

                ArrayList<String> words = new ArrayList<String>();
                ArrayList<String> amount = new ArrayList<String>();

                for (int i = 3; i < myResult.length; i++) {
                    String line = myResult[i];
                    if (isNumeric(line)) {
                        amount.add(line);
                    } else if (isWord(line)) {
                        words.add(line);
                    }
                }

                Log.d("Word: ", words.size() + "");
                Log.d("Amount: ", amount.size() + "");
                double totalPrice = 0;
                int position = 0;
                for (int i = 0; i < amount.size(); i++) {
                    double price = Double.parseDouble(amount.get(i));
                    if (price > totalPrice) {
                        totalPrice = price;
                        position = i;
                    }
                }
                amount.remove(position);

                LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

                ArrayList<ItemInventoryMap> itemInventoryMaps =
                        levenshteinDistance.doLevenshteinDistance(words, itemInventoryManager);

                for (int i = 0; i < itemInventoryMaps.size(); i++) {
                    ItemInventoryMap itemInventoryMap = itemInventoryMaps.get(i);
                    double price = Double.parseDouble(amount.get(i));
                    itemOCRManager.addItemOCR(new ItemOCR(itemInventoryMap, price, 1));
                }


                loadingOCR.setVisibility(View.GONE);
                PurchaseItemOCRFragment purchaseItemOCRFragment = PurchaseItemOCRFragment
                        .newInstance(itemOCRManager.getItemOCRs(), currentStore);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerOCR, purchaseItemOCRFragment, PURCHASE_ITEM__FRAGMENT_TAG)
                        .commit();


            }
        }.execute();
    }

    private void toast(){
        Toast.makeText(this, "Cannot do receipt recognition", Toast.LENGTH_SHORT).show();
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
        if(labels == null){

            return null;

        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cItemsListener != null) {
            cItemsListener.remove();
            cItemsListener = null;
        }
    }

    /******************************************************************************************
     * ****************************** Listener variable *********************************************
     *******************************************************************************************/

   /* final View.OnClickListener saveInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressBarOCR.setVisibility(View.VISIBLE);

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    CollectionReference storeRef = db.collection("stores").document(currentStore)
                            .collection("products");
                    CollectionReference itemRef = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                            .collection("items");

                    long[]amountSet = new long[itemOCRManager.getItemOCRs().size()];
                    double[]priceSet = new double[itemOCRManager.getItemOCRs().size()];

                    for (int i = 0; i < itemOCRManager.getItemOCRs().size(); i++) {
                        ItemOCR itemOCR = itemOCRManager.getItemOCRs().get(i);
                        priceSet[i] = itemOCR.getPrice();
                        amountSet[i] = itemOCR.getAmount();
                    }

                    ArrayList<ProductCrowd> products = new ArrayList<ProductCrowd>();
                    for (int i = 0; i < itemOCRManager.getItemOCRs().size(); i++) {
                        String itemInventoryID = itemOCRManager.getItemOCRs().get(i).getItemInventoryMap().getId();
                        ProductCrowd newData = transaction.get(storeRef.document(itemInventoryID)).toObject(ProductCrowd.class);
                        products.add(newData);
                    }

                    ArrayList<ItemInventory> itemInventories = new ArrayList<ItemInventory>();
                    double[]updateAmount = new double[itemOCRManager.getItemOCRs().size()];
                    for (int i = 0; i < itemOCRManager.getItemOCRs().size(); i++) {
                        String itemInventoryID = itemOCRManager.getItemOCRs().get(i).getItemInventoryMap().getId();
                        ItemInventory newData = transaction.get(itemRef.document(itemInventoryID)).toObject(ItemInventory.class);
                        itemInventories.add(newData);
                        updateAmount[i] = newData.getAmount() + amountSet[i];
                    }

                    for (int i = 0; i < itemOCRManager.getItemOCRs().size(); i++) {
                        String itemInventoryID = itemOCRManager.getItemOCRs().get(i).getItemInventoryMap().getId();
                        Map<String, Object> update = new HashMap<>();
                        update.put("price", priceSet[i]);
                        transaction.update(storeRef.document(itemInventoryID), update);
                    }

                    for (int i = 0; i < itemOCRManager.getItemOCRs().size(); i++) {
                        String itemInventoryID = itemOCRManager.getItemOCRs().get(i).getItemInventoryMap().getId();
                        Map<String, Object> update = new HashMap<>();
                        update.put("amount", updateAmount[i]);
                        transaction.update(itemRef.document(itemInventoryID), update);
                    }

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBarOCR.setVisibility(View.GONE);
                    Log.d(TAG, "Transaction success!");
                    Toast.makeText(OCRActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Transaction failure.", e);
                        }
                    });

           ตรงนี้ for (int i = 0; i < itemOCRManager.getItemOCRs().size(); i++) {
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
    }; */

    final AdapterView.OnItemSelectedListener storeSelectedLister = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 0:
                    currentStore = "Big C Bangpakok";
                    break;
                case 1:
                    currentStore = "Max Value Pracha Uthit";
                    break;
                case 2:
                    currentStore = "Tesco Lotus Bangpakok";
                    break;
                case 3:
                    currentStore = "Tesco Lotus ตลาดโลตัสประชาอุทิศ";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    final EventListener<QuerySnapshot> itemListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }


            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        ItemInventory item = documentSnapshot.toObject(ItemInventory.class);
                        String id = documentSnapshot.getId();

                        //Add
                        ItemInventoryMap itemMap = new ItemInventoryMap(id, item);
                        itemInventoryManager.addItemInventory(itemMap);

                        //Toast.makeText(OCRActivity.this, "Added " + item.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:
                        documentSnapshot = dc.getDocument();
                        ItemInventory update = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        int index = itemInventoryManager.getIndexByKey(id);
                        itemMap = itemInventoryManager.getItemInventory(index);

                        //Update
                        itemMap.setItemInventory(update);
                        itemInventoryManager.sortItem();

                        Toast.makeText(OCRActivity.this, "Update " + update.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        item = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        index = itemInventoryManager.getIndexByKey(id);
                        itemInventoryManager.removeItemInventory(index);

                        Toast.makeText(OCRActivity.this, "Remove " + item.getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };


    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/

    @Override
    public String getMainFragmentTag() {
        return PURCHASE_ITEM__FRAGMENT_TAG;
    }
}