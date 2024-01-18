package com.prototype.isbi.isbiprototype1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MRuto on 12/16/2016.
 */

public class AddInventoryActivity extends Activity{
    public static String TAG = "AddInventoryActivity";

    Button btnExit, btnAdd;
    EditText product, number, cost, sprice;
    Spinner unit;

    String units;
    int cstTotal, spriceTotal, quantityToatal;

    InventoryHandler idb = new InventoryHandler(this);

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;

        getWindow().setLayout((400), (380));

        //lining parameters to values on xml
        product = (EditText) findViewById(R.id.product);
        number = (EditText) findViewById(R.id.number);
        cost = (EditText) findViewById(R.id.cost);
        sprice = (EditText) findViewById(R.id.sprice);

        btnExit = (Button) findViewById(R.id.btn_exit);
        btnAdd = (Button) findViewById(R.id.btn_add);

        cost.addTextChangedListener(commaSeparator);
        sprice.addTextChangedListener(commaSeparator);
        number.addTextChangedListener(commaSeparator);
        /*
        Spinner units
         */
        unit = (Spinner) findViewById(R.id.unit);

        final List<String> unitTypes = new ArrayList<String>();
        unitTypes.add("UNITS");
        unitTypes.add("Pcs");
        unitTypes.add("Kgs");
        unitTypes.add("Ls");

        ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<String>(this, R.layout.activity_spinner_item, unitTypes);

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
                                                      if (!(number.getText().toString().matches(""))){
                                                          int numberstring = quantityToatal;
                                                          number.setText("" + numberstring);
                                                          number.setSelection(number.getText().length());

                                                      }

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
        /*
        End of Spinner units
         */
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

                if((product.getText()).toString().matches("")){
                    Log.d(TAG, "btnAdd; no product set..");

                    product.setError("Please Enter Product Name");
                    product.requestFocus();

                } else if ((number.getText()).toString().matches("")){
                    Log.d(TAG, "btnAdd; no quantity set..");

                    number.setError("Please Enter Quantity");
                    number.requestFocus();

                } else if((cost.getText()).toString().matches("")){
                    Log.d(TAG, "btnAdd; cost null..");

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

                } else {
                    StringBuilder confMessaage = new StringBuilder();

                    confMessaage.append("Add " + product.getText().toString() + " with Quantity of "
                            + (number.getText().toString() + units) + " for "
                            + cost.getText().toString() + " per " + units);

                    if (!(sprice.getText().toString().matches(""))){
                        confMessaage.append(". Selling Price " + spriceTotal);
                    }

                    new AlertDialog.Builder(AddInventoryActivity.this)
                            .setIcon(R.drawable.invenotry_icon_5)
                            .setTitle("Confirm Product")
                            .setMessage("" + confMessaage.toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Inserting row
                                    int numb = quantityToatal;
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
                                    idb.addInventory(new Inventory(product.getText().toString(),
                                            numb, units, cst, (int) Math.round(numb * cst),
                                            spriceTotal));

                                    finish();

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
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
//                Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }

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

            sprice.removeTextChangedListener(commaSeparator);

            if (!(sprice.getText()).toString().matches("")) {
                String cstArray[] = (sprice.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }
                spriceTotal = Integer.parseInt(cst.toString());
                sprice.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
            }
            sprice.addTextChangedListener(commaSeparator);

            number.removeTextChangedListener(commaSeparator);

            if (!(number.getText()).toString().matches("")) {
                String cstArray[] = (number.getText().toString()).split(",");
                StringBuilder cst = new StringBuilder();

                for (int i = 0; i < cstArray.length; i++) {
                    cst.append(cstArray[i]);
                }

                if (!(cst.toString().matches(""))) {
                    quantityToatal = Integer.parseInt(cst.toString());
                    number.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
                }

            }
            number.addTextChangedListener(commaSeparator);
        }

        @Override
        public void afterTextChanged(Editable s) {
            cost.setSelection(cost.getText().length());
            sprice.setSelection(sprice.getText().length());
            number.setSelection(number.getText().length());
        }
    };

    /**
     * end of text watcher
     **/
}
