package com.example.seniorproject.smartshopping.controller.fragment.dialogfragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.alarm.ItemAlarm;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class DialogAddAlarmFragment extends DialogFragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private Button btnAdd;
    private Button btnCancel;
    private Spinner spinner;
    private TimePicker timePicker;
    private ArrayAdapter<String> adapter;

    private FirebaseFirestore db;
    private DocumentReference dAlarm;
    private CollectionReference cAlarm;

    private Calendar calendar;

    private int day;
    private String dayName;
    private long indexState;
    private ItemInventoryMap itemInventoryMap;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public DialogAddAlarmFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static DialogAddAlarmFragment newInstance(ItemInventoryMap itemInventoryMap) {
        DialogAddAlarmFragment fragment = new DialogAddAlarmFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_dialog_add_alarm, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        calendar = Calendar.getInstance();
        day = calendar.SUNDAY;
        dayName = "Sunday";

        adapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        db = FirebaseFirestore.getInstance();
        dAlarm = db.collection("sharestate").document("alarm");
        cAlarm = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items").document(itemInventoryMap.getId())
                .collection("alarm");

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        spinner = (Spinner) rootView.findViewById(R.id.spinnerDay);
        timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);

        spinner.setAdapter(adapter);
        btnCancel.setOnClickListener(cancelListener);
        btnAdd.setOnClickListener(addAlarmListener);
        spinner.setOnItemSelectedListener(selectedDayListener);

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

    private void closeDialog(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(DialogAddAlarmFragment.this)
                .commit();
    }


    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    private final AdapterView.OnItemSelectedListener selectedDayListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position){
                case 0 :
                    day = calendar.SUNDAY;
                    dayName = "Sunday";
                    break;
                case 1 :
                    day = calendar.MONDAY;
                    dayName = "Monday";
                    break;
                case 2 :
                    day = calendar.TUESDAY;
                    dayName = "Tuesday";
                    break;
                case 3 :
                    day = calendar.WEDNESDAY;
                    dayName = "Wednesday";
                    break;
                case 4 :
                    day = calendar.THURSDAY;
                    dayName = "Thursday";
                    break;
                case 5 :
                    day = calendar.FRIDAY;
                    dayName = "Friday";
                    break;
                case 6 :
                    day = calendar.SATURDAY;
                    dayName = "Saturday";
                    break;
            }
        };

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private final View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeDialog();
        }
    };


    private final View.OnClickListener addAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dAlarm.get().addOnCompleteListener(getAlarmIndex);
        }
    };

    private final OnCompleteListener<DocumentSnapshot> getAlarmIndex = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.isSuccessful()){
                DocumentSnapshot data = task.getResult();
                indexState = data.getLong("index");

                int hour = 0;
                int minute = 0;

                if(Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }

                //calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(java.util.Calendar.DAY_OF_WEEK, day);

                Long currentTimeInMillis = calendar.getTimeInMillis();

                ItemAlarm itemAlarm = new ItemAlarm(dayName, hour, minute , currentTimeInMillis, indexState);


                cAlarm.add(itemAlarm).addOnCompleteListener(addAlarmSuccessful);


            }
            else{
                Log.d("TAG", "Get alarm share state failed");
            }
        }
    };


    private OnCompleteListener<DocumentReference> addAlarmSuccessful = new OnCompleteListener<DocumentReference>() {
        @Override
        public void onComplete(@NonNull Task<DocumentReference> task) {
            if(task.isSuccessful()){
                Map<String, Object> update = new HashMap<>();
                indexState = indexState + 1;
                update.put("index", indexState);
                dAlarm.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Add Alarm Sucessful", Toast.LENGTH_SHORT).show();
                            closeDialog();
                        }
                        else{
                            Log.d("TAG", "Update share state failed");
                        }
                    }
                });
            }
            else{
                Log.d("TAG", "Add alarmfailed");
            }
        }
    };






    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
