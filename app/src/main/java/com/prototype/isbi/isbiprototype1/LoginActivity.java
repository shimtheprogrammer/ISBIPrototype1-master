package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.AccountDetails;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AccountDetailsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.CashHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GlobalVariablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalData;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PersonalDataHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PurchaseHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPurchasedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.DeliveryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidReceivablesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ClosedPreOrderHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ClosedPreSaleHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ExpensesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.OwnerHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetGainsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SoldAssetsHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReUpHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InstalmentHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MRuto on 12/15/2016.
 */

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static String database;

    // initialise parameters to be used
    //Button btnLogin, btnExit;
    TextView btnRegister, btnLogin, btnExit;
    EditText pin;
    AutoCompleteTextView email, address;

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

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String PinKey = "pinKey";
    public static final String EmailKey = "emailKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // from manifest @mipmap/ic_launcher

        //check if tables exist
//        addb.dropTable();
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

        addb.isTableExists();
//        cdb.isTableExists();
//        pdb.isTableExists();
//        pudb.isTableExists();
//        pddb.isTableExists();
//        gpdb.isTableExists();
//        gsdb.isTableExists();
//        idb.isTableExists();
//        rdb.isTableExists();
//        sdb.isTableExists();
//        ddb.isTableExists();
//        ppdb.isTableExists();
//        prdb.isTableExists();
//        psdb.isTableExists();
//        podb.isTableExists();
//        cpsdb.isTableExists();
//        cpodb.isTableExists();
//        gpsdb.isTableExists();
//        gpodb.isTableExists();
//        edb.isTableExists();
//        odb.isTableExists();
//        gvdb.isTableExists();
//        adb.isTableExists();
//        sadb.isTableExists();
//        rudb.isTableExists();
//        agdb.isTableExists();
//        ldb.isTableExists();
//        ildb.isTableExists();
//        mdb.isTableExists();

//        if (mdb.getMixCount() <= 0) {
//            mdb.addMix(new Mix("false", 0, 0, 0, 0, 0, 0));
//        }

        //lining parameters to values on xml
//        email = (EditText) findViewById(R.id.location);
//        pin = (EditText) findViewById(R.id.password);
//        btnLogin = (Button) findViewById(R.id.log_in_button);
//        //btnExit = (Button) findViewById(R.id.btnExit);
//        btnRegister = (TextView) findViewById(R.id.register_button);

//        email = (AutoCompleteTextView) findViewById(R.id.email);
        address = (AutoCompleteTextView) findViewById(R.id.email);
        pin = (EditText) findViewById(R.id.password);
        btnLogin = (TextView) findViewById(R.id.btn_submit);
        btnExit = (TextView) findViewById(R.id.btn_cancel);
        btnRegister = (TextView) findViewById(R.id.btn_register);

        List<String> emailNames = new ArrayList<>();

        List<AccountDetails> accountDetailsList = addb.getAllAccounts();
        for (AccountDetails pd : accountDetailsList) {
            String log = pd.getEmail();
            // Writing Contacts to log
            Log.d("Name: ", log);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < emailNames.size(); i++){
                    if((emailNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    emailNames.add(log);
                }
            }
        }

        String[] goods = emailNames.toArray(new String[emailNames.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goods);

        //Find TextView control
        address.setThreshold(1);

        address.setAdapter(adapter);
        //Set the number of characters the user must type before the drop down list is shown

        //Set the adapter

        //product.addTextChangedListener(textWatcher);

        address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pin.requestFocus();

            }
        });

        /**
         * end of auto complete product name
         **/

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

//        btnLogin.setBackgroundResource(R.drawable.see_through_button);
//        btnLogin.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        btnLogin.setBackgroundResource(R.drawable.half_see_through_button);
//                        return false; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        btnLogin.setBackgroundResource(R.drawable.see_through_button);
//                        return false; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //creating listener and event for register button;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String e  = location.getText().toString();
//                String p  = pin.getText().toString();
//
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//
//                editor.putString(Pin, p);
//                editor.putString(Email, e);
//                editor.commit();
//
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();

                login();
            }
        });

        //creating listener and event for login button;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
//                startActivity(intent);
//                finish();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String emailString  = address.getText().toString();
        String password  = pin.getText().toString();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(PinKey, password);
        editor.putString(EmailKey, emailString);
        editor.commit();

        database = address.getText().toString();
//        setUpDB();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        //onLoginFailed();
                        progressDialog.dismiss();
//                        onResume();
                        finish();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        //finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = address.getText().toString();
        String password = pin.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            pin.setError("between 4 and 10 alphanumeric characters");
            pin.requestFocus();
            valid = false;

        } else {
            pin.setError(null);

        }

        final List<AccountDetails> accountDetailsList = addb.getAllAccounts();

        boolean flag = true;

        for (AccountDetails pd : accountDetailsList) {
            String log = pd.getEmail();
            // Writing Contacts to log
            Log.d("Name: ", log);
            if(log.matches(address.getText().toString())){
                flag = false;

                if(!(pin.getText().toString().matches(""))){
                    if(pd.getPin() != Integer.parseInt(pin.getText().toString())){
                        pin.setError("Wrong Pin");
                        pin.requestFocus();
                        valid = false;

                    }
                } else {
                    pin.setError("Enter Pin");
                    pin.requestFocus();
                    valid = false;

                }
            }
        }

        if (flag){
            address.setError("Account is not Registered");
            address.requestFocus();
            valid = false;

        }

        if (username.isEmpty()){// || !android.util.Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
            address.setError("enter a valid username");
            address.requestFocus();
            valid = false;

        } if (username.contains(" ")) {
            address.setError("Username cannot have space");
            address.requestFocus();
            valid = false;

        } else if (username.contains("~") || username.contains("`") || username.contains("!") || username.contains("@")
                || username.contains("#") || username.contains("$") || username.contains("%") || username.contains("^")
                || username.contains("&") || username.contains("*") || username.contains("(") || username.contains(")")
                || username.contains("-") || username.contains("_") || username.contains("=") || username.contains("+")
                || username.contains("[") || username.contains("{") || username.contains("]") || username.contains("}")
                || username.contains(";") || username.contains(":") || username.contains("'") || username.contains("\"")
                || username.contains("\\") || username.contains("|") || username.contains("/") || username.contains("?")
                || username.contains(".") || username.contains(">") || username.contains(",") || username.contains("<")) {
            address.setError("Username cannot have special characters");
            address.requestFocus();
            valid = false;

        } else {
            address.setError(null);

        }

        return valid;
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
}
