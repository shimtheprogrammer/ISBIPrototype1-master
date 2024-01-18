package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;

import java.text.NumberFormat;

/**
 * Created by MRuto on 2/15/2017.
 */

public class LoanOptionActivity extends Activity {

    Button btnPay, btnView, btnBack;
    TextView creditor, total, instalment;

    InstalmentHandler ildb = new InstalmentHandler(this);
    LoansHandler ldb = new LoansHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_options);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(290, 293);

        //lining parameters to values on xml
        creditor = (TextView) findViewById(R.id.creditor);
        total = (TextView) findViewById(R.id.total);
        instalment = (TextView) findViewById(R.id.instalment);
        btnPay = (Button) findViewById(R.id.btn_pay);
        btnView = (Button) findViewById(R.id.btn_view);
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

        Loans loans = ldb.getLoans(Integer.parseInt(getIntent().getStringExtra("ID")));

        String name[] = loans.getName().split("-");
        final String show = name[(name.length - 1)];

        creditor.setText("" + name[0]);
        total.setText("" + numberFormat.format(loans.getTotal()));
        instalment.setText("" + numberFormat.format(loans.getInstalments()));

        if((ildb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")))).size() <= 0){
            btnView.setBackgroundResource(R.drawable.button_off_2);
            btnView.setEnabled(false);
        }

        if(getIntent().getStringExtra("FOR").matches("PAY")) {
            btnPay.setText("Withdraw Savings");
        }

        if(show.matches("PAY")){
            btnPay.setBackgroundResource(R.drawable.button_off_2);
            btnPay.setEnabled(false);
        }

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
            public void onClick(View v) { finish();}
        });

        btnPay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnPay.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnPay.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InstalmentActivity.class);

                intent.putExtra("FROM", "LOA");
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                intent.putExtra("WHAT", "" + getIntent().getStringExtra("FOR"));

                startActivity(intent);
                finish();
            }
        });

        btnView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnView.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnView.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnView.setBackgroundResource(R.drawable.button_on);
                Intent intent = new Intent(getApplicationContext(), InstalmentDisplayActivity.class);

                intent.putExtra("FROM", "LOA");
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));

                startActivity(intent);
                finish();
            }
        });
    }
}
