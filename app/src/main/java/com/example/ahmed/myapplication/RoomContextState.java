package com.example.ahmed.myapplication;

import android.view.View;
import android.widget.TextView;

/**
 * Created by safaa on 29/12/2017.
 */

public class RoomContextState {

    private String room;
    private String status;
    private String Ringerstatus;

    private int light;
    private float noise;

    public RoomContextState() {
    }

    public RoomContextState(String room, String status, String Ringerstatus, int light, float noise) {
        super();
        this.room = room;
        this.status = status;
        this.Ringerstatus = Ringerstatus;
        this.light = light;
        this.noise = noise;
    }

    public String getRoom() {
        return this.room;
    }

    public String getStatus() {
        return this.status;
    }

    public int getLight() {
        return this.light;
    }

    public float getNoise() {
        return this.noise;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRingerstatus() {
        return Ringerstatus;
    }

    public void setRingerstatus(String ringerstatus) {
        Ringerstatus = ringerstatus;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public void setNoise(float noise) {
        this.noise = noise;
    }
}