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

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Owner;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GlobalVariables;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GlobalVariablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 1/31/2017.
 */

public class OwnerActivity extends Activity {
    public static String TAG = "OwnerActivity";

//    TextInputLayout infoDesign, periodDesign;
    Spinner type, payment;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    Button submit, back, btnDate;
    EditText total, info, fee;
    LinearLayout feeLL;

    String paymentMethode = "", ownerType = "";
    public int globalTotal = 0, feeTotal = 0;
    public boolean fromMix = true;

    OwnerHandler odb = new OwnerHandler(this);
    CashHandler cdb = new CashHandler(this);
    GlobalVariablesHandler gvdb = new GlobalVariablesHandler(this);
    PersonalDataHandler pddb = new PersonalDataHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    MixHandler mdb = new MixHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        total = (EditText) findViewById(R.id.total);
        info = (EditText) findViewById(R.id.info);
        btnDate = (Button) findViewById(R.id.btn_date);
        //expListView = (ExpandableListView) findViewById(R.id.lvExp2);
        dateView = (TextView) findViewById(R.id.date);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

//        infoDesign = (TextInputLayout) findViewById(R.id.info_design);
//        periodDesign = (TextInputLayout) findViewById(R.id.period_design);

        //setting text watcher
        total.addTextChangedListener(totalWatcher);
        fee.addTextChangedListener(comma2);
        total.addTextChangedListener(commaSeparator);

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

                if((total.getText().toString()).matches("") || (globalTotal <= 0)){

                    Log.d("Values: ", "totals was null..");
                    Toast.makeText(getApplicationContext(), "Amount not set",
                            Toast.LENGTH_LONG).show();

                } else if(paymentMethode.matches("--Select Payment Method--") && fromMix){

                    Log.d("Values: ", "no pay meth selected..");
                    Toast.makeText(getApplicationContext(), "Please Select a payment method",
                            Toast.LENGTH_LONG).show();

                } else if(paymentMethode.matches("Cash") && (globalTotal > cash)
                        && !ownerType.matches("Add Funds")){
                    Log.d("Values: ", "actual too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Cash Available",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("MPESA") && (globalTotal > mpesa)
                        && !ownerType.matches("Add Funds")){
                    Log.d("Values: ", "mpesa too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough MPESA Available",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("Bank Transfer") && (globalTotal > bank)
                        && !ownerType.matches("Add Funds")){
                    Log.d("Values: ", "Bank Account too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Money in Bank A/C Available",
                            Toast.LENGTH_LONG).show();
                } else {
                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("Transcaction Type " + ownerType + ", amount "
                            + (globalTotal) + " on " + dateView.getText().toString());

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

                    new AlertDialog.Builder(OwnerActivity.this)
                            .setIcon(R.drawable.owner_icon_2)
                            .setTitle("Confirm Owner Drawing")
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
                                        if ((mix.getCash() > 0) && (mix.getCash() > cash) && !ownerType.matches("Add Funds")) {
                                            Log.d("Values: ", "actual too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough Cash Available",
                                                    Toast.LENGTH_LONG).show();
                                        } else if (((mix.getMpesa() > 0) && ((mix.getMpesa() + mix.getMpesaFee()) > mpesa)
                                                && !ownerType.matches("Add Funds"))) {
                                            Log.d("Values: ", "mpesa too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough MPESA Available",
                                                    Toast.LENGTH_LONG).show();
                                        } else if ((mix.getBank() > 0) && ((mix.getBank() + mix.getBankFee()) > bank)
                                                && !ownerType.matches("Add Funds")) {
                                            Log.d("Values: ", "Bank Account too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough Money in Bank A/C Available",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            if (mix.getCash() > 0) {
                                                if (ownerType.matches("Add Funds")) {
                                                    setDrawing("ADD", globalTotal);
                                                    cdb.updateSingleCash("Cash", mix.getCash());

                                                } else {
                                                    setDrawing("SUB", globalTotal);
                                                    cdb.updateSingleCashPurchase("Cash", mix.getCash());

                                                }
                                            }

                                            if (mix.getBank() > 0) {
                                                if (ownerType.matches("Add Funds")) {
                                                    setDrawing("ADD", globalTotal);
                                                    cdb.updateSingleCash("Bank Transfer", mix.getBank());

                                                } else {
                                                    setDrawing("SUB", globalTotal);
                                                    cdb.updateSingleCashPurchase("Bank Transfer", mix.getBank());

                                                    if (mix.getBankFee() > 0) {
                                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMethode,
                                                                ("Owner Drawing " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                                    }

                                                }
                                            }

                                            if (mix.getMpesa() > 0) {
                                                if (ownerType.matches("Add Funds")) {
                                                    setDrawing("ADD", globalTotal);
                                                    cdb.updateSingleCash("MPESA", mix.getMpesa());

                                                } else {
                                                    setDrawing("SUB", globalTotal);
                                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());

                                                    if (mix.getMpesaFee() > 0) {
                                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMethode,
                                                                ("Owner Drawing " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                                        cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                                    }
                                                }
                                            }

                                            String send = info.getText().toString();
                                            odb.addOwner(new Owner(ownerType,
                                                    globalTotal, paymentMethode, send, dateView.getText().toString()));

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
                                        if (ownerType.matches("Add Funds")) {
                                            setDrawing("ADD", globalTotal);
                                            cdb.updateSingleCash(paymentMethode, globalTotal);

                                        } else {
                                            setDrawing("SUB", globalTotal);
                                            cdb.updateSingleCashPurchase(paymentMethode, globalTotal);

                                            if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                                                String feeName = "MPESA";

                                                if (paymentMethode.matches("Bank Transfer")) {
                                                    feeName = "Bank A/C";
                                                }

                                                edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMethode,
                                                        ("Owner Drawing " + feeName + " Transfer"), dateView.getText().toString()));
                                                cdb.updateSingleCashPurchase(paymentMethode, feeTotal);
                                            }
                                        }

                                        String send = info.getText().toString();
                                        odb.addOwner(new Owner(ownerType, globalTotal, paymentMethode,
                                                send, dateView.getText().toString()));

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
         * spinner for payment
         **/

        payment = (Spinner) findViewById(R.id.payment);

        // Spinner Drop down elements
        final List<String> paymentMethod = new ArrayList<String>();
        paymentMethod.add("--Select Payment Method--");
        paymentMethod.add("Cash");
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
                        String item = parent.getItemAtPosition(position).toString();
                        paymentMethode = item;

                        feeTotal = 0;
                        fee.setText("");

                        Mix mix = mdb.getMix(1);

//                        if((paymentMethode.matches("Cash") || paymentMethode.matches("MPESA")
//                                || paymentMethode.matches("Bank Transfer"))
//                                && (getIntent().getStringExtra("FROM").matches("MIX"))){
//                            total.setText("" + globalTotal);
//                            fromMix = true;
//                        }

                        LinearLayout.LayoutParams show =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                        LinearLayout.LayoutParams hide =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        0, 0);

                        if(!(paymentMethode.matches("MPESA") || paymentMethode.matches("Bank Transfer"))){
                            feeLL.setLayoutParams(hide);

                        } else if(ownerType.matches("Draw from Till")){
                            feeLL.setLayoutParams(show);

                        } else {
                            feeLL.setLayoutParams(hide);
                        }

                        if(paymentMethode.matches("Mix")){
                            if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                                Log.d(TAG, "totals was null..");
                                total.setError("No amount set");
                                total.requestFocus();
                                payment.setSelection(0);

                            } else {
                                Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                if(ownerType.matches("Draw from Till")) {
                                    intent.putExtra("FROM", "PAY");
                                    intent.putExtra("CREDIT", "NO");

                                    intent.putExtra("TOTAL", "" + (globalTotal));

                                    startActivity(intent);


                                } else if(ownerType.matches("Add Funds")) {
                                    intent.putExtra("FROM", "REC");
                                    intent.putExtra("CREDIT", "NO");

                                    intent.putExtra("TOTAL", "" + (globalTotal));

                                    startActivity(intent);

                                }
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

                        }
                    }

                    public void onNothingSelected(AdapterView arg0) {
                        // TODO Auto-generated method stub
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
        allocationMethod.add("Draw from Till");
        allocationMethod.add("Add Funds");
        allocationMethod.add("Draw from Inventory");
        //paymentMethod.add("Mix");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allocationMethod);

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
                        ownerType = item;

                        if (ownerType.matches("Draw from Inventory")){
                            Toast.makeText(getApplicationContext(), "Select an Item Please", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplication(), InventoryDisplayActivity.class);
                            startActivity(intent);
//                            finish();

                        }

                        LinearLayout.LayoutParams show =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                        LinearLayout.LayoutParams hide =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        0, 0);

                        if(ownerType.matches("Draw from Till")){
                            feeLL.setLayoutParams(show);

                        } else {
                            feeLL.setLayoutParams(hide);
                            feeTotal = 0;

                        }

                        // Showing selected spinner item
                       // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
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

            text.append("= (total)" + sum);

            total.setText(text);

            globalTotal = sum;

            info.setText("" + getIntent().getStringExtra("INFO"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));

            if(getIntent().getStringExtra("TYPE").matches("Add Funds")){
                type.setSelection(0);
            } else if(getIntent().getStringExtra("TYPE").matches("Draw from Till")){
                type.setSelection(1);
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
         * initialise dates
         **/

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        /**
         * end date
         **/

    }
    /*
    update global variable drawings
     */

    public void setDrawing(String what, int numb){
        String date = dateView.getText().toString();

        List<GlobalVariables> globalVariablesList = gvdb.getAllVariable();

        boolean flag = true;
        for( GlobalVariables pd : globalVariablesList){
            if(pd.getVariable().matches("drawings")){
                flag = false;
            }
        }

        if(flag){
            gvdb.addVariable( new GlobalVariables("drawings", 0));
        }

        if(what.matches("ADD")){
            gvdb.updateAddVariable("drawings", numb);
        } else{
            String dat[] = date.split("/");
            int day = Integer.parseInt(dat[0]);
            int month = Integer.parseInt(dat[1]);
            int year = Integer.parseInt(dat[2]);

            List<Owner> ownerList = odb.getAllOwner();

            int total = 0;
            for( Owner pd : ownerList){
                String date2 = pd.getDate();

                String dat2[] = date2.split("/");
                int day2 = Integer.parseInt(dat2[0]);
                int month2 = Integer.parseInt(dat2[1]);
                int year2 = Integer.parseInt(dat2[2]);

                if(month == month2 && year == year2){
                    total = total + pd.getTotal();
                }
            }

            if(total > (pddb.getPerson(1).getMDSalary())){
                gvdb.updateSubVariable("drawings", numb);
            } else {

                int send = (pddb.getPerson(1).getMDSalary()) - total - numb;
                Log.d("drawings calc", "salary; " + (pddb.getPerson(1).getMDSalary()) + " total; " + total + " numb" + numb);

                if (send < 0) {
                    send = send * (-1);

                    gvdb.updateSubVariable("drawings", send);
                }
            }

        }
    }

    /*

     */

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
        // TODO Auto-generated method stub
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
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
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
