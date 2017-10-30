package com.example.seniorproject.smartshopping.controller.fragment.settingfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupList;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class CreatedGroupFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    public interface GroupSettingCancel{
        void cancel();
    }

    public interface GroupSettingAdded{
        void add();
    }

    public interface PickImage{
        void pickImage();
    }

    private EditText edtGroupName;
    private EditText edtGroupDescribe;
    private Button btnUploadImage;
    private Button btnCancel;
    private Button btnAdd;
    private TextView tvURI;

    private StorageReference mGroupStorageRef;
    private FirebaseFirestore db;
    private CollectionReference cGroups;
    private CollectionReference cGroupUser;
    private FirebaseUser user;
    private DatabaseReference generateId;

    private Uri selectedImageUri;

    public static final int RC_PHOTO_PICKER =  2;




    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public CreatedGroupFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static CreatedGroupFragment newInstance() {
        CreatedGroupFragment fragment = new CreatedGroupFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

        generateId = FirebaseDatabase.getInstance().getReference();
        mGroupStorageRef = FirebaseStorage.getInstance().getReference().child("groups");

        db = FirebaseFirestore.getInstance();
        cGroups = db.collection("groups");

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            cGroupUser = db.collection("users").document(user.getUid()).collection("groups");
        }
        else{
            Toast.makeText(getContext(), "Cannot find User", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        edtGroupName = (EditText) rootView.findViewById(R.id.edtGroupName);
        edtGroupDescribe = (EditText) rootView.findViewById(R.id.edtGroupDescribe);
        btnUploadImage = (Button) rootView.findViewById(R.id.btnUploadImage);
        tvURI = (TextView) rootView.findViewById(R.id.tvURI);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        btnCancel.setOnClickListener(cancelListener);
        btnUploadImage.setOnClickListener(uploadImageListener);
        btnAdd.setOnClickListener(createGroupListener);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = data.getData();
        this.selectedImageUri = selectedImageUri;
        tvURI.setText(selectedImageUri.getLastPathSegment());
        tvURI.setVisibility(View.VISIBLE);

    }


    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            GroupSettingCancel groupSettingCancel = (GroupSettingCancel) getActivity();
            groupSettingCancel.cancel();
        }
    };

    final View.OnClickListener uploadImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PickImage pickImage =
                    (PickImage) getActivity();

            pickImage.pickImage();

        }
    };

    private void addGroupToDatabase(String downloadUrl){

        String groupId = generateId.push().getKey().toString();
        String name = edtGroupName.getText().toString();
        String photoUrl = downloadUrl.toString();
        String quote = edtGroupDescribe.getText().toString();
        String token = name + groupId.substring(1,5);

        final GroupList groupList = new GroupList(groupId, name, photoUrl);
        Group group = new Group();
        group.setName(name);
        group.setPhotoUrl(photoUrl);
        group.setQuote(quote);
        group.setToken(token);

        WriteBatch batch = db.batch();

        batch.set(cGroups.document(groupId), group);
        batch.set(cGroupUser.document(groupId), groupList);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Create Group Success", Toast.LENGTH_SHORT).show();

                GroupSettingAdded groupSettingAdded = (GroupSettingAdded) getActivity();
                groupSettingAdded.add();
            }
        });


    }

    final View.OnClickListener createGroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(selectedImageUri != null){
                StorageReference photRef = mGroupStorageRef.child(selectedImageUri.getLastPathSegment());
                photRef.putFile(selectedImageUri).addOnSuccessListener(uploadSuccessListener).addOnFailureListener(uploadFailed);
            } else{
                addGroupToDatabase("");
            }
        }
    };


    final OnSuccessListener<UploadTask.TaskSnapshot> uploadSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.d("Tag", "Success");
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            addGroupToDatabase(downloadUrl.toString());

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
