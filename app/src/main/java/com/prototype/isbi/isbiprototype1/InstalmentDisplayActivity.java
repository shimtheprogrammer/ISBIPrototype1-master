package com.prototype.isbi.isbiprototype1;

import android.app.Activity;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.View;

import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Instalment;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.FourELAdapter;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 2/16/2017. extends AppCompatActivity
 implements NavigationView.OnNavigationItemSelectedListener
 */

public class InstalmentDisplayActivity extends Activity {
    public static String TAG = "InstlmentDisplyActivity";

    FourELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    InstalmentHandler ildb = new InstalmentHandler(this);

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

        listAdapter = new FourELAdapter(this, listDataHeader,
                listDataChild, "INS", false);

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

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> assets = new ArrayList<>();

        List<Instalment> allAssets = ildb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

        //List<Instalment> allAssets = ildb.getAllInstalment();

        String title = "ID-Total-Method-Date";
        //assets.add(title);
        int count = 0;
        for (Instalment pd : allAssets) {
            String log = pd.getNumber() + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                    + "-" + pd.getDate();
            Log.d(TAG, "Insert: " + log);
            assets.add(log);
            count++;
        }

        listDataHeader.add("Instalments: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), assets); // Header, Child data
    }
}
