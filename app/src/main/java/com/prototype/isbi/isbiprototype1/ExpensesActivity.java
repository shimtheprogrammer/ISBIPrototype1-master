package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;

/**
 * Created by MRuto on 1/25/2017.
 */

public class ExpensesActivity extends Activity {
    public static String TAG = "ExpensesActivity";

    TextInputLayout infoDesign, periodDesign;
    Spinner type, payment;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    Button submit, back, btnDate;
    EditText total, name, info, period, fee;
    LinearLayout paymentLL, feeLL;

    String paymentMethode = "", expenseType = "";
    public int globalTotal = 0, feeTotal = 0;
    public boolean fromMix = true;

    ExpensesHandler edb = new ExpensesHandler(this);
    CashHandler cdb = new CashHandler(this);
    MixHandler mdb = new MixHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        total = (EditText) findViewById(R.id.total);
        name = (EditText) findViewById(R.id.name);
        info = (EditText) findViewById(R.id.info);
        period = (EditText) findViewById(R.id.period);
        btnDate = (Button) findViewById(R.id.btn_date);
        //expListView = (ExpandableListView) findViewById(R.id.lvExp2);
        dateView = (TextView) findViewById(R.id.date);
        paymentLL = (LinearLayout) findViewById(R.id.payment_ll);
        infoDesign = (TextInputLayout) findViewById(R.id.info_design);
        periodDesign = (TextInputLayout) findViewById(R.id.period_design);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

        //setting text watcher
        total.addTextChangedListener(totalWatcher);

        LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);

        if (getIntent().getStringExtra("FROM").matches("REG")){
            paymentLL.setLayoutParams(hiding);
            feeLL.setLayoutParams(hiding);
        }

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

                int rentMonths = 1;

                if (expenseType.matches("Rent")){
                    if (!period.getText().toString().matches("")) {
                        rentMonths = Integer.parseInt(period.getText().toString());
                    }
                }

                Log.d("Expense Total2", "" + globalTotal);
                if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                    Log.d("Values: ", "totals was null..");
                    Toast.makeText(getApplicationContext(), "Amount not set",
                            Toast.LENGTH_LONG).show();
                } else if(name.getText().toString().matches("")){
                    Log.d("Values: ", "no added info..");
                    Toast.makeText(getApplicationContext(), "Please Enter To Who/Where",
                            Toast.LENGTH_LONG).show();
                } else if(expenseType.matches("--Select Expense--")){
                    Log.d("Values: ", "no Expense selected..");
                    Toast.makeText(getApplicationContext(), "Please Select an Expense",
                            Toast.LENGTH_LONG).show();
                } else if(expenseType.matches("Rent") && (period.getText().toString().matches("")
                        || rentMonths <= 0)){
                    Log.d("Values: ", "rent months error..");

                    period.setError("Please Enter the Number of Months");
                    period.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("REG")){
                    if(((expenseType.matches("Equipment"))
                            && globalTotal >= 10000)) {
                        Log.d("Values: ", "equipment too cheap..");
                        Toast.makeText(getApplicationContext(), "The Equipment bought is too expensive and should be set as an Asset",
                                Toast.LENGTH_LONG).show();

                    } else {
                        String send = info.getText().toString() + period.getText().toString();

                        edb.addExpense(new Expenses(expenseType, name.getText().toString(),
                                globalTotal, " ", send, dateView.getText().toString()));

                        finish();

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
                } else if((expenseType.matches("Equipment") && globalTotal >= 10000)){
                    Log.d("Values: ", "equipment too expensive..");
                    Toast.makeText(getApplicationContext(), "The Equipment bought is too expensive and has been set as an Asset",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), AssetsActivity.class);

                    intent.putExtra("FROM", "EXP");
                    intent.putExtra("INFO", ""+ name.getText().toString() + " " + info.getText().toString());
                    intent.putExtra("DATE", ""+ dateView.getText().toString());
                    intent.putExtra("PAY", ""+ paymentMethode);
                    intent.putExtra("TOTAL", "" + globalTotal);

                    startActivity(intent);
                    finish();

                } else {

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
                                                ("Expense " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                    }
                                }

                                if (mix.getMpesa() > 0) {
                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());
                                    pay.append("MPESA ");
                                    if (mix.getMpesaFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMethode,
                                                ("Expense " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                    }
                                }

                                String send = info.getText().toString() + period.getText().toString();
                                edb.addExpense(new Expenses(expenseType, name.getText().toString(),
                                        globalTotal, pay.toString(), send, dateView.getText().toString()));

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
                            cdb.updateSingleCashPurchase(paymentMethode, globalTotal);

                            String send = info.getText().toString() + period.getText().toString();
                            edb.addExpense(new Expenses(expenseType, name.getText().toString(),
                                    globalTotal, paymentMethode, send, dateView.getText().toString()));

                            if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                                String feeName = "MPESA";

                                if (paymentMethode.matches("Bank Transfer")) {
                                    feeName = "Bank A/C";
                                }

                                edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMethode,
                                        ("Expense " + feeName + " Transfer"), dateView.getText().toString()));
                                cdb.updateSingleCashPurchase(paymentMethode, feeTotal);
                            }

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
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
                        Mix mix = mdb.getMix(1);

                        LinearLayout.LayoutParams hide = new LinearLayout.LayoutParams(0, 0, 0);
                        LinearLayout.LayoutParams show = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        String item = parent.getItemAtPosition(position).toString();
                        paymentMethode = item;

                        feeTotal = 0;
                        fee.setText("");

//                        if((paymentMethode.matches("Cash") || paymentMethode.matches("MPESA")
//                                || paymentMethode.matches("Bank Transfer"))
//                                && (getIntent().getStringExtra("FROM").matches("MIX"))){
//                            total.setText("" + globalTotal);
//                            fromMix = true;
//                        }

                        if(!(paymentMethode.matches("MPESA") || paymentMethode.matches("Bank Transfer"))){
                            feeLL.setLayoutParams(hide);

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
                                intent.putExtra("CREDIT", "NO");

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

                        }

                        // Showing selected spinner item
//                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
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
        final List<String> allocationMethod = new ArrayList<String>();
        allocationMethod.add("--Select Expense--");
        allocationMethod.add("Salary");
        allocationMethod.add("Rent");
        allocationMethod.add("Utilities");
        allocationMethod.add("Wage");
        allocationMethod.add("Taxes");
        allocationMethod.add("Airtime");
        allocationMethod.add("Equipment");
        allocationMethod.add("Other");

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
                        expenseType = item;

                        LinearLayout.LayoutParams layoutParams =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                        LinearLayout.LayoutParams layoutParams2 =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 0f);

                        if(expenseType.matches("Salary") ||expenseType.matches("Rent") || expenseType.matches("Utilities")
                                || expenseType.matches("Wage") || expenseType.matches("Taxes")){
                            periodDesign.setLayoutParams(layoutParams2);
                            infoDesign.setLayoutParams(layoutParams);
                        } else if(expenseType.matches("Equipment")){
                            periodDesign.setLayoutParams(layoutParams2);
                            infoDesign.setLayoutParams(layoutParams2);
                        }else {
                            infoDesign.setLayoutParams(layoutParams2);
                            periodDesign.setLayoutParams(layoutParams);
                        }

                        if(expenseType.matches("Rent")){
                            period.setInputType(InputType.TYPE_CLASS_NUMBER);
                            periodDesign.setHint("Number of Months");
                        } else {
                            period.setInputType(InputType.TYPE_CLASS_TEXT);
                            periodDesign.setHint("Payment Period");
                        }

                        // Showing selected spinner item
                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );

        /**
         * end spinner allocsation
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

            text.append("= (total) " + sum);

            total.setText(text);

            globalTotal = sum;

            Log.d("Expense Total", "" + globalTotal);

            name.setText("" + getIntent().getStringExtra("NAME"));
            info.setText("" + getIntent().getStringExtra("INFO"));
            period.setText("" + getIntent().getStringExtra("PERIOD"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));

            if(getIntent().getStringExtra("TYPE").matches("Salary")){
                type.setSelection(0);
            } else if(getIntent().getStringExtra("TYPE").matches("Rent")){
                type.setSelection(1);
            } else if(getIntent().getStringExtra("TYPE").matches("Utilities")){
                type.setSelection(2);
            } else if(getIntent().getStringExtra("TYPE").matches("Wage")){
                type.setSelection(3);
            } else if(getIntent().getStringExtra("TYPE").matches("Taxes")){
                type.setSelection(4);
            } else if(getIntent().getStringExtra("TYPE").matches("Airtime")){
                type.setSelection(5);
            } else if(getIntent().getStringExtra("TYPE").matches("Equipment")){
                type.setSelection(6 );
            } else if(getIntent().getStringExtra("TYPE").matches("Other")){
                type.setSelection(7);
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
         * Handling data from assets
         **/

        if(getIntent().getStringExtra("FROM").matches("ASS")){


            globalTotal = Integer.parseInt(getIntent().getStringExtra("TOTAL"));

            total.setText("" + getIntent().getStringExtra("TOTAL"));
            name.setText("" + getIntent().getStringExtra("INFO"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));

            if(getIntent().getStringExtra("TYPE").matches("EQU")) {
                type.setSelection(6);
            } else if(getIntent().getStringExtra("TYPE").matches("OTH")) {
                type.setSelection(7);
            }

            if(getIntent().getStringExtra("PAY").matches("Cash")){
                payment.setSelection(1);

            } else if(getIntent().getStringExtra("PAY").matches("MPESA")){
                payment.setSelection(2);

            } else if(getIntent().getStringExtra("PAY").matches("Bank Transfer")){
                payment.setSelection(4);

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
