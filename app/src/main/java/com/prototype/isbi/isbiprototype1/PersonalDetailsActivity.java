package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;

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
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalData;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PurchaseHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;

/**
 * Created by MRuto on 12/15/2016.
 */

public class PersonalDetailsActivity extends Activity {
    public static String TAG = "PersonalDetailsActivity";

    // initialise parameters to be used
    Button btnNext, btnBack;
    EditText fName, email, confPin, pin, uname;
    LinearLayout titleLayout, buttonLayout, scrollLayout, mainLayout, btnNextLayout, btnBackLayout;
    ScrollView verticleScroll;

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
        setContentView(R.layout.activity_personal_details);

        //lining parameters to values on xml
        confPin = (EditText) findViewById(R.id.md_salary);
        pin = (EditText) findViewById(R.id.pin);
        fName = (EditText) findViewById(R.id.bName);
        uname = (EditText) findViewById(R.id.uName);
        email = (EditText) findViewById(R.id.location);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNxt);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
        scrollLayout = (LinearLayout) findViewById(R.id.scroll_layout);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        btnBackLayout = (LinearLayout) findViewById(R.id.btnBackLayout);
        btnNextLayout = (LinearLayout) findViewById(R.id.btnNxtLayout);
        verticleScroll = (ScrollView) findViewById(R.id.vertical_scroll);

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
                boolean flag = false;

                List<AccountDetails> accountDetailsList = addb.getAllAccounts();
                for (AccountDetails pd : accountDetailsList) {
                    String log = pd.getEmail();
                    // Writing Contacts to log
                    Log.d("Name: ", log);

                    if(log.matches(uname.getText().toString())){
                        flag = true;

                    }
                }

                if((fName.getText()).toString().matches("") || (email.getText()).toString().matches("") ||
                        (confPin.getText()).toString().matches("") || (pin.getText()).toString().matches("")){

                    Log.d("Values: ", "some were null..");

                } else if (!validate()) {
                    Log.d(TAG, "invalid input");

                } else if(flag){
                    Log.d(TAG, "uname taken");
                    uname.setError("User Name already Registered");
                    uname.requestFocus();

                } else if(Integer.parseInt(confPin.getText().toString())
                        != Integer.parseInt(pin.getText().toString())){

                    Log.d("Values: ", "pin do not match..");
                    pin.setError("Pins do not match");
                    pin.requestFocus();

                } else{
                    /**
                     * CRUD Operations
                     * */

                    addb.addAccount(new AccountDetails(uname.getText().toString(), Integer.parseInt(pin.getText().toString())));

                    LoginActivity.database = uname.getText().toString();

                    Log.d(TAG, "uname is " + uname.getText().toString() + " and database is " + LoginActivity.database);

                    setUpDB();

                    List<PersonalData> personalData = pddb.getAllPeople();

                    boolean flag2 = true;
                    for (PersonalData pd : personalData) {
                        flag2 = false;
                    }

                    if(flag2){
                        pddb.addPerson(new PersonalData("ISBI","ISBI",0,0,"ISBI","ISBI-Nairobi"));
                    }

                    PersonalData personalData1 = pddb.getPerson(1);
                    personalData1.setName(fName.getText().toString());
                    personalData1.setEmail(email.getText().toString());
                    personalData1.setPin(Integer.parseInt(pin.getText().toString()));

                    pddb.updatePerson(personalData1);

                    Intent intent = new Intent(getApplicationContext(), BuisnessDetailsActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

        //creating listener and event for back button;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void setUpDB(){
//        cdb.dropTable();
//        pdb.dropTable();
//        pudb.dropTable();
//        pddb.dropTable();
//        gpdb.dropTable();
//        gsdb.dropTable();
//        idb.dropTable();
//        rdb.dropTable();
//        sdb.dropTable();
//        ddb.dropTable();
//        ppdb.dropTable();
//        prdb.dropTable();
//        psdb.dropTable();
//        podb.dropTable();
//        cpsdb.dropTable();
//        cpodb.dropTable();
//        gpsdb.dropTable();
//        gpodb.dropTable();
//        adb.dropTable();
//        odb.dropTable();
//        gvdb.dropTable();
//        adb.dropTable();
//        sadb.dropTable();
//        rudb.dropTable();
//        agdb.dropTable();
//        ldb.dropTable();
//        ildb.dropTable();
//        mdb.dropTable();

        cdb.isTableExists();
        pdb.isTableExists();
        pudb.isTableExists();
        pddb.isTableExists();
        gpdb.isTableExists();
        gsdb.isTableExists();
        idb.isTableExists();
        rdb.isTableExists();
        sdb.isTableExists();
        ddb.isTableExists();
        ppdb.isTableExists();
        prdb.isTableExists();
        psdb.isTableExists();
        podb.isTableExists();
        cpsdb.isTableExists();
        cpodb.isTableExists();
        gpsdb.isTableExists();
        gpodb.isTableExists();
        edb.isTableExists();
        odb.isTableExists();
        gvdb.isTableExists();
        adb.isTableExists();
        sadb.isTableExists();
        rudb.isTableExists();
        agdb.isTableExists();
        ldb.isTableExists();
        ildb.isTableExists();
        mdb.isTableExists();

        if (mdb.getMixCount() <= 0) {
            mdb.addMix(new Mix("false", 0, 0, 0, 0, 0, 0));
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email2 = email.getText().toString();
        String username = uname.getText().toString();
        String password = pin.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            pin.setError("between 4 and 10 alphanumeric characters");
            pin.requestFocus();
            valid = false;

        } else {
            pin.setError(null);

        }

        if (email2.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
            email.setError("enter a valid location address");
            email.requestFocus();
            valid = false;

        } else {
            email.setError(null);

        }

        if (username.contains(" ")) {
            uname.setError("Username cannot have space");
            uname.requestFocus();
            valid = false;

        } else if (username.contains("~") || username.contains("`") || username.contains("!") || username.contains("@")
                || username.contains("#") || username.contains("$") || username.contains("%") || username.contains("^")
                || username.contains("&") || username.contains("*") || username.contains("(") || username.contains(")")
                || username.contains("-") || username.contains("_") || username.contains("=") || username.contains("+")
                || username.contains("[") || username.contains("{") || username.contains("]") || username.contains("}")
                || username.contains(";") || username.contains(":") || username.contains("'") || username.contains("\"")
                || username.contains("\\") || username.contains("|") || username.contains("/") || username.contains("?")
                || username.contains(".") || username.contains(">") || username.contains(",") || username.contains("<")) {
            uname.setError("Username cannot have special characters");
            uname.requestFocus();
            valid = false;

        } else {
            uname.setError(null);

        }

        return valid;
    }

}
