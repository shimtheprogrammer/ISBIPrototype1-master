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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.prototype.isbi.isbiprototype1.databaseHandlers.Instalment;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 2/15/2017.
 */

public class InstalmentActivity extends Activity {
    public static String TAG = "InstalmentActivity";

    Spinner payment;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    Button submit, back, btnDate;
    EditText total, savings, fee;
    CheckBox checkBox;
    LinearLayout savingll, feeLL;

    String paymentMethode = "";
    public int globalTotal = 0, savingsTotals = 0, feeTotal = 0;
    public boolean fromMix = true, checked = false;

    InstalmentHandler ildb = new InstalmentHandler(this);
    LoansHandler ldb = new LoansHandler(this);
    CashHandler cdb = new CashHandler(this);
    MixHandler mdb = new MixHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalment);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(400, 305);

        total = (EditText) findViewById(R.id.total);
        savings = (EditText) findViewById(R.id.savings);
        btnDate = (Button) findViewById(R.id.btn_date);
        dateView = (TextView) findViewById(R.id.date);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        savingll = (LinearLayout) findViewById(R.id.linear_savings);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

        total.addTextChangedListener(commaSeparator);
        savings.addTextChangedListener(commaSeparator);
        fee.addTextChangedListener(comma2);

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

        //setting text watcher
        total.addTextChangedListener(totalWatcher);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
            }
        });

        final Loans loan = ldb.getLoans(Integer.parseInt(getIntent().getStringExtra("ID")));
        List<Instalment> instalmentList = ildb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

        LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);

        if (loan.getContractualSavings() <= 0 || getIntent().getStringExtra("WHAT").matches("PAY")){
            savingll.setLayoutParams(hiding);

        } else {
            for( Instalment pd : instalmentList){
                savings.setText("" + numberFormat.format(pd.getContractualSavings()));
                savingsTotals = pd.getContractualSavings();

            }
        }

        if (getIntent().getStringExtra("WHAT").matches("PAY")) {
            feeLL.setLayoutParams(hiding);

        }

        int sav = 0;

        if(getIntent().getStringExtra("WHAT").matches("PAY")) {
            for( Instalment pd : instalmentList){
                sav = sav + pd.getContractualSavings();
            }

            total.setText("" + sav);
            globalTotal = sav;
            total.setEnabled(false);

            checkBox.setVisibility(View.INVISIBLE);
        }

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
                if(getIntent().getStringExtra("WHAT").matches("PAY")) {
                    if (paymentMethode.matches("--Select Payment Method--") && fromMix) {
                        Log.d(TAG, "btnAdd; no payment method selected..");

                        TextView errorText = (TextView)payment.getSelectedView();
                        errorText.setError("Non Selected");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("--Select Payment Method--");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    } else {
                        StringBuilder confMessaage = new StringBuilder();

                        confMessaage.append("Withdraw Contractual Saving amount " + numberFormat.format(globalTotal)
                                + " on " + dateView.getText().toString() + " to " + paymentMethode);

                        new AlertDialog.Builder(InstalmentActivity.this)
                                .setIcon(R.drawable.loan_icon_2)
                                .setTitle("Confirm Contractual Savings")
                                .setMessage("" + confMessaage.toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Mix mix = mdb.getMix(1);
                                        if (!fromMix) {
                                            StringBuilder pay = new StringBuilder();
                                            if (mix.getCash() > 0) {
                                                cdb.updateSingleCash("Cash", mix.getCash());
                                                pay.append("Cash ");
                                            }

                                            if (mix.getBank() > 0) {
                                                cdb.updateSingleCash("Bank Transfer", mix.getBank());
                                                pay.append("Bank Transfer ");
                                            }

                                            if (mix.getMpesa() > 0) {
                                                cdb.updateSingleCash("MPESA", mix.getMpesa());
                                                pay.append("MPESA");
                                            }

                                            String name[] = loan.getName().split("-");

                                            StringBuilder send = new StringBuilder();

                                            for(int i = 0; i < (name.length - 1); i++){
                                                send.append(name[i]);
                                                send.append("-");
                                            }

                                            send.append("PAY");

                                            loan.setName(send.toString());
                                            loan.setContractualSavingsDate(dateView.getText().toString());

                                            ldb.updateLoan(loan);

                                            mix.setFlag("false");
                                            mix.setCredit(0);
                                            mix.setCash(0);
                                            mix.setMpesa(0);
                                            mix.setBank(0);
                                            mix.setMpesaFee(0);
                                            mix.setBankFee(0);

                                            mdb.updateMix(mix);

                                            Intent intent = new Intent(getApplicationContext(), LoansDisplayActivity.class);
                                            intent.putExtra("FOR", "PAY");
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            cdb.updateSingleCash(paymentMethode, globalTotal);

                                            String name[] = loan.getName().split("-");

                                            StringBuilder send = new StringBuilder();

                                            for(int i = 0; i < (name.length - 1); i++){
                                                send.append(name[i]);
                                                send.append("-");
                                            }

                                            send.append("PAY");

                                            loan.setName(send.toString());
                                            loan.setContractualSavingsDate(dateView.getText().toString());

                                            ldb.updateLoan(loan);

                                            Intent intent = new Intent(getApplicationContext(), LoansDisplayActivity.class);
                                            intent.putExtra("FOR", "PAY");
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }

                } else if(getIntent().getStringExtra("WHAT").matches("NOT")) {
                    Mix mix = mdb.getMix(1);

                    List<Cash> allcash = cdb.getAllCash();

                    int cash = 0;
                    int bank = 0;
                    int mpesa = 0;

                    for (Cash pd : allcash) {
                        cash = pd.getActual();
                        bank = pd.getBank();
                        mpesa = pd.getMpesa();
                    }

                    int loansTotal = loan.getTotal();

                    List<Instalment> instalmentsList = ildb.getByForeignKey(loan.getID());

                    for (Instalment pd1 : instalmentsList) {
                        double perc = ((double) (loan.getInstalments()) * (double) loan.getInstalmentsNo());
                        double send2 = (((double) loan.getTotal() / perc) * (double) pd1.getTotal());
                        loansTotal = loansTotal - (int) Math.round(send2);
                        Log.d("cheq: ", "perce " + perc + " send " + send2 + " tot " + loansTotal);
                    }

                    if (((total.getText().toString()).matches("") || (globalTotal <= 0))) {
                        Log.d(TAG, "total not entered or is 0.. globalTotal = " + globalTotal);
                        total.setError("Please Enter Amount");
                        total.requestFocus();

                    } else if (paymentMethode.matches("--Select Payment Method--") && fromMix) {
                        Log.d(TAG, "btnAdd; no payment method selected..");

                        TextView errorText = (TextView)payment.getSelectedView();
                        errorText.setError("Non Selected");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("--Select Payment Method--");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    } else if (paymentMethode.matches("Cash") && ((globalTotal + savingsTotals) > cash)) {
                        Log.d(TAG, "btnAdd; actual too little..");

                        TextView errorText = (TextView)payment.getSelectedView();
                        errorText.setError("Not Enough Cash available");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Not Enough Cash");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    } else if (paymentMethode.matches("MPESA") && ((globalTotal + savingsTotals) > mpesa)) {
                        Log.d(TAG, "btnAdd; mpesa too little..");

                        TextView errorText = (TextView)payment.getSelectedView();
                        errorText.setError("Not Enough money in MPESA");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    } else if (paymentMethode.matches("Bank Transfer") && ((globalTotal + savingsTotals) > bank)) {
                        Log.d(TAG, "btnAdd; bank too little..");

                        TextView errorText = (TextView)payment.getSelectedView();
                        errorText.setError("Not Enough money in Bank A/C");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                        payment.requestFocus();
                        payment.performClick();

                    } else if (checked && (globalTotal < loansTotal)) {
                        Log.d(TAG, "not enough cash..");
                        total.setError("Last Instalment Must Clear Remaining Balance");
                        total.requestFocus();

                    } else if (loan.getContractualSavings() > 0 && savings.getText().toString().matches("")) {
                        Log.d(TAG, "enter savings..");
                        savings.setError("Please Enter Contractual Savings");
                        savings.requestFocus();

                    } else {
                        StringBuilder confMessaage = new StringBuilder();

                        confMessaage.append("Pay Instalment of amount " + numberFormat.format(globalTotal)
                                + " on " + dateView.getText().toString() + " via " + paymentMethode);

                        if (!(savings.getText().toString().matches(""))) {
                            confMessaage.append("\nContractual Savings is " + numberFormat.format(savingsTotals));
                        }
                        if (checked) {
                            confMessaage.append("\nLast Instalment");
                        }

                        new AlertDialog.Builder(InstalmentActivity.this)
                                .setIcon(R.drawable.loan_icon_2)
                                .setTitle("Confirm Instalment")
                                .setMessage("" + confMessaage.toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Mix mix = mdb.getMix(1);

                                        List<Cash> allcash = cdb.getAllCash();

                                        int cash2 = 0;
                                        int bank2 = 0;
                                        int mpesa2 = 0;

                                        for (Cash pd : allcash) {
                                            cash2 = pd.getActual();
                                            bank2 = pd.getBank();
                                            mpesa2 = pd.getMpesa();
                                        }
                                        int loansTotal2 = loan.getTotal();

                                        List<Instalment> instalmentsList = ildb.getByForeignKey(loan.getID());

                                        for (Instalment pd1 : instalmentsList) {
                                            double perc = ((double) (loan.getInstalments()) * (double) loan.getInstalmentsNo());
                                            double send2 = (((double) loan.getTotal() / perc) * (double) pd1.getTotal());
                                            loansTotal2 = loansTotal2 - (int) Math.round(send2);
                                            Log.d("cheq: ", "perce " + perc + " send " + send2 + " tot " + loansTotal2);
                                        }

                                        if (!fromMix) {
                                            if ((mix.getCash() > 0) && (mix.getCash() > cash2)) {
                                                Log.d(TAG, "btnAdd; actual too little..");

                                                TextView errorText = (TextView)payment.getSelectedView();
                                                errorText.setError("Not Enough Cash available");
                                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                                errorText.setText("Not Enough Cash");//changes the selected item text to this
                                                payment.requestFocus();
                                                payment.performClick();

                                            } else if ((mix.getMpesa() > 0) && (mix.getMpesa() > mpesa2)) {
                                                Log.d(TAG, "btnAdd; mpesa too little..");

                                                TextView errorText = (TextView)payment.getSelectedView();
                                                errorText.setError("Not Enough money in MPESA");
                                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                                errorText.setText("Not Enough Money in MPESA");//changes the selected item text to this
                                                payment.requestFocus();
                                                payment.performClick();

                                            } else if ((mix.getBank() > 0) && (mix.getBank() >  bank2)) {
                                                Log.d(TAG, "btnAdd; bank too little..");

                                                TextView errorText = (TextView)payment.getSelectedView();
                                                errorText.setError("Not Enough money in Bank A/C");
                                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                                errorText.setText("Not Enough Money in Bank");//changes the selected item text to this
                                                payment.requestFocus();
                                                payment.performClick();

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
                                                                ("Loan Instalment " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());

                                                    }
                                                }

                                                if (mix.getMpesa() > 0) {
                                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());
                                                    pay.append("MPESA");

                                                    if (mix.getMpesaFee() > 0) {
                                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMethode,
                                                                ("Loan Instalment " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getMpesaFee());

                                                    }
                                                }

                                                if (globalTotal >= loansTotal2) {
                                                    loan.setStatus("PAY");
                                                    ldb.updateLoan(loan);
                                                }

                                                List<Instalment> instalmentList = ildb.getByForeignKey(loan.getID());

                                                String number = "" + (instalmentList.size() + 1);

                                                if (checked) {
                                                    number = "Last";
                                                }

                                                ildb.addInstalments(new Instalment(globalTotal, pay.toString(),
                                                        dateView.getText().toString(), number, savingsTotals, loan.getID()));

                                                mix.setFlag("false");
                                                mix.setCredit(0);
                                                mix.setCash(0);
                                                mix.setMpesa(0);
                                                mix.setBank(0);
                                                mix.setMpesaFee(0);
                                                mix.setBankFee(0);

                                                mdb.updateMix(mix);

                                                Intent intent = new Intent(getApplicationContext(), LoansDisplayActivity.class);
                                                intent.putExtra("FOR", "NOT");
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            cdb.updateSingleCashPurchase(paymentMethode, globalTotal);

                                            List<Instalment> instalmentList = ildb.getByForeignKey(loan.getID());

                                            String number = "" + (instalmentList.size() + 1);

                                            if (checked) {
                                                number = "Last";
                                            }

                                            ildb.addInstalments(new Instalment(globalTotal, paymentMethode,
                                                    dateView.getText().toString(), number, savingsTotals, loan.getID()));

                                            if (globalTotal >= loansTotal2) {
                                                loan.setStatus("PAY");
                                                ldb.updateLoan(loan);
                                            }

                                            if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                                                String feeName = "MPESA";

                                                if (paymentMethode.matches("Bank Transfer")) {
                                                    feeName = "Bank A/C";
                                                }

                                                edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMethode,
                                                        ("Loan Instalment " + feeName + " Transfer"), dateView.getText().toString()));
                                                cdb.updateSingleCashPurchase(paymentMethode, feeTotal);
                                            }

                                            Intent intent = new Intent(getApplicationContext(), LoansDisplayActivity.class);
                                            intent.putExtra("FOR", "NOT");
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
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
        paymentMethod.add("MPESA");
        paymentMethod.add("Mix");
        paymentMethod.add("Bank Transfer");

        // Creating adapter for spinner
        //ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentMethod);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, R.layout.activity_spinner_item, paymentMethod);

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

                        Mix mix = mdb.getMix(1);

                        feeTotal = 0;
                        fee.setText("");

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
                                new LinearLayout.LayoutParams(0, 0, 0);

                        if((paymentMethode.matches("MPESA") || paymentMethode.matches("Bank Transfer"))
                                && !(getIntent().getStringExtra("WHAT").matches("PAY")) ){
                            feeLL.setLayoutParams(show);
                            feeTotal = 0;

                        } else {
                            feeLL.setLayoutParams(hide);

                        }

                        if (paymentMethode.matches("Mix")) {
                            if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                                Log.d(TAG, "totals was null..");
                                total.setError("No amount set");
                                total.requestFocus();
                                payment.setSelection(0);

                            } else {
                                Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                if ((getIntent().getStringExtra("WHAT").matches("PAY"))) {
                                    intent.putExtra("FROM", "REC");
                                    intent.putExtra("CREDIT", "NO");

                                } else {
                                    intent.putExtra("FROM", "PAY");
                                    intent.putExtra("CREDIT", "NO");

                                }

                                intent.putExtra("TOTAL", "" + (globalTotal + savingsTotals));

                                startActivity(intent);
                            }
                        }  else {
                            mix.setFlag("false");
                            mix.setCredit(0);
                            mix.setCash(0);
                            mix.setMpesa(0);
                            mix.setBank(0);
                            mix.setMpesaFee(0);
                            mix.setBankFee(0);

                            mdb.updateMix(mix);

                            total.setText("" + numberFormat.format(globalTotal));

                            if (paymentMethode.matches("MPESA") && !(getIntent().getStringExtra("WHAT").matches("PAY"))) {
                                int feeSuj = mpesaFee(globalTotal);

                                fee.setText("" + feeSuj);
                                feeTotal = feeSuj;
                            }

                            fromMix = true;

                        }

                        // Showing selected spinner item
                        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView arg0) {

                    }
                }
        );

        /**
         * end of spinner payment
         **/

        /**
         * Handling data from mix
         **/

        if(getIntent().getStringExtra("FROM").matches("MIX")){
            fromMix = false;

            StringBuilder text = new StringBuilder();

            int sum = 0;
            text.append("Mix Payment; ");

            if (!(getIntent().getStringExtra("CASH").matches("0"))) {
                text.append(" (actual) " + getIntent().getStringExtra("CASH"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("CASH"));
            }

            if (!(getIntent().getStringExtra("BANK").matches("0"))) {
                text.append(" (bank) " + getIntent().getStringExtra("BANK"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("BANK"));
            }

            if (!(getIntent().getStringExtra("MPESA").matches("0"))) {
                text.append(" (mpesa) " + getIntent().getStringExtra("MPESA"));
                sum = sum + Integer.parseInt(getIntent().getStringExtra("MPESA"));
            }
            text.append("= (total) " + sum);

            globalTotal = sum - savingsTotals;

            total.setText(text);

            globalTotal = sum - savingsTotals;

            dateView.setText("" + getIntent().getStringExtra("DATE"));
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


            globalTotal = sum - savingsTotals;

            total.removeTextChangedListener(commaSeparator);

            total.setText(text);

            total.addTextChangedListener(commaSeparator);


        } else {

            if (globalTotal > 0) {
                total.setText("" + numberFormat.format(globalTotal));
            }

            if (paymentMethode.matches("MPESA") && !(getIntent().getStringExtra("WHAT").matches("PAY"))) {
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


            globalTotal = sum - savingsTotals;

            total.removeTextChangedListener(commaSeparator);

            total.setText(text);

            total.addTextChangedListener(commaSeparator);


        } else {

            if (globalTotal > 0) {
                total.setText("" + numberFormat.format(globalTotal));
            }

            if (paymentMethode.matches("MPESA") && !(getIntent().getStringExtra("WHAT").matches("PAY"))) {
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
                //globalTotal = Integer.parseInt(total.getText().toString());
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
                //globalTotal = Integer.parseInt(total.getText().toString());
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
     * text watcher for formating cost
     **/

    private final TextWatcher commaSeparator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            total.removeTextChangedListener(commaSeparator);

            if (!(total.getText()).toString().matches("")) {
                String cstArray[] = (total.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                globalTotal = Integer.parseInt(cst.toString());
                total.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            total.addTextChangedListener(commaSeparator);

            savings.removeTextChangedListener(commaSeparator);

            if (!(savings.getText()).toString().matches("")) {
                String cstArray[] = (savings.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                savingsTotals = Integer.parseInt(cst.toString());
                savings.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            savings.addTextChangedListener(commaSeparator);
        }

        @Override
        public void afterTextChanged(Editable s) {
            total.setSelection(total.getText().length());
            savings.setSelection(savings.getText().length());
        }
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
