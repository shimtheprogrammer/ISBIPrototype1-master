package com.prototype.isbi.isbiprototype1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.prototype.isbi.isbiprototype1.databaseHandlers.Assets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Instalment;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Sales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSale;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Owner;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalData;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGains;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;


import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/15/2017.
 */

public class PAndLActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String TAG = "PAndLActivity";

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);
    HashMap<String, String> ownerDrawPnl = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    SalesHandler sdb = new SalesHandler(this);
    GoodsSalesHandler gsdb = new GoodsSalesHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);
    OwnerHandler odb = new OwnerHandler(this);
    PersonalDataHandler pddb = new PersonalDataHandler(this);
    AssetsHandler adb = new AssetsHandler(this);
    SoldAssetsHandler sadb = new SoldAssetsHandler(this);
    AssetGainsHandler agdb = new AssetGainsHandler(this);
    LoansHandler ldb = new LoansHandler(this);
    InstalmentHandler ildb = new InstalmentHandler(this);
    PaidReceivablesHandler prdb = new PaidReceivablesHandler(this);

    double salesTotalPnL = 0, cogsTotalPnL = 0, sgaExpensesTotalPnL = 0, ebitdaTotalsPnL = 0, taxesTotalsPnL = 0,
             grossMarginTotalPnL = 0, netProfitTotalsPnL = 0, mdSalaryTotalPnL = 0, depreciationTotalPnL = 0,
            operatingProfitsTotalPnL = 0, interestTotalPnL = 0, assetGainsTotalsPnL = 0;

    TextView title, sales, cogs, sgaExpenses, ebitda, tax, netProfit, gross_margin,
            mdSalary, depreciation, operatingProfits, interest, assetGains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_and_l);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message = "Payable\n" +
//                        "Recievables";
//                Snackbar.make(view, message, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * setting up start and end dates
         **/

        // declare textviews
        title = (TextView) findViewById(R.id.title);
        sales = (TextView) findViewById(R.id.sales);
        cogs = (TextView) findViewById(R.id.cogs);
        sgaExpenses  = (TextView) findViewById(R.id.expenses);
        ebitda = (TextView) findViewById(R.id.ebitda);
        tax = (TextView) findViewById(R.id.taxes);
        gross_margin = (TextView) findViewById(R.id.gross_margin);
        netProfit = (TextView) findViewById(R.id.net_profit);
        mdSalary = (TextView) findViewById(R.id.md_salary);
        depreciation = (TextView) findViewById(R.id.depreciation_amortization);
        operatingProfits = (TextView) findViewById(R.id.operating_profit);
        interest = (TextView) findViewById(R.id.interest);
        assetGains = (TextView) findViewById(R.id.asset_gains);

        /**
         * setting up start and end dates
         **/
        int startYear = Integer.parseInt(getIntent().getStringExtra("START_YEAR"));
        int endYear = Integer.parseInt(getIntent().getStringExtra("END_YEAR"));
        int startMonth = Integer.parseInt(getIntent().getStringExtra("START_MONTH")) - 1;
        int endMonth = Integer.parseInt(getIntent().getStringExtra("END_MONTH")) - 1;
        int startDay = Integer.parseInt(getIntent().getStringExtra("START_DAY"));
        int endDay  = Integer.parseInt(getIntent().getStringExtra("END_DAY")) + 1;

        dayNumb(startDay, startMonth, startYear, endDay, endMonth, endYear);

        title.setText("Profit and Loss Statement\nFROM:" + startDay + "-" + (startMonth + 1) + "-" + startYear
                + "\tTO:" + (endDay - 1) + "-" + (endMonth + 1) + "-" + endYear);


        Log.i("Start and end Dates: ", endDay + " " + endMonth + " " + endYear);

        System.out.println("Start and end Dates: " + startYear + " " + startMonth + " " + startDay);

        /**
         * end of date setting
         **/

        Calendar startDatePnL;
        startDatePnL = Calendar.getInstance();
        startDatePnL.set(Calendar.DAY_OF_MONTH, startDay);
        startDatePnL.set(Calendar.MONTH, startMonth);
        startDatePnL.set(Calendar.YEAR, startYear);

//        setMonth(startDatePnL, startMonth);

        Calendar endDatePnL;
        endDatePnL = Calendar.getInstance();
        endDatePnL.set(Calendar.DAY_OF_MONTH, endDay);
        endDatePnL.set(Calendar.MONTH, endMonth);
        endDatePnL.set(Calendar.YEAR, endYear);

//        setMonth(endDatePnL, endMonth);

        Log.i("Starting 2 date; ", " " + startDatePnL.get(Calendar.DAY_OF_MONTH)
                + " " + startDatePnL.get(Calendar.MONTH)
                + " " + startDatePnL.get(Calendar.YEAR));

        Log.i("Ending 2 date; ", " " + endDatePnL.get(Calendar.DAY_OF_MONTH)
                + " " + endDatePnL.get(Calendar.MONTH)
                + " " + endDatePnL.get(Calendar.YEAR));

        List<Sales> allSales = sdb.getAllSales();

        for (Sales pd : allSales) {
            String date = pd.getDate();

            String dat[] = date.split("/");
            int day = Integer.parseInt(dat[0]);
            int month = Integer.parseInt(dat[1]) - 1;
            int year = Integer.parseInt(dat[2]);

            Calendar actualDate;
            actualDate = Calendar.getInstance();
            actualDate.set(Calendar.DAY_OF_MONTH, day);
            actualDate.set(Calendar.MONTH, month);
            actualDate.set(Calendar.YEAR, year);

            Log.i("Doing for date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if(((startDatePnL.compareTo(actualDate)) < 0) && ((actualDate.compareTo(endDatePnL) < 0))){
                salesTotalPnL = salesTotalPnL + pd.getTotal();
                Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

                List<GoodsSales> allGoods = gsdb.getByForeignKey(pd.getId());

                for (GoodsSales pd1 : allGoods) {
                    cogsTotalPnL = cogsTotalPnL + Math.round(pd1.getPCost() * pd1.getNumber());
                    Log.i("its products; ", " " + day  + " " + month +" " +year + " cogs" + (pd1.getPCost() * pd1.getNumber()));
                }
            } else if((startDatePnL.compareTo(actualDate) == 0)){
                salesTotalPnL = salesTotalPnL + pd.getTotal();
                Log.i("date is same; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

                List<GoodsSales> allGoods = gsdb.getByForeignKey(pd.getId());

                for (GoodsSales pd1 : allGoods) {
                    cogsTotalPnL = cogsTotalPnL + Math.round(pd1.getPCost() * pd1.getNumber());
                    Log.i("its products; ", " " + day  + " " + month +" " +year + " cogs" + (pd1.getPCost() * pd1.getNumber()));
                }
            } else if((endDatePnL.compareTo(actualDate) == 0)){
                salesTotalPnL = salesTotalPnL + pd.getTotal();
                Log.i("date is same; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

                List<GoodsSales> allGoods = gsdb.getByForeignKey(pd.getId());

                for (GoodsSales pd1 : allGoods) {
                    cogsTotalPnL = cogsTotalPnL + Math.round(pd1.getPCost() * pd1.getNumber());
                    Log.i("its products; ", " " + day  + " " + month +" " +year + " cogs " + (pd1.getPCost() * pd1.getNumber()));
                }
            }
        }

        List<PreSale> allPreSales = psdb.getAllPreSale();

        for (PreSale pd : allPreSales) {
            Log.i("looping pre ; ", " so not finding ");
            if(pd.getStatus().matches("CLOSED")) {
                String date = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Log.i("Doing pre for date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((startDatePnL.compareTo(actualDate)) < 0) && ((actualDate.compareTo(endDatePnL) < 0))) {
                    salesTotalPnL = salesTotalPnL + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    List<GoodsPreOrdered> allGoods = gpsdb.getByForeignKey(pd.getId());

                    for (GoodsPreOrdered pd1 : allGoods) {
                        Inventory inventory = idb.getInventoryByProduct(pd1.getProduct());

                        cogsTotalPnL = cogsTotalPnL + Math.round(inventory.getCost() * pd1.getNumber());
                        Log.i("its products; ", " " + day + " " + month + " " + year + " cogs" + (pd1.getCost() * pd1.getNumber()));
                    }
                } else if ((startDatePnL.compareTo(actualDate) == 0)) {
                    salesTotalPnL = salesTotalPnL + pd.getTotal();
                    Log.i("date is same; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    List<GoodsPreOrdered> allGoods = gpsdb.getByForeignKey(pd.getId());

                    for (GoodsPreOrdered pd1 : allGoods) {
                        Inventory inventory = idb.getInventoryByProduct(pd1.getProduct());

                        cogsTotalPnL = cogsTotalPnL + Math.round(inventory.getCost() * pd1.getNumber());
                        Log.i("its products; ", " " + day + " " + month + " " + year + " cogs" + (pd1.getCost() * pd1.getNumber()));
                    }
                } else if ((endDatePnL.compareTo(actualDate) == 0)) {
                    salesTotalPnL = salesTotalPnL + pd.getTotal();
                    Log.i("date is same; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    List<GoodsPreOrdered> allGoods = gpsdb.getByForeignKey(pd.getId());

                    for (GoodsPreOrdered pd1 : allGoods) {
                        Inventory inventory = idb.getInventoryByProduct(pd1.getProduct());

                        cogsTotalPnL = cogsTotalPnL + Math.round(inventory.getCost() * pd1.getNumber());
                        Log.i("its products; ", " " + day + " " + month + " " + year + " cogs" + (pd1.getCost() * pd1.getNumber()));
                    }
                }
            }
        }

        List<PaidReceivable> paidReceivableList = prdb.getAllPaidReceivable();

        for (PaidReceivable pd : paidReceivableList){
            if(pd.getPayment().matches("Dept. Default")){
                String date = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Log.i("Doing paidR date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if(((startDatePnL.compareTo(actualDate)) < 0) && ((actualDate.compareTo(endDatePnL) < 0))){
                    salesTotalPnL = salesTotalPnL - pd.getTotal();
                    Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " dept default " + pd.getTotal());

                } else if((startDatePnL.compareTo(actualDate) == 0)){
                    salesTotalPnL = salesTotalPnL - pd.getTotal();
                    Log.i("date is same; ", " " + day  + " " + month +" " +year + " dept default " + pd.getTotal());

                } else if((endDatePnL.compareTo(actualDate) == 0)){
                    salesTotalPnL = salesTotalPnL - pd.getTotal();
                    Log.i("date is same; ", " " + day  + " " + month +" " +year + " dept default " + pd.getTotal());

                }
            }
        }

        List<AssetGains> assetGainsList = agdb.getAllAssetGains();

        for (AssetGains pd : assetGainsList) {
            String date = pd.getDate();

            String dat[] = date.split("/");
            int day = Integer.parseInt(dat[0]);
            int month = Integer.parseInt(dat[1]) - 1;
            int year = Integer.parseInt(dat[2]);

            Calendar actualDate;
            actualDate = Calendar.getInstance();
            actualDate.set(Calendar.DAY_OF_MONTH, day);
            actualDate.set(Calendar.MONTH, month);
            actualDate.set(Calendar.YEAR, year);

            Log.i("Doing asset date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if(((startDatePnL.compareTo(actualDate)) < 0) && ((actualDate.compareTo(endDatePnL) < 0))){
                assetGainsTotalsPnL = assetGainsTotalsPnL + pd.getTotal();
                Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " expense" + pd.getTotal());

            } else if((startDatePnL.compareTo(actualDate) == 0)){
                assetGainsTotalsPnL = assetGainsTotalsPnL + pd.getTotal();
                Log.i("date is same; ", " " + day  + " " + month +" " +year + " expense" + pd.getTotal());

            } else if((endDatePnL.compareTo(actualDate) == 0)){
                assetGainsTotalsPnL = assetGainsTotalsPnL + pd.getTotal();
                Log.i("date is same; ", " " + day  + " " + month +" " +year + " expense" + pd.getTotal());

            }
        }

        List<Expenses> allExpenses = edb.getAllExpenses();

        for (Expenses pd : allExpenses) {
            if (!(pd.getType().matches("Rent"))) {
                String date = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Log.i("Doing expense date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((startDatePnL.compareTo(actualDate)) < 0) && ((actualDate.compareTo(endDatePnL) < 0))) {
                    if (pd.getType().matches("Taxes")) {
                        taxesTotalsPnL = taxesTotalsPnL + pd.getTotal();
                    } else {
                        sgaExpensesTotalPnL = sgaExpensesTotalPnL + pd.getTotal();
                    }
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " expense" + pd.getTotal());

                } else if ((startDatePnL.compareTo(actualDate) == 0)) {
                    if (pd.getType().matches("Taxes")) {
                        taxesTotalsPnL = taxesTotalsPnL + pd.getTotal();
                    } else {
                        sgaExpensesTotalPnL = sgaExpensesTotalPnL + pd.getTotal();
                    }
                    Log.i("date is same; ", " " + day + " " + month + " " + year + " expense" + pd.getTotal());

                } else if ((endDatePnL.compareTo(actualDate) == 0)) {
                    if (pd.getType().matches("Taxes")) {
                        taxesTotalsPnL = taxesTotalsPnL + pd.getTotal();
                    } else {
                        sgaExpensesTotalPnL = sgaExpensesTotalPnL + pd.getTotal();
                    }
                    Log.i("date is same; ", " " + day + " " + month + " " + year + " expense" + pd.getTotal());

                }
            } else if (pd.getType().matches("Rent")){
                String startD = pd.getDate();

                String dat[] = startD.split("/");
                int startRDay = Integer.parseInt(dat[0]);
                int startRMonth = Integer.parseInt(dat[1]) - 1;
                int startRYear = Integer.parseInt(dat[2]);

                int months = Integer.parseInt(pd.getInfo());
                Log.d(TAG, "allExpenses Rent months " + months);

                int endDa = Integer.parseInt(dat[0]);
                int endM = Integer.parseInt(dat[1]) - 1 + months;
                int endY = Integer.parseInt(dat[2]);

                Calendar startRDate;
                startRDate = Calendar.getInstance();
                startRDate.set(Calendar.DAY_OF_MONTH, startRDay);
                startRDate.set(Calendar.MONTH, startRMonth);
                startRDate.set(Calendar.YEAR, startRYear);

                Calendar endRDate;
                endRDate = Calendar.getInstance();
                endRDate.set(Calendar.DAY_OF_MONTH, endDa);
                endRDate.set(Calendar.MONTH, endM);
                endRDate.set(Calendar.YEAR, endY);

                Log.i(TAG, "allExpenses Rent Doing expense start date " + startRDate.get(Calendar.DAY_OF_MONTH)
                        + " " + startRDate.get(Calendar.MONTH)
                        + " " + startRDate.get(Calendar.YEAR));

                Log.i(TAG, "allExpenses Rent Doing expense end date " + endRDate.get(Calendar.DAY_OF_MONTH)
                        + " " + endRDate.get(Calendar.MONTH)
                        + " " + endRDate.get(Calendar.YEAR));

                int totalDays = dayNumb(startRDay, startRMonth, startRYear, endDa, endM, endY) + 1;
                Log.d(TAG, "allExpenses Rent totalDays " + totalDays);

                if((startDatePnL.before(startRDate)) && endRDate.before(endDatePnL)){
                    Log.d(TAG, "allExpenses Rent inbetween " + pd.getTotal());
                    sgaExpensesTotalPnL = sgaExpensesTotalPnL + (int) Math.round(pd.getTotal());

                } else if((startRDate.before(startDatePnL)) && endDatePnL.before(endRDate)){
                    int passedDays = dayNumb(startDay, startMonth, startYear, endDay, endMonth, endYear) + 1;

                    double perDay = (double) pd.getTotal() / (double) totalDays;
                    double remove = perDay * (double) passedDays;

                    sgaExpensesTotalPnL = sgaExpensesTotalPnL + (int) Math.round(remove);
                    Log.d(TAG, "allExpenses Rent sarrounded passedDays " + passedDays + " perDay " + perDay + " remove " + remove);

                } else if ((startDatePnL.before(startRDate))){
                    int passedDays = dayNumb(startRDay, startRMonth, startRYear, endDay, endMonth, endYear) + 1;

                    double perDay = (double) pd.getTotal() / (double) totalDays;
                    double remove = perDay * (double) passedDays;

                    sgaExpensesTotalPnL = sgaExpensesTotalPnL + (int) Math.round(remove);
                    Log.d(TAG, "allExpenses Rent startbefor passedDays " + passedDays + " perDay " + perDay + " remove " + remove);

                } else if ((endRDate.before(endDatePnL))){
                    int passedDays = dayNumb(startDay, startMonth, startYear, endDa, endM, endY) + 1;

                    double perDay = (double) pd.getTotal() / (double) totalDays;
                    double remove = perDay * (double) passedDays;

                    sgaExpensesTotalPnL = sgaExpensesTotalPnL + (int) Math.round(remove);
                    Log.d(TAG, "allExpenses Rent end befor passedDays " + passedDays + " perDay " + perDay + " remove " + remove);

                }
            }
        }

        List<Owner> ownerList = odb.getAllOwner();

        for(Owner pd: ownerList){
            if (!pd.getType().matches("Add Funds")) {
                String date = pd.getDate();

                String dat[] = date.split("/");
                int actualDay = Integer.parseInt(dat[0]);
                int actualMonth = Integer.parseInt(dat[1]) - 1;
                int actualYear = Integer.parseInt(dat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
                actualDate.set(Calendar.MONTH, actualMonth);
                actualDate.set(Calendar.YEAR, actualYear);

                Log.i("Doing Owner date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(endDatePnL)) && (actualDate.after(startDatePnL)))) {
                    if (!(pd.getType().matches("Add Funds"))) {
                        String date2 = pd.getDate();

                        String dat2[] = date2.split("/");
                        int actualMonth2 = Integer.parseInt(dat2[1]) - 1;
                        int actualYear2 = Integer.parseInt(dat2[2]);

                        String name = "" + actualMonth2 + "-" + actualYear2;
                        boolean flag = false;

                        for (String key : ownerDrawPnl.keySet()) {
                            if (key.matches(name)) {
                                flag = true;
                            }
                        }

                        if (flag) {
                            int numb = pd.getTotal() + Integer.parseInt(ownerDrawPnl.get("" + name));

                            ownerDrawPnl.remove("" + name);
                            ownerDrawPnl.put("" + name, "" + numb);

                        } else if (!flag) {
                            ownerDrawPnl.put("" + name, "" + pd.getTotal());

                        }
                    }
                }
            }
        }

        PersonalData personalData = pddb.getPerson(1);

        int mSalary = personalData.getMDSalary();

        for(String key : ownerDrawPnl.keySet()){
            Log.d("PAndL", "date " + key + " total " + ownerDrawPnl.get(key));

            int numb = Integer.parseInt(ownerDrawPnl.get(key));

            if(numb > mSalary){
                mdSalaryTotalPnL = mdSalaryTotalPnL + mSalary;
            } else if(numb > 0){
                mdSalaryTotalPnL = mdSalaryTotalPnL + numb;
            }
            Log.d("PAndL", "numb " + numb + " tot " + mdSalaryTotalPnL);
        }

        List<Assets> assetsList = adb.getAllAssets();

        double assetsBefore = 0;
        double assetsAfter = 0;
        for (Assets pd : assetsList) {
            if (pd.getType().matches("Vehicle")
                    || pd.getType().matches("Equipment")
                    || pd.getType().matches("Other")
                    || pd.getType().matches("Licenses")) {
                String date = pd.getDate();

                String dat[] = date.split("/");
                int actualDay = Integer.parseInt(dat[0]);
                int actualMonth = Integer.parseInt(dat[1]) - 1;
                int actualYear = Integer.parseInt(dat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
                actualDate.set(Calendar.MONTH, actualMonth);
                actualDate.set(Calendar.YEAR, actualYear);

                String endDat[] = date.split("/");
                int stopDay = Integer.parseInt(endDat[0]);
                int stopMonth = Integer.parseInt(endDat[1]) - 1 + pd.getUsefull();
                int stopYear = Integer.parseInt(endDat[2]);

                Calendar stopDate;
                stopDate = Calendar.getInstance();
                stopDate.set(Calendar.DAY_OF_MONTH, stopDay);
                stopDate.set(Calendar.MONTH, stopMonth);
                stopDate.set(Calendar.YEAR, stopYear);

                stopDay = stopDate.get(Calendar.DAY_OF_MONTH);
                stopMonth = stopDate.get(Calendar.MONTH);
                stopYear = stopDate.get(Calendar.YEAR);

                Log.i("Doing payables date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(startDatePnL)))) {
                    if (pd.getType().matches("Vehicle")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double)pd.getTotal() - (double)pd.getScrsp())
                                / (daysTotal)) * (double)days));

                        Log.i(TAG, "Before Vehicle calcing;  total " + pd.getTotal()
                                + " minus " + (((((double) pd.getTotal()
                                - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                                + " days " + days
                                + " daysTot " + daysTotal
                                + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                        if (((double) pd.getTotal() - pass) <= (double) pd.getScrsp()){

                        } else {
                            assetsBefore = assetsBefore + pass;
                        }
                    } else if (pd.getType().matches("Equipment")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days));

                        Log.i(TAG, "Before Equipment calcing;  total " + pd.getTotal()
                                + " minus " + (((((double) pd.getTotal()
                                - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                                + " days " + days
                                + " daysTot " + daysTotal
                                + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                        if (((double) pd.getTotal() - pass) <= (double)pd.getScrsp()){

                        } else {
                            assetsBefore = assetsBefore + pass;
                        }
                    } else if (pd.getType().matches("Other")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double)pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double)days));

                        Log.i(TAG, "Before Other calcing;  total " + pd.getTotal()
                                + " minus " + (((((double) pd.getTotal()
                                - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                                + " days " + days
                                + " daysTot " + daysTotal
                                + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                        if ((pd.getTotal() - pass) <= pd.getScrsp()){

                        } else {
                            assetsBefore = assetsBefore + pass;
                        }
                    } else if (pd.getType().matches("Licenses")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                        Log.i(TAG, "Before Licenses calc total " + pd.getTotal()
                                + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
                                + " days " + days
                                + " per day " + ((double) pd.getTotal() / (daysTotal))
                                + " totaldays " + daysTotal);

                        if ((pd.getTotal() - pass) <= 0){

                        } else {
                            assetsBefore = assetsBefore + pass;
                        }
                    }

                    Log.i("date is btwn; ", " " + actualDay + " " + actualMonth + " " + actualYear + " amount" + pd.getTotal());
                }
                if (((actualDate.before(endDatePnL)))) {
                    if (pd.getType().matches("Vehicle")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days));

                        Log.i(TAG, "After Vehicle calcing;  total " + pd.getTotal()
                                + " minus " + (((((double) pd.getTotal()
                                - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                                + " days " + days
                                + " daysTot " + daysTotal
                                + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                        if (((double) pd.getTotal() - pass) <= (double) pd.getScrsp()){

                        } else {
                            assetsAfter = assetsAfter + pass;
                        }
                    } else if (pd.getType().matches("Equipment")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * days));

                        Log.i(TAG, "After Equipment calcing;  total " + pd.getTotal()
                                + " minus " + (((((double) pd.getTotal()
                                - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                                + " days " + days
                                + " daysTot " + daysTotal
                                + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                        if ((pd.getTotal() - pass) <= pd.getScrsp()){

                        } else {
                            assetsAfter = assetsAfter + pass;
                        }
                    } else if (pd.getType().matches("Other")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days));

                        Log.i(TAG, "After Other calcing;  total " + pd.getTotal()
                                + " minus " + (((((double) pd.getTotal()
                                - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                                + " days " + days
                                + " daysTot " + daysTotal
                                + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                        if ((pd.getTotal() - pass) <= pd.getScrsp()){

                        } else {
                            assetsAfter = assetsAfter + pass;
                        }
                    } else if (pd.getType().matches("Licenses")){
//                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
//                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;
//
//                        double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));
//
//                        Log.i("calcing; ", " total " + pd.getTotal()
//                                + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
//                                + " days " + days);

                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                        Log.i(TAG, "After Licenses calc total " + pd.getTotal()
                                + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
                                + " days " + days
                                + " per day " + ((double) pd.getTotal() / (daysTotal))
                                + " totaldays " + daysTotal);

                        if ((pd.getTotal() - pass) <= 0){

                        } else {
                            assetsAfter = assetsAfter + pass;
                        }
                    }

                    Log.i("date is btwn; ", " " + actualDay + " " + actualMonth + " " + actualYear + " amount" + pd.getTotal());
                }
            }
        }

        List<SoldAssets> soldAssetsList = sadb.getAllAssets();

        for (SoldAssets pd : soldAssetsList) {
            if (pd.getType().matches("Vehicle")
                    || pd.getType().matches("Equipment")
                    || pd.getType().matches("Other")
                    || pd.getType().matches("Licenses")) {
                String date = pd.getDate();
                String paidDate = pd.getSaleDate();

                String dat[] = date.split("/");
                int actualDay = Integer.parseInt(dat[0]);
                int actualMonth = Integer.parseInt(dat[1]) - 1;
                int actualYear = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
                actualDate.set(Calendar.MONTH, actualMonth);
                actualDate.set(Calendar.YEAR, actualYear);

                String endDat[] = date.split("/");
                int stopDay = Integer.parseInt(endDat[0]);
                int stopMonth = Integer.parseInt(endDat[1]) - 1 + pd.getUsefull();
                int stopYear = Integer.parseInt(endDat[2]);

                Calendar stopDate;
                stopDate = Calendar.getInstance();
                stopDate.set(Calendar.DAY_OF_MONTH, stopDay);
                stopDate.set(Calendar.MONTH, stopMonth);
                stopDate.set(Calendar.YEAR, stopYear);

                stopDay = stopDate.get(Calendar.DAY_OF_MONTH);
                stopMonth = stopDate.get(Calendar.MONTH);
                stopYear = stopDate.get(Calendar.YEAR);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing asset date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and sold date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDatePnL)) {
                    if (((actualDate.before(startDatePnL))) && (paidCDate.after(startDatePnL))) {
                        if (pd.getType().matches("Vehicle")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if ((pd.getTotal() - pass) <= pd.getScrsp()){

                            } else {
                                assetsBefore = assetsBefore + pass;
                            }
                        } else if (pd.getType().matches("Equipment")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if ((pd.getTotal() - pass) <= pd.getScrsp()){

                            } else {
                                assetsBefore = assetsBefore + pass;
                            }
                        } else if (pd.getType().matches("Other")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if ((pd.getTotal() - pass) <= pd.getScrsp()){

                            } else {
                                assetsBefore = assetsBefore + pass;
                            }
                        } else if (pd.getType().matches("Licenses")){
//                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
//                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;
//
//                            double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));
//
//                            Log.i("calcing; ", " total " + pd.getTotal()
//                                    + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
//                                    + " days " + days);

                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                            Log.i(TAG, "Licenses calc total " + pd.getTotal()
                                    + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
                                    + " days " + days
                                    + " per day " + ((double) pd.getTotal() / (daysTotal))
                                    + " totaldays " + daysTotal);

                            if ((pd.getTotal() - pass) <= 0){

                            } else {
                                assetsBefore = assetsBefore + pass;
                            }
                        }

                        Log.i("date is btwn; ", " " + actualDay + " " + actualMonth + " " + actualYear + " amount" + pd.getTotal());
                    }
                    if (((actualDate.before(endDatePnL))) && (paidCDate.after(endDatePnL))) {
                        if (pd.getType().matches("Vehicle")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if (pass <= pd.getScrsp()){

                            } else {
                                assetsAfter = assetsAfter + pass;
                            }
                        } else if (pd.getType().matches("Equipment")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if ((pd.getTotal() - pass) <= pd.getScrsp()){

                            } else {
                                assetsAfter = assetsAfter + pass;
                            }
                        } else if (pd.getType().matches("Other")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if ((pd.getTotal() - pass) <= pd.getScrsp()){

                            } else {
                                assetsAfter = assetsAfter + pass;
                            }
                        } else if (pd.getType().matches("Licenses")){
//                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
//                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;
//
//                            double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));
//
//                            Log.i("calcing; ", " total " + pd.getTotal()
//                                    + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
//                                    + " days " + days);

                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                            Log.i(TAG, "Licenses calc total " + pd.getTotal()
                                    + " minus " + (((double) pd.getTotal() / (daysTotal)) * (double) days)
                                    + " days " + days
                                    + " per day " + ((double) pd.getTotal() / (daysTotal))
                                    + " totaldays " + daysTotal);

                            if ((pd.getTotal() - pass) <= 0){

                            } else {
                                assetsAfter = assetsAfter + pass;
                            }
                        }

                        Log.i("date is btwn; ", " " + actualDay + " " + actualMonth + " " + actualYear + " amount" + pd.getTotal());
                    }
                }
            }
        }

        depreciationTotalPnL = Math.round(assetsAfter - assetsBefore);

        List<Loans> loans = ldb.getAllLoans();

        double interestBefore = 0;
        double interestAfter = 0;
        for(Loans pd: loans){
            if(pd.getStatus().toString().matches("NOT")){
                String startD = pd.getStartDate();

                String strdat[] = startD.split("/");
                int actualDay = Integer.parseInt(strdat[0]);
                int actualMonth = Integer.parseInt(strdat[1]) - 1;
                int actualYear = Integer.parseInt(strdat[2]);

                String endD = pd.getEndDate();

                String enddat[] = endD.split("/");
                int endDa = Integer.parseInt(enddat[0]);
                int endM = Integer.parseInt(enddat[1]) - 1;
                int endY = Integer.parseInt(enddat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
                actualDate.set(Calendar.MONTH, actualMonth);
                actualDate.set(Calendar.YEAR, actualYear);

                if (((actualDate.before(startDatePnL)))) {
                    double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                    Log.i("geto day tot st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday "
                            + endDa + " " + endM + " " + endY);

                    if(totalDays == 0){
                        totalDays = 1;
                    }

                    double days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                    Log.i("get day st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday "
                            + startDay + " " + startMonth + " " + startYear);

                    double loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());

                    double loanInterestPerDay = loanCost / totalDays;

                    double interest = loanInterestPerDay * days;

                    interestBefore = interestBefore + (interest);

                    Log.d(TAG, "Loan getInstalments " + pd.getInstalments());
                    Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
                    Log.d(TAG, "Loan getInstalments * getInstalmentsNo " + (pd.getInstalmentsNo() * pd.getInstalments()));
                    Log.d(TAG, "Loan getTotal " + pd.getTotal());
                    Log.d(TAG, "Loan loanCost " + loanCost);
                    Log.d(TAG, "Loan loanInterestPerDay " + loanInterestPerDay);
                    Log.d(TAG, "Loan interest " + interest);
                    Log.d(TAG, "Loan interestBefore " + interestBefore);

                    List<Instalment> instalments = ildb.getByForeignKey(pd.getID());

                    for(Instalment pd1: instalments) {
                        String strdat2[] = pd1.getDate().split("/");
                        int actualDay2 = Integer.parseInt(strdat2[0]);
                        int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                        int actualYear2 = Integer.parseInt(strdat2[2]);

                        Calendar actualDate2;
                        actualDate2 = Calendar.getInstance();
                        actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                        actualDate2.set(Calendar.MONTH, actualMonth2);
                        actualDate2.set(Calendar.YEAR, actualYear2);

                        if (actualDate2.before(startDatePnL)) {
                            interestBefore = interestBefore - (interest / days);

                            Log.i(TAG, " interest / days " + (interest / days));
                            Log.i(TAG, " interestBefore " + interestBefore);
                        }
                    }
                }
                if ((actualDate.before(endDatePnL))) {
                    double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                    Log.i("get day tot en; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                            + endDa + " " + endM + " " + endY);

                    if(totalDays == 0){
                        totalDays = 1;
                    }

                    double days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;

                    Log.i("get day en; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday "
                            + endDay + " " + endMonth + " " + endYear);

                    double loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());

                    double loanInterestPerDay = loanCost / totalDays;

                    double interest = loanInterestPerDay * days;

                    interestAfter = interestAfter + (interest);

                    Log.d(TAG, "Loan getInstalments " + pd.getInstalments());
                    Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
                    Log.d(TAG, "Loan getInstalments * getInstalmentsNo " + (pd.getInstalmentsNo() * pd.getInstalments()));
                    Log.d(TAG, "Loan getTotal " + pd.getTotal());
                    Log.d(TAG, "Loan loanCost " + loanCost);
                    Log.d(TAG, "Loan loanInterestPerDay " + loanInterestPerDay);
                    Log.d(TAG, "Loan interest " + interest);
                    Log.d(TAG, "Loan interestAfter " + interestAfter);

                    List<Instalment> instalments = ildb.getByForeignKey(pd.getID());

                    for(Instalment pd1: instalments) {
                        String strdat2[] = pd1.getDate().split("/");
                        int actualDay2 = Integer.parseInt(strdat2[0]);
                        int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                        int actualYear2 = Integer.parseInt(strdat2[2]);

                        Calendar actualDate2;
                        actualDate2 = Calendar.getInstance();
                        actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                        actualDate2.set(Calendar.MONTH, actualMonth2);
                        actualDate2.set(Calendar.YEAR, actualYear2);

                        if (actualDate2.before(endDatePnL)) {
                            interestAfter = interestAfter - (interest / days);

                            Log.i(TAG, " interest / days " + (interest / days));
                            Log.i(TAG, " interestAfter " + interestAfter);
                        }
                    }
                }
            }
        }

        for(Loans pd: loans){
            if(pd.getStatus().toString().matches("PAY")) {
                String startD = pd.getStartDate();

                String strdat[] = startD.split("/");
                int actualDay = Integer.parseInt(strdat[0]);
                int actualMonth = Integer.parseInt(strdat[1]) - 1;
                int actualYear = Integer.parseInt(strdat[2]);

                String endD = pd.getEndDate();

                String enddat[] = endD.split("/");
                int endDa = Integer.parseInt(enddat[0]);
                int endM = Integer.parseInt(enddat[1]) - 1;
                int endY = Integer.parseInt(enddat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
                actualDate.set(Calendar.MONTH, actualMonth);
                actualDate.set(Calendar.YEAR, actualYear);

//                setMonth(actualDate, actualMonth);

                List<Instalment> instalments1 = ildb.getByForeignKey(pd.getID());

                Instalment instalment = new Instalment();

                for (Instalment pd1 : instalments1) {
                    if (pd1.getNumber().matches("Last")){
                        instalment = pd1;
                    }
                }

                String paidDat[] = instalment.getDate().split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

//                setMonth(paidCDate, paidMonth);

                if (paidCDate.after(startDatePnL)) {
                    if (((actualDate.before(startDatePnL)))) {
                        double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                        Log.i("geto day tot st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday "
                                + endDa + " " + endM + " " + endY);

                        if(totalDays == 0){
                            totalDays = 1;
                        }

                        double days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        Log.i("get day st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday "
                                + startDay + " " + startMonth + " " + startYear);

                        double loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());

                        double loanInterestPerDay = loanCost / totalDays;

                        double interest = loanInterestPerDay * days;

                        interestBefore = interestBefore + (interest);

                        Log.d(TAG, "Loan getInstalments " + pd.getInstalments());
                        Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
                        Log.d(TAG, "Loan getInstalments * getInstalmentsNo " + (pd.getInstalmentsNo() * pd.getInstalments()));
                        Log.d(TAG, "Loan getTotal " + pd.getTotal());
                        Log.d(TAG, "Loan loanCost " + loanCost);
                        Log.d(TAG, "Loan loanInterestPerDay " + loanInterestPerDay);
                        Log.d(TAG, "Loan interest " + interest);
                        Log.d(TAG, "Loan interestBefore " + interestBefore);

                        List<Instalment> instalments = ildb.getByForeignKey(pd.getID());

                        for(Instalment pd1: instalments) {
                            String strdat2[] = pd1.getDate().split("/");
                            int actualDay2 = Integer.parseInt(strdat2[0]);
                            int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                            int actualYear2 = Integer.parseInt(strdat2[2]);

                            Calendar actualDate2;
                            actualDate2 = Calendar.getInstance();
                            actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                            actualDate2.set(Calendar.MONTH, actualMonth2);
                            actualDate2.set(Calendar.YEAR, actualYear2);

                            if (actualDate2.before(startDatePnL)) {
                                interestBefore = interestBefore - (interest / days);

                                Log.i(TAG, " interest / days " + (interest / days));
                                Log.i(TAG, " interestBefore " + interestBefore);
                            }
                        }
                    }
                    if ((actualDate.before(endDatePnL))) {
                        double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                        Log.i("get day tot en; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                                + endDa + " " + endM + " " + endY);

                        if(totalDays == 0){
                            totalDays = 1;
                        }

                        double days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;

                        Log.i("get day en; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday "
                                + endDay + " " + endMonth + " " + endYear);

                        double loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());

                        double loanInterestPerDay = loanCost / totalDays;

                        double interest = loanInterestPerDay * days;

                        interestAfter = interestAfter + (interest);

                        Log.d(TAG, "Loan getInstalments " + pd.getInstalments());
                        Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
                        Log.d(TAG, "Loan getInstalments * getInstalmentsNo " + (pd.getInstalmentsNo() * pd.getInstalments()));
                        Log.d(TAG, "Loan getTotal " + pd.getTotal());
                        Log.d(TAG, "Loan loanCost " + loanCost);
                        Log.d(TAG, "Loan loanInterestPerDay " + loanInterestPerDay);
                        Log.d(TAG, "Loan interest " + interest);
                        Log.d(TAG, "Loan interestAfter " + interestAfter);

                        List<Instalment> instalments = ildb.getByForeignKey(pd.getID());

                        for(Instalment pd1: instalments) {
                            String strdat2[] = pd1.getDate().split("/");
                            int actualDay2 = Integer.parseInt(strdat2[0]);
                            int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                            int actualYear2 = Integer.parseInt(strdat2[2]);

                            Calendar actualDate2;
                            actualDate2 = Calendar.getInstance();
                            actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                            actualDate2.set(Calendar.MONTH, actualMonth2);
                            actualDate2.set(Calendar.YEAR, actualYear2);

                            if (actualDate2.before(endDatePnL)) {
                                interestAfter = interestAfter - (interest / days);

                                Log.i(TAG, " interest / days " + (interest / days));
                                Log.i(TAG, " interestAfter " + interestAfter);
                            }
                        }
                    }
                }
            }
        }


        //********************************************************************************************************

//        for(Loans pd : loans) {
//            if (pd.getStatus().toString().matches("NOT")) {
//                Log.d(TAG, " PnL Start of loan loop NOT");
//                String startD = pd.getStartDate();
//
//                String strdat[] = startD.split("/");
//                int actualDay = Integer.parseInt(strdat[0]);
//                int actualMonth = Integer.parseInt(strdat[1]) - 1;
//                int actualYear = Integer.parseInt(strdat[2]);
//
//                String endD = pd.getEndDate();
//
//                String enddat[] = endD.split("/");
//                int endDa = Integer.parseInt(enddat[0]);
//                int endM = Integer.parseInt(enddat[1]) - 1;
//                int endY = Integer.parseInt(enddat[2]);
//
//                Calendar actualDate;
//                actualDate = Calendar.getInstance();
//                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
//                actualDate.set(Calendar.MONTH, actualMonth);
//                actualDate.set(Calendar.YEAR, actualYear);
//
////                setMonth(actualDate, actualMonth);
//
//                List<Instalment> instalments = ildb.getByForeignKey(pd.getID());
//
////            String paidDat[] = instalments.get(instalments.size() - 1).getDate().split("/");
////            int paidDay = Integer.parseInt(paidDat[0]);
////            int paidMonth = Integer.parseInt(paidDat[1]);
////            int paidYear = Integer.parseInt(paidDat[2]);
////
////            Calendar paidCDate;
////            paidCDate = Calendar.getInstance();
////            paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
////            paidCDate.set(Calendar.YEAR, paidYear);
////
////            setMonth(paidCDate, paidMonth);
//
//                if (endDatePnL.after(actualDate)) {
//
//                    int totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY);
//                    Log.i("get day tot st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                            + endDa + " " + endM + " " + endY);
//
//                    if (totalDays == 0) {
//                        totalDays = 1;
//                    }
//                    totalDays = totalDays + 1;
//
//                    Log.d(TAG, "Loan totalDays " + totalDays);
//
////                int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear);
//                    int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear);
//
//                    if (days > totalDays) {
//                        days = totalDays;
//
//                    }
//
//                    Log.d(TAG, "Loan days " + days);
//
//                    Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                            + startDay + " " + startMonth + " " + startYear);
//
//                    int loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());
//
//                    double loanInterestPerDay = (double) loanCost / (double) totalDays;
//
//                    double interest = loanInterestPerDay * days;
//
//                    Log.d(TAG, "Loan getInstalments " + pd.getInstalments());
//                    Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
//                    Log.d(TAG, "Loan loanCost " + loanCost);
//                    Log.d(TAG, "Loan loanInterestPerDay " + loanInterestPerDay);
//                    Log.d(TAG, "Loan interest " + interest);
//
//                    Log.i("calcinginst; ", " actual ins " + (pd.getInstalments())
//                            + " total payback " + ((pd.getInstalments()) * pd.getInstalmentsNo())
//                            + " interest " + (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
//                            + " per day " + ((((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
//                            / totalDays)
//                            + " now " + ((((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
//                            / totalDays) * days);
//
//                    interestTotalPnL = interestTotalPnL + (interest);
//                    Log.i("int B PY; ", " " + interestTotalPnL);
//
//                    Log.i("calcingloan; ", " days " + days
//                            + " totalDays " + totalDays
//                            + " interest " + interest
//                            + " loanPayableTotal " + interestBefore);
//
//                    for (Instalment pd1 : instalments) {
//                        String strdat2[] = pd1.getDate().split("/");
//                        int actualDay2 = Integer.parseInt(strdat2[0]);
//                        int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
//                        int actualYear2 = Integer.parseInt(strdat2[2]);
//
//                        Calendar actualDate2;
//                        actualDate2 = Calendar.getInstance();
//                        actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
//                        actualDate2.set(Calendar.MONTH, actualMonth2);
//                        actualDate2.set(Calendar.YEAR, actualYear2);
//
////                        setMonth(actualDate2, actualMonth2);
//
//                        if (actualDate2.before(endDatePnL)) {
//                            double perc = ((double) (pd.getInstalments()) * (double) pd.getInstalmentsNo());
//                            double send = (((perc - (double) pd.getTotal()) / perc) * (double) pd1.getTotal());
//                            Log.i("calcingInte%; ", " tot " + ((pd.getInstalments()) * pd.getInstalmentsNo())
//                                    + " divid " + (perc - (double) pd.getTotal())
//                                    + " send " + (((perc - (double) pd.getTotal()) / perc) * (double) pd1.getTotal()));
//
//                            int days2 = dayNumb(actualDay, actualMonth, actualYear, actualDay2, actualMonth2, actualYear2);
//
//                            Log.d(TAG, "Loan days " + days);
//
//                            Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                    + startDay + " " + startMonth + " " + startYear);
//
//                            int instalCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());
//
//                            double instalInterestPerDay = (double) loanCost / (double) totalDays;
//
//                            double instal = loanInterestPerDay * days;
//
//                            Log.d(TAG, "Loan days 2 " + days2);
//                            Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
//                            Log.d(TAG, "Loan instalCost " + instalCost);
//                            Log.d(TAG, "Loan instalInterestPerDay " + instalInterestPerDay);
//                            Log.d(TAG, "Loan instal " + instal);
//
//                            int days3 = dayNumb(startDay, startMonth, startYear, actualDay2, actualMonth2, actualYear2);
//
//                            if (days3 < 0) {
//                                days3 = 0;
//
//                            }
//
//                            Log.d(TAG, "Loan days " + days);
//
//                            Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                    + startDay + " " + startMonth + " " + startYear);
//
//                            double addiional = loanInterestPerDay * days3;
//
//                            Log.d(TAG, "Loan days3 " + days3);
//                            Log.d(TAG, "Loan addiional " + addiional);
//
//                            int days4 = dayNumb(actualDay2, actualMonth2, actualYear2, endDay, endMonth, endYear);
//
//                            if (days4 < 0) {
//                                days4 = 0;
//
//                            }
//
//                            Log.d(TAG, "Loan days " + days);
//
//                            Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                    + startDay + " " + startMonth + " " + startYear);
//
//                            double addiionalAfter = loanInterestPerDay * days4;
//
//                            Log.d(TAG, "Loan days3 " + days3);
//                            Log.d(TAG, "Loan addiionalAfter " + addiionalAfter);
//
//                            interestTotalPnL = (interestTotalPnL - instal) + addiional + addiionalAfter;
//                            Log.i("int B PY; ", " " + interestBefore);
//                        }
//                    }
//
//                }
//            }
//        }
//
//        for(Loans pd : loans) {
//            if (pd.getStatus().toString().matches("PAY")) {
//                Log.d(TAG, " PnL Start of loan loop Pay");
//                String startD = pd.getStartDate();
//
//                String strdat[] = startD.split("/");
//                int actualDay = Integer.parseInt(strdat[0]);
//                int actualMonth = Integer.parseInt(strdat[1]) - 1;
//                int actualYear = Integer.parseInt(strdat[2]);
//
//                String endD = pd.getEndDate();
//
//                String enddat[] = endD.split("/");
//                int endDa = Integer.parseInt(enddat[0]);
//                int endM = Integer.parseInt(enddat[1]) - 1;
//                int endY = Integer.parseInt(enddat[2]);
//
//                Calendar actualDate;
//                actualDate = Calendar.getInstance();
//                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
//                actualDate.set(Calendar.MONTH, actualMonth);
//                actualDate.set(Calendar.YEAR, actualYear);
//
////                setMonth(actualDate, actualMonth);
//
//                List<Instalment> instalments = ildb.getByForeignKey(pd.getID());
//
//                String paidDat[] = instalments.get(instalments.size() - 1).getDate().split("/");
//                int paidDay = Integer.parseInt(paidDat[0]);
//                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
//                int paidYear = Integer.parseInt(paidDat[2]);
//
//                Calendar paidCDate;
//                paidCDate = Calendar.getInstance();
//                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
//                paidCDate.set(Calendar.MONTH, paidMonth);
//                paidCDate.set(Calendar.YEAR, paidYear);
//
////                setMonth(paidCDate, paidMonth);
//
//                if (startDatePnL.before(paidCDate)) {
//                    if (endDatePnL.after(actualDate)) {
//
//                        int totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY);
//                        Log.i("get day tot st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                + endDa + " " + endM + " " + endY);
//
//                        if (totalDays == 0) {
//                            totalDays = 1;
//                        }
//                        totalDays = totalDays + 1;
//
//                        Log.d(TAG, "Loan totalDays " + totalDays);
//
//                        int days;
//
//                        if (endDatePnL.before(paidCDate)) {
//                            days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear);
//
//                        } else {
//                            days = dayNumb(actualDay, actualMonth, actualYear, paidDay, paidMonth, paidYear);
//
//                        }
//
//                        if (days > totalDays) {
//                            days = totalDays;
//
//                        }
//
//                        Log.d(TAG, "Loan days " + days);
//
//                        Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                + startDay + " " + startMonth + " " + startYear);
//
//                        int loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());
//
//                        double loanInterestPerDay = (double) loanCost / (double) totalDays;
//
//                        double interest = loanInterestPerDay * days;
//
//                        Log.d(TAG, "Loan getInstalments " + pd.getInstalments());
//                        Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
//                        Log.d(TAG, "Loan loanCost " + loanCost);
//                        Log.d(TAG, "Loan loanInterestPerDay " + loanInterestPerDay);
//                        Log.d(TAG, "Loan interest " + interest);
//
//                        Log.i("calcinginst; ", " actual ins " + (pd.getInstalments())
//                                + " total payback " + ((pd.getInstalments()) * pd.getInstalmentsNo())
//                                + " interest " + (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
//                                + " per day " + ((((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
//                                / totalDays)
//                                + " now " + ((((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
//                                / totalDays) * days);
//
//
//
//                        interestTotalPnL = interestTotalPnL + (interest);
//                        Log.i("int B PY; ", " " + interestTotalPnL);
//
//                        Log.i("calcingloan; ", " days " + days
//                                + " totalDays " + totalDays
//                                + " interest " + interest
//                                + " loanPayableTotal " + interestBefore);
//
//                        for (Instalment pd1 : instalments) {
//                            String strdat2[] = pd1.getDate().split("/");
//                            int actualDay2 = Integer.parseInt(strdat2[0]);
//                            int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
//                            int actualYear2 = Integer.parseInt(strdat2[2]);
//
//                            Calendar actualDate2;
//                            actualDate2 = Calendar.getInstance();
//                            actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
//                            actualDate2.set(Calendar.MONTH, actualMonth2);
//                            actualDate2.set(Calendar.YEAR, actualYear2);
////
//////                            setMonth(actualDate2, actualMonth2);
////
////                            if (actualDate2.before(endDatePnL)) {
//////                                double perc = ((pd.getInstalments()) * pd.getInstalmentsNo());
//////                                double send = (((perc - (double) pd.getTotal()) / perc) * (double) pd1.getTotal());
//////                                Log.i("calcingInte%; ", " tot " + ((pd.getInstalments()) * pd.getInstalmentsNo())
//////                                        + " divid " + (perc - (double) pd.getTotal())
//////                                        + " send " + (((perc - (double) pd.getTotal()) / perc) * (double) pd1.getTotal()));
////
////                                days = dayNumb(actualDay, actualMonth, actualYear, actualDay2, actualMonth2, actualYear2);
////
////                                if (days > totalDays) {
////                                    days = totalDays;
////
////                                }
////
////                                Log.d(TAG, "Loan days " + days);
////
////                                Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
////                                        + startDay + " " + startMonth + " " + startYear);
////
////                                loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());
////
////                                loanInterestPerDay = (double) loanCost / (double) totalDays;
////
////                                interest = loanInterestPerDay * days;
////
////                                interestTotalPnL = interestTotalPnL - (interest);
////                                Log.i("int B PY; ", " " + interestBefore);
////                            }
////
////                            actualDay = actualDay2;
////                            actualYear = actualYear2;
////                            actualMonth = actualMonth2;
//
//
//                            if (actualDate2.before(endDatePnL)) {
//                                double perc = ((double) (pd.getInstalments()) * (double) pd.getInstalmentsNo());
//                                double send = (((perc - (double) pd.getTotal()) / perc) * (double) pd1.getTotal());
//                                Log.i("calcingInte%; ", " tot " + ((pd.getInstalments()) * pd.getInstalmentsNo())
//                                        + " divid " + (perc - (double) pd.getTotal())
//                                        + " send " + (((perc - (double) pd.getTotal()) / perc) * (double) pd1.getTotal()));
//
//                                int days2 = dayNumb(actualDay, actualMonth, actualYear, actualDay2, actualMonth2, actualYear2);
//
//                                Log.d(TAG, "Loan days " + days);
//
//                                Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                        + startDay + " " + startMonth + " " + startYear);
//
//                                int instalCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());
//
//                                double instalInterestPerDay = (double) loanCost / (double) totalDays;
//
//                                double instal = loanInterestPerDay * days;
//
//                                Log.d(TAG, "Loan days 2 " + days2);
//                                Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
//                                Log.d(TAG, "Loan instalCost " + instalCost);
//                                Log.d(TAG, "Loan instalInterestPerDay " + instalInterestPerDay);
//                                Log.d(TAG, "Loan instal " + instal);
//
//                                int days3 = dayNumb(startDay, startMonth, startYear, actualDay2, actualMonth2, actualYear2);
//
//                                if (days3 < 0) {
//                                    days3 = 0;
//
//                                }
//
//                                Log.d(TAG, "Loan days " + days);
//
//                                Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                        + startDay + " " + startMonth + " " + startYear);
//
//                                double addiional = loanInterestPerDay * days3;
//
//                                Log.d(TAG, "Loan days3 " + days3);
//                                Log.d(TAG, "Loan addiional " + addiional);
//
//                                int days4 = dayNumb(actualDay2, actualMonth2, actualYear2, endDay, endMonth, endYear);
//
//                                if (days4 < 0) {
//                                    days4 = 0;
//
//                                }
//
//                                Log.d(TAG, "Loan days " + days);
//
//                                Log.i("get day st P; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
//                                        + startDay + " " + startMonth + " " + startYear);
//
//                                double addiionalAfter = loanInterestPerDay * days4;
//
//                                Log.d(TAG, "Loan days3 " + days3);
//                                Log.d(TAG, "Loan addiionalAfter " + addiionalAfter);
//
//                                interestTotalPnL = (interestTotalPnL - instal) + addiional + addiionalAfter;
//                                Log.i("int B PY; ", " " + interestBefore);
//                            }
//                        }
//                    }
//                }
//            }
//        }

        interestTotalPnL = Math.round(interestAfter - interestBefore);
        Log.i("calc int tot; ", " " + interestAfter + " - " + interestBefore + " = " + interestTotalPnL);

        interestTotalPnL =  Math.round(interestTotalPnL);

        /**
         * end of fetching and totals
         **/
        grossMarginTotalPnL = salesTotalPnL - cogsTotalPnL + assetGainsTotalsPnL;
        ebitdaTotalsPnL = grossMarginTotalPnL - sgaExpensesTotalPnL - mdSalaryTotalPnL;
        operatingProfitsTotalPnL = ebitdaTotalsPnL - depreciationTotalPnL;
        netProfitTotalsPnL = operatingProfitsTotalPnL - taxesTotalsPnL - interestTotalPnL;

        //setting totals in view
        sales.setText("" + numberFormat.format((salesTotalPnL)));
        cogs.setText("" + numberFormat.format(cogsTotalPnL));
        assetGains.setText("" + numberFormat.format(assetGainsTotalsPnL));
        gross_margin.setText("" + (numberFormat.format(grossMarginTotalPnL)));
        sgaExpenses.setText("" + (numberFormat.format(sgaExpensesTotalPnL)));
        mdSalary.setText("" + (numberFormat.format(mdSalaryTotalPnL)));
        ebitda.setText("" + (numberFormat.format(ebitdaTotalsPnL)));
        depreciation.setText("" + (numberFormat.format(depreciationTotalPnL)));
        operatingProfits.setText("" + (numberFormat.format(operatingProfitsTotalPnL)));
        interest.setText("" + (numberFormat.format(interestTotalPnL)));
        tax.setText("" + (numberFormat.format(taxesTotalsPnL)));
        netProfit.setText("" + (numberFormat.format(netProfitTotalsPnL)));

    }

    /*
    setting month
     */

//    public Calendar setMonth(Calendar date, int month){
//
//        if(month == 1){
//            date.set(Calendar.MONTH, Calendar.JANUARY);
//        } else if(month == 2){
//            date.set(Calendar.MONTH, Calendar.FEBRUARY);
//        } else if(month == 3){
//            date.set(Calendar.MONTH, Calendar.MARCH);
//        } else if(month == 4){
//            date.set(Calendar.MONTH, Calendar.APRIL);
//        } else if(month == 5){
//            date.set(Calendar.MONTH, Calendar.MAY);
//        } else if(month == 6){
//            date.set(Calendar.MONTH, Calendar.JUNE);
//        } else if(month == 7){
//            date.set(Calendar.MONTH, Calendar.JULY);
//        } else if(month == 8){
//            date.set(Calendar.MONTH, Calendar.AUGUST);
//        } else if(month == 9){
//            date.set(Calendar.MONTH, Calendar.SEPTEMBER);
//        } else if(month == 10){
//            date.set(Calendar.MONTH, Calendar.OCTOBER);
//        } else if(month == 11){
//            date.set(Calendar.MONTH, Calendar.NOVEMBER);
//        } else if(month == 12){
//            date.set(Calendar.MONTH, Calendar.DECEMBER);
//        } else {
//            date.set(Calendar.MONTH, 12);
//        }
//
//        return date;
//    }

    /*
    end of setting month
     */

    /*
    getting number of months
     */
//
//    public int monthNumb(int startMonth, int startYear, int endMonth, int endYear){
//        int numb = 1;
//
//        int years = endYear - startYear;
//
//        Log.i("calc months start; ", "numb " + numb + " strt year" + startYear + " endeyar" + endYear
//                + " stsrt mnth" + startMonth + " end mnth" + endMonth);
//
//        if(years > 1){
//            numb = numb + (12 * (years - 1));
//            numb = numb + (12 - startMonth) + (endMonth);
//            Log.i("calc months >1; ", "numb " + numb + " strt year" + startYear + " endeyar" + endYear
//                    + " stsrt mnth" + startMonth + " end mnth" + endMonth);
//        } else if (years == 0){
//            numb = numb + (endMonth - startMonth);
//            Log.i("calc months ==0; ", "numb " + numb + " strt year" + startYear + " endeyar" + endYear
//                    + " stsrt mnth" + startMonth + " end mnth" + endMonth);
//        } else if (years == 1){
//            numb = numb + (12 - startMonth) + (endMonth);
//            Log.i("calc months ==1; ", "numb " + numb + " strt year" + startYear + " endeyar" + endYear
//                    + " stsrt mnth" + startMonth + " end mnth" + endMonth);
//        }
//
//        if (numb < 0){
//            numb = 0;
//        }
//
//        return numb;
//    }

//    public int dayNumb(double startDay, double startMonth, double startYear, double endDay, double endMonth, double endYear){
//
//        double numb = 0;
//
//        double maxDays = 365.2;
//        double daysPer = 30.4375;
//        double years = endYear - startYear;
//        double months = endMonth - startMonth;
//
//        if (years == 0 && months == 0){
//            numb = endDay - startDay;
//        } else if (months == 1 && years ==0){
//            numb = (daysPer - startDay) + (endDay);
//        } else if (months > 1 && years == 0){
//            numb = ((endMonth - startMonth - 1) * daysPer) + (daysPer - startDay) + (endDay);
//        } else if (years >= 1){
//            months = (endMonth + (12 * (endYear - startYear))) - startMonth - 1;
//
//            if (months == 0){
//                numb = (daysPer - startDay) + (endDay);
//                Log.i("calcing2; ", " diff is 1 " + numb);
//            } else if (months > 0) {
//                numb = (((endMonth + (12 * (endYear - startYear))) - startMonth - 1) * daysPer) + (daysPer - startDay) + (endDay);
//                Log.i("calcing2; ", " diff is > 1 " + numb);
//            }
//
//            Log.i("calcing2; ", " months " + months);
//        }
//
//        int ret = (int) Math.round(numb);
//
//        return ret;
//    }

    public int dayNumb(double startDay, double startMonth, double startYear, double endDay, double endMonth, double endYear){
        endMonth = endMonth + 1;
        startMonth = startMonth + 1;

        double numb = 0;
        double years = endYear - startYear;
        double months = endMonth - startMonth;

        if (years == 0 && months == 0){
            Log.i(TAG, "In same month");
            Log.i(TAG, "diff between " + endDay + " and " + startDay + " is " + (endDay - startDay));
            numb = endDay - startDay;

        } else if (months == 1 && years ==0){
            Log.i(TAG, "in same year and adjusent months");
            Log.i(TAG, "diff between " + (daysPerMonth((int) (startMonth), (int) (startYear)))
                    + " and " + startDay + " plus " + endDay + " is "
                    + (((daysPerMonth((int) (startMonth), (int) (startYear))) - startDay) + (endDay)));
            int monthDays = daysPerMonth((int) (startMonth), (int) (startYear));
            numb = (monthDays - startDay) + (endDay);

        } else if (months > 1 && years == 0){
            Log.i(TAG, "same year months greater than 1" );
            int startMonthDays = daysPerMonth((int) (startMonth), (int) (startYear));
            Log.i(TAG, "add to day " + startMonthDays + " minus " + startDay);
            numb = numb + (startMonthDays - startDay);
            Log.i(TAG, "add to day enddays " + endDay);
            numb = numb + (endDay);

            for (int i = ((int)startMonth + 1); i < endMonth; i++){
                Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (startYear)));
                numb = numb + daysPerMonth(i, (int) (startYear));

            }

        } else if (years >= 1){
            Log.i(TAG, "diferent years" );
            Log.i(TAG, "months number is " + endMonth + " plus " + ((12 * (endYear - startYear))) + " minus " + startMonth + " minus 1 "
                    + " is " + ((endMonth + (12 * (endYear - startYear))) - startMonth - 1));
            months = (endMonth + (12 * (endYear - startYear))) - startMonth - 1;

            if (months == 0){
                Log.i(TAG, "adjustent months" );
                Log.i(TAG, "diff between " + (daysPerMonth((int) (startMonth), (int) (startYear)))
                        + " and " + startDay + " plus " + endDay + " is "
                        + (((daysPerMonth((int) (startMonth), (int) (startYear))) - startDay) + (endDay)));
                int monthDays = daysPerMonth((int) (startMonth), (int) (startYear));
                numb = (monthDays - startDay) + (endDay);

            } else if (months > 0) {
                Log.i(TAG, "many months" );
                if (years > 1){
                    Log.i(TAG, "many years" );
                    for (int i = ((int) startYear + 1); i < endYear; i++) {
                        for (int j = 1; j <= 12; j++){
                            numb = numb + daysPerMonth(j, i);

                        }
                    }

                    int startMonthDays = daysPerMonth((int) (startMonth), (int) (startYear));
                    Log.i(TAG, "add to day " + startMonthDays + " minus " + startDay);
                    numb = numb + (startMonthDays - startDay);

                    for (int i = ((int)startMonth + 1); i <= 12; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (startYear)));
                        numb = numb + daysPerMonth(i, (int) (startYear));

                    }

                    Log.i(TAG, "add to day enddays " + endDay);
                    numb = numb + (endDay);

                    for (int i = 1; i < (int)endMonth; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (endYear)));
                        numb = numb + daysPerMonth(i, (int) (endYear));

                    }
                } else {
                    Log.i(TAG, "adjusent year" );
                    int startMonthDays = daysPerMonth((int) (startMonth), (int) (startYear));
                    Log.i(TAG, "add to day " + startMonthDays + " minus " + startDay);
                    numb = numb + (startMonthDays - startDay);

                    for (int i = ((int)startMonth + 1); i <= 12; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (startYear)));
                        numb = numb + daysPerMonth(i, (int) (startYear));

                    }

                    Log.i(TAG, "add to day enddays " + endDay);
                    numb = numb + (endDay);

                    for (int i = 1; i < (int)endMonth; i++){
                        Log.i(TAG, "add to day for month " + i + " days " + daysPerMonth(i, (int) (endYear)));
                        numb = numb + daysPerMonth(i, (int) (endYear));

                    }
                }
            }

            Log.i("calcing2; ", " months " + months);
        }

        Log.i(TAG, "diff between " + startDay + "/" + startMonth + "/" + startYear + " and " + endDay
                + "/" + endMonth + "/" + endYear + " is " + numb + " rounded off is " + ((int) Math.round(numb)));
        int ret = (int) Math.round(numb);

        Log.i(TAG, "Therefore DaysNumb is " + ret + " - 1");

        if(ret > 0) {
            ret = ret - 1;
        }

        return ret;
    }

    public int daysPerMonth(int month, int year){
        int numb;

        if (month  == 1 || month  == 3 || month  == 5 || month  == 7 || month  == 8 || month  == 10 || month  == 12){
            numb = 31;

        } else if (month == 2){
            if (year % 4 == 0){
                numb = 29;

            } else {
                numb = 28;

            }
        } else {
            numb = 30;

        }

        Log.d(TAG, "For month " + month + " in year " + year + " days are " + numb);
        return numb;
    }

    /*
    end of number of monthd
     */

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

}
