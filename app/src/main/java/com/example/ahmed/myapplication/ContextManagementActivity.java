package com.example.ahmed.myapplication;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarContextView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodSession;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import static android.R.attr.data;
import static java.sql.Types.NULL;

public class ContextManagementActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private PahoMqttClient pahoMqttClient;

    RoomContextHttpManager roomHttpManager;
    RoomContextState state;
    String room;
    View contextView;
    ImageView imageLight;
    ImageView imageRinger;
    String previous = "OFF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_management);
        imageLight = (ImageView) findViewById(R.id.imageView1);
        imageRinger = (ImageView) findViewById(R.id.imageView);
        contextView = (View) findViewById(R.id.contextLayout);
        //contextView.setVisibility(View.INVISIBLE);
        roomHttpManager = new RoomContextHttpManager(Volley.newRequestQueue(this));
        roomHttpManager.setMainContextActivity(this);
        List rules = new ArrayList();
        rules.add(new RoomContextRule() {
            @Override
            public void apply(RoomContextState roomContextState) {
                super.apply(roomContextState);
                if (condition(roomContextState)) {
                    Toast.makeText(ContextManagementActivity.this, "switch to silent mode", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected boolean condition(RoomContextState roomContextState) {
                return roomContextState.getLight() > 100
                        && roomContextState.getNoise() > 1.0;
            }

            @Override
            protected void action() {
                ((AudioManager) getApplicationContext().getSystemService(
                        Context.AUDIO_SERVICE))
                        .setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }

            public String toString() {
                return "Rule 1";
            }
        });
        pahoMqttClient = new PahoMqttClient();
        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);

    }

    public void checkButton(View view) {
        room = ((EditText) findViewById(R.id.editText1))
                .getText().toString();
        Log.v("Roooooom Id", room);
        roomHttpManager.retrieveRoomContextState(room);
    }

    public void switchRinger(View view) {
        Toast.makeText(this, "appel fonc switchRinger",Toast.LENGTH_LONG).show();
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }


        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int mode = audioManager.getRingerMode();
        if (mode == AudioManager.RINGER_MODE_SILENT)
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
        roomHttpManager.switchRinger(room);
        roomHttpManager.retrieveRoomContextState(room);
    }

    public void switchLight(View view) {
        Toast.makeText(this, "appel fonc switchLight", Toast.LENGTH_LONG).show();

        String msg;
        if(previous.equals("OFF"))
        { msg = "ON"; previous = "ON";}
        else
        {msg ="OFF"; previous = "OFF";}

        try {
            System.out.print("publishsing to mqtt");
            pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        roomHttpManager.switchLight(room);
        roomHttpManager.retrieveRoomContextState(room);
    }

    public void updateContextView() {


        Toast.makeText(this, "appel fonc updateContextView", Toast.LENGTH_LONG).show();
        if (this.state != null) {
            Log.v("LightStatus: ", state.getStatus());
            Log.v("RingerStatus: ", state.getRingerstatus());

            contextView.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewLightValue)).setText(" " + Integer.toString(state.getLight()));
            ((TextView) findViewById(R.id.textViewNoiseValue)).setText(" " + Float.toString(state.getNoise()));
            if (state.getStatus().equals("ON"))
                imageLight.setImageResource(R.drawable.ic_bulb_on);
            else
                imageLight.setImageResource(R.drawable.ic_bulb_off);

            if (state.getRingerstatus().equals("ON"))
                imageRinger.setImageResource(R.drawable.ringer_on1);
            else
                imageRinger.setImageResource(R.drawable.ringer_off1);
        } else {
            //initView();
        }
    }

    public void onStateChanged(RoomContextState state) {
        this.state = state;
        updateContextView();
    }


}