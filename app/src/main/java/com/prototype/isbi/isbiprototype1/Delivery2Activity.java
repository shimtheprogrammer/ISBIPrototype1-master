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
import com.prototype.isbi.isbiprototype1.databaseHandlers.Delivery;
import com.prototype.isbi.isbiprototype1.databaseHandlers.DeliveryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
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
 * Created by MRuto on 1/23/2017.
 */

public class Delivery2Activity extends Activity{
    public static String TAG = "Delivery2Activity";

    FourELAdapter listAdapter, listAdapter2;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Spinner purchases, payment, allocation;

    private Calendar calendar;
    private TextView dateView, dueDate, dueDateText;
    private int year, month, day;

    Button submit, back, btnDueDate, btnDate;
    EditText total, transporter, fee;
    LinearLayout layoutSV, layoutEL, feeLL;

    HashMap<String , String> idList = new HashMap<>();
    List<String> selected = new ArrayList<>();

    List<String> purchase = new ArrayList<>();

    GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);
    CashHandler cdb = new CashHandler(this);
    PurchaseHandler pdb = new PurchaseHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    PayablesHandler paydb = new PayablesHandler(this);
    DeliveryHandler ddb = new DeliveryHandler(this);
    MixHandler mdb = new MixHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("#.###");

    String paymentMethode = "", allocationType = "";
    public int globalTotal = 0, feeTotal = 0;
    public boolean creditFromMix = false, atLeastOne = true, fromMix = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery2);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        total = (EditText) findViewById(R.id.total);
        transporter = (EditText) findViewById(R.id.transporter);
        dueDate = (TextView) findViewById(R.id.due_date);
        btnDueDate = (Button) findViewById(R.id.btn_due_date);
        btnDate = (Button) findViewById(R.id.btn_date);
        //expListView = (ExpandableListView) findViewById(R.id.lvExp2);
        dateView = (TextView) findViewById(R.id.date);
        dueDateText = (TextView) findViewById(R.id.due_date_text);
        layoutEL = (LinearLayout) findViewById(R.id.layout_e_l);
        layoutSV = (LinearLayout) findViewById(R.id.layout_s_v);
        //setting text watcher
        total.addTextChangedListener(totalWatcher);
        total.addTextChangedListener(commaSeparator);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

        fee.addTextChangedListener(comma2);

        final LinearLayout.LayoutParams showing =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams hiding = new LinearLayout.LayoutParams(0, 0, 0);

        feeLL.setLayoutParams(hiding);

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

        /*
        Handling deliver new
         */

        if(getIntent().getStringExtra("FROM").matches("PUR")){
            selected.add("" + getIntent().getStringExtra("ID"));
        }
        /*
        end of deliver now
         */

        /*
        Button setting
         */

        submit = (Button) findViewById(R.id.btn_submit);
        back = (Button) findViewById(R.id.btn_back);

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        submit.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        submit.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Cash> allcash = cdb.getAllCash();

                Mix mix1 = mdb.getMix(1);

                int cash = 0;
                int bank = 0;
                int mpesa  = 0;

                for (Cash pd : allcash) {
                    cash = pd.getActual();
                    bank = pd.getBank();
                    mpesa  = pd.getMpesa();
                }

                if(!fromMix && !(transporter.getText().toString().matches(""))) {
                    creditFromMix = false;
                }

                if((total.getText().toString()).matches("") || (globalTotal <= 0)){

                    Log.d("Values: ", "totals was null..");
                    total.setError("Please Enter Cost of Delivery");
                    total.requestFocus();

                } else if(paymentMethode.matches("--Select Payment Method--") && fromMix){

                    Log.d("Values: ", "no pay meth selected..");
                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("--Select Allocation Type--");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("--Select Allocation Type--");//changes the selected item text to this
                    payment.requestFocus();

                } else if(allocationType.matches("--Select Allocation Type--")){

                    Log.d("Values: ", "no allo type selected..");
                    TextView errorText = (TextView)allocation.getSelectedView();
                    errorText.setError("--Select Allocation Type--");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("--Select Allocation Type--");//changes the selected item text to this
                    allocation.requestFocus();

                } else if(paymentMethode.matches("Credit") && transporter.getText().toString().matches("")){

                    Log.d("Values: ", "no Driver/Company on credit..");
                    transporter.setError("Please Enter Name of Creditor");
                    transporter.requestFocus();

                } else if(creditFromMix){

                    Log.d("Values: ", "no Driver on credit..");
                    transporter.setError("Please Enter Name of Creditor");
                    transporter.requestFocus();

                } else if(paymentMethode.matches("Cash") && (globalTotal > cash)){
                    Log.d("Values: ", "actual too little..");
                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough Cash available");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Cash");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(paymentMethode.matches("MPESA") && (globalTotal > mpesa)){
                    Log.d("Values: ", "mpesa too little..");
                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough money in MPESA");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if(paymentMethode.matches("Bank Transfer") && (globalTotal > bank)){
                    Log.d("Values: ", "Bank Account too little..");
                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Not Enough money in Bank A/C");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if (!fromMix && ((mix1.getCash() > 0) && (mix1.getCash() > cash))) {
                    Log.d(TAG, "btnAdd; actual too little from mix..");

                    TextView errorText = (TextView) payment.getSelectedView();
                    errorText.setError("Not Enough Cash");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Cash");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if (!fromMix && ((mix1.getMpesa() > 0) && ((mix1.getMpesa()
                        + mix1.getMpesaFee()) > mpesa))) {
                    Log.d(TAG, "btnAdd; mpesa too little from mix..");

                    TextView errorText = (TextView) payment.getSelectedView();
                    errorText.setError("Not Enough money in MPESA");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else if (!fromMix && ((mix1.getBank() > 0) && ((mix1.getBank()
                        + mix1.getBankFee()) > bank))) {
                    Log.d(TAG, "btnAdd; bank too little from mix..");

                    TextView errorText = (TextView) payment.getSelectedView();
                    errorText.setError("Not Enough money in Bank");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else {
                    Mix globalMix = mdb.getMix(1);

                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("Delivery for " + total.getText().toString() );

                    if (!(transporter.getText().toString().matches(""))) {
                        confMessaage.append(" by " + transporter.getText().toString());
                    }

                    confMessaage.append(" Allocation by " + allocationType + " Payment via " + paymentMethode
                            + " on date " + dateView.getText().toString());

                    if (paymentMethode.matches("Credit") || globalMix.getCredit() > 0){
                        confMessaage.append("\nCredit to be paid on Date " + dueDateText.getText().toString());
                    }

                    new AlertDialog.Builder(Delivery2Activity.this)
                            .setIcon(R.drawable.delivery_icon_2)
                            .setTitle("Confirm Delivery")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    List<Cash> allcash = cdb.getAllCash();
//
//                                    int cash1 = 0;
//                                    int bank1 = 0;
//                                    int mpesa1  = 0;
//
//                                    for (Cash pd : allcash) {
//                                        cash1 = pd.getActual();
//                                        bank1 = pd.getBank();
//                                        mpesa1  = pd.getMpesa();
//                                    }

                                    Log.d("Values: ", "All values filled");

                                    if (paymentMethode.matches("Credit")) {
                                        paydb.addPayable(new Payable("Delivery By " + transporter.getText().toString(),
                                                globalTotal, dueDate.getText().toString(), dateView.getText().toString()));
                                    }

                                    StringBuilder idList2 = new StringBuilder();

                                    for (String Key : selected) {
                                        Log.d("selectes", "" + Key);// + " found " + selected.get(i));
                                        idList2.append(Key + "-");
                                    }

                                    //updating actual
                                    if (!fromMix) {
                                        Mix mix = mdb.getMix(1);

                                        StringBuilder pay = new StringBuilder();
                                        if (mix.getCash() > 0) {
                                            cdb.updateSingleCashPurchase("Cash", mix.getCash());
                                            pay.append("Cash ");
                                        }

                                        if (mix.getBank() > 0) {
                                            cdb.updateSingleCashPurchase("Bank Transfer", mix.getBank());
                                            pay.append("Bank Transfer ");
                                            if (mix.getBankFee() > 0) {
                                                edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMethode,
                                                        ("Delivery " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                                cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                            }
                                        }

                                        if (mix.getMpesa() > 0) {
                                            cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());
                                            pay.append("MPESA ");
                                            if (mix.getMpesaFee() > 0) {
                                                edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMethode,
                                                        ("Delivery " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                                cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                            }
                                        }

                                        if (mix.getCredit() > 0) {
                                            paydb.addPayable(new Payable(transporter.getText().toString(), mix.getCredit(),
                                                    dueDate.getText().toString(), dateView.getText().toString()));
                                            pay.append("Credit");
                                        }

                                        //Inserting row
                                        ddb.addDelivery(new Delivery(allocationType, globalTotal,
                                                pay.toString(), dateView.getText().toString(), idList2.toString()));

//                                            StringBuilder pay = new StringBuilder();
//                                            if (!(getIntent().getStringExtra("CASH").matches("0"))) {
//                                                cdb.updateSingleCashPurchase("Cash", Integer.parseInt(getIntent().getStringExtra("CASH")));
//                                                pay.append("Cash ");
//                                            }
//
//                                            if (!(getIntent().getStringExtra("BANK").matches("0"))) {
//                                                cdb.updateSingleCashPurchase("Bank Transfer", Integer.parseInt(getIntent().getStringExtra("BANK")));
//                                                pay.append("Bank Transfer ");
//                                            }
//
//                                            if (!(getIntent().getStringExtra("MPESA").matches("0"))) {
//                                                cdb.updateSingleCashPurchase("MPESA", Integer.parseInt(getIntent().getStringExtra("MPESA")));
//                                                pay.append("MPESA ");
//                                            }
//
//                                            if (!(getIntent().getStringExtra("CREDIT").matches("0"))) {
//                                                paydb.addPayable(new Payable(transporter.getText().toString(),
//                                                        Integer.parseInt(getIntent().getStringExtra("CREDIT")),
//                                                        dueDate.getText().toString(), dateView.getText().toString()));
//                                                pay.append("Credit");
//                                            }
//
//                                            //Inserting row
//                                            ddb.addDelivery(new Delivery(allocationType, globalTotal,
//                                                    pay.toString(), dateView.getText().toString(), idList2.toString()));
                                    } else {
                                        //update actual accounts
                                        cdb.updateSingleCashPurchase(paymentMethode, globalTotal);
                                        //Inserting row
                                        ddb.addDelivery(new Delivery(allocationType, globalTotal,
                                                paymentMethode, dateView.getText().toString(), idList2.toString()));
                                    }

                    /*
                    updating totals in inventory
                     */
                                    Log.d(TAG, "submitbnt, del " + allocationType);

                                    if (allocationType.matches("Volume")) {
                                        double numb = 0;
                                        for (String Key : selected) {
                                            List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                                            for (GoodsPurchased pd1 : allGoods) {
                                                if (pd1.getUnit().matches("Pcs")) {
                                                    Log.d(TAG, "submitbnt, Volume, Pcs " + pd1.getNumber());
                                                    numb = numb + pd1.getNumber();

                                                } else {
                                                    numb = numb + ((double) pd1.getNumber() / 1000.0);
                                                    Log.d(TAG, "submitbnt, Volume, else " + ((double) pd1.getNumber() / 1000.0));

                                                }
                                            }
                                        }

                                        for (String Key : selected) {
                                            List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                                            for (GoodsPurchased pd : allGoods) {
                                                double original = pd.getNumber();

                                                if (!(pd.getUnit().matches("Pcs"))) {
                                                    original = ((double) pd.getNumber() / 1000.0);
                                                }

                                                double _dcost = ((double) original / (double) numb) * globalTotal;
                                                //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                                                int dcost = (int) (Math.round(_dcost));

                                                Log.d(TAG, "submitbnt volume inventory " + pd.getProduct() + " " + dcost);
                                                idb.updateNumberInventoryDelivery(pd.getProduct(), dcost);
                                            }

                                        }

                                    } else if (allocationType.matches("Cost")) {
                                        double total = 0;
                                        for (String Key : selected) {
                                            Purchase purchase = pdb.getPurchase(Integer.parseInt(Key));

                                            total = total + purchase.getTotal();
                                        }

                                        for (String Key : selected) {

                                            List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                                            for (GoodsPurchased pd : allGoods) {
                                                double cost = pd.getTotal();
                                                double _dcost = ((double) cost / (double) total) * (double) globalTotal;
                                                //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                                                int dcost = (int) (Math.round(_dcost));

                                                Log.d(TAG, "submitbnt cost inventory " + pd.getProduct() + " " + dcost);
                                                idb.updateNumberInventoryDelivery(pd.getProduct(), dcost);
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

                                        if (paymentMethode.matches("Bank Transfer")) {
                                            feeName = "Bank A/C";
                                        }

                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMethode,
                                                ("Delivery " + feeName + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase(paymentMethode, feeTotal);
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
        });


        /*
        end of button settings
         */

        /**
         * setting up purchases spinner
         **/

        purchases = (Spinner) findViewById(R.id.purchases);

        populatePurchases();

        purchases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                Log.d("selected itemdo ", "" + position  + "; and ;" + item);

                parent.getItemAtPosition(position).toString();

                if(!(item.matches("--Select an item--"))) {
                    atLeastOne = false;
                    for(String Key : idList.keySet()){
                        if(Key.matches("" + position)) {
                            Log.d("addingS; ", "" + Key + " is same " + position + " has " + idList.get(Key));
                            selected.add("" + (idList.get(Key)));
                        }
                    }

                    chack();
                    refresh();

                    populatePurchases();

                   // Log.d("removed ", "" + (String) purchases.getItemAtPosition(position));
                    purchases.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * end of spinner setup
         **/

        /**
         * spinner for payment
         **/

        payment = (Spinner) findViewById(R.id.payment);

        // Spinner Drop down elements
        final List<String> paymentMethod = new ArrayList<>();
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

        payment.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView parent, View view, int position, long id) {
                        // On selecting a spinner item
                        String item = parent.getItemAtPosition(position).toString();
                        paymentMethode = item;

                        feeTotal = 0;
                        fee.setText("");

                        Mix mix = mdb.getMix(1);

                        LinearLayout.LayoutParams hide = new LinearLayout.LayoutParams(0, 0, 0);
                        LinearLayout.LayoutParams show = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        if(paymentMethode.matches("Credit")){
                            dueDate.setVisibility(View.VISIBLE);
                            btnDueDate.setVisibility(View.VISIBLE);
                            dueDateText.setVisibility(View.VISIBLE);
                        } else if(!creditFromMix){
                            dueDate.setVisibility(View.INVISIBLE);
                            btnDueDate.setVisibility(View.INVISIBLE);
                            dueDateText.setVisibility(View.INVISIBLE);
                        }
                        if(!(paymentMethode.matches("Credit"))
                                && !(paymentMethode.matches("--Select Payment Method--"))){
                            dueDate.setVisibility(View.INVISIBLE);
                            btnDueDate.setVisibility(View.INVISIBLE);
                            dueDateText.setVisibility(View.INVISIBLE);
                        }
//
//                        if((paymentMethode.matches("Cash") || paymentMethode.matches("MPESA")
//                                || paymentMethode.matches("Bank Transfer") || paymentMethode.matches("Credit"))
//                                && (getIntent().getStringExtra("FROM").matches("MIX"))){
//                            total.setText("" + globalTotal);
//                            fromMix = true;
//                            creditFromMix = false;
//                            //globalTotal = 0;
//                        }

                        if((paymentMethode.matches("Cash") || paymentMethode.matches("MPESA")
                                || paymentMethode.matches("Bank Transfer") || paymentMethode.matches("Credit"))
                                && mix.getFlag().matches("true")){
                            total.setText("" + numberFormat.format(globalTotal));
                            fromMix = true;
                            creditFromMix = false;

                        }

                        if(!(paymentMethode.matches("MPESA") || paymentMethode.matches("Bank Transfer"))){
                            feeLL.setLayoutParams(hide);

                        } else {
                            feeLL.setLayoutParams(show);

                        }


//                        if(paymentMethode.matches("Mix")){
//                            Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);
//
//                            intent.putExtra("FROM", "DEL");
//                            intent.putExtra("ALLO", "" + allocationType);
//                            intent.putExtra("SELEC", "" + selected.size());
//
//                            int i = 0;
//                            for(String Key : selected){
//                                Log.d("sent to mix", "" + Key);// + " found " + selected.get(i));
//                                intent.putExtra("ID"+i, "" + Key);
//                                i++;
//                            }
//
//                            intent.putExtra("TOTAL", "" + globalTotal);
//
//                            startActivity(intent);
//
//                        }

                        if(paymentMethode.matches("Mix")){

                            if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                                Log.d(TAG, "totals was null..");
                                total.setError("No amount set");
                                total.requestFocus();
                                payment.setSelection(0);

                            } else {
                                Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                intent.putExtra("FROM", "PAY");
                                intent.putExtra("CREDIT", "YES");

                                intent.putExtra("TOTAL", "" + (globalTotal));

                                startActivity(intent);

                            }
                        } else {
                            mix.setFlag("false");
                            mix.setCredit(0);
                            mix.setCash(0);
                            mix.setMpesa(0);
                            mix.setBank(0);
                            mix.setMpesaFee(0);
                            mix.setBankFee(0);

                            mdb.updateMix(mix);

                            total.setText("" + numberFormat.format(globalTotal));

                            if (paymentMethode.matches("MPESA")) {
                                int feeSuj = mpesaFee(globalTotal);

                                fee.setText("" + feeSuj);
                                feeTotal = feeSuj;
                            }

                            fromMix = true;
                            creditFromMix = false;

                        }

                        // Showing selected spinner item
//                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                        refresh();
                    }

                    public void onNothingSelected(AdapterView arg0) {

                    }
                }
        );

        /**
         * end of spinner payment
         **/

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

                        refresh();
                    }

                    public void onNothingSelected(AdapterView arg0) {

                    }
                }
        );

        /**
         * end spinner allocsation
         **/

        /**
         * Handling data from mix
         **/

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

            if(!(getIntent().getStringExtra("CREDIT").matches("0"))){
                text.append(" (credit) " + getIntent().getStringExtra("CREDIT"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("CREDIT"));

                dueDate.setVisibility(View.VISIBLE);
                btnDueDate.setVisibility(View.VISIBLE);
                dueDateText.setVisibility(View.VISIBLE);

                creditFromMix = true;
            }

            text.append("= (total)" + sum);

            total.setText(text);

            globalTotal = sum;

            for(int i = 0; i < Integer.parseInt(getIntent().getStringExtra("SELEC")); i++){
                atLeastOne = true;
                selected.add(getIntent().getStringExtra("ID"+i));

                for(String Key : idList.keySet()){
                    if(idList.get(Key).matches(getIntent().getStringExtra("ID"+i))) {
                        //dataAdapter.remove((String) purchases.getItemAtPosition(Integer.parseInt(Key)));
                        Log.d("removed after mix; ", "" + Key + " attacched to " + idList.get(Key));

                    }
                }
            }

            if(getIntent().getStringExtra("ALLO").matches("Cost")){
                allocation.setSelection(1);
            }

            populatePurchases();
            chack();
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

            globalTotal = sum;

            total.removeTextChangedListener(commaSeparator);

            total.setText(text);

            total.addTextChangedListener(commaSeparator);

//            for(int i = 0; i < Integer.parseInt(getIntent().getStringExtra("SELEC")); i++){
//                atLeastOne = true;
//                selected.add(getIntent().getStringExtra("ID"+i));
//
//                for(String Key : idList.keySet()){
//                    if(idList.get(Key).matches(getIntent().getStringExtra("ID"+i))) {
//                        //dataAdapter.remove((String) purchases.getItemAtPosition(Integer.parseInt(Key)));
//                        Log.d("removed after mix; ", "" + Key + " attacched to " + idList.get(Key));
//
//                    }
//                }
//            }
//
//            if(getIntent().getStringExtra("ALLO").matches("Cost")){
//                allocation.setSelection(1);
//            }
//
//            populatePurchases();
//            chack();

        } else {

            if (globalTotal > 0) {
                total.setText("" + numberFormat.format(globalTotal));
            }

            if (paymentMethode.matches("MPESA")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            fromMix = true;
            creditFromMix = false;

            if(paymentMethode.matches("Mix")) {
                payment.setSelection(0);
            }

            if(!paymentMethode.matches("Credit")) {
                dueDate.setVisibility(View.INVISIBLE);
                btnDueDate.setVisibility(View.INVISIBLE);
                dueDateText.setVisibility(View.INVISIBLE);

            }

        }

        /**
         * end of mix data
         **/

        /**
         * initialise both dates
         **/

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDueDate(year, month+1, day);

        /**
         * end date
         **/

        for(String Key : idList.keySet()){
            Log.d("idlist; ", "" + Key + " attacched to " +  idList.get(Key));
        }


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        /**
         * bellow hanbed the expandable lists
         **/
        // preparing list data
        prepareListData();

        listAdapter = new FourELAdapter(this, listDataHeader,
                listDataChild, "DEL", false);

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
                layoutSV.setLayoutParams(show);
                layoutEL.setLayoutParams(show);
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                layoutSV.setLayoutParams(show);
                layoutEL.setLayoutParams(show);
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
        /**
         * end of ecpandable implementing
         **/

    }

    @Override
    protected void onResume() {
        super.onResume();

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

            globalTotal = sum;

            total.removeTextChangedListener(commaSeparator);

            total.setText(text);

            total.addTextChangedListener(commaSeparator);

        } else {
            total.setText("" + numberFormat.format(globalTotal));

            fromMix = true;
            creditFromMix = false;

            if(paymentMethode.matches("Mix")) {
                payment.setSelection(0);
            }

            total.setText("" + numberFormat.format(globalTotal));

            if (paymentMethode.matches("MPESA")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            fromMix = true;
            creditFromMix = false;

            if(!paymentMethode.matches("Credit")) {
                dueDate.setVisibility(View.INVISIBLE);
                btnDueDate.setVisibility(View.INVISIBLE);
                dueDateText.setVisibility(View.INVISIBLE);

            }

        }
    }

    /**
     * populating purchases
     **/

    public void populatePurchases(){
        List<String> purchase = new ArrayList<>();

        List<Purchase> allPurchases = pdb.getAllPurchase();
        List<Delivery> allDelivery = ddb.getAllDelivery();

        purchase.add("--Select an item--");

        int count = 1;
        for (Purchase pd : allPurchases) {
            boolean flag = true;
            for (Delivery pd1 : allDelivery){
                String foreignKeyList = (pd1.getForeignKey());
                String foreignKeys[] = foreignKeyList.split("-");

                for(int i = 0; i < foreignKeys.length; i++) {
                    if (pd.getId() == Integer.parseInt(foreignKeys[i])) {
                        flag = false;
                    }
                }
            }
            if(flag) {
                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(pd.getId());

                boolean flag2 = true;
                for(String Key : selected){
                    if(Integer.parseInt(Key) == pd.getId()){
                        flag2 = false;
                        Log.d("Found similar: ", Key);
                    }
                }
                if(flag2) {
                    String name[] = pd.getSupplier().split("-");
                    String log = " \t\tFrom: " + name[0] + "\t\tFor: " + pd.getTotal() +
                            "\t\tDate of Purchase: " + pd.getDate() + "\t\tNumber of Products: " + allGoods.size();

                    Log.d("Insert: ", log);
                    purchase.add(log);

                    String getid = "" + pd.getId();
                    idList.put("" + count, "" + getid);
                    count++;
                }
            }
        }

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.activity_spinner_item, purchase);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        purchases.setAdapter(dataAdapter);

    }

    /**
     * populating purchases
     **/

    //chercking if seldected is set
    public void chack(){
        for(String Key : selected){
            Log.d("selectes", "" + Key);// + " found " + selected.get(i));
        }
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
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        } if (id == 998) {
            return new DatePickerDialog(this,
                    myDueDateListener, year, month, day);
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
    }

    private void showDueDate(int year, int month, int day) {
        dueDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    /**
     * end of date methodes
     **/

    /**
     * text watcher for getting total
     **/

    private final TextWatcher totalWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!((total.getText().toString()).matches(""))
                    && fromMix){
                //globalTotal = Integer.parseInt(total.getText().toString());
                if(globalTotal > 999999999){
                    Toast.makeText(getApplicationContext(), "Amount too high",
                            Toast.LENGTH_LONG).show();
                    total.setText("");
                }
            } else {
                if(fromMix) {
                    globalTotal = 0;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!((total.getText().toString()).matches(""))
                    && fromMix){
                //globalTotal = Integer.parseInt(total.getText().toString());
                refresh();
            }
        }
    };

    /**
     * end of textr watcher
     **/

    /**
     * refrech list data
     **/

    public void refresh(){
        prepareListData();

        listAdapter2 = new FourELAdapter(this, listDataHeader,
                listDataChild, "DEL", false);

        // setting list adapter
        expListView.setAdapter(listAdapter2);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
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
        /**
         * end of ecpandable implementing
         **/
    }
    /**
     * end of refrshment
     **/

    /**
     * populate expandable
     */

    // populatinf expandable list
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> purchases = new ArrayList<>();

        String title = "ID-Product-Volume-Price";

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
                        numb = numb + ((double) pd1.getNumber() / 1000.0);
                        Log.d(TAG, "prepareListData, Volume, else " + ((double) pd1.getNumber() / 1000.0));
                    }
                }

            }

            for(String Key : selected) {
                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                for (GoodsPurchased pd : allGoods) {
                    double original = pd.getNumber();

                    if(!(pd.getUnit().matches("Pcs"))){
                        original = ((double) pd.getNumber() / 1000.0);
                    }

                    double _dcost = ((double) original / (double) numb) * globalTotal;
                    count++;
                    //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                    int numberTot = pd.getNumber();
                    String unitsCost = pd.getUnit();

                    if(!pd.getUnit().matches("Pcs")) {
                        numberTot = (int) Math.round((double) numberTot / 1000.0);
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
                    total = total + pd1.getTotal();

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
            double numb = 0;
            for(String Key : selected){
                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                for (GoodsPurchased pd1 : allGoods) {

                    if(pd1.getUnit().matches("Pcs")){
                        numb = numb + pd1.getNumber();
                    } else {
                        numb = numb + ((double) pd1.getNumber() / 1000.0);
                    }
                }

            }

            for(String Key : selected) {
                List<GoodsPurchased> allGoods = gpdb.getByForeignKey(Integer.parseInt(Key));

                for (GoodsPurchased pd : allGoods) {
//                    double original = pd.getNumber();
//
//                    if(!(pd.getUnit().matches("Pcs"))){
//                        original = (pd.getNumber() / 1000);
//                    }

                    //double _dcost = (original / numb) * globalTotal;
                    count++;
                    //double dcost = Integer.parseInt(decimalFormat.format("" + _dcost));
                    int numberTot = pd.getNumber();
                    String unitsCost = pd.getUnit();

                    if(!pd.getUnit().matches("Pcs")) {
                        numberTot = (int) ((double) numberTot / 1000.0);
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

    /**
     * end of populating
     */

    /**
     * text watcher for formating cost
     **/

    private final TextWatcher commaSeparator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(paymentMethode.matches("Mix")) {
                total.removeTextChangedListener(commaSeparator);

                total.setText("" + numberFormat.format(globalTotal));
                payment.setSelection(0);

                total.addTextChangedListener(commaSeparator);

            }

            total.removeTextChangedListener(commaSeparator);

            if (!(total.getText()).toString().matches("")) {
                String cstArray[] = (total.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }

                Mix mix = mdb.getMix(1);

                if(mix.getFlag().matches("true")){
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

                    }

                    globalTotal = sum;

                } else {
                    globalTotal = Integer.parseInt(cst.toString());

                }

                if(paymentMethode.matches("MPESA")) {
                    fee.removeTextChangedListener(comma2);
                    int feeSuj = mpesaFee((globalTotal));

                    fee.setText("" + feeSuj);
                    feeTotal = feeSuj;

                    fee.addTextChangedListener(comma2);

                }

                total.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }

            total.addTextChangedListener(commaSeparator);

        }

        @Override
        public void afterTextChanged(Editable s) { total.setSelection(total.getText().length()); }
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

        if(numb <= 1){
            numbFee = 0;

        } else if (numb <= 100){
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
