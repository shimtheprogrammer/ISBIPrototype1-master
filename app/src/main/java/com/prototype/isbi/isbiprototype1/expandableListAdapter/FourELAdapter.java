package com.prototype.isbi.isbiprototype1.expandableListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototype.isbi.isbiprototype1.AddPreOrderActivity;
import com.prototype.isbi.isbiprototype1.AddPreSalesActivity;
import com.prototype.isbi.isbiprototype1.AddPurchaseActivity;
import com.prototype.isbi.isbiprototype1.AddSalesActivity;
import com.prototype.isbi.isbiprototype1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 2/16/2017.
 */

public class FourELAdapter extends BaseExpandableListAdapter implements TextWatcher {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<EditText> editTextList = new ArrayList<EditText>();

    private boolean _button;

    private String _what;

    String name,purpose;

    public FourELAdapter(Context context, List<String> listDataHeader,
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
            convertView = infalInflater.inflate(R.layout.activity_four_items, null);
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

        for(int i = 0; i < 4; i++) {
            String txtFld = "lblListItems" + i;
            int txtId = _context.getResources().getIdentifier(txtFld, "id", _context.getPackageName());
            TextView txtListChild = (TextView) convertView.findViewById(txtId);
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

        String title = "Name-Number-Price-Total";

        if(headerTitle.matches("Must Add Product. Not Just Dellivery" + "-" + title)
                || headerTitle.matches("Must Add Product." + "-" + title)
                || headerTitle.matches("The Total Cannot be 0" + "-" + title)){
            lblListHeader.setTextColor(Color.RED);
        }

        ImageButton Add = (ImageButton) convertView.findViewById(R.id.btn_add);

        Add.setFocusable(false);

        if(!_button){
            Add.setVisibility(View.INVISIBLE);
        }

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_what.matches("SAL")) {
                    Intent i1 = new Intent(_context, AddSalesActivity.class);
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    _context.startActivity(i1);
                } else if(_what.matches("PUR")) {
                    Intent i1 = new Intent(_context, AddPurchaseActivity.class);
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    _context.startActivity(i1);
                } else if(_what.matches("PRES")) {
                    Intent i1 = new Intent(_context, AddPreSalesActivity.class);
                    i1.putExtra("FROM", "MAIN");
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    _context.startActivity(i1);
                } else if(_what.matches("PREO")) {
                    Intent i1 = new Intent(_context, AddPreOrderActivity.class);
                    i1.putExtra("FROM", "MAIN");
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    _context.startActivity(i1);
                } else if(_what.matches("PRESR")) {
                    Intent i1 = new Intent(_context, AddPreSalesActivity.class);
                    i1.putExtra("FROM", "REG");
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    _context.startActivity(i1);
                } else if(_what.matches("PREOR")) {
                    Intent i1 = new Intent(_context, AddPreOrderActivity.class);
                    i1.putExtra("FROM", "REG");
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    _context.startActivity(i1);
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

