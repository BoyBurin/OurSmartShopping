package com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.boardcastreceiver.AlarmReceiver;
import com.example.seniorproject.smartshopping.controller.fragment.dialogfragment.DialogAddAlarmFragment;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.alarm.ItemAlarm;
import com.example.seniorproject.smartshopping.model.dao.alarm.ItemAlarmIdAction;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemAlarmIdListenerManager;
import com.example.seniorproject.smartshopping.view.adapter.iteminventory.ItemAlarmAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;


public class MoreItemInventoryAlarmFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ItemInventoryMap itemInventoryMap;

    private FloatingActionButton fab;
    private ListView listView;

    private ItemAlarmAdapter itemAlarmAdapter;
    private MutableInteger lastPositionInteger;
    private ItemAlarmIdListenerManager itemList;


    private FirebaseFirestore db;
    private CollectionReference cItemAlarm;
    private ListenerRegistration cItemAlarmListener;




    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreItemInventoryAlarmFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreItemInventoryAlarmFragment newInstance(ItemInventoryMap itemInventoryMap) {
        MoreItemInventoryAlarmFragment fragment = new MoreItemInventoryAlarmFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_more_item_inventory_alarm, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

        lastPositionInteger = new MutableInteger(-1);
        itemAlarmAdapter = new ItemAlarmAdapter(lastPositionInteger);
        itemList = new ItemAlarmIdListenerManager();

        db = FirebaseFirestore.getInstance();
        cItemAlarm = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items").document(itemInventoryMap.getId())
                .collection("alarm");

        cItemAlarmListener = cItemAlarm.addSnapshotListener(itemAlarmListener);



    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        listView = (ListView) rootView.findViewById(R.id.listViewAlarmList);
        listView.setAdapter(itemAlarmAdapter);

        fab.setOnClickListener(addAlarmListerner);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cItemAlarmListener != null){
            cItemAlarmListener.remove();
            cItemAlarmListener = null;
        }
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

    final View.OnClickListener addAlarmListerner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogAddAlarmFragment dialogAddAlarmFragment = DialogAddAlarmFragment.newInstance(itemInventoryMap);
            dialogAddAlarmFragment.show(getActivity().getSupportFragmentManager(), "alarm");
        }
    };

    final EventListener<QuerySnapshot> itemAlarmListener = new EventListener<QuerySnapshot>() {
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
                        final String id = documentSnapshot.getId();
                        ItemAlarm itemAlarm = documentSnapshot.toObject(ItemAlarm.class);
                        View.OnClickListener deleteListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cItemAlarm.document(id).delete().addOnSuccessListener(deleteAlarmSuccess)
                                        .addOnFailureListener(deleteAlarmFailed);
                            }
                        };
                        ItemAlarmIdAction newData = new ItemAlarmIdAction(itemAlarm, id, deleteListener);
                        itemList.addItemAlarm(newData);

                        itemAlarmAdapter.setGroups(itemList.getItemAlarms());
                        itemAlarmAdapter.notifyDataSetChanged();

                        AlarmManager alarmManager = (AlarmManager) getContext()
                                .getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("item", itemInventoryMap.getItemInventory());
                        intent.putExtra("item", bundle);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), (int)itemAlarm.getIndex(), intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, itemAlarm.getHour());
                        calendar.set(Calendar.MINUTE, itemAlarm.getMinute());
                        calendar.set(Calendar.SECOND, 0);

                        int dayNum = 0;
                        switch (itemAlarm.getDay()){
                            case "Sunday":
                                dayNum = Calendar.SUNDAY;
                                break;
                            case "Monday":
                                dayNum = Calendar.MONDAY;
                                break;
                            case "Tuesday":
                                dayNum = Calendar.TUESDAY;
                                break;
                            case "Wednesday":
                                dayNum = Calendar.WEDNESDAY;
                                break;
                            case "Thursday":
                                dayNum = Calendar.THURSDAY;
                                break;
                            case "Friday":
                                dayNum = Calendar.FRIDAY;
                                break;
                            case "Saturday":
                                dayNum = Calendar.SATURDAY;
                                break;
                        }
                        calendar.set(Calendar.DAY_OF_WEEK, dayNum);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, itemAlarm.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY * 7, alarmIntent);




                        break;
                    case MODIFIED:

                        break;
                    case REMOVED:

                        documentSnapshot = dc.getDocument();
                        itemAlarm = documentSnapshot.toObject(ItemAlarm.class);
                        String key = documentSnapshot.getId();
                        itemList.removeItemAlarm(key);

                        itemAlarmAdapter.setGroups(itemList.getItemAlarms());
                        itemAlarmAdapter.notifyDataSetChanged();

                        alarmManager = (AlarmManager) getContext()
                                .getSystemService(Context.ALARM_SERVICE);
                        intent = new Intent(getContext(), AlarmReceiver.class);
                        alarmIntent = PendingIntent.getBroadcast(getContext(), (int)itemAlarm.getIndex(), intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.cancel(alarmIntent);
                        Toast.makeText(getContext(), "Delete " + itemAlarm.getIndex() + "Successful", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    final OnSuccessListener<Void> deleteAlarmSuccess = new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Toast.makeText(getContext(), "Delete Alarm Successful", Toast.LENGTH_SHORT).show();
            Log.d("TAG"," successfully deleted!");
        }
    };

    final OnFailureListener deleteAlarmFailed = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.d("TAG", "Deleted Error " );
        }
    };



    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
