package com.akexorcist.d2j.singleton.bluetooth.event;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class BluetoothConnectedEvent {
    String name;
    String address;

    public BluetoothConnectedEvent() {
    }

    public BluetoothConnectedEvent(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
