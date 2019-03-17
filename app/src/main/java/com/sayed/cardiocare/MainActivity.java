package com.sayed.cardiocare;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sayed.cardiocare.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.ArrayAdapter.*;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Spinner customSpinner;
    HashMap<String,String> speciality;
    ArrayList<String> specialityName;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.display);
        customSpinner = (Spinner) findViewById(R.id.speciality_spinner);

        specialityName = new ArrayList<>();
        speciality = new HashMap<>();

        getAllSpeciality();

        getDoctorsSchedule("E000024");

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                getDoctorsBySpeciality(speciality.get(value));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getAllSpeciality(){
        //getting all the posts
        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/specialities";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Response", response.toString());
                        pDialog.hide();
                        specialityName.add("All");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                speciality.put(response.getJSONObject(i).getString("specialityFullName"),response.getJSONObject(i).getString("id"));
                                specialityName.add(response.getJSONObject(i).getString("specialityFullName"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> customSpinAdap = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, specialityName);
                        customSpinAdap.setDropDownViewResource(R.layout.custom_dropdown);
                        customSpinner.setAdapter(customSpinAdap);
//                        tv.setText(speciality.get("636703695615967302"));

                        ////                            textView.setText(response.getJSONObject("links").getString("first")+"");
//                            textView.setText(response.getJSONArray("data").get(3).toString()+"");

//                            link = gson.fromJson(String.valueOf(response.getJSONObject("links")),Links.class);
//                            meta = gson.fromJson(String.valueOf(response.getJSONObject("meta")),Meta.class);
//                            JSONArray posts = response.getJSONArray("data");
//
//                            for (int i = 0; i < posts.length(); i++) {
//                                Post post = (Post) gson.fromJson(String.valueOf(posts.getJSONObject(i)), Post.class);
//                                postArrayList.add(post);
//                            }
//
//                            pagination();
//
//                            listAdapterWithRecycleView = new ListAdapterWithRecycleView(ViewActivity.this,postArrayList);
//                            linearLayoutManager = new LinearLayoutManager(ViewActivity.this,LinearLayoutManager.VERTICAL,false);
//                            LinearLayoutManager layoutManager=new LinearLayoutManager(ViewActivity.this);
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            recyclerView.setAdapter(listAdapterWithRecycleView);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("response", "Error: " + error.getMessage());
                        // hide the progress dialog
                        pDialog.hide();
                    }
                });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getDoctorsBySpeciality(String id){

        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/doctors/speciality/"+id;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Response", response.toString());
                        pDialog.hide();
//                        specialityName.add("All");
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                tv.append(response.getJSONObject(i).getString("employeeId")+" :"+response.getJSONObject(i).getJSONObject("hrM_EmployeeInfo").getString("employeeFullName")+" ");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        ArrayAdapter<String> customSpinAdap = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, specialityName);
//                        customSpinAdap.setDropDownViewResource(R.layout.custom_dropdown);
//                        customSpinner.setAdapter(customSpinAdap);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void getDoctorsSchedule(String id){

        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/scheduleSlots/doctor/"+id+"/fromDate/2019-03-17/next/3";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Response", response.toString());
                        pDialog.hide();
//                        specialityName.add("All");
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                tv.append(response.getJSONObject(i).getString("date")+" :"+response.getJSONObject(i).getString("slotMppingId")+" ");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        ArrayAdapter<String> customSpinAdap = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, specialityName);
//                        customSpinAdap.setDropDownViewResource(R.layout.custom_dropdown);
//                        customSpinner.setAdapter(customSpinAdap);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}