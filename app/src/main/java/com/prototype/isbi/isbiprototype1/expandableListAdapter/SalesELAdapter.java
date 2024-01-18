package com.prototype.isbi.isbiprototype1.expandableListAdapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.prototype.isbi.isbiprototype1.AddPreSalesActivity;
import com.prototype.isbi.isbiprototype1.AddSalesActivity;
import com.prototype.isbi.isbiprototype1.PreSaleActivity;
import com.prototype.isbi.isbiprototype1.R;
import com.prototype.isbi.isbiprototype1.SaleActivity;

/**
 * Created by MRuto on 1/9/2017.
 */

public class SalesELAdapter extends BaseExpandableListAdapter implements TextWatcher {

    //declare constants
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listChildData;
    private ArrayList<EditText> editTextList = new ArrayList<EditText>();
    private String _where;

    //defince
    public SalesELAdapter(Context contex, List<String> listDataHeader, HashMap<String, List<String>> listChildData,String where) {
        this._context = contex;
        this._listChildData = listChildData;
        this._listDataHeader = listDataHeader;
        this._where = where;
    }

    //get child given position
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChildData.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    //get child ID
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, final int childPosition,
                            boolean isLastChild, View convertView, ViewGroup Parent){

        final String childText = (String) getChild(groupPosition, childPosition);

        if( convertView == null){
            LayoutInflater inflator = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.activity_sales_items, null);
        }

        TextView childItem = (TextView) convertView.findViewById(R.id.lblListItem);
        childItem.setText(childText);

        return convertView;
    }

    // get child count
    public int getChildrenCount(int groupPostion){
        return this._listChildData.get(this._listDataHeader.get(groupPostion)).size();
    }

    //get group
    public Object getGroup(int groupPosition){ return this._listDataHeader.get(groupPosition); }

    //get group number
    public int getGroupCount() { return this._listDataHeader.size(); }

    //get group id
    public long getGroupId(int groupPosition) { return groupPosition; }

    // create the group view
    public View getGroupView(int groupPosition, boolean isExpandable, View convertView, ViewGroup parent){

        String headerTitle = (String) getGroup(groupPosition);

        if( convertView == null){
            LayoutInflater inflator = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.activity_sale_groups, null);
        }

        TextView groupHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        groupHeader.setTypeface(null, Typeface.BOLD);
        groupHeader.setText(headerTitle);

        Button addButton = (Button) convertView.findViewById(R.id.btn_add);
        addButton.setTypeface(null, Typeface.BOLD);
        addButton.setFocusable(false);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_where.matches("pre")) {
                    Intent intent = new Intent(_context, AddPreSalesActivity.class);
                    _context.startActivity(intent);
//                    ((PreSaleActivity) _context).finish();
                } else {
                    Intent intent = new Intent(_context, AddSalesActivity.class);
                    _context.startActivity(intent);
//                    ((SaleActivity) _context).finish();
                }
            }
        });

        return convertView;
    }

    //check id stability
    public boolean hasStableIds() { return false; }

    //check id child is selectable
    public boolean isChildSelectable(int groupPositon, int childPosition) { return false; }

    //do before text change
    public void beforeTextChanged(CharSequence s, int start, int count, int after){    }

    //do while text change
    public void onTextChanged(CharSequence s, int start, int before, int count){    }

    // do after text change
    public void afterTextChanged(Editable s){    }
}
