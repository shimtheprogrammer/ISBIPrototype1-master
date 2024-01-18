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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Instalment;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 2/15/2017.
 */

public class LoansActivity extends Activity {
    public static String TAG = "LoansActivity";

    private Calendar calendar;
    private TextView dateView, dueDate, lastDate;
    private int year, month, day;
//    private int heightEL = 0;
//    private int heightEL0 = 0;

    CheckBox save;
    Button btnExit, btnAdd, btnDueDate, btnDate, btnLastDate;
    EditText total, instalment, instalmentNo, savings, paidLoan;
    Spinner payment;
    TextInputLayout instalmentWidget;
    LinearLayout instalmenstNoPaidLL, paymentLL, saveBox, saveNumb;

    //String productName = "";
    String paymentMethode = "";
    public int globalTotal = 0, instalmentTotal, savingsTotals = 0, paidLoanTotal;
    public boolean fromMix = true, saver = false;

    public AutoCompleteTextView creditor;

    LoansHandler ldb = new LoansHandler(this);
    InstalmentHandler ildb = new InstalmentHandler(this);
    CashHandler cdb = new CashHandler(this);
    MixHandler mdb = new MixHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));
        //lining parameters to values on xml
        creditor = (AutoCompleteTextView) findViewById(R.id.creditor);
        total = (EditText) findViewById(R.id.total);
        payment = (Spinner) findViewById(R.id.payment);
        dueDate = (TextView) findViewById(R.id.due_date);
        btnDueDate = (Button) findViewById(R.id.btn_due_date);
        btnLastDate = (Button) findViewById(R.id.btn_last_date);
        btnDate = (Button) findViewById(R.id.btn_date);
        dateView = (TextView) findViewById(R.id.date);
        lastDate = (TextView) findViewById(R.id.last_date);
        instalment = (EditText) findViewById(R.id.instalment);
        instalmentWidget = (TextInputLayout) findViewById(R.id.instalment_widget);
        instalmentNo = (EditText) findViewById(R.id.instalmentNo);
        savings = (EditText) findViewById(R.id.contractual_savings);
        paidLoan = (EditText) findViewById(R.id.instalment_paid);
        instalmenstNoPaidLL = (LinearLayout) findViewById(R.id.no_instal_ll);
        paymentLL = (LinearLayout) findViewById(R.id.payment_ll);
        save = (CheckBox) findViewById(R.id.savings);
        saveBox = (LinearLayout) findViewById(R.id.layout_check_box_save);
        saveNumb = (LinearLayout) findViewById(R.id.layout_save_e_t);

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

        btnLastDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnLastDate.setBackgroundResource(R.drawable.button_small_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnLastDate.setBackgroundResource(R.drawable.button_small);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        //setting text watcher
        total.addTextChangedListener(totalWatcher);
        total.addTextChangedListener(commaSeparator);
        instalment.addTextChangedListener(commaSeparator);
        paidLoan.addTextChangedListener(commaSeparator);
        savings.addTextChangedListener(commaSeparator);

        final LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);
        final LinearLayout.LayoutParams showing =
                new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        final LinearLayout.LayoutParams showing2 =
                new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2);

        if (getIntent().getStringExtra("FROM").matches("REG")){
            paymentLL.setLayoutParams(hiding);

        } else {
            instalmenstNoPaidLL.setLayoutParams(hiding);

        }

        saveNumb.setLayoutParams(hiding);
        saveBox.setLayoutParams(showing2);

        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saver = isChecked;

                if (saver){
                    instalmentWidget.setHint("Instalment Amount Excluding Contractual Savings");

                } else {
                    instalmentWidget.setHint("Instalment Amount");

                }

                if (getIntent().getStringExtra("FROM").matches("REG")) {
                    if (isChecked) {
                        saveBox.setLayoutParams(showing);
                        saveNumb.setLayoutParams(showing);

                    } else {
                        saveNumb.setLayoutParams(hiding);
                        saveBox.setLayoutParams(showing2);

                    }
                } else {
                    saveNumb.setLayoutParams(hiding);
                    saveBox.setLayoutParams(showing2);

                }
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
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethod);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        payment.setAdapter(dataAdapter1);

        payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                paymentMethode = item;

                if ((paymentMethode.matches("Cash") || paymentMethode.matches("MPESA")
                        || paymentMethode.matches("Bank Transfer"))
                        && (getIntent().getStringExtra("FROM").matches("MIX"))) {
                    total.setText("" + globalTotal);
                    fromMix = true;

                }

                if (paymentMethode.matches("Mix")) {
                    if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                        Log.d(TAG, "totals was null..");
                        total.setError("No amount set");
                        total.requestFocus();
                        payment.setSelection(0);

                    } else {
                        Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                        intent.putExtra("FROM", "REC");
                        intent.putExtra("CREDIT", "NO");

                        intent.putExtra("TOTAL", "" + (globalTotal));

                        startActivity(intent);

                    }

                }
            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

        if (getIntent().getStringExtra("FROM").matches("MIX")) {
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
            text.append("= (total)" + sum);

            globalTotal = sum;

            total.setText(text);

            creditor.setText("" + getIntent().getStringExtra("NAME"));
            instalment.setText("" + getIntent().getStringExtra("INSTAL"));
            instalmentNo.setText("" + getIntent().getStringExtra("INSTALNO"));
            savings.setText("" + getIntent().getStringExtra("SAVE"));
            dateView.setText("" + getIntent().getStringExtra("SDATE"));
            dueDate.setText("" + getIntent().getStringExtra("EDATE"));
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

            fromMix = true;

            if(paymentMethode.matches("Mix")) {
                payment.setSelection(0);
            }
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDueDate(year, month+1, day);
        showLateDate(year, month+1, day);

        btnExit = (Button) findViewById(R.id.btn_back);
        btnAdd = (Button) findViewById(R.id.btn_submit);

        final List<String> creditorList = new ArrayList<String>();

        List<Loans> loans = ldb.getAllLoans();
        for (Loans pd : loans) {
            String name[] = pd.getName().split("-");
            String log = name[0];
            // Writing Contacts to log
            Log.d("Name: ", log);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < creditorList.size(); i++){
                    if((creditorList.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    creditorList.add(log);
                }
            }
        }

        String[] creditors = creditorList.toArray(new String[creditorList.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, creditors);

        //Find TextView control

        //Set the number of characters the user must type before the drop down list is shown
        creditor.setThreshold(1);

        //Set the adapter
        creditor.setAdapter(adapter);

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

                if((total.getText().toString()).matches("") ){
                    Log.d(TAG, "totals was null..");
                    total.setError("Please Enter Net Loan Amount");
                    total.requestFocus();

                } else if(globalTotal <= 0){
                    Log.d(TAG, "no amount set..");
                    total.setError("Amount Cannot be 0");
                    total.requestFocus();

                } else if(instalment.getText().toString().matches("")){
                    Log.d(TAG, "no instalment set..");
                    instalment.setError("Pease Enter Instalment Amuont");
                    instalment.requestFocus();

                } else if(instalmentTotal <= 0){
                    Log.d(TAG, "no amount set..");
                    instalment.setError("Amount Cannot be 0");
                    instalment.requestFocus();

                } else if(instalmentNo.getText().toString().matches("")){
                    Log.d(TAG, "no no. of instalment set..");
                    instalmentNo.setError("Pease Enter Number of Instalments");
                    instalmentNo.requestFocus();

                } else if(Integer.parseInt(instalmentNo.getText().toString()) > 200){
                    Log.d(TAG, "instalmentNo too high..");
                    instalmentNo.setError("Number too high");
                    instalmentNo.requestFocus();

                } else if(instalmentTotal > globalTotal){
                    Log.d(TAG, "instalment too high..");
                    instalment.setError("Amount too high");
                    instalment.requestFocus();

                } else if(creditor.getText().toString().matches("")){
                    Log.d(TAG, "no creditor on credit..");
                    creditor.setError("Please Enter Creditor Name");
                    creditor.requestFocus();

                } else if (getIntent().getStringExtra("FROM").matches("REG")){
                    if (paidLoan.getText().toString().matches("")){
                        Log.d("Values: ", "no of instalment paid blank..");
                        paidLoan.setError("Please Enter Amount Already Paid");
                        paidLoan.requestFocus();

                    } else if(savingsTotals > globalTotal){
                        Log.d(TAG, "savings too high..");
                        savings.setError("Amount too high");
                        savings.requestFocus();

                    } else if(paidLoanTotal > (Integer.parseInt(instalmentNo.getText().toString()) * instalmentTotal)){
                        Log.d(TAG, "paidLoan too high..");
                        paidLoan.setError("Amount too high");
                        paidLoan.requestFocus();

                    } else {
                        StringBuilder confMessaage = new StringBuilder();

                        confMessaage.append("Loan from " + creditor.getText().toString() + " amount "
                                + numberFormat.format(globalTotal) + " on " + dateView.getText().toString());
                        confMessaage.append("\n" + instalmentNo.getText().toString() + " instalments of "
                                + numberFormat.format(instalmentTotal) + " to be paid by " + dueDate.getText().toString());
                        if (saver) {
                            confMessaage.append("\nContractual Savings Required");
                        } else {
                            confMessaage.append("\nContractual Savings NOT Required");
                        }
                        confMessaage.append("\nAmount already Paid " + numberFormat.format(paidLoanTotal));
                        confMessaage.append("\nContractual Savings accrued " + numberFormat.format(savingsTotals));
                        confMessaage.append("\nDate of last instalment " + lastDate.getText().toString());

                        new AlertDialog.Builder(LoansActivity.this)
                                .setIcon(R.drawable.loan_icon_2)
                                .setTitle("Confirm Loan")
                                .setMessage("" + confMessaage.toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int save = 0;

                                        if (saver){
                                            save = 1;
                                        }

                                        ldb.addLoan(new Loans(creditor.getText().toString() + "-SAV", globalTotal,
                                                paymentMethode, dateView.getText().toString(), dueDate.getText().toString(),
                                                instalmentTotal, Integer.parseInt(instalmentNo.getText().toString()),
                                                save, "", "NOT"));

                                        ildb.addInstalments(new Instalment(paidLoanTotal, " ",
                                                lastDate.getText().toString(), "Registration", savingsTotals, ldb.getLoansCount()));

                                        finish();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                } else if(paymentMethode.matches("--Select Payment Method--") && fromMix){
                    Log.d(TAG, "btnAdd; no payment method selected..");

                    TextView errorText = (TextView)payment.getSelectedView();
                    errorText.setError("Non Selected");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("--Select Payment Method--");//changes the selected item text to this
                    payment.requestFocus();
                    payment.performClick();

                } else {
                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("Loan from " + creditor.getText().toString() + " amount "
                            + numberFormat.format(globalTotal) + " on " + dateView.getText().toString());
                    confMessaage.append("\n" + instalmentNo.getText().toString() + " instalments of "
                            + numberFormat.format(instalmentTotal) + " to be paid by " + dueDate.getText().toString());
                    if (saver) {
                        confMessaage.append("\nContractual Savings Required");
                    } else {
                        confMessaage.append("\nContractual Savings NOT Required");
                    }

                    new AlertDialog.Builder(LoansActivity.this)
                            .setIcon(R.drawable.loan_icon_2)
                            .setTitle("Confirm Loan")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!fromMix){
                                        Mix mix = mdb.getMix(1);

                                        StringBuilder pay = new StringBuilder();
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
                                        int save = 0;

                                        if (saver){
                                            save = 1;
                                        }

                                        mix.setFlag("false");
                                        mix.setCredit(0);
                                        mix.setCash(0);
                                        mix.setMpesa(0);
                                        mix.setBank(0);
                                        mix.setMpesaFee(0);
                                        mix.setBankFee(0);

                                        mdb.updateMix(mix);

                                        ldb.addLoan(new Loans(creditor.getText().toString() + "-SAV", globalTotal,
                                                pay.toString(), dateView.getText().toString(), dueDate.getText().toString(),
                                                instalmentTotal, Integer.parseInt(instalmentNo.getText().toString()),
                                                save, "", "NOT"));
                                    } else {
                                        cdb.updateSingleCash(paymentMethode, globalTotal);
                                        //Inserting row
                                        int save = 0;

                                        if (saver){
                                            save = 1;
                                        }

                                        ldb.addLoan(new Loans(creditor.getText().toString() + "-SAV", globalTotal,
                                                paymentMethode, dateView.getText().toString(), dueDate.getText().toString(),
                                                instalmentTotal, Integer.parseInt(instalmentNo.getText().toString()),
                                                save, "", "NOT"));
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

    public void setLastDate(View view) {
        showDialog(997);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        } if (id == 998) {
            return new DatePickerDialog(this,
                    myDueDateListener, year, month, day);
        } if (id == 997) {
            return new DatePickerDialog(this,
                    myLateDateListener, year, month, day);
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

    private DatePickerDialog.OnDateSetListener myLateDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showLateDate(arg1, arg2+1, arg3);
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

    private void showLateDate(int year, int month, int day) {
        lastDate.setText(new StringBuilder().append(day).append("/")
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
                String cstArray[] = (total.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                globalTotal = Integer.parseInt(cst.toString());
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
                String cstArray[] = (total.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                globalTotal = Integer.parseInt(cst.toString());
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

            if (!(total.getText()).toString().matches("") && fromMix) {
                String cstArray[] = (total.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                globalTotal = Integer.parseInt(cst.toString());
                total.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            total.addTextChangedListener(commaSeparator);

            instalment.removeTextChangedListener(commaSeparator);

            if (!(instalment.getText()).toString().matches("")) {
                String cstArray[] = (instalment.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                instalmentTotal = Integer.parseInt(cst.toString());
                instalment.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            instalment.addTextChangedListener(commaSeparator);

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

            paidLoan.removeTextChangedListener(commaSeparator);

            if (!(paidLoan.getText()).toString().matches("")) {
                String cstArray[] = (paidLoan.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                paidLoanTotal = Integer.parseInt(cst.toString());
                paidLoan.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            paidLoan.addTextChangedListener(commaSeparator);
        }

        @Override
        public void afterTextChanged(Editable s) {
            total.setSelection(total.getText().length());
            instalment.setSelection(instalment.getText().length());
            savings.setSelection(savings.getText().length());
            paidLoan.setSelection(paidLoan.getText().length());
        }
    };

    /**
     * end of text watcher
     **/
}
