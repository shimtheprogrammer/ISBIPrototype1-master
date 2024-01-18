package com.prototype.isbi.isbiprototype1;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;

import android.app.ProgressDialog;

import android.content.Intent;

import android.graphics.Color;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalData;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PurchaseHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String TAG = "MainActivity";
    public static String database;

    //ImageButton sale, purchase, delivery2, expenses, preorders, presales, owner, assets, loans;
    CardView saleCard, purchaseCard, deliveryCard, expensesCard, preordersCard, presalesCard, ownerCard, assetsCard, loansCard;
    TextView mdSal, title;

    private String names[] = {"Cash", "Bank A/C", "M-PESA"};
    private float data[] = new float[3];
    PieChart pieChart;

    CashHandler cdb = new CashHandler(this);
    PersonalDataHandler pddb = new PersonalDataHandler(this);
    GoodsSalesHandler gsdb = new GoodsSalesHandler(this);
    SalesHandler sdb = new SalesHandler(this);
    GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);
    PurchaseHandler pdb = new PurchaseHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);
    GoodsPreOrderedHandler gpodb = new GoodsPreOrderedHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);
    MixHandler mdb = new MixHandler(this);

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (width < height) {
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main_landscape);

        }

        database = LoginActivity.database;
        Log.d(TAG, "database is " + database);
//        Toast.makeText(MainActivity.this, "database is " + database,
//                Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DASHBOARD");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pieChart = (PieChart) findViewById(R.id.pie_chart);

        List<Cash> cashList = cdb.getAllCash();

        int tot = 0;
        for (Cash pd : cashList) {
            data[0] = (float) (pd.getActual());
            data[1] = (float) (pd.getBank());
            data[2] = (float) (pd.getMpesa());

            tot = (int) (data[0] + data[1] + data[2]);
        }

        Description description = new Description();
        description.setText("Money Distribution");
        description.setTextSize(20);
        description.setPosition(0,0);

        pieChart.setContentDescription("Money Chart");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(1f);
        pieChart.setTransparentCircleRadius(1f);
//        pieChart.setCenterText("Total: " + numberFormat.format(tot));
//        pieChart.setCenterTextSize(20);

        addDataSet(pieChart);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "Entry " + e.toString());
                Log.d(TAG, "Highlight " + h.toString());

                if (h.toString().contains("x: 2.0")){
                    Log.d(TAG, "Money M-PESA");
                    Intent intent = new Intent(getApplicationContext(), MoneyTransferActivity.class);
                    intent.putExtra("FROM", "MPESA");
                    startActivity(intent);

                } else if (h.toString().contains("x: 1.0")){
                    Log.d(TAG, "Money Bank");
                    Intent intent = new Intent(getApplicationContext(), MoneyTransferActivity.class);
                    intent.putExtra("FROM", "Bank");
                    startActivity(intent);

                } else if (h.toString().contains("x: 0.0")){
                    Log.d(TAG, "Money Cash");
                    Intent intent = new Intent(getApplicationContext(), MoneyTransferActivity.class);
                    intent.putExtra("FROM", "Cash");
                    startActivity(intent);

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

//        sale = (ImageButton) findViewById(R.id.btn_sale);
//
//        sale.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        sale.setImageResource(R.drawable.button_sale_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        sale.setImageResource(R.drawable.button_sale);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        sale.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                List<GoodsSales> sales = gsdb.getByForeignKey(sdb.getRowCount() + 1);
//                Intent intent;
//                if(sales.size() <= 0 || sales.isEmpty() || sales == null){
//                    intent = new Intent(getApplicationContext(), AddSalesActivity.class);
//                } else {
//                    intent = new Intent(getApplicationContext(), SaleActivity.class);
//                }
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//
//            }
//        });

        mdSal = (TextView) findViewById(R.id.md_sal);

        List<PersonalData> personalDatas = pddb.getAllPeople();

        int sal = 0;
        for (PersonalData pd : personalDatas) {
            sal = pd.getMDSalary();
        }

        mdSal.setText("" + numberFormat.format(sal));

        title = (TextView) findViewById(R.id.title);

        title.setText(personalDatas.get(0).getBName());

        saleCard = (CardView) findViewById(R.id.sale_card);
        purchaseCard = (CardView) findViewById(R.id.purchase_card);
        deliveryCard = (CardView) findViewById(R.id.delivery_card);
        preordersCard = (CardView) findViewById(R.id.pre_purchase_card);
        presalesCard = (CardView) findViewById(R.id.pre_sale_card);
        expensesCard = (CardView) findViewById(R.id.expenses_card);
        ownerCard = (CardView) findViewById(R.id.owner_card);
        assetsCard = (CardView) findViewById(R.id.asset_card);
        loansCard = (CardView) findViewById(R.id.loan_card);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = AnimatorInflater
                    .loadStateListAnimator(getApplicationContext(), R.drawable.lift_on_touch);
            saleCard.setStateListAnimator(stateListAnimator);
            purchaseCard.setStateListAnimator(stateListAnimator);
            deliveryCard.setStateListAnimator(stateListAnimator);
            preordersCard.setStateListAnimator(stateListAnimator);
            presalesCard.setStateListAnimator(stateListAnimator);
            expensesCard.setStateListAnimator(stateListAnimator);
            ownerCard.setStateListAnimator(stateListAnimator);
            assetsCard.setStateListAnimator(stateListAnimator);
            loansCard.setStateListAnimator(stateListAnimator);

        }

//        saleCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        saleCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        saleCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        saleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsSales> sales = gsdb.getByForeignKey(sdb.getRowCount() + 1);
                Intent intent;
                if(sales.size() <= 0 || sales.isEmpty() || sales == null){
                    intent = new Intent(getApplicationContext(), AddSalesActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), SaleActivity.class);
                }
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Sales Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        purchaseCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        purchaseCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        purchaseCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        purchaseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsPurchased> purchased = gpdb.getByForeignKey(pdb.getRowCount() + 1);
                Intent intent;
                if(purchased.size() <= 0 || purchased.isEmpty() || purchased == null){
                    intent = new Intent(getApplicationContext(), AddPurchaseActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), PurchaseActivity.class);
                }
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Purchase Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        deliveryCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        deliveryCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        deliveryCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        deliveryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Delivery2Activity.class);
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Delivery Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        purchase = (ImageButton) findViewById(R.id.btn_purchase);
//
//        purchase.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        purchase.setImageResource(R.drawable.button_purchase_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        purchase.setImageResource(R.drawable.button_purchase);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        purchase.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                List<GoodsPurchased> purchased = gpdb.getByForeignKey(pdb.getRowCount() + 1);
//                Intent intent;
//                if(purchased.size() <= 0 || purchased.isEmpty() || purchased == null){
//                    intent = new Intent(getApplicationContext(), AddPurchaseActivity.class);
//                } else {
//                    intent = new Intent(getApplicationContext(), PurchaseActivity.class);
//                }
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });
//
//        delivery2 = (ImageButton) findViewById(R.id.btn_delivery2);
//
//        delivery2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        delivery2.setImageResource(R.drawable.button_delivery_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        delivery2.setImageResource(R.drawable.button_delivery);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        delivery2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(), Delivery2Activity.class);
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });

//        expensesCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        expensesCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        expensesCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        expensesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Expenses Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        expenses = (ImageButton) findViewById(R.id.btn_expenses);
//
//        expenses.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        expenses.setImageResource(R.drawable.button_expenses_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        expenses.setImageResource(R.drawable.button_expenses);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        expenses.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });

//        presalesCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        presalesCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        presalesCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        presalesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsPreOrdered> preSale = gpsdb.getByForeignKey(psdb.getRowCount() + 1);
                Intent intent;
                if(preSale.size() <= 0 || preSale.isEmpty() || preSale == null){
                    intent = new Intent(getApplicationContext(), AddPreSalesActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), PreSaleActivity.class);
                }
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Pre-Sale Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        preordersCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        preordersCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        preordersCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        preordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsPreOrdered> prePurchased = gpodb.getByForeignKey(podb.getRowCount() + 1);
                Intent intent;
                if(prePurchased.size() <= 0 || prePurchased.isEmpty() || prePurchased == null){
                    intent = new Intent(getApplicationContext(), AddPreOrderActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), PreOrderActivity.class);
                }
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Pre-Purchase Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        preorders = (ImageButton) findViewById(R.id.btn_pre_order);
//
//        preorders.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        preorders.setImageResource(R.drawable.button_pre_purchase_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        preorders.setImageResource(R.drawable.button_pre_purchase);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        preorders.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(), AddPreOrderActivity.class);
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });
//
//        presales = (ImageButton) findViewById(R.id.btn_pre_sale);
//
//        presales.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        presales.setImageResource(R.drawable.button_pre_sale_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        presales.setImageResource(R.drawable.button_pre_sale);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        presales.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(), AddPreSalesActivity.class);
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });

//        ownerCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        ownerCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        ownerCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        ownerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OwnerActivity.class);
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Owner Drawings Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        owner = (ImageButton) findViewById(R.id.btn_owner);
//
//        owner.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        owner.setImageResource(R.drawable.button_owner_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        owner.setImageResource(R.drawable.button_owner);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        owner.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(), OwnerActivity.class);
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });

//        assets = (ImageButton) findViewById(R.id.btn_assets);
//
//        assets.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        assets.setImageResource(R.drawable.button_assets_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        assets.setImageResource(R.drawable.button_assets);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        assets.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(), AssetsActivity.class);
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });

//        assetsCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        assetsCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        assetsCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        assetsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssetsActivity.class);
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Assets Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });

//        loans = (ImageButton) findViewById(R.id.btn_loan);
//
//        loans.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        loans.setImageResource(R.drawable.button_loans_on);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        loans.setImageResource(R.drawable.button_loans);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });
//
//        loans.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(), LoansActivity.class);
//                intent.putExtra("FROM", "MAIN");
//                startActivity(intent);
//            }
//        });

//        loansCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        loansCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransClick));
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        loansCard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransUp));
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        loansCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoansActivity.class);
                intent.putExtra("FROM", "MAIN");

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Loans Ticket...");
                progressDialog.show();

                startActivity(intent);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        navigationItemSelected.NavigationItem(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void addDataSet(PieChart pieChart){
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            yEntries.add(new PieEntry(data[i], i));
            xEntries.add(names[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "");
        //"MD Salary; " + numberFormat.format(sal) + "\n Blue-Cash \n Green-Bank \n Red-M-PESA"
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colour = new ArrayList<>();
        colour.add(Color.BLUE);
        colour.add(Color.GREEN);
        colour.add(Color.RED);

        pieDataSet.setColors(colour);

//        Legend legend = pieChart.getLegend();
//
//        legend.setFormSize(10f); // set the size of the legend forms/shapes
//        legend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
//        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        legend.setTextSize(12f);
//        legend.setTextColor(Color.BLACK);
//        legend.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
//        legend.setYEntrySpace(5f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Mix mix = mdb.getMix(1);

        mix.setFlag("false");
        mix.setCredit(0);
        mix.setCash(0);
        mix.setMpesa(0);
        mix.setBank(0);
        mix.setMpesaFee(0);
        mix.setBankFee(0);

        mdb.updateMix(mix);

    }
}
