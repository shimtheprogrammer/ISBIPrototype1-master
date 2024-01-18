package com.prototype.isbi.isbiprototype1;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.prototype.isbi.isbiprototype1.databaseHandlers.AccountDetails;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AccountDetailsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ClosedPreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ClosedPreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.DeliveryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GlobalVariablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PurchaseHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;

/**
 * Created by MRuto on 5/5/2017.
 */

public class DeleteActivity extends Activity {

    AccountDetailsHandler addb = new AccountDetailsHandler(this);
    CashHandler cdb = new CashHandler(this);
    PayablesHandler pdb = new PayablesHandler(this);
    PurchaseHandler pudb = new PurchaseHandler(this);
    PersonalDataHandler pddb = new PersonalDataHandler(this);
    GoodsPurchasedHandler gpdb = new GoodsPurchasedHandler(this);
    GoodsSalesHandler gsdb = new GoodsSalesHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    ReceivablesHandler rdb = new ReceivablesHandler(this);
    SalesHandler sdb = new SalesHandler(this);
    DeliveryHandler ddb = new DeliveryHandler(this);
    PaidPayablesHandler ppdb = new PaidPayablesHandler(this);
    PaidReceivablesHandler prdb = new PaidReceivablesHandler(this);
    PreSaleHandler psdb = new PreSaleHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    ClosedPreSaleHandler cpsdb = new ClosedPreSaleHandler(this);
    ClosedPreOrderHandler cpodb = new ClosedPreOrderHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);
    GoodsPreOrderedHandler gpodb = new GoodsPreOrderedHandler(this);
    ExpensesHandler edb = new ExpensesHandler(this);
    OwnerHandler odb = new OwnerHandler(this);
    GlobalVariablesHandler gvdb = new GlobalVariablesHandler(this);
    AssetsHandler adb = new AssetsHandler(this);
    SoldAssetsHandler sadb = new SoldAssetsHandler(this);
    ReUpHandler rudb = new ReUpHandler(this);
    AssetGainsHandler agdb = new AssetGainsHandler(this);
    LoansHandler ldb = new LoansHandler(this);
    InstalmentHandler ildb = new InstalmentHandler(this);
    MixHandler mdb = new MixHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        new AlertDialog.Builder(DeleteActivity.this)
                .setIcon(R.drawable.purchase_icon_2)
                .setTitle("Confirm Deletion")
                .setMessage("This will Delete all information from the App")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cdb.dropTable();
                        pdb.dropTable();
                        pudb.dropTable();
                        pddb.dropTable();
                        gpdb.dropTable();
                        gsdb.dropTable();
                        idb.dropTable();
                        rdb.dropTable();
                        sdb.dropTable();
                        ddb.dropTable();
                        ppdb.dropTable();
                        prdb.dropTable();
                        psdb.dropTable();
                        podb.dropTable();
                        cpsdb.dropTable();
                        cpodb.dropTable();
                        gpsdb.dropTable();
                        gpodb.dropTable();
                        edb.dropTable();
                        odb.dropTable();
                        gvdb.dropTable();
                        adb.dropTable();
                        sadb.dropTable();
                        rudb.dropTable();
                        agdb.dropTable();
                        ldb.dropTable();
                        ildb.dropTable();
                        mdb.dropTable();

                        AccountDetails accountDetails = addb.getAccountByName(LoginActivity.database);

                        addb.deleteAccount(accountDetails);

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .show();

    }
}
