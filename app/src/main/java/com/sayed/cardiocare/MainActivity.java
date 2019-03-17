package com.sayed.cardiocare;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sayed.cardiocare.Adapter.ListAdapterWithRecycleView;
import com.sayed.cardiocare.Models.Doctors;
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
    ArrayList<Doctors> doctorsList;
    ListAdapterWithRecycleView listAdapterWithRecycleView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tv = findViewById(R.id.display);
        customSpinner = (Spinner) findViewById(R.id.speciality_spinner);
        recyclerView =(RecyclerView) findViewById(R.id.recycleListView);

        specialityName = new ArrayList<>();
        speciality = new HashMap<>();
        doctorsList = new ArrayList<>();

        getAllSpeciality();

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                clear();
                if(value.equals("All")){
                    getAllDoctor();
                }else {
                    getDoctorsBySpeciality(speciality.get(value));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void clear() {
        final int size = doctorsList.size();
        doctorsList.clear();
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

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                Doctors doctor = new Doctors(
                                        response.getJSONObject(i).getString("employeeId"),
                                        response.getJSONObject(i).getJSONObject("hrM_EmployeeInfo").getString("employeeFullName"),
                                        response.getJSONObject(i).getString("shortBio"),
                                        response.getJSONObject(i).getString("specialityId"),
                                        response.getJSONObject(i).getJSONObject("doctorSpeciality").getString("specialityFullName")
                                );
                                doctorsList.add(doctor);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        listAdapterWithRecycleView = new ListAdapterWithRecycleView(MainActivity.this,doctorsList);
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(listAdapterWithRecycleView);



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

    public void getAllDoctor(){
        //getting all the posts
        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/doctors";

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

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                Doctors doctor = new Doctors(
                                        response.getJSONObject(i).getString("employeeId"),
                                        response.getJSONObject(i).getJSONObject("hrM_EmployeeInfo").getString("employeeFullName"),
                                        response.getJSONObject(i).getString("shortBio"),
                                        response.getJSONObject(i).getString("specialityId"),
                                        response.getJSONObject(i).getJSONObject("doctorSpeciality").getString("specialityFullName")
                                );
                                doctorsList.add(doctor);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        listAdapterWithRecycleView = new ListAdapterWithRecycleView(MainActivity.this,doctorsList);
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(listAdapterWithRecycleView);
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
