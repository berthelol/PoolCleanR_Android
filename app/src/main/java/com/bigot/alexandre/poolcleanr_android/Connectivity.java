package com.bigot.alexandre.poolcleanr_android;

/**
 * Created by AlexandreBigot on 04/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Connectivity extends AppCompatActivity {

    public static final String TAG = "Connectivity";

    public String jsonvalue;
    private Context context;

    // Constructor
    public Connectivity(final Context context) {
        this.context = context;
        jsonvalue="";
    }

    // JSON Request
    public void getJSONRequest() {
        //WTF ?
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://httpbin.org/get?site=code&network=tutsplus";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Take what you need in the JSON
                            response = response.getJSONObject("args");
                            String site = response.getString("site"),
                                    network = response.getString("network");
                            Log.i(TAG, "Site: "+site+"\nNetwork: "+network);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(jsonRequest);
    }

    // String Request
    public void getStringRequest() {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://loicberthelot.freeboxos.fr/device/pH/";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response
                        //Log.i(TAG, "Response is: "+ response);
                        storedata(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "That didn't work!");
            }
        });
        queue.add(stringRequest);

    }

    public void storedata (String data)
    {
        Log.i("from storedata:","data:"+data);
        this.jsonvalue=data;
    }

}
