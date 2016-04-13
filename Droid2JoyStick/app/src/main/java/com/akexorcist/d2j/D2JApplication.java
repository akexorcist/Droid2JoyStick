package com.akexorcist.d2j;

import android.app.Application;

import com.akexorcist.d2j.singleton.bluetooth.BluetoothManager;
import com.akexorcist.d2j.singleton.Contextor;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class D2JApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
        BluetoothManager.init(Contextor.getInstance().getContext());
    }
}
