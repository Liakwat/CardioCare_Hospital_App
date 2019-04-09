package com.sayed.cardiocare.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;
import android.net.NetworkInfo;

import com.sayed.cardiocare.HomeActivity;


public class CheckConnectivity extends BroadcastReceiver
    {

        AlertDialog alertDialog;
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            try
            {
                if (!isOnline(context)) {
                    alertDialog = new AlertDialog.Builder(context)
                            .setTitle("Warning")
                            .setMessage("You have no Internet Connection. Connect to internet and Try again")

//                    To prevent dialog box from getting dismissed on back key pressed use this

                    .setCancelable(false)
//                    And to prevent dialog box from getting dismissed on outside touch use this


                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Close app", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

//                                    context.startActivity(new Intent(context, HomeActivity.class));
                                    ((Activity)context).finish();
//                                    ((Activity)context).finishAffinity();
//                                    System.exit(0);
                                }
                            })

//                    handler.postDelayed(this, 10000);
                            // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    if(alertDialog != null){
                        alertDialog.dismiss();
                    }

//                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
