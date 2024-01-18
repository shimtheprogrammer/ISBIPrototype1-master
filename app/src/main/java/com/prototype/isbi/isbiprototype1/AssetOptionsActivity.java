package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Assets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUp;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by MRuto on 2/8/2017.
 */

public class AssetOptionsActivity extends Activity {

    Button btnSell, btnRepair, btnUpgrade, btnRemove, btnBack, btnView;
    TextView type, info, total;
    LinearLayout btnSellLL, btnRepairLL, btnUpgradeLL, btnRemoveLL, btnViewLL;

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    ReUpHandler rudb = new ReUpHandler(this);
    AssetsHandler adb = new AssetsHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_option);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(290, 293);
        if(getIntent().getStringExtra("FROM").matches("LAN")){
            getWindow().setLayout(290, 273);
        } else if(getIntent().getStringExtra("FROM").matches("VEH")){
            getWindow().setLayout(290, 353);
        } else if(getIntent().getStringExtra("FROM").matches("EQU")){
            getWindow().setLayout(290, 353);
        } else if(getIntent().getStringExtra("FROM").matches("OTH")){
            getWindow().setLayout(290, 293);
        } else if(getIntent().getStringExtra("FROM").matches("BUI")){
            getWindow().setLayout(290, 383);
        } else {
            getWindow().setLayout(290, 293);
        }

        //lining parameters to values on xml
        type = (TextView) findViewById(R.id.type);
        info = (TextView) findViewById(R.id.info);
        total = (TextView) findViewById(R.id.total);

        btnSell = (Button) findViewById(R.id.btn_sell);
        btnUpgrade = (Button) findViewById(R.id.btn_upgrade);
        btnRemove = (Button) findViewById(R.id.btn_remove);
        btnRepair = (Button) findViewById(R.id.btn_repair);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnView =  (Button) findViewById(R.id.btn_view);

        btnSellLL = (LinearLayout) findViewById(R.id.btn_sell_ll);
        btnUpgradeLL = (LinearLayout) findViewById(R.id.btn_upgrade_ll);
        btnRemoveLL = (LinearLayout) findViewById(R.id.btn_remove_ll);
        btnRepairLL = (LinearLayout) findViewById(R.id.btn_repair_ll);
        btnViewLL =  (LinearLayout) findViewById(R.id.btn_view_ll);

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

        Assets assets = adb.getAssets(Integer.parseInt(getIntent().getStringExtra("ID")));

        type.setText("" + assets.getType());
        info.setText("" + assets.getInfo());
        total.setText("" + numberFormat.format(assets.getTotal()));

        LinearLayout.LayoutParams hide =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        0, 0f);

        if(getIntent().getStringExtra("FROM").matches("LAN")){
            btnRepairLL.setLayoutParams(hide);
            btnUpgradeLL.setLayoutParams(hide);
            btnRemoveLL.setLayoutParams(hide);
            btnViewLL.setLayoutParams(hide);
        } else if(getIntent().getStringExtra("FROM").matches("VEH")){
            btnUpgradeLL.setLayoutParams(hide);
            btnViewLL.setLayoutParams(hide);
        } else if(getIntent().getStringExtra("FROM").matches("EQU")){
            btnUpgradeLL.setLayoutParams(hide);
            btnViewLL.setLayoutParams(hide);
        } else if(getIntent().getStringExtra("FROM").matches("OTH")){
            btnRepairLL.setLayoutParams(hide);
            btnUpgradeLL.setLayoutParams(hide);
            btnViewLL.setLayoutParams(hide);
        } else if(getIntent().getStringExtra("FROM").matches("BUI")){
            List<ReUp> allExpenses = rudb.getByForeignKey(Integer.parseInt(getIntent().getStringExtra("ID")));

            if (allExpenses.size() <= 0) {
                btnViewLL.setLayoutParams(hide);
            }
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
            public void onClick(View v) {
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
                Intent intent = new Intent(getApplicationContext(), FloatFourELActivity.class);

                intent.putExtra("FROM", "AOA");
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));

                startActivity(intent);
                finish();
            }
        });

        btnSell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnSell.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnSell.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SoldAssetActivity.class);

                intent.putExtra("FROM", "AOA");
                intent.putExtra("HOW", "SEL");
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                intent.putExtra("WHAT", "" + getIntent().getStringExtra("FROM"));

                startActivity(intent);
                finish();
            }
        });

        btnUpgrade.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnUpgrade.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnUpgrade.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SoldAssetActivity.class);

                intent.putExtra("FROM", "AOA");
                intent.putExtra("HOW", "UPG");
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                intent.putExtra("WHAT", "" + getIntent().getStringExtra("FROM"));

                startActivity(intent);
                finish();
            }
        });

        btnRepair.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnRepair.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnRepair.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SoldAssetActivity.class);

                intent.putExtra("FROM", "AOA");
                intent.putExtra("HOW", "REP");
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                intent.putExtra("WHAT", "" + getIntent().getStringExtra("FROM"));

                startActivity(intent);
                finish();
            }
        });

        btnRemove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnRemove.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnRemove.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SoldAssetActivity.class);

                intent.putExtra("FROM", "AOA");
                intent.putExtra("HOW", "REM");
                intent.putExtra("ID", "" + getIntent().getStringExtra("ID"));
                intent.putExtra("WHAT", "" + getIntent().getStringExtra("FROM"));

                startActivity(intent);
                finish();
            }
        });
    }
}
