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
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrder;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
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

public class PreOrderActivity extends Activity {
    public static String TAG = "PreOrderActivity";

    FourELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, String> idList = new HashMap<>();

    public int idGoods;
    private Calendar calendar;
    private TextView dateView, dueDate, dueDateText;
    private int year, month, day;

    LinearLayout layoutEL, layoutSV, paymentLL, feeLL;
    Button btnExit, btnAdd, btnDueDate, btnDate;
    EditText total, discount, fee;
    Spinner payment;
    TextInputLayout discountWidget;

    String paymentMeth = "";
    public int globalTotal = 0, discountTotal = 0, feeTotal = 0;
    public boolean creditFromMix = false, fromMix = true;

    public AutoCompleteTextView supplier, phone;

    GoodsPreOrderedHandler gpodb = new GoodsPreOrderedHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    PayablesHandler pdb = new PayablesHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    CashHandler cdb = new CashHandler(this);
    MixHandler mdb = new MixHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);

    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("#.###");
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);

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
        payment = (Spinner) findViewById(R.id.payment);
        dueDate = (TextView) findViewById(R.id.due_date);
        btnDueDate = (Button) findViewById(R.id.btn_due_date);
        btnDate = (Button) findViewById(R.id.btn_date);
        expListView = (ExpandableListView) findViewById(R.id.lvExp2);
        dateView = (TextView) findViewById(R.id.date);
        dueDateText = (TextView) findViewById(R.id.due_date_text);
        layoutEL = (LinearLayout) findViewById(R.id.linear_e_l);
        layoutSV = (LinearLayout) findViewById(R.id.linear_sc_v);
        paymentLL = (LinearLayout) findViewById(R.id.payment_ll);
        discountWidget = (TextInputLayout) findViewById(R.id.discount_widget);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

        discount.addTextChangedListener(commaSeparator);
        fee.addTextChangedListener(comma2);

        LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);

        if (getIntent().getStringExtra("FROM").matches("REG")){
            paymentLL.setLayoutParams(hiding);
            discount.setVisibility(View.INVISIBLE);
            discountWidget.setVisibility(View.INVISIBLE);

        }

        LinearLayout.LayoutParams hide = new LinearLayout.LayoutParams(0, 0, 0);
        LinearLayout.LayoutParams show = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 2);

        if(!(paymentMeth.matches("MPESA") || paymentMeth.matches("Bank Transfer"))){
            feeLL.setLayoutParams(hide);
            feeTotal = 0;

        } else {
            feeLL.setLayoutParams(show);

        }

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

                        if(!(paymentMeth.matches("MPESA") || paymentMeth.matches("Bank Transfer"))){
                            feeLL.setLayoutParams(hide);
                            feeTotal = 0;

                        } else {
                            feeLL.setLayoutParams(show);

                        }

                        Mix mix1 = mdb.getMix(1);

//                        if((paymentMeth.matches("Cash") || paymentMeth.matches("MPESA")
//                                || paymentMeth.matches("Bank Transfer") || paymentMeth.matches("Credit"))
//                                && (getIntent().getStringExtra("FROM").matches("MIX"))){
//                            total.setText("" + globalTotal);
//                            fromMix = true;
//                            creditFromMix = false;
//                            //globalTotal = 0;
//                        }

                        if(!(paymentMeth.matches("MPESA") || paymentMeth.matches("Bank Transfer"))){
                            feeLL.setLayoutParams(hide);
                            feeTotal = 0;

                        } else {
                            feeLL.setLayoutParams(show);

                        }

                        if(paymentMeth.matches("Mix")){

                            if((total.getText().toString()).matches("") || (globalTotal <= 0)){

                                Log.d("Values: ", "totals was null..");
                                Toast.makeText(getApplicationContext(), "No products selected",
                                        Toast.LENGTH_LONG).show();

                            } else {
                                Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                intent.putExtra("FROM", "PAY");
                                intent.putExtra("SUPPLIER", "" + supplier.getText().toString());
                                intent.putExtra("NUMBER", "" + phone.getText().toString());
                                intent.putExtra("DISCOUNT", "" + discount.getText().toString());
                                intent.putExtra("DATE", "" + dateView.getText().toString());
                                intent.putExtra("DUEDATE", "" + dueDate.getText().toString());
                                intent.putExtra("CREDIT", "NO");

//                                int discountTot = 0;
//                                if (!(discount.getText().toString().matches(""))){
//                                    discountTot = Integer.parseInt(discount.getText().toString());
////                                    globalTotal = globalTotal
////                                            - Integer.parseInt(discount.getText().toString());
//                                }

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

                            List<GoodsPreOrdered> purchases = gpodb.getByForeignKey((podb.getRowCount() + 1));

                            int sum = 0;
                            for (GoodsPreOrdered pd: purchases){
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
                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView arg0) {

                    }
                }
        );

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

            text.append("= (total)" + sum);

            globalTotal = sum;

            supplier.setText("" + getIntent().getStringExtra("SUPPLIER"));
            phone.setText("" + getIntent().getStringExtra("NUMBER"));
            discount.setText("" + getIntent().getStringExtra("DISCOUNT"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));
            dueDate.setText("" + getIntent().getStringExtra("DUEDATE"));

            total.setText(text);

        }

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

//            globalTotal = sum;

            total.setText(text);


        } else {
            List<GoodsPreOrdered> purchases = gpodb.getByForeignKey((podb.getRowCount() + 1));

            int sum = 0;
            for (GoodsPreOrdered pd: purchases){
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

        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDueDate(year, month+1, day);

        total.setEnabled(false);

        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);

        final List<String> sellersNames = new ArrayList<String>();
        final List<String> buyerPhone = new ArrayList<String>();

        List<Payable> payable = pdb.getAllPayable();
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

        List<PreOrder> purchase = podb.getAllPreOrder();
        for (PreOrder pd : purchase) {
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sellers);

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
                List<Cash> allcash = cdb.getAllCash();

                int cash = 0;
                int bank = 0;
                int mpesa  = 0;

                for (Cash pd : allcash) {
                    cash = pd.getActual();
                    bank = pd.getBank();
                    mpesa  = pd.getMpesa();
                }

//                int discountTotal = 0;
//
//                if(!(discount.getText().toString().matches(""))) {
//                    discountTotal = Integer.parseInt(discount.getText().toString());
//                }

                if(!fromMix && !(supplier.getText().toString().matches(""))) {
                    creditFromMix = false;
                }

                if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                    Log.d(TAG, "btnAdd; totals was null..");
                    prepareListData("Products");

//                    listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
//                            listDataChild, "PREO", true);

                    if ((getIntent().getStringExtra("FROM").matches("REG"))){
                        listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                                listDataChild, "PREOR", true);
                    } else {
                        listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                                listDataChild, "PREO", true);
                    }

                    // setting list adapter
                    expListView.setAdapter(listAdapter);
                    expListView.expandGroup(0);

                } else if(discountTotal >= (globalTotal)){
                    Log.d(TAG, "btnAdd; high discount..");

                    discount.setError("Discount Cannot Exceed Total Amount");
                    discount.requestFocus();

                } else if(supplier.getText().toString().matches("")){
                    Log.d(TAG, "btnAdd; no cutomer name..");

                    supplier.setError("Please Enter Supplier Name");
                    supplier.requestFocus();

                } else if(phone.getText().toString().matches("")){
                    Log.d(TAG, "btnAdd; no phone..");

                    phone.setError("Please Enter Phone Number");
                    phone.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("REG")){
                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("From " + supplier.getText().toString() + " for "
                            + (globalTotal) + " on " + dateView.getText().toString());

//                    if (discountTotal > 0){
//                        confMessaage.append("\nDiscount is " + discountTotal);
//                    }

                    confMessaage.append("\n\nGoods Pre-Purchased;");

                    StringBuilder titleBuild = new StringBuilder();

                    String Title = titleBuild.toString();
                    confMessaage.append("" + Title);

                    List<GoodsPreOrdered> goodsPurchased2 = gpodb.getByForeignKey(podb.getRowCount() + 1);

                    for (GoodsPreOrdered pd : goodsPurchased2) {
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

                        String displ = displBuilder.toString();
                        confMessaage.append("" + displ);
                    }

                    new AlertDialog.Builder(PreOrderActivity.this)
                            .setIcon(R.drawable.purchase_icon_2)
                            .setTitle("Confirm Pre-Purchase")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Inserting Contacts
                                    StringBuilder nameBuild = new StringBuilder();
                                    nameBuild.append(supplier.getText().toString());

                                    if(!(phone.getText().toString().matches(""))){
                                        nameBuild.append("-" + phone.getText().toString());
                                    }

                                    podb.addPreOrder(new PreOrder(nameBuild.toString(), (globalTotal),
                                            " ", dueDate.getText().toString(),
                                            dateView.getText().toString(), "OPEN"));

                                    List<GoodsPreOrdered> goodsPurchased = gpodb.getByForeignKey(podb.getRowCount());

                                    //updating date for purchases
                                    for (GoodsPreOrdered pd : goodsPurchased) {
                                        pd.setDate(dateView.getText().toString());
                                        gpodb.updateGoodsPreOrdered(pd);

                                    }

                                    finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                } else if(paymentMeth.matches("--Select Payment Method--") && fromMix){
                    Log.d(TAG, "btnAdd; no pay meth selected..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Non Selected");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("--Select Payment Method--");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(paymentMeth.matches("Cash") && ((globalTotal -  discountTotal) > cash)){
                    Log.d(TAG, "btnAdd; actual too little..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough Cash available");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Cash");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(paymentMeth.matches("MPESA") && ((globalTotal -  discountTotal) > mpesa)){
                    Log.d(TAG, "btnAdd; mpesa too little..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough money in MPESA");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(paymentMeth.matches("Bank Transfer") && ((globalTotal -  discountTotal) > bank)){
                    Log.d(TAG, "btnAdd; bank too little..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough money in Bank A/C");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else {
//                    globalTotal = globalTotal - discountTotal;

                    if (!fromMix) {
                        Mix mix = mdb.getMix(1);

                        if (mix.getCash() > 0 && (mix.getCash() > cash)) {
                            Log.d(TAG, "btnAdd; actual too little from mix..");

                            TextView errorText = (TextView) payment.getSelectedView();
                            errorText.setError("Not Enough Cash");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Not Enough Cash");//changes the selected item text to this
                            payment.requestFocus();
                            payment.performClick();

                        } else if (mix.getMpesa() > 0 && (mix.getMpesa() > mpesa)) {
                            Log.d(TAG, "btnAdd; mpesa too little from mix..");

                            TextView errorText = (TextView) payment.getSelectedView();
                            errorText.setError("Not Enough money in MPESA");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                            payment.requestFocus();
                            payment.performClick();

                        } else if (mix.getBank() > 0 && (mix.getBank() > bank)) {
                            Log.d(TAG, "btnAdd; bank too little from mix..");

                            TextView errorText = (TextView) payment.getSelectedView();
                            errorText.setError("Not Enough money in Bank");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                            payment.requestFocus();
                            payment.performClick();

                        } else if ((mix.getBank() + mix.getMpesa() + mix.getCash()) > (globalTotal -  discountTotal)) {
                            Log.d(TAG, "btnAdd; total was too low..");
                            Log.d(TAG, "MPESA " + Integer.parseInt(getIntent().getStringExtra("MPESA")));
                            Log.d(TAG, "CASH " + Integer.parseInt(getIntent().getStringExtra("CASH")));
                            Log.d(TAG, "BANK " + Integer.parseInt(getIntent().getStringExtra("BANK")));
                            Log.d(TAG, "globalTotal " + (globalTotal -  discountTotal));

                            TextView errorText = (TextView) payment.getSelectedView();
                            errorText.setError("Not Enough Money");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Not Enough Money");//changes the selected item text to this
                            payment.requestFocus();
                            payment.performClick();

                        } else {
                            StringBuilder confMessaage = new StringBuilder();

                            confMessaage.append("From " + supplier.getText().toString() + " for "
                                    + (globalTotal -  discountTotal) + " on " + dateView.getText().toString());

                            confMessaage.append("\nMode of payment; ");

                            if(!fromMix){
//                                Mix mix = mdb.getMix(1);

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

                            confMessaage.append("\n\nGoods Pre-Purchased;");

                            StringBuilder titleBuild = new StringBuilder();

                            String Title = titleBuild.toString();
                            confMessaage.append("" + Title);

                            List<GoodsPreOrdered> goodsPurchased2 = gpodb.getByForeignKey(podb.getRowCount() + 1);

                            for (GoodsPreOrdered pd : goodsPurchased2) {
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

                                String displ = displBuilder.toString();
                                confMessaage.append("" + displ);
                            }

                            new AlertDialog.Builder(PreOrderActivity.this)
                                    .setIcon(R.drawable.purchase_icon_2)
                                    .setTitle("Confirm Pre-Purchase")
                                    .setMessage("" + confMessaage.toString())
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            int discountTotal2 = 0;
//
//                                            if(!(discount.getText().toString().matches(""))) {
//                                                discountTotal2 = Integer.parseInt(discount.getText().toString());
//                                            }

                                            StringBuilder pay = new StringBuilder();

                                            Mix mix = mdb.getMix(1);

                                            if (mix.getCash() > 0) {
                                                cdb.updateSingleCashPurchase("Cash",mix.getCash());
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

                                            //Inserting Contacts
                                            StringBuilder nameBuild = new StringBuilder();
                                            nameBuild.append(supplier.getText().toString());

                                            if(!(phone.getText().toString().matches(""))){
                                                nameBuild.append("-" + phone.getText().toString());
                                            }

                                            podb.addPreOrder(new PreOrder(nameBuild.toString(), (globalTotal -  discountTotal),
                                                    pay.toString(), dueDate.getText().toString(),
                                                    dateView.getText().toString(), "OPEN"));

                                            List<GoodsPreOrdered> goodsPurchased = gpodb.getByForeignKey(podb.getRowCount());

                                            //updating date for purchases
                                            for (GoodsPreOrdered pd : goodsPurchased) {
                                                pd.setDate(dateView.getText().toString());

                                                if (discountTotal > 0) {
                                                    double tot = pd.getTotal(), gtot = globalTotal;
                                                    double goodDiscount = (tot / gtot) * (double) discountTotal;

                                                    pd.setTotal(Math.round(pd.getTotal() - (int) (goodDiscount)));

                                                }

                                                gpodb.updateGoodsPreOrdered(pd);
                                            }

//                                            Mix mix = mdb.getMix(1);

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
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    } else {
//                        int discountTotal2 = 0;
                        Mix mix = mdb.getMix(1);

                        if(!(discount.getText().toString().matches(""))) {
                            discountTotal = Integer.parseInt(discount.getText().toString());
                        }
                        StringBuilder confMessaage = new StringBuilder();

                        confMessaage.append("From " + supplier.getText().toString() + " for "
                                + (globalTotal -  discountTotal) + " on " + dateView.getText().toString());

                        confMessaage.append("\nMode of payment; ");

                        if(!fromMix){
                            if(mix.getCash() > 0){
                                confMessaage.append("Cash ");
                            }

                            if(mix.getBank() > 0){
                                confMessaage.append("Bank Transfer ");
                            }

                            if (mix.getMpesa() > 0){
                                confMessaage.append("MPESA ");
                            }

                        } else {
                            confMessaage.append("" + paymentMeth);
                        }

                        confMessaage.append("\n\nGoods Pre-Purchased;");

                        StringBuilder titleBuild = new StringBuilder();

                        titleBuild.append("\nName, Number, Price, Total");

                        String Title = titleBuild.toString();
                        confMessaage.append("" + Title);

                        List<GoodsPreOrdered> goodsPurchased2 = gpodb.getByForeignKey(podb.getRowCount() + 1);

                        for (GoodsPreOrdered pd : goodsPurchased2) {
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

                            StringBuilder displBuilder = new StringBuilder();

                            displBuilder.append("\n" + name + ", " + decimalFormat2.format(numbe) + " " + units + ", "
                                    + decimalFormat.format(cost) + " Per " + unitsCost + ", " + numberFormat.format(pd.getTotal()));

                            String displ = displBuilder.toString();
                            confMessaage.append("" + displ);
                        }

                        new AlertDialog.Builder(PreOrderActivity.this)
                                .setIcon(R.drawable.purchase_icon_2)
                                .setTitle("Confirm Pre-Purchase")
                                .setMessage("" + confMessaage.toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                        int discountTotal3 = 0;

//                                        if(!(discount.getText().toString().matches(""))) {
//                                            discountTotal3 = Integer.parseInt(discount.getText().toString());
//                                        }

                                    cdb.updateSingleCashPurchase(paymentMeth, (globalTotal -  discountTotal));
                                    //Inserting Contacts
                                    StringBuilder nameBuild = new StringBuilder();
                                    nameBuild.append(supplier.getText().toString());

                                    if (!(phone.getText().toString().matches(""))) {
                                        nameBuild.append("-" + phone.getText().toString());
                                    }

                                        List<GoodsPreOrdered> goodsPurchased = gpodb.getByForeignKey(podb.getRowCount());

                                        //updating date for purchases
                                        for (GoodsPreOrdered pd : goodsPurchased) {
                                            pd.setDate(dateView.getText().toString());

                                            if (discountTotal > 0) {
                                                double tot = pd.getTotal(), gtot = globalTotal;
                                                double goodDiscount = (tot / gtot) * (double) discountTotal;

                                                pd.setTotal((int) (Math.round(( (double) pd.getTotal()) - goodDiscount)));

                                                pd.setCost((((double) pd.getTotal()) - goodDiscount)
                                                        / (double) (pd.getNumber()));

                                            }

                                            gpodb.updateGoodsPreOrdered(pd);
                                        }

                                    podb.addPreOrder(new PreOrder(nameBuild.toString(), (globalTotal -  discountTotal),
                                            paymentMeth, dueDate.getText().toString(), dateView.getText().toString(), "OPEN"));


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
                new AlertDialog.Builder(PreOrderActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Exit")
                        .setMessage("This will remove all entries")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            gpodb.deleteByForeignKey(podb.getRowCount() + 1);
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

//        listAdapter = new FourELAdapter(this, listDataHeader,
//                listDataChild, "PREO", true);

        if ((getIntent().getStringExtra("FROM").matches("REG"))){
            listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                    listDataChild, "PREOR", true);
        } else {
            listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                    listDataChild, "PREO", true);
        }

        final LinearLayout.LayoutParams show2 =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

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
                prepareListData("Hide");

//                listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
//                        listDataChild, "PREO", true);

                if ((getIntent().getStringExtra("FROM").matches("REG"))){
                    listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PREOR", true);
                } else {
                    listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PREO", true);
                }

                // setting list adapter
                //expListView.setAdapter(listAdapter);

                layoutSV.setLayoutParams(show2);
                layoutEL.setLayoutParams(show2);

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                prepareListData("Show");

//                listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
//                        listDataChild, "PREO", true);

                if ((getIntent().getStringExtra("FROM").matches("REG"))){
                    listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PREOR", true);
                } else {
                    listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                            listDataChild, "PREO", true);
                }

                // setting list adapter
                //expListView.setAdapter(listAdapter);

                layoutSV.setLayoutParams(show2);
                layoutEL.setLayoutParams(show2);

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
                        GoodsPreOrdered goodsSales = gpodb.getGoodsPreOrdered(Integer.parseInt(idList.get(Key)));
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
                            numbe = (double) numbe / 1000.0;
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

                        new AlertDialog.Builder(PreOrderActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Deletion")
                                .setMessage("Delete; " + name + " " + decimalFormat2.format(numbe) + units + " " + decimalFormat.format(cost)
                                        + " Per " + unitsCost + " " + numberFormat.format(goodsSales.getTotal()))
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GoodsPreOrdered goodsSales2 = gpodb.getGoodsPreOrdered(idGoods);
                                        gpodb.deleteGoodsPreOrdered(goodsSales2);

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

//        listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
//                listDataChild, "PREO", true);

        if ((getIntent().getStringExtra("FROM").matches("REG"))){
            listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                    listDataChild, "PREOR", true);
        } else {
            listAdapter = new FourELAdapter(getApplicationContext(), listDataHeader,
                    listDataChild, "PREO", true);
        }

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);

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

//            globalTotal = sum;

            total.setText(text);


        } else {
            List<GoodsPreOrdered> purchases = gpodb.getByForeignKey((podb.getRowCount() + 1));

            int sum = 0;
            for (GoodsPreOrdered pd: purchases){
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

        }
    }

    /**
     * the beelow methodes aree used to manage the date field
     * **/
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    public void setDueDate(View view) { showDialog(998); }

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
        List<String> purchase = new ArrayList<>();

        List<GoodsPreOrdered> purchases = gpodb.getByForeignKey((podb.getRowCount() + 1));

        String title = "Name-Number-Price-Total";
        //purchase.add(Title);
        int count = 0;
        int tot = 0;

        for (GoodsPreOrdered pd : purchases) {
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
                    + decimalFormat.format(cost) + " Per " + unitsCost + "-" + numberFormat.format(pd.getTotal());
            // Writing Contacts to log
            purchase.add(displ);
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
        if(what.matches("Products")){
            groupName = "Must Add Product.";
        } else if(what.matches("Total")){
            groupName = "The Total Cannot be 0";
        } else if(what.matches("Show")){
            groupName = "Show Items Being Pre Purchased; " + count;
        } else {
            groupName = "Hide/Show Items Being Pre Purchased; " + count;
        }
        listDataHeader.add(groupName + "-" + title);

        listDataChild.put(listDataHeader.get(0), purchase); // Header, Child data

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


                if(paymentMeth.matches("MPESA")) {
                    fee.removeTextChangedListener(comma2);
                    int feeSuj = mpesaFee(globalTotal - discountTotal);

                    fee.setText("" + feeSuj);
                    feeTotal = feeSuj;

                    fee.addTextChangedListener(comma2);

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
