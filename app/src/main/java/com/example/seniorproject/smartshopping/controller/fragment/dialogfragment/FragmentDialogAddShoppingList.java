package com.example.seniorproject.smartshopping.controller.fragment.dialogfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingList;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private FirebaseFirestore db;
    private CollectionReference cShoppingLists;

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

        db = FirebaseFirestore.getInstance();
        cShoppingLists = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("shoppinglists");

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

    private void addShoppingListToDatabase(String downloadUrl){

        ShoppingList newShoppingList = new ShoppingList();
        newShoppingList.setName(edtListName.getText().toString());
        newShoppingList.setDescript(edtListDescribe.getText().toString());
        newShoppingList.setPhotoURL(downloadUrl.toString());

        cShoppingLists.add(newShoppingList).addOnSuccessListener(addShoppingListSuccess).addOnFailureListener(addShoppingListFailed);

        Toast.makeText(getActivity(), "Add Shooping List " + newShoppingList.getName() + " Success", Toast.LENGTH_SHORT).show();
        closeDialog();
    }

    final View.OnClickListener addDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(selectedImageUri != null){
                StorageReference photRef = mShoopingListStorageRef.child(selectedImageUri.getLastPathSegment());
                photRef.putFile(selectedImageUri).addOnSuccessListener(uploadSuccessListener).addOnFailureListener(uploadFailed);
            } else{
                addShoppingListToDatabase("");
            }
        }
    };


    final OnSuccessListener<UploadTask.TaskSnapshot> uploadSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.d("Tag", "Success");
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            addShoppingListToDatabase(downloadUrl.toString());

        }
    };

    final OnFailureListener uploadFailed = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
        }
    };

    final OnSuccessListener<DocumentReference> addShoppingListSuccess = new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
        }
    };

    final OnFailureListener addShoppingListFailed = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.w("TAG", "Error adding document", e);
            Toast.makeText(getActivity(), "Add Shooping List Failed", Toast.LENGTH_SHORT).show();
        }
    };






    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
