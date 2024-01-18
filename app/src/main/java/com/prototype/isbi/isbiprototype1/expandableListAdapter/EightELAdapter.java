package com.prototype.isbi.isbiprototype1.expandableListAdapter;

import android.content.Context;
import android.content.Intent;

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

import com.prototype.isbi.isbiprototype1.AssetsActivity;
import com.prototype.isbi.isbiprototype1.LoansActivity;
import com.prototype.isbi.isbiprototype1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 2/15/2017.
 */

public class EightELAdapter extends BaseExpandableListAdapter implements TextWatcher {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<EditText> editTextList = new ArrayList<>();

    private boolean _button;

    private String _what;

    String name,purpose;

    public EightELAdapter(Context context, List<String> listDataHeader,
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
                LayoutInflater infalInflater2 = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater2.inflate(R.layout.activity_eight_items, null);
            }

            String items[] = childText.split("-");

            LinearLayout linearLayout2 = (LinearLayout) convertView
                    .findViewById(R.id.eight_layout);

            int holoBlueDarker = _context.getResources().getColor(R.color.holoBlueDarker);
            linearLayout2.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(holoBlueDarker);
            drawable.setSize(2, 2);
            linearLayout2.setDividerDrawable(drawable);

            for (int i = 0; i < 8; i++) {
                String txtFld2 = "lblListItem" + i;
                int txtId2 = _context.getResources().getIdentifier(txtFld2, "id", _context.getPackageName());
                TextView txtListChild2 = (TextView) convertView
                        .findViewById(txtId2);

                txtListChild2.setText(items[i]);

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
            convertView = infalInflater.inflate(R.layout.activity_eight_groups, null);
        }

        String items[] = headerTitle.split("-");

        LinearLayout linearLayout = (LinearLayout) convertView
                .findViewById(R.id.eight_layout_group);

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
        } else {
            Add.setVisibility(View.VISIBLE);
        }

        if (_what.matches("ASS")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (_context, AssetsActivity.class);
                    intent.putExtra("FROM", "REG");
                    _context.startActivity(intent);

                }
            });
        }

        if (_what.matches("LOA")){
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (_context, LoansActivity.class);
                    intent.putExtra("FROM", "REG");
                    _context.startActivity(intent);

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

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        name=editTextList.get(0).getText().toString();
        purpose=editTextList.get(1).getText().toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

        System.out.println("Name is"+name);
        System.out.println("purpose is"+purpose);

    }

}
