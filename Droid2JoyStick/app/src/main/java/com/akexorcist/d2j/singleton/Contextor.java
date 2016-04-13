package com.akexorcist.d2j.singleton;

import android.content.Context;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class Contextor {
    private static Contextor contextor;

    public static Contextor getInstance() {
        if (contextor == null) {
            contextor = new Contextor();
        }
        return contextor;
    }

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
