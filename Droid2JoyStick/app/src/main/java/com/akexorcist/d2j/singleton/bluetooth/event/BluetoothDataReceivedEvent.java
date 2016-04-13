package com.akexorcist.d2j.singleton.bluetooth.event;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class BluetoothDataReceivedEvent {
    private byte[] data;
    private String message;

    public BluetoothDataReceivedEvent() {
    }

    public BluetoothDataReceivedEvent(byte[] data, String message) {
        this.data = data;
        this.message = message;
    }

    public byte[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
