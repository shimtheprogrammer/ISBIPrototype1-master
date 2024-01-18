package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;

import android.os.Bundle;

import android.support.design.widget.TextInputLayout;

import android.text.InputType;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGains;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Owner;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;

import java.text.DecimalFormat;

import java.util.Calendar;

/**
 * Created by MRuto on 3/20/2017.
 */

public class InventoryChangeActivity extends Activity {
    public static String TAG = "InventoryChangeActivity";

    TextView total;
    Button btnExit, btnAdd, btnDate;
    TextInputLayout totalWidget;

    Calendar calendar;
    TextView dateView;
    int year, month, day;

    InventoryHandler idb = new InventoryHandler(this);
    OwnerHandler odb = new OwnerHandler(this);
    AssetGainsHandler agdb = new AssetGainsHandler(this);

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("0.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_change);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(290, 263);

        total = (TextView) findViewById(R.id.total);
        totalWidget = (TextInputLayout) findViewById(R.id.total_widget);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);
        dateView = (TextView) findViewById(R.id.date);
        btnDate = (Button) findViewById(R.id.btn_date);

        Log.d(TAG, "ID " + Integer.parseInt(getIntent().getStringExtra("ID")));

        Inventory inventory = idb.getInventory(Integer.parseInt(getIntent().getStringExtra("ID")));

        String units = inventory.getUnit();

        Log.d(TAG, "Units " + units);
        Log.d(TAG, "Product " + inventory.getProduct());

        String from = getIntent().getStringExtra("FROM");

        Log.d(TAG, "From " + getIntent().getStringExtra("FROM"));

        if (from.matches("SPR")) {
            totalWidget.setHint("Sale Price");
            total.setText("" + inventory.getSPrice());
            btnAdd.setText("Set");

        } else {
            if (units.matches("Pcs")){
                totalWidget.setHint("Quantity in Pcs");

            } else {
                Log.d(TAG, "setting cost to decimal number");
                total.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | InputType.TYPE_CLASS_NUMBER);

                if (units.matches("Grms")){
                    totalWidget.setHint("Quantity in Kgs");

                } else if (units.matches("mLs")){
                    totalWidget.setHint("Quantity in Ls");

                }
            }
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

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

        btnAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnAdd.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnAdd.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        //creating listener and event for back button;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total.getText().toString().matches("")){
                    Log.d(TAG, "total was empty");
                    total.setError("Please Enter a Value");
                    total.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("SPR")){
                    Inventory inventory2 = idb.getInventory(Integer.parseInt(getIntent().getStringExtra("ID")));

                    StringBuilder message = new StringBuilder();

                    message.append("Change Selling Price from " + inventory2.getSPrice()
                            + " to " + Integer.parseInt(total.getText().toString()));

                    new AlertDialog.Builder(InventoryChangeActivity.this)
                            .setIcon(R.drawable.invenotry_icon_5)
                            .setTitle("Confirm Selling Price")
                            .setMessage(message.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Inventory inventory3 = idb.getInventory(Integer.parseInt(getIntent().getStringExtra("ID")));

                                    inventory3.setSPrice(Integer.parseInt(total.getText().toString()));
                                    idb.updateInventory(inventory3);

                                    finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    Inventory inventory4 = idb.getInventory(Integer.parseInt(getIntent().getStringExtra("ID")));
                    String units4 = inventory4.getUnit();

                    if (units4.matches("Pcs")
                            && (inventory4.getNumber() < Integer.parseInt(total.getText().toString()))){
                        Log.d(TAG, "total was too high");
                        total.setError("Value Exceeds Quantity in Stock");
                        total.requestFocus();

                    } else if ((units4.matches("Grms") || units4.matches("mLs"))
                            && (inventory4.getNumber() < (Double.parseDouble(total.getText().toString()) * 1000))){
                        Log.d(TAG, "total was too high");
                        total.setError("Value Exceeds Quantity in Stock");
                        total.requestFocus();

                    } else {
                        StringBuilder message = new StringBuilder();

                        message.append("Remove a Quantity of ");

                        if (units4.matches("Pcs")){
                            message.append(Integer.parseInt(total.getText().toString()) + " Pcs ");

                        } else if (units4.matches("Grms")){
                            message.append(decimalFormat2.format(Double.parseDouble(total.getText().toString())) + " Kgs ");

                        } else if (units4.matches("mLs")){
                            message.append(decimalFormat2.format(Double.parseDouble(total.getText().toString())) + " Ls ");

                        }

                        message.append("of the Product " + inventory4.getProduct());

                        new AlertDialog.Builder(InventoryChangeActivity.this)
                                .setIcon(R.drawable.invenotry_icon_5)
                                .setTitle("Confirm Quantity to Remove")
                                .setMessage(message.toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Inventory inventory5 = idb.getInventory(Integer.parseInt(getIntent().getStringExtra("ID")));

                                        int numb = inventory5.getNumber();

                                        if (inventory5.getUnit().matches("Pcs")){
                                            inventory5.setNumber(numb - Integer.parseInt(total.getText().toString()));
                                            inventory5.setTotal((int)(
                                                    (numb - Integer.parseInt(total.getText().toString()))
                                                    * inventory5.getCost()));

                                        } else {
                                            inventory5.setNumber((int)
                                                    Math.round(numb - (Double.parseDouble(total.getText().toString()) * 1000)));
                                            inventory5.setTotal((int)
                                                    ((numb - (Double.parseDouble(total.getText().toString()) * 1000))
                                                    * inventory5.getCost()));

                                        }

                                        idb.updateInventory(inventory5);

                                        if (getIntent().getStringExtra("FROM").matches("OWE")) {
                                            double globalTotal;
                                            String paymentMethode;

                                            if (inventory5.getUnit().matches("Pcs")){
                                                globalTotal = inventory5.getCost() * Integer.parseInt(total.getText().toString());
                                                paymentMethode = "Pcs";

                                            } else {
                                                globalTotal = inventory5.getCost()
                                                        * (Double.parseDouble(total.getText().toString()) * 1000);

                                                if (inventory5.getUnit().matches("Grms")){
                                                    paymentMethode = "Kgs";

                                                } else {
                                                    paymentMethode = "Ls";

                                                }

                                            }

                                            odb.addOwner(new Owner("Draw from Stock",
                                                    (int) Math.round(globalTotal), paymentMethode, inventory5.getProduct(),
                                                    dateView.getText().toString()));
                                        }

                                        if (getIntent().getStringExtra("FROM").matches("REM")) {
                                            Inventory inventory6 = idb.getInventory(Integer.parseInt(getIntent().getStringExtra("ID")));

                                            double globalTotal;

                                            if (inventory6.getUnit().matches("Pcs")){
                                                globalTotal = inventory6.getCost() * Integer.parseInt(total.getText().toString());

                                            } else {
                                                globalTotal = inventory6.getCost()
                                                        * (Double.parseDouble(total.getText().toString()) * 1000);

                                            }

                                            globalTotal = globalTotal * (-1);

                                            agdb.addAssetGains(new AssetGains("INV", (int) Math.round(globalTotal),
                                                    ("loss of stock " + inventory6.getProduct()),
                                                    dateView.getText().toString(), inventory6.getID()));

                                        }

                                        finish();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                }
            }
        });

        btnExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnExit.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnExit.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        //creating listener and event for back button;
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * the below methodes aree used to manage the date field
     * **/
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    myDateListener, year, month, day);

            Calendar maxDate = Calendar.getInstance();

            //datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis() - 1000);

            return datePickerDialog;

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
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

    }

    /**
     * end of date methodes
     **/
}
