package com.example.ahmed.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * Created by safaa on 29/12/2017.
 */

public class RoomContextHttpManager {


    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();
    private RequestQueue queue;
    RoomContextState state;
    final String CONTEXT_SERVER_URL="http://springlight.herokuapp.com/api/rooms";
    ContextManagementActivity mainContextActivity;


    public RoomContextHttpManager(RequestQueue queue) {
        this.queue = queue;
    }

    public void setMainContextActivity(ContextManagementActivity mainContextActivity) {
        this.mainContextActivity = mainContextActivity;
    }

    public void switchLight(final String room){
        String url = CONTEXT_SERVER_URL + "/" + room + "/switch-light";

        //post switch light
        JsonObjectRequest contextRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        retrieveRoomContextState(room);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Some error to access URL : Room may not exists...
                    }
                });
        queue.add(contextRequest);

    }

    public void switchRinger(final String room){
        String url = CONTEXT_SERVER_URL + "/" + room + "/switch-noise";

        //post switch light
        JsonObjectRequest contextRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        retrieveRoomContextState(room);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Some error to access URL : Room may not exists...
                    }
                });
        queue.add(contextRequest);

    }

    public RoomContextState retrieveRoomContextState(String room){

        String url = CONTEXT_SERVER_URL + "/" + room + "/";
        //get room sensed context
        JsonObjectRequest contextRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String id = response.getString("id").toString();
                            int lightLevel = Integer.parseInt(response.getJSONObject("light").get("level").toString());
                            String lightStatus = response.getJSONObject("light").get("status").toString();
                            String RingerStatus = response.getJSONObject("noise").get("status").toString();
                            float noiseLevel = Float.parseFloat(response.getJSONObject("noise").get("level").toString());

                            //notifiyListners
                            state=new RoomContextState(id, lightStatus,RingerStatus,lightLevel,noiseLevel);
                            Log.v("Log",response.toString());
                            // notify main activity for update...
                            mainContextActivity.onStateChanged(state);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Some error to access URL : Room may not exists...
                    }
                });
        queue.add(contextRequest);
        return (state);



    }








}
