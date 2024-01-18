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

import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGains;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Assets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUp;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 2/8/2017.
 */

public class SoldAssetActivity extends Activity {
    public static String TAG = "SoldAssetActivity";

    Spinner payment;

    private Calendar calendar;
    private TextView dateView, dueDate, dueDateText;
    private int year, month, day;

    Button submit, back, btnDate, btnDueDate;
    EditText total, info, supplier, fee;
    LinearLayout ifCredit, selling, infoing, totaling, paying, feeLL;

    String paymentMethode = "";
    public int globalTotal = 0, feeTotal = 0;
    public boolean creditFromMix = false, fromMix = true;

    SoldAssetsHandler sadb = new SoldAssetsHandler(this);
    AssetsHandler adb = new AssetsHandler(this);
    CashHandler cdb = new CashHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    PayablesHandler pdb = new PayablesHandler(this);
    ReUpHandler rudb = new ReUpHandler(this);
    ReceivablesHandler rdb = new ReceivablesHandler(this);
    AssetGainsHandler agdb = new AssetGainsHandler(this);
    MixHandler mdb = new MixHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_assets);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .5), (int) (height * .5));

        total = (EditText) findViewById(R.id.total);
        info = (EditText) findViewById(R.id.info);
        btnDate = (Button) findViewById(R.id.btn_date);
        btnDueDate = (Button) findViewById(R.id.btn_due_date);
        dateView = (TextView) findViewById(R.id.date);
        dueDate = (TextView) findViewById(R.id.due_date);
        dueDateText = (TextView) findViewById(R.id.due_date_text);
        ifCredit = (LinearLayout) findViewById(R.id.ifCredit);
        selling = (LinearLayout) findViewById(R.id.selling);
        supplier = (EditText) findViewById(R.id.supplier);
        infoing = (LinearLayout) findViewById(R.id.infoing);
        totaling = (LinearLayout) findViewById(R.id.totaling);
        paying = (LinearLayout) findViewById(R.id.paying);
        feeLL = (LinearLayout) findViewById(R.id.fee_ll);
        fee = (EditText) findViewById(R.id.fee);

        //setting text watcher
        total.addTextChangedListener(totalWatcher);
        fee.addTextChangedListener(comma2);
        total.addTextChangedListener(commaSeparator);

        /*
        set visibility
         */

        LinearLayout.LayoutParams hide =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        0, 0f);
        LinearLayout.LayoutParams hide2 =
                new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0f);
        LinearLayout.LayoutParams hiding =
                new LinearLayout.LayoutParams(0, 0, 0);
        LinearLayout.LayoutParams show =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        if(getIntent().getStringExtra("HOW").matches("SEL")){
            infoing.setLayoutParams(hide);
            ifCredit.setLayoutParams(show);
            dueDate.setVisibility(View.INVISIBLE);
            btnDueDate.setVisibility(View.INVISIBLE);
            dueDateText.setVisibility(View.INVISIBLE);

        } else if(getIntent().getStringExtra("HOW").matches("REM")){
            totaling.setLayoutParams(hide);
            paying.setLayoutParams(hide2);
            feeLL.setLayoutParams(hiding);

        }  else if(getIntent().getStringExtra("HOW").matches("UPG")){
            feeLL.setLayoutParams(hiding);

        }

        /*
        done
         */

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

                if(((total.getText().toString()).matches("") || (globalTotal <= 0))
                        && !(getIntent().getStringExtra("HOW").matches("REM"))){
                    Log.d("Values: ", "totals was null..");
                    Toast.makeText(getApplicationContext(), "Amount not set",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("--Select Payment Method--") && fromMix
                        && !(getIntent().getStringExtra("HOW").matches("REM"))){
                    Log.d("Values: ", "no pay meth selected..");
                    Toast.makeText(getApplicationContext(), "Please Select a payment method",
                            Toast.LENGTH_LONG).show();
                } else if(info.getText().toString().matches("")
                        && (getIntent().getStringExtra("HOW").matches("REM"))){
                    Log.d("Values: ", "no added info..");
                    Toast.makeText(getApplicationContext(), "Please Enter Additional Information",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("Cash") && (globalTotal > cash)
                        && !(getIntent().getStringExtra("HOW").matches("SEL"))){
                    Log.d("Values: ", "actual too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Cash Available",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("MPESA") && (globalTotal > mpesa)
                        && !(getIntent().getStringExtra("HOW").matches("SEL"))){
                    Log.d("Values: ", "mpesa too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough MPESA Available",
                            Toast.LENGTH_LONG).show();
                } else if(paymentMethode.matches("Bank Transfer") && (globalTotal > bank)
                        && !(getIntent().getStringExtra("HOW").matches("SEL"))){
                    Log.d("Values: ", "Bank Account too little..");
                    Toast.makeText(getApplicationContext(), "Not Enough Money in Bank A/C Available",
                            Toast.LENGTH_LONG).show();
                }
//                else if(getIntent().getStringExtra("HOW").matches("SEL")
//                        && supplier.getText().toString().matches("")){
//                    Log.d("Values: ", "no buyer on sale..");
//                    Toast.makeText(getApplicationContext(), "Please Enter Name",
//                            Toast.LENGTH_LONG).show();
//                }
                else if(paymentMethode.matches("Credit") && supplier.getText().toString().matches("")){
                    Log.d("Values: ", "no Supplier on credit..");
                    Toast.makeText(getApplicationContext(), "Please Enter Name",
                            Toast.LENGTH_LONG).show();
                } else if(creditFromMix){
                    Log.d("Values: ", "no Supplier on credit..");
                    Toast.makeText(getApplicationContext(), "Please Enter Name For Credit",
                            Toast.LENGTH_LONG).show();
                } else {

                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("Transcaction Type " + getIntent().getStringExtra("WHAT") + ", amount "
                            + (globalTotal) + " on " + dateView.getText().toString());

                    confMessaage.append("\nMode of payment; ");

                    if (!fromMix) {
                        if (mix.getCash() > 0) {
                            confMessaage.append("Cash ");
                        }

                        if (mix.getBank() > 0) {
                            confMessaage.append("Bank Transfer ");
                        }

                        if (mix.getMpesa() > 0) {
                            confMessaage.append("MPESA ");
                        }

                    } else {
                        confMessaage.append("" + paymentMethode);
                    }

                    int transactionCost = 0;

                    if (!fromMix) {
                        if (mix.getBankFee() > 0) {
                            transactionCost = transactionCost + mix.getBankFee();
                        }

                        if (mix.getMpesa() > 0) {
                            transactionCost = transactionCost + mix.getMpesaFee();
                        }

                    } else {
                        transactionCost = transactionCost + feeTotal;
                    }

                    if (transactionCost > 0) {
                        confMessaage.append("\nTransaction Cost; " + transactionCost);

                    }

                    new AlertDialog.Builder(SoldAssetActivity.this)
                            .setIcon(R.drawable.assets_icon_3)
                            .setTitle("Confirm Asset Transaction")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    List<Cash> allcash = cdb.getAllCash();

                                    int cash = 0;
                                    int bank = 0;
                                    int mpesa  = 0;

                                    for (Cash pd : allcash) {
                                        cash = pd.getActual();
                                        bank = pd.getBank();
                                        mpesa  = pd.getMpesa();
                                    }
                                    Mix mix = mdb.getMix(1);
                                    if (!fromMix) {
                                        if (((mix.getCash() > 0) && (mix.getCash() > cash))
                                                && !(getIntent().getStringExtra("HOW").matches("SEL"))) {
                                            Log.d("Values: ", "actual too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough Cash Available",
                                                    Toast.LENGTH_LONG).show();

                                        } else if (((mix.getMpesa() > 0) && ((mix.getMpesa() + mix.getMpesaFee()) > mpesa))
                                                && !(getIntent().getStringExtra("HOW").matches("SEL"))) {
                                            Log.d("Values: ", "mpesa too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough MPESA Available",
                                                    Toast.LENGTH_LONG).show();

                                        } else if (((mix.getBank() > 0) && ((mix.getBank() + mix.getBankFee()) > bank))
                                                && !(getIntent().getStringExtra("HOW").matches("SEL"))) {
                                            Log.d("Values: ", "Bank Account too little..");
                                            Toast.makeText(getApplicationContext(), "Not Enough Money in Bank A/C Available",
                                                    Toast.LENGTH_LONG).show();

                                        } else {
                                            StringBuilder pay = new StringBuilder();

                                            if (mix.getCash() > 0) {
                                                if (getIntent().getStringExtra("HOW").matches("SEL")) {
                                                    cdb.updateSingleCash("Cash", mix.getCash());

                                                } else {
                                                    cdb.updateSingleCashPurchase("Cash", mix.getCash());

                                                }
                                                pay.append("Cash ");
                                            }

                                            if (mix.getBank() > 0) {
                                                if (getIntent().getStringExtra("HOW").matches("SEL")) {
                                                    cdb.updateSingleCash("Bank Transfer", mix.getBank());

                                                } else {
                                                    cdb.updateSingleCashPurchase("Bank Transfer", mix.getBank());

                                                    if (mix.getBankFee() > 0) {
                                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getBankFee(), paymentMethode,
                                                                ("Asset " + "Bank A/C" + " Transfer"), dateView.getText().toString()));
                                                        cdb.updateSingleCashPurchase("Bank Transfer", mix.getBankFee());

                                                    }
                                                }
                                                pay.append("Bank Transfer ");
                                            }

                                            if (mix.getMpesa() > 0) {
                                                if (getIntent().getStringExtra("HOW").matches("SEL")) {
                                                    cdb.updateSingleCash("MPESA", mix.getMpesa());

                                                } else {
                                                    cdb.updateSingleCashPurchase("MPESA", mix.getMpesa());

                                                    if (mix.getMpesaFee() > 0) {
                                                        edb.addExpense(new Expenses("Other", ("Transaction Fee"), mix.getMpesaFee(), paymentMethode,
                                                                ("Asset " + "MPESA" + " Transfer"), dateView.getText().toString()));
                                                        cdb.updateSingleCashPurchase("MPESA", mix.getMpesaFee());

                                                    }
                                                }
                                                pay.append("MPESA ");
                                            }

                                            if (mix.getCredit() > 0) {
                                                if ((getIntent().getStringExtra("FROM").matches("UPG") ||
                                                        getIntent().getStringExtra("HOW").matches("REP"))) {
                                                    pdb.addPayable(new Payable(supplier.getText().toString(), mix.getCredit(),
                                                            dueDate.getText().toString(), dateView.getText().toString()));

                                                } else if ((getIntent().getStringExtra("HOW").matches("SEL"))) {
                                                    rdb.addReceivable(new Receivable(supplier.getText().toString(), mix.getCredit(),
                                                            dueDate.getText().toString(), dateView.getText().toString()));

                                                }
                                                pay.append("Credit ");
                                            }

                                            Assets assets = adb.getAssets(Integer.parseInt(getIntent().getStringExtra("ID")));

                                            if (getIntent().getStringExtra("HOW").matches("SEL")) {
                                                sadb.addAssets(new SoldAssets(assets.getType(), assets.getInfo(),
                                                        assets.getTotal(), pay.toString(), assets.getDate(),
                                                        assets.getUsefull(), assets.getScrsp(), globalTotal,
                                                        dateView.getText().toString(), assets.getID()));

                                                int profit = 0;

                                                if (getIntent().getStringExtra("WHAT").matches("LAN")
                                                        || getIntent().getStringExtra("WHAT").matches("BUI")) {
                                                    profit = globalTotal - assets.getTotal();
                                                }

                                                if (getIntent().getStringExtra("WHAT").matches("VEH")
                                                        || getIntent().getStringExtra("WHAT").matches("OTH")
                                                        || getIntent().getStringExtra("WHAT").matches("EQU")) {
                                                    String dateNow = dateView.getText().toString();

                                                    String datNow[] = dateNow.split("/");
                                                    int day = Integer.parseInt(datNow[0]);
                                                    int month = Integer.parseInt(datNow[1]) - 1;
                                                    int year = Integer.parseInt(datNow[2]);

                                                    String date = assets.getDate();

                                                    String dat[] = date.split("/");
                                                    int actualDay = Integer.parseInt(dat[0]);
                                                    int actualMonth = Integer.parseInt(dat[1]) - 1;
                                                    int actualYear = Integer.parseInt(dat[2]);

                                                    String endDat[] = date.split("/");
                                                    int stopDay = Integer.parseInt(endDat[0]);
                                                    int stopMonth = Integer.parseInt(endDat[1]) - 1 + assets.getUsefull();
                                                    int stopYear = Integer.parseInt(endDat[2]);

                                                    Calendar stopDate;
                                                    stopDate = Calendar.getInstance();
                                                    stopDate.set(Calendar.DAY_OF_MONTH, stopDay);
                                                    stopDate.set(Calendar.MONTH, stopMonth);
                                                    stopDate.set(Calendar.YEAR, stopYear);

                                                    stopDay = stopDate.get(Calendar.DAY_OF_MONTH);
                                                    stopMonth = stopDate.get(Calendar.MONTH);
                                                    stopYear = stopDate.get(Calendar.YEAR);

                                                    int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year) + 1;
                                                    double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                                                    double value = Math.round((double) assets.getTotal()
                                                            - ((((double) assets.getTotal() - (double) assets.getScrsp())
                                                            / (daysTotal)) * (double) days));

                                                    int pass = (int) value;

                                                    int vehiclesTotal = 0;

                                                    if (pass < assets.getScrsp()) {
                                                        vehiclesTotal = vehiclesTotal + assets.getScrsp();
                                                    } else {
                                                        vehiclesTotal = vehiclesTotal + pass;
                                                    }
                                                    profit = globalTotal - vehiclesTotal;
                                                }

                                                agdb.addAssetGains(new AssetGains(supplier.getText().toString(), profit,
                                                        pay.toString(), dateView.getText().toString(), assets.getID()));

                                                adb.deleteAssets(assets);
                                            }

                                            if (getIntent().getStringExtra("HOW").matches("REM")) {
                                                sadb.addAssets(new SoldAssets(assets.getType(), assets.getInfo() + "; " + info.getText().toString(),
                                                        assets.getTotal(), " ", assets.getDate(),
                                                        assets.getUsefull(), assets.getScrsp(), globalTotal,
                                                        dateView.getText().toString(), assets.getID()));

                                                int profit = 0;

                                                if (getIntent().getStringExtra("WHAT").matches("BUI")) {
                                                    profit = 0 - assets.getTotal();
                                                }

                                                if (getIntent().getStringExtra("WHAT").matches("VEH")
                                                        || getIntent().getStringExtra("WHAT").matches("EQU")
                                                        || getIntent().getStringExtra("WHAT").matches("OTH")) {
                                                    String dateNow = dateView.getText().toString();

                                                    String datNow[] = dateNow.split("/");
                                                    int day = Integer.parseInt(datNow[0]);
                                                    int month = Integer.parseInt(datNow[1]) - 1;
                                                    int year = Integer.parseInt(datNow[2]);

                                                    String date = assets.getDate();

                                                    String dat[] = date.split("/");
                                                    int actualDay = Integer.parseInt(dat[0]);
                                                    int actualMonth = Integer.parseInt(dat[1]) - 1;
                                                    int actualYear = Integer.parseInt(dat[2]);

                                                    String endDat[] = date.split("/");
                                                    int stopDay = Integer.parseInt(endDat[0]);
                                                    int stopMonth = Integer.parseInt(endDat[1]) - 1 + assets.getUsefull();
                                                    int stopYear = Integer.parseInt(endDat[2]);

                                                    Calendar stopDate;
                                                    stopDate = Calendar.getInstance();
                                                    stopDate.set(Calendar.DAY_OF_MONTH, stopDay);
                                                    stopDate.set(Calendar.MONTH, stopMonth);
                                                    stopDate.set(Calendar.YEAR, stopYear);

                                                    stopDay = stopDate.get(Calendar.DAY_OF_MONTH);
                                                    stopMonth = stopDate.get(Calendar.MONTH);
                                                    stopYear = stopDate.get(Calendar.YEAR);

                                                    int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year) + 1;
                                                    double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                                                    double value = (int) Math.round((double) assets.getTotal()
                                                            - ((((double) assets.getTotal() - (double) assets.getScrsp())
                                                            / (daysTotal)) * (double) days));

                                                    int pass = (int) value;

                                                    int vehiclesTotal = 0;

                                                    if (pass < assets.getScrsp()) {
                                                        vehiclesTotal = vehiclesTotal + assets.getScrsp();
                                                    } else {
                                                        vehiclesTotal = vehiclesTotal + pass;
                                                    }
                                                    profit = 0 - vehiclesTotal;
                                                }

                                                agdb.addAssetGains(new AssetGains(supplier.getText().toString(), profit,
                                                        info.getText().toString(), dateView.getText().toString(), assets.getID()));

                                                adb.deleteAssets(assets);
                                            }

                                            if (getIntent().getStringExtra("HOW").matches("UPG")) {
                                                rudb.addReUp(new ReUp(info.getText().toString(), globalTotal, pay.toString(),
                                                        dateView.getText().toString(), assets.getID()));
                                                assets.setTotal(assets.getTotal() + globalTotal);
                                                adb.updateAssets(assets);
                                            }

                                            if (getIntent().getStringExtra("HOW").matches("REP")) {
                                                edb.addExpense(new Expenses("Other", "Repair on; " + assets.getInfo(),
                                                        globalTotal, pay.toString(), info.getText().toString(),
                                                        dateView.getText().toString()));
                                            }

                                            mix.setFlag("false");
                                            mix.setCredit(0);
                                            mix.setCash(0);
                                            mix.setMpesa(0);
                                            mix.setBank(0);
                                            mix.setMpesaFee(0);
                                            mix.setBankFee(0);

                                            mdb.updateMix(mix);

                                            Intent intent = new Intent(getApplicationContext(), AssetsDisplayActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        if (paymentMethode.matches("Credit") && (getIntent().getStringExtra("HOW").matches("UPG") ||
                                                getIntent().getStringExtra("HOW").matches("REP"))) {
                                            pdb.addPayable(new Payable(supplier.getText().toString(), globalTotal,
                                                    dueDate.getText().toString(), dateView.getText().toString()));

                                        } else if (paymentMethode.matches("Credit")
                                                && (getIntent().getStringExtra("HOW").matches("SEL"))) {
                                            rdb.addReceivable(new Receivable(supplier.getText().toString(), globalTotal,
                                                    dueDate.getText().toString(), dateView.getText().toString()));

                                        } else if (getIntent().getStringExtra("HOW").matches("UPG") ||
                                                getIntent().getStringExtra("HOW").matches("REP")) {
                                            cdb.updateSingleCashPurchase(paymentMethode, globalTotal);

                                        } else if (getIntent().getStringExtra("HOW").matches("SEL")) {
                                            cdb.updateSingleCash(paymentMethode, globalTotal);

                                        }

                                        Assets assets = adb.getAssets(Integer.parseInt(getIntent().getStringExtra("ID")));

                                        if (getIntent().getStringExtra("HOW").matches("SEL")) {
                                            sadb.addAssets(new SoldAssets(assets.getType(), assets.getInfo(),
                                                    assets.getTotal(), paymentMethode, assets.getDate(),
                                                    assets.getUsefull(), assets.getScrsp(), globalTotal,
                                                    dateView.getText().toString(), assets.getID()));

                                            int profit = 0;

                                            if (getIntent().getStringExtra("WHAT").matches("LAN")
                                                    || getIntent().getStringExtra("WHAT").matches("BUI")) {
                                                profit = globalTotal - assets.getTotal();
                                            }

                                            if (getIntent().getStringExtra("WHAT").matches("VEH")
                                                    || getIntent().getStringExtra("WHAT").matches("OTH")
                                                    || getIntent().getStringExtra("WHAT").matches("EQU")) {
                                                String dateNow = dateView.getText().toString();

                                                String datNow[] = dateNow.split("/");
                                                int day = Integer.parseInt(datNow[0]);
                                                int month = Integer.parseInt(datNow[1]) - 1;
                                                int year = Integer.parseInt(datNow[2]);

                                                String date = assets.getDate();

                                                String dat[] = date.split("/");
                                                int actualDay = Integer.parseInt(dat[0]);
                                                int actualMonth = Integer.parseInt(dat[1]) - 1;
                                                int actualYear = Integer.parseInt(dat[2]);

                                                String endDat[] = date.split("/");
                                                int stopDay = Integer.parseInt(endDat[0]);
                                                int stopMonth = Integer.parseInt(endDat[1]) - 1 + assets.getUsefull();
                                                int stopYear = Integer.parseInt(endDat[2]);

                                                Calendar stopDate;
                                                stopDate = Calendar.getInstance();
                                                stopDate.set(Calendar.DAY_OF_MONTH, stopDay);
                                                stopDate.set(Calendar.MONTH, stopMonth);
                                                stopDate.set(Calendar.YEAR, stopYear);

                                                stopDay = stopDate.get(Calendar.DAY_OF_MONTH);
                                                stopMonth = stopDate.get(Calendar.MONTH);
                                                stopYear = stopDate.get(Calendar.YEAR);

                                                int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year) + 1;
                                                double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                                                double value = Math.round((double) assets.getTotal()
                                                        - ((((double) assets.getTotal() - (double) assets.getScrsp())
                                                        / (daysTotal)) * (double) days));

                                                int pass = (int) value;

                                                int vehiclesTotal = 0;

                                                if (pass < assets.getScrsp()) {
                                                    vehiclesTotal = vehiclesTotal + assets.getScrsp();
                                                } else {
                                                    vehiclesTotal = vehiclesTotal + pass;
                                                }
                                                profit = globalTotal - vehiclesTotal;
                                            }

                                            agdb.addAssetGains(new AssetGains(supplier.getText().toString(), profit,
                                                    paymentMethode, dateView.getText().toString(), assets.getID()));

                                            adb.deleteAssets(assets);
                                        }

                                        if (getIntent().getStringExtra("HOW").matches("REM")) {
                                            sadb.addAssets(new SoldAssets(assets.getType(), (assets.getInfo() + "; " + info.getText().toString()),
                                                    assets.getTotal(), " ", assets.getDate(),
                                                    assets.getUsefull(), assets.getScrsp(), globalTotal,
                                                    dateView.getText().toString(), assets.getID()));

                                            int profit = 0;

                                            if (getIntent().getStringExtra("WHAT").matches("BUI")) {
                                                profit = 0 - assets.getTotal();
                                            }

                                            if (getIntent().getStringExtra("WHAT").matches("VEH")
                                                    || getIntent().getStringExtra("WHAT").matches("OTH")
                                                    || getIntent().getStringExtra("WHAT").matches("EQU")) {
                                                String dateNow = dateView.getText().toString();

                                                String datNow[] = dateNow.split("/");
                                                int day = Integer.parseInt(datNow[0]);
                                                int month = Integer.parseInt(datNow[1]) - 1;
                                                int year = Integer.parseInt(datNow[2]);

                                                String date = assets.getDate();

                                                String dat[] = date.split("/");
                                                int actualDay = Integer.parseInt(dat[0]);
                                                int actualMonth = Integer.parseInt(dat[1]) - 1;
                                                int actualYear = Integer.parseInt(dat[2]);

                                                String endDat[] = date.split("/");
                                                int stopDay = Integer.parseInt(endDat[0]);
                                                int stopMonth = Integer.parseInt(endDat[1]) - 1 + assets.getUsefull();
                                                int stopYear = Integer.parseInt(endDat[2]);

                                                Calendar stopDate;
                                                stopDate = Calendar.getInstance();
                                                stopDate.set(Calendar.DAY_OF_MONTH, stopDay);
                                                stopDate.set(Calendar.MONTH, stopMonth);
                                                stopDate.set(Calendar.YEAR, stopYear);

                                                stopDay = stopDate.get(Calendar.DAY_OF_MONTH);
                                                stopMonth = stopDate.get(Calendar.MONTH);
                                                stopYear = stopDate.get(Calendar.YEAR);

                                                int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year);
                                                double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                                                double value = (int) Math.round((double) assets.getTotal()
                                                        - ((((double) assets.getTotal() - (double) assets.getScrsp())
                                                        / (daysTotal)) * (double) days));

                                                int pass = (int) value;

                                                int vehiclesTotal = 0;

                                                if (pass < assets.getScrsp()) {
                                                    vehiclesTotal = vehiclesTotal + assets.getScrsp();
                                                } else {
                                                    vehiclesTotal = vehiclesTotal + pass;
                                                }
                                                profit = 0 - vehiclesTotal;
                                            }

                                            agdb.addAssetGains(new AssetGains((supplier.getText().toString() + " "), profit,
                                                    (info.getText().toString() + " "), dateView.getText().toString(), assets.getID()));

                                            adb.deleteAssets(assets);
                                        }

                                        if (getIntent().getStringExtra("HOW").matches("UPG")) {
                                            rudb.addReUp(new ReUp(info.getText().toString(), globalTotal, paymentMethode,
                                                    dateView.getText().toString(), assets.getID()));
                                            assets.setTotal(assets.getTotal() + globalTotal);
                                            adb.updateAssets(assets);
                                        }

                                        if (getIntent().getStringExtra("HOW").matches("REP")) {
                                            edb.addExpense(new Expenses("Other", "Repair on; " + assets.getInfo(),
                                                    globalTotal, paymentMethode, info.getText().toString(),
                                                    dateView.getText().toString()));
                                        }

                                        if (!(fee.getText().toString().matches("")) && feeTotal > 0
                                                && !(getIntent().getStringExtra("HOW").matches("SEL"))) {
                                            String feeName = "MPESA";

                                            if (paymentMethode.matches("Bank Transfer")) {
                                                feeName = "Bank A/C";
                                            }

                                            edb.addExpense(new Expenses("Other", ("Transaction Fee"), feeTotal, paymentMethode,
                                                    ("Asset " + feeName + " Transfer"), dateView.getText().toString()));
                                            cdb.updateSingleCashPurchase(paymentMethode, feeTotal);
                                        }

                                        Intent intent = new Intent(getApplicationContext(), AssetsDisplayActivity.class);
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
//                            creditFromMix = false;
//                        }

                        LinearLayout.LayoutParams show =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                        LinearLayout.LayoutParams hide =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        0, 0);

                        if(paymentMethode.matches("Credit")){
                            if(!(getIntent().getStringExtra("HOW").matches("SEL"))) {
                                ifCredit.setLayoutParams(show);
                            }
                            dueDate.setVisibility(View.VISIBLE);
                            btnDueDate.setVisibility(View.VISIBLE);
                            dueDateText.setVisibility(View.VISIBLE);

                        } else if(paymentMethode.matches("--Select Payment Method--")){
                        } else {
                            if(!(getIntent().getStringExtra("HOW").matches("SEL"))) {
                                ifCredit.setLayoutParams(hide);
                            }
                            dueDate.setVisibility(View.INVISIBLE);
                            btnDueDate.setVisibility(View.INVISIBLE);
                            dueDateText.setVisibility(View.INVISIBLE);

                        }

                        if(!(paymentMethode.matches("MPESA") || paymentMethode.matches("Bank Transfer"))){
                            feeLL.setLayoutParams(hide);
                            feeTotal = 0;

                        } else {
                            if (!(getIntent().getStringExtra("HOW").matches("SEL"))){
                                feeLL.setLayoutParams(show);
                            }
                        }

                        if(paymentMethode.matches("Mix")){
                            if((total.getText().toString()).matches("") || (globalTotal <= 0)){
                                Log.d(TAG, "totals was null..");
                                total.setError("No amount set");
                                total.requestFocus();
                                payment.setSelection(0);

                            } else {
                                Intent intent = new Intent(getApplicationContext(), MixPaymentActivity.class);

                                if (getIntent().getStringExtra("HOW").matches("SEL")) {
                                    intent.putExtra("FROM", "REC");
                                    intent.putExtra("CREDIT", "YES");

                                } else {
                                    intent.putExtra("FROM", "PAY");
                                    intent.putExtra("CREDIT", "YES");

                                }

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

                            if (paymentMethode.matches("MPESA") && !(getIntent().getStringExtra("HOW").matches("SEL"))) {
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

            LinearLayout.LayoutParams show2 =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams hide3 =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0f);

            if(!(getIntent().getStringExtra("CREDIT").matches("0"))){
                ifCredit.setLayoutParams(show2);
            } else {
                ifCredit.setLayoutParams(hide3);
            }

            text.append("= (total) " + sum);

            globalTotal = sum;

            total.setText(text);

            info.setText("" + getIntent().getStringExtra("INFO"));
            supplier.setText("" + getIntent().getStringExtra("SUPPLIER"));
            dateView.setText("" + getIntent().getStringExtra("DATE"));
            dueDate.setText("" + getIntent().getStringExtra("DUEDATE"));

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

            if(mix.getCredit() > 0){
                sum = sum + mix.getCredit();
                creditFromMix = true;

            }

            if(mix.getBank() > 0){
                sum = sum + mix.getBank();

            }

            if(mix.getMpesa() > 0){
                sum = sum + mix.getMpesa();

            }


            LinearLayout.LayoutParams show2 =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams hide3 =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0f);

            if(mix.getCredit() > 0){
                ifCredit.setLayoutParams(show2);

            } else {
                ifCredit.setLayoutParams(hide3);

            }

            if(mix.getCredit() > 0){
                if(!(getIntent().getStringExtra("HOW").matches("SEL"))) {
                    ifCredit.setLayoutParams(show);
                }
                dueDate.setVisibility(View.VISIBLE);
                btnDueDate.setVisibility(View.VISIBLE);
                dueDateText.setVisibility(View.VISIBLE);

            } else {
                if(!(getIntent().getStringExtra("HOW").matches("SEL"))) {
                    ifCredit.setLayoutParams(hide);
                }
                dueDate.setVisibility(View.INVISIBLE);
                btnDueDate.setVisibility(View.INVISIBLE);
                dueDateText.setVisibility(View.INVISIBLE);

            }

            text.append("(total) " + numberFormat.format(sum) + " = ");

            if(mix.getCash() > 0){
                text.append(" (actual) " + numberFormat.format(mix.getCash()));
            }

            if(mix.getCredit() > 0){
                text.append(" (credit) " + mix.getCredit());
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

            if (paymentMethode.matches("MPESA") && !(getIntent().getStringExtra("HOW").matches("SEL"))) {
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

            if(mix.getCredit() > 0){
                sum = sum + mix.getCredit();
                creditFromMix = true;

            }

            if(mix.getBank() > 0){
                sum = sum + mix.getBank();

            }

            if(mix.getMpesa() > 0){
                sum = sum + mix.getMpesa();

            }

            LinearLayout.LayoutParams show2 =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams hide3 =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0f);

            if(mix.getCredit() > 0){
                ifCredit.setLayoutParams(show2);
                dueDate.setVisibility(View.VISIBLE);
                btnDueDate.setVisibility(View.VISIBLE);
                dueDateText.setVisibility(View.VISIBLE);

            } else {
                ifCredit.setLayoutParams(hide3);
                dueDate.setVisibility(View.INVISIBLE);
                btnDueDate.setVisibility(View.INVISIBLE);
                dueDateText.setVisibility(View.INVISIBLE);

            }

            text.append("(total) " + numberFormat.format(sum) + " = ");

            if(mix.getCash() > 0){
                text.append(" (actual) " + numberFormat.format(mix.getCash()));
            }

            if(mix.getCredit() > 0){
                text.append(" (credit) " + mix.getCredit());
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

            if (paymentMethode.matches("MPESA") && !(getIntent().getStringExtra("HOW").matches("SEL"))) {
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

    /*
    calc number of days
     */

    public int dayNumb(double startDay, double startMonth, double startYear, double endDay, double endMonth, double endYear){
        endMonth = endMonth + 1;
        startMonth = startMonth + 1;

        double numb = 0;
        double years = endYear - startYear;
        double months = endMonth - startMonth;

        if (years == 0 && months == 0){
            Log.i(TAG, "In same month");
            Log.i(TAG, "diff between " + endDay + " and " + startDay + " is " + (endDay - startDay));
            numb = endDay - startDay;

        } else if (months == 1 && years ==0){
            Log.i(TAG, "in same year and adjusent months");
            Log.i(TAG, "diff between " + (daysPerMonth((int) (startMonth), (int) (startYear)))
                    + " and " + startDay + " plus " + endDay + " is "
                    + (((daysPerMonth((int) (startMonth), (int) (startYear))) - startDay) + (endDay)));
            int monthDays = daysPerMonth((int) (startMonth), (int) (startYear));
            numb = (monthDays - startDay) + (endDay);

        } else if (months > 1 && years == 0){
            Log.i(TAG, "same year months greater than 1" );
            int startMonthDays = daysPerMonth((int) (startMonth), (int) (startYear));
            Log.i(TAG, "add to day " + startMonthDays + " minus " + startDay);
            numb = numb + (startMonthDays - startDay);
            Log.i(TAG, "add to day enddays " + endDay);
            numb = numb + (endDay);

            for (int i = ((int)startMonth + 1); i < endMonth; i++){
                Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (startYear)));
                numb = numb + daysPerMonth(i, (int) (startYear));

            }

        } else if (years >= 1){
            Log.i(TAG, "diferent years" );
            Log.i(TAG, "months number is " + endMonth + " plus " + ((12 * (endYear - startYear))) + " minus " + startMonth + " minus 1 "
                    + " is " + ((endMonth + (12 * (endYear - startYear))) - startMonth - 1));
            months = (endMonth + (12 * (endYear - startYear))) - startMonth - 1;

            if (months == 0){
                Log.i(TAG, "adjustent months" );
                Log.i(TAG, "diff between " + (daysPerMonth((int) (startMonth), (int) (startYear)))
                        + " and " + startDay + " plus " + endDay + " is "
                        + (((daysPerMonth((int) (startMonth), (int) (startYear))) - startDay) + (endDay)));
                int monthDays = daysPerMonth((int) (startMonth), (int) (startYear));
                numb = (monthDays - startDay) + (endDay);

            } else if (months > 0) {
                Log.i(TAG, "many months" );
                if (years > 1){
                    Log.i(TAG, "many years" );
                    for (int i = ((int) startYear + 1); i < endYear; i++) {
                        for (int j = 1; j <= 12; j++){
                            numb = numb + daysPerMonth(j, i);

                        }
                    }

                    int startMonthDays = daysPerMonth((int) (startMonth), (int) (startYear));
                    Log.i(TAG, "add to day " + startMonthDays + " minus " + startDay);
                    numb = numb + (startMonthDays - startDay);

                    for (int i = ((int)startMonth + 1); i <= 12; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (startYear)));
                        numb = numb + daysPerMonth(i, (int) (startYear));

                    }

                    Log.i(TAG, "add to day enddays " + endDay);
                    numb = numb + (endDay);

                    for (int i = 1; i < (int)endMonth; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (endYear)));
                        numb = numb + daysPerMonth(i, (int) (endYear));

                    }
                } else {
                    Log.i(TAG, "adjusent year" );
                    int startMonthDays = daysPerMonth((int) (startMonth), (int) (startYear));
                    Log.i(TAG, "add to day " + startMonthDays + " minus " + startDay);
                    numb = numb + (startMonthDays - startDay);

                    for (int i = ((int)startMonth + 1); i <= 12; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (startYear)));
                        numb = numb + daysPerMonth(i, (int) (startYear));

                    }

                    Log.i(TAG, "add to day enddays " + endDay);
                    numb = numb + (endDay);

                    for (int i = 1; i < (int)endMonth; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (endYear)));
                        numb = numb + daysPerMonth(i, (int) (endYear));

                    }
                }
            }

            Log.i("calcing2; ", " months " + months);
        }

        Log.i(TAG, "diff between " + startDay + "/" + startMonth + "/" + startYear + " and " + endDay
                + "/" + endMonth + "/" + endYear + " is " + numb + " rounded off is " + ((int) Math.round(numb)));
        int ret = (int) Math.round(numb);

        Log.i(TAG, "Therefore DaysNumb is " + ret + " - 1");

        if(ret > 0) {
            ret = ret - 1;
        }

        return ret;
    }

    public int daysPerMonth(int month, int year){
        int numb;

        if (month  == 1 || month  == 3 || month  == 5 || month  == 7 || month  == 8 || month  == 10 || month  == 12){
            numb = 31;

        } else if (month == 2){
            if (year % 4 == 0){
                numb = 29;

            } else {
                numb = 28;

            }
        } else {
            numb = 30;

        }

        Log.d(TAG, "For month " + month + " in year " + year + " days are " + numb);
        return numb;
    }

    /*
    end of calc days
     */

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
                    Toast.makeText(getApplicationContext(), "Amuont too high",
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
                    Toast.makeText(getApplicationContext(), "Amuont too high",
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
