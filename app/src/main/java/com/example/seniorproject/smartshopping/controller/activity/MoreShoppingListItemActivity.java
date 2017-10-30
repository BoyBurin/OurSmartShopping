package com.example.seniorproject.smartshopping.controller.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment.MoreShoppingListItemFragment;
import com.example.seniorproject.smartshopping.model.dao.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class MoreShoppingListItemActivity extends AppCompatActivity {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    final String MORE_SHOOPING_LIST_ITEM = "moreShoppingListItem";

    private ShoppingListMap shoppingListMap;
    private int position;

    private FirebaseFirestore db;
    private CollectionReference cItemShoppingList;


    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_shopping_list_item);

        position = getIntent().getIntExtra("position", 0);
        shoppingListMap = getIntent().getParcelableExtra("shoppingListMap");

        initInstances();

        if(savedInstanceState == null){
            MoreShoppingListItemFragment moreShoppingListItemFragment = MoreShoppingListItemFragment.newInstance(shoppingListMap, position);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerMoreShoppingListItem, moreShoppingListItemFragment,
                            MORE_SHOOPING_LIST_ITEM)
                    .commit();
        }
    }

    private void initInstances() {
        setTitle(shoppingListMap.getShoppingList().getName().toString());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        if(item.getItemId() == R.id.action_delete){
            deleteShoppingList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    private void deleteShoppingList(){
        String groupId = GroupManager.getInstance().getCurrentGroup().getId();
        String shoppingListId = shoppingListMap.getId();

        cItemShoppingList = db.collection("groups").document(groupId)
                .collection("shoppinglists").document(shoppingListId)
                .collection("items");

        cItemShoppingList.get().addOnCompleteListener(deleteItemShoppingList);
    }



    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/

    final OnCompleteListener<QuerySnapshot> deleteItemShoppingList = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                WriteBatch batch = db.batch();

                for (DocumentSnapshot document : task.getResult()) {
                    DocumentReference dItemShoppingList = cItemShoppingList.document(document.getId());
                    batch.delete(dItemShoppingList);
                }

                String groupId = GroupManager.getInstance().getCurrentGroup().getId();
                DocumentReference dShoppingList = db.collection("groups").document(groupId)
                        .collection("shoppinglists").document(shoppingListMap.getId());

                batch.delete(dShoppingList);

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            String name = shoppingListMap.getShoppingList().getName();
                            Toast.makeText(MoreShoppingListItemActivity.this, "Delete " + name + "Success", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                        else{
                            Log.w("TAG", "Error delete ShoppingList.", task.getException());
                        }
                    }
                });


            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        }
    };

    /***********************************************************************************************
     ************************************* Implement Methods ********************************************
     ***********************************************************************************************/
}
