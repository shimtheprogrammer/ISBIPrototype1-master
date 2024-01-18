package com.prototype.isbi.isbiprototype1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrder;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Purchase;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PurchaseHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/10/2017.
 */

public class PurchasesDisplayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static String TAG = "PurchaseDisplayActivity";

    SixELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String, String> idList = new HashMap<>();
    HashMap<String, String> idList2 = new HashMap<>();

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    PurchaseHandler pdb = new PurchaseHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new SixELAdapter(this, listDataHeader,
                listDataChild, "PUR", false);

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
                if(groupPosition == 0) {
                    for (String Key : idList.keySet()) {
                        if (Key.matches("" + (childPosition + "-1"))) {
                            Intent intent = new Intent(getApplicationContext(), FloatFourELActivity.class);
                            intent.putExtra("FROM", "PUR");
                            intent.putExtra("ID", idList.get(Key));
                            startActivity(intent);
                        }
                    }
                } else if (groupPosition == 1) {
                    for (String Key : idList2.keySet()) {
                        if (Key.matches("" + (childPosition + "-2"))) {
                            Intent intent = new Intent(getApplicationContext(), FloatFourELActivity.class);
                            intent.putExtra("FROM", "PPU");
                            intent.putExtra("ID", idList2.get(Key));
                            startActivity(intent);
                        }
                    }
                }

                return false;
            }
        });

        expListView.expandGroup(0);
        expListView.expandGroup(1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        navigationItemSelected.NavigationItem(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> purchase = new ArrayList<>();

        List<Purchase> allPurchases = pdb.getAllPurchase();

        String title = "ID-Supplier-Pnone No.-Total-Method-Date";
        //purchase.add(title);
        int count = 0;
        for (Purchase pd : allPurchases) {
            String name[] = pd.getSupplier().split("-");
            String names = name[0];
            String logPhone = "";
            if(name.length > 1){
                logPhone = name[name.length - 1];
            }
            String log = pd.getId() + "-" + names + "-" + logPhone
                    + "-" + numberFormat.format(pd.getTotal())
                    + "-" + pd.getPayment() + "-" + pd.getDate();
            Log.d(TAG, "Insert" + log);
            purchase.add(log);
            idList.put("" + count + "-1", "" + pd.getId());
            count++;
        }

        List<String> preOrder = new ArrayList<String>();
        List<PreOrder> preOrders = podb.getAllPreOrder();

        String title2 = "ID-Supplier-Pnone No.-Total-Method-Date";
        //preOrder.add(prePurchase);
        int count2 = 0;
        for (PreOrder pd : preOrders) {
            if(pd.getStatus().matches("CLOSED")) {
                String name[] = pd.getSupplier().split("-");
                String names = name[0];
                String logPhone = "";
                if(name.length > 1){
                    logPhone = name[name.length - 1];
                }

                String log = pd.getId() + "-" + names + "-" + logPhone
                        + "-" + numberFormat.format(pd.getTotal())
                        + "-" + pd.getPayment() + "-" + pd.getDate();
                Log.d(TAG, "Insert; " + log);
                preOrder.add(log);
                idList2.put("" + count2 + "-2", "" + pd.getId());
                count2++;
            }
        }

        listDataHeader.add("Purchases: " + count + "-" + title);
        listDataHeader.add("Fulfilled Pre Purchases: " + count2 + "-" + title2);

        listDataChild.put(listDataHeader.get(0), purchase); // Header, Child data
        listDataChild.put(listDataHeader.get(1), preOrder); // Header, Child data
    }
}
