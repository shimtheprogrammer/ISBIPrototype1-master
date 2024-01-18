package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;

import java.text.NumberFormat;

/**
 * Created by MRuto on 1/23/2017.
 */

public class MixPaymentActivity extends Activity {
    public static String TAG = "MixPaymentActivity";

    TextView title;
    EditText actual, credit, bank, mpesa, bankfee, mpesafee;
    Button done, back;
    LinearLayout mpesaLL, mpesaFeeLL, bankLL, bankFeeLL, creditLL;

    int cashTotal = 0, creditTotal = 0, bankTotal = 0, mpesaTotal = 0, bankFeeTotal = 0, mpesaFeeTotal = 0;

    MixHandler mdb = new MixHandler(this);
    CashHandler cdb = new CashHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_payment);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;

        getWindow().setLayout(350, 350);

        title = (TextView) findViewById(R.id.title_text);
        actual = (EditText) findViewById(R.id.cash);
        credit = (EditText) findViewById(R.id.credit);
        bank = (EditText) findViewById(R.id.bank);
        mpesa = (EditText) findViewById(R.id.mpesa);
        bankfee = (EditText) findViewById(R.id.bankfee);
        mpesafee = (EditText) findViewById(R.id.mpesafee);
        done = (Button) findViewById(R.id.done);
        back = (Button) findViewById(R.id.back);
        mpesaLL =(LinearLayout) findViewById(R.id.mpesa_ll);
        bankLL =(LinearLayout) findViewById(R.id.bank_ll);
        mpesaFeeLL =(LinearLayout) findViewById(R.id.mpesafee_ll);
        bankFeeLL =(LinearLayout) findViewById(R.id.bankfee_ll);
        creditLL =(LinearLayout) findViewById(R.id.credit_ll);

//        credit.setText("" + 0);
//        actual.setText("" + 0);
//        mpesa.setText("" + 0);
//        bank.setText("" + 0);

        actual.addTextChangedListener(totalWatcher);
        credit.addTextChangedListener(totalWatcher);
        bank.addTextChangedListener(totalWatcher);
        mpesa.addTextChangedListener(totalWatcher);
        bankfee.addTextChangedListener(totalWatcher);
        mpesafee.addTextChangedListener(totalWatcher);

        actual.addTextChangedListener(commaSeparator);
        credit.addTextChangedListener(commaSeparator);
        bank.addTextChangedListener(commaSeparator);
        mpesa.addTextChangedListener(commaSeparator);
        bankfee.addTextChangedListener(commaSeparator);
        mpesafee.addTextChangedListener(commaSeparator);

        title.setText("Distribute Total Amount Accordingly: "
                + numberFormat.format(Integer.parseInt(getIntent().getStringExtra("TOTAL"))));

        if((getIntent().getStringExtra("FROM")).matches("EXP")
                || (getIntent().getStringExtra("FROM")).matches("PRES")
                || (getIntent().getStringExtra("FROM")).matches("PREO")
                || (getIntent().getStringExtra("FROM")).matches("OWN")
                || (getIntent().getStringExtra("FROM")).matches("LOA")
                || (getIntent().getStringExtra("FROM")).matches("INS")){
            credit.setEnabled(false);
        }

        LinearLayout.LayoutParams showing =
                new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);


        if ((getIntent().getStringExtra("FROM")).matches("REC")){
            mpesaFeeLL.setLayoutParams(hiding);
            mpesaLL.setLayoutParams(showing);
            bankFeeLL.setLayoutParams(hiding);
            bankLL.setLayoutParams(showing);

        }

        if ((getIntent().getStringExtra("CREDIT")).matches("NO")){
            creditLL.setLayoutParams(hiding);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cash cash = cdb.getCash(1);

                int realActual = 0, realMpesa = 0, realBank = 0;

                realActual = cash.getActual();
                realMpesa = cash.getMpesa();
                realBank = cash.getBank();

                if((getIntent().getStringExtra("FROM")).matches("DEL")){
//                    if (!credit.getText().toString().matches("")) {
//                        creditTotal = (Integer.parseInt(credit.getText().toString()));
//                    }
//                    if (!actual.getText().toString().matches("")) {
//                        cashTotal = Integer.parseInt(actual.getText().toString());
//                    }
//                    if (!mpesa.getText().toString().matches("")) {
//                        mpesaTotal = Integer.parseInt(mpesa.getText().toString());
//                    }
//                    if (!bank.getText().toString().matches("")) {
//                        bankTotal = Integer.parseInt(bank.getText().toString());
//                    }


                    if(Integer.parseInt(getIntent().getStringExtra("TOTAL")) <= 0) {
                        Intent intent = new Intent(getApplicationContext(), Delivery2Activity.class);

                        intent.putExtra("CASH", "" + cashTotal);
                        intent.putExtra("BANK", "" + bankTotal);
                        intent.putExtra("MPESA", "" + mpesaTotal);
                        intent.putExtra("CREDIT", "" + creditTotal);
                        intent.putExtra("FROM", "MIX");

                        intent.putExtra("ALLO", "" + getIntent().getStringExtra("ALLO"));
                        intent.putExtra("SELEC", "" + getIntent().getStringExtra("SELEC"));

                        for (int i = 0; i < Integer.parseInt(getIntent().getStringExtra("SELEC")); i++) {
                            Log.d("sent FROM mix", "" + getIntent().getStringExtra("ID" + i));// + " found " + selected.get(i));
                            intent.putExtra("ID" + i, "" + getIntent().getStringExtra("ID" + i));
                        }

                        int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                        intent.putExtra("TOTAL", "" + globalTotal);

                        startActivity(intent);
                        finish();
                    } else {

                        if (credit.getText().toString().matches("") && actual.getText().toString().matches("")
                                && mpesa.getText().toString().matches("") && bank.getText().toString().matches("")) {
                            Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0",
                                    Toast.LENGTH_LONG).show();
                        } else if (creditTotal <= 0 && cashTotal <= 0 && mpesaTotal <= 0 && bankTotal <= 0) {
                            Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0",
                                    Toast.LENGTH_LONG).show();
                        } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                                > Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                            Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +
                                    getIntent().getStringExtra("TOTAL"), Toast.LENGTH_LONG).show();
                        } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                                < Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                            Toast.makeText(MixPaymentActivity.this, "The Total " + getIntent().getStringExtra("TOTAL")
                                    + " has not been accounted for", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Delivery2Activity.class);

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            intent.putExtra("ALLO", "" + getIntent().getStringExtra("ALLO"));
                            intent.putExtra("SELEC", "" + getIntent().getStringExtra("SELEC"));

                            for (int i = 0; i < Integer.parseInt(getIntent().getStringExtra("SELEC")); i++) {
                                Log.d("sent FROM mix", "" + getIntent().getStringExtra("ID" + i));// + " found " + selected.get(i));
                                intent.putExtra("ID" + i, "" + getIntent().getStringExtra("ID" + i));
                            }

                            int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                            intent.putExtra("TOTAL", "" + globalTotal);

                            startActivity(intent);
                            finish();
                        }

                    }

                } else if((getIntent().getStringExtra("FROM")).matches("EXP")
                        || (getIntent().getStringExtra("FROM")).matches("ASS")
                        || (getIntent().getStringExtra("FROM")).matches("SAS")
                        || (getIntent().getStringExtra("FROM")).matches("LOA")
                        || (getIntent().getStringExtra("FROM")).matches("INS")){
//                    if (!credit.getText().toString().matches("")) {
//                        creditTotal = (Integer.parseInt(credit.getText().toString()));
//                    }
//                    if (!actual.getText().toString().matches("")) {
//                        cashTotal = Integer.parseInt(actual.getText().toString());
//                    }
//                    if (!mpesa.getText().toString().matches("")) {
//                        mpesaTotal = Integer.parseInt(mpesa.getText().toString());
//                    }
//                    if (!bank.getText().toString().matches("")) {
//                        bankTotal = Integer.parseInt(bank.getText().toString());
//                    }


                    if(Integer.parseInt(getIntent().getStringExtra("TOTAL")) <= 0) {
                        if((getIntent().getStringExtra("FROM")).matches("EXP")) {
                            Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            intent.putExtra("TYPE", "" + getIntent().getStringExtra("TYPE"));
                            intent.putExtra("NAME", "" + getIntent().getStringExtra("NAME"));
                            intent.putExtra("INFO", "" + getIntent().getStringExtra("INFO"));
                            intent.putExtra("PERIOD", "" + getIntent().getStringExtra("PERIOD"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));

                            int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                            intent.putExtra("TOTAL", "" + globalTotal);

                            startActivity(intent);
                            finish();
                        } else if((getIntent().getStringExtra("FROM")).matches("ASS")){
                            Intent intent = new Intent(getApplicationContext(), AssetsActivity.class);

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            intent.putExtra("TYPE", "" + getIntent().getStringExtra("TYPE"));
                            intent.putExtra("USEFULL", ""+ getIntent().getStringExtra("USEFULL"));
                            intent.putExtra("SCRAP", ""+ getIntent().getStringExtra("SCRAP"));
                            intent.putExtra("INFO", ""+ getIntent().getStringExtra("INFO"));
                            intent.putExtra("SUPPLIER", ""+ getIntent().getStringExtra("SUPPLIER"));
                            intent.putExtra("DATE", ""+ getIntent().getStringExtra("DATE"));
                            intent.putExtra("DUEDATE", ""+ getIntent().getStringExtra("DUEDATE"));

                            int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                            intent.putExtra("TOTAL", "" + globalTotal);

                            startActivity(intent);
                            finish();
                        } else if ((getIntent().getStringExtra("FROM")).matches("SAS")) {
                            Intent intent = new Intent(getApplicationContext(), SoldAssetActivity.class);

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            intent.putExtra("INFO", "" + getIntent().getStringExtra("INFO"));
                            intent.putExtra("SUPPLIER", "" + getIntent().getStringExtra("SUPPLIER"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));
                            intent.putExtra("DUEDATE", "" + getIntent().getStringExtra("DUEDATE"));
                            intent.putExtra("HOW", ""+ getIntent().getStringExtra("HOW"));
                            intent.putExtra("ID", ""+ getIntent().getStringExtra("ID"));
                            intent.putExtra("WHAT", ""+ getIntent().getStringExtra("WHAT"));

                            int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                            intent.putExtra("TOTAL", "" + globalTotal);

                            startActivity(intent);
                            finish();
                        } else if ((getIntent().getStringExtra("FROM")).matches("LOA")) {
                            Intent intent = new Intent(getApplicationContext(), LoansActivity.class);

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            intent.putExtra("NAME", "" + getIntent().getStringExtra("NAME"));
                            intent.putExtra("INSTAL", "" + getIntent().getStringExtra("INSTAL"));
                            intent.putExtra("INSTALNO", "" + getIntent().getStringExtra("INSTALNO"));
                            intent.putExtra("SAVE", "" + getIntent().getStringExtra("SAVE"));
                            intent.putExtra("SDATE", "" + getIntent().getStringExtra("SDATE"));
                            intent.putExtra("EDATE", "" + getIntent().getStringExtra("EDATE"));

                            int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                            intent.putExtra("TOTAL", "" + globalTotal);

                            startActivity(intent);
                            finish();
                        } else if ((getIntent().getStringExtra("FROM")).matches("INS")) {
                            Intent intent = new Intent(getApplicationContext(), InstalmentActivity.class);

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");
                            intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                            intent.putExtra("WHAT", "" + getIntent().getStringExtra("WHAT"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));

                            int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                            intent.putExtra("TOTAL", "" + globalTotal);

                            startActivity(intent);
                            finish();
                        }
                    } else {

                        if (credit.getText().toString().matches("") && actual.getText().toString().matches("")
                                && mpesa.getText().toString().matches("") && bank.getText().toString().matches("")) {
                            Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                        } else if (creditTotal <= 0 && cashTotal <= 0 && mpesaTotal <= 0 && bankTotal <= 0) {
                            Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                        } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                                > Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                            Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +  getIntent().getStringExtra("TOTAL"),
                                    Toast.LENGTH_LONG).show();
                        } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                                < Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                            Toast.makeText(MixPaymentActivity.this, "The Total " + getIntent().getStringExtra("TOTAL")
                                    + " has not been accounted for", Toast.LENGTH_LONG).show();
                        } else {
                            if ((getIntent().getStringExtra("FROM")).matches("EXP")) {
                                Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);

                                intent.putExtra("CASH", "" + cashTotal);
                                intent.putExtra("BANK", "" + bankTotal);
                                intent.putExtra("MPESA", "" + mpesaTotal);
                                intent.putExtra("CREDIT", "" + creditTotal);
                                intent.putExtra("FROM", "MIX");

                                intent.putExtra("TYPE", "" + getIntent().getStringExtra("TYPE"));
                                intent.putExtra("NAME", "" + getIntent().getStringExtra("NAME"));
                                intent.putExtra("INFO", "" + getIntent().getStringExtra("INFO"));
                                intent.putExtra("PERIOD", "" + getIntent().getStringExtra("PERIOD"));
                                intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));

                                int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                                intent.putExtra("TOTAL", "" + globalTotal);

                                startActivity(intent);
                                finish();
                            } else if ((getIntent().getStringExtra("FROM")).matches("ASS")) {
                                Intent intent = new Intent(getApplicationContext(), AssetsActivity.class);

                                intent.putExtra("CASH", "" + cashTotal);
                                intent.putExtra("BANK", "" + bankTotal);
                                intent.putExtra("MPESA", "" + mpesaTotal);
                                intent.putExtra("CREDIT", "" + creditTotal);
                                intent.putExtra("FROM", "MIX");

                                intent.putExtra("TYPE", "" + getIntent().getStringExtra("TYPE"));
                                intent.putExtra("USEFULL", "" + getIntent().getStringExtra("USEFULL"));
                                intent.putExtra("SCRAP", "" + getIntent().getStringExtra("SCRAP"));
                                intent.putExtra("INFO", "" + getIntent().getStringExtra("INFO"));
                                intent.putExtra("SUPPLIER", "" + getIntent().getStringExtra("SUPPLIER"));
                                intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));
                                intent.putExtra("DUEDATE", "" + getIntent().getStringExtra("DUEDATE"));

                                int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                                intent.putExtra("TOTAL", "" + globalTotal);

                                startActivity(intent);
                                finish();
                            } else if ((getIntent().getStringExtra("FROM")).matches("SAS")) {
                                Intent intent = new Intent(getApplicationContext(), SoldAssetActivity.class);

                                intent.putExtra("CASH", "" + cashTotal);
                                intent.putExtra("BANK", "" + bankTotal);
                                intent.putExtra("MPESA", "" + mpesaTotal);
                                intent.putExtra("CREDIT", "" + creditTotal);
                                intent.putExtra("FROM", "MIX");

                                intent.putExtra("INFO", "" + getIntent().getStringExtra("INFO"));
                                intent.putExtra("SUPPLIER", "" + getIntent().getStringExtra("SUPPLIER"));
                                intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));
                                intent.putExtra("DUEDATE", "" + getIntent().getStringExtra("DUEDATE"));
                                intent.putExtra("HOW", ""+ getIntent().getStringExtra("HOW"));
                                intent.putExtra("ID", ""+ getIntent().getStringExtra("ID"));
                                intent.putExtra("WHAT", ""+ getIntent().getStringExtra("WHAT"));

                                int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                                intent.putExtra("TOTAL", "" + globalTotal);

                                startActivity(intent);
                                finish();
                            } else if ((getIntent().getStringExtra("FROM")).matches("LOA")) {
                                Intent intent = new Intent(getApplicationContext(), LoansActivity.class);

                                intent.putExtra("CASH", "" + cashTotal);
                                intent.putExtra("BANK", "" + bankTotal);
                                intent.putExtra("MPESA", "" + mpesaTotal);
                                intent.putExtra("CREDIT", "" + creditTotal);
                                intent.putExtra("FROM", "MIX");

                                intent.putExtra("NAME", "" + getIntent().getStringExtra("NAME"));
                                intent.putExtra("INSTAL", "" + getIntent().getStringExtra("INSTAL"));
                                intent.putExtra("INSTALNO", "" + getIntent().getStringExtra("INSTALNO"));
                                intent.putExtra("SAVE", "" + getIntent().getStringExtra("SAVE"));
                                intent.putExtra("SDATE", "" + getIntent().getStringExtra("SDATE"));
                                intent.putExtra("EDATE", "" + getIntent().getStringExtra("EDATE"));

                                int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                                intent.putExtra("TOTAL", "" + globalTotal);

                                startActivity(intent);
                                finish();
                            } else if ((getIntent().getStringExtra("FROM")).matches("INS")) {
                                Intent intent = new Intent(getApplicationContext(), InstalmentActivity.class);

                                intent.putExtra("CASH", "" + cashTotal);
                                intent.putExtra("BANK", "" + bankTotal);
                                intent.putExtra("MPESA", "" + mpesaTotal);
                                intent.putExtra("CREDIT", "" + creditTotal);
                                intent.putExtra("FROM", "MIX");
                                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                                intent.putExtra("WHAT", "" + getIntent().getStringExtra("WHAT"));
                                intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));

                                int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                                intent.putExtra("TOTAL", "" + globalTotal);

                                startActivity(intent);
                                finish();
                            }
                        }

                    }

                } else if((getIntent().getStringExtra("FROM")).matches("OWN")){
//                    if (!credit.getText().toString().matches("")) {
//                        creditTotal = (Integer.parseInt(credit.getText().toString()));
//                    }
//                    if (!actual.getText().toString().matches("")) {
//                        cashTotal = Integer.parseInt(actual.getText().toString());
//                    }
//                    if (!mpesa.getText().toString().matches("")) {
//                        mpesaTotal = Integer.parseInt(mpesa.getText().toString());
//                    }
//                    if (!bank.getText().toString().matches("")) {
//                        bankTotal = Integer.parseInt(bank.getText().toString());
//                    }


                    if(Integer.parseInt(getIntent().getStringExtra("TOTAL")) <= 0) {
                        Intent intent = new Intent(getApplicationContext(), OwnerActivity.class);

                        intent.putExtra("CASH", "" + cashTotal);
                        intent.putExtra("BANK", "" + bankTotal);
                        intent.putExtra("MPESA", "" + mpesaTotal);
                        intent.putExtra("CREDIT", "" + creditTotal);
                        intent.putExtra("FROM", "MIX");

                        intent.putExtra("TYPE", "" + getIntent().getStringExtra("TYPE"));
                        intent.putExtra("INFO", "" + getIntent().getStringExtra("INFO"));
                        intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));

                        int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                        intent.putExtra("TOTAL", "" + globalTotal);

                        startActivity(intent);
                        finish();
                    } else {

                        if (credit.getText().toString().matches("") && actual.getText().toString().matches("")
                                && mpesa.getText().toString().matches("") && bank.getText().toString().matches("")) {
                            Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                        } else if (creditTotal <= 0 && cashTotal <= 0 && mpesaTotal <= 0 && bankTotal <= 0) {
                            Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                        } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                                > Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                            Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +  getIntent().getStringExtra("TOTAL"),
                                    Toast.LENGTH_LONG).show();
                        } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                                < Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                            Toast.makeText(MixPaymentActivity.this, "The Total " + getIntent().getStringExtra("TOTAL")
                                    + " has not been accounted for", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), OwnerActivity.class);

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            intent.putExtra("TYPE", "" + getIntent().getStringExtra("TYPE"));
                            intent.putExtra("INFO", "" + getIntent().getStringExtra("INFO"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));

                            int globalTotal = bankTotal + creditTotal + mpesaTotal + cashTotal;

                            intent.putExtra("TOTAL", "" + globalTotal);

                            startActivity(intent);
                            finish();
                        }

                    }

                } else if ((getIntent().getStringExtra("FROM")).matches("PAY")){
                    if (credit.getText().toString().matches("") && actual.getText().toString().matches("")
                            && mpesa.getText().toString().matches("") && bank.getText().toString().matches("")) {
                        Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                    } else if (creditTotal <= 0 && cashTotal <= 0 && mpesaTotal <= 0 && bankTotal <= 0) {
                        Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                    } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                            > Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                        Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +  getIntent().getStringExtra("TOTAL"),
                                Toast.LENGTH_LONG).show();
                    } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                            < Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                        Toast.makeText(MixPaymentActivity.this, "The Total " + getIntent().getStringExtra("TOTAL")
                                + " has not been accounted for", Toast.LENGTH_LONG).show();
                    } else if (cashTotal > realActual) {
                        Log.d(TAG, "too litle actual cash");
                        actual.setError("Not Enough Cash in Till");
                        actual.requestFocus();

                    } else if ((mpesaFeeTotal + mpesaTotal) > realMpesa) {
                        Log.d(TAG, "too litle mpesa");
                        mpesa.setError("Not Enough M-PESA Balance");
                        mpesa.requestFocus();

                    } else if ((bankTotal + bankFeeTotal) > realBank) {
                        Log.d(TAG, "too litle bank");
                        bank.setError("Not Enough Money in Bank A/C");
                        bank.requestFocus();

                    } else if (mpesaFeeTotal > mpesaTotal) {
                        Log.d(TAG, "fee too high");
                        mpesafee.setError("Transaction fee cannot be greater than actual amount");
                        mpesafee.requestFocus();

                    } else if (bankFeeTotal > bankTotal) {
                        Log.d(TAG, "fee too high");
                        bankfee.setError("Transaction fee cannot be greater than actual amount");
                        bankfee.requestFocus();

                    } else {
                        Mix mix = mdb.getMix(1);

                        mix.setFlag("true");
                        mix.setCredit(creditTotal);
                        mix.setCash(cashTotal);
                        mix.setMpesa(mpesaTotal);
                        mix.setBank(bankTotal);
                        mix.setMpesaFee(mpesaFeeTotal);
                        mix.setBankFee(bankFeeTotal);

                        mdb.updateMix(mix);

                        finish();
                    }

                } else if ((getIntent().getStringExtra("FROM")).matches("REC")){
                    if (credit.getText().toString().matches("") && actual.getText().toString().matches("")
                            && mpesa.getText().toString().matches("") && bank.getText().toString().matches("")) {
                        Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                    } else if (creditTotal <= 0 && cashTotal <= 0 && mpesaTotal <= 0 && bankTotal <= 0) {
                        Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                    } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                            > Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                        Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +  getIntent().getStringExtra("TOTAL"),
                                Toast.LENGTH_LONG).show();
                    } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                            < Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                        Toast.makeText(MixPaymentActivity.this, "The Total " + getIntent().getStringExtra("TOTAL")
                                + " has not been accounted for", Toast.LENGTH_LONG).show();
                    } else {
                        Mix mix = mdb.getMix(1);

                        mix.setFlag("true");
                        mix.setCredit(creditTotal);
                        mix.setCash(cashTotal);
                        mix.setMpesa(mpesaTotal);
                        mix.setBank(bankTotal);
                        mix.setMpesaFee(0);
                        mix.setBankFee(0);

                        mdb.updateMix(mix);

                        finish();
                    }

                } else {
                    if (credit.getText().toString().matches("") && actual.getText().toString().matches("")
                            && mpesa.getText().toString().matches("") && bank.getText().toString().matches("")) {
                        Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                    } else if (creditTotal <= 0 && cashTotal <= 0 && mpesaTotal <= 0 && bankTotal <= 0) {
                        Toast.makeText(MixPaymentActivity.this, "At least one mode of payment must be greater than 0", Toast.LENGTH_LONG).show();
                    } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                            > Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                        Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +  getIntent().getStringExtra("TOTAL"),
                                Toast.LENGTH_LONG).show();
                    } else if ((creditTotal + cashTotal + mpesaTotal + bankTotal)
                            < Integer.parseInt(getIntent().getStringExtra("TOTAL"))) {
                        Toast.makeText(MixPaymentActivity.this, "The Total " + getIntent().getStringExtra("TOTAL")
                                + " has not been accounted for", Toast.LENGTH_LONG).show();
                    } else {

//                        if (!credit.getText().toString().matches("")) {
//                            creditTotal = (Integer.parseInt(credit.getText().toString()));
//                        }
//                        if (!actual.getText().toString().matches("")) {
//                            cashTotal = Integer.parseInt(actual.getText().toString());
//                        }
//                        if (!mpesa.getText().toString().matches("")) {
//                            mpesaTotal = Integer.parseInt(mpesa.getText().toString());
//                        }
//                        if (!bank.getText().toString().matches("")) {
//                            bankTotal = Integer.parseInt(bank.getText().toString());
//                        }

                        if ((getIntent().getStringExtra("FROM")).matches("SALE")) {
                            Intent intent = new Intent(getApplicationContext(), SaleActivity.class);

                            intent.putExtra("CUSTOPMER", "" + getIntent().getStringExtra("CUSTOPMER"));
                            intent.putExtra("NUMBER", "" + getIntent().getStringExtra("NUMBER"));
                            intent.putExtra("DISCOUNT", "" + getIntent().getStringExtra("DISCOUNT"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));
                            intent.putExtra("DUEDATE", "" + getIntent().getStringExtra("DUEDATE"));

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            startActivity(intent);
                            finish();
                        } else if ((getIntent().getStringExtra("FROM")).matches("PUR")) {
                            Intent intent = new Intent(getApplicationContext(), PurchaseActivity.class);

                            intent.putExtra("SUPPLIER", "" + getIntent().getStringExtra("SUPPLIER"));
                            intent.putExtra("NUMBER", "" + getIntent().getStringExtra("NUMBER"));
                            intent.putExtra("DISCOUNT", "" + getIntent().getStringExtra("DISCOUNT"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));
                            intent.putExtra("DUEDATE", "" + getIntent().getStringExtra("DUEDATE"));
                            intent.putExtra("DELIVERY", "" + getIntent().getStringExtra("DELIVERY"));
                            intent.putExtra("NOW", "" + getIntent().getStringExtra("NOW"));
                            intent.putExtra("DELTOT", "" + getIntent().getStringExtra("DELTOT"));
                            intent.putExtra("ALLOCATION", "" + getIntent().getStringExtra("ALLOCATION"));

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            startActivity(intent);
                            finish();
                        } else if ((getIntent().getStringExtra("FROM")).matches("PRES")) {
                            Intent intent = new Intent(getApplicationContext(), PreSaleActivity.class);

                            intent.putExtra("CUSTOPMER", "" + getIntent().getStringExtra("CUSTOPMER"));
                            intent.putExtra("NUMBER", "" + getIntent().getStringExtra("NUMBER"));
                            intent.putExtra("DISCOUNT", "" + getIntent().getStringExtra("DISCOUNT"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));
                            intent.putExtra("DUEDATE", "" + getIntent().getStringExtra("DUEDATE"));

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            startActivity(intent);
                            finish();
                        } else if ((getIntent().getStringExtra("FROM")).matches("PREO")) {
                            Intent intent = new Intent(getApplicationContext(), PreOrderActivity.class);

                            intent.putExtra("SUPPLIER", "" + getIntent().getStringExtra("SUPPLIER"));
                            intent.putExtra("NUMBER", "" + getIntent().getStringExtra("NUMBER"));
                            intent.putExtra("DISCOUNT", "" + getIntent().getStringExtra("DISCOUNT"));
                            intent.putExtra("DATE", "" + getIntent().getStringExtra("DATE"));
                            intent.putExtra("DUEDATE", "" + getIntent().getStringExtra("DUEDATE"));

                            intent.putExtra("CASH", "" + cashTotal);
                            intent.putExtra("BANK", "" + bankTotal);
                            intent.putExtra("MPESA", "" + mpesaTotal);
                            intent.putExtra("CREDIT", "" + creditTotal);
                            intent.putExtra("FROM", "MIX");

                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });
    }

    /**
     * text watcher for getting total
     **/

    private final TextWatcher totalWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (!credit.getText().toString().matches("")) {
//                creditTotal = (Integer.parseInt(credit.getText().toString()));
//            }
//            if (!actual.getText().toString().matches("")) {
//                cashTotal = Integer.parseInt(actual.getText().toString());
//            }
//            if (!mpesa.getText().toString().matches("")) {
//                mpesaTotal = Integer.parseInt(mpesa.getText().toString());
//            }
//            if (!bank.getText().toString().matches("")) {
//                bankTotal = Integer.parseInt(bank.getText().toString());
//            }
//            if (!mpesafee.getText().toString().matches("")) {
//                mpesaFeeTotal = Integer.parseInt(mpesafee.getText().toString());
//            }
//            if (!bankfee.getText().toString().matches("")) {
//                bankFeeTotal = Integer.parseInt(bankfee.getText().toString());
//            }

            if (credit.getText().toString().matches("")) {
                creditTotal = 0;
            }
            if (actual.getText().toString().matches("")) {
                cashTotal = 0;
            }
            if (mpesa.getText().toString().matches("")) {
                mpesaTotal = 0;
            }
            if (bank.getText().toString().matches("")) {
                bankTotal = 0;
            }
            if (mpesafee.getText().toString().matches("")) {
                mpesaFeeTotal = 0;
            }
            if (bankfee.getText().toString().matches("")) {
                bankFeeTotal = 0;
            }

            if( (creditTotal + cashTotal + mpesaTotal + bankTotal)
                    > Integer.parseInt(getIntent().getStringExtra("TOTAL"))
                    && !(getIntent().getStringExtra("FROM")).matches("DEL")
                    && !(getIntent().getStringExtra("FROM")).matches("EXP")
                    && !(getIntent().getStringExtra("FROM")).matches("OWN")
                    && !(getIntent().getStringExtra("FROM")).matches("LOA")
                    && !(getIntent().getStringExtra("FROM")).matches("INS")){
                Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +  getIntent().getStringExtra("TOTAL"),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
//            if (!credit.getText().toString().matches("")) {
//                creditTotal = (Integer.parseInt(credit.getText().toString()));
//            }
//            if (!actual.getText().toString().matches("")) {
//                cashTotal = Integer.parseInt(actual.getText().toString());
//            }
//            if (!mpesa.getText().toString().matches("")) {
//                mpesaTotal = Integer.parseInt(mpesa.getText().toString());
//            }
//            if (!bank.getText().toString().matches("")) {
//                bankTotal = Integer.parseInt(bank.getText().toString());
//            }
//            if (!mpesafee.getText().toString().matches("")) {
//                mpesaFeeTotal = Integer.parseInt(mpesafee.getText().toString());
//            }
//            if (!bankfee.getText().toString().matches("")) {
//                bankFeeTotal = Integer.parseInt(bankfee.getText().toString());
//            }

            if (credit.getText().toString().matches("")) {
                creditTotal = 0;
            }
            if (actual.getText().toString().matches("")) {
                cashTotal = 0;
            }
            if (mpesa.getText().toString().matches("")) {
                mpesaTotal = 0;
            }
            if (bank.getText().toString().matches("")) {
                bankTotal = 0;
            }
            if (mpesafee.getText().toString().matches("")) {
                mpesaFeeTotal = 0;
            }
            if (bankfee.getText().toString().matches("")) {
                bankFeeTotal = 0;
            }

            if( (creditTotal + cashTotal + mpesaTotal + bankTotal)
                    > Integer.parseInt(getIntent().getStringExtra("TOTAL"))
                    && !(getIntent().getStringExtra("FROM")).matches("DEL")
                    && !(getIntent().getStringExtra("FROM")).matches("EXP")
                    && !(getIntent().getStringExtra("FROM")).matches("OWN")
                    && !(getIntent().getStringExtra("FROM")).matches("LOA")
                    && !(getIntent().getStringExtra("FROM")).matches("INS")){
                Toast.makeText(MixPaymentActivity.this, "The Amount Exceds the Total set: " +  getIntent().getStringExtra("TOTAL"),
                        Toast.LENGTH_LONG).show();
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

            credit.removeTextChangedListener(commaSeparator);

            if (!(credit.getText()).toString().matches("")) {
                String cstArray[] = (credit.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                creditTotal = Integer.parseInt(cst.toString());
                credit.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            credit.addTextChangedListener(commaSeparator);

            actual.removeTextChangedListener(commaSeparator);

            if (!(actual.getText()).toString().matches("")) {
                String cstArray[] = (actual.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                cashTotal = Integer.parseInt(cst.toString());
                actual.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            actual.addTextChangedListener(commaSeparator);

            mpesa.removeTextChangedListener(commaSeparator);

            if (!(mpesa.getText()).toString().matches("")) {
                String cstArray[] = (mpesa.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                mpesafee.removeTextChangedListener(commaSeparator);
                int feeSuj = getMpesaFee(Integer.parseInt(cst.toString()));

                mpesafee.setText("" + numberFormat.format(feeSuj));
                mpesaFeeTotal = feeSuj;

                mpesafee.addTextChangedListener(commaSeparator);

                mpesaTotal = Integer.parseInt(cst.toString());
                mpesa.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }

            mpesa.addTextChangedListener(commaSeparator);

            mpesafee.removeTextChangedListener(commaSeparator);

            if (!(mpesafee.getText()).toString().matches("")) {
                String cstArray[] = (mpesafee.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                mpesaFeeTotal = Integer.parseInt(cst.toString());
                mpesafee.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            mpesafee.addTextChangedListener(commaSeparator);

            bank.removeTextChangedListener(commaSeparator);

            if (!(bank.getText()).toString().matches("")) {
                String cstArray[] = (bank.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                bankTotal = Integer.parseInt(cst.toString());
                bank.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            bank.addTextChangedListener(commaSeparator);

            bankfee.removeTextChangedListener(commaSeparator);

            if (!(bankfee.getText()).toString().matches("")) {
                String cstArray[] = (bankfee.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                bankFeeTotal = Integer.parseInt(cst.toString());
                bankfee.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            bankfee.addTextChangedListener(commaSeparator);
        }

        @Override
        public void afterTextChanged(Editable s) {
            credit.setSelection(credit.getText().length());
            actual.setSelection(actual.getText().length());
            mpesa.setSelection(mpesa.getText().length());
            mpesafee.setSelection(mpesafee.getText().length());
            bank.setSelection(bank.getText().length());
            bankfee.setSelection(bankfee.getText().length());
        }
    };

    /**
     * end of text watcher
     **/

    public int getMpesaFee(int numb){
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
