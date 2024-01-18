package com.prototype.isbi.isbiprototype1;

import android.app.Activity;

import android.content.Intent;

import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Instalment;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.EightELAdapter;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 3/30/2017.
 */

public class LoanRegistrationActivity extends Activity {
    public static String TAG = "LoanRegistratonActivity";

    EightELAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String, String> idList = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    LoansHandler ldb = new LoansHandler(this);
    InstalmentHandler ildb = new InstalmentHandler(this);

    Button Next, Exit;
    LinearLayout buttonLayout, mainLayout, btnNextLayout, btnBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp2);

        Next = (Button) findViewById(R.id.btn_next);
        Exit = (Button) findViewById(R.id.btn_exit);
        buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        btnBackLayout = (LinearLayout) findViewById(R.id.btnBackLayout);
        btnNextLayout = (LinearLayout) findViewById(R.id.btnNxtLayout);

        int holoBlueBright = getApplication().getResources().getColor(R.color.holoBlueBright);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueBright);
        drawable.setSize(2, 2);

        buttonLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mainLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable2 = new GradientDrawable();
        drawable2.setColor(holoBlueBright);
        drawable2.setSize(1, 1);
        buttonLayout.setDividerDrawable(drawable2);
        mainLayout.setDividerDrawable(drawable2);

        //creating listener and event for Submit button;
        Exit.setOnTouchListener(new View.OnTouchListener() {
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

        Next.setOnTouchListener(new View.OnTouchListener() {
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

        Next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpensesRegistrationActivity.class);
                startActivity(intent);
                finish();

            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssetRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // preparing list data
        prepareListData();

        listAdapter = new EightELAdapter(this, listDataHeader,
                listDataChild, "LOA", true);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });

        expListView.expandGroup(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        prepareListData();

        listAdapter = new EightELAdapter(this, listDataHeader,
                listDataChild, "LOA", true);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.expandGroup(0);
    }

    /*
         * Preparing the list data
         */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> loans = new ArrayList<>();

        List<Loans> loansList = ldb.getAllLoans();

        String title = "ID-Creditor-Total-Date-Last Ins.-Instalment-No.-Savings";
        //loans.add(title);
        int count = 0;
        for (Loans pd : loansList) {
            if(pd.getStatus().matches("NOT")) {
                int loansTotal = pd.getTotal();

                List<Instalment> instalments = ildb.getByForeignKey(pd.getID());


                for(Instalment pd1: instalments) {
                    double perc = ((pd.getInstalments() - pd.getContractualSavings()) * pd.getInstalmentsNo());
                    double send2 = ((pd.getTotal() / perc) * pd1.getTotal());
                    loansTotal = loansTotal - (int) Math.round(send2);
                    Log.d("cheq: ", "perce " + perc + " send " + send2 + " tot " + loansTotal);
                }

                String savings = "No";
                if (pd.getContractualSavings() > 0){
                    savings = "Yes";
                }
                String name[] = pd.getName().split("-");
                String show = name[0];

                String log = pd.getID() + "-" + show
                        + "-" + numberFormat.format(loansTotal)
                        + "-" + pd.getStartDate() + "-" + pd.getEndDate()
                        + "-" + numberFormat.format(pd.getInstalments()) + "-" + pd.getInstalmentsNo()
                        + "-" + savings;
                Log.d(TAG,"Insert: " + log);
                loans.add(log);
                idList.put("" + count, "" + pd.getID());
                count++;
            }
        }

        String head = "";

        head = "Declare Loans: ";

        listDataHeader.add(head + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), loans); // Header, Child data
    }
}

