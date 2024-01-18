package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DatePickerDialog;

import android.content.DialogInterface;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.NumberFormat;
import java.util.List;
import java.util.Calendar;

import android.view.MotionEvent;
import android.view.View;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;



/**
 * Created by MRuto on 12/18/2016.
 */

public class AddReceivableActivity extends Activity{
    public static String TAG = "AddReceivableActivity";

    Button btnExit, btnAdd, btnDate, btnRecDate;
    EditText from, total, phone;
    TextView date, recDate;

    int year, month, day;

    int globalTotal;

    Calendar calendar;

    ReceivablesHandler rdb = new ReceivablesHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receivable);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

//        int width = dm.widthPixels;
//        int height = dm.heightPixels;

        getWindow().setLayout((550), (400));

        //lining parameters to values on xml
        from = (EditText) findViewById(R.id.from);
        total = (EditText) findViewById(R.id.total);
        phone = (EditText) findViewById(R.id.number);
        date = (TextView) findViewById(R.id.date);
        recDate = (TextView) findViewById(R.id.rec_date);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDate = (Button) findViewById(R.id.btn_date);
        btnRecDate = (Button) findViewById(R.id.btn_rec_date);

        total.addTextChangedListener(commaSeparator);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(year, month+1, day);
        showRecDate(year, month+1, day);

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

                if((from.getText()).toString().matches("")){
                    Log.d(TAG, "btnAdd; no product set..");

                    from.setError("Please Enter Name");
                    from.requestFocus();

                } else if ((total.getText()).toString().matches("")){
                    Log.d(TAG, "btnAdd; no quantity set..");

                    total.setError("Please Enter Amount");
                    total.requestFocus();

                } else {
                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("Receivable from " + from.getText().toString() + " for amount "
                            + numberFormat.format(globalTotal)
                            + " to be paid on date " + date.getText().toString() );

                    new AlertDialog.Builder(AddReceivableActivity.this)
                            .setIcon(R.drawable.receivable_icon_2)
                            .setTitle("Confirm Receivable")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    StringBuilder nameBuild = new StringBuilder();
                                    nameBuild.append(from.getText().toString());

                                    if(!(phone.getText().toString().matches(""))){
                                        nameBuild.append("-" + phone.getText().toString());
                                    }

                                    rdb.addReceivable(new Receivable(nameBuild.toString(), globalTotal,
                                            date.getText().toString(), recDate.getText().toString()));

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
//                Intent intent = new Intent(getApplicationContext(), ReceivablesActivity.class);
//                //((ReceivablesActivity) getApplicationContext()).finish();
//                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * the bellow methodes are used to manage the date text field
     * **/

    private void showDate(int year, int month, int day){
        date.setText(new StringBuilder().append(day).append('/')
                .append(month).append('/').append(year));
    }

    private void showRecDate(int year, int month, int day){
        recDate.setText(new StringBuilder().append(day).append('/')
                .append(month).append('/').append(year));
    }

    public void setDate(View view){
        showDialog(999);
    }

    public void setRecDate(View view){
        showDialog(998);
    }

    protected Dialog onCreateDialog(int id){
        if(id == 999){
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }

        if(id == 998){
            return new DatePickerDialog(this, myRecDateListener, year, month, day);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3){
            showDate(arg1, arg2+1, arg3);
        }
    };

    private DatePickerDialog.OnDateSetListener myRecDateListener = new
            DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3){
                    showRecDate(arg1, arg2+1, arg3);
                }
            };

    /**
     * the end of the date methodes**/

    /*
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

        }

        @Override
        public void afterTextChanged(Editable s) {
            total.setSelection(total.getText().length());
        }
    };

    /**
     * end of text watcher
     **/
}
