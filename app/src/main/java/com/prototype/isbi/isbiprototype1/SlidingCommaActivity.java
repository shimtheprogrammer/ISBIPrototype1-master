package com.prototype.isbi.isbiprototype1;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by MRuto on 7/20/2017.
 */

public class SlidingCommaActivity {
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    EditText editText;
    int number;

    public int SlidingCommaActivity(EditText getEditText){
        editText = getEditText;

        editText.addTextChangedListener(slidingCommaWatcher);

        String editTextArray[] = (editText.getText().toString()).split(",");
        StringBuilder editTextBuilder = new StringBuilder();

        for(int i = 0; i < editTextArray.length; i++){
            editTextBuilder.append(editTextArray[i]);
        }

        return Integer.parseInt(editTextBuilder.toString());
    }

    private final TextWatcher slidingCommaWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!editText.getText().toString().matches("")) {
                editText.removeTextChangedListener(slidingCommaWatcher);

                if (!(editText.getText()).toString().matches("")) {
                    String editTexArray[] = (editText.getText().toString()).split(",");
                    StringBuilder cst = new StringBuilder();

                    for (int i = 0; i < editTexArray.length; i++) {
                        cst.append(editTexArray[i]);
                    }

                    editText.setText("" + numberFormat.format(((Integer.parseInt(cst.toString())))));
                }

                String actualArray[] = (editText.getText().toString()).split(",");
                StringBuilder actualBuilder = new StringBuilder();

                for (int i = 0; i < actualArray.length; i++) {
                    actualBuilder.append(actualArray[i]);
                }

                editText.addTextChangedListener(slidingCommaWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!editText.getText().toString().matches("")) {
                editText.setSelection(editText.getText().length());
            }
        }
    };

/**
 * end of textr watcher
 **/

}
