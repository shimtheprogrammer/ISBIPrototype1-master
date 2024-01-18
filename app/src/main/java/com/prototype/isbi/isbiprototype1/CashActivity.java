package com.prototype.isbi.isbiprototype1;

import android.app.Activity;

import android.content.Intent;

import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;

import java.text.NumberFormat;

import java.util.List;

/**
 * Created by MRuto on 12/16/2016.
 */

public class CashActivity extends Activity{
    public static String TAG = "CashActivity";

    Button btnNext, btnBack;
    EditText actual, bank, mpesa;
    LinearLayout titleLayout, buttonLayout, scrollLayout, mainLayout, btnNextLayout, btnBackLayout;
    ScrollView verticleScroll;

    CashHandler cdb = new CashHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        //lining parameters to values on xml
        actual = (EditText) findViewById(R.id.actual);
        bank = (EditText) findViewById(R.id.bank);
        mpesa = (EditText) findViewById(R.id.mpesa);

        actual.addTextChangedListener(totalWatcher);
        bank.addTextChangedListener(totalWatcher);
        mpesa.addTextChangedListener(totalWatcher);

        btnBack = (Button) findViewById(R.id.btn_back);
        btnNext = (Button) findViewById(R.id.btn_next);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        scrollLayout = (LinearLayout) findViewById(R.id.scroll_layout);
        btnBackLayout = (LinearLayout) findViewById(R.id.btnBackLayout);
        btnNextLayout = (LinearLayout) findViewById(R.id.btnNxtLayout);
        verticleScroll = (ScrollView) findViewById(R.id.vertical_scroll);

        int holoBlueBright = getApplication().getResources().getColor(R.color.holoBlueBright);
        scrollLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueBright);
        drawable.setSize(2, 2);
        scrollLayout.setDividerDrawable(drawable);

        titleLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        buttonLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mainLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable2 = new GradientDrawable();
        drawable2.setColor(holoBlueBright);
        drawable2.setSize(1, 1);
        titleLayout.setDividerDrawable(drawable2);
        buttonLayout.setDividerDrawable(drawable2);
        mainLayout.setDividerDrawable(drawable2);

        verticleScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                btnBackLayout.setBackgroundResource(R.color.seeThrough);
                btnNextLayout.setBackgroundResource(R.color.seeThrough);
            }
        });

        //creating listener and event for Submit button;
        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnBackLayout.setBackgroundResource(R.color.halfSeeThrough);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnBackLayout.setBackgroundResource(R.color.seeThrough);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnNext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnNextLayout.setBackgroundResource(R.color.halfSeeThrough);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnNextLayout.setBackgroundResource(R.color.seeThrough);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });


        //creating listener and event for Submit button;
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String actualArray[] = (actual.getText().toString()).split(",");
                StringBuilder actualBuilder = new StringBuilder();

                for(int i = 0; i < actualArray.length; i++){
                    actualBuilder.append(actualArray[i]);
                }

                String bankArray[] = (bank.getText().toString()).split(",");
                StringBuilder bankBuilder = new StringBuilder();

                for(int i = 0; i < bankArray.length; i++){
                    bankBuilder.append(bankArray[i]);
                }

                String mpesaArray[] = (mpesa.getText().toString()).split(",");
                StringBuilder mpesaBuilder = new StringBuilder();

                for(int i = 0; i < mpesaArray.length; i++){
                    mpesaBuilder.append(mpesaArray[i]);
                }

                if((actual.getText()).toString().matches("") || (bank.getText()).toString().matches("") ||
                        (mpesa.getText()).toString().matches("")){

                    Log.d("Values: ", "some were null..");

                    Toast.makeText(getApplicationContext(),
                            " Some Values were empty",
                            Toast.LENGTH_SHORT).show();


                } else if((Integer.parseInt(actualBuilder.toString()) + Integer.parseInt(bankBuilder.toString())
                        + Integer.parseInt(mpesaBuilder.toString())) <= 0){
                    Log.d(TAG, "total was <= 0..");

                    actual.setError("Toatl cannot be 0");

                } else {
                    cdb.dropTable();
                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");
                    cdb.addCash(new Cash(Integer.parseInt(actualBuilder.toString()),
                            Integer.parseInt(bankBuilder.toString()), Integer.parseInt(mpesaBuilder.toString())));

                    // Reading all contacts
                    Log.d("Reading: ", "Reading all contacts..");
                    List<Cash> cash = cdb.getAllCash();

                    for (Cash pd : cash) {
                        String log = "Id: " + pd.getID() + " ,Actual: " + (pd.getActual())
                                + " ,Bank: " + (pd.getBank()) + " ,Mpesa: " + (pd.getMpesa());
                        // Writing Contacts to log
                        Log.d("Name: ", log);
                    }

                    Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
                    startActivity(intent);
                    finish();
                }

//                Intent intent = new Intent(getApplicationContext(), InventoryDisplayActivity.class);
//                intent.putExtra("FROM", "REG");
//                startActivity(intent);
//                finish();
            }
        });

        //creating listener and event for back button;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuisnessDetailsActivity.class);
                startActivity(intent);
                finish();
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
            if(!actual.getText().toString().matches("")) {
                actual.removeTextChangedListener(totalWatcher);

                if (!(actual.getText()).toString().matches("")) {
                    String cstArray[] = (actual.getText().toString()).split(",");
                    StringBuilder cst = new StringBuilder();

                    for (int i = 0; i < cstArray.length; i++) {
                        cst.append(cstArray[i]);
                    }

                    actual.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
                }

                String actualArray[] = (actual.getText().toString()).split(",");
                StringBuilder actualBuilder = new StringBuilder();

                for(int i = 0; i < actualArray.length; i++){
                    actualBuilder.append(actualArray[i]);
                }

                actual.addTextChangedListener(totalWatcher);
            }
            if(!mpesa.getText().toString().matches("")) {
                mpesa.removeTextChangedListener(totalWatcher);

                if (!(mpesa.getText()).toString().matches("")) {
                    String cstArray[] = (mpesa.getText().toString()).split(",");
                    StringBuilder cst = new StringBuilder();

                    for (int i = 0; i < cstArray.length; i++) {
                        cst.append(cstArray[i]);
                    }

                    mpesa.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
                }

                String mpesaArray[] = (mpesa.getText().toString()).split(",");
                StringBuilder mpesaBuilder = new StringBuilder();

                for(int i = 0; i < mpesaArray.length; i++){
                    mpesaBuilder.append(mpesaArray[i]);
                }

                mpesa.addTextChangedListener(totalWatcher);
            }
            if(!bank.getText().toString().matches("")) {
                bank.removeTextChangedListener(totalWatcher);

                if (!(bank.getText()).toString().matches("")) {
                    String cstArray[] = (bank.getText().toString()).split(",");
                    StringBuilder cst = new StringBuilder();

                    for (int i = 0; i < cstArray.length; i++) {
                        cst.append(cstArray[i]);
                    }

                    bank.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
                }

                String bankArray[] = (bank.getText().toString()).split(",");
                StringBuilder bankBuilder = new StringBuilder();

                for(int i = 0; i < bankArray.length; i++){
                    bankBuilder.append(bankArray[i]);
                }

                bank.addTextChangedListener(totalWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!actual.getText().toString().matches("")) {
                actual.setSelection(actual.getText().length());
            }
            if(!mpesa.getText().toString().matches("")) {
                mpesa.setSelection(mpesa.getText().length());
            }
            if(!bank.getText().toString().matches("")) {
                bank.setSelection(bank.getText().length());
            }
        }
    };

    /**
     * end of textr watcher
     **/

}
