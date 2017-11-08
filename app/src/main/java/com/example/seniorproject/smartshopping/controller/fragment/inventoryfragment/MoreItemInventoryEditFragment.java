package com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


public class MoreItemInventoryEditFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private ItemInventoryMap itemInventoryMap;

    private EditText edtComment;
    private EditText edtAmount;
    private EditText edtSoft;
    private EditText edtHard;
    private Button btnSave;

    private FirebaseFirestore db;
    private DocumentReference dItem;

    private ProgressBar progressBar;
    private View backgroundLoading;




    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreItemInventoryEditFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreItemInventoryEditFragment newInstance(ItemInventoryMap itemInventoryMap) {
        MoreItemInventoryEditFragment fragment = new MoreItemInventoryEditFragment();
        Bundle args = new Bundle();
        args.putParcelable("itemInventoryMap", itemInventoryMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemInventoryMap = getArguments().getParcelable("itemInventoryMap");
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_more_item_inventory_edit, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        dItem = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items").document(itemInventoryMap.getId());

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        edtComment = (EditText) rootView.findViewById(R.id.edtComment);
        edtAmount = (EditText) rootView.findViewById(R.id.edtAmount);
        edtSoft = (EditText) rootView.findViewById(R.id.edtSoft);
        edtHard = (EditText) rootView.findViewById(R.id.edtHard);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        edtComment.setText(itemInventoryMap.getItemInventory().getComment().toString());
        edtAmount.setText(itemInventoryMap.getItemInventory().getAmount() + "");
        edtSoft.setText(itemInventoryMap.getItemInventory().getSoft() + "");
        edtHard.setText(itemInventoryMap.getItemInventory().getHard() + "");

        btnSave.setOnClickListener(saveItemInventoryListener);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarSaveIteminventory);
        backgroundLoading = (View) rootView.findViewById(R.id.backgroundLoading);
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

    private void clearText(){
        edtAmount.setText("");
        edtComment.setText("");
        edtHard.setText("");
        edtSoft.setText("");
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener saveItemInventoryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressBar.setVisibility(View.VISIBLE);
            backgroundLoading.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);

            final String comment = edtComment.getText().toString();
            final long amount = Integer.parseInt(edtAmount.getText().toString());
            final long soft = Long.parseLong(edtSoft.getText().toString());
            final long hard = Long.parseLong(edtHard.getText().toString());

            WriteBatch batch = db.batch();

            batch.update(dItem, "comment", comment);
            batch.update(dItem, "amount", amount);
            batch.update(dItem, "soft", soft);
            batch.update(dItem, "hard", hard);

            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        backgroundLoading.setVisibility(View.GONE);
                        btnSave.setVisibility(View.VISIBLE);

                        //clearText();
                        Toast.makeText(getActivity(), "Update Item Inventory Successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Update Item Inventory Failed", Toast.LENGTH_SHORT).show();
                        //clearText();
                    }
                }
            });


        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
