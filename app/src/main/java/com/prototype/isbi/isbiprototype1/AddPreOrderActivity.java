package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrdered;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreOrderedHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.GoodsPreSoldHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PreOrderHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MRuto on 1/25/2017.
 */

public class AddPreOrderActivity extends Activity {
    public static String TAG = "AddPreOrderActivity";

    Button btnExit, btnAdd;
    EditText number, cost, total;
    AutoCompleteTextView product;
    Spinner unit;

    private Calendar calendar;
    private int year, month, day;
    private String units;

    int cstTotal;

    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    GoodsPreOrderedHandler gpodb = new GoodsPreOrderedHandler(this);
    PreOrderHandler podb = new PreOrderHandler(this);
    InventoryHandler idb = new InventoryHandler(this);
    GoodsPreSoldHandler gpsdb = new GoodsPreSoldHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pre_order);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //getWindow().setLayout((343), (291));
        getWindow().setLayout((400), (380));

        //lining parameters to values on xml
        product = (AutoCompleteTextView) findViewById(R.id.product);
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


        /**
         * populate auto complete product name
         **/

        /*
        Spinner units
         */
        unit = (Spinner) findViewById(R.id.unit);

        final List<String> unitTypes = new ArrayList<String>();
        unitTypes.add("UNITS");
        unitTypes.add("Pcs");
        unitTypes.add("Kgs");
        unitTypes.add("Ls");

        setUnitSpinner(unitTypes);
        /*
        End of Spinner units
         */

        List<String> goodsNames = new ArrayList<>();

        final List<Inventory> inventories = idb.getAllInventory();
        for (Inventory pd : inventories) {
            String log = pd.getProduct();
            // Writing Contacts to log
            Log.d("Name: ", log);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < goodsNames.size(); i++){
                    if((goodsNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    goodsNames.add(log);
                }
                flag = true;
            }
        }

        List<GoodsPreOrdered> goodsPreOrdereds = gpodb.getAllGoodsPreOrders();
        for (GoodsPreOrdered pd : goodsPreOrdereds) {
            String log = pd.getProduct();
            // Writing Contacts to log
            Log.d("Name: ", log);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < goodsNames.size(); i++){
                    if((goodsNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    goodsNames.add(log);
                }
            }
        }

        List<GoodsPreOrdered> goodsPreSold = gpsdb.getAllGoodsPreSold();
        for (GoodsPreOrdered pd : goodsPreSold) {
            String log = pd.getProduct();
            // Writing Contacts to log
            Log.d("Name: ", log);
            if(log.matches("")){

            } else {
                boolean flag = true;
                for(int i = 0; i < goodsNames.size(); i++){
                    if((goodsNames.get(i).matches(log))){
                        flag = false;
                    }
                }
                if(flag){
                    goodsNames.add(log);
                }
            }
        }

        String[] goods = goodsNames.toArray(new String[goodsNames.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goods);

        //Find TextView control

        //Set the number of characters the user must type before the drop down list is shown
        product.setThreshold(1);
        //Set the adapter
        product.setAdapter(adapter);

        product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                Log.d(TAG, "onItemSelected; checking for " + item);

                //Inventory pd = idb.getInventoryByProduct(item);

                List<Inventory> inventoryList = idb.getAllInventory();

                for (Inventory pd : inventoryList) {
                    if (pd.getProduct().matches("" + item)) {
                        double cst = (pd.getCost());

                        if (pd != null) {
                            if (pd.getUnit().matches("Pcs")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Pcs");

                                setUnitSpinner(unitTypes);

                            } else if (pd.getUnit().matches("Grms")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Kgs");

                                cst = cst * 1000;

                                setUnitSpinner(unitTypes);

                            } else if (pd.getUnit().matches("mLs")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Ls");

                                cst = cst * 1000;

                                setUnitSpinner(unitTypes);

                            }

                            cost.setText("" + numberFormat.format(Math.round(cst)));
                            product.addTextChangedListener(textWatcher);
                        }
                    }
                }

                List<GoodsPreOrdered> goodsPreOrdereds = gpodb.getAllGoodsPreOrders();

                for (GoodsPreOrdered pd : goodsPreOrdereds) {
                    if (pd.getProduct().matches("" + item)) {
                        double cst = (pd.getCost());

                        if (pd != null) {
                            if (pd.getUnit().matches("Pcs")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Pcs");

                                setUnitSpinner(unitTypes);

                            } else if (pd.getUnit().matches("Grms")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Kgs");

                                cst = cst * 1000;

                                setUnitSpinner(unitTypes);

                            } else if (pd.getUnit().matches("mLs")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Ls");

                                cst = cst * 1000;

                                setUnitSpinner(unitTypes);

                            }

                            cost.setText("" + numberFormat.format(Math.round(cst)));
                            product.addTextChangedListener(textWatcher);
                        }
                    }
                }

                List<GoodsPreOrdered> goodsPreSold = gpsdb.getAllGoodsPreSold();

                for (GoodsPreOrdered pd : goodsPreSold) {
                    if (pd.getProduct().matches("" + item)) {
                        double cst = (pd.getCost());

                        if (pd != null) {
                            if (pd.getUnit().matches("Pcs")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Pcs");

                                setUnitSpinner(unitTypes);

                            } else if (pd.getUnit().matches("Grms")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Kgs");

                                cst = cst * 1000;

                                setUnitSpinner(unitTypes);

                            } else if (pd.getUnit().matches("mLs")) {
                                Log.d(TAG, "onItemSelected; found unit " + pd.getUnit());
                                List<String> unitTypes = new ArrayList<>();
                                unitTypes.add("Ls");

                                cst = cst * 1000;

                                setUnitSpinner(unitTypes);

                            }

                            cost.setText("" + numberFormat.format(Math.round(cst)));
                            product.addTextChangedListener(textWatcher);
                        }
                    }
                }
            }
        });

        /**
         * end of auto complete product name
         **/

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

                for(int i = 0; i < cstArray.length; i++){
                    cst1.append(cstArray[i]);
                }

                if (product.getText().toString().matches("")) {
                    Log.d(TAG , "btnAdd; product was null..");

                    product.setError("Please Enter Product Name");
                    product.requestFocus();

                } else if(number.getText().toString().matches("")){
                    Log.d(TAG , "btnAdd; number was null..");

                    number.setError("Please Enter Quantity");
                    number.requestFocus();

                } else if(cost.getText().toString().matches("")){
                    Log.d(TAG , "btnAdd; cost was null..");

                    cost.setError("Please Enter Price Per Pcs/Kg/L");
                    cost.requestFocus();

                } else if(units.matches("UNITS")){
                    Log.d(TAG, "btnAdd; no units selected..");

                    TextView errorText = (TextView)unit.getSelectedView();
                    //errorText.setError("Select Unit");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("UNITS");//changes the selected item text to this
                    unit.requestFocus();
                    unit.performClick();

                } else if(((Double.parseDouble(cst1.toString())) * (Double.parseDouble(number.getText().toString()))) <= 0){
                    Log.d(TAG, "btnAdd; total null..");

                    total.setError("Total Cannot be 0");
                    total.requestFocus();

                } else {
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

                    gpodb.addGoodsPreOrdered(new GoodsPreOrdered(product.getText().toString(),
                            (int) Math.round(numb), units, cst, (int) Math.round(numb * cst),
                            date , podb.getRowCount() + 1));

                    //List<GoodsPreOrdered> sales = gpsdb.getByForeignKey(podb.getRowCount() + 1);
                    List<GoodsPreOrdered> prePurchased = gpodb.getByForeignKey(podb.getRowCount() + 1);

                    Intent intent = new Intent(getApplicationContext(), PreOrderActivity.class);
                    intent.putExtra("FROM", "ADD");

                    if(prePurchased.size() == 1 && !(getIntent().getStringExtra("FROM").matches("REG"))){
                        startActivity(intent);
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
     * methodes for unit spinner
     **/

    public void setUnitSpinner(List<String> list) {
        ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<>(this, R.layout.activity_spinner_item, list);

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

                                               if (units.matches("Pcs")) {
                                                   Log.d(TAG, "setting cost to real number");
                                                   number.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                   int numberstring;

                                                   if (!(number.getText().toString().matches(""))){
                                                       numberstring = (int) Math.round(Double.parseDouble(number.getText().toString()));
                                                       number.setText("" + numberstring);
                                                       number.setSelection(number.getText().length());

                                                   }

                                               } else {
                                                   Log.d(TAG, "setting cost to decimal number");
                                                   number.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                                                           | InputType.TYPE_CLASS_NUMBER);
                                                   //number.setText("");

                                               }

                                               cost.setText(cost.getText().toString());
                                               cost.setSelection(cost.getText().length());

                                           }

                                           public void onNothingSelected(AdapterView arg0) {

                                           }
                                       }
        );
    }
    /**
     * end of product unit methode
     **/

    /**
     * text watcher for getting total
     **/

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<String> unitTypes1 = new ArrayList<>();
            unitTypes1.add("UNITS");
            unitTypes1.add("Pcs");
            unitTypes1.add("Kgs");
            unitTypes1.add("Ls");

            setUnitSpinner(unitTypes1);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * end of textr watcher
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

            } else{
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

                total.setText("" + numberFormat.format((Double.parseDouble(cst.toString())) * numb));

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if((number.getText()).toString().matches("") || (number.getText()).toString().matches(".")
                    || (cost.getText()).toString().matches("")){

            } else{
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

                total.setText("" + numberFormat.format((Double.parseDouble(cst.toString())) * numb));

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
                cost.setText("" + numberFormat.format(((Double.parseDouble(cst.toString())))));
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
                    number.setText("");
                }

            }
            if(!total.getText().toString().matches("")) {
                if (total.getText().length() > 9) {
                    total.setError("Total too high");
                    total.requestFocus();
                    total.setText("");
                    number.setText("");
                    cost.setText("");
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
