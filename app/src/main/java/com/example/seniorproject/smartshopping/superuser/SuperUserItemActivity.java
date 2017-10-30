package com.example.seniorproject.smartshopping.superuser;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SuperUserItemActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private TextView tvShowBarcode;
    private Button btnAddBarcode;
    private EditText edtName;
    private EditText edtRetailPrice;
    private EditText edtType;
    private EditText edtUnit;

    private Button btnUploadImage;
    private TextView tvURI;

    private Uri selectedImageUri;

    final static int RC_PHOTO_PICKER =  2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_user_item);
        init();
    }

    public void init(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(savedDataListener);

        tvShowBarcode = (TextView) findViewById(R.id.tvShowBarcode);
        btnAddBarcode = (Button) findViewById(R.id.btnAddBarcode);
        edtName = (EditText) findViewById(R.id.edtName);
        edtRetailPrice = (EditText) findViewById(R.id.edtRetailPrice);
        edtType = (EditText) findViewById(R.id.edtType);
        edtUnit = (EditText) findViewById(R.id.edtUnit);

        btnUploadImage = (Button) findViewById(R.id.btnUploadImage);
        tvURI = (TextView) findViewById(R.id.tvURI);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        btnUploadImage.setOnClickListener(uploadImageListener);

        btnAddBarcode.setOnClickListener(addBarcodeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }


    final View.OnClickListener addItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER){
            if(resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();
                this.selectedImageUri = selectedImageUri;
                tvURI.setText(selectedImageUri.getLastPathSegment());
                tvURI.setVisibility(View.VISIBLE);
            }
        }
        if(requestCode == 0){
            if(resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                tvShowBarcode.setText(contents);
            }

        }
    }

    private void clearProductData(){
        tvShowBarcode.setText("");
        edtName.setText("");
        edtRetailPrice.setText("");
        edtType.setText("");
        edtUnit.setText("");
        tvURI.setText("");
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener uploadImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                    RC_PHOTO_PICKER);
        }
    };

    final View.OnClickListener addBarcodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);
        }
    };

    final View.OnClickListener savedDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StorageReference mProductListStorageRef = FirebaseStorage.getInstance().getReference()
                    .child("productlist");
            StorageReference photRef = mProductListStorageRef.child(selectedImageUri.getLastPathSegment());
            photRef.putFile(selectedImageUri).addOnSuccessListener(uploadSuccessListener);
        }
    };

    final OnSuccessListener<UploadTask.TaskSnapshot> uploadSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.d("Tag", "Success");
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            String name = edtName.getText().toString();
            double retailPrice = Double.parseDouble(edtRetailPrice.getText().toString());
            String type = edtType.getText().toString();
            String unit = edtUnit.getText().toString();
            String barcodeID = tvShowBarcode.getText().toString();
            ProductList productList = new ProductList(name,retailPrice,type,unit, downloadUrl.toString(), barcodeID);


            DatabaseReference ref =  mDatabaseRef.child("productlist").child(barcodeID);
            ref.setValue(productList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    clearProductData();
                    Toast.makeText(SuperUserItemActivity.this, "Added Product Success", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
