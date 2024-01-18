package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;

import android.os.Bundle;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Delivery;
import com.prototype.isbi.isbiprototype1.databaseHandlers.DeliveryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Purchase;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PurchaseHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.FourELAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/10/2017.
 */

public class PurchaseActivity extends Activity {
    public static String TAG = "PurchaseActivity";

    FourELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, String> idList = new HashMap<>();

    public int idGoods;
    private Calendar calendar;
    private TextView dateView, dueDate, dueDateText;
    private int year, month, day;

    Button btnExit, btnAdd, btnDueDate, btnDate;
    EditText total, discount, deliveryET, fee;
    Spinner payment, allocation;
    CheckBox checkBox, deliver;

    LinearLayout layoutEL, layoutSV, layoutDeliver, layoutCheckDel, layoutCheckNow, feeLL;

    String paymentMeth = "",  allocationType = "";
    public int globalTotal, deliveryTotl = 0, feeTotal = 0, discountTotal = 0;
    public boolean creditFromMix = false, fromMix = true, checked = false, del = false;

    public AutoCompleteTextView supplier, phone;

    GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);
    PurchaseHandler pdb = new PurchaseHandler(this);
    PayablesHandler padb = new PayablesHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    CashHandler cdb = new CashHandler(this);
    DeliveryHandler ddb = new DeliveryHandler(this);
    MixHandler mdb = new MixHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);

    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("#.###");
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        //lining parameters to values on xml
        supplier = (AutoCompleteTextView) findViewById(R.id.supplier);
        phone = (AutoCompleteTextView) findViewById(R.id.number);
        total = (EditText) findViewById(R.id.total);
        discount = (EditText) findViewById(R.id.discount);
        deliveryET = (EditText) findViewById(R.id.delivery_total);
        payment = (Spinner) findViewById(R.id.payment);
        dueDate = (TextView) findViewById(R.id.due_date);
        btnDueDate = (Button) findViewById(R.id.btn_due_date);
        btnDate = (Button) findViewById(R.id.btn_date);
        expListView = (ExpandableListView) findViewById(R.id.lvExp2);
        dateView = (TextView) findViewById(R.id.date);
        dueDateText = (TextView) findViewById(R.id.due_date_text);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        deliver = (CheckBox) findViewById(R.id.deliver_now);
        layoutEL = (LinearLayout) findViewById(R.id.linear_e_l);
        layoutSV = (LinearLayout) findViewById(R.id.linear_sc_v);
        layoutCheckDel = (LinearLayout) findViewById(R.id.layout_check_box_del);
        layoutCheckNow = (LinearLayout) findViewById(R.id.layout_check_box_now);
        layoutDeliver = (LinearLayout) findViewById(R.id.layout_deliver);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

        deliveryET.addTextChangedListener(deliveryWatcher);
        discount.addTextChangedListener(discountWatcher);

        discount.addTextChangedListener(commaSeparator);
        deliveryET.addTextChangedListener(commaSeparator);
        fee.addTextChangedListener(comma2);


        final LinearLayout.LayoutParams showing =
                new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        final LinearLayout.LayoutParams showing3 =
                new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        final LinearLayout.LayoutParams showing2 =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);
        final LinearLayout.LayoutParams hiding2 =
                new LinearLayout.LayoutParams(0,
                        0);

        checkBox.setChecked(true);
        checked = true;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;

                if (isChecked){
                    layoutCheckDel.setLayoutParams(showing);
                    layoutCheckNow.setLayoutParams(showing);
                } else {
                    layoutCheckNow.setLayoutParams(hiding);
                    layoutCheckDel.setLayoutParams(showing3);
                    deliver.setChecked(false);
                    del = false;
                    deliveryET.setText("");
                    allocation.setSelection(0);
                }
            }
        });

        deliver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                del = isChecked;

                if(isChecked){
                    layoutDeliver.setLayoutParams(showing2);
                } else {
                    layoutDeliver.setLayoutParams(hiding2);
                    deliveryET.setText("");
                    allocation.setSelection(0);
                }
            }
        });

        // Spinner Drop down elements
        final List<String> paymentMethod = new ArrayList<String>();
        paymentMethod.add("--Select Payment Method--");
        paymentMethod.add("Cash");
        paymentMethod.add("Credit");
        paymentMethod.add("MPESA");
        paymentMethod.add("Mix");
        paymentMethod.add("Bank Transfer");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, R.layout.activity_spinner_item, paymentMethod);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        payment.setAdapter(dataAdapter1);

        payment.setFocusable(true);

        payment.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                                              @Override
                                              public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                                  // On selecting a spinner item
                                                  String item = parent.getItemAtPosition(position).toString();
                                                  paymentMeth = item;

                                                  feeTotal = 0;
                                                  fee.setText("");

                                                  LinearLayout.LayoutParams hide = new LinearLayout.LayoutParams(0, 0, 0);
                                                  LinearLayout.LayoutParams show = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                          ViewGroup.LayoutParams.WRAP_CONTENT, 2);

                                                  Mix mix1 = mdb.getMix(1);

                                                  if(paymentMeth.matches("Credit")){
                                                      dueDate.setVisibility(View.VISIBLE);
                                                      btnDueDate.setVisibility(View.VISIBLE);
                                                      dueDateText.setVisibility(View.VISIBLE);
                                                  } else if(!creditFromMix ){
                                                      dueDate.setVisibility(View.INVISIBLE);
                                                      btnDueDate.setVisibility(View.INVISIBLE);
                                                      dueDateText.setVisibility(View.INVISIBLE);
                                                  }

                                                  if(!(paymentMeth.matches("Credit"))){
//                                                          && !(paymentMeth.matches("--Select Payment Method--"))){
                                                      dueDate.setVisibility(View.INVISIBLE);
                                                      btnDueDate.setVisibility(View.INVISIBLE);
                                                      dueDateText.setVisibility(View.INVISIBLE);
                                                  }

                                                  if(!(paymentMeth.matches("MPESA") || paymentMeth.matches("Bank Transfer"))){
                                                      feeLL.setLayoutParams(hide);
                                                      feeTotal = 0;

                                                  } else {
                                                      feeLL.setLayoutParams(show);

                                                  }

//                                                  if((paymentMeth.matches("Cash") || paymentMeth.matches("MPESA")
//                                                          || paymentMeth.matches("--Select Payment Method--")
//                                                          || paymentMeth.matches("Bank Transfer") || paymentMeth.matches("Credit"))
//                                                          && (mix1.getFlag().matches("true"))){
//                                                      total.setText("" + globalTotal);
//                                                      fromMix = true;
//                                                      creditFromMix = false;
//
//                                                  }

                                                  if((paymentMeth.matches("Cash") || paymentMeth.matches("MPESA")
                                                          || paymentMeth.matches("Bank Transfer") || paymentMeth.matches("Credit"))
                                                          && mix1.getFlag().matches("true")){
                                                      total.setText("" + numberFormat.format(globalTotal));
                                                      fromMix = true;
                                                      creditFromMix = false;

                                                  }

                                                  if(paymentMeth.matches("Mix")){

                                                      if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                                                          Log.d("Values: ", "totals was null..");
                                                          prepareListData("Products");

                                                          listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                                                                  listDataChild, "PUR", true);

                                                          // setting list adapter
                                                          expListView.setAdapter(listAdapter);
                                                          expListView.expandGroup(0);

                                                      } else {
                                                          Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                                          intent.putExtra("FROM", "PAY");
                                                          intent.putExtra("SUPPLIER", "" + supplier.getText().toString());
                                                          intent.putExtra("NUMBER", "" + phone.getText().toString());
                                                          intent.putExtra("DISCOUNT", "" + discountTotal);
                                                          intent.putExtra("DATE", "" + dateView.getText().toString());
                                                          intent.putExtra("DUEDATE", "" + dueDate.getText().toString());
                                                          intent.putExtra("CREDIT", "YES");

                                                          if (checked) {
                                                              intent.putExtra("DELIVERY", "true");
                                                          } else {
                                                              intent.putExtra("DELIVERY", "false");
                                                          }
                                                          if (del) {
                                                              intent.putExtra("NOW", "true");
                                                          } else {
                                                              intent.putExtra("NOW", "false");
                                                          }
                                                          intent.putExtra("DELTOT", "" + deliveryTotl);
                                                          intent.putExtra("ALLOCATION", "" + allocationType);

                                                          int discountToatl = 0, deliveryTot = 0;
                                                          if (!(deliveryET.getText().toString().matches(""))){
                                                              deliveryTot = deliveryTotl;
//                                                              globalTotal = globalTotal
//                                                                      + Integer.parseInt(deliveryET.getText().toString());
                                                          }

                                                          if (!(discount.getText().toString().matches(""))){
                                                              discountToatl = discountTotal;

                                                          }

                                                          intent.putExtra("TOTAL", "" + (globalTotal + deliveryTot - discountToatl));

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

                                                      List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

                                                      int sum = 0;
                                                      for (GoodsPurchased pd: purchases){
                                                          sum = sum + pd.getTotal();
                                                      }
                                                      globalTotal = sum;

                                                      total.setText("" + numberFormat.format(globalTotal));

                                                      if (paymentMeth.matches("MPESA")) {
                                                          int feeSuj = mpesaFee(globalTotal);

                                                          fee.setText("" + feeSuj);
                                                          feeTotal = feeSuj;
                                                      }

                                                      fromMix = true;
                                                      creditFromMix = false;
                                                  }

                                                  // Showing selected spinner item
                                              }

                                              public void onNothingSelected(AdapterView arg0) {

                                              }
                                          }
        );

        /**
         * allocation spinner
         **/

        allocation = (Spinner) findViewById(R.id.allocation);

        // Spinner Drop down elements
        final List<String> allocationMethod = new ArrayList<>();
        allocationMethod.add("--Select Allocation Type--");
        allocationMethod.add("Volume");
        allocationMethod.add("Cost");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, R.layout.activity_spinner_item, allocationMethod);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        allocation.setAdapter(dataAdapter2);

        allocation.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView parent, View view, int position, long id) {
                        // On selecting a spinner item
                        allocationType = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView arg0) {

                    }
                }
        );

        /**
         * end spinner allocsation
         **/

        Mix mix = mdb.getMix(1);

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

                dueDate.setVisibility(View.VISIBLE);
                btnDueDate.setVisibility(View.VISIBLE);
                dueDateText.setVisibility(View.VISIBLE);

                creditFromMix = true;
            }

            text.append("(total) " + numberFormat.format(sum) + " = ");

            if(mix.getCash() > 0){
                text.append(" (actual) " + numberFormat.format(mix.getCash()));
            }

            if(mix.getBank() > 0){
                text.append(" (bank) " + numberFormat.format(mix.getBank()));
                if(mix.getBankFee() > 0){
                    text.append(" (Fee) " + numberFormat.format(mix.getBankFee()));

                }
            }

            if(mix.getMpesa() > 0){
                text.append(" (mpesa) " + numberFormat.format(mix.getMpesa()));
                if(mix.getMpesaFee() > 0){
                    text.append(" (Fee) " + numberFormat.format(mix.getMpesaFee()));

                }
            }

            if(mix.getCredit() > 0){
                text.append(" (credit) " + mix.getCredit());
            }

//            globalTotal = sum;

            total.setText(text);


        } else {
            List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

            int sum = 0;
            for (GoodsPurchased pd: purchases){
                sum = sum + pd.getTotal();
            }
            globalTotal = sum;

            total.setText("" + numberFormat.format(globalTotal));

            if (paymentMeth.matches("MPESA")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            fromMix = true;
            creditFromMix = false;

            if(paymentMeth.matches("Mix")) {
                payment.setSelection(0);
            }

            if(!paymentMeth.matches("Credit")) {
                dueDate.setVisibility(View.INVISIBLE);
                btnDueDate.setVisibility(View.INVISIBLE);
                dueDateText.setVisibility(View.INVISIBLE);

            }
        }

//        if(getIntent().getStringExtra("FROM").matches("MIX")){
//            fromMix = false;
//
//            StringBuilder text = new StringBuilder();
//
//            int sum = 0;
//            text.append("Mix Payment; ");
//
//            if(!(getIntent().getStringExtra("CASH").matches("0"))){
//                text.append(" (actual) " + getIntent().getStringExtra("CASH"));
//                sum = sum + Integer.parseInt(getIntent().getStringExtra("CASH"));
//            }
//
//            if(!(getIntent().getStringExtra("BANK").matches("0"))){
//                text.append(" (bank) " + getIntent().getStringExtra("BANK"));
//                sum = sum + Integer.parseInt(getIntent().getStringExtra("BANK"));
//            }
//
//            if(!(getIntent().getStringExtra("MPESA").matches("0"))){
//                text.append(" (mpesa) " + getIntent().getStringExtra("MPESA"));
//                sum = sum + Integer.parseInt(getIntent().getStringExtra("MPESA"));
//            }
//
//            if(!(getIntent().getStringExtra("CREDIT").matches("0"))){
//                text.append(" (credit) " + getIntent().getStringExtra("CREDIT"));
//                sum = sum + Integer.parseInt(getIntent().getStringExtra("CREDIT"));
//
//                dueDate.setVisibility(View.VISIBLE);
//                btnDueDate.setVisibility(View.VISIBLE);
//                dueDateText.setVisibility(View.VISIBLE);
//
//                creditFromMix = true;
//            }
//
//            text.append("= (total)" + numberFormat.format(sum));
//
//            globalTotal = sum;
//
//            supplier.setText("" + getIntent().getStringExtra("SUPPLIER"));
//            phone.setText("" + getIntent().getStringExtra("NUMBER"));
//            discount.setText("" + getIntent().getStringExtra("DISCOUNT"));
//            dateView.setText("" + getIntent().getStringExtra("DATE"));
//            dueDate.setText("" + getIntent().getStringExtra("DUEDATE"));
//
//            if (getIntent().getStringExtra("NOW").matches("true")){
//                deliver.setChecked(true);
//                del = true;
//            }
//            if (getIntent().getStringExtra("DELIVERY").matches("false")){
//                checkBox.setChecked(false);
//                checked = false;
//            }
//            deliveryET.setText("" + getIntent().getStringExtra("DELTOT"));
//            if (getIntent().getStringExtra("ALLOCATION").matches("Volume")){
//                allocation.setSelection(1);
//            } else if (getIntent().getStringExtra("ALLOCATION").matches("Cost")){
//                allocation.setSelection(2);
//            }
//
//            total.setText(text);
//
//        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDueDate(year, month+1, day);

        total.setEnabled(false);

        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);

        final List<String> sellersNames = new ArrayList<>();
        final List<String> buyerPhone = new ArrayList<>();

        final List<Payable> payable = padb.getAllPayable();
        for (Payable pd : payable) {
            String name[] = pd.getTo().split("-");
            String log = name[0];
            String logPhone = "";
            if(name.length > 1){
                logPhone = name[name.length - 1];
            }
            // Writing Contacts to log
            Log.d("Name: ", log);
            Log.d("Phone: ", logPhone);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < sellersNames.size(); i++){
                    if((sellersNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    sellersNames.add(log);
                    buyerPhone.add(logPhone);
                }
            }
        }

        List<Purchase> purchase = pdb.getAllPurchase();
        for (Purchase pd : purchase) {
            String name[] = pd.getSupplier().split("-");
            String log = name[0];
            String logPhone = "";
            if(name.length > 1){
                logPhone = name[name.length - 1];
            }
            // Writing Contacts to log
            Log.d("Name: ", log);
            Log.d("Phone: ", logPhone);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < sellersNames.size(); i++){
                    if((sellersNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    sellersNames.add(log);
                    buyerPhone.add(logPhone);
                }
            }
        }

        String[] sellers = sellersNames.toArray(new String[sellersNames.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sellers);

        //Find TextView control

        //Set the number of characters the user must type before the drop down list is shown
        supplier.setThreshold(1);
        //Set the adapter
        supplier.setAdapter(adapter);

        supplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                for(int i = 0; i < buyerPhone.size(); i++){
                    Log.d("chekin all", "" + buyerPhone.get(i) + " for " + sellersNames.get(i));
                    if(sellersNames.get(i).matches(item)){
                        phone.setText("" + buyerPhone.get(i));
                        Log.d("chekin phone", "" + buyerPhone.get(i) + " for " + item);
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
                Mix mix1 = mdb.getMix(1);

                List<Cash> allcash = cdb.getAllCash();

                int cash = 0;
                int bank = 0;
                int mpesa  = 0;

                for (Cash pd : allcash) {
                    cash = pd.getActual();
                    bank = pd.getBank();
                    mpesa  = pd.getMpesa();
                }

                int delTotal;

                if (deliveryET.getText().toString().matches("")){
                    delTotal = 0;
                } else {
                    delTotal = deliveryTotl;
                }

                //globalTotal = globalTotal + delTotal;

//                //int discountTotal = 0;
//
//                if(!(discount.getText().toString().matches(""))) {
//                    discountTotal = discountTotal;
//                }

                if(!fromMix && !(supplier.getText().toString().matches(""))) {
                    creditFromMix = false;
                }

                if(mix1.getCredit() > 0 && (supplier.getText().toString().matches(""))) {
                    creditFromMix = true;
                }

                boolean creditFromMix1 = false;

                if(mix1.getCredit() > 0 && (phone.getText().toString().matches(""))) {
                    creditFromMix1 = true;
                }

                if((total.getText().toString()).matches("") || ((globalTotal) <= 0)){
                    Log.d(TAG, "btnAdd; totals was null..");
                    prepareListData("Products");

                    listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PUR", true);

                    // setting list adapter
                    expListView.setAdapter(listAdapter);
                    expListView.expandGroup(0);

                } else if(paymentMeth.matches("--Select Payment Method--")){ //  && fromMix
                    Log.d(TAG, "btnAdd; no pay meth selected..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Non Selected");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("--Select Payment Method--");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(discountTotal >= globalTotal){
                    Log.d(TAG, "btnAdd; high discount..");

                    discount.setError("Discount Cannot Exceed Total Amount");
                    discount.requestFocus();

                } else if(paymentMeth.matches("Credit") && supplier.getText().toString().matches("")){
                    Log.d(TAG, "btnAdd; no cutomer on credit..");

                    supplier.setError("Please Enter Customer Name");
                    supplier.requestFocus();

                } else if(paymentMeth.matches("Credit") && phone.getText().toString().matches("")){
                    Log.d(TAG, "btnAdd; no phone on credit..");

                    phone.setError("Please Enter Phone Number");
                    phone.requestFocus();

                } else if(creditFromMix){
                    Log.d(TAG, "btnAdd; no cutomer on credit from mix..");

                    supplier.setError("Please Enter Customer Name");
                    supplier.requestFocus();

                } else if(creditFromMix1){
                    Log.d(TAG, "btnAdd; no cutomer on credit from mix..");

                    phone.setError("Please Enter Phone Number");
                    phone.requestFocus();

                } else if(del && (deliveryET.getText().toString().matches("")
                        || deliveryTotl <= 0)){
                    Log.d(TAG, "btnAdd; delivery amount error..");

                    deliveryET.setError("Please Enter Cost of Delivery");
                    deliveryET.requestFocus();

                } else if(del && (allocationType.matches("--Select Allocation Type--"))){
                    Log.d(TAG, "btnAdd; delivery allocation error.." + allocationType);

                    TextView errorText = (TextView)allocation.getSelectedView();
                    errorText.setError("--Select Allocation Type--");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("--Select Allocation Type--");//changes the selected item text to this
                    allocation.requestFocus();

                } else if(paymentMeth.matches("Cash") && ((globalTotal + delTotal - discountTotal) > cash)){
                    Log.d(TAG, "btnAdd; actual too little..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough Cash available");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Cash");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(paymentMeth.matches("MPESA") && ((globalTotal  + feeTotal + delTotal - discountTotal) > mpesa)){
                    Log.d(TAG, "btnAdd; mpesa too little..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough money in MPESA");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(paymentMeth.matches("Bank Transfer") && ((globalTotal + feeTotal + delTotal - discountTotal) > bank)){
                    Log.d(TAG, "btnAdd; bank too little..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough money in Bank A/C");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if (!fromMix) {
                    Mix mix = mdb.getMix(1);

                    if ((mix.getCash() > 0) && (mix.getCash() > cash)) {
                        Log.d(TAG, "btnAdd; actual too little from mix..");

                        TextView errorText = (TextView) payment.getSelectedView();
                        errorText.setError("Not Enough Cash");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Not Enough Cash");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    } else if ((mix.getMpesa() > 0) && ((mix.getMpesa() + mix.getMpesaFee()) > mpesa)) {
                        Log.d(TAG, "btnAdd; mpesa too little from mix..");

                        TextView errorText = (TextView) payment.getSelectedView();
                        errorText.setError("Not Enough money in MPESA");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    } else if ((mix.getBank() > 0) && ((mix.getBank() + mix.getBankFee()) > bank)) {
                        Log.d(TAG, "btnAdd; bank too little from mix..");

                        TextView errorText = (TextView) payment.getSelectedView();
                        errorText.setError("Not Enough money in Bank");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    }
//                    else if ((mix.getBank() + mix.getMpesa() + mix.getCash() + mix.getBankFee() + mix.getMpesaFee())
//                            > (globalTotal + delTotal - discountTotal - mix.getCredit())) {
//                        Log.d(TAG, "btnAdd; total was too low..");
//                        Log.d(TAG, "MPESA " + mix.getMpesa());
//                        Log.d(TAG, "CASH " + mix.getCash());
//                        Log.d(TAG, "BANK " + mix.getBank());
//                        Log.d(TAG, "BANK fee" + mix.getBankFee());
//                        Log.d(TAG, "MPESA " + mix.getMpesa());
//                        Log.d(TAG, "TOT " + ((mix.getBank() + mix.getMpesa() + mix.getCash())));
//                        Log.d(TAG, "globalTotal " + (globalTotal + delTotal - discountTotal));
//
//                        TextView errorText = (TextView) payment.getSelectedView();
//                        errorText.setError("Not Enough Money");
//                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
//                        errorText.setText("Not Enough Money");//changes the selected item text to this
//                        payment.requestFocus();
//                        payment.performClick();
//
//                    }
                    else {
                        addFunction();

                    }
                } else {
                    //globalTotal = globalTotal - discountTotal;

                    addFunction();
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

                new AlertDialog.Builder(PurchaseActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Exit")
                        .setMessage("This will remove all entries")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Stop the activity
                                //PurchaseActivity.this.finish();
                                gpdb.deleteByForeignKey(pdb.getRowCount() + 1);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

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

        /**
         * the bellow part is for handling the epandable list for adding products
         * **/
        prepareListData("Normal");

        listAdapter = new FourELAdapter(this, listDataHeader,
                listDataChild, "PUR", true);

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

                listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                        listDataChild, "PUR", true);

                // setting list adapter
                //expListView.setAdapter(listAdapter);

                layoutSV.setLayoutParams(show);
                layoutEL.setLayoutParams(show);

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                prepareListData("Show");

                listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                        listDataChild, "PUR", true);

                // setting list adapter
                //expListView.setAdapter(listAdapter);

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
                        GoodsPurchased goodsSales = gpdb.getPurchase(Integer.parseInt(idList.get(Key)));
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

                        new AlertDialog.Builder(PurchaseActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Deletion")
                                .setMessage("Delete; " + name + " " + decimalFormat2.format(numbe) + units + " " + decimalFormat.format(cost)
                                        + " Per " + unitsCost + " " + numberFormat.format(goodsSales.getTotal()))
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GoodsPurchased goodsSales2 = gpdb.getPurchase(idGoods);
                                        gpdb.deletePurchase(goodsSales2);

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

    @Override
    protected void onResume() {
        super.onResume();

        prepareListData("Normal");

        listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                listDataChild, "PUR", true);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);

        Mix mix = mdb.getMix(1);

        Log.d(TAG, "Flag " + mix.getFlag() + " actual " + mix.getCash() + " bank " + mix.getBank()
                + " mpesa " + mix.getMpesa() + " credit " + mix.getCredit());

        if (mix.getFlag().matches("true")){
            fromMix = false;

            StringBuilder text = new StringBuilder();


            //text.append("Mix Payment; ");

            int sum = 0;
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

                dueDate.setVisibility(View.VISIBLE);
                btnDueDate.setVisibility(View.VISIBLE);
                dueDateText.setVisibility(View.VISIBLE);

                creditFromMix = true;
            }

            text.append("(total) " + numberFormat.format(sum) + " = ");

            if(mix.getCash() > 0){
                text.append(" (actual) " + numberFormat.format(mix.getCash()));
            }

            if(mix.getBank() > 0){
                text.append(" (bank) " + numberFormat.format(mix.getBank()));
                if(mix.getBankFee() > 0){
                    text.append(" (Fee) " + numberFormat.format(mix.getBankFee()));

                }
            }

            if(mix.getMpesa() > 0){
                text.append(" (mpesa) " + numberFormat.format(mix.getMpesa()));
                if(mix.getMpesaFee() > 0){
                    text.append(" (Fee) " + numberFormat.format(mix.getMpesaFee()));

                }
            }

            if(mix.getCredit() > 0){
                text.append(" (credit) " + mix.getCredit());
            }

//            globalTotal = sum;

            total.setText(text);

        } else {
            List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

            int sum = 0;
            for (GoodsPurchased pd: purchases){
                sum = sum + pd.getTotal();
            }

            globalTotal = sum;

            total.setText("" + numberFormat.format(globalTotal));

            if (paymentMeth.matches("MPESA")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            if(paymentMeth.matches("Mix")) {
                payment.setSelection(0);
            }

            if(!paymentMeth.matches("Credit")) {
                dueDate.setVisibility(View.INVISIBLE);
                btnDueDate.setVisibility(View.INVISIBLE);
                dueDateText.setVisibility(View.INVISIBLE);

            }
        }

    }

    /*
    add function
     */

    public void addFunction(){
        List<GoodsPurchased> Purchased = gpdb.getByForeignKey(pdb.getRowCount() + 1);

        int delTotal;

        if (deliveryET.getText().toString().matches("")){
            delTotal = 0;
        } else {
            delTotal = deliveryTotl;
        }

        Mix globalMix = mdb.getMix(1);

//        int discountTotal = 0;
//
//        if(!(discount.getText().toString().matches(""))) {
//            discountTotal = Integer.parseInt(discount.getText().toString());
//        }

        StringBuilder confMessaage = new StringBuilder();

        confMessaage.append("From " + supplier.getText().toString() + " for "
                + numberFormat.format((globalTotal - discountTotal)) + " on " + dateView.getText().toString());

        if (paymentMeth.matches("Credit") || globalMix.getCredit() > 0){
            confMessaage.append("\nCredit to be paid on " + dueDateText.getText().toString());
        }

        confMessaage.append("\nMode of payment; ");

        if(!fromMix){
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

        if(checked){
            if(!del) {
                confMessaage.append("\nRequires Delivery Later");
            } else {
                confMessaage.append("\nDelivered now for " + numberFormat.format(delTotal)
                        + " allocation by " + allocationType);
            }
        } else {
            confMessaage.append("\nNO Delivery Required");
        }

        confMessaage.append("\n\nGoods Purchased;");

        StringBuilder titleBuild = new StringBuilder();

        titleBuild.append("\nName, Number, Price, Total");

        if (del){
            titleBuild.append(", Delivery Cost");
        }

        String Title = titleBuild.toString();
        confMessaage.append("" + Title);

        for (GoodsPurchased pd : Purchased) {
            String name = pd.getProduct();
            if (name.matches("--Delivery--")) {
                String del[] = name.split("-");
                name = del[2];
            }

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
                cost = cost * 1000;
            }
            if (pd.getUnit().matches("mLs")) {
                unitsCost = "Ls";
                cost = cost * 1000;
            }

            StringBuilder displBuilder = new StringBuilder();

            displBuilder.append("\n" + name + ", " + decimalFormat2.format(numbe) + " " + units + ", "
                    + decimalFormat.format(cost) + " Per " + unitsCost + ", " + numberFormat.format(pd.getTotal()));

            if (del){
                if (allocationType.matches("Volume")) {
                    double numb = 0;

                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pdb.getRowCount() + 1);

                    for (GoodsPurchased pd1 : allGoods) {
                        if(pd1.getUnit().matches("Pcs")){
                            Log.d(TAG, "prepareListData, Volume, Pcs " + pd1.getNumber());
                            numb = numb + pd1.getNumber();
                        } else {
                            numb = numb + ((double) pd1.getNumber() / 1000.0);
                            Log.d(TAG, "prepareListData, Volume, else " + (pd1.getNumber() / 1000.0));
                        }
                    }

                    double original = pd.getNumber();

                    if(!(pd.getUnit().matches("Pcs"))){
                        original = ((double) pd.getNumber() / 1000.0);
                    }

                    double _dcost = (original / numb) * (double) delTotal;
                    //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                    int dcost = (int) (Math.round(_dcost));
                    String log = "Name: " + pd.getProduct() + "\t\tNumber: " + pd.getNumber() +
                            "\t\tDelivery Cost: " + numberFormat.format(dcost);//(pd.getNumber() / numb) *
                    displBuilder.append(", " + numberFormat.format(dcost));

                } else if (allocationType.matches("Cost")) {
                    double total1 = 0;

                    List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pdb.getRowCount() + 1);

                    for (GoodsPurchased pd1 : allGoods) {

                        if(pd1.getUnit().matches("Pcs")){
                            Log.d(TAG, "prepareListData, Cost, Pcs " + pd1.getTotal());
                            total1 = total1 + pd1.getTotal();
                        } else {
                            Log.d(TAG, "prepareListData, Cost, else " + (pd1.getTotal()));
                            total1 = total1 + (pd1.getTotal());
                        }
                    }

                    double cost1 = pd.getTotal();
                    double _dcost = (cost1 / total1) * (double) delTotal;

                    int dcost = (int) (Math.round(_dcost));
                    String log = "Name: " + pd.getProduct() + "\t\tCost: " + numberFormat.format(pd.getTotal()) +
                            "\t\tDelivery Cost: " + numberFormat.format(dcost);//(pd.getCost() / purchase.getTotal()) *

                    displBuilder.append(", " + numberFormat.format(dcost));

                }

            }

            String displ = displBuilder.toString();
            confMessaage.append("" + displ);
        }

        new AlertDialog.Builder(PurchaseActivity.this)
                .setIcon(R.drawable.purchase_icon_2)
                .setTitle("Confirm Purchase")
                .setMessage("" + confMessaage.toString())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Cash> allcash = cdb.getAllCash();

                        int cash = 0;
                        int bank = 0;
                        int mpesa = 0;

                        for (Cash pd : allcash) {
                            cash = pd.getActual();
                            bank = pd.getBank();
                            mpesa = pd.getMpesa();
                        }

                        int delTotal2;

                        if (deliveryET.getText().toString().matches("")){
                            delTotal2 = 0;
                        } else {
                            delTotal2 = deliveryTotl;
                        }

                        int discountTotal2 = 0;

                        if(!(discount.getText().toString().matches(""))) {
                            discountTotal2 = discountTotal;
                        }

                        if (!fromMix) {
                            Mix mix = mdb.getMix(1);

                            if (mix.getCash() > 0 && (mix.getCash() > cash)) {
                                Log.d(TAG, "btnAdd; actual too little from mix..");

                                TextView errorText = (TextView)payment.getSelectedView();
                                errorText.setError("Not Enough Cash");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Not Enough Cash");//changes the selected item text to this
                                payment.requestFocus();

                            } else if (mix.getMpesa() > 0 && (mix.getMpesa() > mpesa)) {
                                Log.d(TAG, "btnAdd; mpesa too little from mix..");

                                TextView errorText = (TextView)payment.getSelectedView();
                                errorText.setError("Not Enough money in MPESA");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                                payment.requestFocus();

                            } else if (mix.getBank() > 0 && (mix.getBank() > bank)) {
                                Log.d(TAG, "btnAdd; bank too little from mix..");

                                TextView errorText = (TextView)payment.getSelectedView();
                                errorText.setError("Not Enough money in Bank");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                                payment.requestFocus();

                            } else {
                                StringBuilder pay = new StringBuilder();
                                if (mix.getCash() > 0) {
                                    cdb.updateSingleCashPurchase("Cash", mix.getCash());
                                    pay.append("Cash ");
                                }

                                if (mix.getBank() > 0) {
                                    cdb.updateSingleCashPurchase("Bank Transfer", mix.getBank());
                                    pay.append("Bank Transfer ");
                                    if (mix.getBankFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMeth,
                                                ("Purchase " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                    }
                                }

                                if (mix.getMpesa() > 0) {
                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());
                                    pay.append("MPESA ");
                                    if (mix.getMpesaFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMeth,
                                                ("Purchase " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                    }
                                }

                                if (mix.getCredit() > 0) {
                                    StringBuilder nameBuild = new StringBuilder();
                                    nameBuild.append(supplier.getText().toString());

                                    if (!(phone.getText().toString().matches(""))) {
                                        nameBuild.append("-" + phone.getText().toString());
                                    }

                                    padb.addPayable(new Payable(nameBuild.toString(), (mix.getCredit()),
                                            dueDate.getText().toString(), dateView.getText().toString()));
                                    pay.append("Credit");
                                }
                                //Inserting Contacts
                                StringBuilder nameBuild = new StringBuilder();
                                nameBuild.append(supplier.getText().toString());

                                if (!(phone.getText().toString().matches(""))) {
                                    nameBuild.append("-" + phone.getText().toString());
                                }

                                pdb.addPurchase(new Purchase(nameBuild.toString(), (globalTotal - discountTotal2),
                                        pay.toString(), dateView.getText().toString()));

                                if (del) {
                                    int cst = mix.getCash();
                                    int bnk = mix.getBank();
                                    int crdt = mix.getCredit();
                                    int mpes = mix.getMpesa();

                                    if (deliveryTotl < (cst + bnk + mpes)) {
                                        ddb.addDelivery(new Delivery(allocationType, delTotal2,
                                                pay.toString(), dateView.getText().toString(), ("" + pdb.getRowCount())));
                                    } else {
                                        padb.addPayable(new Payable(nameBuild.toString(), crdt,
                                                dueDate.getText().toString(), dateView.getText().toString()));
                                    }
                                }

                            }
                        } else {

                            Log.d("Values: ", "All values filled");

                            if (paymentMeth.matches("Credit")) {
                                StringBuilder nameBuild = new StringBuilder();
                                nameBuild.append(supplier.getText().toString());

                                if (!(phone.getText().toString().matches(""))) {
                                    nameBuild.append("-" + phone.getText().toString());
                                }

                                padb.addPayable(new Payable(nameBuild.toString(), (globalTotal - discountTotal2),
                                        dueDate.getText().toString(), dateView.getText().toString()));

                                if (del) {
                                    padb.addPayable(new Payable("Delivery By " + nameBuild.toString(), deliveryTotl,
                                            dueDate.getText().toString(), dateView.getText().toString()));
                                }

                            } else {
                                cdb.updateSingleCashPurchase(paymentMeth, (globalTotal - discountTotal2 + delTotal2));
                            }

                            //Inserting Contacts
                            StringBuilder nameBuild = new StringBuilder();
                            nameBuild.append(supplier.getText().toString());

                            if (!(phone.getText().toString().matches(""))) {
                                nameBuild.append("-" + phone.getText().toString());
                            }
                            pdb.addPurchase(new Purchase(nameBuild.toString(), (globalTotal - discountTotal2 + delTotal2),
                                    paymentMeth, dateView.getText().toString()));

                            if (del){
                                ddb.addDelivery(new Delivery(allocationType, delTotal2,
                                        paymentMeth, dateView.getText().toString(), ("" + pdb.getRowCount())));
                            }
                        }

                    /*
                    Updating inventory
                     */

                        List<Inventory> inventories = idb.getAllInventory();
                        List<GoodsPurchased> goodsPurchased = gpdb.getByForeignKey(pdb.getRowCount());

                        for (GoodsPurchased pd : goodsPurchased) {
                            if (discountTotal2 > 0) {
                                double tot = pd.getTotal(), gtot = globalTotal;
                                double goodDiscount = (tot / gtot) * (double) discountTotal2;

                                pd.setTotal((int) (Math.round((double) (pd.getTotal()) - goodDiscount)));

                                pd.setCost((int) (Math.round((double) (pd.getTotal()) / (double) (pd.getNumber()))));
                            }
                            gpdb.updatePurchase(pd);

                        }

                        //updating date for purchases
                        for (GoodsPurchased pd : goodsPurchased) {
                            pd.setDate(dateView.getText().toString());
                            gpdb.updatePurchase(pd);
                        }

                        //updating inventory
                        for (GoodsPurchased pd : goodsPurchased) {
                            boolean flag = true;
                            for (Inventory pd1 : inventories) {
                                String log = pd1.getProduct();
                                if (log.matches(pd.getProduct())) {
                                    idb.updateNumberInventoryPurchase(pd.getProduct(), pd.getNumber(), pd.getTotal());
                                    flag = false;

                                    // Reading all entries
                                    Log.d("Updating: ", "updating old...");
                                    List<Inventory> inventory = idb.getAllInventory();

                                    for (Inventory pd2 : inventory) {
                                        String log1 = "Transaction Id: " + pd2.getID() + "\t\tName: " + pd2.getProduct()
                                                + "\t\tNumber: " + pd2.getNumber() + "\t\tCost: " + pd2.getCost()
                                                + "\t\tTotal: " + pd2.getTotal();
                                        // Writing Contacts to log
                                        Log.d("Name: ", log1);
                                    }
                                }
                            }
                            if (flag) {
                                idb.addInventory(new Inventory(pd.getProduct(), pd.getNumber(),
                                        pd.getUnit(), pd.getCost(), pd.getTotal(), 0));

                            }
                        }

                    /*
                    Updating inventory
                     */
                        if (!checked) {
                            ddb.addDelivery(new Delivery("None", 0,
                                    "None", dateView.getText().toString(), ("" + pdb.getRowCount())));
                        }

                        //update inventorry by delivery
                        if (del){
//                            double delTotal2 = Double.parseDouble(deliveryET.getText().toString());
                            Log.d(TAG, "submitbnt, del " + allocationType);

                            if (allocationType.matches("Volume")) {
                                double numb = 0;

                                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pdb.getRowCount());

                                for (GoodsPurchased pd1 : allGoods) {
                                    if(pd1.getUnit().matches("Pcs")){
                                        Log.d(TAG, "submitbnt, Volume, Pcs " + pd1.getNumber());
                                        numb = numb + pd1.getNumber();
                                    } else {
                                        numb = numb + ((double) pd1.getNumber() / 1000.0);
                                        Log.d(TAG, "submitbnt, Volume, else " + ((double) pd1.getNumber() / 1000.0));
                                    }
                                }

                                for (GoodsPurchased pd1 : allGoods) {
                                    double original = pd1.getNumber();

                                    if(!(pd1.getUnit().matches("Pcs"))){
                                        original = ((double) pd1.getNumber() / 1000.0);
                                    }

                                    double _dcost = ( original / numb) * (double) delTotal2;
                                    //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                                    int dcost = (int) (Math.round(_dcost));
                                    String log = "Name: " + pd1.getProduct() + "\t\tNumber: " + pd1.getNumber() +
                                            "\t\tDelivery Cost: " + numberFormat.format(dcost);//(pd.getNumber() / numb) *

                                    Log.d(TAG, "submitbnt volume inventory " + pd1.getProduct() + " " + dcost);
                                    idb.updateNumberInventoryDelivery(pd1.getProduct(), dcost);
                                }

                            } else if (allocationType.matches("Cost")) {
                                double total1 = 0;

                                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pdb.getRowCount());

                                for (GoodsPurchased pd1 : allGoods) {

                                    if(pd1.getUnit().matches("Pcs")){
                                        Log.d(TAG, "submitbnt, Cost, Pcs " + pd1.getTotal());
                                        total1 = total1 + pd1.getTotal();
                                    } else {
                                        Log.d(TAG, "submitbnt, Cost, else " + (pd1.getTotal()));
                                        total1 = total1 + (pd1.getTotal());
                                    }
                                }

                                for (GoodsPurchased pd1 : allGoods) {
                                    double cost1 = pd1.getTotal();
                                    double _dcost = (cost1 / total1) * (double) delTotal2;

                                    int dcost = (int) (Math.round(_dcost));
                                    String log = "Name: " + pd1.getProduct() + "\t\tCost: " + numberFormat.format(pd1.getTotal()) +
                                            "\t\tDelivery Cost: " + numberFormat.format(dcost);//(pd.getCost() / purchase.getTotal()) *

                                    Log.d(TAG, "submitbnt cost inventory " + pd1.getProduct() + " " + dcost);
                                    idb.updateNumberInventoryDelivery(pd1.getProduct(), dcost);
                                }

                            }

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

                        if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                            String feeName = "MPESA";

                            if (paymentMeth.matches("Bank Transfer")) {
                                feeName = "Bank A/C";
                            }

                            edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMeth,
                                    ("Purchase " + feeName + " Transfer"), dateView.getText().toString()));
                            cdb.updateSingleCashPurchase(paymentMeth, feeTotal);
                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNeutralButton("Confirm and Set Sale Price", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Cash> allcash = cdb.getAllCash();

                        int cash = 0;
                        int bank = 0;
                        int mpesa = 0;

                        for (Cash pd : allcash) {
                            cash = pd.getActual();
                            bank = pd.getBank();
                            mpesa = pd.getMpesa();
                        }

                        int delTotal2;

                        if (deliveryET.getText().toString().matches("")){
                            delTotal2 = 0;
                        } else {
                            delTotal2 = deliveryTotl;
                        }

                        int discountTotal2 = 0;

                        if(!(discount.getText().toString().matches(""))) {
                            discountTotal2 = discountTotal;
                        }

                        if (!fromMix) {
                            Mix mix = mdb.getMix(1);

                            if (mix.getCash() > 0 && (mix.getCash() > cash)) {
                                Log.d(TAG, "btnAdd; actual too little from mix..");

                                TextView errorText = (TextView)payment.getSelectedView();
                                errorText.setError("Not Enough Cash");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Not Enough Cash");//changes the selected item text to this
                                payment.requestFocus();

                            } else if (mix.getMpesa() > 0 && (mix.getMpesa() > mpesa)) {
                                Log.d(TAG, "btnAdd; mpesa too little from mix..");

                                TextView errorText = (TextView)payment.getSelectedView();
                                errorText.setError("Not Enough money in MPESA");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                                payment.requestFocus();

                            } else if (mix.getBank() > 0 && (mix.getBank() > bank)) {
                                Log.d(TAG, "btnAdd; bank too little from mix..");

                                TextView errorText = (TextView)payment.getSelectedView();
                                errorText.setError("Not Enough money in Bank");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                                payment.requestFocus();

                            } else {
                                StringBuilder pay = new StringBuilder();
                                if (mix.getCash() > 0) {
                                    cdb.updateSingleCashPurchase("Cash", mix.getCash());
                                    pay.append("Cash ");
                                }

                                if (mix.getBank() > 0) {
                                    cdb.updateSingleCashPurchase("Bank Transfer", mix.getBank());
                                    pay.append("Bank Transfer ");
                                    if (mix.getBankFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMeth,
                                                ("Purchase " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                    }
                                }

                                if (mix.getMpesa() > 0) {
                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());
                                    pay.append("MPESA ");
                                    if (mix.getMpesaFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMeth,
                                                ("Purchase " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                    }
                                }

                                if (mix.getCredit() > 0) {
                                    StringBuilder nameBuild = new StringBuilder();
                                    nameBuild.append(supplier.getText().toString());

                                    if (!(phone.getText().toString().matches(""))) {
                                        nameBuild.append("-" + phone.getText().toString());
                                    }

                                    padb.addPayable(new Payable(nameBuild.toString(), (mix.getCredit()),
                                            dueDate.getText().toString(), dateView.getText().toString()));
                                    pay.append("Credit");
                                }
                                //Inserting Contacts
                                StringBuilder nameBuild = new StringBuilder();
                                nameBuild.append(supplier.getText().toString());

                                if (!(phone.getText().toString().matches(""))) {
                                    nameBuild.append("-" + phone.getText().toString());
                                }

                                pdb.addPurchase(new Purchase(nameBuild.toString(), (globalTotal - discountTotal2),
                                        pay.toString(), dateView.getText().toString()));

                                if (del) {
                                    int cst = mix.getCash();
                                    int bnk = mix.getBank();
                                    int crdt = mix.getCredit();
                                    int mpes = mix.getMpesa();

                                    if (deliveryTotl < (cst + bnk + mpes)) {
                                        ddb.addDelivery(new Delivery(allocationType, delTotal2,
                                                pay.toString(), dateView.getText().toString(), ("" + pdb.getRowCount())));
                                    } else {
                                        padb.addPayable(new Payable(nameBuild.toString(), crdt,
                                                dueDate.getText().toString(), dateView.getText().toString()));
                                    }
                                }

                            }
                        } else {

                            Log.d("Values: ", "All values filled");

                            if (paymentMeth.matches("Credit")) {
                                StringBuilder nameBuild = new StringBuilder();
                                nameBuild.append(supplier.getText().toString());

                                if (!(phone.getText().toString().matches(""))) {
                                    nameBuild.append("-" + phone.getText().toString());
                                }

//                                int discountTotal2 = 0;
//
//                                if (!discount.getText().toString().matches("")){
//                                    discountTotal2 = Integer.parseInt(discount.getText().toString());
//                                }

                                padb.addPayable(new Payable(nameBuild.toString(), (globalTotal - discountTotal2),
                                        dueDate.getText().toString(), dateView.getText().toString()));

                                if (del) {
                                    padb.addPayable(new Payable("Delivery By " + nameBuild.toString(), deliveryTotl,
                                            dueDate.getText().toString(), dateView.getText().toString()));
                                }

                            } else {
                                cdb.updateSingleCashPurchase(paymentMeth, (globalTotal - discountTotal2 + delTotal2));
                            }

                            //Inserting Contacts
                            StringBuilder nameBuild = new StringBuilder();
                            nameBuild.append(supplier.getText().toString());

                            if (!(phone.getText().toString().matches(""))) {
                                nameBuild.append("-" + phone.getText().toString());
                            }
                            pdb.addPurchase(new Purchase(nameBuild.toString(), (globalTotal - discountTotal2 + delTotal2),
                                    paymentMeth, dateView.getText().toString()));

                            if (del){
                                ddb.addDelivery(new Delivery(allocationType, delTotal2,
                                        paymentMeth, dateView.getText().toString(), ("" + pdb.getRowCount())));
                            }
                        }

                    /*
                    Updating inventory
                     */

                        List<Inventory> inventories = idb.getAllInventory();
                        List<GoodsPurchased> goodsPurchased = gpdb.getByForeignKey(pdb.getRowCount());

                        for (GoodsPurchased pd : goodsPurchased) {
                            if (discountTotal2 > 0) {
                                double tot = (double) pd.getTotal(), gtot = globalTotal;
                                double goodDiscount = (tot / gtot) * (double) discountTotal2;

                                pd.setTotal((int) (Math.round(( (double) pd.getTotal()) - goodDiscount)));

                                pd.setCost((((double) pd.getTotal()) - goodDiscount)
                                        / (double) (pd.getNumber()));
                            }
                            gpdb.updatePurchase(pd);

                        }

                        //updating date for purchases
                        for (GoodsPurchased pd : goodsPurchased) {
                            pd.setDate(dateView.getText().toString());
                            gpdb.updatePurchase(pd);

                        }

                        //updating inventory
                        for (GoodsPurchased pd : goodsPurchased) {
                            boolean flag = true;
                            for (Inventory pd1 : inventories) {
                                String log = pd1.getProduct();
                                if (log.matches(pd.getProduct())) {
                                    idb.updateNumberInventoryPurchase(pd.getProduct(), pd.getNumber(), pd.getTotal());
                                    flag = false;

                                    // Reading all entries
                                    Log.d("Updating: ", "updating old...");
                                    List<Inventory> inventory = idb.getAllInventory();

                                    for (Inventory pd2 : inventory) {
                                        String log1 = "Transaction Id: " + pd2.getID() + "\t\tName: " + pd2.getProduct()
                                                + "\t\tNumber: " + pd2.getNumber() + "\t\tCost: " + pd2.getCost()
                                                + "\t\tTotal: " + pd2.getTotal();
                                        // Writing Contacts to log
                                        Log.d("Name: ", log1);
                                    }
                                }
                            }
                            if (flag) {
                                idb.addInventory(new Inventory(pd.getProduct(), pd.getNumber(),
                                        pd.getUnit(), pd.getCost(), pd.getTotal(), 0));

                            }
                        }

                    /*
                    Updating inventory
                     */
                        if (!checked) {
                            ddb.addDelivery(new Delivery("None", 0,
                                    "None", dateView.getText().toString(), ("" + pdb.getRowCount())));
                        }

                        //update inventorry by delivery
                        if (del){
//                            double delTotal2 = Double.parseDouble(deliveryET.getText().toString());
                            Log.d(TAG, "submitbnt, del " + allocationType);

                            if (allocationType.matches("Volume")) {
                                double numb = 0;

                                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pdb.getRowCount());

                                for (GoodsPurchased pd1 : allGoods) {
                                    if(pd1.getUnit().matches("Pcs")){
                                        Log.d(TAG, "submitbnt, Volume, Pcs " + pd1.getNumber());
                                        numb = numb + pd1.getNumber();
                                    } else {
                                        numb = numb + ((double) pd1.getNumber() / 1000.0);
                                        Log.d(TAG, "submitbnt, Volume, else " + ((double) pd1.getNumber() / 1000.0));
                                    }
                                }

                                for (GoodsPurchased pd1 : allGoods) {
                                    double original = pd1.getNumber();

                                    if(!(pd1.getUnit().matches("Pcs"))){
                                        original = ((double) pd1.getNumber() / 1000.0);
                                    }

                                    double _dcost = (original / numb) * (double) delTotal2;
                                    //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                                    int dcost = (int) (Math.round(_dcost));

                                    Log.d(TAG, "submitbnt volume inventory " + pd1.getProduct() + " " + dcost);
                                    idb.updateNumberInventoryDelivery(pd1.getProduct(), dcost);
                                }

                            } else if (allocationType.matches("Cost")) {
                                double total1 = 0;

                                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pdb.getRowCount());

                                for (GoodsPurchased pd1 : allGoods) {
                                    total1 = total1 + pd1.getTotal();

                                }

                                for (GoodsPurchased pd1 : allGoods) {
                                    double cost1 = pd1.getTotal();
                                    double _dcost = (cost1 / total1) * (double) delTotal2;

                                    int dcost = (int) (Math.round(_dcost));

                                    Log.d(TAG, "submitbnt cost inventory " + pd1.getProduct() + " " + dcost);
                                    idb.updateNumberInventoryDelivery(pd1.getProduct(), dcost);

                                }
                            }
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

                        if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                            String feeName = "MPESA";

                            if (paymentMeth.matches("Bank Transfer")) {
                                feeName = "Bank A/C";
                            }

                            edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMeth,
                                    ("Purchase " + feeName + " Transfer"), dateView.getText().toString()));
                            cdb.updateSingleCashPurchase(paymentMeth, feeTotal);
                        }

//                        Intent intent = new Intent(getApplicationContext(), EditSalePriceActivity.class);
                        Intent intent = new Intent(getApplicationContext(), InventoryDisplayActivity.class);
                        startActivity(intent);

                        Toast.makeText(PurchaseActivity.this, "Select a Product for Sale Price Editing",
                                Toast.LENGTH_LONG).show();

                        finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

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
            actualDate.set(Calendar.MONTH, month);
            actualDate.set(Calendar.YEAR, year);

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
        List<String> purchase = new ArrayList<>();

        List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

        String title = "Name-Number-Price-Total";
        //purchase.add(Title);
        int count = 0;
        int tot = 0;

        for (GoodsPurchased pd : purchases) {
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
                cost = cost * 1000.0;
            }
            if(pd.getUnit().matches("mLs")){
                unitsCost = "Ls";
                cost = cost * 1000.0;
            }

            String displ = name + "-" + decimalFormat2.format(numbe) + " " + units + "-"
                    + numberFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
            // Writing Contacts to log
            purchase.add(displ);
            tot = tot + pd.getTotal();
            idList.put("" + count, "" + pd.getId());
            count++;
        }

        //setting auto add total
        String showTot = "" + numberFormat.format(tot);

        if(!(getIntent().getStringExtra("FROM").matches("MIX"))) {
            total.setText(showTot);
            globalTotal = tot;
        }

        String groupName;
        if(what.matches("Products")){
            groupName = "Must Add Product.";
        } else if(what.matches("Total")){
            groupName = "The Total Cannot be 0";
        } else if(what.matches("Show")){
            groupName = "Show Items Being Purchased; " + count;
        } else {
            groupName = "Hide/Show Items Being Purchased; " + count;
        }
        listDataHeader.add(groupName + "-" + title);

        listDataChild.put(listDataHeader.get(0), purchase); // Header, Child data
    }

    /**
     * text watcher for textWatcher
     **/

    private final TextWatcher deliveryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!(deliveryET.getText().toString().matches(""))) {
                if (paymentMeth.matches("Mix")) {
                    Mix mix = mdb.getMix(1);

                    mix.setFlag("false");
                    mix.setCredit(0);
                    mix.setCash(0);
                    mix.setMpesa(0);
                    mix.setBank(0);
                    mix.setMpesaFee(0);
                    mix.setBankFee(0);

                    mdb.updateMix(mix);

                    List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

                    int sum = 0;
                    for (GoodsPurchased pd: purchases){
                        sum = sum + pd.getTotal();
                    }

                    globalTotal = sum;

                    total.setText("" + numberFormat.format(globalTotal));

                    if (paymentMeth.matches("MPESA")) {
                        int feeSuj = mpesaFee(globalTotal);

                        fee.setText("" + feeSuj);
                        feeTotal = feeSuj;
                    }

                    payment.setSelection(0);
                    onResume();

                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!(deliveryET.getText().toString().matches(""))) {
                if (paymentMeth.matches("Mix")) {
                    Mix mix = mdb.getMix(1);

                    mix.setFlag("false");
                    mix.setCredit(0);
                    mix.setCash(0);
                    mix.setMpesa(0);
                    mix.setBank(0);
                    mix.setMpesaFee(0);
                    mix.setBankFee(0);

                    mdb.updateMix(mix);

                    List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

                    int sum = 0;
                    for (GoodsPurchased pd: purchases){
                        sum = sum + pd.getTotal();
                    }
                    globalTotal = sum;

                    total.setText("" + numberFormat.format(globalTotal));

                    if (paymentMeth.matches("MPESA")) {
                        int feeSuj = mpesaFee(globalTotal);

                        fee.setText("" + feeSuj);
                        feeTotal = feeSuj;
                    }

                    payment.setSelection(0);
                    onResume();

                }
            }
        }
    };

    /**
     * end of textWatcher
     **/

    /**
     * text watcher for textWatcher
     **/

    private final TextWatcher discountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!(discount.getText().toString().matches(""))) {
                if (paymentMeth.matches("Mix")) {
                    Mix mix = mdb.getMix(1);

                    mix.setFlag("false");
                    mix.setCredit(0);
                    mix.setCash(0);
                    mix.setMpesa(0);
                    mix.setBank(0);
                    mix.setMpesaFee(0);
                    mix.setBankFee(0);

                    mdb.updateMix(mix);

                    List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

                    int sum = 0;
                    for (GoodsPurchased pd: purchases){
                        sum = sum + pd.getTotal();
                    }
                    globalTotal = sum;

                    total.setText("" + numberFormat.format(globalTotal));
                    payment.setSelection(0);
                    onResume();

                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!(discount.getText().toString().matches(""))) {
                if (paymentMeth.matches("Mix")) {
                    Mix mix = mdb.getMix(1);

                    mix.setFlag("false");
                    mix.setCredit(0);
                    mix.setCash(0);
                    mix.setMpesa(0);
                    mix.setBank(0);
                    mix.setMpesaFee(0);
                    mix.setBankFee(0);

                    mdb.updateMix(mix);

                    List<GoodsPurchased> purchases = gpdb.getByForeignKey(pdb.getRowCount() + 1);

                    int sum = 0;
                    for (GoodsPurchased pd: purchases){
                        sum = sum + pd.getTotal();
                    }
                    globalTotal = sum;

                    total.setText("" + numberFormat.format(globalTotal));

                    if (paymentMeth.matches("MPESA")) {
                        int feeSuj = mpesaFee(globalTotal);

                        fee.setText("" + feeSuj);
                        feeTotal = feeSuj;
                    }

                    payment.setSelection(0);
                    onResume();

                }
            }
        }
    };

    /**
     * end of textWatcher
     **/

    /*
    setting month
     */

//    public Calendar setMonth(Calendar date, int month){
//
//        if(month == 1){
//            date.set(Calendar.MONTH, Calendar.JANUARY);
//        } else if(month == 2){
//            date.set(Calendar.MONTH, Calendar.FEBRUARY);
//        } else if(month == 3){
//            date.set(Calendar.MONTH, Calendar.MARCH);
//        } else if(month == 4){
//            date.set(Calendar.MONTH, Calendar.APRIL);
//        } else if(month == 5){
//            date.set(Calendar.MONTH, Calendar.MAY);
//        } else if(month == 6){
//            date.set(Calendar.MONTH, Calendar.JUNE);
//        } else if(month == 7){
//            date.set(Calendar.MONTH, Calendar.JULY);
//        } else if(month == 8){
//            date.set(Calendar.MONTH, Calendar.AUGUST);
//        } else if(month == 9){
//            date.set(Calendar.MONTH, Calendar.SEPTEMBER);
//        } else if(month == 10){
//            date.set(Calendar.MONTH, Calendar.OCTOBER);
//        } else if(month == 11){
//            date.set(Calendar.MONTH, Calendar.NOVEMBER);
//        } else if(month == 12){
//            date.set(Calendar.MONTH, Calendar.DECEMBER);
//        }
//
//        return date;
//    }

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

                if(paymentMeth.matches("MPESA")) {
                    fee.removeTextChangedListener(comma2);
                    int feeSuj = mpesaFee((globalTotal - discountTotal) + deliveryTotl);

                    fee.setText("" + feeSuj);
                    feeTotal = feeSuj;

                    fee.addTextChangedListener(comma2);

                }
            }
            discount.addTextChangedListener(commaSeparator);

            deliveryET.removeTextChangedListener(commaSeparator);

            if (!(deliveryET.getText()).toString().matches("")) {
                String cstArray[] = (deliveryET.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                deliveryTotl = Integer.parseInt(cst.toString());
                deliveryET.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));

                if(paymentMeth.matches("MPESA")) {
                    fee.removeTextChangedListener(comma2);
                    int feeSuj = mpesaFee((globalTotal - discountTotal) + deliveryTotl);

                    fee.setText("" + feeSuj);
                    feeTotal = feeSuj;

                    fee.addTextChangedListener(comma2);

                }
            }
            deliveryET.addTextChangedListener(commaSeparator);
        }

        @Override
        public void afterTextChanged(Editable s) {
            discount.setSelection(discount.getText().length());
            deliveryET.setSelection(deliveryET.getText().length());
        }
    };

    /**
     * end of text watcher
     **/

    /**
     * text watcher for getting total
     **/

    private final TextWatcher comma2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!fee.getText().toString().matches("")) {
                fee.removeTextChangedListener(comma2);

                String cstArray[] = (fee.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }

                feeTotal = Integer.parseInt(cst.toString());
                if (feeTotal > globalTotal){
                    fee.setError("Fee cannot be greater than Total");
                    feeTotal = 0;
                    fee.setText("");

                } else {
                    fee.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));

                }

                fee.addTextChangedListener(comma2);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!fee.getText().toString().matches("")) {
                fee.setSelection(fee.getText().length());
            }
        }
    };

    /**
     * end of textr watcher
     **/

    /**
     * text watcher for gmpesaChart
     **/

    private final TextWatcher mpesaChart = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            total.removeTextChangedListener(mpesaChart);
            if(paymentMeth.matches("MPESA")) {
                if (globalTotal <= 100){
                    fee.setText("" + 1);
                    feeTotal = 1;

                } else if (globalTotal <= 500){
                    fee.setText("" + 11);
                    feeTotal = 11;

                } else if (globalTotal <= 1000){
                    fee.setText("" + 15);
                    feeTotal = 15;

                } else if (globalTotal <= 1500){
                    fee.setText("" + 25);
                    feeTotal = 25;

                } else if (globalTotal <= 2500){
                    fee.setText("" + 20);
                    feeTotal = 20;

                } else if (globalTotal <= 3500){
                    fee.setText("" + 55);
                    feeTotal = 55;

                } else if (globalTotal <= 5000){
                    fee.setText("" + 60);
                    feeTotal = 60;

                } else if (globalTotal <= 7500){
                    fee.setText("" + 75);
                    feeTotal = 75;

                } else if (globalTotal <= 10000){
                    fee.setText("" + 85);
                    feeTotal = 85;

                } else if (globalTotal <= 15000){
                    fee.setText("" + 95);
                    feeTotal = 95;

                } else if (globalTotal <= 20000){
                    fee.setText("" + 100);
                    feeTotal = 100;

                } else {
                    fee.setText("" + 110);
                    feeTotal = 110;

                }
            }
            total.addTextChangedListener(mpesaChart);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!total.getText().toString().matches("")) {
                total.setSelection(total.getText().length());
            }
        }
    };

    /**
     * end of textr mpesaChart
     **/

    public int mpesaFee(int numb){
        int numbFee = 0;

        if (numb <= 100){
            numbFee = 1;

        } else if (numb <= 500){
            numbFee = 11;

        } else if (numb <= 1000){
            numbFee = 15;

        } else if (numb <= 1500){
            numbFee = 25;

        } else if (numb <= 2500){
            numbFee = 20;

        } else if (numb <= 3500){
            numbFee = 55;

        } else if (numb <= 5000){
            numbFee = 60;

        } else if (numb <= 7500){
            numbFee = 75;

        } else if (numb <= 10000){
            numbFee = 85;

        } else if (numb <= 15000){
            numbFee = 95;

        } else if (numb <= 20000){
            numbFee = 100;

        } else {
            numbFee = 110;

        }

        return numbFee;
    }

}
