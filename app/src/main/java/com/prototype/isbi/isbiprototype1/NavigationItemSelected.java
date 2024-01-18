package com.prototype.isbi.isbiprototype1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.view.MenuItem;

/**
 * Created by MRuto on 1/12/2017.
 */

public class NavigationItemSelected {

    private Context context;

    public NavigationItemSelected(Context _context){
        this.context = _context;
    }

    public void NavigationItem(MenuItem item){

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        } else if (context == null) {
            System.out.print("context is null");

        } else if (id == R.id.nav_inventory) {
            Intent intent = new Intent(context, InventoryDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Inventory...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_purchases) {
            Intent intent = new Intent(context, PurchasesDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Purchases...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_sales) {
            Intent intent = new Intent(context, SalesDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Sales...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_payables) {
            Intent intent = new Intent(context, PayablesDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Payables...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_delivery) {
            Intent intent = new Intent(context, DeliveryDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Deliveries...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_receivables) {
            Intent intent = new Intent(context, ReceivablesDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Receivables...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_balance_sheet) {
            Intent intent = new Intent(context, BalanceSheetActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Generating Balance Sheet...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_p_and_l) {
            Intent intent = new Intent(context, StatmentsDatesActivity.class);
            intent.putExtra("TO", "PANDL");
            context.startActivity(intent);

        } else if (id == R.id.nav_cash_flow) {
            Intent intent = new Intent(context, StatmentsDatesActivity.class);
            intent.putExtra("TO", "CASHFLOW");
            context.startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(context, OrdersDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Orders...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_expenses) {
            Intent intent = new Intent(context, ExpensesDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Expenses...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_owner) {
            Intent intent = new Intent(context, OwnerDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Owner Drawings...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_asset) {
            Intent intent = new Intent(context, AssetsDisplayActivity.class);
            intent.putExtra("FROM", "NAV");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Assets...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_loans) {
            Intent intent = new Intent(context, LoansDisplayActivity.class);
            intent.putExtra("FROM", "NAV");
            intent.putExtra("FOR", "NOT");

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Loans...");
            progressDialog.show();

            context.startActivity(intent);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedpreferences = context.getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            //editor.commit();
            editor.apply();

            LoginActivity.database = null;

            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

            context.startActivity(intent);

        } else if (id == R.id.nav_dell) {
            Intent intent = new Intent(context, DeleteActivity.class);
            context.startActivity(intent);

        }
    }
}