package com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.view.specialview.SlidingTabLayout;


public class MoreItemInventoryFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ViewPager viewPager;
    private ItemInventoryMap itemInventoryMap;
    private SlidingTabLayout slidingTabLayout;

    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreItemInventoryFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreItemInventoryFragment newInstance(ItemInventoryMap itemInventoryMap) {
        MoreItemInventoryFragment fragment = new MoreItemInventoryFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_more_item_inventory, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int page) {
                switch (page) {
                    case 0:
                        return MoreItemInventoryPhotoSummaryFragment.newInstance(itemInventoryMap);
                    case 1:
                        return MoreItemInventoryEditFragment.newInstance(itemInventoryMap);
                    case 2:
                        return MoreItemInventoryAlarmFragment.newInstance(itemInventoryMap);
                    default:
                        return null;

                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Summary";
                    case 1:
                        return "Update Item";
                    case 2:
                        return "Item Alarm";
                    default:
                        return "";
                }
            }
        });

        hideKeyboard();

        slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setViewPager(viewPager);
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

    private void hideKeyboard(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    /*final FragmentStatePagerAdapter fragmentStatePagerAdapter =
            new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position){
                        case 0:
                            return MoreItemInventoryPhotoSummaryFragment.newInstance();
                        case 1:
                            return MoreItemInventoryEditFragment.newInstance();
                        default:
                            return null;

                    }
                }

                @Override
                public int getCount() {
                    return 2;
                }
            };*/


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
