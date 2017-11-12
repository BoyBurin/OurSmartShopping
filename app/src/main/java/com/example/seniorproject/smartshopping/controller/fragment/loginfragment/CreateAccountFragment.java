package com.example.seniorproject.smartshopping.controller.fragment.loginfragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.settingfragment.CreatedGroupFragment;
import com.example.seniorproject.smartshopping.model.dao.group.Group;
import com.example.seniorproject.smartshopping.model.dao.user.User;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class CreateAccountFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface SaveInfoListener {
        void saveInfo();
    }

    public interface CancelInfoListener {
        void cancel();
    }

    public interface PickImageUser {
        void pickImageUser();
    }

    public interface PickImageGroup {
        void pickImageGroup();
    }

    public static final int RC_PHOTO_USER = 0;
    public static final int RC_PHOTO_GROUP = 1;

    private CustomViewGroupEditText customEmail;
    private CustomViewGroupEditText customUsername;
    private CustomViewGroupEditText customPassword;
    private CustomViewGroupEditText customCreateGroup;
    private ImageView imgViewUser;
    private ImageView imgViewGroup;

    private Button btnCreateAccount;
    private Button btnCancel;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    private String userID;
    private String groupID;

    private Uri selectedImageUriUser;
    private Uri selectedImageUriGroup;
    String userDownloadUrl;
    String groupDownloadUrl;

    StorageReference mGroupStorageRef;
    StorageReference mUserStorageRef;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public CreateAccountFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static CreateAccountFragment newInstance() {
        CreateAccountFragment fragment = new CreateAccountFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_login_create_account, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mGroupStorageRef = FirebaseStorage.getInstance().getReference().child("groups");
        mUserStorageRef = FirebaseStorage.getInstance().getReference().child("user");

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        customEmail = (CustomViewGroupEditText) rootView.findViewById(R.id.customEmail);
        customUsername = (CustomViewGroupEditText) rootView.findViewById(R.id.customUsername);
        customPassword = (CustomViewGroupEditText) rootView.findViewById(R.id.customPassword);
        customCreateGroup = (CustomViewGroupEditText) rootView.findViewById(R.id.customCreateGroup);
        btnCreateAccount = (Button) rootView.findViewById(R.id.btnCreateAccount);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        imgViewUser = (ImageView) rootView.findViewById(R.id.imgViewUser);
        imgViewGroup = (ImageView) rootView.findViewById(R.id.imgViewGroup);


        customEmail.setImage(getResources().getDrawable(R.drawable.email_icon, null));
        customEmail.setHintEditText("Email");
        customUsername.setImage(getResources().getDrawable(R.drawable.user_icon, null));
        customUsername.setHintEditText("Username");
        customPassword.setImage(getResources().getDrawable(R.drawable.password_login, null));
        customPassword.setHintEditText("Password");
        customCreateGroup.setImage(getResources().getDrawable(R.drawable.group_icon, null));
        customCreateGroup.setHintEditText("Group name");


        btnCreateAccount.setOnClickListener(saveInfo);
        btnCancel.setOnClickListener(cancelListener);

        imgViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageUser pickImagrUser = (PickImageUser) getActivity();
                pickImagrUser.pickImageUser();
            }
        });

        imgViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageGroup pickImageGroup = (PickImageGroup) getActivity();
                pickImageGroup.pickImageGroup();
            }
        });
    }

    private boolean emptyFill(String name, String password, String group, String username) {
        return name.equals("") || password.equals("") || group.equals("") || username.equals("");
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

        if (requestCode == CreateAccountFragment.RC_PHOTO_USER) {
            Uri selectedImageUriUser = data.getData();
            this.selectedImageUriUser = selectedImageUriUser;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUriUser);
                imgViewUser.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CreateAccountFragment.RC_PHOTO_GROUP) {
            Uri selectedImageUriGroup = data.getData();
            this.selectedImageUriGroup = selectedImageUriGroup;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUriGroup);
                imgViewGroup.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CancelInfoListener cancelInfoListener = (CancelInfoListener) getActivity();
            cancelInfoListener.cancel();
        }
    };
    final View.OnClickListener saveInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final String email = customEmail.getTextFromEditText();
            final String password = customPassword.getTextFromEditText();
            final String groupName = customCreateGroup.getTextFromEditText();
            final String username = customUsername.getTextFromEditText();


            if (emptyFill(email, password, groupName, username)) {
                Toast.makeText(getContext(), "Please complete your information", Toast.LENGTH_SHORT);
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User(username, email, "");
                        userID = mAuth.getCurrentUser().getUid();

                        if (selectedImageUriUser != null) {
                            StorageReference photRef = mUserStorageRef.child(selectedImageUriUser.getLastPathSegment());
                            photRef.putFile(selectedImageUriUser).addOnSuccessListener(uploadUserSuccessListener)
                                    .addOnFailureListener(uploadUserFailed);
                        } else {
                            addUserToDatabase("");
                        }


                    } else {
                        Log.w("CreateAccount: ", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    final OnSuccessListener<UploadTask.TaskSnapshot> uploadUserSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.d("Tag", "Success");
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            addUserToDatabase(downloadUrl.toString());

        }
    };

    final OnFailureListener uploadUserFailed = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
        }
    };


    private void addUserToDatabase(String downloadUrl) {
        userDownloadUrl = downloadUrl;
        if (selectedImageUriGroup != null) {
            StorageReference photRef = mGroupStorageRef.child(selectedImageUriGroup.getLastPathSegment());
            photRef.putFile(selectedImageUriGroup).addOnSuccessListener(uploadGroupSuccessListener).addOnFailureListener(uploadGroupFailed);
        } else {
            addGroupToDatabase("");
        }
    }

    final OnSuccessListener<UploadTask.TaskSnapshot> uploadGroupSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.d("Tag", "Success");
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            addGroupToDatabase(downloadUrl.toString());

        }
    };

    final OnFailureListener uploadGroupFailed = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void addGroupToDatabase (String downloadUrl){
        groupDownloadUrl = downloadUrl;

        final String email = customEmail.getTextFromEditText();
        final String password = customPassword.getTextFromEditText();
        final String groupName = customCreateGroup.getTextFromEditText();
        final String username = customUsername.getTextFromEditText();


        if (emptyFill(email, password, groupName, username)) {
            Toast.makeText(getContext(), "Please complete your information", Toast.LENGTH_SHORT);
            return;
        }

        User user = new User(username, email, userDownloadUrl);
        userID = mAuth.getCurrentUser().getUid();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userID).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        groupID = mDatabaseRef.child("groups").push().getKey();
                        Group group = new Group();
                        group.setName(groupName);
                        group.setToken(groupName + groupID.substring(1, 5));
                        group.setPhotoUrl(groupDownloadUrl);

                        db.collection("groups").document(groupID).set(group)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        WriteBatch batch = db.batch();

                                        HashMap<String,Object> newData = new HashMap<String, Object>();
                                        newData.put("name", username);
                                        newData.put("photoUrl", userDownloadUrl);
                                        batch.set( db.collection("groups").document(groupID).collection("users").document(userID), newData);

                                        HashMap<String,Object> newData2 = new HashMap<String, Object>();
                                        newData2.put("id", groupID);
                                        newData2.put("name", groupName);
                                        newData2.put("photoUrl", groupDownloadUrl);
                                        batch.set(db.collection("users").document(userID).collection("groups").document(groupID), newData2);

                                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //Toast.makeText(getContext(), "Create Account Successfully", Toast.LENGTH_SHORT).show();
                                                SaveInfoListener saveInfoListener = (SaveInfoListener) getActivity();
                                                saveInfoListener.saveInfo();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error writing group", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing user", e);
                    }
                });
    }


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
