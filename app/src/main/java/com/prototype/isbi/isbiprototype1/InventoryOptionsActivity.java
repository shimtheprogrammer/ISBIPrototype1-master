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

import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;

import java.text.DecimalFormat;

/**
 * Created by MRuto on 3/13/2017.
 */

public class InventoryOptionsActivity extends Activity {
    public static String TAG = "InventoryOptionsActivity";

    TextView product, quantity, costTxt;
    Button back, setSPrice, owner, remove;

    InventoryHandler idb = new InventoryHandler(this);

    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    DecimalFormat decimalFormat2 = new DecimalFormat("#.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_options);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(290, 363);

        product = (TextView) findViewById(R.id.product);
        quantity = (TextView) findViewById(R.id.quantity);
        costTxt = (TextView) findViewById(R.id.cost);

        back = (Button) findViewById(R.id.btn_back);
        setSPrice = (Button) findViewById(R.id.btn_sprice);
        owner = (Button) findViewById(R.id.btn_owner);
        remove = (Button) findViewById(R.id.btn_remove);

        LinearLayout.LayoutParams hide = new LinearLayout.LayoutParams(0, 0);

        LinearLayout imageView1 = (LinearLayout) findViewById(R.id.sale_icon);
        LinearLayout imageView2 = (LinearLayout) findViewById(R.id.purchse_icon);

        if(getIntent().getStringExtra("FROM").matches("SALE")) {
            imageView2.setLayoutParams(hide);
        }

        if(getIntent().getStringExtra("FROM").matches("ORDER")) {
            imageView1.setLayoutParams(hide);
        }

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


//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
//        //LinearLayout linearLayoutButton = (LinearLayout) findViewById(R.id.linear_layout_button);
//        LinearLayout linearLayout0 = (LinearLayout) findViewById(R.id.linearLayout);
//        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
//        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
//        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
//
//        int holoBlueBright = getApplication().getResources().getColor(R.color.holoBlueBright);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        //linearLayoutButton.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setColor(holoBlueBright);
//        drawable.setSize(2, 2);
//        linearLayout.setDividerDrawable(drawable);
//        //linearLayoutButton.setDividerDrawable(drawable);
//
//        linearLayout0.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout1.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout2.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout3.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        GradientDrawable drawable2 = new GradientDrawable();
//        drawable2.setColor(holoBlueBright);
//        drawable2.setSize(1, 1);
//        linearLayout0.setDividerDrawable(drawable2);
//        linearLayout1.setDividerDrawable(drawable2);
//        linearLayout2.setDividerDrawable(drawable2);
//        linearLayout3.setDividerDrawable(drawable2);

        Inventory inventory = idb.getInventory(Integer.parseInt(getIntent().getStringExtra("ID")));

        double numbe = inventory.getNumber();
        String units = inventory.getUnit();
        String unitsCost = inventory.getUnit();
        double cost = inventory.getCost();

        if(numbe >= 1000 && !units.matches("Pcs")){
            numbe = numbe / 1000;
            if(inventory.getUnit().matches("Grms")){
                units = "Kgs";
            }
            if(inventory.getUnit().matches("mLs")){
                units = "Ls";
            }
        }

        if(!units.matches("Pcs")) {
            cost = cost * 1000;
        }

        if(inventory.getUnit().matches("Grms")){
            unitsCost = "Kgs";
        }
        if(inventory.getUnit().matches("mLs")){
            unitsCost = "Ls";
        }

        product.setText("" + inventory.getProduct());
        quantity.setText("" + decimalFormat2.format(numbe) + " " + units);
        costTxt.setText("" + decimalFormat.format(cost) + " Per " + unitsCost);

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setSPrice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setSPrice.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        setSPrice.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        setSPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InventoryChangeActivity.class);
                intent.putExtra("FROM", "SPR");
                intent.putExtra("ID", "" + Integer.parseInt(getIntent().getStringExtra("ID")));
                startActivity(intent);
                finish();

            }
        });

        owner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        owner.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        owner.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InventoryChangeActivity.class);
                intent.putExtra("FROM", "OWE");
                intent.putExtra("ID", "" + Integer.parseInt(getIntent().getStringExtra("ID")));
                startActivity(intent);
                finish();

            }
        });

        remove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        remove.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        remove.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InventoryChangeActivity.class);
                intent.putExtra("FROM", "REM");
                intent.putExtra("ID", "" + Integer.parseInt(getIntent().getStringExtra("ID")));
                startActivity(intent);
                finish();

            }
        });
    }
}
