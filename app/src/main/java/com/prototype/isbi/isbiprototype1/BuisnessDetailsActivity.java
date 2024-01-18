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

import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalData;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by MRuto on 3/30/2017.
 */

public class BuisnessDetailsActivity extends Activity {

    // initialise parameters to be used
    Button btnNext, btnBack;
    EditText fName, location, mdSalary; //, pin;
    LinearLayout titleLayout, buttonLayout, scrollLayout, mainLayout, btnNextLayout, btnBackLayout;
    ScrollView verticleScroll;

    int mdSalaryTotal;

    PersonalDataHandler pdb = new PersonalDataHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buisness_details);

        //lining parameters to values on xml
        mdSalary = (EditText) findViewById(R.id.md_salary);
        //pin = (EditText) findViewById(R.id.pin);
        fName = (EditText) findViewById(R.id.bName);
        location = (EditText) findViewById(R.id.location);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNxt);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
        scrollLayout = (LinearLayout) findViewById(R.id.scroll_layout);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        btnBackLayout = (LinearLayout) findViewById(R.id.btnBackLayout);
        btnNextLayout = (LinearLayout) findViewById(R.id.btnNxtLayout);
        verticleScroll = (ScrollView) findViewById(R.id.vertical_scroll);

        mdSalary.addTextChangedListener(commaSeparator);

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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((fName.getText()).toString().matches("") || (location.getText()).toString().matches("") ||
                        (mdSalary.getText()).toString().matches("")){

                    Log.d("Values: ", "some were null..");

                } else{
                    /**
                     * CRUD Operations
                     * */
                    List<PersonalData> personalData = pdb.getAllPeople();

                    boolean flag = true;
                    for (PersonalData pd : personalData) {
                        flag = false;
                    }

                    if(flag){
                        pdb.addPerson(new PersonalData("ISBI","ISBI",0,0,"ISBI","ISBI-Nairobi"));
                    }

                    PersonalData personalData1 = pdb.getPerson(1);
                    personalData1.setBName(fName.getText().toString());
                    personalData1.setLocation(location.getText().toString());
                    personalData1.setMDSalary(mdSalaryTotal);

                    pdb.updatePerson(personalData1);

                    Intent intent = new Intent(getApplicationContext(), CashActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

        //creating listener and event for back button;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * text watcher for formating cost
     **/

    private final TextWatcher commaSeparator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            mdSalary.removeTextChangedListener(commaSeparator);

            if (!(mdSalary.getText()).toString().matches("")) {
                String cstArray[] = (mdSalary.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                mdSalaryTotal = Integer.parseInt(cst.toString());
                mdSalary.setText("" + numberFormat.format(((Double.parseDouble(cst.toString())))));
            }
            mdSalary.addTextChangedListener(commaSeparator);
        }

        @Override
        public void afterTextChanged(Editable s) {
            mdSalary.setSelection(mdSalary.getText().length());
        }
    };

    /**
     * end of text watcher
     **/
}
