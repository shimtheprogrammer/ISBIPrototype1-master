package com.prototype.isbi.isbiprototype1.fragments;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.R;

import com.prototype.isbi.isbiprototype1.LoanOptionActivity;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Loans;
import com.prototype.isbi.isbiprototype1.databaseHandlers.LoansHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.EightELAdapter;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 3/9/2017.
 */

public class PaidLoansDisplayFragment extends Fragment {
    public static String TAG = "PaidLoansDisplFragment";

    EightELAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String, String> idList = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get the listview
        final View view = inflater.inflate(R.layout.fragment_pre_purchase_display, container, false);
        final ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData(getActivity());

        listAdapter = new EightELAdapter(getActivity(), listDataHeader,
                listDataChild, "LOA", false);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                for (String Key : idList.keySet()) {
                    if (Key.matches("" + childPosition)) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoanOptionActivity.class);
                        intent.putExtra("INFO", listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition));
                        intent.putExtra("ID", idList.get(Key));
                        intent.putExtra("FOR", "PAY");
                        intent.putExtra("VIEW", "DIS");
                        startActivity(intent);
                    }
                }

                return false;
            }
        });

        expListView.expandGroup(0);

        return view;
    }

    private void prepareListData(Activity activity) {
        LoansHandler ldb = new LoansHandler(activity.getApplicationContext());

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> loans = new ArrayList<>();

        List<Loans> loansList = ldb.getAllLoans();

        String title = "ID-Creditor-Total-Date-Last Ins.-Instalment-No.-Savings";
        //loans.add(title);
        int count = 0;
        for (Loans pd : loansList) {
            if(pd.getStatus().matches("PAY")) {
                int loansTotal = pd.getTotal();

                String name[] = pd.getName().split("-");
                String show = name[0];

                String savings = "No";
                if (pd.getContractualSavings() > 0){
                    savings = "Yes";
                }

                String log = pd.getID() + "-" + show
                        + "-" + numberFormat.format(loansTotal)
                        + "-" + pd.getStartDate() + "-" + pd.getEndDate()
                        + "-" + numberFormat.format(pd.getInstalments()) + "-" + pd.getInstalmentsNo()
                        + "-" + savings;
                Log.d(TAG,"Insert: " + log);
                loans.add(log);
                idList.put("" + count, "" + pd.getID());
                count++;
            }
        }

        String head = "Paid Loans: ";

        listDataHeader.add(head + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), loans); // Header, Child data
    }
}
