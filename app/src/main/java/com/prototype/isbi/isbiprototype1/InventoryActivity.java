package com.prototype.isbi.isbiprototype1;

import android.app.Activity;

import android.content.Intent;

import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.DecimalFormat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 12/16/2016.
 */

public class InventoryActivity extends Activity{
    public static String TAG = "InventoryActivity";

    SixELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, String> idList = new HashMap<>();

    Button Next, Exit;
    LinearLayout buttonLayout, mainLayout, btnNextLayout, btnBackLayout;
//    TextView title;

    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("#.###");
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        //  record = new RecordData();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp2);

        Next = (Button) findViewById(R.id.btn_next);
        Exit = (Button) findViewById(R.id.btn_exit);
//        title = (TextView) findViewById(R.id.title);
        buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        btnBackLayout = (LinearLayout) findViewById(R.id.btnBackLayout);
        btnNextLayout = (LinearLayout) findViewById(R.id.btnNxtLayout);

        int holoBlueBright = getApplication().getResources().getColor(R.color.holoBlueBright);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueBright);
        drawable.setSize(2, 2);

        buttonLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mainLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable2 = new GradientDrawable();
        drawable2.setColor(holoBlueBright);
        drawable2.setSize(1, 1);
        buttonLayout.setDividerDrawable(drawable2);
        mainLayout.setDividerDrawable(drawable2);

        //creating listener and event for Submit button;
        Exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnBackLayout.setBackgroundResource(R.color.halfSeeThrough);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnBackLayout.setBackgroundResource(R.color.seeThrough);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        Next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnNextLayout.setBackgroundResource(R.color.halfSeeThrough);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnNextLayout.setBackgroundResource(R.color.seeThrough);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

//        Next.setBackgroundResource(R.drawable.see_through_button);
//        Next.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Next.setBackgroundResource(R.drawable.half_see_through_button);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        Next.setBackgroundResource(R.drawable.see_through_button);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        Next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PreSaleRegistrationActivity.class);
                startActivity(intent);
                finish();

            }
        });

//        Exit.setBackgroundResource(R.drawable.see_through_button);
//        Exit.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Exit.setBackgroundResource(R.drawable.half_see_through_button);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        Exit.setBackgroundResource(R.drawable.see_through_button);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        Exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CashActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // preparing list data
        prepareListData();

        listAdapter = new SixELAdapter(this, listDataHeader,
                listDataChild, "INV2", true);

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

                return false;
            }
        });

        expListView.expandGroup(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // preparing list data
        prepareListData();

        listAdapter = new SixELAdapter(this, listDataHeader,
                listDataChild, "INV2", true);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.expandGroup(0);

    }

    /*
         * Preparing the list data
         */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> inventory = new ArrayList<>();

        InventoryHandler db = new InventoryHandler(this);

        List<Inventory> allInventory = db.getAllInventory();

        String title = "ID-Product-Quantity-Cost-Total-Sale Price";
        //inventory.add(titles);
        int count = 0;
        for (Inventory pd : allInventory) {
            double numbe = pd.getNumber();
            String units = pd.getUnit();
            String unitsCost = pd.getUnit();
            double cost = pd.getCost();

            if(numbe >= 1000 && !units.matches("Pcs")){
                numbe = numbe / 1000;
                if(pd.getUnit().matches("Grms")){
                    units = "Kgs";
                }
                if(pd.getUnit().matches("mLs")){
                    units = "Ls";
                }
            }

            if(!units.matches("Pcs")) {
                cost = cost * 1000;
            }

            if(pd.getUnit().matches("Grms")){
                unitsCost = "Kgs";
            }
            if(pd.getUnit().matches("mLs")){
                unitsCost = "Ls";
            }
            String log =  pd.getID() + "-" + pd.getProduct() + "-" + decimalFormat2.format(numbe) + " " + units +
                    "-" + decimalFormat.format(cost) + " Per " + unitsCost + "-"
                    + numberFormat.format(pd.getTotal()) + "-" + numberFormat.format(pd.getSPrice());
            // Writing Contacts to log
            Log.d(TAG, "Data" + log);
            inventory.add(log);
            Log.d(TAG, "Adding to list; " + count + " / " + pd.getID());
            idList.put("" + count, "" + pd.getID());
            count++;
        }

        listDataHeader.add("Inventory: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), inventory); // Header, Child data
    }
}
