package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Delivery;
import com.prototype.isbi.isbiprototype1.databaseHandlers.DeliveryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUp;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.FourELAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/10/2017.
 */

public class FloatFourELActivity extends Activity {
    public static String TAG = "FloatFourELActivity";

    FourELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

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

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new FourELAdapter(this, listDataHeader,
                listDataChild, "FFE", false);

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
        if(getIntent().getStringExtra("FROM").matches("PUR")) {
            expListView.expandGroup(1);
        }
    }

    private void prepareListData() {

        if(getIntent().getStringExtra("FROM").matches("SAL")) {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> goodsSale = new ArrayList<>();

            GoodsSalesHandler db = new GoodsSalesHandler(this);

            List<GoodsSales> allGoods = db.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            String title = "Product-Quantity-Price Each-Total";
            int count = 0;
            for (GoodsSales pd : allGoods) {
                String product = pd.getProduct();

                if (product.matches("--Delivery--")) {
                    product = "Delivery";
                }

                double numbe = pd.getNumber();
                String units = pd.getUnit();
                String unitsCost = pd.getUnit();
                double cost = pd.getCost();

                if (numbe >= 1000 && !units.matches("Pcs")) {
                    numbe = numbe / 1000;
                    if (pd.getUnit().matches("Grms")) {
                        units = "Kgs";
                    }
                    if (pd.getUnit().matches("mLs")) {
                        units = "Ls";
                    }
                }

                if (pd.getUnit().matches("Grms")) {
                    unitsCost = "Kgs";
                    cost = cost * 1000;
                }
                if (pd.getUnit().matches("mLs")) {
                    unitsCost = "Ls";
                    cost = cost * 1000;
                }

                String log = product + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                        + decimalFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
                Log.d("Insert: ", log);
                goodsSale.add(log);
                count++;
            }

            listDataHeader.add("Goods Sold: " + count + "-" + title);

            listDataChild.put(listDataHeader.get(0), goodsSale); // Header, Child data
        }

        if(getIntent().getStringExtra("FROM").matches("PSA")) {
            GoodsPreSoldHandler db = new GoodsPreSoldHandler(this);

            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> goodsSale = new ArrayList<>();

            List<GoodsPreOrdered> allGoods = db.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            String title = "Product-Quantity-Price Each-Total";
            int count = 0;
            for (GoodsPreOrdered pd : allGoods) {
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

                if(pd.getUnit().matches("Grms")){
                    unitsCost = "Kgs";
                    cost = cost * 1000;
                }
                if(pd.getUnit().matches("mLs")){
                    unitsCost = "Ls";
                    cost = cost * 1000;
                }

                String log = pd.getProduct() + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                        + decimalFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
                Log.d("Insert: ", log);
                goodsSale.add(log);
                count++;
            }

            listDataHeader.add("Goods Pre Sold: " + count + "-" + title);

            listDataChild.put(listDataHeader.get(0), goodsSale); // Header, Child data
        }

        if(getIntent().getStringExtra("FROM").matches("PSO")) {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> goodsPreSale = new ArrayList<>();

            GoodsPreSoldHandler db = new GoodsPreSoldHandler(this);

            List<GoodsPreOrdered> allGoods = db.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            String title = "Product-Quantity-Price Each-Total";
            int count = 0;
            for (GoodsPreOrdered pd : allGoods) {
                String product = pd.getProduct();

                if (product.matches("--Delivery--")) {
                    product = "Delivery";
                }

                double numbe = pd.getNumber();
                String units = pd.getUnit();
                String unitsCost = pd.getUnit();
                double cost = pd.getCost();

                if (numbe >= 1000 && !units.matches("Pcs")) {
                    numbe = numbe / 1000;
                    if (pd.getUnit().matches("Grms")) {
                        units = "Kgs";
                    }
                    if (pd.getUnit().matches("mLs")) {
                        units = "Ls";
                    }
                }

                if (pd.getUnit().matches("Grms")) {
                    unitsCost = "Kgs";
                    cost = cost * 1000;
                }
                if (pd.getUnit().matches("mLs")) {
                    unitsCost = "Ls";
                    cost = cost * 1000;
                }

                String log = product + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                        + decimalFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
                Log.d("Insert: ", log);
                goodsPreSale.add(log);
                count++;
            }

            listDataHeader.add("Goods Pre Sold: " + count + "-" + title);

            listDataChild.put(listDataHeader.get(0), goodsPreSale); // Header, Child data
        }

        if(getIntent().getStringExtra("FROM").matches("PPO")) {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> goodsPreBought = new ArrayList<>();

            GoodsPreOrderedHandler db = new GoodsPreOrderedHandler(this);

            List<GoodsPreOrdered> allGoods = db.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            String title = "Product-Quantity-Price Each-Total";
            int count = 0;
            for (GoodsPreOrdered pd : allGoods) {
                String product = pd.getProduct();

                if (product.matches("--Delivery--")) {
                    product = "Delivery";
                }

                double numbe = pd.getNumber();
                String units = pd.getUnit();
                String unitsCost = pd.getUnit();
                double cost = pd.getCost();

                if (numbe >= 1000 && !units.matches("Pcs")) {
                    numbe = numbe / 1000;
                    if (pd.getUnit().matches("Grms")) {
                        units = "Kgs";
                    }
                    if (pd.getUnit().matches("mLs")) {
                        units = "Ls";
                    }
                }

                if (pd.getUnit().matches("Grms")) {
                    unitsCost = "Kgs";
                    cost = cost * 1000;
                }
                if (pd.getUnit().matches("mLs")) {
                    unitsCost = "Ls";
                    cost = cost * 1000;
                }

                String log = product + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                        + decimalFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
                Log.d("Insert: ", log);
                goodsPreBought.add(log);
                count++;
            }

            listDataHeader.add("Goods Pre Purchased: " + count + "-" + title);

            listDataChild.put(listDataHeader.get(0), goodsPreBought); // Header, Child data
        }

        if(getIntent().getStringExtra("FROM").matches("PUR")) {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> goodsPreBought = new ArrayList<>();

            GoodsPurchasedHandler db = new GoodsPurchasedHandler(this);

            List<GoodsPurchased> allGoods = db.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            String title = "Product-Quantity-Price Each-Total";
            int count = 0;
            for (GoodsPurchased pd : allGoods) {
                String product = pd.getProduct();

                if (product.matches("--Delivery--")) {
                    product = "Delivery";
                }

                double numbe = pd.getNumber();
                String units = pd.getUnit();
                String unitsCost = pd.getUnit();
                double cost = pd.getCost();

                if (numbe >= 1000 && !units.matches("Pcs")) {
                    numbe = numbe / 1000;
                    if (pd.getUnit().matches("Grms")) {
                        units = "Kgs";
                    }
                    if (pd.getUnit().matches("mLs")) {
                        units = "Ls";
                    }
                }

                if (pd.getUnit().matches("Grms")) {
                    unitsCost = "Kgs";
                    cost = cost * 1000;
                }
                if (pd.getUnit().matches("mLs")) {
                    unitsCost = "Ls";
                    cost = cost * 1000;
                }

                String log = product + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                        + decimalFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
                Log.d("Insert: ", log);
                goodsPreBought.add(log);
                count++;
            }

            DeliveryHandler ddb = new DeliveryHandler(this);

            List<String> delivery = new ArrayList<>();
            List<Delivery> deliveryList = ddb.getAllDelivery();

            String heading = "Not Yet Delivered";
            String title2 = "Allocation by-Amount-Payment Method-Date";
            boolean flag = false;
            for (Delivery pd: deliveryList){
                String idList[] = pd.getForeignKey().split("-");

                for (String i : idList){
                    if (Integer.parseInt(i) == Integer.parseInt(getIntent().getStringExtra("ID"))){
                        if (pd.getAllocation().matches("None")) {
                            flag = true;

                        } else {
                            heading = "Delivery";
                            String log = pd.getAllocation() + "-" + numberFormat.format(pd.getTotal()) + "-"
                                    + pd.getPaymentMethode() + "-" + pd.getDate();
                            Log.d("Insert: ", log);
                            delivery.add(log);

                        }
                    }
                }
            }

            if (flag){
                heading = "NO Delivery Required";
                delivery.remove(title2);
            }

            if (!heading.matches("Delivery")){
                delivery.remove(title2);
            }

            listDataHeader.add("Goods Purchased: " + count + "-" + title);
            listDataHeader.add(heading + "-" + title2);

            listDataChild.put(listDataHeader.get(0), goodsPreBought); // Header, Child data
            listDataChild.put(listDataHeader.get(1), delivery); // Header, Child data
        }

        if(getIntent().getStringExtra("FROM").matches("PPU")) {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> goodsPreBought = new ArrayList<>();

            GoodsPreOrderedHandler db = new GoodsPreOrderedHandler(this);

            List<GoodsPreOrdered> allGoods = db.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            String title = "Product-Quantity-Price Each-Total";
            int count = 0;
            for (GoodsPreOrdered pd : allGoods) {
                String product = pd.getProduct();

                if (product.matches("--Delivery--")) {
                    product = "Delivery";
                }

                double numbe = pd.getNumber();
                String units = pd.getUnit();
                String unitsCost = pd.getUnit();
                double cost = pd.getCost();

                if (numbe >= 1000 && !units.matches("Pcs")) {
                    numbe = numbe / 1000;
                    if (pd.getUnit().matches("Grms")) {
                        units = "Kgs";
                    }
                    if (pd.getUnit().matches("mLs")) {
                        units = "Ls";
                    }
                }

                if (pd.getUnit().matches("Grms")) {
                    unitsCost = "Kgs";
                    cost = cost * 1000;
                }
                if (pd.getUnit().matches("mLs")) {
                    unitsCost = "Ls";
                    cost = cost * 1000;
                }

                String log = product + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                        + decimalFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
                Log.d("Insert: ", log);
                goodsPreBought.add(log);
                count++;
            }

            listDataHeader.add("Goods Pre Purchased: " + count + "-" + title);

            listDataChild.put(listDataHeader.get(0), goodsPreBought); // Header, Child data
        }

        if(getIntent().getStringExtra("FROM").matches("DEL")) {
            DeliveryHandler ddb = new DeliveryHandler(this);
            GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);

            Delivery delivery = ddb.getDelivery(Integer.parseInt(getIntent().getStringExtra("ID")));

            String allocationType = delivery.getAllocation();
            int globalTotal = delivery.getTotal();
            String idList[] = delivery.getForeignKey().split("-");
            List<String> selected = new ArrayList<>();

            for (String i : idList){
                selected.add(i);
            }

            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> purchases = new ArrayList<>();

            String title = "";

            int count = 0;
            if(allocationType.matches("Volume")){
                title = "ID-Product-Volume-Cost";
                double numb = 0;
                for(String Key : selected){
                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                    for (GoodsPurchased pd1 : allGoods) {

                        if(pd1.getUnit().matches("Pcs")){
                            Log.d(TAG, "prepareListData, Volume, Pcs " + pd1.getNumber());
                            numb = numb + pd1.getNumber();
                        } else {
                            numb = numb + (pd1.getNumber() / 1000);
                            Log.d(TAG, "prepareListData, Volume, else " + (pd1.getNumber() / 1000));
                        }
                    }

                }

                for(String Key : selected) {
                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                    for (GoodsPurchased pd : allGoods) {
                        double original = pd.getNumber();

                        if(!(pd.getUnit().matches("Pcs"))){
                            original = (pd.getNumber() / 1000);
                        }

                        double _dcost = (original / numb) * globalTotal;
                        count++;
                        //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                        int numberTot = pd.getNumber();
                        String unitsCost = pd.getUnit();

                        if(!pd.getUnit().matches("Pcs")) {
                            numberTot = numberTot / 1000;
                        }

                        if(pd.getUnit().matches("Grms")){
                            unitsCost = "Kgs";
                        }
                        if(pd.getUnit().matches("mLs")){
                            unitsCost = "Ls";
                        }

                        int dcost = (int) (Math.round(_dcost));
                        String log = count + "-" + pd.getProduct() + "-" + decimalFormat2.format(numberTot)  + unitsCost +
                                "-" + numberFormat.format(dcost);//(pd.getNumber() / numb) *
                        purchases.add(log);

                    }

                }

            } else if(allocationType.matches("Cost")){
                title = "ID-Product-Price-Cost";
                double total = 0;
                for(String Key : selected) {
//                Purchase purchase = pdb.getPurchase(Integer.parseInt(Key));
//
//                total = total + purchase.getTotal();
                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                    for (GoodsPurchased pd1 : allGoods) {

                        if(pd1.getUnit().matches("Pcs")){
                            Log.d(TAG, "prepareListData, Cost, Pcs " + pd1.getTotal());
                            total = total + pd1.getTotal();
                        } else {
                            Log.d(TAG, "prepareListData, Cost, else " + (pd1.getTotal()));
                            total = total + (pd1.getTotal());
                        }
                    }

                }


                for(String Key : selected) {

                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                    for (GoodsPurchased pd : allGoods) {
                        double cost = pd.getTotal();
                        double _dcost = (cost/total) * globalTotal;
                        //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                        int dcost = (int) (Math.round(_dcost));
                        count++;
                        String log = count+"-" + pd.getProduct() + "-" + numberFormat.format(pd.getTotal()) +
                                "-" + numberFormat.format(dcost);//(pd.getCost() / purchase.getTotal()) *
                        purchases.add(log);

                    }
                }
            } else {
                title = "ID-Product-Number-Price";
                double numb = 0;
                for(String Key : selected){
                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                    for (GoodsPurchased pd1 : allGoods) {

                        if(pd1.getUnit().matches("Pcs")){
                            numb = numb + pd1.getNumber();
                        } else {
                            numb = numb + (pd1.getNumber() / 1000);
                        }
                    }

                }

                for(String Key : selected) {
                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                    for (GoodsPurchased pd : allGoods) {
//                        double original = pd.getNumber();
//
//                        if(!(pd.getUnit().matches("Pcs"))){
//                            original = (pd.getNumber() / 1000);
//                        }

                        //double _dcost = (original / numb) * globalTotal;
                        count++;
                        //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                        int numberTot = pd.getNumber();
                        String unitsCost = pd.getUnit();

                        if(!pd.getUnit().matches("Pcs")) {
                            numberTot = numberTot / 1000;
                        }

                        if(pd.getUnit().matches("Grms")){
                            unitsCost = "Kgs";
                        }
                        if(pd.getUnit().matches("mLs")){
                            unitsCost = "Ls";
                        }

                        int dcost = (Math.round(pd.getTotal()));
                        String log = count + "-" + pd.getProduct() + "-" + decimalFormat2.format(numberTot)  + unitsCost +
                                "-" + numberFormat.format(dcost);//(pd.getNumber() / numb) *
                        purchases.add(log);

                    }

                }

            }

            listDataHeader.add("Hide/Show Products: " + count + "-" + title);

            listDataChild.put(listDataHeader.get(0), purchases); // Header, Child data
        }

        if(getIntent().getStringExtra("FROM").matches("AOA")) {
            ReUpHandler rudb = new ReUpHandler(this);

            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            List<String> expenses = new ArrayList<>();

            List<ReUp> allExpenses = rudb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            String title = "Info-Total-Method-Date";
            int count = 0;
            for (ReUp pd : allExpenses) {
                String log = pd.getID() + "-" + pd.getInfo()
                        + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                        + "-" + pd.getDate();
                Log.d(TAG,"Insert: " + log);
                expenses.add(log);
                count++;
            }

            listDataHeader.add("Upgrades on Buidings: " + count + "-" + title);

            listDataChild.put(listDataHeader.get(0), expenses); // Header, Child data
        }
    }
}
