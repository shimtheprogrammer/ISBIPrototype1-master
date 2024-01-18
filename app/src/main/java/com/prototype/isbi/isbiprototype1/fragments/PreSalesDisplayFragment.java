package com.prototype.isbi.isbiprototype1.fragments;

import android.app.Activity;

import android.content.Intent;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.R;
import com.prototype.isbi.isbiprototype1.ClosePreActivity;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSale;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 3/9/2017.
 */

public class PreSalesDisplayFragment extends Fragment {

    public static String TAG = "PreSalesFragment";

    SixELAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String , String> idList = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

//    public static PreSalesDisplayFragment newInstance() {
//        return new PreSalesDisplayFragment();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pre_sales_display, container, false);
        final ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // preparing list data
        prepareListData(getActivity());

        listAdapter = new SixELAdapter(getActivity(), listDataHeader,
                listDataChild, "ORD", false);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(getActivity(), ClosePreActivity.class);
                intent.putExtra("FROM", "SALE");
                intent.putExtra("TEXT", "" + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition));

                for(String Key : idList.keySet()){
                    if(Key.matches("" + childPosition)) {
                        intent.putExtra("ID", "" + (idList.get(Key)));
                    }
                }

                startActivity(intent);

                return false;
            }
        });

        expListView.expandGroup(0);

        return view;
    }

    private void prepareListData(Activity activity) {
        PreSaleHandler psdb = new PreSaleHandler(activity.getApplicationContext());

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> purchase = new ArrayList<>();

        String title = "ID-Name-Phone No.-Total-Payment Method-Due Date";
        //purchase.add(title);

        List<PreSale> allPurchases = psdb.getAllPreSale();

        int count = 0;
        for (PreSale pd : allPurchases) {
            if(pd.getStatus().matches("OPEN")) {
                String name[] = pd.getCustomer().split("-");
                String names = name[0];
                String logPhone = "";
                if(name.length > 1){
                    logPhone = name[name.length - 1];
                }
                String log = pd.getId() + "-" + names + "-" + logPhone
                        + "-" + numberFormat.format(pd.getTotal())
                        + "-" + pd.getPayment() + "-" + pd.getOriginalDate();
                Log.d(TAG, "Values Added; " + log);
                purchase.add(log);

                String getid = "" + pd.getId();
                idList.put("" + count, "" + getid);
                count++;
            }
        }

        listDataHeader.add("Pre Sales: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), purchase); // Header, Child data
    }
}
