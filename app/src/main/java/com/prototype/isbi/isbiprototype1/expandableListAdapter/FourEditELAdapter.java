package com.prototype.isbi.isbiprototype1.expandableListAdapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.MainActivity;
import com.prototype.isbi.isbiprototype1.R;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Inventory;
import com.prototype.isbi.isbiprototype1.databaseHandlers.InventoryHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 2/16/2017.
 */

public class FourEditELAdapter extends BaseExpandableListAdapter implements TextWatcher {
    public static String TAG = "FourEditELAdapter";

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<EditText> editTextList = new ArrayList<>();
    private ArrayList<TextView> textViewList = new ArrayList<>();
    private ArrayList<String> stringList =  new ArrayList<>();
    private String[] strings;

    private boolean _button;

    private String _what;

    String name,purpose;

    public FourEditELAdapter(Context context, List<String> listDataHeader,
                         HashMap<String, List<String>> listChildData, String what, boolean button) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._what = what;
        this._button = button;
    }



    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_four_edit_items, null);

        }

        String items[] = childText.split("-");

        LinearLayout linearLayout = (LinearLayout) convertView
                .findViewById(R.id.four_layout);

        int holoBlueDarker = _context.getResources().getColor(R.color.holoBlueDarker);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueDarker);
        drawable.setSize(2, 2);
        linearLayout.setDividerDrawable(drawable);

        for(int i = 0; i < 3; i++) {
            String txtFld = "lblListItems" + i;
            int txtId = _context.getResources().getIdentifier(txtFld, "id", _context.getPackageName());
            TextView txtListChild = (TextView) convertView.findViewById(txtId);
            txtListChild.setText(items[i]);

            if (i == 0){
                textViewList.add(txtListChild);
            }
        }

        EditText editText = (EditText) convertView.findViewById(R.id.lblListItems3);
        TextInputLayout textInputLayout = (TextInputLayout) convertView.findViewById(R.id.WidlblListItems3);

        textInputLayout.setHint("Sale Price " + items[3]);
        editText.addTextChangedListener(this);
        editText.setFocusable(true);

        if (stringList.size() <= 1 || stringList == null){
            stringList.add("");
        }

        editTextList.add(editText);

        for (int i = 0; i < editTextList.size(); i++){
            editTextList.get(i).setText("" + stringList.get(i));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_four_groups, null);
        }

        String items[] = headerTitle.split("-");

        LinearLayout linearLayout = (LinearLayout) convertView
                .findViewById(R.id.four_layout_group);

        int holoBlueDarker = _context.getResources().getColor(R.color.holoBlueDarker);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueDarker);
        drawable.setSize(2, 2);
        linearLayout.setDividerDrawable(drawable);

        for (int i = 1; i < 5; i++) {
            String txtFld = "GrouplblListItem" + i;
            int txtId = _context.getResources().getIdentifier(txtFld, "id", _context.getPackageName());
            TextView txtListChild = (TextView) convertView
                    .findViewById(txtId);

            txtListChild.setText(items[i]);

            txtListChild.setTypeface(null, Typeface.BOLD_ITALIC);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(items[0]);

        ImageButton Add = (ImageButton) convertView.findViewById(R.id.btn_add);

        Add.setFocusable(false);

        if(!_button){
            Add.setVisibility(View.INVISIBLE);
        }

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_what.matches("SLP")) {
                    boolean flag = true;

                    for (int i = 0; i < editTextList.size(); i++){
                        Log.d(TAG, "Final Edit " + editTextList.get(i).getText().toString());
                        Log.d(TAG, "Final Text " + textViewList.get(i).getText().toString());
                        Log.d(TAG, "Final string list " + stringList.get(i));
                        Log.d(TAG, "Final string array " + strings[i]);

                        editTextList.get(i).setText("" + strings[i]);

                        if (editTextList.get(i).getText().toString().matches("")){
                            editTextList.get(i).setError("Please Enter a Sale Price");
                            editTextList.get(i).requestFocus();
                            flag = false;

                        }
                    }

                    if (flag) {
                        InventoryHandler idb = new InventoryHandler(_context);

                        for (int i = 0; i < editTextList.size(); i++){
                            Inventory inventory = idb.getInventoryByProduct(textViewList.get(i).getText().toString());

                            inventory.setSPrice(Integer.parseInt(strings[i]));
                            idb.updateInventory(inventory);

                        }

                        Intent i1 = new Intent(_context, MainActivity.class);
                        i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        _context.startActivity(i1);

                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        name=editTextList.get(0).getText().toString();
//        purpose=editTextList.get(1).getText().toString();

        strings = new String[editTextList.size()];

        for (int i = 0; i < editTextList.size(); i++){
            strings[i] = editTextList.get(i).getText().toString();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        for ( int i = 0; i < editTextList.size(); i++){
            //Log.d(TAG, "" + editText.getText().toString());
            stringList.add(editTextList.get(i).getText().toString());
            stringList.set(i, editTextList.get(i).getText().toString());
        }
    }
}