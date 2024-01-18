package com.prototype.isbi.isbiprototype1;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSales;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsSalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Mix;
import com.prototype.isbi.isbiprototype1.databaseHandlers.MixHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.SalesHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * Created by MRuto on 1/9/2017.
 */

public class AddSalesActivity extends Activity implements OnItemSelectedListener {
    public static String TAG = "AddSalesActivity";

    Button btnExit, btnAdd;
    EditText number, cost, total;
    Spinner product, unit;

    private Calendar calendar;
    private int year, month, day;
    private String units;

    String productName = "";
    int cstTotal;

    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    GoodsSalesHandler gsdb = new GoodsSalesHandler(this);
    SalesHandler sdb = new SalesHandler(this);
    InventoryHandler db = new InventoryHandler(this);
    MixHandler mdb = new MixHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //getWindow().setLayout((343), (285));
        getWindow().setLayout((400), (380));

        //lining parameters to values on xml
        product = (Spinner) findViewById(R.id.product);
        number = (EditText) findViewById(R.id.number);
        cost = (EditText) findViewById(R.id.cost);
        total = (EditText) findViewById(R.id.total);

        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);

        //setting up auto calc of total
        total.setEnabled(false);
        number.addTextChangedListener(totalWatcher);
        cost.addTextChangedListener(totalWatcher);
        cost.addTextChangedListener(commaSeparator);
        number.addTextChangedListener(maxIntChecker);
        cost.addTextChangedListener(maxIntChecker);
        total.addTextChangedListener(maxIntChecker);

        /*
        Spinner units
         */
        unit = (Spinner) findViewById(R.id.unit);

        final List<String> unitTypes = new ArrayList<>();
        unitTypes.add("Pcs");
        unitTypes.add("Kgs");
        unitTypes.add("Ls");

        ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<>(this, R.layout.activity_spinner_item, unitTypes);

        // Drop down layout style - list view with radio button
        dataAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        unit.setAdapter(dataAdapterUnits);

        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                           @Override
                                           public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                               // On selecting a spinner item
                                               units = parent.getItemAtPosition(position).toString();

                                               if (units.matches("Pcs")) {
                                                   Log.d(TAG, "setting cost to real number");
                                                   number.setInputType(InputType.TYPE_CLASS_NUMBER);
                                               } else {
                                                   Log.d(TAG, "setting cost to decimal number");
                                                   number.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                                                           | InputType.TYPE_CLASS_NUMBER);
                                               }

                                           }

                                           public void onNothingSelected(AdapterView arg0) {

                                           }
                                       }
        );

        unit.setEnabled(false);
        /*
        End of Spinner units
         */

        //setting products spinner
        product.setOnItemSelectedListener(this);

        List<Inventory> inventory = db.getAllInventory();

        List<String> products = new ArrayList<>();

        List<GoodsSales> goodsSale = gsdb.getByForeignKey(sdb.getRowCount() + 1);

        boolean found = false;
        for (GoodsSales pd : goodsSale) {
            if(pd.getProduct().matches("--Delivery--")){
                found = true;
            }
        }

        if(!found) {
            products.add("--Delivery--");
        }

        for (Inventory pd : inventory) {
            String name = pd.getProduct();
            // Writing Contacts to log
            Log.d(TAG,"Name: " + name);

            int num = 0;

            num = num + pd.getNumber();

            List<GoodsSales> goodsSale1 = gsdb.getByForeignKey(sdb.getRowCount() + 1);

            for (GoodsSales pd1 : goodsSale1) {
                if(name.matches(pd1.getProduct())){
                    num = num - pd1.getNumber();
                }
            }

            if(num > 0) {
                products.add(name);
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_spinner_item, products);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        product.setAdapter(adapter);

        btnAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnAdd.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnAdd.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        //creating listener and event for Submit button;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cstArray[] = (cost.getText().toString()).split(",");
                StringBuilder cst1 = new StringBuilder();

//                for(int i = 0; i < cstArray.length; i++){
//                    cst1.append(cstArray[i]);
//                }

                for( String i : cstArray){
                    cst1.append(i);
                }

                if ((number.getText()).toString().matches("")){
                    Log.d(TAG, "btnAdd; no quantity set..");

                    number.setError("Please Enter Quantity");
                    number.requestFocus();

                } else if((cost.getText()).toString().matches("")){
                    Log.d(TAG, "btnAdd; cost null..");

                    cost.setError("Please Enter Price Per Pcs/Kg/L");
                    cost.requestFocus();

                } else if(((Integer.parseInt(cst1.toString())) * (Double.parseDouble(number.getText().toString()))) <= 0){
                    Log.d(TAG, "btnAdd; some total null..");

                    total.setError("Total Cannot be 0");
                    total.requestFocus();

                } else {
                    /**
                     * CRUD Operations
                     * */

                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);

                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);

                    String date = day + "/" + (month + 1) + "/" + year;

                    double numb = Double.parseDouble(number.getText().toString());
                    double cst = cstTotal;
                    if(!units.matches("Pcs")) {
                        cst = cst / 1000;
                    }
                    if(units.matches("Kgs") || units.matches("Ls")){
                        numb = numb * 1000;
                    }
                    if(units.matches("Kgs")){
                        units = "Grms";
                    }
                    if(units.matches("Ls")){
                        units = "mLs";
                    }
                    if(!(productName.matches("--Delivery--"))) {
                        double pcost = 0;

                        List<Inventory> allInventory = db.getAllInventory();

                        for (Inventory pd : allInventory) {
                            if (pd.getProduct().matches(productName)) {
                                pcost = pd.getCost();
                            }
                        }

                        gsdb.addSales(new GoodsSales(productName,
                                (int) Math.round(numb), units, cst, (int) Math.round(numb * cst),
                                date , (sdb.getRowCount() + 1), pcost));
                    } else{
                        gsdb.addSales(new GoodsSales(productName, (int) Math.round(Double.parseDouble(number.getText().toString())),
                                "Trip", Integer.parseInt(cst1.toString()), Integer.parseInt(cst1.toString()),
                                date , (sdb.getRowCount() + 1), 0));
                    }

                    Intent intent = new Intent(getApplicationContext(), SaleActivity.class);

                    intent.putExtra("FROM", "ADD");

                    List<GoodsSales> sales = gsdb.getByForeignKey(sdb.getRowCount() + 1);

                    if(sales.size() == 1){
                        startActivity(intent);
                    }

                    Mix mix = mdb.getMix(1);

                    if(mix.getFlag().matches("true")) {
                        mix.setFlag("false");
                        mix.setCredit(0);
                        mix.setCash(0);
                        mix.setMpesa(0);
                        mix.setBank(0);
                        mix.setMpesaFee(0);
                        mix.setBankFee(0);

                        mdb.updateMix(mix);
                    }

                    finish();
                }

            }
        });

        btnExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnExit.setBackgroundResource(R.drawable.button_on);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        btnExit.setBackgroundResource(R.drawable.button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        //creating listener and event for back button;
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * methodes for product spinner
     **/

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        productName = item;

        number.setText("");
        cost.setText("");
        total.setText("");

        if(item.matches("--Delivery--")){
            number.setText("" + 1);
            number.setEnabled(false);

            List<String> unitTypes = new ArrayList<>();
            unitTypes.add("UNITS");

            ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<>(this, R.layout.activity_spinner_item, unitTypes);

            // Drop down layout style - list view with radio button
            dataAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            unit.setAdapter(dataAdapterUnits);

            unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                               @Override
                                               public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                                   // On selecting a spinner item
                                                   units = parent.getItemAtPosition(position).toString();

                                               }

                                               public void onNothingSelected(AdapterView arg0) {

                                               }
                                           }
            );

            unit.setEnabled(false);

        } else{
            number.setEnabled(true);

            Inventory inventory = db.getInventoryByProduct(item);

            cost.setText("" + inventory.getSPrice());

            if(inventory.getUnit().matches("Pcs")){
                List<String> unitTypes = new ArrayList<String>();
                unitTypes.add("Pcs");

                ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<>(this, R.layout.activity_spinner_item, unitTypes);

                // Drop down layout style - list view with radio button
                dataAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                unit.setAdapter(dataAdapterUnits);

                unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                   @Override
                                                   public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                                       // On selecting a spinner item
                                                       String item = parent.getItemAtPosition(position).toString();
                                                       units = item;

                                                   }

                                                   public void onNothingSelected(AdapterView arg0) {

                                                   }
                                               }
                );

                unit.setEnabled(false);

            } else if(inventory.getUnit().matches("Grms")){

                List<String> unitTypes = new ArrayList<>();
                unitTypes.add("Kgs");

                ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<>(this, R.layout.activity_spinner_item, unitTypes);

                // Drop down layout style - list view with radio button
                dataAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                unit.setAdapter(dataAdapterUnits);

                unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                   @Override
                                                   public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                                       // On selecting a spinner item
                                                       String item = parent.getItemAtPosition(position).toString();
                                                       units = item;

                                                       Log.d(TAG, "settinng cost to decimal number");
                                                       number.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                                                               | InputType.TYPE_CLASS_NUMBER);
                                                   }

                                                   public void onNothingSelected(AdapterView arg0) {

                                                   }
                                               }
                );

                unit.setEnabled(true);

            } else if(inventory.getUnit().matches("mLs")){

                List<String> unitTypes = new ArrayList<>();
                unitTypes.add("Ls");

                ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<>(this, R.layout.activity_spinner_item, unitTypes);

                // Drop down layout style - list view with radio button
                dataAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                unit.setAdapter(dataAdapterUnits);

                unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                   @Override
                                                   public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                                       // On selecting a spinner item
                                                       String item = parent.getItemAtPosition(position).toString();
                                                       units = item;

                                                       Log.d(TAG, "settinng cost to decimal number");
                                                       number.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                                                               | InputType.TYPE_CLASS_NUMBER);
                                                   }

                                                   public void onNothingSelected(AdapterView arg0) {

                                                   }
                                               }
                );

                unit.setEnabled(true);
            }
        }

    }

    public void onNothingSelected(AdapterView arg0) {

    }
    /**
     * end of product spinner methode
     **/

    /**
     * text watcher for getting total
     **/

    private final TextWatcher totalWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if((number.getText()).toString().matches("")
                    || (cost.getText()).toString().matches("")){

            } else if(units.matches("Pcs") || units.matches("Kgs") ||
                    units.matches("Ls") || productName.matches("--Delivery--")){
                String cstArray[] = (cost.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for(int i = 0; i < cstArray.length; i++){
                    cst.append(cstArray[i]);
                }

                double numb;

                if (!(number.getText().toString().contains("."))){
                    numb = Double.parseDouble(number.getText().toString());
                } else {
                    if(number.getText().toString().matches(".")){
                        number.setText("0.");
                        number.setSelection(number.length());
                    }
                    numb = Double.parseDouble(number.getText().toString());

                }

                total.setText("" + numberFormat.format(((Integer.parseInt(cst.toString()) * numb))));

            } else if(units.matches("Grms") || units.matches("mLs")) {
                String cstArray[] = (cost.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for(int i = 0; i < cstArray.length; i++){
                    cst.append(cstArray[i]);
                }
                double numb;

                if (!(number.getText().toString().contains("."))){
                    numb = Double.parseDouble(number.getText().toString());
                } else {
                    if(number.getText().toString().matches(".")){
                        number.setText("0.");
                        number.setSelection(number.length());
                    }
                    numb = Double.parseDouble(number.getText().toString());

                }

                double costTotal = Double.parseDouble(cst.toString());
                total.setText("" + decimalFormat.format(((numb / 1000) * costTotal)));

            }

            if(!((number.getText()).toString().matches(""))
                    && !(productName.matches("--Delivery--"))){
                int num = 0;

                num = num + (db.getInventoryByProduct(productName)).getNumber();

                List<GoodsSales> goodsSale = gsdb.getByForeignKey(sdb.getRowCount() + 1);

                for (GoodsSales pd : goodsSale) {
                    if(productName.matches(pd.getProduct())){
                        num = num - pd.getNumber();
                    }
                }
                double numb;

                if (!(number.getText().toString().contains("."))){
                    numb = Double.parseDouble(number.getText().toString());
                } else {
                    if(number.getText().toString().matches(".")){
                        number.setText("0.");
                        number.setSelection(number.length());
                    }
                    numb = Double.parseDouble(number.getText().toString());

                }

                if(units.matches("Kgs") || units.matches("Ls")){
                    numb = numb * 1000;
                }

                if (numb > (num)) {
                    number.setText("");
                    number.setError("Not enough stock");
                    total.setText("");

                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(number.getText().toString().matches("")
                    || (cost.getText()).toString().matches("")){

            } else if(units.matches("Pcs") || units.matches("Kgs")
                    || units.matches("Ls") || productName.matches("--Delivery--")){
                String cstArray[] = (cost.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for(int i = 0; i < cstArray.length; i++){
                    cst.append(cstArray[i]);
                }
                double numb;

                if (!(number.getText().toString().contains("."))){
                    numb = Double.parseDouble(number.getText().toString());
                } else {
                    if(number.getText().toString().matches(".")){
                        number.setText("0.");
                        number.setSelection(number.length());
                    }
                    numb = Double.parseDouble(number.getText().toString());

                }

                total.setText("" + numberFormat.format(((Integer.parseInt(cst.toString()) * numb))));

            } else if(units.matches("Grms") || units.matches("mLs")) {
                String cstArray[] = (cost.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for(int i = 0; i < cstArray.length; i++){
                    cst.append(cstArray[i]);
                }
                double numb;

                if (!(number.getText().toString().contains("."))){
                    numb = Double.parseDouble(number.getText().toString());
                } else {
                    if(number.getText().toString().matches(".")){
                        number.setText("0.");
                        number.setSelection(number.length());
                    }
                    numb = Double.parseDouble(number.getText().toString());
                }

                double costTotal = Double.parseDouble(cst.toString());

                total.setText("" + decimalFormat.format(((numb / 1000) * costTotal)));

            }

            if(!((number.getText()).toString().matches(""))
                    && !(productName.matches("--Delivery--"))){
                int num = 0;

                num = num + (db.getInventoryByProduct(productName)).getNumber();

                List<GoodsSales> goodsSale = gsdb.getByForeignKey(sdb.getRowCount() + 1);

                for (GoodsSales pd : goodsSale) {
                    if(productName.matches(pd.getProduct())){
                        num = num - pd.getNumber();
                    }
                }

                double numb;

                if (!(number.getText().toString().contains("."))){
                    numb = Double.parseDouble(number.getText().toString());
                } else {
                    if(number.getText().toString().matches(".")){
                        number.setText("0.");
                        number.setSelection(number.length());
                    }
                    numb = Double.parseDouble(number.getText().toString());

                }

                if(units.matches("Kgs") || units.matches("Ls")){
                    numb = numb * 1000;
                }

                if (numb > (num)) {
                    number.setText("");
                    number.setError("Not enough stock");
                    total.setText("");
                }
            }
        }
    };

    /**
     * end of textr watcher
     **/

    /**
     * text watcher for formating cost
     **/

    private final TextWatcher commaSeparator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            cost.removeTextChangedListener(commaSeparator);

            if (!(cost.getText()).toString().matches("")) {
                String cstArray[] = (cost.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                cstTotal = Integer.parseInt(cst.toString());
                cost.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            cost.addTextChangedListener(commaSeparator);
        }

        @Override
        public void afterTextChanged(Editable s) {
            cost.setSelection(cost.getText().length());
        }
    };

    /**
     * end of text watcher
     **/

    /**
     * text watcher for maxIntChecker
     **/

    private final TextWatcher maxIntChecker = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!cost.getText().toString().matches("")) {
                if (cost.getText().length() > 9) {
                    cost.setError("Amount too high");
                    cost.setText("");
                }

            }
            if(!number.getText().toString().matches("")) {
                if (number.getText().length() > 9) {
                    number.setError("Amount too high");
                    if(productName.matches("--Delivery--")) {
                        number.setText("" + 1);
                    } else {
                        number.setText("");
                    }
                }

            }
            if(!total.getText().toString().matches("")) {
                if (total.getText().length() > 9) {
                    total.setError("Total too high");
                    total.requestFocus();
                    total.setText("");
                    cost.setText("");
                    if(productName.matches("--Delivery--")) {
                        number.setText("" + 1);
                    } else {
                        number.setText("");
                    }

                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * end of maxIntChecker text watcher
     **/

}
