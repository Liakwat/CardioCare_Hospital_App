package com.sayed.cardiocare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.sayed.cardiocare.Adapter.PatientLabAdapter;
import com.sayed.cardiocare.Adapter.TimeSlotAdapter;
import com.sayed.cardiocare.Models.LabReportModel;
import com.sayed.cardiocare.Models.LoggedpatientDetail;
import com.sayed.cardiocare.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientDetailActivity extends AppCompatActivity {


    TextView pName,pId,pPhone,tv;
    Gson gson;
    ArrayList<LabReportModel> labLists;
    RecyclerView recyclerView;
    PatientLabAdapter patientLabAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        gson = new Gson();
        labLists = new ArrayList<>();

        pName = findViewById(R.id.pname_et);
        pId = findViewById(R.id.pid_et);
        pPhone = findViewById(R.id.phone_et);
        tv = findViewById(R.id.tv);
        recyclerView = findViewById(R.id.recycleListView);

        Intent i = getIntent();
        LoggedpatientDetail patient = (LoggedpatientDetail)i.getSerializableExtra("patientObj");

        pName.setText("Name: "+patient.getUserName());
        pId.setText("Id: "+patient.getUserId());
        pPhone.setText("Phone: "+patient.getPhoneNumber());

        requestForLabs(patient.getToken(),patient);
    }

    public void appointmentClicker(View v){
        Intent i = new Intent(PatientDetailActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void requestForLabs(final String accesstoken, final LoggedpatientDetail patientObj){
        // Post request
        String tag_json_obj = "json_obj_req";
        String url ="http://203.190.9.108/api.paitent.ecure24.com/api/Labs";

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i=0; i<response.length(); i++){
                            try {
                                LabReportModel labReportModel = (LabReportModel) gson.fromJson(response.getJSONObject(i).toString(),LabReportModel.class);
                                labLists.add(labReportModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        patientLabAdapter = new PatientLabAdapter(PatientDetailActivity.this,labLists,patientObj);
                        linearLayoutManager = new LinearLayoutManager(PatientDetailActivity.this,LinearLayoutManager.HORIZONTAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(patientLabAdapter);
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
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }
}
