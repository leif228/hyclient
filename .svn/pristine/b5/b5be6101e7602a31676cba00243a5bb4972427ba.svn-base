package com.eyunda.third;

import com.squareup.otto.Bus;

public class BusManager {

    private static Bus bus = null;
    private BusManager() {
    }
    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }

}
