package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Intent;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.DeliveryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;

import java.util.Calendar;

public class StatmentsDatesActivity extends Activity {
    public static String TAG = "StatmentsDatesActivity";

    Button btnYes, btnNo;

    TextView startDate, endDate;

    Calendar calendar;
    private int year, month, day;

    public int startYear, startMonth, startDay, endYear, endMonth, endDay;

    SalesHandler sdb = new SalesHandler(this);
    GoodsSalesHandler gsdb = new GoodsSalesHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    ReceivablesHandler rdb = new ReceivablesHandler(this);
    PayablesHandler pdb = new PayablesHandler(this);
    PaidReceivablesHandler prdb = new PaidReceivablesHandler(this);
    PaidPayablesHandler ppdb = new PaidPayablesHandler(this);
    GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);
    DeliveryHandler ddb = new DeliveryHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    GoodsPreOrderedHandler gpodb = new GoodsPreOrderedHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);
    OwnerHandler odb = new OwnerHandler(this);
    AssetsHandler adb = new AssetsHandler(this);
    SoldAssetsHandler sadb = new SoldAssetsHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    AssetGainsHandler agdb = new AssetGainsHandler(this);
    ReUpHandler rudb = new ReUpHandler(this);
    LoansHandler ldb = new LoansHandler(this);
    InstalmentHandler ildb = new InstalmentHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statments_dates);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.5),(int)(height*.5));

        final String toWhere = getIntent().getStringExtra("TO");

        btnNo = (Button) findViewById(R.id.btn_no);
        btnYes = (Button) findViewById(R.id.btn_yes);

        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showEndDate(year, month+1, day);

        //text.setText(" ");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String startdateString[] = startDate.getText().toString().split("/");

                Calendar startDate1;
                startDate1 = Calendar.getInstance();
                startDate1.set(Calendar.DAY_OF_MONTH, startDay);
                startDate1.set(Calendar.YEAR, startYear);
                startDate1.set(Calendar.MONTH, (startMonth - 1));

                Calendar endDate1;
                endDate1 = Calendar.getInstance();
                endDate1.set(Calendar.DAY_OF_MONTH, (endDay + 1));
                endDate1.set(Calendar.YEAR, endYear);
                endDate1.set(Calendar.MONTH, (endMonth - 1));

                if (endDate1.before(startDate1)){
                    Log.d(TAG, "EndDate " + endDate1.get(Calendar.DAY_OF_MONTH) + "/"
                            + endDate1.get(Calendar.MONTH) + "/" + endDate1.get(Calendar.YEAR)
                            + " StartDate " + startDate1.get(Calendar.DAY_OF_MONTH) + "/"
                            + startDate1.get(Calendar.MONTH) + "/" + startDate1.get(Calendar.YEAR));

                    Toast.makeText(getApplicationContext(), "End Date cannot be before Start Date", Toast.LENGTH_LONG).show();

                } else if (toWhere.matches("CASHFLOW")) {
                    int go = sdb.getRowCount() + gsdb.getRowCount() + idb.getInventoryCount() + edb.getExpenseCount()
                            + psdb.getRowCount() + psdb.getRowCount() + gpsdb.getRowCount() + odb.getOwnerCount()
                            + adb.getAssetsCount() + sadb.getAssetsCount() + agdb.getRowCount() + ldb.getLoansCount()
                            + ildb.getRowCount() + prdb.getPaidReceivableCount() + rdb.getReceivableCount()
                            + pdb.getPayableyCount() + ppdb.getPaidPayableCount() + gpdb.getRowCount() + ddb.getRowCount()
                            + podb.getRowCount() + gpodb.getRowCount() + rudb.getReUpCount();

                    if (go > 0) {
                        Intent intent = new Intent(getApplicationContext(), CashFlowActivity.class);
                        intent.putExtra("START_YEAR", "" + startYear);
                        intent.putExtra("END_YEAR", "" + endYear);
                        intent.putExtra("START_MONTH", "" + startMonth);
                        intent.putExtra("END_MONTH", "" + endMonth);
                        intent.putExtra("START_DAY", "" + startDay);
                        intent.putExtra("END_DAY", "" + endDay);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no Data to refrence", Toast.LENGTH_LONG).show();

                    }
                } else if (toWhere.matches("PANDL")) {
                    int go = sdb.getRowCount() + gsdb.getRowCount() + idb.getInventoryCount() + edb.getExpenseCount()
                            + psdb.getRowCount() + psdb.getRowCount() + gpsdb.getRowCount() + odb.getOwnerCount()
                            + adb.getAssetsCount() + sadb.getAssetsCount() + agdb.getRowCount() + ldb.getLoansCount()
                            + ildb.getRowCount() + prdb.getPaidReceivableCount();

                    if (go > 0) {
                        Intent intent = new Intent(getApplicationContext(), PAndLActivity.class);
                        intent.putExtra("START_YEAR", "" + startYear);
                        intent.putExtra("END_YEAR", "" + endYear);
                        intent.putExtra("START_MONTH", "" + startMonth);
                        intent.putExtra("END_MONTH", "" + endMonth);
                        intent.putExtra("START_DAY", "" + startDay);
                        intent.putExtra("END_DAY", "" + endDay);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no Data to refrence", Toast.LENGTH_LONG).show();

                    }
                }
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

    public void setEndDate(View view) {
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
                    showEndDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        startDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        startYear = year;
        startMonth = month;
        startDay = day;
    }

    private void showEndDate(int year, int month, int day) {
        endDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        endYear =year;
        endMonth = month;
        endDay = day;
    }

    /**
     * end of date methodes
     **/
}
