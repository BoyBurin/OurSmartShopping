package com.example.seniorproject.smartshopping.controller.fragment.dialogfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ShoppingList;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class FragmentDialogAddShoppingList extends DialogFragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    public interface DeleteAddShoppingListDialog{
        void delete();
    }
    public interface PickImageShoppingListDialog{
        void pickImage();
    }

    private EditText edtListName;
    private EditText edtListDescribe;
    private Button btnUploadImage;
    private TextView tvURI;
    private Button btnCancel;
    private Button btnAdd;

    private StorageReference mShoopingListStorageRef;
    private DatabaseReference mShoppingListDatabaseRef;
    private DatabaseReference mShoppingListInGroupDatabaseRef;

    private Uri selectedImageUri;

    public static final int RC_PHOTO_PICKER =  2;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public FragmentDialogAddShoppingList() {
        super();
    }

    @SuppressWarnings("unused")
    public static FragmentDialogAddShoppingList newInstance() {
        FragmentDialogAddShoppingList fragment = new FragmentDialogAddShoppingList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_add_shopping_list, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        mShoopingListStorageRef = FirebaseStorage.getInstance().getReference().child("shoppinglist");
        mShoppingListDatabaseRef = FirebaseDatabase.getInstance().getReference().child("shoppinglist");
        mShoppingListInGroupDatabaseRef = FirebaseDatabase.getInstance().getReference().child("shoppinglistingroup");

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        edtListName = (EditText) rootView.findViewById(R.id.edtListName);
        edtListDescribe = (EditText) rootView.findViewById(R.id.edtListDescribe);
        btnUploadImage = (Button) rootView.findViewById(R.id.btnUploadImage);
        tvURI = (TextView) rootView.findViewById(R.id.tvURI);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        btnCancel.setOnClickListener(cancelDialogListener);
        btnUploadImage.setOnClickListener(uploadImageListener);
        btnAdd.setOnClickListener(addDialogListener);
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = data.getData();
        this.selectedImageUri = selectedImageUri;
        tvURI.setText(selectedImageUri.getLastPathSegment());
        tvURI.setVisibility(View.VISIBLE);

    }

    private void closeDialog(){
        DeleteAddShoppingListDialog deleteAddShoppingListDialog =
                (DeleteAddShoppingListDialog) getActivity();
        deleteAddShoppingListDialog.delete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyboard();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener cancelDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeDialog();
        }
    };

    final View.OnClickListener uploadImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PickImageShoppingListDialog pickImageShoppingListDialog =
                    (PickImageShoppingListDialog) getActivity();

            pickImageShoppingListDialog.pickImage();

        }
    };

    final View.OnClickListener addDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(selectedImageUri != null){
                StorageReference photRef = mShoopingListStorageRef.child(selectedImageUri.getLastPathSegment());
                photRef.putFile(selectedImageUri).addOnSuccessListener(uploadSuccessListener);
            } else{
                ShoppingList sp = new ShoppingList();
                sp.setName(edtListName.getText().toString());
                sp.setDescript(edtListDescribe.getText().toString());
                DatabaseReference ref =  mShoppingListDatabaseRef.push();
                final String shoppingListID = ref.getKey();
                ref.setValue(sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String groupID = GroupManager.getInstance().getCurrentGroup().getId();
                        mShoppingListInGroupDatabaseRef.child(groupID)
                                .child(shoppingListID)
                                .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Add Shooping List Success", Toast.LENGTH_SHORT);
                                closeDialog();
                            }
                        });
                    }
                });
            }
        }
    };


    final OnSuccessListener<UploadTask.TaskSnapshot> uploadSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.d("Tag", "Success");
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            ShoppingList sp = new ShoppingList();
            sp.setName(edtListName.getText().toString());
            sp.setDescript(edtListDescribe.getText().toString());
            sp.setPhotoURL(downloadUrl.toString());

            DatabaseReference ref =  mShoppingListDatabaseRef.push();
            final String shoppingListID = ref.getKey();
            ref.setValue(sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String groupID = GroupManager.getInstance().getCurrentGroup().getId();
                    mShoppingListInGroupDatabaseRef.child(groupID)
                            .child(shoppingListID)
                            .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Add Shooping List Success", Toast.LENGTH_SHORT);
                            closeDialog();
                        }
                    });
                }
            });
        }
    };






    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
