package com.prototype.isbi.isbiprototype1.expandableListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.isbi.isbiprototype1.AddInventoryActivity;
import com.prototype.isbi.isbiprototype1.AddPayableActivity;
import com.prototype.isbi.isbiprototype1.AddReceivableActivity;
import com.prototype.isbi.isbiprototype1.AssetsActivity;
import com.prototype.isbi.isbiprototype1.ExpensesActivity;
import com.prototype.isbi.isbiprototype1.PreOrderActivity;
import com.prototype.isbi.isbiprototype1.PreSaleActivity;
import com.prototype.isbi.isbiprototype1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 2/13/2017.
 */

public class SixELAdapter extends BaseExpandableListAdapter implements TextWatcher {
    public static String TAG = "SixELAdapter";

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<EditText> editTextList = new ArrayList<EditText>();

    private boolean _button;

    private String _what;

    String name,purpose;

    public SixELAdapter(Context context, List<String> listDataHeader,
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
            convertView = infalInflater.inflate(R.layout.activity_six_items, null);
        }

        String items[] = childText.split("-");

        LinearLayout linearLayout = (LinearLayout) convertView
                .findViewById(R.id.six_layout);

        int holoBlueDarker = _context.getResources().getColor(R.color.holoBlueDarker);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueDarker);
        drawable.setSize(2, 2);
        linearLayout.setDividerDrawable(drawable);

        for(int i = 0; i < 6; i++) {
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
             convertView = infalInflater.inflate(R.layout.activity_six_groups, null);
        }

        String items[] = headerTitle.split("-");

        LinearLayout linearLayout = (LinearLayout) convertView
                .findViewById(R.id.six_layout_group);

        int holoBlueDarker = _context.getResources().getColor(R.color.holoBlueDarker);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(holoBlueDarker);
        drawable.setSize(2, 2);
        linearLayout.setDividerDrawable(drawable);

        for (int i = 1; i < items.length; i++) {
            String txtFld = "GrouplblListItem" + i;
            int txtId = _context.getResources().getIdentifier(txtFld, "id", _context.getPackageName());
            TextView txtListChild = (TextView) convertView
                    .findViewById(txtId);

            Log.d(TAG, "" + items[i] + " " + i);
            txtListChild.setText("" + items[i]);

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

        if (_what.matches("INV2")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent (_context, AddInventoryActivity.class);
                    _context.startActivity(i1);

                }
            });
        }

        if (_what.matches("PAY")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent (_context, AddPayableActivity.class);
                    _context.startActivity(i1);

                }
            });
        }

        if (_what.matches("REC")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent (_context, AddReceivableActivity.class);
                    _context.startActivity(i1);

                }
            });
        }

        if (_what.matches("LOA")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent (_context, ExpensesActivity.class);
                    i1.putExtra("FROM", "REG");
                    _context.startActivity(i1);

                }
            });
        }

        if (_what.matches("PRES")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent (_context, PreSaleActivity.class);
                    i1.putExtra("FROM", "REG");
                    _context.startActivity(i1);

                }
            });
        }

        if (_what.matches("PREP")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent (_context, PreOrderActivity.class);
                    i1.putExtra("FROM", "REG");
                    _context.startActivity(i1);

                }
            });
        }

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
