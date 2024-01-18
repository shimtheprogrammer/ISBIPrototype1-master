package com.prototype.isbi.isbiprototype1.expandableListAdapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.AddInventoryActivity;
import com.prototype.isbi.isbiprototype1.InventoryActivity;
import com.prototype.isbi.isbiprototype1.R;
import com.prototype.isbi.isbiprototype1.SaleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 12/16/2016.
 */

public class InventoryELAdapter extends BaseExpandableListAdapter implements TextWatcher{

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<EditText> editTextList = new ArrayList<EditText>();

    String name,purpose;

    public InventoryELAdapter(Context context, List<String> listDataHeader,
                           HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
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
            convertView = infalInflater.inflate(R.layout.activity_inventory_items, null);
        }

        String items[] = childText.split("-");

        for(int i = 0; i < items.length; i++) {
            String txtFld = "lblListItem" + i;
            int txtId = _context.getResources().getIdentifier(txtFld, "id", _context.getPackageName());
            TextView txtListChild = (TextView) convertView
                    .findViewById(txtId);

            txtListChild.setText(items[i]);
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
            convertView = infalInflater.inflate(R.layout.activity_inventory_groups, null);
        }


        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        Button Add = (Button) convertView.findViewById(R.id.btnAdd);
        Add.setTypeface(null, Typeface.BOLD);
        Add.setFocusable(false);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i1 = new Intent (_context, AddInventoryActivity.class);
                _context.startActivity(i1);
                //((InventoryActivity) _context).finish();
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        name=editTextList.get(0).getText().toString();
        purpose=editTextList.get(1).getText().toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub


        System.out.println("Name is"+name);
        System.out.println("purpose is"+purpose);

        //Toast.makeText(_context, "Name"+name, Toast.LENGTH_LONG).show();
        //Toast.makeText(_context, "Purpose"+purpose, Toast.LENGTH_LONG).show();

    }

}
