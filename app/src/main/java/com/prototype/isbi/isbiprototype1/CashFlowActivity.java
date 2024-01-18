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

import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGains;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Assets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Instalment;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Owner;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalData;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchased;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Sales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Delivery;
import com.prototype.isbi.isbiprototype1.databaseHandlers.DeliveryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrder;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSale;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
//import com.prototype.isbi.isbiprototype1.databaseHandlers.ClosedPreOrderHandler;
//import com.prototype.isbi.isbiprototype1.databaseHandlers.ClosedPreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUp;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 1/15/2017.
 */

public class CashFlowActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String TAG = "CashFlowActivity";

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

    HashMap<String, String> ownerDraw = new HashMap<>();
    HashMap<String, String> ownerDrawPnl = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    DecimalFormat decimalFormat = new DecimalFormat("0.##");

    SalesHandler sdb = new SalesHandler(this);
    GoodsSalesHandler gsdb = new GoodsSalesHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    ReceivablesHandler rdb = new ReceivablesHandler(this);
    PayablesHandler pdb = new PayablesHandler(this);
    PaidReceivablesHandler prdb = new PaidReceivablesHandler(this);
    PaidPayablesHandler ppdb = new PaidPayablesHandler(this);
    GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);
    DeliveryHandler ddb = new DeliveryHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    GoodsPreOrderedHandler gpodb = new GoodsPreOrderedHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);
    OwnerHandler odb = new OwnerHandler(this);
    PersonalDataHandler pddb = new PersonalDataHandler(this);
    AssetsHandler adb = new AssetsHandler(this);
    SoldAssetsHandler sadb = new SoldAssetsHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    AssetGainsHandler agdb = new AssetGainsHandler(this);
    ReUpHandler rudb = new ReUpHandler(this);
    LoansHandler ldb = new LoansHandler(this);
    InstalmentHandler ildb = new InstalmentHandler(this);

    double receivablesTotal = 0, payablesTotals = 0, inventoryTotals = 0, netCashOperationsTotal = 0,
            preSaleTotals = 0, preOrderTotals = 0, ownerDrawingTotal = 0, depreciationTotal = 0,
            equipmentTotal = 0, othersTotal = 0, buildingsTotals = 0, vehiclesTotals = 0, rightsTotals = 0, landTotals = 0,
            netCashInvestingTotal = 0, loansTotals = 0,  contractualSavingsTotals = 0, netCashFinancingTotal = 0,
            netCashBusinessTotal = 0, totalNetCashTotals = 0, loanInterestPayableTotals = 0, otherTotals = 0, rentTotals = 0;
    //salesTotalPnL = 0, cogsTotalPnL = 0,

    TextView title, netIncome, payables, receivables, inventory, netCashOperations,
            netCash, preSale, preOrder, ownerDrawing, depreciation, depreciation2,
            equipment, buildings, vehicles, rights, land, other, netCashInvesting,
            loanstxt, contractualSavings, netCashFinancing, netCashBusiness, totalNetCash,
            loanInterestPayable, rent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message = "Payable\n" +
//                        "Recievables";
//
////                View snackbarView = snackbar.getView();
////                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
////                textView.setMaxLines(5);  // show multiple line
//
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
        int startYear = Integer.parseInt(getIntent().getStringExtra("START_YEAR"));
        int endYear = Integer.parseInt(getIntent().getStringExtra("END_YEAR"));
        int startMonth = Integer.parseInt(getIntent().getStringExtra("START_MONTH")) - 1;
        int endMonth = Integer.parseInt(getIntent().getStringExtra("END_MONTH")) - 1;
        int startDay = Integer.parseInt(getIntent().getStringExtra("START_DAY"));
        int endDay  = Integer.parseInt(getIntent().getStringExtra("END_DAY")) + 1;


        Log.i("Start and end Dates: ", endDay + " " + endMonth + " " + endYear);

        System.out.println("Start and end Dates: " + startYear + " " + startMonth + " " + startDay);

        /**
         * end of date setting
         **/

        // declare textviews
        title = (TextView) findViewById(R.id.title);
        netIncome = (TextView) findViewById(R.id.net_income);
        payables = (TextView) findViewById(R.id.payablescf);
        receivables = (TextView) findViewById(R.id.receivablescf);
        inventory = (TextView) findViewById(R.id.inventory);
        preOrder = (TextView) findViewById(R.id.pre_orders);
        preSale = (TextView) findViewById(R.id.pre_sales);
        depreciation = (TextView) findViewById(R.id.depreciation_amortization);
        netCashOperations = (TextView) findViewById(R.id.net_cash_operations);
        netCash = (TextView) findViewById(R.id.net_cash);
        ownerDrawing = (TextView) findViewById(R.id.owner_drawings);
        depreciation2 = (TextView) findViewById(R.id.depreciation_amortization_2);
        equipment = (TextView) findViewById(R.id.equipment);
        //others = (TextView) findViewById(R.id.others);
        buildings = (TextView) findViewById(R.id.buildings);
        vehicles = (TextView) findViewById(R.id.vehicles);
        rights = (TextView) findViewById(R.id.rights);
        land = (TextView) findViewById(R.id.land);
        other = (TextView) findViewById(R.id.other);
        netCashInvesting = (TextView) findViewById(R.id.net_cash_investing);
        loanstxt = (TextView) findViewById(R.id.loans);
        contractualSavings = (TextView) findViewById(R.id.contractual_savings);
        netCashFinancing = (TextView) findViewById(R.id.net_cash_financing);
        netCashBusiness = (TextView) findViewById(R.id.net_cash_business);
        totalNetCash = (TextView) findViewById(R.id.net_cash);
        loanInterestPayable = (TextView) findViewById(R.id.loan_lnterest_payable);
        rent =  (TextView) findViewById(R.id.rent);

        title.setText("CASH FLOW STATEMENT\nFROM:" + startDay + "-" + (startMonth + 1 ) + "-" + startYear
                + "\tTO:" + (endDay - 1) + "-" + (endMonth + 1) + "-" + endYear);
        Calendar startDate;
        startDate = Calendar.getInstance();
//        startDate.set(startYear, startMonth, startDay);
        startDate.set(Calendar.DAY_OF_MONTH, startDay);
        startDate.set(Calendar.MONTH, startMonth);
        startDate.set(Calendar.YEAR, startYear);

//        setMonth(startDate, startMonth);

        Calendar endDate;
        endDate = Calendar.getInstance();
        //endDate.set(endYear, endMonth, endDay + 1);
        endDate.set(Calendar.DAY_OF_MONTH, endDay);
        endDate.set(Calendar.MONTH, endMonth);
        endDate.set(Calendar.YEAR, endYear);

//        setMonth(endDate, endMonth);

        Log.i(TAG, "Starting date;  " + startDate.get(Calendar.DAY_OF_MONTH)
                + " " + startDate.get(Calendar.MONTH)
                + " " + startDate.get(Calendar.YEAR));

        Log.i(TAG, "Ending date;  " + endDate.get(Calendar.DAY_OF_MONTH)
                + " " + endDate.get(Calendar.MONTH)
                + " " + endDate.get(Calendar.YEAR));

        /**
         * fetching data and getting totals
         **/
        List<Receivable> allReceivable = rdb.getAllReceivable();

        int receivableBefore = 0;
        int receivableAfter = 0;
        for (Receivable pd : allReceivable) {
            String date = pd.getOriginalDate();

            String dat[] = date.split("/");
            int day = Integer.parseInt(dat[0]);
            int month = Integer.parseInt(dat[1]) - 1;
            int year = Integer.parseInt(dat[2]);

            Calendar actualDate;
            actualDate = Calendar.getInstance();
            actualDate.set(Calendar.DAY_OF_MONTH, day);
            actualDate.set(Calendar.MONTH, month);
            actualDate.set(Calendar.YEAR, year);

            Log.i("Doing receivable date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if(((actualDate.before(startDate)))){
                receivableBefore = receivableBefore + pd.getTotal();
                Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

            }
            if(((actualDate.before(endDate)))){
                receivableAfter = receivableAfter + pd.getTotal();
                Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

            }
        }

        List<PaidReceivable> allPaidReceivable = prdb.getAllPaidReceivable();

        for (PaidReceivable pd : allPaidReceivable) {
            //if(!(pd.getPayment().matches("Dept. Default"))) {
                String date = pd.getOriginalDate();
                String paidDate = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing paid rec date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        //if(!pd.getPayment().matches("Dept. Default")) {
                            receivableBefore = receivableBefore + pd.getTotal();
                            Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());
                            Log.i(TAG, "looking for bad dept; dept " + pd.getPayment() + " amount " + pd.getTotal()
                                    + " gotten " + pd.getTotal());
                        //}
                    }
                    if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                        //if(!pd.getPayment().matches("Dept. Default")) {
                            receivableAfter = receivableAfter + pd.getTotal();
                            Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());
                            Log.i(TAG, "looking for bad dept; dept " + pd.getPayment() + " amount " + pd.getTotal()
                                    + " gotten " + pd.getTotal());
                        //}

                    }
                }
            //}
        }

//        List<PaidReceivable> paidReceivableList = prdb.getAllPaidReceivable();
//
//        for (PaidReceivable pd : paidReceivableList){
//            if(pd.getPayment().matches("Dept. Default")){
//                String date = pd.getDate();
//
//                String dat[] = date.split("/");
//                int day = Integer.parseInt(dat[0]);
//                int month = Integer.parseInt(dat[1]);
//                int year = Integer.parseInt(dat[2]);
//
//                Calendar actualDate;
//                actualDate = Calendar.getInstance();
//                actualDate.set(Calendar.DAY_OF_MONTH, day);
//                actualDate.set(Calendar.YEAR, year);
//
//                setMonth(actualDate, month);
//
//                Log.i("Doing paidR date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
//                        + " " + actualDate.get(Calendar.MONTH)
//                        + " " + actualDate.get(Calendar.YEAR));
//
//                if(((startDatePnL.compareTo(actualDate)) < 0) && ((actualDate.compareTo(endDatePnL) < 0))){
//                    salesTotalPnL = salesTotalPnL - pd.getTotal();
//                    Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " dept default " + pd.getTotal());
//
//                } else if((startDatePnL.compareTo(actualDate) == 0)){
//                    salesTotalPnL = salesTotalPnL - pd.getTotal();
//                    Log.i("date is same; ", " " + day  + " " + month +" " +year + " dept default " + pd.getTotal());
//
//                } else if((endDatePnL.compareTo(actualDate) == 0)){
//                    salesTotalPnL = salesTotalPnL - pd.getTotal();
//                    Log.i("date is same; ", " " + day  + " " + month +" " +year + " dept default " + pd.getTotal());
//
//                }
//            }
//        }

//        for (PaidReceivable pd : allPaidReceivable) {
//            if(pd.getPayment().matches("Dept. Default")) {
//                String date = pd.getOriginalDate();
//                String paidDate = pd.getDate();
//
//                String dat[] = date.split("/");
//                int day = Integer.parseInt(dat[0]);
//                int month = Integer.parseInt(dat[1]);
//                int year = Integer.parseInt(dat[2]);
//
//                String paidDat[] = paidDate.split("/");
//                int paidDay = Integer.parseInt(paidDat[0]);
//                int paidMonth = Integer.parseInt(paidDat[1]);
//                int paidYear = Integer.parseInt(paidDat[2]);
//
//                Calendar actualDate;
//                actualDate = Calendar.getInstance();
//                actualDate.set(Calendar.DAY_OF_MONTH, day);
//                actualDate.set(Calendar.YEAR, year);
//
//                setMonth(actualDate, month);
//
//                Calendar paidCDate;
//                paidCDate = Calendar.getInstance();
//                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
//                paidCDate.set(Calendar.YEAR, paidYear);
//
//                setMonth(paidCDate, paidMonth);
//
//                Log.i("Doing paid rec date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
//                        + " " + actualDate.get(Calendar.MONTH)
//                        + " " + actualDate.get(Calendar.YEAR));
//
//                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
//                        + " " + paidCDate.get(Calendar.MONTH)
//                        + " " + paidCDate.get(Calendar.YEAR));
//
//                if ((((actualDate.after(endDate))) && (actualDate.before(endDate))) && (paidCDate.after(endDate))) {
//                    receivableAfter = receivableAfter + pd.getTotal();
//                    Log.i(TAG, " " + day + "before payment default " + month + " " + year + " amount" + pd.getTotal());
//
//                }
//            }
//        }

        receivablesTotal = receivableAfter - receivableBefore;

        Log.i("recevable calculation; ", " " + receivableBefore + " - " + receivableAfter + " = " + receivablesTotal);

        List<Payable> allPayable = pdb.getAllPayable();

        int payableAfter = 0;
        int payableBefore = 0;
        for (Payable pd : allPayable) {
            String date = pd.getOriginalDate();

            String dat[] = date.split("/");
            int day = Integer.parseInt(dat[0]);
            int month = Integer.parseInt(dat[1]) - 1;
            int year = Integer.parseInt(dat[2]);

            Calendar actualDate;
            actualDate = Calendar.getInstance();
            actualDate.set(Calendar.DAY_OF_MONTH, day);
            actualDate.set(Calendar.MONTH, month);
            actualDate.set(Calendar.YEAR, year);

            Log.i("Doing payables date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if(((actualDate.before(startDate)))){
                payableBefore = payableBefore + pd.getTotal();
                Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

            }
            if((( actualDate.before(endDate)))){
                payableAfter = payableAfter + pd.getTotal();
                Log.i("date is btwn; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

            }
        }

        List<PaidPayable> allPaidPayable = ppdb.getAllPaidPayable();

        for (PaidPayable pd : allPaidPayable) {
            String date = pd.getOriginalDate();
            String paidDate = pd.getDate();

            String dat[] = date.split("/");
            int day = Integer.parseInt(dat[0]);
            int month = Integer.parseInt(dat[1]) - 1;
            int year = Integer.parseInt(dat[2]);

            String paidDat[] = paidDate.split("/");
            int paidDay = Integer.parseInt(paidDat[0]);
            int paidMonth = Integer.parseInt(paidDat[1]) - 1;
            int paidYear = Integer.parseInt(paidDat[2]);

            Calendar actualDate;
            actualDate = Calendar.getInstance();
            actualDate.set(Calendar.DAY_OF_MONTH, day);
            actualDate.set(Calendar.MONTH, month);
            actualDate.set(Calendar.YEAR, year);

            Calendar paidCDate;
            paidCDate = Calendar.getInstance();
            paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
            paidCDate.set(Calendar.MONTH, paidMonth);
            paidCDate.set(Calendar.YEAR, paidYear);


            Log.i("Doing paidpay date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                    + " " + paidCDate.get(Calendar.MONTH)
                    + " " + paidCDate.get(Calendar.YEAR));

            if (paidCDate.after(startDate)) {
                if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                    payableBefore = payableBefore + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
                if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                    payableAfter = payableAfter + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
            }
        }

        payablesTotals = payableAfter - payableBefore;

        Log.i("payable calculation; ", " " + payableBefore + " - " + payableAfter + " = " + payablesTotals);


        List<PreOrder> allPreOrder = podb.getAllPreOrder();

        int preOrderAfter = 0;
        int preOrderBefore = 0;
        for (PreOrder pd : allPreOrder) {
            if(pd.getStatus().matches("OPEN")) {
                String date = pd.getOriginalDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Log.i("Doing preorder date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(startDate)))) {
                    preOrderBefore = preOrderBefore + pd.getTotal();
                    Log.i("date is down; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
                if (((actualDate.before(endDate)))) {
                    preOrderAfter = preOrderAfter + pd.getTotal();
                    Log.i("date is up; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
            }
        }

//        List<PreOrder> allClosedPreOrder = cpodb.getAllPreOrder();

        for (PreOrder pd : allPreOrder) {
            if(pd.getStatus().matches("CLOSED")) {
                String date = pd.getOriginalDate();
                String paidDate = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing closedpo date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        preOrderBefore = preOrderBefore + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                    if (((actualDate.before(endDate))) && ( paidCDate.after(endDate))) {
                        preOrderAfter = preOrderAfter + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                }
            }
        }

        preOrderTotals = preOrderAfter - preOrderBefore;

        Log.i("preOrder calculation; ", " " + preOrderBefore + " - " + preOrderAfter + " = " + preOrderTotals);


        List<PreSale> allPreSale = psdb.getAllPreSale();

        int preSaleAfter = 0;
        int preSaleBefore = 0;
        for (PreSale pd : allPreSale) {
            if(pd.getStatus().matches("OPEN")) {
                String date = pd.getOriginalDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Log.i("Doing presale date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(startDate)))) {
                    preSaleBefore = preSaleBefore + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
                if (((actualDate.before(endDate)))) {
                    preSaleAfter = preSaleAfter + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
            }
        }

        for (PreSale pd : allPreSale) {
            if(pd.getStatus().matches("CLOSED")) {
                String date = pd.getOriginalDate();
                String paidDate = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing closedps date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        preSaleBefore = preSaleBefore + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                    if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                        preSaleAfter = preSaleAfter + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                }
            }
        }

        preSaleTotals = preSaleAfter - preSaleBefore;

        Log.i("preSale calculation; ", " " + preSaleBefore + " - " + preSaleAfter + " = " + preSaleTotals);

        List<Inventory> allInventory = idb.getAllInventory();

        int standingInventory = 0;
        for (Inventory pd : allInventory) {
            standingInventory = standingInventory + pd.getTotal();
        }

        List<GoodsSales> allGoodsSales = gsdb.getAllSales();

        double saleAfterStart = 0;
        double saleAfterEnd = 0;
        for (GoodsSales pd : allGoodsSales) {
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

            Log.i("Doing goods sale date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if(((actualDate.after(startDate)))){
                saleAfterStart = saleAfterStart + (pd.getNumber() * pd.getPCost());
                Log.i("inventoy at start add; ", " " + day  + " " + month +" " +year + " amount" + (pd.getNumber() * pd.getPCost()));

            }
            if((( actualDate.after(endDate)))){
                saleAfterEnd = saleAfterEnd + (pd.getNumber() * pd.getPCost());
                Log.i("inventoy at end add; ", " " + day  + " " + month +" " +year + " amount" + (pd.getNumber() * pd.getPCost()));
            }
        }

        List<GoodsPurchased> goodsPurchaseds = gpdb.getAllPurchase();

        int purchAfterStart = 0;
        int purchAfterEnd = 0;
        for(GoodsPurchased pd : goodsPurchaseds){
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

            Log.i("Doing goodspurch date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if(((actualDate.after(startDate)))){
                purchAfterStart = purchAfterStart + pd.getTotal();
                Log.i("inventoy at start add; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

            }
            if((( actualDate.after(endDate)))){
                purchAfterEnd = purchAfterEnd + pd.getTotal();
                Log.i("inventoy at end add; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());
            }
        }

        List<Delivery> deliveries = ddb.getAllDelivery();

        int deliveryAfterStart = 0;
        int deliveryAfterEnd = 0;
        for(Delivery pd : deliveries){
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

            Log.i("Doing goodsdelv date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if(((actualDate.after(startDate)))){
                deliveryAfterStart = deliveryAfterStart + pd.getTotal();
                Log.i("delivery at start add; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());

            }
            if((( actualDate.after(endDate)))){
                deliveryAfterEnd = deliveryAfterEnd + pd.getTotal();
                Log.i("delivery at end add; ", " " + day  + " " + month +" " +year + " amount" + pd.getTotal());
            }
        }

        List<PreOrder> allPreOrdered = podb.getAllPreOrder();

        double preOrderStart = 0;
        double preOrderEnd = 0;
        for (PreOrder pd : allPreOrdered) {
            if(pd.getStatus().matches("CLOSED")) {
                Log.i(TAG, " CLosed pre order ");
                String date = pd.getOriginalDate();
                String paidDate = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing closedpO date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate) && paidCDate.before(endDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        List<GoodsPreOrdered> goodsPreordered = gpodb.getByForeignKey(pd.getId());

                        for(GoodsPreOrdered pd1 : goodsPreordered) {
                            preOrderStart = preOrderStart + (pd1.getNumber() * pd1.getCost());
                            Log.i("preorder inven bef; ", " " + day + " " + month + " " + year + " amount" + (pd1.getNumber() * pd1.getCost()));
                        }
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                    if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                        List<GoodsPreOrdered> goodsPreordered = gpodb.getByForeignKey(pd.getId());

                        for(GoodsPreOrdered pd1 : goodsPreordered) {
                            preOrderEnd = preOrderEnd + (pd1.getNumber() * pd1.getCost());
                            Log.i("preor inve aft; ", " " + day + " " + month + " " + year + " amount" + (pd1.getNumber() * pd1.getCost()));
                        }
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                }
            }
        }

        List<PreSale> preSales = psdb.getAllPreSale();

        int preSaleStart = 0;
        int preSaleEnd = 0;
        for(PreSale pd : preSales){
            if(pd.getStatus().matches("CLOSED")) {
                Log.i(TAG, " CLosed pre sale ");
                String date = pd.getOriginalDate();
                String paidDate = pd.getDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing closedps date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate) && paidCDate.before(endDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        List<GoodsPreOrdered> goodsPreSold = gpsdb.getByForeignKey(pd.getId());

                        for(GoodsPreOrdered pd1 : goodsPreSold) {
                            Inventory inventory1 = idb.getInventoryByProduct(pd1.getProduct());

                            double goodCost = (int) Math.round(inventory1.getCost() * pd1.getNumber());

                            preSaleStart = preSaleStart + (int) goodCost;
                            Log.i("preSALE inven bef; ", " " + day + " " + month + " " + year
                                    + " amount" + pd1.getTotal() + " goodCost" + goodCost);
                        }
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                    if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                        List<GoodsPreOrdered> goodsPreSold = gpsdb.getByForeignKey(pd.getId());

                        for(GoodsPreOrdered pd1 : goodsPreSold) {
                            Inventory inventory1 = idb.getInventoryByProduct(pd1.getProduct());

                            double goodCost = (int) Math.round(inventory1.getCost() * pd1.getNumber());

                            preSaleEnd = preSaleEnd + (int) goodCost;
                            Log.i("preor inve aft; ", " " + day + " " + month + " " + year
                                    + " amount" + pd1.getTotal() + " goodCost" + goodCost);
                        }
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                }
            }
        }

        List<Owner> ownerList = odb.getAllOwner();

        for(Owner pd: ownerList){
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

            Log.i(TAG, "Doing Owner date; " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if (actualDate.after(startDate)) {
                if((pd.getType().matches("Draw from Stock"))) {
                    purchAfterStart = purchAfterStart - pd.getTotal();
                    Log.i(TAG, "Draw stock amount = " + pd.getTotal() + " on date " + pd.getDate());

                }
            }
            if (actualDate.after(endDate)) {
                if((pd.getType().matches("Draw from Stock"))) {
                    purchAfterEnd = purchAfterEnd - pd.getTotal();
                    Log.i(TAG, "Draw stock amount = " + pd.getTotal() + " on date " + pd.getDate());

                }
            }
        }

        List<AssetGains> assetGainsList = agdb.getAllAssetGains();

        for (AssetGains pd : assetGainsList) {
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

            Log.i(TAG, "Doing inventory loss date; " + actualDate.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate.get(Calendar.MONTH)
                    + " " + actualDate.get(Calendar.YEAR));

            if (actualDate.after(startDate)) {
                if((pd.getCustomer().matches("INV"))) {
                    purchAfterStart = purchAfterStart + pd.getTotal();
                    Log.i(TAG, "Loss stock amount = " + pd.getTotal() + " on date " + pd.getDate());

                }
            }
            if (actualDate.after(endDate)) {
                if((pd.getCustomer().matches("INV"))) {
                    purchAfterEnd = purchAfterEnd + pd.getTotal();
                    Log.i(TAG, "Loss stock amount = " + pd.getTotal() + " on date " + pd.getDate());

                }
            }
        }

        inventoryTotals = (standingInventory + purchAfterStart + deliveryAfterStart - saleAfterStart - preSaleStart + preOrderStart)
                -  (standingInventory + purchAfterEnd + deliveryAfterEnd - saleAfterEnd - preSaleEnd + preOrderEnd);

        for(Owner pd: ownerList){
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

            if (((actualDate.before(endDate)) && (actualDate.after(startDate)))) {
                if(!(pd.getType().matches("Add Funds"))) {
                    String date2 = pd.getDate();

                    String dat2[] = date2.split("/");
                    int actualMonth2 = Integer.parseInt(dat2[1]) - 1;
                    int actualYear2 = Integer.parseInt(dat2[2]);

                    String name = "" + actualMonth2 + "-" + actualYear2;
                    boolean flag = false;

                    for (String key : ownerDraw.keySet()) {
                        if (key.matches(name)) {
                            flag = true;
                        }
                    }

                    if (flag) {
                        int numb = pd.getTotal() + Integer.parseInt(ownerDraw.get("" + name));

                        ownerDraw.remove("" + name);
                        ownerDraw.put("" + name, "" + numb);
                    } else if (!flag) {
                        ownerDraw.put("" + name, "" + pd.getTotal());
                    }
                }
                if((pd.getType().matches("Add Funds"))) {

                }
            }
        }

        PersonalData personalData = pddb.getPerson(1);

        int mdSalary = personalData.getMDSalary();

        for(String key : ownerDraw.keySet()){
            Log.d("Cash Flow", "date " + key + " total " + ownerDraw.get(key));

            int numb = Integer.parseInt(ownerDraw.get(key)) - mdSalary;

            if(numb > 0){
                ownerDrawingTotal = ownerDrawingTotal - numb;
            }
            Log.d("Cash Flow", "numb " + numb + " tot " + ownerDrawingTotal);
        }

        List<Assets> assetsList = adb.getAllAssets();

        double assetsBefore = 0;
        double assetsAfter = 0;
        double vehicleBefore = 0;
        double vehicleAfter = 0;
        double equipmentBefore = 0;
        double equipmentAfter = 0;
        double rightsBefore = 0;
        double rightsAfter = 0;
        double othersBefore = 0;
        double othersAfter = 0;
        for (Assets pd : assetsList) {
            if (pd.getType().matches("Vehicle")
                    || pd.getType().matches("Equipment")
                    || pd.getType().matches("Licenses")
                    || pd.getType().matches("Other")) {
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

                Log.i("Doing Casset date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(startDate)))) {
                    if (pd.getType().matches("Vehicle")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = ((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days);

                        if((pd.getTotal() - pass) <= pd.getScrsp()) {
                            vehicleBefore = vehicleBefore + pd.getScrsp();
                            Log.i("calcing veh; ", " total " + pd.getTotal()
                                    + " scrap " +  pd.getScrsp()
                                    + " vehbef " + vehicleBefore);
                        } else {
                            assetsBefore = assetsBefore + pass;
                            vehicleBefore = vehicleBefore + pd.getTotal() - pass;
                            Log.i("calcing veh; ", " total " + pd.getTotal()
                                    + " minus " + pass
                                    + " vehbef " + vehicleBefore);
                        }
                    } else if (pd.getType().matches("Equipment")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = ((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days);

                        if((pd.getTotal() - pass) <= pd.getScrsp()) {
                            equipmentBefore = equipmentBefore + pd.getScrsp();
                            Log.i("calcing equ; ", " total " + pd.getTotal()
                                    + " scrap " +  pd.getScrsp()
                                    + " equbef " + equipmentBefore);
                        } else {
                            assetsBefore = assetsBefore + pass;
                            equipmentBefore = equipmentBefore + pd.getTotal() - pass;
                            Log.i("calcing equ; ", " total " + pd.getTotal()
                                    + " minus " + pass
                                    + " equbef " + equipmentBefore);
                        }
                    } else if (pd.getType().matches("Other")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = ((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days);

                        if((pd.getTotal() - pass) <= pd.getScrsp()) {
                            othersBefore = othersBefore + pd.getScrsp();
                            Log.i("calcing othersBefore; ", " total " + pd.getTotal()
                                    + " scrap " +  pd.getScrsp()
                                    + " equbef " + othersBefore);
                        } else {
                            assetsBefore = assetsBefore + pass;
                            othersBefore = othersBefore + pd.getTotal() - pass;
                            Log.i("calcing othersBefore; ", " total " + pd.getTotal()
                                    + " minus " + pass
                                    + " equbef " + othersBefore);
                        }
                    } else if (pd.getType().matches("Licenses")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                        Log.i("calcing; ", " total " + pd.getTotal()
                                + " minus " + ((double) pd.getTotal() / (daysTotal)) * days
                                + " passn " +  ((double) pd.getTotal() - (((double) pd.getTotal()
                                / (daysTotal)) * days))
                                + " days " + days);

                        if ((pd.getTotal() - pass) <= 0){

                        } else {
                            assetsBefore = assetsBefore + pass;
                            rightsBefore = rightsBefore + pd.getTotal() - pass;
                        }
                    }

                    Log.i("date is btwn; ", " " + actualDay + " " + actualMonth + " " + actualYear + " amount" + pd.getTotal());
                }
                if (((actualDate.before(endDate)))) {
                    if (pd.getType().matches("Vehicle")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days));

                        if((pd.getTotal() - pass) <= pd.getScrsp()) {
                            vehicleAfter = vehicleAfter + pd.getScrsp();
                            Log.i("calcing veh; ", " total " + pd.getTotal()
                                    + " scrap " +  pd.getScrsp()
                                    + " vehaft " + vehicleAfter);
                        } else {
                            assetsAfter = assetsAfter + pass;
                            vehicleAfter = vehicleAfter + pd.getTotal() - pass;
                            Log.i("calcing veh; ", " total " + pd.getTotal()
                                    + " minus " + pass
                                    + " vehaft " + vehicleAfter);
                        }
                    } else if (pd.getType().matches("Equipment")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days));

                        if((pd.getTotal() - pass) <= pd.getScrsp()) {
                            equipmentAfter = equipmentAfter + pd.getScrsp();
                            Log.i("calcing equ; ", " total " + pd.getTotal()
                                    + " scrap " +  pd.getScrsp()
                                    + " equaft " + equipmentAfter);
                        } else {
                            assetsAfter = assetsAfter + pass;
                            equipmentAfter = equipmentAfter + pd.getTotal() - pass;
                            Log.i("calcing equ; ", " total " + pd.getTotal()
                                    + " minus " + pass
                                    + " equaft " + equipmentAfter);
                        }
                    } else if (pd.getType().matches("Other")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                / (daysTotal)) * (double) days));

                        if((pd.getTotal() - pass) <= pd.getScrsp()) {
                            othersAfter = othersAfter + pd.getScrsp();
                            Log.i("calcing othersAfter; ", " total " + pd.getTotal()
                                    + " scrap " +  pd.getScrsp()
                                    + " equaft " + othersAfter);
                        } else {
                            assetsAfter = assetsAfter + pass;
                            othersAfter = othersAfter + pd.getTotal() - pass;
                            Log.i("calcing othersAfter; ", " total " + pd.getTotal()
                                    + " minus " + pass
                                    + " equaft " + othersAfter);
                        }
                    } else if (pd.getType().matches("Licenses")){
                        int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                        double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                        Log.i("calcing; ", " total " + pd.getTotal()
                                + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
                                + " passn " +  ((double) pd.getTotal() - (((double) pd.getTotal()
                                / (daysTotal)) * (double) days))
                                + " days " + days);

                        if ((pd.getTotal() - pass) <= 0){

                        } else {
                            assetsAfter = assetsAfter + pass;
                            rightsAfter = rightsAfter + pd.getTotal() - pass;
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

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate)))) {
                        if (pd.getType().matches("Vehicle")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = ((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days);

                            if((pd.getTotal() - pass) <= pd.getScrsp()) {
                                vehicleBefore = vehicleBefore + pd.getScrsp();
                                Log.i("calcing veh; ", " total " + pd.getTotal()
                                        + " scrap " +  pd.getScrsp()
                                        + " vehbef " + vehicleBefore);
                            } else {
                                assetsBefore = assetsBefore + pass;
                                vehicleBefore = vehicleBefore + pd.getTotal() - pass;
                                Log.i("calcing veh; ", " total " + pd.getTotal()
                                        + " minus " + pass
                                        + " vehbef " + vehicleBefore);
                            }
                        } else if (pd.getType().matches("Equipment")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = ((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days);

                            if((pd.getTotal() - pass) <= pd.getScrsp()) {
                                equipmentBefore = equipmentBefore + pd.getScrsp();
                                Log.i("calcing equ; ", " total " + pd.getTotal()
                                        + " scrap " +  pd.getScrsp()
                                        + " equbef " + equipmentBefore);
                            } else {
                                assetsBefore = assetsBefore + pass;
                                equipmentBefore = equipmentBefore + pd.getTotal() - pass;
                                Log.i("calcing equ; ", " total " + pd.getTotal()
                                        + " minus " + pass
                                        + " equbef " + equipmentBefore);
                            }
                        } else if (pd.getType().matches("Other")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = ((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days);

                            if((pd.getTotal() - pass) <= pd.getScrsp()) {
                                othersBefore = othersBefore + pd.getScrsp();
                                Log.i("calcing othersBefore; ", " total " + pd.getTotal()
                                        + " scrap " +  pd.getScrsp()
                                        + " equbef " + othersBefore);
                            } else {
                                assetsBefore = assetsBefore + pass;
                                othersBefore = othersBefore + pd.getTotal() - pass;
                                Log.i("calcing othersBefore; ", " total " + pd.getTotal()
                                        + " minus " + pass
                                        + " equbef " + othersBefore);
                            }
                        } else if (pd.getType().matches("Licenses")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                            Log.i("calcing; ", " total " + pd.getTotal()
                                    + " minus " + ((double) pd.getTotal() / (daysTotal)) * days
                                    + " passn " +  ((double) pd.getTotal() - (((double) pd.getTotal()
                                    / (daysTotal)) * days))
                                    + " days " + days);

                            if ((pd.getTotal() - pass) <= 0){

                            } else {
                                assetsBefore = assetsBefore + pass;
                                rightsBefore = rightsBefore + pd.getTotal() - pass;
                            }
                        }

                        Log.i("date is btwn; ", " " + actualDay + " " + actualMonth + " " + actualYear + " amount" + pd.getTotal());
                    }
                    if (((actualDate.before(endDate)))) {
                        if (pd.getType().matches("Vehicle")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if((pd.getTotal() - pass) <= pd.getScrsp()) {
                                vehicleAfter = vehicleAfter + pd.getScrsp();
                                Log.i("calcing veh; ", " total " + pd.getTotal()
                                        + " scrap " +  pd.getScrsp()
                                        + " vehaft " + vehicleAfter);
                            } else {
                                assetsAfter = assetsAfter + pass;
                                vehicleAfter = vehicleAfter + pd.getTotal() - pass;
                                Log.i("calcing veh; ", " total " + pd.getTotal()
                                        + " minus " + pass
                                        + " vehaft " + vehicleAfter);
                            }
                        } else if (pd.getType().matches("Equipment")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if((pd.getTotal() - pass) <= pd.getScrsp()) {
                                equipmentAfter = equipmentAfter + pd.getScrsp();
                                Log.i("calcing equ; ", " total " + pd.getTotal()
                                        + " scrap " +  pd.getScrsp()
                                        + " equaft " + equipmentAfter);
                            } else {
                                assetsAfter = assetsAfter + pass;
                                equipmentAfter = equipmentAfter + pd.getTotal() - pass;
                                Log.i("calcing equ; ", " total " + pd.getTotal()
                                        + " minus " + pass
                                        + " equaft " + equipmentAfter);
                            }
                        } else if (pd.getType().matches("Other")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = (((((double) pd.getTotal() - (double) pd.getScrsp())
                                    / (daysTotal)) * (double) days));

                            if((pd.getTotal() - pass) <= pd.getScrsp()) {
                                othersAfter = othersAfter + pd.getScrsp();
                                Log.i("calcing othersAfter; ", " total " + pd.getTotal()
                                        + " scrap " +  pd.getScrsp()
                                        + " equaft " + othersAfter);
                            } else {
                                assetsAfter = assetsAfter + pass;
                                othersAfter = othersAfter + pd.getTotal() - pass;
                                Log.i("calcing othersAfter; ", " total " + pd.getTotal()
                                        + " minus " + pass
                                        + " equaft " + othersAfter);
                            }
                        } else if (pd.getType().matches("Licenses")){
                            int days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                            double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear) + 1;

                            double pass = ((((double) pd.getTotal() / (daysTotal)) * (double) days));

                            Log.i("calcing; ", " total " + pd.getTotal()
                                    + " minus " + ((double) pd.getTotal() / (daysTotal)) * (double) days
                                    + " passn " +  ((double) pd.getTotal() - (((double) pd.getTotal()
                                    / (daysTotal)) * (double) days))
                                    + " days " + days);

                            if ((pd.getTotal() - pass) <= 0){

                            } else {
                                assetsAfter = assetsAfter + pass;
                                rightsAfter = rightsAfter + pd.getTotal() - pass;
                            }
                        }

                        Log.i("date is btwn; ", " " + actualDay + " " + actualMonth + " " + actualYear + " amount" + pd.getTotal());
                    }
                }
            }
        }

        depreciationTotal = Math.round(assetsAfter - assetsBefore);

        vehiclesTotals = Math.round(vehicleAfter - vehicleBefore);

        Log.i("land calculation; ", " " + vehicleAfter + " - " + vehicleBefore + " = " + vehiclesTotals);

        equipmentTotal = Math.round(equipmentAfter - equipmentBefore);

        Log.i("equipment calculation; ", " " + equipmentAfter + " - " + equipmentBefore + " = " + equipmentTotal);

        othersTotal = Math.round(othersAfter - othersBefore);

        Log.i("othersTotal calc; ", " " + othersAfter + " - " + othersBefore + " = " + othersTotal);

        rightsTotals = Math.round(rightsAfter - rightsBefore);

        Log.i("Licenses calculation; ", " " + rightsAfter + " - " + rightsBefore + " = " + rightsTotals);

        int landAfter = 0;
        int landBefore = 0;
        for (Assets pd : assetsList) {
            if (pd.getType().matches("Land")){
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

                Log.i("Doing land date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(startDate)))) {
                    landBefore = landBefore + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
                if (((actualDate.before(endDate)))) {
                    landAfter = landAfter + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
            }
        }

        for (SoldAssets pd : soldAssetsList) {
            if (pd.getType().matches("Land")) {
                String date = pd.getDate();
                String paidDate = pd.getSaleDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing soldland date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        landBefore = landBefore + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                    if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                        landAfter = landAfter + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                }
            }
        }

        landTotals = landAfter - landBefore;

        Log.i("land calculation; ", " " + landBefore + " - " + landAfter + " = " + landTotals);

        int buildingAfter = 0;
        int buildingBefore = 0;
        for (Assets pd : assetsList) {
            if (pd.getType().matches("Building")) {
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

                Log.i("Doing building date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(startDate)))) {
                    buildingBefore = buildingBefore + pd.getTotal();
                    Log.i("date is befor; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    buildingBefore = minusUpgrade(startDate, buildingBefore, pd.getID());
                }
                if (((actualDate.before(endDate)))) {
                    buildingAfter = buildingAfter + pd.getTotal();
                    Log.i("date is inside; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());
                    buildingAfter = minusUpgrade(endDate, buildingAfter, pd.getID());
                }
            }
        }

        for (SoldAssets pd : soldAssetsList) {
            if (pd.getType().matches("Building")) {
                String date = pd.getDate();
                String paidDate = pd.getSaleDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                paidCDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing soldbuild date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        buildingBefore = buildingBefore + pd.getTotal();
                        Log.i("date is before; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());
                        buildingBefore = minusUpgrade(startDate, buildingBefore, pd.getID());
                    }
                    if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                        buildingAfter = buildingAfter + pd.getTotal();
                        Log.i("date is inside; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());
                        buildingAfter = minusUpgrade(endDate, buildingAfter, pd.getID());
                    }
                }
            }
        }

        buildingsTotals = buildingAfter - buildingBefore;

        Log.i("building calculation; ", " " + buildingBefore + " - " + buildingAfter + " = " + buildingsTotals);

        int otherAfter = 0;
        int otherBefore = 0;
        for (Assets pd : assetsList) {
            if (pd.getType().matches("Other")){
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

                Log.i("Doing other date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                if (((actualDate.before(startDate)))) {
                    otherBefore = otherBefore + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
                if (((actualDate.before(endDate)))) {
                    otherAfter = otherAfter + pd.getTotal();
                    Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                }
            }
        }

        for (SoldAssets pd : soldAssetsList) {
            if (pd.getType().matches("Other")) {
                String date = pd.getDate();
                String paidDate = pd.getSaleDate();

                String dat[] = date.split("/");
                int day = Integer.parseInt(dat[0]);
                int month = Integer.parseInt(dat[1]) - 1;
                int year = Integer.parseInt(dat[2]);

                String paidDat[] = paidDate.split("/");
                int paidDay = Integer.parseInt(paidDat[0]);
                int paidMonth = Integer.parseInt(paidDat[1]) - 1;
                int paidYear = Integer.parseInt(paidDat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, day);
                actualDate.set(Calendar.MONTH, month);
                actualDate.set(Calendar.YEAR, year);

                Calendar paidCDate;
                paidCDate = Calendar.getInstance();
                paidCDate.set(Calendar.DAY_OF_MONTH, paidDay);
                actualDate.set(Calendar.MONTH, paidMonth);
                paidCDate.set(Calendar.YEAR, paidYear);

                Log.i("Doing soldother date; ", " " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + " " + actualDate.get(Calendar.MONTH)
                        + " " + actualDate.get(Calendar.YEAR));

                Log.i("and paid date; ", " " + paidCDate.get(Calendar.DAY_OF_MONTH)
                        + " " + paidCDate.get(Calendar.MONTH)
                        + " " + paidCDate.get(Calendar.YEAR));

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate))) && (paidCDate.after(startDate))) {
                        otherBefore = otherBefore + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " amount" + pd.getTotal());

                    }
                    if (((actualDate.before(endDate))) && (paidCDate.after(endDate))) {
                        otherAfter = otherAfter + pd.getTotal();
                        Log.i("date is btwn; ", " " + day + " " + month + " " + year + " yamount" + pd.getTotal());

                    }
                }
            }
        }

        otherTotals = otherAfter - otherBefore;

        Log.i("other calculation; ", " " + otherBefore + " - " + otherAfter + " = " + otherTotals);


        List<Loans> loansList = ldb.getAllLoans();

        double interestBefore = 0;
        double interestAfter = 0;
        double loanBefore = 0;
        double loanAfter = 0;
        double saveBefore = 0;
        double saveAfter = 0;
        for(Loans pd: loansList){
            if(pd.getStatus().toString().matches("NOT")){
                Log.d(TAG, "Loop loansList NOT");
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

                Calendar stopDate;
                stopDate = Calendar.getInstance();
                stopDate.set(Calendar.DAY_OF_MONTH, endDa);
                stopDate.set(Calendar.MONTH, endM);
                stopDate.set(Calendar.YEAR, endY);

                if (((actualDate.before(startDate)))) {
                    loanBefore = loanBefore + pd.getTotal();

                    double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                    Log.i("geto day tot st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                            + endDa + " " + endM + " " + endY);

                    if(totalDays == 0){
                        totalDays = 1;
                    }
                    Log.d(TAG, "Loan Tot Days bef SD " + totalDays);

                    double days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                    Log.d(TAG, "Loan days " + days);

                    Log.i("get day st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                            + startDay + " " + startMonth + " " + startYear);

                    double daysToNow = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                    Log.d(TAG, "Loan daysToNow " + daysToNow);

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
                        Log.d(TAG, "Checking Instalment amount " + pd1.getTotal());
                        Log.d(TAG, "Instalment date " + pd1.getDate());
                        String strdat2[] = pd1.getDate().split("/");
                        int actualDay2 = Integer.parseInt(strdat2[0]);
                        int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                        int actualYear2 = Integer.parseInt(strdat2[2]);

                        Calendar actualDate2;
                        actualDate2 = Calendar.getInstance();
                        actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                        actualDate2.set(Calendar.MONTH, actualMonth2);
                        actualDate2.set(Calendar.YEAR, actualYear2);

                        if (actualDate2.before(startDate)) {
                            double remove = (double)pd1.getTotal() * (loanCost / (((double)pd.getInstalments())
                                    * (double)pd.getInstalmentsNo()));

                            interestBefore = interestBefore - remove - loanInterestPerDay;

                            Log.d(TAG, "Removing from Loan Interest before " + interestBefore);
                            Log.d(TAG, "remove " + remove);

                            double remove2 = (double)pd1.getTotal() * (pd.getTotal() / (((double)pd.getInstalments())
                                    * (double)pd.getInstalmentsNo()));

                            loanBefore = loanBefore - remove2;

                            saveBefore = saveBefore - pd1.getContractualSavings();
                        }
                    }
                }
                if ((actualDate.before(endDate))) {
                    loanAfter = loanAfter + pd.getTotal();

                    double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                    Log.i("geto day tot st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                            + endDa + " " + endM + " " + endY);

                    if(totalDays == 0){
                        totalDays = 1;
                    }
                    Log.d(TAG, "Loan Tot Days bef SD " + totalDays);

                    double days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                    Log.d(TAG, "Loan days " + days);

                    Log.i("get day st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                            + startDay + " " + startMonth + " " + startYear);

                    double daysToNow = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                    Log.d(TAG, "Loan daysToNow " + daysToNow);

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
                        Log.d(TAG, "Instalment fits end date " + pd1.getDate());
                        String strdat2[] = pd1.getDate().split("/");
                        int actualDay2 = Integer.parseInt(strdat2[0]);
                        int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                        int actualYear2 = Integer.parseInt(strdat2[2]);

                        Calendar actualDate2;
                        actualDate2 = Calendar.getInstance();
                        actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                        actualDate2.set(Calendar.MONTH, actualMonth2);
                        actualDate2.set(Calendar.YEAR, actualYear2);

                        if (actualDate2.before(endDate)) {
                            double remove = (double)pd1.getTotal() * (loanCost / (((double)pd.getInstalments())
                                    * (double)pd.getInstalmentsNo()));

                            interestAfter = interestAfter - remove - loanInterestPerDay;

                            Log.d(TAG, "Removing from Loan Interest before " + interestBefore);
                            Log.d(TAG, "remove " + remove);

                            double remove2 = (double)pd1.getTotal() * (pd.getTotal() / (((double)pd.getInstalments())
                                    * (double)pd.getInstalmentsNo()));

                            loanAfter = loanAfter - remove2;

                            saveAfter = saveAfter - pd1.getContractualSavings();
                        }
                    }
                }
            }
        }

        boolean last = false;
        double paidinterestBefore = 0;
        double paidinterestAfter = 0;
        for(Loans pd: loansList){
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

                Calendar stopDate;
                stopDate = Calendar.getInstance();
                stopDate.set(Calendar.DAY_OF_MONTH, endDa);
                stopDate.set(Calendar.MONTH, endM);
                stopDate.set(Calendar.YEAR, endY);

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

                if (paidCDate.after(startDate)) {
                    if (((actualDate.before(startDate)))) {
                        loanBefore = loanBefore + pd.getTotal();

                        double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                        Log.i("geto day tot st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                                + endDa + " " + endM + " " + endY);

                        if(totalDays == 0){
                            totalDays = 1;
                        }
                        Log.d(TAG, "Loan Tot Days bef SD " + totalDays);

                        double days = dayNumb(actualDay, actualMonth, actualYear, startDay, startMonth, startYear) + 1;
                        Log.d(TAG, "Loan days " + days);

                        Log.i("get day st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                                + startDay + " " + startMonth + " " + startYear);

                        double daysToNow = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        Log.d(TAG, "Loan daysToNow " + daysToNow);

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
                            Log.d(TAG, "Checking Instalment amount " + pd1.getTotal());
                            Log.d(TAG, "Instalment date " + pd1.getDate());
                            String strdat2[] = pd1.getDate().split("/");
                            int actualDay2 = Integer.parseInt(strdat2[0]);
                            int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                            int actualYear2 = Integer.parseInt(strdat2[2]);

                            Calendar actualDate2;
                            actualDate2 = Calendar.getInstance();
                            actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                            actualDate2.set(Calendar.MONTH, actualMonth2);
                            actualDate2.set(Calendar.YEAR, actualYear2);

                            if (actualDate2.before(startDate)) {
                                double remove = (double)pd1.getTotal() * (loanCost / (((double)pd.getInstalments())
                                        * (double)pd.getInstalmentsNo()));

                                interestBefore = interestBefore - remove - loanInterestPerDay;

                                Log.d(TAG, "Removing from Loan Interest before " + interestBefore);
                                Log.d(TAG, "remove " + remove);

                                double remove2 = (double)pd1.getTotal() * (pd.getTotal() / (((double)pd.getInstalments())
                                        * (double)pd.getInstalmentsNo()));

                                loanBefore = loanBefore - remove2;

                                saveBefore = saveBefore - pd1.getContractualSavings();
                            }
                        }
                    }
                    if ((actualDate.before(endDate))) {
                        loanAfter = loanAfter + pd.getTotal();

                        double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                        Log.i("geto day tot st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                                + endDa + " " + endM + " " + endY);

                        if(totalDays == 0){
                            totalDays = 1;
                        }
                        Log.d(TAG, "Loan Tot Days bef SD " + totalDays);

                        double days = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        Log.d(TAG, "Loan days " + days);

                        Log.i("get day st; ", " " + actualDay + " " + actualMonth + " " + actualYear + " startday"
                                + startDay + " " + startMonth + " " + startYear);

                        double daysToNow = dayNumb(actualDay, actualMonth, actualYear, endDay, endMonth, endYear) + 1;
                        Log.d(TAG, "Loan daysToNow " + daysToNow);

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
                            Log.d(TAG, "Instalment fits end date " + pd1.getDate());
                            String strdat2[] = pd1.getDate().split("/");
                            int actualDay2 = Integer.parseInt(strdat2[0]);
                            int actualMonth2 = Integer.parseInt(strdat2[1]) - 1;
                            int actualYear2 = Integer.parseInt(strdat2[2]);

                            Calendar actualDate2;
                            actualDate2 = Calendar.getInstance();
                            actualDate2.set(Calendar.DAY_OF_MONTH, actualDay2);
                            actualDate2.set(Calendar.MONTH, actualMonth2);
                            actualDate2.set(Calendar.YEAR, actualYear2);

                            if (actualDate2.before(endDate)) {
                                double remove = (double)pd1.getTotal() * (loanCost / (((double)pd.getInstalments())
                                        * (double)pd.getInstalmentsNo()));

                                interestAfter = interestAfter - remove - loanInterestPerDay;

                                Log.d(TAG, "Removing from Loan Interest before " + interestBefore);
                                Log.d(TAG, "remove " + remove);

                                double remove2 = (double)pd1.getTotal() * (pd.getTotal() / (((double)pd.getInstalments())
                                        * (double)pd.getInstalmentsNo()));

                                loanAfter = loanAfter - remove2;

                                saveAfter = saveAfter - pd1.getContractualSavings();
                            }
                        }
                    }
                }
            }

            if(!last){
                interestAfter = interestAfter + paidinterestAfter;
                interestBefore = interestBefore + paidinterestBefore;
            }
        }

        for(Loans pd: loansList) {
            if (pd.getStatus().toString().matches("PAY")) {
                Log.d(TAG, "Looking for Paid Savings");
                String startD = pd.getContractualSavingsDate();

                String strdat[] = startD.split("/");
                int actualDay = Integer.parseInt(strdat[0]);
                int actualMonth = Integer.parseInt(strdat[1]) - 1;
                int actualYear = Integer.parseInt(strdat[2]);

                Calendar actualDate;
                actualDate = Calendar.getInstance();
                actualDate.set(Calendar.DAY_OF_MONTH, actualDay);
                actualDate.set(Calendar.MONTH, actualMonth);
                actualDate.set(Calendar.YEAR, actualYear);

                int savingTot = 0;

                List<Instalment> instalments = ildb.getByForeignKey(pd.getID());

                for(Instalment pd1: instalments) {
                    savingTot = savingTot + pd1.getContractualSavings();

                }

                if (((actualDate.before(startDate)))) {
                    saveBefore = saveBefore + savingTot;

                }
                if ((actualDate.before(endDate))) {
                    saveAfter = saveAfter + savingTot;

                }
            }
        }

        loanInterestPayableTotals = Math.round(interestAfter - interestBefore);
        Log.i(TAG, "interestAfter " + interestAfter + " - interestBefore " + interestBefore
                + " = loanInterestPayableTotals " + loanInterestPayableTotals);
        Log.i("calc int tot; ", " " + interestAfter + " - " + interestBefore + " = " + loanInterestPayableTotals);

        loansTotals = Math.round(loanAfter - loanBefore);
        contractualSavingsTotals = Math.round(saveAfter - saveBefore);

        List<Expenses> expensesList = edb.getAllExpenses();

        for( Expenses pd : expensesList){
            if (pd.getType().matches("Rent")){

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

                if((startDate.before(startRDate)) && endRDate.before(endDate)){
                    Log.d(TAG, "allExpenses Rent inbetween " + pd.getTotal());
                    rentTotals = rentTotals - (int) Math.round(pd.getTotal());

                } else if((startRDate.before(startDate)) && endDate.before(endRDate)){
                    int passedDays = dayNumb(startDay, startMonth, startYear, endDay, endMonth, endYear) + 1;

                    double perDay = (double) pd.getTotal() / (double) totalDays;
                    double remove = perDay * (double) passedDays;

                    rentTotals = rentTotals - (int) Math.round(remove);
                    Log.d(TAG, "allExpenses Rent sarrounded passedDays " + passedDays + " perDay " + perDay + " remove " + remove);

                } else if ((startDate.before(startRDate))){
                    int passedDays = dayNumb(startRDay, startRMonth, startRYear, endDay, endMonth, endYear) + 1;

                    double perDay = (double) pd.getTotal() / (double) totalDays;
                    double remove = perDay * (double) passedDays;

                    rentTotals = rentTotals - (int) Math.round(remove);
                    Log.d(TAG, "allExpenses Rent startbefor passedDays " + passedDays + " perDay " + perDay + " remove " + remove);

                } else if ((endRDate.before(endDate))){
                    int passedDays = dayNumb(startDay, startMonth, startYear, endDa, endM, endY) + 1;

                    double perDay = (double) pd.getTotal() / (double) totalDays;
                    double remove = perDay * (double) passedDays;

                    rentTotals = rentTotals - (int) Math.round(remove);
                    Log.d(TAG, "allExpenses Rent end befor passedDays " + passedDays + " perDay " + perDay + " remove " + remove);

                }
            }
        }

        /**
         * end of fetching and totals
         **/

        double netIN = getnet();

        netCashOperationsTotal = Math.round((netIN) + (payablesTotals) - (receivablesTotal) - (inventoryTotals)
                - (preOrderTotals) + (preSaleTotals) + (depreciationTotal) + (loanInterestPayableTotals) - rentTotals);

        netCashInvestingTotal = Math.round(0 - (equipmentTotal) - (othersTotal) - (buildingsTotals) - (vehiclesTotals)
                - (rightsTotals) - (landTotals) - (depreciationTotal) - (otherTotals));

        netCashFinancingTotal = Math.round(loansTotals + contractualSavingsTotals);

        netCashBusinessTotal = netCashOperationsTotal + netCashInvestingTotal + netCashFinancingTotal;

        totalNetCashTotals = netCashBusinessTotal + ownerDrawingTotal;

        int invenotyInt = (int) Math.round(inventoryTotals);

        //setting totals in view
        //netIncome.setText("" + numberFormat.format(((salesTotalPnL) - cogsTotalPnL)));
        netIncome.setText("" + numberFormat.format((netIN)));
        payables.setText("" + numberFormat.format(payablesTotals));
        receivables.setText("" + numberFormat.format(receivablesTotal));
        inventory.setText("" + numberFormat.format(invenotyInt));
        netCashOperations.setText("" + numberFormat.format(netCashOperationsTotal));
        preOrder.setText("" + numberFormat.format(preOrderTotals));
        preSale.setText("" + numberFormat.format(preSaleTotals));
        depreciation.setText("" + (numberFormat.format(depreciationTotal)));
        loanInterestPayable.setText("" + (numberFormat.format(loanInterestPayableTotals)));
        //netCash.setText("" + numberFormat.format(netCashTotal));
        ownerDrawing.setText("" + numberFormat.format(ownerDrawingTotal));
        depreciation2.setText("" + numberFormat.format(depreciationTotal));
        equipment.setText("" + numberFormat.format(equipmentTotal));
        //others.setText("" + numberFormat.format(equipmentTotal));
        buildings.setText("" + numberFormat.format(buildingsTotals));
        vehicles.setText("" + numberFormat.format(vehiclesTotals));
        rights.setText("" + numberFormat.format(rightsTotals));
        land.setText("" + numberFormat.format(landTotals));
        other.setText("" + numberFormat.format(othersTotal));
        netCashInvesting.setText("" + numberFormat.format(netCashInvestingTotal));
        loanstxt.setText("" + numberFormat.format(loansTotals));
        contractualSavings.setText("" + numberFormat.format(contractualSavingsTotals));
        netCashFinancing.setText("" + numberFormat.format(netCashFinancingTotal));
        netCashBusiness.setText("" + numberFormat.format(netCashBusinessTotal));
        totalNetCash.setText("" + numberFormat.format(totalNetCashTotals));
        rent.setText("" + numberFormat.format(rentTotals));
    }

    /*
    getting upgarde
     */

    public int minusUpgrade(Calendar endDate, int building, int id){
        List<ReUp> reUpList = rudb.getByForeignKey(id);

        Log.i("gotten; ", " amount " + building);

        for (ReUp pd1 : reUpList) {
            String date1 = pd1.getDate();

            String dat1[] = date1.split("/");

            int day1 = Integer.parseInt(dat1[0]);
            int month1 = Integer.parseInt(dat1[1]) - 1;
            int year1 = Integer.parseInt(dat1[2]);

            Calendar actualDate1;
            actualDate1 = Calendar.getInstance();
            actualDate1.set(Calendar.DAY_OF_MONTH, day1);
            actualDate1.set(Calendar.MONTH, month1);
            actualDate1.set(Calendar.YEAR, year1);

            Log.i("Doing reup date; ", " " + actualDate1.get(Calendar.DAY_OF_MONTH)
                    + " " + actualDate1.get(Calendar.MONTH)
                    + " " + actualDate1.get(Calendar.YEAR));

            if (((actualDate1.after(endDate)))) {
                building = building - pd1.getTotal();
                Log.i("remove upgrade; ", " " + day1 + " " + month1 + " " + year1 + " amount" + pd1.getTotal());
            }
        }

        Log.i("retrn; ", " amount " + building);
        return building;
    }

    /*
    end of getting upgarde
     */

    /*
    getting net profits
     */
    public double getnet(){
        double salesTotalPnL = 0, cogsTotalPnL = 0, sgaExpensesTotalPnL = 0, ebitdaTotalsPnL = 0, taxesTotalsPnL = 0,
                grossMarginTotalPnL = 0, netProfitTotalsPnL = 0, mdSalaryTotalPnL = 0, depreciationTotalPnL = 0,
                operatingProfitsTotalPnL = 0, interestTotalPnL = 0, assetGainsTotalsPnL = 0;

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

        Calendar endDatePnL;
        endDatePnL = Calendar.getInstance();
        endDatePnL.set(Calendar.DAY_OF_MONTH, endDay);
        endDatePnL.set(Calendar.MONTH, endMonth);
        endDatePnL.set(Calendar.YEAR, endYear);

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

        Log.d(TAG, "the mdsal is" + mdSalaryTotalPnL);
        Log.d(TAG, "the loas on stock is" + assetGainsTotalsPnL);
        Log.d(TAG, "the net profit is" + netProfitTotalsPnL);

        return netProfitTotalsPnL;
    }

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
    end of setting month
     */

    /*
    getting number of months
     */

    public int monthNumb(int startMonth, int startYear, int endMonth, int endYear){
        int numb = 1;

        int years = endYear - startYear;

        if(years > 1){
            numb = numb + (12 * (years - 1));
            numb = numb + (12 - startMonth) + (endMonth);
        } else if (years == 0){
            numb = numb + (endMonth - startMonth);
        } else if (years == 1){
            numb = numb + (12 - startMonth) + (endMonth);
        }
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
