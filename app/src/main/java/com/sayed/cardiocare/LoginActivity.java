package com.sayed.cardiocare;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.net.ConnectivityManager;
        import android.os.Build;
        import android.os.Handler;
        import android.support.design.widget.Snackbar;
        import android.support.v4.content.LocalBroadcastManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.CardView;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.NetworkResponse;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.ServerError;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.HttpHeaderParser;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.sayed.cardiocare.Models.LoggedpatientDetail;
        import com.sayed.cardiocare.app.AppController;
        import com.sayed.cardiocare.utils.CheckConnectivity;
        import com.sayed.cardiocare.utils.ConnectionChecker;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.util.HashMap;
        import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView tv;

    EditText mobileEt, idEtReg;
    EditText passEt, idEt;
    String granType = "password";
    CardView logCv,regCv;
    private CheckConnectivity checkConnectivity;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv = findViewById(R.id.tv);
        idEt = findViewById(R.id.id_et_log);
        passEt = findViewById(R.id.pass_et_log);
        mobileEt = findViewById(R.id.mobile_num_et);
        idEtReg = findViewById(R.id.id_et);
        logCv = findViewById(R.id.login_layout);
        regCv = findViewById(R.id.register_layout);

        checkConnectivity = new CheckConnectivity();
        registerNetworkBroadcastForNougat();

        if(getIntent().getStringExtra("home_msg")!=null){
            Snackbar.make(findViewById(android.R.id.content), getIntent().getStringExtra("home_msg"), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(checkConnectivity, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(checkConnectivity, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(checkConnectivity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void loginClick(View v){
        requestForLogIn();
    }

    public void registrationClick(View v){
        requestForRegister();
    }

    public void trigger(View v){
        if(v.getId() == R.id.to_register){
            regCv.setVisibility(View.VISIBLE);

            logCv.animate().translationXBy(-1500).setDuration(300);

            regCv.setTranslationX(1500);
            regCv.animate().translationXBy(-1500).setDuration(300);

            logCv.setVisibility(View.GONE);

        }else if(v.getId() == R.id.to_login){

            logCv.setVisibility(View.VISIBLE);

            regCv.animate().translationXBy(1500).setDuration(300);

            logCv.setTranslationX(-1500);
            logCv.animate().translationXBy(1500).setDuration(300);

            regCv.setVisibility(View.GONE);
        }
    }

    public void requestForLogIn(){
        // Post request
        String tag_json_obj = "json_obj_req";
        String url ="http://203.190.9.108/api.auth.ecure24.com/api/token";
/*
{
    "appointment_Schedule_Day_Slot_MappingId": "636711476987797174",
    "doctorId": "E000024",
    "visitedDate": "2019-03-19",
    "timeSlot": "18:00:00",
    "patientInfo": {
        "sex": "male",
        "patientName": "test by siddik",
        "age2": 35,
        "addressInfo": {
            "mobile": "01847140114"
        }
    },
    "consultationMainServiceCharges": "636711476987797168"
}
*/

        JSONObject patient = new JSONObject();

        try {
            patient.put("userId",idEt.getText().toString().trim());
            patient.put("password",passEt.getText().toString().trim());
            patient.put("grantType",granType);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, patient,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getJSONObject("tokens").getString("accessToken") != null){

                                String token = response.getJSONObject("tokens").getString("accessToken");
                                String userId = response.getJSONObject("user").getString("id");
                                String phoneNumber = response.getJSONObject("user").getString("phoneNumber");
                                String userName = response.getJSONObject("user").getString("userName");

                                LoggedpatientDetail loggedpatientDetail = new LoggedpatientDetail(token,userId,phoneNumber,userName);

                                Intent i = new Intent(LoginActivity.this, PatientDetailActivity.class);
                                i.putExtra("patientObj",loggedpatientDetail);
                                startActivity(i);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        idEt.setError(obj.getJSONArray("messages").get(0)+"");
                        passEt.setError(obj.getJSONArray("messages").get(0)+"");

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            }

        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void requestForRegister(){
        // Post request
        String tag_json_obj = "json_obj_req";
        String url ="http://203.190.9.108/api.auth.ecure24.com/api/PatientUserRegistrations";
/*
{
    "appointment_Schedule_Day_Slot_MappingId": "636711476987797174",
    "doctorId": "E000024",
    "visitedDate": "2019-03-19",
    "timeSlot": "18:00:00",
    "patientInfo": {
        "sex": "male",
        "patientName": "test by siddik",
        "age2": 35,
        "addressInfo": {
            "mobile": "01847140114"
        }
    },
    "consultationMainServiceCharges": "636711476987797168"
}
*/

        JSONObject patient = new JSONObject();

        try {
            patient.put("mobileNo",mobileEt.getText().toString().trim());
            patient.put("id",idEt.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, patient,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

//                        Log.i("Response",response.toString());
//                        tv.setText(response.toString());
                        dialogMessage("Registration Successful.\n SMS has been sent to your mobile number with login information.");

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
//                        tv.setText(res);

                        String msg = "";
                        for (int i=0;i<obj.getJSONArray("messages").length();i++ ){
                            msg = msg+obj.getJSONArray("messages").getString(i)+"\n";
                        }

                        dialogMessage(msg);

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            }

        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void dialogMessage(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);

        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

//                            builder1.setNegativeButton(
//                                    "No",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
