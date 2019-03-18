package com.sayed.cardiocare;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sayed.cardiocare.Models.AddressInfo;
import com.sayed.cardiocare.Models.AppointmentRequest;
import com.sayed.cardiocare.Models.PatientInfo;
import com.sayed.cardiocare.Models.TimeSlot;
import com.sayed.cardiocare.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/*
response format
{
    "id": "636884723342207678",
    "serialNo": 1,
    "validityConfirmed": false,
    "isVisited": false,
    "isPaymentCollected": false,
    "consultationMainServiceChargesId": null,
    "createdBy": null,
    "createdDate": "2019-03-17T21:01:46.1195336Z",
    "patientInfo": {
        "id": "000445-03-19",
        "patientTypeId": null,
        "patientName": "test by siddik",
        "spouseName": null,
        "fatherName": null,
        "motherName": null,
        "guardianName": null,
        "dob": null,
        "age": 0,
        "age2": "35",
        "sex": "male",
        "maritalStatus": null,
        "occupation": null,
        "email": null,
        "photo": null,
        "addressID": 42207679,
        "createdBy": null,
        "createdDate": "2019-03-17T21:01:46.1131959Z",
        "updatedBy": null,
        "updatedDate": null,
        "bloodGroup": null,
        "religionID": null,
        "nationalityID": null,
        "nationalIDNo": null,
        "addressInfo": {
            "addressID": 42207679,
            "addressTypeID": 1,
            "house": null,
            "street": null,
            "countryId": null,
            "city": null,
            "zipCode": null,
            "mobile": "01847140114",
            "phone": null,
            "workPhone": null,
            "im": null,
            "note": null
        }
    },
    "doctorId": null,
    "appointment_Schedule_Day_Slot_MappingId": "636711476987797174",
    "patientInfoId": "000445-03-19",
    "visitedDate": "2019-03-19T00:00:00",
    "timeSlot": "18:00:00",
    "hrM_EmployeeInfoEmployeeID": null
}
 */


public class AppointmentActivity extends AppCompatActivity {

    Gson gson;
    TextView tv;
    String drId,timeSlot,slotId,visitDate;
    RadioButton fst,snd,report;
    RadioGroup services,sex;
    HashMap<String,String> serviceId;
    String sexVar;

    EditText name,age,mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        tv = findViewById(R.id.tv);

        fst = findViewById(R.id.fst);
        snd = findViewById(R.id.scnd);
        report = findViewById(R.id.report);
        services = findViewById(R.id.services);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        mobile = findViewById(R.id.mobile);
        sex = findViewById(R.id.sex);

        serviceId = new HashMap<>();

        drId = getIntent().getStringExtra("dr_id");
        slotId = getIntent().getStringExtra("slot_map_id");
        visitDate = getIntent().getStringExtra("visit_date");
        timeSlot = getIntent().getStringExtra("time_slot");

        getConsultationMainServiceCharges(drId);

        services.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.fst){

                    tv.setText(serviceId.get("1st Visit"));

                }else if(checkedId == R.id.scnd){

                    tv.setText(serviceId.get("2nd Visit"));

                }else if(checkedId == R.id.report){

                    tv.setText(serviceId.get("Report Check"));

                }
            }
        });

        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.male){

                    sexVar = "male";
                    tv.setText(sexVar);

                }else if(checkedId == R.id.female){

                    sexVar = "female";
                    tv.setText(sexVar);

                }else if(checkedId == R.id.Others){

                    sexVar = "others";
                    tv.setText(sexVar);

                }
            }
        });

//        getConsultationMainServiceCharges("E000024","Report Check" );
////        tv.setText(serviceid+"");


//
//        gson = new Gson();
//
//
//        PatientInfo patientInfo = new PatientInfo(
//                "male",
//                "sayed",
//                "35",
//                new AddressInfo("01737272727")
//        );
//
//        AppointmentRequest appointmentRequest = new AppointmentRequest(
//                "636711476987797174",
//                "E0024","2019-03-19",
//                "18:00:00",patientInfo,
//                "636711476987797168"
//        );
//
//        String json = gson.toJson(appointmentRequest);
//
//        Log.i("json",json);


                // Post request
//        String tag_json_obj = "json_obj_req";
//        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/appointments";


//        Map<String, String> params = new HashMap<String, String>();
//        params.put("title", "title from Android");
//        params.put("body", "Android body good one");

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

//        JSONObject address = new JSONObject();
//        JSONObject patientObj = new JSONObject();
//        JSONObject rootObj = new JSONObject();
//        try {
//            address.put("mobile","01847140114");
//
//            patientObj.put("sex","male");
//            patientObj.put("patientName","sayed");
//            patientObj.put("age2","32");
//            patientObj.put("addressInfo",address);
//
//
//            rootObj.put("appointment_Schedule_Day_Slot_MappingId", "636711476987797174");
//            rootObj.put("doctorId", "E000024");
//            rootObj.put("visitedDate", "2019-03-19");
//            rootObj.put("timeSlot", "18:10:00");
//            rootObj.put("consultationMainServiceCharges", "636711476987797168");
//            rootObj.put("patientInfo", patientObj);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.i("datatta",rootObj.toString());
//
//
//            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                    url, rootObj,
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            Log.i("Response",response.toString());
//                            tv.setText(response.toString());
//
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    // As of f605da3 the following should work
//                    NetworkResponse response = error.networkResponse;
//                    if (error instanceof ServerError && response != null) {
//                        try {
//                            String res = new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                            // Now you can use any deserializer to make sense of data
//                            JSONObject obj = new JSONObject(res);
//                            tv.setText(res);
//                        } catch (UnsupportedEncodingException e1) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (JSONException e2) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//                    }
//
//                }
//
//            })
//            {
//                /**
//                 * Passing some request headers
//                 * */
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("Content-Type", "application/json; charset=utf-8");
//                    return headers;
//                }
//
//            }
//            ;
//
//            // Adding request to request queue
//            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);




    }

    public void click(View v){

        getConsultationMainServiceCharges(drId);
        tv.setText(serviceId.toString()+"");
    }


    public void getConsultationMainServiceCharges(String drId) {
        // Tag used to cancel the request

        String tag_json_obj = "json_array_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/doctors/"+drId;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        pDialog.hide();

                        try {

                            for (int i = 0; i < response.getJSONArray("consultationMainServiceCharges").length(); i++) {

                                String sn = response.getJSONArray("consultationMainServiceCharges").getJSONObject(i).getJSONObject("service").getString("serviceName");
                                String id = response.getJSONArray("consultationMainServiceCharges").getJSONObject(i).getString("id");

//                                tv.append(sn+" "+id);

                                serviceId.put(sn,id);

                            }
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
