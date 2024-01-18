package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.TextInputLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 4/20/2017.
 */

public class MoneyTransferActivity extends Activity {
    public static String TAG = "MoneyTransferActivity";

    EditText total, fee;
    Button btnExit, btnAdd;
    TextInputLayout totalWidget;
    Spinner money;
    LinearLayout feeLL, dateLL;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    int globalTotal = 0, globalFee = 0;

    String moneySelected = "";

    CashHandler cdb = new CashHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(350, 350);

        total = (EditText) findViewById(R.id.total);
        totalWidget = (TextInputLayout) findViewById(R.id.total_widget);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);
        money = (Spinner) findViewById(R.id.money);
        fee = (EditText) findViewById(R.id.fee);
        dateView = (TextView) findViewById(R.id.date);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        dateLL = (LinearLayout) findViewById(R.id.date_ll);

        total.addTextChangedListener(comma);
//        total.addTextChangedListener(mpesaChart);
        fee.addTextChangedListener(comma2);

        String from = getIntent().getStringExtra("FROM");

        Log.d(TAG, "From " + getIntent().getStringExtra("FROM"));

        final List<String> moneyTypes = new ArrayList<>();

        LinearLayout.LayoutParams hide = new LinearLayout.LayoutParams(0, 0, 0);


        if (from.matches("Cash")) {
            totalWidget.setHint("Transfer From Cash");
            moneyTypes.add("To MPESA");
            moneyTypes.add("To Bank A/C");

            feeLL.setLayoutParams(hide);
            dateLL.setLayoutParams(hide);

        } else if (from.matches("Bank")) {
            totalWidget.setHint("Transfer from Bank A/C");
            moneyTypes.add("To Cash");
            moneyTypes.add("To MPESA");

        } else if (from.matches("MPESA")) {
            totalWidget.setHint("Transfer from MPESA");
            moneyTypes.add("To Cash");
            moneyTypes.add("To Bank A/C");

        }

        ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<>(this, R.layout.activity_spinner_item, moneyTypes);

        // Drop down layout style - list view with radio button
        dataAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        money.setAdapter(dataAdapterUnits);

        money.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                           @Override
                                           public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                               // On selecting a spinner item
                                               moneySelected = parent.getItemAtPosition(position).toString();

                                           }

                                           public void onNothingSelected(AdapterView arg0) {

                                           }
                                       }
        );

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

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

                int cash = 0, bank = 0, mpesa = 0;
                List<Cash> cashList = cdb.getAllCash();

                for (Cash pd : cashList) {
                    cash = pd.getActual();
                    bank = pd.getBank();
                    mpesa = pd.getMpesa();

                }

                int feeTotal = 0;

                if (!(fee.getText().toString().matches(""))){
                    Log.d(TAG, "fee present");
                    feeTotal = globalFee;

                }

                if (total.getText().toString().matches("")){
                    Log.d(TAG, "total was empty");
                    total.setError("Please Enter a Value");
                    total.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("Cash")
                        && globalTotal > cash){
                    Log.d(TAG, "actual too little");
                    total.setError("Value Exceeds Cash Amount");
                    total.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("MPESA")
                        && globalTotal > mpesa){
                    Log.d(TAG, "mpesa too little");
                    total.setError("Value Exceeds Amount in MPESA");
                    total.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("Bank")
                        && globalTotal > bank){
                    Log.d(TAG, "bank too little");
                    total.setError("Value Exceeds Amount in Bank A/C");
                    total.requestFocus();

                } else if (feeTotal > globalTotal){
                    Log.d(TAG, "fee too hight");
                    fee.setError("Transfer Fee cannot be more than Total Amount");
                    fee.requestFocus();

                } else {
                    StringBuilder message = new StringBuilder();

                    message.append("Move " + globalTotal + " KSH From ");

                    String from2 = getIntent().getStringExtra("FROM");

                    if (from2.matches("Cash")) {
                        message.append("Cash in Hand ");

                    } else if (from2.matches("Bank")) {
                        message.append("Bank A/C ");

                    } else if (from2.matches("MPESA")) {
                        message.append("MPESA ");

                    }

                    message.append(moneySelected);

                    if (!(getIntent().getStringExtra("FROM").matches("Cash"))) {
                        message.append("\nTransfer Fee is " + feeTotal);
                    }

                    new AlertDialog.Builder(MoneyTransferActivity.this)
                            .setIcon(R.drawable.cash_flow_icon_2)
                            .setTitle("Confirm Money Transfer")
                            .setMessage(message.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (moneySelected.matches("To Cash")) {
                                        cdb.updateSingleCash("Cash",  globalTotal);

                                    } else if (moneySelected.matches("To Bank A/C")) {
                                        cdb.updateSingleCash("Bank Transfer", globalTotal);


                                    } else if (moneySelected.matches("To MPESA")) {
                                        cdb.updateSingleCash("MPESA", globalTotal);

                                    }

                                    String from3 = getIntent().getStringExtra("FROM");

                                    if (from3.matches("Cash")) {
                                        cdb.updateSingleCashPurchase("Cash",  globalTotal);

                                    } else if (from3.matches("Bank")) {
                                        cdb.updateSingleCashPurchase("Bank Transfer", globalTotal);

                                    } else if (from3.matches("MPESA")) {
                                        cdb.updateSingleCashPurchase("MPESA", globalTotal);

                                    }

//                                    calendar = Calendar.getInstance();
//                                    year = calendar.get(Calendar.YEAR);
//
//                                    month = calendar.get(Calendar.MONTH);
//                                    day = calendar.get(Calendar.DAY_OF_MONTH);

                                    if (!(fee.getText().toString().matches(""))){
                                        Log.d(TAG, "fee present, adding expense");
                                        edb.addExpense(new Expenses("Other", "Transaction Fee" ,
                                                globalFee, from3, ("Transfer " + moneySelected),
                                                dateView.getText().toString()));

                                        if (from3.matches("Bank")) {
                                            cdb.updateSingleCashPurchase("Bank Transfer", globalFee);

                                        } else if (from3.matches("MPESA")) {
                                            cdb.updateSingleCashPurchase("MPESA", globalFee);

                                        }

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

            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis() - 1000);

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

    /**
     * text watcher for getting total
     **/

    private final TextWatcher comma = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!total.getText().toString().matches("")) {
                total.removeTextChangedListener(comma);

                String cstArray[] = (total.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }

                globalTotal = Integer.parseInt(cst.toString());
                total.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));

                if(getIntent().getStringExtra("FROM").matches("MPESA") && moneySelected.matches("To Cash")) {
                    fee.removeTextChangedListener(comma2);
                    int feeSuj = mpesaFee(globalTotal);

                    fee.setText("" + feeSuj);
                    globalFee = feeSuj;

                    fee.addTextChangedListener(comma2);

                }

                total.addTextChangedListener(comma);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!total.getText().toString().matches("")) {
                total.setSelection(total.getText().length());
            }
        }
    };

    /**
     * end of textr watcher
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

                globalFee = Integer.parseInt(cst.toString());
                fee.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));

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

            if(getIntent().getStringExtra("FROM").matches("MPESA") && moneySelected.matches("To Cash")) {
                if (globalTotal <= 50){
                    fee.setText("" + 0);
                    globalFee = 0;

                } else if (globalTotal <= 100){
                    fee.setText("" + 10);
                    globalFee = 10;

                } else if (globalTotal <= 2500){
                    fee.setText("" + 27);
                    globalFee = 27;

                } else if (globalTotal <= 3500){
                    fee.setText("" + 49);
                    globalFee = 49;

                } else if (globalTotal <= 5000){
                    fee.setText("" + 66);
                    globalFee = 66;

                } else if (globalTotal <= 7500){
                    fee.setText("" + 82);
                    globalFee = 82;

                } else if (globalTotal <= 10000){
                    fee.setText("" + 110);
                    globalFee = 110;

                } else if (globalTotal <= 15000){
                    fee.setText("" + 159);
                    globalFee = 159;

                } else if (globalTotal <= 20000){
                    fee.setText("" + 176);
                    globalFee = 176;

                } else if (globalTotal <= 35000){
                    fee.setText("" + 187);
                    globalFee = 187;

                } else if (globalTotal <= 50000){
                    fee.setText("" + 275);
                    globalFee = 275;

                } else {
                    fee.setText("" + 330);
                    globalFee = 330;

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

        if (numb <= 50){
            numbFee = 0;

        } else if (numb <= 100){
            numbFee = 10;

        } else if (numb <= 2500){
            numbFee = 27;

        } else if (numb <= 3500){
            numbFee = 49;

        } else if (numb <= 5000){
            numbFee = 66;

        } else if (numb <= 7500){
            numbFee = 82;

        } else if (numb <= 10000){
            numbFee = 110;

        } else if (numb <= 15000){
            numbFee = 159;

        } else if (numb <= 20000){
            numbFee = 176;

        } else if (numb <= 35000){
            numbFee = 187;

        } else if (numb <= 50000){
            numbFee = 275;

        } else {
            numbFee = 330;

        }

        return numbFee;
    }

}
