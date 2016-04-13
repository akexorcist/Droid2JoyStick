package com.akexorcist.d2j.singleton;

import com.squareup.otto.Bus;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class BusProvider {
    private static BusProvider busProvider;

    public static BusProvider getInstance() {
        if (busProvider == null) {
            busProvider = new BusProvider();
        }
        return busProvider;
    }

    private Bus bus;

    public BusProvider() {
        bus = new Bus();
    }

    public Bus getProvider() {
        return bus;
    }
}
