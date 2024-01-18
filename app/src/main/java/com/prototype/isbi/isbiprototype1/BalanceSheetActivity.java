package com.prototype.isbi.isbiprototype1;

import android.nfc.Tag;
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

import com.prototype.isbi.isbiprototype1.databaseHandlers.Cash;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Expenses;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GlobalVariablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalData;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Payable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrder;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSale;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Owner;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Assets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Instalment;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by MRuto on 1/11/2017.
 */

public class BalanceSheetActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String TAG = "BalanceSheetActivity";

    NavigationItemSelected navigationItemSelected = new NavigationItemSelected(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    HashMap<String, String> ownerDraw = new HashMap<>();

    TextView cash, payables, recievables, inventory, preOrder, preSale,
            currentAssets, totalAssets, liabilities, equity, totalLAndE,
            ownerDrawings, shareHolders, equipment, rights, vehicles,
            buildings, land, others, fixedAssets, loansText, loanPayable, savings, rent;

    int cashTotal = 0, payableTotal = 0, recievabltTotal = 0, inventoryTotal = 0, currentAssetsTotal = 0,
            preOrderdTotal = 0, preSalesTotals = 0, assetTotal = 0, liabilitiesTotal = 0, equityTotal = 0,
            lAndETotal = 0, ownerDrawingsTotal = 0, shareHoldersTotal = 0, equipmentTotal = 0, rightsTotal = 0,
            vehiclesTotal = 0, buildingsTotal = 0, othersTotal = 0, fixedAssetsTotal = 0,
            landTotal = 0, savingsTotals = 0, rentTotals = 0;

    double inventoryTotalD = 0, loanPayableTotal = 0, loansTotal = 0;

    CashHandler cdb = new CashHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    ReceivablesHandler rdb = new ReceivablesHandler(this);
    PayablesHandler pdb = new PayablesHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    OwnerHandler odb = new OwnerHandler(this);
    PersonalDataHandler pddb = new PersonalDataHandler(this);
    AssetsHandler adb = new AssetsHandler(this);
    SoldAssetsHandler sadb = new SoldAssetsHandler(this);
    LoansHandler ldb = new LoansHandler(this);
    InstalmentHandler ildb = new InstalmentHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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

        // declare textviews
        cash = (TextView) findViewById(R.id.cash);
        payables = (TextView) findViewById(R.id.payables);
        recievables = (TextView) findViewById(R.id.recievables);
        inventory = (TextView) findViewById(R.id.inventory);
        preOrder = (TextView) findViewById(R.id.pre_order);
        preSale = (TextView) findViewById(R.id.pre_sale);
        currentAssets = (TextView) findViewById(R.id.current_assets);
        totalAssets = (TextView) findViewById(R.id.total_assets);
        liabilities = (TextView) findViewById(R.id.total_liabilities);
        equity = (TextView) findViewById(R.id.equity);
        totalLAndE = (TextView) findViewById(R.id.total_liabilities_equity);
        ownerDrawings = (TextView) findViewById(R.id.owner_drawings);
        shareHolders = (TextView) findViewById(R.id.share_holders);
        equipment = (TextView) findViewById(R.id.equipment);
        rights = (TextView) findViewById(R.id.rights);
        vehicles = (TextView) findViewById(R.id.vehicles);
        buildings = (TextView) findViewById(R.id.buildings);
        land = (TextView) findViewById(R.id.land);
        others = (TextView) findViewById(R.id.others);
        fixedAssets = (TextView) findViewById(R.id.fixed_assets);
        loansText = (TextView) findViewById(R.id.loan);
        loanPayable = (TextView) findViewById(R.id.loan_lnterest_payable);
        savings = (TextView) findViewById(R.id.savings);
        rent =  (TextView) findViewById(R.id.rent);

        /**
         * querying database for totals and setting totals
         **/

        List<Cash> allcash = cdb.getAllCash();

        for (Cash pd : allcash) {
            String log = "Cash in hand; " + pd.getActual() ;
            String log1 = "Bank Accont; " + pd.getBank() ;
            String log2 = "Mpesa; " + pd.getMpesa() ;
            // Writing Contacts to log
            Log.d("Cash Accoutns: ", log + " " + log1 + " " + log2);

            cashTotal = pd.getActual() + pd.getBank() + pd.getMpesa();
        }

        List<Inventory> allInventory = idb.getAllInventory();

        for (Inventory pd : allInventory) {
            inventoryTotalD = inventoryTotalD + pd.getTotal();
//            inventoryTotalD = inventoryTotalD + (i    nt) Math.round(pd.getCost() * pd.getNumber());
            Log.d(TAG, "Inventory item: Cost; " + pd.getCost() + " Number; " + pd.getNumber() + " total; "
                    + pd.getTotal() + " calc tot; " + ((int) Math.round(pd.getCost() * pd.getNumber())));

        }

        inventoryTotal = (int) Math.round(inventoryTotalD);

        List<Receivable> allReceivable = rdb.getAllReceivable();

        for (Receivable pd : allReceivable) {
            recievabltTotal = recievabltTotal + pd.getTotal();
        }

        List<Payable> allPayable = pdb.getAllPayable();

        for (Payable pd : allPayable) {
            payableTotal = payableTotal + pd.getTotal();
        }

        List<PreOrder> allPreOrder = podb.getAllPreOrder();

        for (PreOrder pd : allPreOrder) {
            if(pd.getStatus().matches("OPEN")) {
                preOrderdTotal = preOrderdTotal + pd.getTotal();
            }
        }

        List<PreSale> allPreSale = psdb.getAllPreSale();

        for (PreSale pd : allPreSale) {
            if(pd.getStatus().matches("OPEN")) {
                preSalesTotals = preSalesTotals + pd.getTotal();
            }
        }

        List<Owner> ownerList = odb.getAllOwner();

        for(Owner pd: ownerList){
            if (!pd.getType().matches("Add Funds")) {
                String date = pd.getDate();

                String dat[] = date.split("/");
                int actualMonth = Integer.parseInt(dat[1]) - 1;
                int actualYear = Integer.parseInt(dat[2]);

                String name = "" + actualMonth + "-" + actualYear;
                boolean flag = false;

                for (String key : ownerDraw.keySet()) {
                    if (key.matches(name)) {
                        Log.d(TAG, "ownerList found " + key + " with value " + ownerDraw.get(key));
                        flag = true;
                    }
                }

                if (flag) {
                    int numb = pd.getTotal() + Integer.parseInt(ownerDraw.get("" + name));
                    Log.d(TAG, "ownerList calc " + pd.getTotal() + " + " + Integer.parseInt(ownerDraw.get("" + name)));

                    ownerDraw.remove("" + name);
                    Log.d(TAG, "ownerList remove " + "" + name);

                    ownerDraw.put("" + name, "" + numb);
                    Log.d(TAG, "ownerList put " + name + " " + numb);

                } else if (!flag) {
                    ownerDraw.put("" + name, "" + pd.getTotal());
                    Log.d(TAG, "ownerList put no remove " + name + " " + pd.getTotal());

                }
            } else if(pd.getType().matches("Add Funds")) {
                ownerDrawingsTotal = ownerDrawingsTotal + pd.getTotal();
                Log.d(TAG, "ownerList Add Funds " + pd.getTotal() + " now " + ownerDrawingsTotal);

            }
        }

        PersonalData personalData = pddb.getPerson(1);

        int mdSalary = personalData.getMDSalary();

        for(String key : ownerDraw.keySet()){
            Log.d(TAG, "ownerDrawPnl.keySet " + key + " total " + ownerDraw.get(key));

            int numb = Integer.parseInt(ownerDraw.get(key)) - mdSalary;
            Log.d(TAG, "ownerDrawPnl.keySet calc numb " + Integer.parseInt(ownerDraw.get(key)) + " - " + mdSalary);

            if(numb > 0){
                Log.d(TAG, "ownerDrawPnl.keySet calc tot " + ownerDrawingsTotal + " - " + numb);
                ownerDrawingsTotal = ownerDrawingsTotal - numb;
            }
            Log.d(TAG, "ownerDrawPnl.keySet final " + ownerDrawingsTotal);

        }

        List<Assets> assetsList = adb.getAllAssets();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

//        int year = 2017;
//        int month = 11;
//        int day = 1;

        Calendar today;
        today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_MONTH, day);
        today.set(Calendar.MONTH, month);
        today.set(Calendar.YEAR, year);

        for (Assets pd : assetsList) {
            String date = pd.getDate();

            String dat[] = date.split("/");
            int actualDay = Integer.parseInt(dat[0]);
            int actualMonth = Integer.parseInt(dat[1]) - 1;
            int actualYear = Integer.parseInt(dat[2]);

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

            Log.d(TAG, " Asset Buy Date " + actualDay + "/" + actualMonth + "/" + actualYear
                    + " Expire Buy Date " + stopDay + "/" + stopMonth + "/" + stopYear
                    + " months between " + pd.getUsefull());

            if (pd.getType().matches("Land")){
                landTotal = landTotal + pd.getTotal();
                Log.i(TAG, "Land calcing;  total " + pd.getTotal());

            } else if (pd.getType().matches("Building")){
                buildingsTotal = buildingsTotal + pd.getTotal();
                Log.i(TAG, "Building calcing;  total " + pd.getTotal());

            }  else if (pd.getType().matches("Vehicle")){
                int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year);
                double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear);

                int pass = (int) Math.round(pd.getTotal()
                        - ((((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)) * (double) days));

                Log.i(TAG, "Vehicle calcing;  total " + pd.getTotal()
                        + " minus " + (pd.getTotal() - ((((double) pd.getTotal()
                        - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                        + " days " + days
                        + " daysTot " + daysTotal
                        + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                if (pass < pd.getScrsp()){
                    vehiclesTotal = vehiclesTotal + pd.getScrsp();
                    Log.d(TAG, "Vehicle; Total = " + pd.getTotal() + " scrap = " + pd.getScrsp() + " vehiclesTotal " + vehiclesTotal);

                } else {
                    vehiclesTotal = vehiclesTotal + pass;
                    Log.d(TAG, "Vehicle; Total = " + pd.getTotal() + " remove = " + pass + " vehiclesTotal " + vehiclesTotal);

                }
            } else if (pd.getType().matches("Equipment")){
                int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year);
                double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear);
//                if (days == 0){
//                    days = 1;
//                }

                int pass = (int) Math.round(pd.getTotal()
                        - ((((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)) * (double) days));

                Log.i(TAG, "Equipment calcing;  total " + pd.getTotal()
                        + " minus " + (pd.getTotal() - ((((double) pd.getTotal()
                        - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                        + " days " + days
                        + " daysTot " + daysTotal
                        + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                if (pass < pd.getScrsp()){
                    equipmentTotal = equipmentTotal + pd.getScrsp();

                } else {
                    equipmentTotal = equipmentTotal + pass;

                }
            } else if (pd.getType().matches("Licenses")){
                int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year);
                double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear);
//                if (days == 0){
//                    days = 1;
//                }

                int pass = (int) Math.round(pd.getTotal()
                        - (((double)pd.getTotal() / (daysTotal)) * (double) days));

                Log.i(TAG, "License calcing;  total " + pd.getTotal()
                        + " minus " + ((double)pd.getTotal() / (daysTotal)) * (double)days
                        + " days " + days
                        + " per day " + ((double)pd.getTotal() / (daysTotal)));

                if (pass < 0){
                    pd.setInfo("" + pd.getInfo() + "; expired");

                    Calendar expire;
                    expire = Calendar.getInstance();
                    expire.set(Calendar.DAY_OF_MONTH, actualDay);
                    expire.set(Calendar.MONTH, (actualMonth));
                    expire.set(Calendar.YEAR, actualYear);

//                    setMonth(expire, (actualMonth + pd.getUsefull()));

                    int expireMonth = expire.get(Calendar.MONTH);
                    int expireDay = expire.get(Calendar.DAY_OF_MONTH);
                    int expireYear = expire.get(Calendar.YEAR);

                    SoldAssets soldAssets = new SoldAssets(pd.getType(), pd.getInfo(),
                            pd.getTotal(), pd.getPayment(), pd.getDate(), pd.getUsefull(),
                            pd.getScrsp(), 0, expireDay + "/" + expireMonth + "/" + expireYear, pd.getID());

                    sadb.addAssets(soldAssets);
                    adb.deleteAssets(pd);

                } else {
                    rightsTotal = rightsTotal + pass;

                }
            } else if (pd.getType().matches("Other")){
//                othersTotal = othersTotal + pd.getTotal();
                int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year);
                double daysTotal = dayNumb(actualDay, actualMonth, actualYear, stopDay, stopMonth, stopYear);

//                if (days == 0){
//                    days = 1;
//                }

                int pass = (int) Math.round(pd.getTotal()
                        - ((((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)) * (double)days));

                Log.i(TAG, "Other calcing;  total " + pd.getTotal()
                        + " minus " + (pd.getTotal() - ((((double) pd.getTotal()
                        - (double) pd.getScrsp()) / (daysTotal)) * (double)days))
                        + " days " + days
                        + " daysTot " + daysTotal
                        + " per day " + (((double) pd.getTotal() - (double) pd.getScrsp()) / (daysTotal)));

                if (pass < pd.getScrsp()){
                    othersTotal = othersTotal + pd.getScrsp();
                    Log.d(TAG, "othersTotal; Total = " + pd.getTotal() + " scrap = " + pd.getScrsp() + " othersTotal " + othersTotal);

                } else {
                    othersTotal = othersTotal + pass;
                    Log.d(TAG, "othersTotal; Total = " + pd.getTotal() + " remove = " + pass + " othersTotal " + othersTotal);

                }
            }
        }

        List<Loans> loans = ldb.getAllLoans();

        for(Loans pd: loans) {
            if (pd.getStatus().toString().matches("PAY")) {
                List<Instalment> instalments = ildb.getByForeignKey(pd.getID());

                String name[] = pd.getName().split("-");
                String show = name[(name.length - 1)];

                for(Instalment pd1: instalments) {
                    if(show.matches("SAV")) {
                        savingsTotals = savingsTotals + pd1.getContractualSavings();
                    }
                }
            }
        }

        for(Loans pd: loans){
            if(pd.getStatus().toString().matches("NOT")){
                Log.d(TAG, "Loan Amount " + pd.getTotal());
                loansTotal = loansTotal + pd.getTotal();

                String startD = pd.getStartDate();
                Log.d(TAG, "Loan date " + pd.getStartDate());

                String dat[] = startD.split("/");
                int actualDay = Integer.parseInt(dat[0]);
                int actualMonth = Integer.parseInt(dat[1]) - 1;
                int actualYear = Integer.parseInt(dat[2]);

                String endD = pd.getEndDate();

                String enddat[] = endD.split("/");
                int endDa = Integer.parseInt(enddat[0]);
                int endM = Integer.parseInt(enddat[1]) - 1;
                int endY = Integer.parseInt(enddat[2]);

                Calendar stopDate;
                stopDate = Calendar.getInstance();
                stopDate.set(Calendar.DAY_OF_MONTH, endDa);
                stopDate.set(Calendar.MONTH, endM);
                stopDate.set(Calendar.YEAR, endY);

                double totalDays = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;
                Log.d(TAG, "Loan days tot " + totalDays);

                if(totalDays <= 0){
                    totalDays = 1;
                }

                double days = dayNumb(actualDay, actualMonth, actualYear, day, month, year);
//                int days = dayNumb(actualDay, actualMonth, actualYear, day, month, year);
                Log.d(TAG, "Loan days " + days);

//                if (days == 0){
//                    days = 1;
//                }

                double loanCost = (((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal());

                double loanInterestPerDay = loanCost / totalDays;

                double interest = loanInterestPerDay * days;

                Log.d(TAG, "Loan getInstalments " + pd.getInstalments());
                Log.d(TAG, "Loan getInstalmentsNo " + pd.getInstalmentsNo());
                Log.d(TAG, "Loan loanCost " + loanCost);
                Log.d(TAG, "Loan Total Days " + totalDays);
                Log.d(TAG, "Loan loanInterestPerDay " + loanInterestPerDay);
                Log.d(TAG, "Loan interest " + interest);

                Log.i("calcinginst; ", " actual ins " + (pd.getInstalments())
                        +" total payback " + ((pd.getInstalments()) * pd.getInstalmentsNo())
                        + " interest " + (((pd.getInstalments() ) * pd.getInstalmentsNo()) - pd.getTotal())
                        + " per day " + ((((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
                        / totalDays)
                        + " now " + ((((pd.getInstalments()) * pd.getInstalmentsNo()) - pd.getTotal())
                        / totalDays) * days);

//                loanPayableTotal = loanPayableTotal + interest;

                Log.i("calcingloan; ", " days " + days
                        +" totalDays " + totalDays
                        + " interest " + interest
//                        + " loanPayableTotal " + days
                        + " loansTotal " + loansTotal);

                List<Instalment> instalments = ildb.getByForeignKey(pd.getID());

                double daysToNow = dayNumb(actualDay, actualMonth, actualYear, day, month, year) + 1;

                if (today.after(stopDate)){
                    Log.d(TAG, "today after stopdate ");
                    daysToNow = dayNumb(actualDay, actualMonth, actualYear, endDa, endM, endY) + 1;

                }

                double interestToNow = loanInterestPerDay * daysToNow;

                loanPayableTotal = loanPayableTotal + interestToNow;

                Log.d(TAG, "Inst loanPayableTotal " + loanPayableTotal
                        + " Inst Days " + daysToNow);

                double instCount = 0;

                for(Instalment pd1: instalments) {
                    savingsTotals = savingsTotals + pd1.getContractualSavings();

//                    double perc = ((pd.getInstalments()) * pd.getInstalmentsNo());
//                    double send = (((perc - (double) pd.getTotal()) / perc) * ((double) pd1.getTotal()));
//
//                    Log.i(TAG, "calcingInte%;  tot " + ((pd.getInstalments()) * pd.getInstalmentsNo())
//                            + " divid " + (perc - pd.getTotal())
//                            +" send " + (((perc - (double) pd.getTotal()) / perc) * ((double) pd1.getTotal())));
//                    loanPayableTotal = loanPayableTotal - send;

                    String startDIns = pd1.getDate();
                    Log.d(TAG, "Inst date " + pd1.getDate());

                    String datIns[] = startDIns.split("/");
                    int actualDayIns = Integer.parseInt(datIns[0]);
                    int actualMonthIns = Integer.parseInt(datIns[1]) - 1;
                    int actualYearIns = Integer.parseInt(datIns[2]);

                    double daysIns = dayNumb(actualDayIns, actualMonthIns, actualYearIns, day, month, year) + 1;
                    double daysBeforePay = dayNumb(actualDay, actualMonth, actualYear, actualDayIns, actualMonthIns, actualYearIns) + 1;

                    double interestIns = loanInterestPerDay * daysIns;

                    double remeinder = (loanInterestPerDay * daysBeforePay) - ((double)pd1.getTotal()
                            * (loanCost / (((double)pd.getInstalments()) * (double)pd.getInstalmentsNo())));

                    double remove = ((double)pd1.getTotal()
                            * (loanCost / (((double)pd.getInstalments()) * (double)pd.getInstalmentsNo())));

                    instCount = instCount + remeinder;
//                    loanPayableTotal = interestIns;

                    loanPayableTotal = loanPayableTotal - remove;

                    Log.d(TAG, "Inst loanPayableTotal " + loanPayableTotal
                            + " Inst Days " + daysIns);

                    actualDay = actualDayIns;
                    actualMonth = actualMonthIns;
                    actualYear = actualYearIns;

                    double remove2 = (double)pd1.getTotal() * (pd.getTotal() / (((double)pd.getInstalments())
                            * (double)pd.getInstalmentsNo()));

                    loansTotal = loansTotal - remove2;

//                    double send2 = ((((double) pd.getTotal()) / perc) * ((double) pd1.getTotal()));
//                    Log.i("calcingInte2%; ", " tot " + ((pd.getInstalments()) * pd.getInstalmentsNo())
//                            + " perc " + perc
//                            +" send2 " + (((double) pd.getTotal() / perc) * ((double) pd1.getTotal())));
//                    loansTotal = loansTotal - (int) Math.round(send2);
//
//                    Log.i("calcingloan2; ", " perc " + perc
//                            + " send2 " + send2
//                            + " loanPayableTotal " + days
//                            + " loansTotal " + loansTotal);

                }
            }
        }

        List<Expenses> expensesList = edb.getAllExpenses();

        for( Expenses pd : expensesList){
            if (pd.getType().matches("Rent")){
                String startD = pd.getDate();

                String dat[] = startD.split("/");
                int startDay = Integer.parseInt(dat[0]);
                int startMonth = Integer.parseInt(dat[1]) - 1;
                int startYear = Integer.parseInt(dat[2]);

                int months = Integer.parseInt(pd.getInfo());

                //int endDa = Integer.parseInt(dat[0]);
                //int endM = Integer.parseInt(dat[1]) + months;
                //int endY = Integer.parseInt(dat[2]);

                Calendar endDate;
                endDate = Calendar.getInstance();
                endDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dat[0]));
                endDate.set(Calendar.MONTH, (Integer.parseInt(dat[1]) - 1 + months));
                endDate.set(Calendar.YEAR, Integer.parseInt(dat[2]));

                //actualDate2 = setMonth(actualDate2, (Integer.parseInt(dat[1]) + months));
                //actualDate2 = setMonth(actualDate2, (12));

                int endDa = endDate.get(Calendar.DAY_OF_MONTH);
                int endM = endDate.get(Calendar.MONTH);
                int endY = endDate.get(Calendar.YEAR);

                Log.d(TAG, " startdate " + pd.getDate());
                Log.d(TAG, " monhts paid " + months);
                Log.d(TAG, " monhts calc " + (Integer.parseInt(dat[1]) + months));
                Log.d(TAG, " end date " + endDate.get(Calendar.DAY_OF_MONTH) + "/"
                        + (endDate.get(Calendar.MONTH)) + "/" + endDate.get(Calendar.YEAR));
                Log.d(TAG, " endMonthz " + endM);

                int totalDays = dayNumb(startDay, startMonth, startYear, endDa, endM, endY);
                Log.d(TAG, " totaldays " + totalDays);
                int passedDays = dayNumb(startDay, startMonth, startYear, day, month, year) + 1;
                Log.d(TAG, " passedDays " + passedDays);
//                if (passedDays == 0){
//                    passedDays = 1;
//                }

                if (totalDays > passedDays) {
                    double perDay = (double) pd.getTotal() / (double) totalDays;
                    double remove = perDay * (double) passedDays;
                    double pass = (double) pd.getTotal() - remove;

                    Log.d(TAG, " perDay " + ((double) pd.getTotal() / (double) totalDays));
                    Log.d(TAG, " remove " + (perDay * (double) passedDays));
                    Log.d(TAG, " pass " + ((double) pd.getTotal() - remove));

//                    rentTotals = rentTotals + pd.getTotal();
                    rentTotals = rentTotals + (int) Math.round(pass);
                }

            }
        }

        currentAssetsTotal = cashTotal + recievabltTotal + inventoryTotal + preOrderdTotal + savingsTotals + rentTotals;
        fixedAssetsTotal = equipmentTotal + landTotal + rightsTotal + othersTotal + buildingsTotal + vehiclesTotal;
        assetTotal = currentAssetsTotal + fixedAssetsTotal;
        liabilitiesTotal = payableTotal + preSalesTotals + (int) Math.round(loansTotal) + (int) Math.round(loanPayableTotal);
        shareHoldersTotal = assetTotal - liabilitiesTotal - ownerDrawingsTotal;
        equityTotal = ownerDrawingsTotal + shareHoldersTotal;
        lAndETotal = equityTotal + liabilitiesTotal;

                /**
                 * end of querying
                 **/

        cash.setText("" + numberFormat.format(cashTotal));
        payables.setText("" + numberFormat.format(payableTotal));
        recievables.setText("" + numberFormat.format(recievabltTotal));
        inventory.setText("" + numberFormat.format(inventoryTotal));
        preOrder.setText("" + numberFormat.format(preOrderdTotal));
        preSale.setText("" + numberFormat.format(preSalesTotals));
        currentAssets.setText("" + numberFormat.format(currentAssetsTotal));
        totalAssets.setText("" + numberFormat.format(assetTotal));
        liabilities.setText("" + numberFormat.format(liabilitiesTotal));
        equity.setText("" + numberFormat.format(equityTotal));
        totalLAndE.setText("" + numberFormat.format(lAndETotal));
        ownerDrawings.setText("" + numberFormat.format(ownerDrawingsTotal));
        shareHolders.setText("" + numberFormat.format(shareHoldersTotal));
        equipment.setText("" + numberFormat.format(equipmentTotal));
        rights.setText("" + numberFormat.format(rightsTotal));
        vehicles.setText("" + numberFormat.format(vehiclesTotal));
        buildings.setText("" + numberFormat.format(buildingsTotal));
        land.setText("" + numberFormat.format(landTotal));
        others.setText("" + numberFormat.format(othersTotal));
        fixedAssets.setText("" + numberFormat.format(fixedAssetsTotal));
        loansText.setText("" + numberFormat.format((int) Math.round(loansTotal)));
        loanPayable.setText("" + numberFormat.format((int)Math.round(loanPayableTotal)));
        savings.setText("" + numberFormat.format(savingsTotals));
        rent.setText("" + numberFormat.format(rentTotals));

    }

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
//        } else  {
//            date.set(Calendar.MONTH, month);
//        }
//
//        return date;
//    }
//
//    public int monthNumb(int startMonth, int startYear, int endMonth, int endYear){
//        int numb = 1;
//
//        int years = endYear - startYear;
//
//        if(years > 1){
//            numb = numb + (12 * (years - 1));
//            numb = numb + (12 - startMonth) + (endMonth);
//        } else if (years == 0){
//            numb = numb + (endMonth - startMonth);
//        } else if (years == 1){
//            numb = numb + (12 - startMonth) + (endMonth);
//        }
//        return numb;
//    }

//    public int dayNumb(double startDay, double startMonth, double startYear, double endDay, double endMonth, double endYear){
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
//           months = (endMonth + (12 * (endYear - startYear))) - startMonth - 1;
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

        Log.i(TAG, "Therefore DaysNumb is " + ret + " minus 1");
//        ret = ret - 1;
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
