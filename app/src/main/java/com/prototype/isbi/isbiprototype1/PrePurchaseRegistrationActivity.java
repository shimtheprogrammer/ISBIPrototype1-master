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

import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrder;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 5/9/2017.
 */

public class PrePurchaseRegistrationActivity extends Activity {
    public static String TAG = "PrePrchsRegstrtActivity";

    SixELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String, String> idList2 = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    PreOrderHandler pdb = new PreOrderHandler(this);

    Button Next, Exit;
    LinearLayout buttonLayout, mainLayout, btnNextLayout, btnBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp2);

        Next = (Button) findViewById(R.id.btn_next);
        Exit = (Button) findViewById(R.id.btn_exit);
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

        Next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PayablesActivity.class);
                startActivity(intent);
                finish();

            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PreSaleRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // preparing list data
        prepareListData();

        listAdapter = new SixELAdapter(this, listDataHeader,
                listDataChild, "PREP", true);

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
                for (String Key : idList2.keySet()) {
                    if (Key.matches("" + (childPosition))) {
                        Intent intent = new Intent(getApplicationContext(), FloatFourELActivity.class);
                        intent.putExtra("FROM", "PPO");
                        intent.putExtra("ID", idList2.get(Key));
                        startActivity(intent);
                    }
                }

                return false;
            }
        });

        expListView.expandGroup(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        prepareListData();

        listAdapter = new SixELAdapter(this, listDataHeader,
                listDataChild, "PREP", true);

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

        List<String> preOrder = new ArrayList<>();

        String title = "ID-Name-Phone No.-Total-Payment Method-Due Date";
        //purchase.add(title);

        List<PreOrder> allPreOrder = pdb.getAllPreOrder();

        int count = 0;
        for (PreOrder pd : allPreOrder) {
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
                preOrder.add(log);

                String getid = "" + pd.getId();
                Log.d(TAG, "Append List; " + count + " / " + getid);
                idList2.put("" + count, "" + getid);
                count++;
            }
        }

        listDataHeader.add("Pre Purchase Orders: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), preOrder); // Header, Child data
    }
}