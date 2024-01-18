package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSale;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Sales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.FourELAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/25/2017.
 */

public class PreSaleActivity extends Activity{
    public static String TAG = "PreSaleActivity";

    FourELAdapter listAdapter, listAdapter2;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, String> idList = new HashMap<>();

    public int idGoods;
    private Calendar calendar;
    TextView dateView;
    private int year, month, day;

    Mix mix2;

    LinearLayout layoutEL, layoutSV, paymentLL;
    Button btnExit, btnAdd, btnDueDate, btnDate;
    EditText total, discount;
    TextView dueDate;
    Spinner payment;
    TextInputLayout discountWidget;

    String paymentMeth = "";
    public int globalTotal = 0, discountTotal = 0;
    public boolean creditFromMix = false, fromMix = true;

    public AutoCompleteTextView customer, phone;

    SalesHandler sdb = new SalesHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);
    ReceivablesHandler rdb = new ReceivablesHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    CashHandler cdb = new CashHandler(this);
    MixHandler mdb = new MixHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("#.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sale);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        //lining parameters to values on xml
        customer = (AutoCompleteTextView) findViewById(R.id.customers);
        phone = (AutoCompleteTextView) findViewById(R.id.number);
        total = (EditText) findViewById(R.id.total);
        discount = (EditText) findViewById(R.id.discount);
        payment = (Spinner) findViewById(R.id.payment);
        dueDate = (TextView) findViewById(R.id.due_date);
        expListView = (ExpandableListView) findViewById(R.id.lvExp2);
        btnDueDate = (Button) findViewById(R.id.btn_due_date);
        btnDate = (Button) findViewById(R.id.btn_date);
        layoutEL = (LinearLayout) findViewById(R.id.linear_e_l);
        layoutSV = (LinearLayout) findViewById(R.id.linear_sc_v);
        paymentLL = (LinearLayout) findViewById(R.id.payment_ll);
        discountWidget = (TextInputLayout) findViewById(R.id.discount_widget);

        mix2 = mdb.getMix(1);

        discount.addTextChangedListener(commaSeparator);

        LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);

        if (getIntent().getStringExtra("FROM").matches("REG")){
            paymentLL.setLayoutParams(hiding);
            discount.setVisibility(View.INVISIBLE);
            discountWidget.setVisibility(View.INVISIBLE);

        }

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

        btnDueDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnDueDate.setBackgroundResource(R.drawable.button_small_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnDueDate.setBackgroundResource(R.drawable.button_small);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });


        // Spinner Drop down elements
        final List<String> paymentMethod = new ArrayList<String>();
        paymentMethod.add("--Select Payment Method--");
        paymentMethod.add("Cash");
        paymentMethod.add("MPESA");
        paymentMethod.add("Mix");
        paymentMethod.add("Bank Transfer");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, R.layout.activity_spinner_item, paymentMethod);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        payment.setAdapter(dataAdapter1);

        payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                              @Override
                                              public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                                  // On selecting a spinner item
                                                  paymentMeth = parent.getItemAtPosition(position).toString();

//                                                  if((paymentMeth.matches("Cash") || paymentMeth.matches("MPESA")
//                                                          || paymentMeth.matches("Bank Transfer") || paymentMeth.matches("Credit"))
//                                                          && (getIntent().getStringExtra("FROM").matches("MIX"))){
//                                                      total.setText("" + globalTotal);
//                                                      fromMix = true;
//                                                      creditFromMix = false;
//
//                                                  }

                                                  if((paymentMeth.matches("Cash") || paymentMeth.matches("MPESA")
                                                          || paymentMeth.matches("Bank Transfer") || paymentMeth.matches("Credit"))
                                                          && mix2.getFlag().matches("true")){
                                                      total.setText("" + numberFormat.format(globalTotal));
                                                      fromMix = true;
                                                      creditFromMix = false;

                                                  }

                                                  if(paymentMeth.matches("Mix")){
                                                      if((total.getText().toString()).matches("") || (globalTotal <= 0)){

                                                          Log.d(TAG, "totals was null..");
                                                          total.setError("No products selected");
                                                          total.requestFocus();
                                                          payment.setSelection(0);

                                                      } else {
                                                          Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                                          intent.putExtra("FROM", "REC");
                                                          intent.putExtra("CUSTOPMER", "" + customer.getText().toString());
                                                          intent.putExtra("NUMBER", "" + phone.getText().toString());
                                                          intent.putExtra("DISCOUNT", "" + discount.getText().toString());
                                                          intent.putExtra("DATE", "" + dateView.getText().toString());
                                                          intent.putExtra("DUEDATE", "" + dueDate.getText().toString());
                                                          intent.putExtra("CREDIT", "NO");

////                                                          int discountTotal = 0;
//                                                          if (!(discount.getText().toString().matches(""))){
//                                                              discountTotal = Integer.parseInt(discount.getText().toString());
////                                                              globalTotal = globalTotal
////                                                                      - Integer.parseInt(discount.getText().toString());
//                                                          }
                                                          intent.putExtra("TOTAL", "" + (globalTotal - discountTotal));

                                                          startActivity(intent);
                                                      }
                                                  } else {
                                                      Mix mix = mdb.getMix(1);

                                                      mix.setFlag("false");
                                                      mix.setCredit(0);
                                                      mix.setCash(0);
                                                      mix.setMpesa(0);
                                                      mix.setBank(0);
                                                      mix.setMpesaFee(0);
                                                      mix.setBankFee(0);

                                                      mdb.updateMix(mix);

                                                      List<GoodsPreOrdered> sales = gpsdb.getByForeignKey((psdb.getRowCount() + 1));

                                                      int sum = 0;
                                                      for (GoodsPreOrdered pd: sales){
                                                          sum = sum + pd.getTotal();
                                                      }
                                                      globalTotal = sum;

                                                      total.setText("" + numberFormat.format(globalTotal));

                                                      fromMix = true;
                                                      creditFromMix = false;
                                                  }
                                              }

                                              public void onNothingSelected(AdapterView arg0) {

                                              }
                                          }
        );

        dateView = (TextView) findViewById(R.id.date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDueDate(year, month+1, day);

        total.setEnabled(false);

        if(getIntent().getStringExtra("FROM").matches("MIX")){
            fromMix = false;

            StringBuilder text = new StringBuilder();

            int sum = 0;
            text.append("Mix Payment; ");

            if(!(getIntent().getStringExtra("CASH").matches("0"))){
                text.append(" (actual) " + getIntent().getStringExtra("CASH"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("CASH"));
            }

            if(!(getIntent().getStringExtra("BANK").matches("0"))){
                text.append(" (bank) " + getIntent().getStringExtra("BANK"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("BANK"));
            }

            if(!(getIntent().getStringExtra("MPESA").matches("0"))){
                text.append(" (mpesa) " + getIntent().getStringExtra("MPESA"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("MPESA"));
            }

            text.append("= (total) " + sum);

            globalTotal = sum;

            customer.setText("" + getIntent().getStringExtra("CUSTOPMER"));
            phone.setText("" + getIntent().getStringExtra("NUMBER"));
            discount.setText("" + getIntent().getStringExtra("DISCOUNT"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));
            dueDate.setText("" + getIntent().getStringExtra("DUEDATE"));

            total.setText(text);

        }

        final Mix mix = mdb.getMix(1);

        Log.d(TAG, "Flag " + mix.getFlag() + " actual " + mix.getCash() + " bank " + mix.getBank()
                + " mpesa " + mix.getMpesa() + " credit " + mix.getCredit());

        if (mix.getFlag().matches("true")){
            fromMix = false;

            StringBuilder text = new StringBuilder();

            int sum = 0;
            //text.append("Mix Payment; ");

            if(mix.getCash() > 0){
                sum = sum + mix.getCash();
            }

            if(mix.getBank() > 0){
                sum = sum + mix.getBank();
            }

            if(mix.getMpesa() > 0){
                sum = sum + mix.getMpesa();
            }

            if(mix.getCredit() > 0){
                sum = sum + mix.getCredit();

//                dueDate.setVisibility(View.VISIBLE);
//                btnDueDate.setVisibility(View.VISIBLE);
//                dueDateText.setVisibility(View.VISIBLE);

                creditFromMix = true;
            }

            text.append("(total) " + numberFormat.format(sum) + " = ");

            if(mix.getCash() > 0){
                text.append(" (actual) " + numberFormat.format(mix.getCash()));
            }

            if(mix.getBank() > 0){
                text.append(" (bank) " + numberFormat.format(mix.getBank()));

            }

            if(mix.getMpesa() > 0){
                text.append(" (mpesa) " + numberFormat.format(mix.getMpesa()));

            }

            if(mix.getCredit() > 0){
                text.append(" (credit) " + mix.getCredit());
            }

//            globalTotal = sum;

            total.setText(text);


        } else {
            List<GoodsPreOrdered> sales = gpsdb.getByForeignKey((psdb.getRowCount() + 1));

            int sum = 0;
            for (GoodsPreOrdered pd: sales){
                sum = sum + pd.getTotal();
            }
            globalTotal = sum;

            total.setText("" + numberFormat.format(globalTotal));

            fromMix = true;
            creditFromMix = false;

            if(paymentMeth.matches("Mix")) {
                payment.setSelection(0);
            }

            if(!paymentMeth.matches("Credit")) {
//                dueDate.setVisibility(View.INVISIBLE);
//                btnDueDate.setVisibility(View.INVISIBLE);
//                dueDateText.setVisibility(View.INVISIBLE);

            }
        }

        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);

        final List<String> buyerNames = new ArrayList<>();
        final List<String> buyerPhone = new ArrayList<>();

        List<Receivable> receivable = rdb.getAllReceivable();
        for (Receivable pd : receivable) {
            String name[] = pd.getFrom().split("-");
            String log = name[0];
            String logPhone = "";
            if(name.length > 1){
                logPhone = name[name.length - 1];
            }
            Log.d(TAG, "Buyer Name; " + log);
            Log.d(TAG, "Buyer Phone Number; " + logPhone);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < buyerNames.size(); i++){
                    if((buyerNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    buyerNames.add(log);
                    buyerPhone.add(logPhone);
                }
            }
        }

        List<Sales> sales = sdb.getAllSales();
        for (Sales pd : sales) {
            String name[] = pd.getCustomer().split("-");
            String log = name[0];
            String logPhone = "";
            if(name.length > 1){
                logPhone = name[name.length - 1];
            }
            // Writing Contacts to log
            Log.d(TAG, "Buyer Name; " + log);
            Log.d(TAG, "Buyer Phone Number; " + logPhone);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < buyerNames.size(); i++){
                    if((buyerNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    buyerNames.add(log);
                    buyerPhone.add(logPhone);
                }
            }
        }

        String[] buyers = buyerNames.toArray(new String[buyerNames.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, buyers);

        //Find TextView control

        //Set the number of characters the user must type before the drop down list is shown
        customer.setThreshold(1);
        //Set the adapter
        customer.setAdapter(adapter);

        customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                for(int i = 0; i < buyerPhone.size(); i++){
                    if(buyerNames.get(i).matches(item)){
                        phone.setText("" + buyerPhone.get(i));
                    }
                }
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

        //creating listener and event for Submit button;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fromMix && !(customer.getText().toString().matches(""))) {
                    creditFromMix = false;
                }

//                int discountTotal = 0;

//                if(!(discount.getText().toString().matches(""))) {
//                    discountTotal = Integer.parseInt(discount.getText().toString());
//                }

                if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                    Log.d(TAG, "btnAdd; totals was null..");
                    prepareListData("Total");

//                    listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
//                            listDataChild, "PRES", true);

                    if ((getIntent().getStringExtra("FROM").matches("REG"))){
                        listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                                listDataChild, "PRESR", true);
                    } else {
                        listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                                listDataChild, "PRES", true);
                    }

                    // setting list adapter
                    expListView.setAdapter(listAdapter);
                    expListView.expandGroup(0);

                } else if(customer.getText().toString().matches("")){

                    Log.d(TAG, "btnAdd; no cutomer..");

                    customer.setError("Please Enter Customer Name");
                    customer.requestFocus();

                } else if(phone.getText().toString().matches("")){
                    Log.d(TAG, "btnAdd; no phone..");

                    phone.setError("Please Enter Phone Number");
                    phone.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("REG")){
                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("To " + customer.getText().toString() + " for "
                            + (globalTotal) + " on " + dateView.getText().toString());

                    confMessaage.append("\n\nGoods Pre-Sold;");

                    List<GoodsPreOrdered> goodsSold2 = gpsdb.getByForeignKey(psdb.getRowCount() + 1);

                    //String Title = "\nName\t\t\tNumber\t\t\tPrice\t\t\tTotal";
                    //confMessaage.append("" + Title);
                    for (GoodsPreOrdered pd : goodsSold2) {
                        String name = pd.getProduct();

                        double numbe = pd.getNumber();
                        String units = pd.getUnit();
                        String unitsCost = pd.getUnit();
                        double cost = pd.getCost();

                        if (numbe >= 1000 && !units.matches("Pcs")) {
                            numbe = numbe / 1000.0;
                            if (pd.getUnit().matches("Grms")) {
                                units = "Kgs";
                            }
                            if (pd.getUnit().matches("mLs")) {
                                units = "Ls";
                            }
                        }

                        if (pd.getUnit().matches("Grms")) {
                            unitsCost = "Kgs";
                            cost = cost * 1000.0;
                        }
                        if (pd.getUnit().matches("mLs")) {
                            unitsCost = "Ls";
                            cost = cost * 1000.0;
                        }

                        String displ = "\n" + name + ", " + decimalFormat2.format(numbe) + " " + units + ", "
                                + decimalFormat.format(cost) + " Per " + unitsCost + ", " + numberFormat.format(pd.getTotal());
                        confMessaage.append("" + displ);
                    }

                    new AlertDialog.Builder(PreSaleActivity.this)
                            .setIcon(R.drawable.sale_icon_2)
                            .setTitle("Confirm Pre-Sale")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    int discountTotal2 = 0;

                                        //Inserting row
                                        StringBuilder nameBuild = new StringBuilder();
                                        nameBuild.append(customer.getText().toString());

                                        if(!(phone.getText().toString().matches(""))){
                                            nameBuild.append("-" + phone.getText().toString());
                                        }

                                        psdb.addPreSale(new PreSale(nameBuild.toString(), (globalTotal),
                                                " ", dueDate.getText().toString(), dateView.getText().toString(), "OPEN"));


                                    List<GoodsPreOrdered> goodsSold = gpsdb.getByForeignKey(psdb.getRowCount());

                                    //updating date for goods
                                    for (GoodsPreOrdered pd : goodsSold){
                                        pd.setDate(dateView.getText().toString());
                                        gpsdb.updateGoodsPreOrdered(pd);

                                    }

                                    finish();

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                } else if(discountTotal >= globalTotal){
                    Log.d(TAG, "btnAdd; high discount..");

                    discount.setError("Discount Cannot Exceed Total Amount");
                    discount.requestFocus();

                } else if((paymentMeth.matches("--Select Payment Method--")
                        || paymentMeth.matches("Mix"))
                        && fromMix){
                    Log.d(TAG, "btnAdd; no payment method selected..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Non Selected");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("--Select Payment Method--");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else{
//                    globalTotal = globalTotal - discountTotal;

                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("To " + customer.getText().toString() + " for "
                            + (globalTotal) + " on " + dateView.getText().toString());

                    if (discountTotal > 0){
                        confMessaage.append("\nDiscount " + discountTotal);
                    }

                    confMessaage.append("\nMode of payment; ");

                    if(!fromMix){
//                        if(!(getIntent().getStringExtra("CASH").matches("0"))){
//                            confMessaage.append("Cash ");
//                        }
//
//                        if(!(getIntent().getStringExtra("BANK").matches("0"))){
//                            confMessaage.append("Bank Transfer ");
//                        }
//
//                        if(!(getIntent().getStringExtra("MPESA").matches("0"))){
//                            confMessaage.append("MPESA ");
//                        }
//
//                        if(!(getIntent().getStringExtra("CREDIT").matches("0"))){
//                            confMessaage.append("Credit");
//                        }

                        Mix mix = mdb.getMix(1);

                        if(mix.getCash() > 0){
                            confMessaage.append("Cash ");
                        }

                        if(mix.getBank() > 0){
                            confMessaage.append("Bank Transfer ");
                        }

                        if(mix.getMpesa() > 0){
                            confMessaage.append("MPESA ");
                        }

                        if(mix.getCredit() > 0){
                            confMessaage.append("Credit");
                        }

                    } else {
                        confMessaage.append("" + paymentMeth);
                    }

                    confMessaage.append("\n\nGoods Pre-Sold;");

                    List<GoodsPreOrdered> goodsSold2 = gpsdb.getByForeignKey(psdb.getRowCount() + 1);

                    //String Title = "\nName\t\t\tNumber\t\t\tPrice\t\t\tTotal";
                    //confMessaage.append("" + Title);
                    for (GoodsPreOrdered pd : goodsSold2) {
                        String name = pd.getProduct();

                        double numbe = pd.getNumber();
                        String units = pd.getUnit();
                        String unitsCost = pd.getUnit();
                        double cost = pd.getCost();

                        if (numbe >= 1000 && !units.matches("Pcs")) {
                            numbe = numbe / 1000.0;
                            if (pd.getUnit().matches("Grms")) {
                                units = "Kgs";
                            }
                            if (pd.getUnit().matches("mLs")) {
                                units = "Ls";
                            }
                        }

                        if (pd.getUnit().matches("Grms")) {
                            unitsCost = "Kgs";
                            cost = cost * 1000.0;
                        }
                        if (pd.getUnit().matches("mLs")) {
                            unitsCost = "Ls";
                            cost = cost * 1000.0;
                        }

                        String displ = "\n" + name + ", " + decimalFormat2.format(numbe) + " " + units + ", "
                                + decimalFormat.format(cost) + " Per " + unitsCost + ", " + numberFormat.format(pd.getTotal());
                        confMessaage.append("" + displ);
                    }

                    new AlertDialog.Builder(PreSaleActivity.this)
                            .setIcon(R.drawable.sale_icon_2)
                            .setTitle("Confirm Pre-Sale")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    int discountTotal2 = 0;
//
//                                    if(!(discount.getText().toString().matches(""))) {
//                                        discountTotal2 = Integer.parseInt(discount.getText().toString());
//                                    }

                    if(!fromMix){
                        StringBuilder pay = new StringBuilder();
//                        if(!(getIntent().getStringExtra("CASH").matches("0"))){
//                            cdb.updateSingleCash("Cash", Integer.parseInt(getIntent().getStringExtra("CASH")));
//                            pay.append("Cash ");
//                        }
//
//                        if(!(getIntent().getStringExtra("BANK").matches("0"))){
//                            cdb.updateSingleCash("Bank Transfer", Integer.parseInt(getIntent().getStringExtra("BANK")));
//                            pay.append("Bank Transfer ");
//                        }
//
//                        if(!(getIntent().getStringExtra("MPESA").matches("0"))){
//                            cdb.updateSingleCash("MPESA", Integer.parseInt(getIntent().getStringExtra("MPESA")));
//                            pay.append("MPESA ");
//                        }

                        if(mix.getCash() > 0){
                            cdb.updateSingleCash("Cash", mix.getCash());
                            pay.append("Cash ");
                        }

                        if(mix.getBank() > 0){
                            cdb.updateSingleCash("Bank Transfer", mix.getBank());
                            pay.append("Bank Transfer ");
                        }

                        if(mix.getMpesa() > 0){
                            cdb.updateSingleCash("MPESA", mix.getMpesa());
                            pay.append("MPESA ");
                        }

                        //Inserting row
                        StringBuilder nameBuild = new StringBuilder();
                        nameBuild.append(customer.getText().toString());

                        if(!(phone.getText().toString().matches(""))){
                            nameBuild.append("-" + phone.getText().toString());
                        }

                        psdb.addPreSale(new PreSale(nameBuild.toString(), (globalTotal - discountTotal),
                                pay.toString(), dueDate.getText().toString(), dateView.getText().toString(), "OPEN"));

                    } else {
                        cdb.updateSingleCash(paymentMeth, (globalTotal - discountTotal));
                        //Inserting row
                        //Inserting row
                        StringBuilder nameBuild = new StringBuilder();
                        nameBuild.append(customer.getText().toString());

                        if(!(phone.getText().toString().matches(""))){
                            nameBuild.append("-" + phone.getText().toString());
                        }
                        psdb.addPreSale(new PreSale(nameBuild.toString(), (globalTotal - discountTotal),
                                paymentMeth, dueDate.getText().toString(), dateView.getText().toString(), "OPEN"));
                    }

                    List<GoodsPreOrdered> goodsSold = gpsdb.getByForeignKey(psdb.getRowCount());

                    //updating date for goods
                    for (GoodsPreOrdered pd : goodsSold){
                        pd.setDate(dateView.getText().toString());

                        if (discountTotal > 0) {
                            double tot = pd.getTotal(), gtot = globalTotal;
                            double goodDiscount = (tot / gtot) * (double) discountTotal;

                            pd.setTotal((int) (Math.round(( (double) pd.getTotal()) - goodDiscount)));

                            pd.setCost((((double) pd.getTotal()) - goodDiscount)
                                    / (double) (pd.getNumber()));

                        }

                        gpsdb.updateGoodsPreOrdered(pd);
                    }

                                    Mix mix = mdb.getMix(1);

                                    mix.setFlag("false");
                                    mix.setCredit(0);
                                    mix.setCash(0);
                                    mix.setMpesa(0);
                                    mix.setBank(0);
                                    mix.setMpesaFee(0);
                                    mix.setBankFee(0);

                                    mdb.updateMix(mix);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
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
                new AlertDialog.Builder(PreSaleActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Exit")
                        .setMessage("This will remove all entries")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gpsdb.deleteByForeignKey(psdb.getRowCount() + 1);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        /**
         * the bellow part is for handling the epandable list for adding products
         * **/
        prepareListData("Normal");

        if ((getIntent().getStringExtra("FROM").matches("REG"))){
            listAdapter = new FourELAdapter(this, listDataHeader,
                    listDataChild, "PRESR", true);
        } else {
            listAdapter = new FourELAdapter(this, listDataHeader,
                    listDataChild, "PRES", true);
        }


        // setting list adapter
        expListView.setAdapter(listAdapter);

        final LinearLayout.LayoutParams show =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

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
                prepareListData("Hide");

//                listAdapter2 = new FourELAdapter(getApplicationContext(), listDataHeader,
//                        listDataChild, "PRES", true);

                if ((getIntent().getStringExtra("FROM").matches("REG"))){
                    listAdapter2 = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PRESR", true);
                } else {
                    listAdapter2 = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PRES", true);
                }

                layoutSV.setLayoutParams(show);
                layoutEL.setLayoutParams(show);

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                prepareListData("Show");

//                listAdapter2 = new FourELAdapter(getApplicationContext(), listDataHeader,
//                        listDataChild, "PRES", true);

                if ((getIntent().getStringExtra("FROM").matches("REG"))){
                    listAdapter2 = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PRESR", true);
                } else {
                    listAdapter2 = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PRES", true);
                }

                layoutSV.setLayoutParams(show);
                layoutEL.setLayoutParams(show);

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                for (String Key : idList.keySet()) {
                    Log.d(TAG, "Iterating list; " + Key + " value " + idList.get(Key));
                    if (Key.matches("" + (childPosition))) {
                        Log.d(TAG, "Found; " + Key + " = " + childPosition);
                        GoodsPreOrdered goodsSales = gpsdb.getGoodsPreOrdered(Integer.parseInt(idList.get(Key)));
                        idGoods = Integer.parseInt(idList.get(Key));

                        String name = goodsSales.getProduct();
                        if (name.matches("--Delivery--")) {
                            String del[] = name.split("-");
                            name = del[2];
                        }

                        double numbe = goodsSales.getNumber();
                        String units = goodsSales.getUnit();
                        String unitsCost = goodsSales.getUnit();
                        double cost = goodsSales.getCost();

                        if (numbe >= 1000 && !units.matches("Pcs")) {
                            numbe = numbe / 1000.0;
                            if (goodsSales.getUnit().matches("Grms")) {
                                units = "Kgs";
                            }
                            if (goodsSales.getUnit().matches("mLs")) {
                                units = "Ls";
                            }
                        }

                        if (goodsSales.getUnit().matches("Grms")) {
                            unitsCost = "Kgs";
                            cost = cost * 1000;
                        }
                        if (goodsSales.getUnit().matches("mLs")) {
                            unitsCost = "Ls";
                            cost = cost * 1000;
                        }

                        new AlertDialog.Builder(PreSaleActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Deletion")
                                .setMessage("Delete; " + name + " " + decimalFormat2.format(numbe) + units + " " + decimalFormat.format(cost)
                                        + " Per " + unitsCost + " " + numberFormat.format(goodsSales.getTotal()))
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GoodsPreOrdered goodsSales2 = gpsdb.getGoodsPreOrdered(idGoods);
                                        gpsdb.deleteGoodsPreOrdered(goodsSales2);

                                        if (paymentMeth.matches("Mix")) {
                                            payment.setSelection(0);

                                            Mix mix = mdb.getMix(1);

                                            mix.setFlag("false");
                                            mix.setCredit(0);
                                            mix.setCash(0);
                                            mix.setMpesa(0);
                                            mix.setBank(0);
                                            mix.setMpesaFee(0);
                                            mix.setBankFee(0);

                                            mdb.updateMix(mix);
                                        }

                                        onResume();
                                    }
                                })
                                .setNegativeButton("Back", null)
                                .show();
                    }
                }

                return false;
            }
        });

        expListView.expandGroup(0);
    }

    /*
    on resume
    */

    @Override
    protected void onResume() {
        super.onResume();

        prepareListData("Normal");

//        listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
//                listDataChild, "PRES", true);

        if ((getIntent().getStringExtra("FROM").matches("REG"))){
            listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                    listDataChild, "PRESR", true);
        } else {
            listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                    listDataChild, "PRES", true);
        }

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);

        final Mix mix = mdb.getMix(1);

        Log.d(TAG, "Flag " + mix.getFlag() + " actual " + mix.getCash() + " bank " + mix.getBank()
                + " mpesa " + mix.getMpesa() + " credit " + mix.getCredit());

        if (mix.getFlag().matches("true")){
            fromMix = false;

            StringBuilder text = new StringBuilder();

            int sum = 0;
            //text.append("Mix Payment; ");

            if(mix.getCash() > 0){
                sum = sum + mix.getCash();
            }

            if(mix.getBank() > 0){
                sum = sum + mix.getBank();
            }

            if(mix.getMpesa() > 0){
                sum = sum + mix.getMpesa();
            }

            if(mix.getCredit() > 0){
                sum = sum + mix.getCredit();

//                dueDate.setVisibility(View.VISIBLE);
//                btnDueDate.setVisibility(View.VISIBLE);
//                dueDateText.setVisibility(View.VISIBLE);

                creditFromMix = true;
            }

            text.append("(total) " + numberFormat.format(sum) + " = ");

            if(mix.getCash() > 0){
                text.append(" (actual) " + numberFormat.format(mix.getCash()));
            }

            if(mix.getBank() > 0){
                text.append(" (bank) " + numberFormat.format(mix.getBank()));

            }

            if(mix.getMpesa() > 0){
                text.append(" (mpesa) " + numberFormat.format(mix.getMpesa()));

            }

            if(mix.getCredit() > 0){
                text.append(" (credit) " + mix.getCredit());
            }

            globalTotal = sum;

            total.setText(text);


        } else {
            List<GoodsPreOrdered> sales = gpsdb.getByForeignKey((psdb.getRowCount() + 1));

            int sum = 0;
            for (GoodsPreOrdered pd: sales){
                sum = sum + pd.getTotal();
            }
            globalTotal = sum;

            total.setText("" + numberFormat.format(globalTotal));

            fromMix = true;
            creditFromMix = false;

            if(paymentMeth.matches("Mix")) {
                payment.setSelection(0);
            }

            if(!paymentMeth.matches("Credit")) {
//                dueDate.setVisibility(View.INVISIBLE);
//                btnDueDate.setVisibility(View.INVISIBLE);
//                dueDateText.setVisibility(View.INVISIBLE);

            }
        }
    }

    /*
    end of resume
     */

    /**
     * the beelow methodes aree used to manage the date field
     * **/
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    public void setDueDate(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    myDateListener, year, month, day);

            Calendar maxDate = Calendar.getInstance();

            //datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis() - 1000);

            return datePickerDialog;

        } if (id == 998) {
            DatePickerDialog dueDateDialogue = new DatePickerDialog(this,
                    myDueDateListener, year, month, day);

            String date = dateView.getText().toString();

            String dat[] = date.split("/");
            int day = Integer.parseInt(dat[0]) + 1;
            int month = Integer.parseInt(dat[1]);
            int year = Integer.parseInt(dat[2]);

            Calendar actualDate;
            actualDate = Calendar.getInstance();
            actualDate.set(Calendar.DAY_OF_MONTH, day);
            actualDate.set(Calendar.YEAR, year);

            setMonth(actualDate, month);

            //dueDateDialogue.getDatePicker().setMinDate(actualDate.getTimeInMillis() - 1000);

            actualDate.set(Calendar.YEAR, year + 5);

            //dueDateDialogue.getDatePicker().setMaxDate(actualDate.getTimeInMillis() - 1000);

            return dueDateDialogue;
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

    private DatePickerDialog.OnDateSetListener myDueDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDueDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

        showDueDate(year, month, day+1);
    }

    private void showDueDate(int year, int month, int day) {
        dueDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    /**
     * end of date methodes
     **/

    /*
     * Preparing the list data
     */
    private void prepareListData(String what) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        List<String> sale = new ArrayList<>();

        List<GoodsPreOrdered> sales = gpsdb.getByForeignKey((psdb.getRowCount() + 1));

        String title = "Name-Number-Price-Total";
        //sale.add(Title);
        int count = 0;
        int tot = 0;
        for (GoodsPreOrdered pd : sales) {
            String name = pd.getProduct();

            double numbe = pd.getNumber();
            String units = pd.getUnit();
            String unitsCost = pd.getUnit();
            double cost = pd.getCost();

            if(numbe >= 1000 && !units.matches("Pcs")){
                numbe = numbe / 1000.0;
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

            String displ = name + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                    + numberFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());

            sale.add(displ);
            tot = tot + pd.getTotal();
            idList.put("" + count, "" + pd.getId());
            count++;
        }

        //setting auto add total
        String showTot = "" + tot;

        if(!(getIntent().getStringExtra("FROM").matches("MIX"))) {
            total.setText(showTot);
            globalTotal = Integer.parseInt(total.getText().toString());
        }

        String groupName;

        if(what.matches("Total")){
            groupName = "The Total Cannot be 0";
        } else if(what.matches("Show")){
            groupName = "Show Items Being Pre Sold; " + count;
        } else {
            groupName = "Hide/Show Items Being Pre Sold; " + count;
        }
        listDataHeader.add(groupName + "-" + title);

        listDataChild.put(listDataHeader.get(0), sale); // Header, Child data

    }

    /*
    setting month
     */

    public Calendar setMonth(Calendar date, int month){

        if(month == 1){
            date.set(Calendar.MONTH, Calendar.JANUARY);
        } else if(month == 2){
            date.set(Calendar.MONTH, Calendar.FEBRUARY);
        } else if(month == 3){
            date.set(Calendar.MONTH, Calendar.MARCH);
        } else if(month == 4){
            date.set(Calendar.MONTH, Calendar.APRIL);
        } else if(month == 5){
            date.set(Calendar.MONTH, Calendar.MAY);
        } else if(month == 6){
            date.set(Calendar.MONTH, Calendar.JUNE);
        } else if(month == 7){
            date.set(Calendar.MONTH, Calendar.JULY);
        } else if(month == 8){
            date.set(Calendar.MONTH, Calendar.AUGUST);
        } else if(month == 9){
            date.set(Calendar.MONTH, Calendar.SEPTEMBER);
        } else if(month == 10){
            date.set(Calendar.MONTH, Calendar.OCTOBER);
        } else if(month == 11){
            date.set(Calendar.MONTH, Calendar.NOVEMBER);
        } else if(month == 12){
            date.set(Calendar.MONTH, Calendar.DECEMBER);
        }

        return date;
    }

    /**
     * text watcher for formating cost
     **/

    private final TextWatcher commaSeparator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            discount.removeTextChangedListener(commaSeparator);

            if (!(discount.getText()).toString().matches("")) {
                String cstArray[] = (discount.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                discountTotal = Integer.parseInt(cst.toString());

                if (discountTotal >= globalTotal){
                    discount.setText("");
                    discount.setError("Discount Cannot be Greater than Total");
                } else {
                    discount.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
                }

                if (paymentMeth.matches("Mix")){
                    payment.setSelection(0);

                    Mix mix = mdb.getMix(1);

                    mix.setFlag("false");
                    mix.setCredit(0);
                    mix.setCash(0);
                    mix.setMpesa(0);
                    mix.setBank(0);
                    mix.setMpesaFee(0);
                    mix.setBankFee(0);

                    mdb.updateMix(mix);

                    total.setText("" +  globalTotal);
                }
            }
            discount.addTextChangedListener(commaSeparator);

        }

        @Override
        public void afterTextChanged(Editable s) {
            discount.setSelection(discount.getText().length());
        }
    };

    /**
     * end of text watcher
     **/
}
