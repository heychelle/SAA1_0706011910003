package com.uc.saa1_0706011910003;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyNotificationManager {

    private Context context;
    RequestQueue requestQueue;
    SharedPreferences userPref;
    String key;

    public MyNotificationManager(Context context) {
        this.context = context;
        this.userPref = context.getSharedPreferences("user", context.MODE_PRIVATE);
    }

    public void pushNotification(final String courseName) {
        String token = userPref.getString("utoken", "-");
        key = context.getString(R.string.fcm_key);

        String title = "Enroll Course Successful";
        String body = "Welcome to " + courseName + " class!";
        JSONObject mainObj = new JSONObject();

        try {
            mainObj.put("to", token);
            JSONObject notifObj = new JSONObject();
            notifObj.put("title", title);
            notifObj.put("body", body);
            mainObj.put("notification", notifObj);
            requestQueue = Volley.newRequestQueue(context, new HurlStack());
            String url = "https://fcm.googleapis.com/fcm/send";
            final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VolleyError", "ERROR!");
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization", "key= "+key);
                    return header;
                }
            };
            requestQueue.add(jor).setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 60000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 0;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
