package com.prototype.isbi.isbiprototype1.fragments;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.R;

import com.prototype.isbi.isbiprototype1.ClosePreActivity;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrder;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 3/9/2017.
 */

public class PrePurchaseDisplayFragment extends Fragment {
    public static String TAG = "PrePurchaseFragment";

    SixELAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String , String> idList = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get the listview
        final View view = inflater.inflate(R.layout.fragment_pre_purchase_display, container, false);
        final ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

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
                intent.putExtra("FROM", "ORDER");
                intent.putExtra("TEXT", "" + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition));

                for(String Key : idList.keySet()){
                    if(Integer.parseInt(Key) == childPosition) {
                        Log.d(TAG, "Values; " + Key + " is same " + childPosition + " has " + idList.get(Key));
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
        PreOrderHandler pdb = new PreOrderHandler(activity.getApplicationContext());

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> purchase = new ArrayList<>();

        String title = "ID-Name-Phone No.-Total-Payment Method-Due Date";
        //purchase.add(title);

        List<PreOrder> allPurchases = pdb.getAllPreOrder();

        int count = 0;
        for (PreOrder pd : allPurchases) {
            if(pd.getStatus().matches("OPEN")) {
                String name[] = pd.getSupplier().split("-");
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
                Log.d(TAG, "Append List; " + count + " / " + getid);
                idList.put("" + count, "" + getid);
                count++;
            }
        }

        listDataHeader.add("Pre Purchases: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), purchase); // Header, Child data
    }
}
