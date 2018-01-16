package com.example.ahmed.myapplication;

/**
 * Created by Ahmed on 11/01/2018.
 */

public abstract class RoomContextRule {

    public void apply(RoomContextState context) {
        if (condition(context))
            action();
    }

    protected abstract boolean condition(RoomContextState context);

    protected abstract void action();
}
