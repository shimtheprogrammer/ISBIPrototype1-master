package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrder;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSale;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;

import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 1/26/2017.
 */

public class ClosePreActivity extends Activity {
    public static String TAG = "ClosePreActivity";

    Button btnYes, btnNo, btnGoods, btnDate;
    TextView closeDate, name, amount, date;

    private Calendar calendar;
    private int year, month, day;

    GoodsPreOrderedHandler gpodb = new GoodsPreOrderedHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_pre);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(290, 290);

        btnNo = (Button) findViewById(R.id.btn_no);
        btnYes = (Button) findViewById(R.id.btn_yes);
        btnGoods = (Button) findViewById(R.id.btn_goods);
        btnDate = (Button) findViewById(R.id.btn_close_date);

        name = (TextView) findViewById(R.id.name);
        amount = (TextView) findViewById(R.id.amount);
        date = (TextView) findViewById(R.id.date);

        closeDate = (TextView) findViewById(R.id.close_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        LinearLayout.LayoutParams hide = new LinearLayout.LayoutParams(0, 0);

        LinearLayout imageView1 = (LinearLayout) findViewById(R.id.sale_icon);
        LinearLayout imageView2 = (LinearLayout) findViewById(R.id.purchse_icon);

        if(getIntent().getStringExtra("FROM").matches("SALE")) {
            imageView2.setLayoutParams(hide);
        }

        if(getIntent().getStringExtra("FROM").matches("ORDER")) {
            imageView1.setLayoutParams(hide);
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        LinearLayout linearLayoutButton = (LinearLayout) findViewById(R.id.linear_layout_button);
        LinearLayout linearLayout0 = (LinearLayout) findViewById(R.id.linearLayout);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);

        int holoBlueBright = getApplication().getResources().getColor(R.color.holoBlueBright);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayoutButton.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueBright);
        drawable.setSize(2, 2);
        linearLayout.setDividerDrawable(drawable);
        linearLayoutButton.setDividerDrawable(drawable);

        linearLayout0.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout1.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout2.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout3.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable2 = new GradientDrawable();
        drawable2.setColor(holoBlueBright);
        drawable2.setSize(1, 1);
        linearLayout0.setDividerDrawable(drawable2);
        linearLayout1.setDividerDrawable(drawable2);
        linearLayout2.setDividerDrawable(drawable2);
        linearLayout3.setDividerDrawable(drawable2);

        String[] entry = getIntent().getStringExtra("TEXT").split("-");

        name.setText(entry[1]);
        amount.setText(entry[3]);
        date.setText(entry[(entry.length - 1)]);

        btnDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnDate.setBackgroundResource(R.drawable.button_small_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnDate.setBackgroundResource(R.drawable.button_small);
                        return false; // if you want to handle the touch event
                }
                return false;

            }
        });

        btnGoods.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnGoods.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnGoods.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;

            }
        });

        btnGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatFourELActivity.class);
                if(getIntent().getStringExtra("FROM").matches("SALE")) {
                    intent.putExtra("FROM", "PSO");
                }
                if(getIntent().getStringExtra("FROM").matches("ORDER")) {
                    intent.putExtra("FROM", "PPO");
                }
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                startActivity(intent);

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("FROM").matches("SALE")) {

                    List<Inventory> inventories = idb.getAllInventory();

                    List<GoodsPreOrdered> goodsPurchased = gpsdb.getByForeignKey(
                            Integer.parseInt(getIntent().getStringExtra("ID")));

                    boolean isThere = false, isEnough = true;
                    String what = "";

                    for(GoodsPreOrdered pd : goodsPurchased) {
                        what = pd.getProduct();
                        for (Inventory pd1 : inventories) {
                            if(pd.getProduct().matches(pd1.getProduct())){
                                isThere = true;
                                what = pd.getProduct();
                                if(pd.getNumber() > pd1.getNumber()){
                                    isEnough = false;
                                    what = pd.getProduct();
                                }
                            }
                        }
                    }

                    if(!isThere){
                        Log.d(TAG, what + "Not present ");

                        new AlertDialog.Builder(ClosePreActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Stock Error!")
                                .setMessage(what + " is not present in inventory")
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "Not present allert");
                                        Intent intent = new Intent(getApplicationContext(), OrdersDisplayActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();

                    } else if(!isEnough){
                        Log.d(TAG, what + " Not enough");

                        new AlertDialog.Builder(ClosePreActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Stock Error!")
                                .setMessage("Quantity of " + what + " is not enough in inventory")
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "Not present allert");
                                        Intent intent = new Intent(getApplicationContext(), OrdersDisplayActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();

                    } else {
                        //updating number in inventory
                        for (GoodsPreOrdered pd : goodsPurchased) {
                            idb.updateNumberInventory(pd.getProduct(), pd.getNumber());
                        }

                        /*
                        updating goods sold
                         */

                        List<GoodsPreOrdered> allPreSold = gpsdb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));
                        List<Inventory> inventory = idb.getAllInventory();


                        for (GoodsPreOrdered pd : allPreSold) {
                            for (Inventory pd2 : inventory) {
                                if(pd2.getProduct().matches(pd.getProduct())){
                                    Log.d(TAG, "cost has been changed from " + pd.getCost() + " to " + pd2.getCost());
                                    //pd.setCost(pd2.getCost());
                                }
                            }

                            pd.setDate(closeDate.getText().toString());
                            gpsdb.updateGoodsPreOrdered(pd);

                        }

                        /*
                        goods updated
                         */

                         /*
                    updating pressale and closec presale
                     */
                        PreSale preSale = psdb.getPreSale(Integer.parseInt(getIntent().getStringExtra("ID")));

                        preSale.setDate(closeDate.getText().toString());
                        preSale.setStatus("CLOSED");

                        psdb.updatePreSale(preSale);
                        /*
                        done moving
                         */

                        Intent intent = new Intent(getApplicationContext(), OrdersDisplayActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } else if(getIntent().getStringExtra("FROM").matches("ORDER")) {

                    List<Inventory> inventories = idb.getAllInventory();

                    List<GoodsPreOrdered> goodsPurchased = gpodb.getByForeignKey(
                            Integer.parseInt(getIntent().getStringExtra("ID")));

                    //updating inventory
                    for (GoodsPreOrdered pd : goodsPurchased) {
                        boolean flag = true;
                        for (Inventory pd1 : inventories) {
                            String log = pd1.getProduct();
                            if(log.matches(pd.getProduct())){
                                idb.updateNumberInventoryPurchase(pd.getProduct(), pd.getNumber(), pd.getTotal());
                                flag = false;
                            }
                        }
                        if(flag){
                            idb.addInventory(new Inventory(pd.getProduct(), pd.getNumber(), pd.getUnit(),
                                    pd.getCost(), pd.getTotal(), 0));
                        }
                    }

                    /*
                    Updating inventory
                     */

                    /*
                        updating goods sold
                         */

                    List<GoodsPreOrdered> allPreOrdered = gpodb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));
                    for (GoodsPreOrdered pd : allPreOrdered) {
                        pd.setDate(closeDate.getText().toString());
                        gpodb.updateGoodsPreOrdered(pd);
                    }

                        /*
                        goods updated
                         */

                    /*
                    updating presorder and closec preorder
                     */

                    PreOrder preOrder = podb.getPreOrder(Integer.parseInt(getIntent().getStringExtra("ID")));

                    preOrder.setDate(closeDate.getText().toString());
                    preOrder.setStatus("CLOSED");

//                    cpodb.addPreOrder(preOrder);

                    podb.updatePreOrder(preOrder);
                    /*
                    done moving
                     */
                    Intent intent = new Intent(getApplicationContext(), OrdersDisplayActivity.class);
                    startActivity(intent);

                    finish();
                }

                Intent intent = new Intent(getApplicationContext(), OrdersDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * the beelow methodes aree used to manage the date field
     * **/
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);

                }
            };

    private void showDate(int year, int month, int day) {
        closeDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    /**
     * end of date methodes
     **/

}
