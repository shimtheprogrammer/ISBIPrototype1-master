package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.prototype.isbi.isbiprototype1.databaseHandlers.Delivery;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Owner;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Purchase;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 1/18/2017.
 */

public class PaymentActivity extends Activity {
    public static String TAG = "PaymentActivity";

//    public int _id;
//    public String _from, _type;

    private Calendar calendar;
    private int year, month, day;

    TextView dueDate, dueDateText, dateView;

    EditText total, fee;
    Button submit, back, btnDueDate, btnDate;
    LinearLayout feeLL;
    Spinner payment;

    String paymentMeth = "";
    public int globalTotal = 0, feeTotal = 0;
    public boolean fromMix = true;

    ReceivablesHandler rdb = new ReceivablesHandler(this);
    PayablesHandler pdb = new PayablesHandler(this);
    PaidPayablesHandler ppdb = new PaidPayablesHandler(this);
    PaidReceivablesHandler prdb = new PaidReceivablesHandler(this);
    CashHandler cdb = new CashHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    MixHandler mdb = new MixHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .6), (int) (height * .6));

        final String  from = getIntent().getStringExtra("FROM");
        final String  type = getIntent().getStringExtra("TYPE");
        final int idSent = Integer.parseInt(getIntent().getStringExtra("ID"));

//        String addText = getIntent().getStringExtra("ID");
//        final int id = Integer.parseInt(addText);
//        _id = id;

        total = (EditText) findViewById(R.id.total);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

//        total.addTextChangedListener(totalWatcher);
        fee.addTextChangedListener(comma2);

        if (type.matches("FULL")) {

            if (from.matches("REC")) {
                Receivable receivable = rdb.getReceivable(idSent);
                total.setText("" + numberFormat.format(receivable.getTotal()));
                globalTotal = receivable.getTotal();

            } else if(from.matches("PAY")){
                Payable payable = pdb.getPayable(idSent);
                total.setText("" + numberFormat.format(payable.getTotal()));
                globalTotal = payable.getTotal();

            }

            total.setEnabled(false);

        } else if (type.matches("PART")) {
            total.addTextChangedListener(totalWatcher);
            total.addTextChangedListener(commaSeparator);

        }

        submit = (Button) findViewById(R.id.btn_submit);
        back = (Button) findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.matches("REC")) {
                    Intent intent = new Intent(getApplicationContext(), ReceivablesDisplayActivity.class);
                    startActivity(intent);
                    finish();
                } else if(from.matches("PAY")){
                    Intent intent = new Intent(getApplicationContext(), PayablesDisplayActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

//                int globalTotal = Integer.parseInt(total.getText().toString());

                if(total.getText().toString().matches("")){
                    Toast.makeText(PaymentActivity.this, "Total was empty", Toast.LENGTH_LONG).show();
                } else if (paymentMeth.matches("Cash") && (globalTotal > cash) && !(from.matches("REC"))) {
                    Log.d("Values: ", "actual too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Cash Available",
                            Toast.LENGTH_LONG).show();
                } else if (paymentMeth.matches("MPESA") && (globalTotal > mpesa) && !(from.matches("REC"))) {
                    Log.d("Values: ", "mpesa too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough MPESA Available",
                            Toast.LENGTH_LONG).show();
                } else if (paymentMeth.matches("Bank Transfer") && (globalTotal > bank) && !(from.matches("REC"))) {
                    Log.d("Values: ", "Bank Account too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Money in Bank A/C Available",
                            Toast.LENGTH_LONG).show();
                }  else {

                    if (type.matches("FULL")) {
                        if (from.matches("REC")) {
                            if(!fromMix){
                                if (mix.getCash() > 0) {
                                    cdb.updateSingleCash("Cash", mix.getCash());

                                }

                                if (mix.getBank() > 0) {
                                    cdb.updateSingleCash("Bank Transfer", mix.getBank());

                                }

                                if (mix.getMpesa() > 0) {
                                    cdb.updateSingleCash("MPESA", mix.getMpesa());

                                }

                                Receivable receivable = rdb.getReceivable(idSent);

                                PaidReceivable paidReceivable = new PaidReceivable(receivable.getFrom(), receivable.getTotal(),
                                        dateView.getText().toString(), receivable.getOriginalDate(), paymentMeth);

                                prdb.addPaidReceivable(paidReceivable);

                                rdb.deleteReceivable(rdb.getReceivable(idSent));

                                mix.setFlag("false");
                                mix.setCredit(0);
                                mix.setCash(0);
                                mix.setMpesa(0);
                                mix.setBank(0);
                                mix.setMpesaFee(0);
                                mix.setBankFee(0);

                                mdb.updateMix(mix);

                                Intent intent = new Intent(getApplicationContext(), ReceivablesDisplayActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Receivable receivable = rdb.getReceivable(idSent);

                                PaidReceivable paidReceivable = new PaidReceivable(receivable.getFrom(), receivable.getTotal(),
                                        dateView.getText().toString(), receivable.getOriginalDate(), paymentMeth);

                                prdb.addPaidReceivable(paidReceivable);

                                rdb.deleteReceivable(rdb.getReceivable(idSent));

                                cdb.updateSingleCash(paymentMeth, globalTotal);

                                Intent intent = new Intent(getApplicationContext(), ReceivablesDisplayActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else if(from.matches("PAY")){

                            if(!fromMix) {
                                if (mix.getCash() > 0) {
                                    cdb.updateSingleCashPurchase("Cash", mix.getCash());

                                }

                                if (mix.getBank() > 0) {
                                    cdb.updateSingleCashPurchase("Bank Transfer", mix.getBank());

                                    if (mix.getBankFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMeth,
                                                ("Payable " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                    }
                                }

                                if (mix.getMpesa() > 0) {
                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());

                                    if (mix.getMpesaFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMeth,
                                                ("Payable " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                    }
                                }

                                Payable payable = pdb.getPayable(idSent);

                                PaidPayable paidPayable = new PaidPayable(payable.getTo(), payable.getTotal(),
                                        dateView.getText().toString(), payable.getOriginalDate(), paymentMeth);

                                ppdb.addPaidPayable(paidPayable);

                                pdb.deletepayable(pdb.getPayable(idSent));

                                cdb.updateSingleCashPurchase(paymentMeth, globalTotal);

                                mix.setFlag("false");
                                mix.setCredit(0);
                                mix.setCash(0);
                                mix.setMpesa(0);
                                mix.setBank(0);
                                mix.setMpesaFee(0);
                                mix.setBankFee(0);

                                mdb.updateMix(mix);

                                Intent intent = new Intent(getApplicationContext(), PayablesDisplayActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Payable payable = pdb.getPayable(idSent);

                                PaidPayable paidPayable = new PaidPayable(payable.getTo(), payable.getTotal(),
                                        dateView.getText().toString(), payable.getOriginalDate(), paymentMeth);

                                ppdb.addPaidPayable(paidPayable);

                                pdb.deletepayable(pdb.getPayable(idSent));

                                cdb.updateSingleCashPurchase(paymentMeth, globalTotal);

                                if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                                    String feeName = "MPESA";

                                    if (paymentMeth.matches("Bank Transfer")) {
                                        feeName = "Bank A/C";
                                    }

                                    edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMeth,
                                            ("Payable " + feeName + " Transfer"), dateView.getText().toString()));
                                    cdb.updateSingleCashPurchase(paymentMeth, feeTotal);
                                }

                                Intent intent = new Intent(getApplicationContext(), PayablesDisplayActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    } else if(type.matches("PART")){

                        if (from.matches("REC")) {
                            if(!fromMix){
                                if (mix.getCash() > 0) {
                                    cdb.updateSingleCash("Cash", mix.getCash());

                                }

                                if (mix.getBank() > 0) {
                                    cdb.updateSingleCash("Bank Transfer", mix.getBank());

                                }

                                if (mix.getMpesa() > 0) {
                                    cdb.updateSingleCash("MPESA", mix.getMpesa());

                                }

                                Receivable receivable = rdb.getReceivable(idSent);

                                PaidReceivable paidReceivable = new PaidReceivable(receivable.getFrom(), globalTotal,
                                        dateView.getText().toString(), receivable.getOriginalDate(), paymentMeth);

                                prdb.addPaidReceivable(paidReceivable);

                                if (globalTotal == receivable.getTotal()) {
                                    rdb.deleteReceivable(rdb.getReceivable(idSent));

                                } else {

                                    receivable.setDate(dueDate.getText().toString());
                                    receivable.setTotal(receivable.getTotal() - globalTotal);

                                    rdb.updateReceivable(receivable);
                                }

                                mix.setFlag("false");
                                mix.setCredit(0);
                                mix.setCash(0);
                                mix.setMpesa(0);
                                mix.setBank(0);
                                mix.setMpesaFee(0);
                                mix.setBankFee(0);

                                mdb.updateMix(mix);

                                Intent intent = new Intent(getApplicationContext(), ReceivablesDisplayActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Receivable receivable = rdb.getReceivable(idSent);

                                PaidReceivable paidReceivable = new PaidReceivable(receivable.getFrom(), globalTotal,
                                        dateView.getText().toString(), receivable.getOriginalDate(), paymentMeth);

                                prdb.addPaidReceivable(paidReceivable);

                                if (globalTotal == receivable.getTotal()) {
                                    rdb.deleteReceivable(rdb.getReceivable(idSent));

                                } else {
                                    receivable.setDate(dueDate.getText().toString());
                                    receivable.setTotal(receivable.getTotal() - globalTotal);

                                    rdb.updateReceivable(receivable);
                                }

                                cdb.updateSingleCash(paymentMeth, globalTotal);

                                Intent intent = new Intent(getApplicationContext(), ReceivablesDisplayActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else if(from.matches("PAY")){
                            if(!fromMix) {
                                if (mix.getCash() > 0) {
                                    cdb.updateSingleCashPurchase("Cash", mix.getCash());

                                }

                                if (mix.getBank() > 0) {
                                    cdb.updateSingleCashPurchase("Bank Transfer", mix.getBank());

                                    if (mix.getBankFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMeth,
                                                ("Payable " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());
                                    }
                                }

                                if (mix.getMpesa() > 0) {
                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());

                                    if (mix.getMpesaFee() > 0) {
                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMeth,
                                                ("Payable " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                        cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());
                                    }
                                }

                                Payable payable = pdb.getPayable(idSent);

                                PaidPayable paidPayable = new PaidPayable(payable.getTo(), globalTotal,
                                        dateView.getText().toString(), payable.getOriginalDate(), paymentMeth);

                                ppdb.addPaidPayable(paidPayable);

                                if (globalTotal == payable.getTotal()) {
                                    pdb.deletepayable(pdb.getPayable(idSent));

                                } else {
                                    payable.setDate(dueDate.getText().toString());
                                    payable.setTotal(payable.getTotal() - globalTotal);

                                    pdb.updatePayable(payable);
                                }

                                mix.setFlag("false");
                                mix.setCredit(0);
                                mix.setCash(0);
                                mix.setMpesa(0);
                                mix.setBank(0);
                                mix.setMpesaFee(0);
                                mix.setBankFee(0);

                                mdb.updateMix(mix);

                                Intent intent = new Intent(getApplicationContext(), PayablesDisplayActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Payable payable = pdb.getPayable(idSent);

                                PaidPayable paidPayable = new PaidPayable(payable.getTo(), globalTotal,
                                        dateView.getText().toString(), payable.getOriginalDate(), paymentMeth);

                                ppdb.addPaidPayable(paidPayable);

                                if (globalTotal == payable.getTotal()) {
                                    pdb.deletepayable(pdb.getPayable(idSent));
                                } else {

                                    payable.setDate(dueDate.getText().toString());
                                    payable.setTotal(payable.getTotal() - globalTotal);

                                    pdb.updatePayable(payable);
                                }

                                cdb.updateSingleCashPurchase(paymentMeth, globalTotal);

                                if (!(fee.getText().toString().matches("")) && feeTotal > 0) {
                                    String feeName = "MPESA";

                                    if (paymentMeth.matches("Bank Transfer")) {
                                        feeName = "Bank A/C";
                                    }

                                    edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMeth,
                                            ("Payable " + feeName + " Transfer"), dateView.getText().toString()));
                                    cdb.updateSingleCashPurchase(paymentMeth, feeTotal);
                                }

                                Intent intent = new Intent(getApplicationContext(), PayablesDisplayActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            }
        });

        /**
         * setting spinner and date
         **/
        payment = (Spinner) findViewById(R.id.payment);
        dueDate = (TextView) findViewById(R.id.due_date);
        btnDueDate = (Button) findViewById(R.id.btn_due_date);
        btnDate = (Button) findViewById(R.id.btn_date);
        dateView = (TextView) findViewById(R.id.date);
        dueDateText = (TextView) findViewById(R.id.due_date_text);

        if (type.matches("PART")) {
            dueDate.setVisibility(View.VISIBLE);
            btnDueDate.setVisibility(View.VISIBLE);
            dueDateText.setVisibility(View.VISIBLE);
        }

        final List<String> paymentMethod = new ArrayList<String>();
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

        payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                              @Override
                                              public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                                  // On selecting a spinner item
                                                  String item = parent.getItemAtPosition(position).toString();
                                                  paymentMeth = item;

                                                  feeTotal = 0;
                                                  fee.setText("");

                                                  Mix mix = mdb.getMix(1);

                                                  LinearLayout.LayoutParams show =
                                                          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                  ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                                                  LinearLayout.LayoutParams hide =
                                                          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                  0, 0);

                                                  if(!(paymentMeth.matches("MPESA") || paymentMeth.matches("Bank Transfer"))){
                                                      feeLL.setLayoutParams(hide);

                                                  } else if(from.matches("PAY")){
                                                      feeLL.setLayoutParams(show);

                                                  } else {
                                                      feeLL.setLayoutParams(hide);
                                                  }

                                                  if(paymentMeth.matches("Mix")){
                                                      if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                                                          Log.d(TAG, "totals was null..");
                                                          total.setError("No amount set");
                                                          total.requestFocus();
                                                          payment.setSelection(0);

                                                      } else {
                                                          Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                                          if(from.matches("PAY")) {
                                                              intent.putExtra("FROM", "PAY");
                                                              intent.putExtra("CREDIT", "NO");

                                                              intent.putExtra("TOTAL", "" + (globalTotal));

                                                              startActivity(intent);


                                                          } else if(from.matches("REC")) {
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

                                                      if (paymentMeth.matches("MPESA") && from.matches("PAY")) {
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


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDueDate(year, month+1, day);

        /**
         * ens of spiner and date begining of list
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

            if (paymentMeth.matches("MPESA") && from.matches("PAY")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            fromMix = true;

            if(paymentMeth.matches("Mix")) {
                payment.setSelection(0);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        final String  from = getIntent().getStringExtra("FROM");

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

            if (paymentMeth.matches("MPESA") && from.matches("PAY")) {
                int feeSuj = mpesaFee(globalTotal);

                fee.setText("" + feeSuj);
                feeTotal = feeSuj;
            }

            fromMix = true;

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
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myDueDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
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

            if(!(total.getText().toString().matches(""))) {
                final String from = getIntent().getStringExtra("FROM");
                if (from.matches("REC")) {
                    int tot = rdb.getReceivable(Integer.parseInt(getIntent().getStringExtra("ID"))).getTotal();
                    if (globalTotal > tot) {
                        total.setText("" + tot);

                    }
                } else if (from.matches("PAY")) {
                    int tot = pdb.getPayable(Integer.parseInt(getIntent().getStringExtra("ID"))).getTotal();
                    if (globalTotal > tot) {
                        total.setText("" + tot);

                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!(total.getText().toString().matches(""))) {
                final String from = getIntent().getStringExtra("FROM");
                if (from.matches("REC")) {
                    int tot = rdb.getReceivable(Integer.parseInt(getIntent().getStringExtra("ID"))).getTotal();
                    if (globalTotal > tot) {
                        total.setText("" + tot);

                    }
                } else if (from.matches("PAY")) {
                    int tot = pdb.getPayable(Integer.parseInt(getIntent().getStringExtra("ID"))).getTotal();
                    if (globalTotal > tot) {
                        total.setText("" + tot);

                    }
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

            if(paymentMeth.matches("Mix")) {
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

                if(paymentMeth.matches("MPESA")) {
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
