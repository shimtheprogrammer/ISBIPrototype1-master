package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PurchaseHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.FourEditELAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/10/2017.
 */

public class EditSalePriceActivity extends Activity {
    public static String TAG = "FloatFourELActivity";

    FourEditELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, String> idList = new HashMap<>();

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

    GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);
    PurchaseHandler pdb = new PurchaseHandler(this);
    InventoryHandler idb = new InventoryHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("0.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalment_display);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new FourEditELAdapter(this, listDataHeader,
                listDataChild, "SLP", true);

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

        // Adding child data
        List<String> sale = new ArrayList<>();

        List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pdb.getRowCount());

        String title = "Name-Cost-Total-Sale Price";
        //sale.add(Title);
        int count = 0;
        int tot = 0;
        for (GoodsPurchased pd : allGoods) {
            String name = pd.getProduct();
            if(name.matches("--Delivery--")){
                String del[] = name.split("-");
                name = del[2];
            }

            String unitsCost = pd.getUnit();
            double cost = pd.getCost();

            if(pd.getUnit().matches("Grms")){
                unitsCost = "Kgs";
                cost = cost * 1000;
            }
            if(pd.getUnit().matches("mLs")){
                unitsCost = "Ls";
                cost = cost * 1000;
            }
            Inventory inventory = idb.getInventoryByProduct(pd.getProduct());

            String displ = name + "-" + decimalFormat.format(cost) + " Per "
                    + unitsCost + "-" + numberFormat.format(pd.getTotal())
                    + "-" + numberFormat.format(inventory.getSPrice());

            sale.add(displ);
            tot = tot + pd.getTotal();
            idList.put("" + count, "" + pd.getId());
            count++;
        }
        String groupName;

        groupName = "Edit Sale Price; " + count;

        listDataHeader.add(groupName + "-" + title);

        listDataChild.put(listDataHeader.get(0), sale); // Header, Child data

    }
}