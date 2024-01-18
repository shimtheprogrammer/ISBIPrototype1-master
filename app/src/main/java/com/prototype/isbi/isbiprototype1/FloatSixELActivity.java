package com.prototype.isbi.isbiprototype1;

import android.app.Activity;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.View;

import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGains;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUp;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/25/2017.
 */

public class FloatSixELActivity extends Activity{
    public static String TAG = "FloatSixELActivity";

    SixELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    DecimalFormat decimalFormat = new DecimalFormat("0.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalment_display);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new SixELAdapter(this, listDataHeader,
                listDataChild, "FSE", false);
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

        if (getIntent().getStringExtra("WHAT").matches("BUI")){
            expListView.expandGroup(0);
        }
        /**
         * end of ecpandable implementing
         **/
    }

    // populatinf expandable list
    private void prepareListData() {
        if (getIntent().getStringExtra("FROM").matches("SAS")) {
            AssetGainsHandler agdb = new AssetGainsHandler(this);
            SoldAssetsHandler adb = new SoldAssetsHandler(this);

            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> assetList = new ArrayList<>();

            SoldAssets assets = adb.getAssets(Integer.parseInt(getIntent().getStringExtra("ID")));
            List<AssetGains> assetGains = agdb.getByForeignKey(assets.gettForeignKey());
            int tot = 0;
            for (AssetGains pd: assetGains) {
                if (!(pd.getCustomer().matches("INV"))) {
                    tot = pd.getTotal();
                    String log;
                    if (tot < 0) {
                        log = assets.getType() + "-" + assets.getTotal() + "-" + pd.getCustomer()
                                + numberFormat.format(pd.getTotal())
                                + "-" + pd.getPayment() + "-" + pd.getDate();
                    } else {
                        log = assets.getType() + "-" + assets.getTotal() + "-" + pd.getCustomer()
                                + "-" + numberFormat.format(pd.getTotal())
                                + "-" + pd.getPayment() + "-" + pd.getDate();
                    }
                    Log.d(TAG, "Insert: " + log);
                    assetList.add(log);
                }
            }

            List<AssetGains> assetGains2 = agdb.getAllAssetGains();
            for (AssetGains pd : assetGains2) {
                String log = pd.getId() + "-" + pd.getCustomer()
                        + "-" + numberFormat.format(pd.getTotal())
                        + "-" + pd.getPayment() + "-" + pd.getDate()
                        + "-" + pd.gettForeignKey();
                Log.d(TAG, "Check All: " + log);
            }
            Log.d(TAG, "ID " + getIntent().getStringExtra("ID"));

            if (tot > 0) {
                String title = "Asset-Original $-To-Gains-Method-Date";
                listDataHeader.add("Gains on Assets Sale" + "-" + title);

            } else {
                String title = "Asset-Original $-To-Loss-Method-Date";
                listDataHeader.add("Loss on Assets Sale" + "-" + title);

            }

            listDataChild.put(listDataHeader.get(0), assetList); // Header, Child data

            if (getIntent().getStringExtra("WHAT").matches("BUI")) {
                ReUpHandler edb = new ReUpHandler(this);

                listDataHeader = new ArrayList<>();
                listDataChild = new HashMap<>();

                List<String> upgradeList = new ArrayList<>();

                List<ReUp> allUpgrades = edb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

                int count = 0;
                for (ReUp pd : allUpgrades) {
                    String log = assets.getInfo() + "-" + pd.getID() + "-" + pd.getInfo()
                            + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                            + "-" + pd.getDate();
                    Log.d(TAG, "Insert: " + log);
                    upgradeList.add(log);
                    count++;
                }

                if (count > 0) {
                    String title = "Building-Upgrade ID-Info-Total-Method-Date";
                    listDataHeader.add("Upgrades on Buiding: " + count + "-" + title);

                } else {
                    listDataHeader.add("No Upgrades Available");
                }

                listDataChild.put(listDataHeader.get(0), upgradeList); // Header, Child data
            }
        }
    }
}