package com.example.seniorproject.smartshopping.superuser;

import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuperUserItemActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private DatabaseReference mDatabaseRef;

    private TextView tvShowBarcode;
    private Button btnAddBarcode;
    private EditText edtName;
    private EditText edtRetailPrice;
    private EditText edtType;
    private EditText edtUnit;

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
        fab.setOnClickListener(addItemListener);

        tvShowBarcode = (TextView) findViewById(R.id.tvShowBarcode);
        btnAddBarcode = (Button) findViewById(R.id.btnAddBarcode);
        edtName = (EditText) findViewById(R.id.edtName);
        edtRetailPrice = (EditText) findViewById(R.id.edtRetailPrice);
        edtType = (EditText) findViewById(R.id.edtType);
        edtUnit = (EditText) findViewById(R.id.edtUnit);
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
}
