package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Assets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 2/7/2017.
 */

public class AssetsActivity extends Activity {
    public static String TAG = "AssetsActivity";

    Spinner type, payment;

    private Calendar calendar;
    private TextView dateView, dueDate;
    private int year, month, day;

    Button submit, back, btnDate;
    EditText total, info, useful, scrap, supplier, fee;
    LinearLayout addInfo, ifCredit, dateLayout, feeLL;

    String paymentMethode = "", assetType = "";
    public int globalTotal = 0, feeTotal = 0, scrapTotal = 0;
    public boolean creditFromMix = false, fromMix = true;

    AssetsHandler adb = new AssetsHandler(this);
    CashHandler cdb = new CashHandler(this);
    PayablesHandler db1 = new PayablesHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    MixHandler mdb = new MixHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        total = (EditText) findViewById(R.id.total);
        info = (EditText) findViewById(R.id.info);
        btnDate = (Button) findViewById(R.id.btn_date);
        useful = (EditText) findViewById(R.id.useful);
        scrap = (EditText) findViewById(R.id.scrap);
        dateView = (TextView) findViewById(R.id.date);
        dueDate = (TextView) findViewById(R.id.due_date);
        addInfo = (LinearLayout) findViewById(R.id.addInfo);
        ifCredit = (LinearLayout) findViewById(R.id.ifCredit);
        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        supplier = (EditText) findViewById(R.id.supplier);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

        //setting text watcher
        total.addTextChangedListener(totalWatcher);
        total.addTextChangedListener(equipWatcher);
        fee.addTextChangedListener(comma2);
        scrap.addTextChangedListener(comma2);
        total.addTextChangedListener(commaSeparator);

        LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);
        LinearLayout.LayoutParams showing =
                new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        /*
        Button setting
         */

        submit = (Button) findViewById(R.id.btn_submit);
        back = (Button) findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mix mix = mdb.getMix(1);

                List<Cash> allcash = cdb.getAllCash();

                int cash = 0;
                int bank = 0;
                int mpesa  = 0;

                for (Cash pd : allcash) {
                    cash = pd.getActual();
                    bank = pd.getBank();
                    mpesa  = pd.getMpesa();
                }

                if(!fromMix && !(supplier.getText().toString().matches(""))) {
                    creditFromMix = false;
                }

                if (assetType.matches("Equipment greater than 9,999")){
                    assetType = ("Equipment");

                }
                if (assetType.matches("Other greater than 9,999")){
                    assetType = ("Other");

                }

                if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                    Log.d("Values: ", "totals was null..");
                    Toast.makeText(getApplicationContext(), "Amount not set",
                            Toast.LENGTH_LONG).show();
                } else if(assetType.matches("--Select Asset--")){
                    Log.d("Values: ", "no asset selected..");
                    Toast.makeText(getApplicationContext(), "Please Select an Asset",
                            Toast.LENGTH_LONG).show();
                } else if(info.getText().toString().matches("")){
                    Log.d("Values: ", "no added info..");
                    Toast.makeText(getApplicationContext(), "Please Enter Additional Information ",
                            Toast.LENGTH_LONG).show();
                } else if (getIntent().getStringExtra("FROM").matches("REG")){
                    if(((assetType.matches("Equipment") || assetType.matches("Other"))
                            && globalTotal < 10000)) {
                        Log.d("Values: ", "equipment too cheap..");
                        Toast.makeText(getApplicationContext(), "Only Equipment above KES 10,000 value",
                                Toast.LENGTH_LONG).show();

                    } else if((assetType.matches("Equipment") || assetType.matches("Vehicle")
                            || assetType.matches("Licenses") || assetType.matches("Other"))
                            && useful.getText().toString().matches("")){
                        Log.d("Values: ", "no useful life on needed..");
                        Toast.makeText(getApplicationContext(), "Please Enter Useful Life",
                                Toast.LENGTH_LONG).show();
                    } else if((assetType.matches("Equipment") || assetType.matches("Vehicle")
                            || assetType.matches("Licenses") || assetType.matches("Other"))
                            && (Integer.parseInt(useful.getText().toString()) <= 0)){
                        Log.d("Values: ", "useful lifelow..");
                        Toast.makeText(getApplicationContext(), "Useful Life Must be Greater than 0",
                                Toast.LENGTH_LONG).show();
                    } else if((assetType.matches("Equipment") || (assetType.matches("Vehicle"))
                            || (assetType.matches("Other"))) && scrap.getText().toString().matches("")){
                        Log.d("Values: ", "no scrap value on needed..");
                        Toast.makeText(getApplicationContext(), "Please Enter Scrap Value",
                                Toast.LENGTH_LONG).show();
                    } else {
                        StringBuilder confMessaage = new StringBuilder();

                        confMessaage.append("Asset Type " + assetType + "\nDescription; " + info.getText().toString()
                                +  "\nFor amount " + + (globalTotal));

                        if(!(useful.getText().toString().matches(""))){
                            confMessaage.append("\nUsefull Life " + useful.getText().toString() + " months");

                        }
                        if(!(scrap.getText().toString().matches(""))){
                            confMessaage.append("\nScrap Value " + numberFormat.format(scrapTotal));

                        }

                        confMessaage.append("\non " + dateView.getText().toString());

                        new AlertDialog.Builder(AssetsActivity.this)
                                .setIcon(R.drawable.assets_icon_3)
                                .setTitle("Confirm Asset at Registration")
                                .setMessage("" + confMessaage.toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int sendUseful = 0, sendScrap = 0;

                                        if(!(useful.getText().toString().matches(""))){
                                            sendUseful = Integer.parseInt(useful.getText().toString());

                                        }
                                        if(!(scrap.getText().toString().matches(""))){
                                            sendScrap = scrapTotal;

                                        }
                                        adb.addAssets(new Assets(assetType, info.getText().toString(),
                                                globalTotal, " ", dateView.getText().toString(),
                                                sendUseful, sendScrap));

                                        finish();

                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();

                    }
                } else if(paymentMethode.matches("--Select Payment Method--") && fromMix){
                    Log.d("Values: ", "no pay meth selected..");
                    Toast.makeText(getApplicationContext(), "Please Select a payment method",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("Cash") && (globalTotal > cash)){
                    Log.d("Values: ", "actual too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Cash Available",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("MPESA") && (globalTotal > mpesa)){
                    Log.d("Values: ", "mpesa too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough MPESA Available",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("Bank Transfer") && (globalTotal > bank)){
                    Log.d("Values: ", "Bank Account too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Money in Bank A/C Available",
                            Toast.LENGTH_LONG).show();
                } else if(((assetType.matches("Equipment") || assetType.matches("Other"))
                        && globalTotal < 10000)){
                    Log.d("Values: ", "equipment too cheap..");
                    Toast.makeText(getApplicationContext(), "Only Equipment above KES 10,000 value",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);

                    intent.putExtra("FROM", "ASS");
                    intent.putExtra("INFO", ""+ info.getText().toString());
                    intent.putExtra("DATE", ""+ dateView.getText().toString());
                    intent.putExtra("PAY", ""+ paymentMethode);
                    intent.putExtra("TOTAL", "" + globalTotal);

                    if(assetType.matches("Equipment")){
                        intent.putExtra("TYPE", "EQU");
                    } else if(assetType.matches("Other")){
                        intent.putExtra("TYPE", "OTH");
                    }

                    startActivity(intent);
                    finish();

                } else if((assetType.matches("Equipment") || assetType.matches("Vehicle")
                        || assetType.matches("Licenses") || assetType.matches("Other"))
                        && useful.getText().toString().matches("")){
                    Log.d("Values: ", "no useful life on needed..");
                    Toast.makeText(getApplicationContext(), "Please Enter Useful Life",
                            Toast.LENGTH_LONG).show();
                } else if((assetType.matches("Equipment") || assetType.matches("Vehicle")
                        || assetType.matches("Licenses") || assetType.matches("Other"))
                        && (Integer.parseInt(useful.getText().toString()) <= 0)){
                    Log.d("Values: ", "useful lifelow..");
                    Toast.makeText(getApplicationContext(), "Useful Life Must be Greater than 0",
                            Toast.LENGTH_LONG).show();
                } else if((assetType.matches("Equipment") || (assetType.matches("Vehicle"))
                        || (assetType.matches("Other"))) && scrap.getText().toString().matches("")){
                    Log.d("Values: ", "no scrap value on needed..");
                    Toast.makeText(getApplicationContext(), "Please Enter Scrap Value",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("Credit") && supplier.getText().toString().matches("")){
                    Log.d("Values: ", "no Supplier on credit..");
                    Toast.makeText(getApplicationContext(), "Please Enter Supplier Name",
                            Toast.LENGTH_LONG).show();
                } else if(creditFromMix){
                    Log.d("Values: ", "no Supplier on credit..");
                    Toast.makeText(getApplicationContext(), "Please Enter Supplier Name For Credit",
                            Toast.LENGTH_LONG).show();
                } else {
                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("Asset Type " + assetType + "\nDescription; " + info.getText().toString()
                            +  "\nFor amount " + + (globalTotal));

                    if(!(useful.getText().toString().matches(""))){
                        confMessaage.append("\nUsefull Life " + useful.getText().toString() + " months");

                    }
                    if(!(scrap.getText().toString().matches(""))){
                        confMessaage.append("\nScrap Value " + numberFormat.format(scrapTotal));

                    }

                    confMessaage.append("\non " + dateView.getText().toString());
                    confMessaage.append("\nMode of payment; ");

                    if(!fromMix){
                        if(mix.getCash() > 0){
                            confMessaage.append("Cash ");
                        }

                        if(mix.getBank() > 0){
                            confMessaage.append("Bank Transfer ");
                        }

                        if(mix.getMpesa() > 0){
                            confMessaage.append("MPESA ");
                        }

                    } else {
                        confMessaage.append("" + paymentMethode);
                    }

                    int transactionCost = 0;

                    if(!fromMix){
                        if(mix.getBankFee() > 0){
                            transactionCost = transactionCost + mix.getBankFee();
                        }

                        if(mix.getMpesa() > 0){
                            transactionCost = transactionCost + mix.getMpesaFee();
                        }

                    } else {
                        transactionCost = transactionCost + feeTotal;
                    }

                    if (transactionCost > 0) {
                        confMessaage.append("\nTransaction Cost; " + transactionCost);

                    }

                    new AlertDialog.Builder(AssetsActivity.this)
                            .setIcon(R.drawable.assets_icon_3)
                            .setTitle("Confirm Asset Purchase")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Mix mix = mdb.getMix(1);

                                    List<Cash> allcash = cdb.getAllCash();

                                    int cash = 0;
                                    int bank = 0;
                                    int mpesa  = 0;

                                    for (Cash pd : allcash) {
                                        cash = pd.getActual();
                                        bank = pd.getBank();
                                        mpesa  = pd.getMpesa();
                                    }

                                    if (!fromMix) {
                                        if (!fromMix && ((mix.getCash() > 0) && (mix.getCash() > cash))) {
                                            Log.d("Values: ", "actual too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough Cash Available",
                                                    Toast.LENGTH_LONG).show();
                                        } else if (!fromMix && ((mix.getMpesa() > 0) && ((mix.getMpesa()
                                                + mix.getMpesaFee()) > mpesa))) {
                                            Log.d("Values: ", "mpesa too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough MPESA Available",
                                                    Toast.LENGTH_LONG).show();
                                        } else if (!fromMix && ((mix.getBank() > 0) && ((mix.getBank()
                                                + mix.getBankFee()) > bank))) {
                                            Log.d("Values: ", "Bank Account too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough Money in Bank A/C Available",
                                                    Toast.LENGTH_LONG).show();
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
                                                    edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMethode,
                                                            ("Asset " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                                    cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                                }
                                            }

                                            if (mix.getMpesa() > 0) {
                                                cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());
                                                pay.append("MPESA ");
                                                if (mix.getMpesaFee() > 0) {
                                                    edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMethode,
                                                            ("Asset " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                                }
                                            }

                                            if (mix.getCredit() > 0) {
                                                db1.addPayable(new Payable(supplier.getText().toString(), mix.getCredit(),
                                                        dueDate.getText().toString(), dateView.getText().toString()));
                                                pay.append("Credit");
                                            }

                                            int sendUseful = 0, sendScrap = 0;

                                            if(!(useful.getText().toString().matches(""))){
                                                sendUseful = Integer.parseInt(useful.getText().toString());

                                            }
                                            if(!(scrap.getText().toString().matches(""))){
                                                sendScrap = scrapTotal;

                                            }
                                            adb.addAssets(new Assets(assetType, info.getText().toString(),
                                                    globalTotal, pay.toString(), dateView.getText().toString(),
                                                    sendUseful, sendScrap));

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

                                    } else {
                                        if(paymentMethode.matches("Credit")){
                                            db1.addPayable(new Payable(supplier.getText().toString(), (Integer.parseInt(total.getText().toString())),
                                                    dueDate.getText().toString(), dateView.getText().toString()));
                                        } else {
                                            cdb.updateSingleCashPurchase(paymentMethode, globalTotal);
                                        }

                                        int sendUseful = 0, sendScrap = 0;

                                        if(!(useful.getText().toString().matches(""))){
                                            sendUseful = Integer.parseInt(useful.getText().toString());
                                        }
                                        if(!(scrap.getText().toString().matches(""))){
                                            sendScrap = scrapTotal;
                                        }

                                        adb.addAssets(new Assets(assetType, info.getText().toString(),
                                                globalTotal, paymentMethode, dateView.getText().toString(),
                                                sendUseful, sendScrap));

                                        if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                                            String feeName = "MPESA";

                                            if (paymentMethode.matches("Bank Transfer")) {
                                                feeName = "Bank A/C";
                                            }

                                            edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMethode,
                                                    ("Asset " + feeName + " Transfer"), dateView.getText().toString()));
                                            cdb.updateSingleCashPurchase(paymentMethode, feeTotal);
                                        }

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
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
         * initialise dates
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

        /**
         * spinner for payment
         **/

        payment = (Spinner) findViewById(R.id.payment);

        // Spinner Drop down elements
        final List<String> paymentMethod = new ArrayList<String>();
        paymentMethod.add("--Select Payment Method--");
        paymentMethod.add("Cash");
        paymentMethod.add("Credit");
        paymentMethod.add("MPESA");
        paymentMethod.add("Mix");
        paymentMethod.add("Bank Transfer");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentMethod);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        payment.setAdapter(dataAdapter1);

        payment.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView parent, View view, int position, long id) {
                        // On selecting a spinner item
                        Mix mix = mdb.getMix(1);

                        String item = parent.getItemAtPosition(position).toString();
                        paymentMethode = item;

                        feeTotal = 0;
                        fee.setText("");

//                        if((paymentMethode.matches("Cash") || paymentMethode.matches("MPESA")
//                                || paymentMethode.matches("Bank Transfer"))
//                                && (getIntent().getStringExtra("FROM").matches("MIX"))){
//                            total.setText("" + globalTotal);
//                            fromMix = true;
//                            creditFromMix = false;
//                        }

                        LinearLayout.LayoutParams show =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                        LinearLayout.LayoutParams hide =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        0, 0);

                        if(paymentMethode.matches("Credit")){
                            ifCredit.setLayoutParams(show);
                        } else if(paymentMethode.matches("--Select Payment Method--")){

                        } else {
                            ifCredit.setLayoutParams(hide);
                        }

                        if(!(paymentMethode.matches("MPESA") || paymentMethode.matches("Bank Transfer"))){
                            feeLL.setLayoutParams(hide);
                            feeTotal = 0;

                        } else {
                            feeLL.setLayoutParams(show);

                        }

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

        type = (Spinner) findViewById(R.id.type);

        // Spinner Drop down elements
        final List<String> allocationMethod = new ArrayList<>();
        allocationMethod.add("--Select Asset--");
        allocationMethod.add("Land");
        allocationMethod.add("Building");
        allocationMethod.add("Vehicle");
        allocationMethod.add("Equipment");
//        allocationMethod.add("Equipment greater than 9,999");
        allocationMethod.add("Licenses");
        allocationMethod.add("Other");
//        allocationMethod.add("Other greater than 9,999");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allocationMethod);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        type.setAdapter(dataAdapter2);

        type.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView parent, View view, int position, long id) {
                        // On selecting a spinner item
                        String item = parent.getItemAtPosition(position).toString();
                        assetType = item;

                        if (assetType.matches("Equipment greater than 9,999")){
                            assetType = ("Equipment");

                        }
                        if (assetType.matches("Other greater than 9,999")){
                            assetType = ("Other");

                        }

                        LinearLayout.LayoutParams show =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                        LinearLayout.LayoutParams hide =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        0, 0f);

                        if(assetType.matches("Land")){
                            //info.setHint("Location");
                            addInfo.setLayoutParams(hide);
                            scrap.setEnabled(true);

                        } else if(assetType.matches("Building")){
                            //info.setHint("Name & Location");
                            addInfo.setLayoutParams(hide);
                            scrap.setEnabled(true);

                        } else if(assetType.matches("Vehicle")){
                            //info.setHint("Make & Model");
                            addInfo.setLayoutParams(show);
                            scrap.setEnabled(true);
                        } else if(assetType.matches("Equipment greater than 9,999")){
                            //info.setHint("Description");
                            addInfo.setLayoutParams(show);
                            scrap.setEnabled(true);

                        } else if(assetType.matches("Equipment")){
                            //info.setHint("Description");
                            addInfo.setLayoutParams(show);
                            scrap.setEnabled(true);
                        } else if(assetType.matches("Other greater than 9,999")){
                            //info.setHint("Description");
                            addInfo.setLayoutParams(show);
                            scrap.setEnabled(true);

                        } else if(assetType.matches("Other")){
                            //info.setHint("Description");
                            addInfo.setLayoutParams(show);
                            scrap.setEnabled(true);

                        } else if(assetType.matches("Licenses")){
                            //info.setHint("Description");
                            addInfo.setLayoutParams(show);
                            scrap.setEnabled(false);

                        } else {

                        }

                        // Showing selected spinner item
                        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView arg0) {

                    }
                }
        );

        /**
         * end spinner allocsation
         **/

        if (getIntent().getStringExtra("FROM").matches("REG")){
            payment.setLayoutParams(hiding);
            dateLayout.setLayoutParams(showing);
            feeLL.setLayoutParams(hiding);
        }

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

            if(!(getIntent().getStringExtra("MPESA").matches("0"))){
                text.append(" (mpesa) " + getIntent().getStringExtra("MPESA"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("MPESA"));
            }

            if(!(getIntent().getStringExtra("CREDIT").matches("0"))){
                text.append(" (credit) " + getIntent().getStringExtra("CREDIT"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("CREDIT"));
                creditFromMix = true;
            }

            if(!(getIntent().getStringExtra("BANK").matches("0"))){
                text.append(" (bank) " + getIntent().getStringExtra("BANK"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("BANK"));
            }

            LinearLayout.LayoutParams show =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams hide =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0f);

            if(!(getIntent().getStringExtra("CREDIT").matches("0"))){
                ifCredit.setLayoutParams(show);
            } else {
                ifCredit.setLayoutParams(hide);
            }

            text.append("= (total) " + sum);

            total.setText(text);

            globalTotal = sum;

            useful.setText("" + getIntent().getStringExtra("USEFULL"));
            scrap.setText("" + getIntent().getStringExtra("SCRAP"));
            info.setText("" + getIntent().getStringExtra("INFO"));
            supplier.setText("" + getIntent().getStringExtra("SUPPLIER"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));
            dueDate.setText("" + getIntent().getStringExtra("DUEDATE"));

            if(getIntent().getStringExtra("TYPE").matches("Land")){
                type.setSelection(1);
            } else if(getIntent().getStringExtra("TYPE").matches("Building")){
                type.setSelection(2);
            } else if(getIntent().getStringExtra("TYPE").matches("Vehicle")){
                type.setSelection(3);
            } else if(getIntent().getStringExtra("TYPE").matches("Equipment")){
                type.setSelection(4);
            } else if(getIntent().getStringExtra("TYPE").matches("Licenses")){
                type.setSelection(5);
            } else if(getIntent().getStringExtra("TYPE").matches("Other")){
                type.setSelection(6);
            }

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

            }

            LinearLayout.LayoutParams show =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams hide =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0f);

            if(mix.getCredit() > 0){
                sum = sum + mix.getCredit();

                ifCredit.setLayoutParams(show);

                creditFromMix = true;
            } else {
                ifCredit.setLayoutParams(hide);

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

            if (globalTotal > 0) {
                total.setText("" + numberFormat.format(globalTotal));
            }

            if (paymentMethode.matches("MPESA")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            fromMix = true;

            if(paymentMethode.matches("Mix")) {
                payment.setSelection(0);
            }
        }

        /**
         * end of mix data
         **/

        /**
         * Handling data from assets
         **/

        if(getIntent().getStringExtra("FROM").matches("EXP")){
            globalTotal = Integer.parseInt(getIntent().getStringExtra("TOTAL"));

            total.setText("" + getIntent().getStringExtra("TOTAL"));
            info.setText("" + getIntent().getStringExtra("INFO"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));
            type.setSelection(4);

            if(getIntent().getStringExtra("PAY").matches("Cash")){
                payment.setSelection(1);
            } else if(getIntent().getStringExtra("PAY").matches("MPESA")){
                payment.setSelection(3);
            } else if(getIntent().getStringExtra("PAY").matches("Bank Transfer")){
                payment.setSelection(5);
            } else {
                payment.setSelection(0);
                mix.setFlag("false");
                mix.setCredit(0);
                mix.setCash(0);
                mix.setMpesa(0);
                mix.setBank(0);
                mix.setMpesaFee(0);
                mix.setBankFee(0);

                mdb.updateMix(mix);
            }
        }

        /**
         * end of mix assets
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

            LinearLayout.LayoutParams show =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams hide =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0f);

            if(mix.getCredit() > 0){
                sum = sum + mix.getCredit();

                ifCredit.setLayoutParams(show);

                creditFromMix = true;
            } else {
                ifCredit.setLayoutParams(hide);

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

            if (globalTotal > 0) {
                total.setText("" + numberFormat.format(globalTotal));
            }

            if (paymentMethode.matches("MPESA")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            fromMix = true;

            if(paymentMethode.matches("Mix")) {
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

    public void setDueDate(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
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
//                globalTotal = Integer.parseInt(total.getText().toString());
                if(globalTotal > 999999999){
                    Toast.makeText(getApplicationContext(), "Amount too high",
                            Toast.LENGTH_LONG).show();
                    total.setText("");
                }
            }
//            else {
//                globalTotal = 0;
//            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!((total.getText().toString()).matches(""))
                    && fromMix){
//                globalTotal = Integer.parseInt(total.getText().toString());
                if(globalTotal > 999999999){
                    Toast.makeText(getApplicationContext(), "Amount too high",
                            Toast.LENGTH_LONG).show();
                    total.setText("");
                }
            }
//            else {
//                globalTotal = 0;
//            }
        }
    };

    /**
     * end of textr watcher
     **/

    /**
     * equip watcher
     **/

    private final TextWatcher equipWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(!((total.getText().toString()).matches(""))
                    && (assetType.matches("Equipment") || assetType.matches("Other"))){
//                int amount = Integer.parseInt(total.getText().toString());
                if(globalTotal < 10000){
                    scrap.setEnabled(false);
                    useful.setEnabled(false);

                } else {
                    scrap.setEnabled(true);
                    useful.setEnabled(true);

                }
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!((total.getText().toString()).matches(""))
                    && (assetType.matches("Equipment") || assetType.matches("Other"))){
//                int amount = Integer.parseInt(total.getText().toString());
                if(globalTotal < 10000){
                    scrap.setEnabled(false);
                    useful.setEnabled(false);

                } else {
                    scrap.setEnabled(true);
                    useful.setEnabled(true);

                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!((total.getText().toString()).matches(""))
                    && (assetType.matches("Equipment") || assetType.matches("Other"))){
//                int amount = Integer.parseInt(total.getText().toString());
                if(globalTotal < 10000){
                    scrap.setEnabled(false);
                    useful.setEnabled(false);

                } else {
                    scrap.setEnabled(true);
                    useful.setEnabled(true);

                }
            }
        }
    };

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

            if(!scrap.getText().toString().matches("")) {
                scrap.removeTextChangedListener(comma2);

                String cstArray[] = (fee.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }

                scrapTotal = Integer.parseInt(cst.toString());

                if (scrapTotal > globalTotal){
                    scrap.setError("Scrap value cannot be greater than Total");
                    scrapTotal = 0;
                    scrap.setText("");

                } else {
                    scrap.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));

                }

                scrap.addTextChangedListener(comma2);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!fee.getText().toString().matches("")) {
                fee.setSelection(fee.getText().length());

            }
            if(!scrap.getText().toString().matches("")) {
                scrap.setSelection(scrap.getText().length());

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
