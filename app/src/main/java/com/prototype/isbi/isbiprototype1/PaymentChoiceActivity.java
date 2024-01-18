package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Intent;

import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;

import java.text.NumberFormat;

import java.util.Calendar;

/**
 * Created by MRuto on 1/13/2017.
 */

public class PaymentChoiceActivity extends Activity{
    public static String TAG = "PaymentChoiceActivity";

    Button btnFullPayment, btnPartialPayment, btnCnangeDate, btnBack, btnDept;
    TextView fromText, totalText, dueDateText, fromTo;
    LinearLayout payableText, receivableText, payableLL, receivableLL, deptLL;

    int year, month, day;

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    String date;
    private Calendar calendar;

    PayablesHandler pdb = new PayablesHandler(this);
    ReceivablesHandler rdb = new ReceivablesHandler(this);
    PaidReceivablesHandler prdb = new PaidReceivablesHandler(this);
    CashHandler cdb = new CashHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_choice);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(290, 353);

        final int idSent = Integer.parseInt(getIntent().getStringExtra("ID"));
        final String from = getIntent().getStringExtra("FROM");

        fromText = (TextView) findViewById(R.id.from);
        dueDateText = (TextView) findViewById(R.id.due_date);
        totalText = (TextView) findViewById(R.id.total);
        payableText = (LinearLayout) findViewById(R.id.payable_text);
        receivableText = (LinearLayout) findViewById(R.id.receivable_text);
        fromTo = (TextView) findViewById(R.id.from_to);
        payableLL = (LinearLayout) findViewById(R.id.payable_icon);
        receivableLL = (LinearLayout) findViewById(R.id.receivable_icon);
        deptLL = (LinearLayout) findViewById(R.id.dept_ll);

        if (from.matches("REC")) {
            Receivable receivable = rdb.getReceivable(idSent);

            String fromRec[] = receivable.getFrom().split("-");
            fromTo.setText("From");
            fromText.setText("" + fromRec[0]);
            dueDateText.setText("" + receivable.getDate());
            totalText.setText("" + numberFormat.format(receivable.getTotal()));

            LinearLayout.LayoutParams hide =
                    new LinearLayout.LayoutParams(0, 0, 0f);

            payableLL.setLayoutParams(hide);
            payableText.setLayoutParams(hide);

        } else {
            Payable payable = pdb.getPayable(idSent);

            String toPay[] = payable.getTo().split("-");
            fromTo.setText("To");
            fromText.setText("" + toPay[0]);
            dueDateText.setText("" + payable.getDate());
            totalText.setText("" + numberFormat.format(payable.getTotal()));

            LinearLayout.LayoutParams hide =
                    new LinearLayout.LayoutParams(0, 0, 0f);

            receivableText.setLayoutParams(hide);
            receivableLL.setLayoutParams(hide);

            deptLL.setLayoutParams(hide);

        }

        //setting date
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btnFullPayment = (Button) findViewById(R.id.btn_full_payment);
        btnPartialPayment = (Button) findViewById(R.id.btn_partial_payment);
        btnCnangeDate = (Button) findViewById(R.id.btn_change_date);
        btnDept = (Button) findViewById(R.id.btn_dept);
        btnBack = (Button) findViewById(R.id.btn_back);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        LinearLayout linearLayout0 = (LinearLayout) findViewById(R.id.linearLayout);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.scroll_layout);

        int holoBlueBright = getApplication().getResources().getColor(R.color.holoBlueBright);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout4.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueBright);
        drawable.setSize(2, 2);
        linearLayout.setDividerDrawable(drawable);
        linearLayout4.setDividerDrawable(drawable);

        linearLayout0.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout1.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout2.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout3.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable2 = new GradientDrawable();
        drawable2.setColor(holoBlueBright);
        drawable2.setSize(1, 1);
        linearLayout0.setDividerDrawable(drawable2);
        linearLayout1.setDividerDrawable(drawable2);
        linearLayout2.setDividerDrawable(drawable2);
        linearLayout3.setDividerDrawable(drawable2);

        btnCnangeDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnCnangeDate.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnCnangeDate.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnBack.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnBack.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFullPayment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnFullPayment.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnFullPayment.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnFullPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("FROM", from);
                intent.putExtra("ID", "" + idSent);
                Log.i("Checking idsent; ", "" + idSent);
                intent.putExtra("TYPE", "FULL");
                startActivity(intent);
                finish();
            }
        });

        btnPartialPayment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnPartialPayment.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnPartialPayment.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnPartialPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("FROM", from);
                intent.putExtra("ID", "" + idSent);
                intent.putExtra("TYPE", "PART");
                startActivity(intent);
                finish();
            }
        });

//        btnDept.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        btnDept.setBackgroundResource(R.drawable.button_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        btnDept.setBackgroundResource(R.drawable.button);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        btnDept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
//                intent.putExtra("FROM", from);
//                intent.putExtra("ID", "" + idSent);
//                intent.putExtra("TYPE", "PART");
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    /**
     * the beelow methodes aree used to manage the date field
     * **/
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    public void setDeptDate(View view) { showDialog(998); }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 998) {
            return new DatePickerDialog(this,
                    myDeptDateListener, year, month, day);
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

    private DatePickerDialog.OnDateSetListener myDeptDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDeptDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int _year, int _month, int _day) {
        this.year = _year;
        this.month = _month;
        this.day = _day;

        date = day + "/" + month  + "/" + year;

        Log.i(TAG, "Check if date is set: " + date);

        if ((getIntent().getStringExtra("FROM")).matches("PAY")){
            Payable payable = pdb.getPayable(Integer.parseInt(getIntent().getStringExtra("ID")));
            payable.setDate(date);
            pdb.updatePayable(payable);

            Intent intent = new Intent(getApplicationContext(), PayablesDisplayActivity.class);
            startActivity(intent);
            finish();

        } else if ((getIntent().getStringExtra("FROM")).matches("REC")){
            Receivable receivable = rdb.getReceivable(Integer.parseInt(getIntent().getStringExtra("ID")));
            receivable.setDate(date);
            rdb.updateReceivable(receivable);

            Intent intent = new Intent(getApplicationContext(), ReceivablesDisplayActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showDeptDate(int _year, int _month, int _day) {
        this.year = _year;
        this.month = _month;
        this.day = _day;

        date = day + "/" + month  + "/" + year;

        Log.i(TAG, "Check if date is set: " + date);

        if ((getIntent().getStringExtra("FROM")).matches("REC")){
            Receivable receivable = rdb.getReceivable(Integer.parseInt(getIntent().getStringExtra("ID")));

            PaidReceivable paidReceivable = new PaidReceivable(
                    receivable.getFrom(), receivable.getTotal(),
                    date, receivable.getOriginalDate(), "Dept. Default");

            prdb.addPaidReceivable(paidReceivable);

            rdb.deleteReceivable(receivable);

            Intent intent = new Intent(getApplicationContext(), ReceivablesDisplayActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * end of date methodes
     **/
}
