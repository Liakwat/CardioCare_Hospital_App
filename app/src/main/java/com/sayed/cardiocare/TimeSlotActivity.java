package com.sayed.cardiocare;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sayed.cardiocare.Adapter.ListAdapterWithRecycleView;
import com.sayed.cardiocare.Adapter.TimeSlotAdapter;
import com.sayed.cardiocare.Models.TimeSlot;
import com.sayed.cardiocare.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeSlotActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener{

        Button showFragment;
        TextView date;
    TimeSlotAdapter timeSlotAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

     ArrayList<TimeSlot> slotLists;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_time_slot);
            date = findViewById(R.id.date);
            showFragment = (Button) findViewById(R.id.btnDatePicker);
            recyclerView = findViewById(R.id.recycleListView);

            slotLists = new ArrayList<>();

            getDoctorsSchedule("E000024","2019-03-16");

        }

        public void showFragment(View view) {
            /**Show DialogFragment By Calling DatePickerFragment Class**/
            DialogFragment newFragment = new DatePickerFragment();
            /**Call show() of DialogFragment class**/
            newFragment.show(getSupportFragmentManager(), "date picker");
        }

    @Override
    public void onComplete(String date){
        clear();
        getDoctorsSchedule("E000024","2019-03-18");
    }

    public void  clear() {
        slotLists.clear();
    }

    public void getDoctorsSchedule(String id, String date){

        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/scheduleSlots/doctor/"+id+"/fromDate/"+date+"/next/0";

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
                        try {
                            String slotId = (String) response.getJSONObject(0).getString("slotMppingId");
                            String slotDate = (String) response.getJSONObject(0).getString("date");

                            for (int i = 0; i < response.getJSONObject(0).getJSONArray("slots").length(); i++) {

                                TimeSlot timeSlot = new TimeSlot(
                                        response.getJSONObject(0).getJSONArray("slots").getJSONObject(i).getString("timeSlot"),
                                        response.getJSONObject(0).getJSONArray("slots").getJSONObject(i).getString("isBooked"),
                                        response.getJSONObject(0).getJSONArray("slots").getJSONObject(i).getString("serialNo")
                                );
                                slotLists.add(timeSlot);
                            }

                            timeSlotAdapter = new TimeSlotAdapter(TimeSlotActivity.this,slotLists);
                            linearLayoutManager = new LinearLayoutManager(TimeSlotActivity.this,LinearLayoutManager.VERTICAL,false);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(TimeSlotActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(timeSlotAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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