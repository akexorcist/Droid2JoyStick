package com.akexorcist.d2j.utility;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class AssaultHorizonUtility {
    private static AssaultHorizonUtility assaultHorizonUtility;

    public static AssaultHorizonUtility getInstance() {
        if (assaultHorizonUtility == null) {
            assaultHorizonUtility = new AssaultHorizonUtility();
        }
        return assaultHorizonUtility;
    }

    public int getPitchFromAccelerometer(float value) {
        float pitch = value / 9;
        pitch = (pitch > 1) ? 1 : pitch;
        pitch = (pitch < -0.4f) ? -0.4f : pitch;
        pitch = (pitch < 0.6) ? (pitch - 0.6f) / 1f : (pitch - 0.6f) / 0.4f;
        return (int) (pitch * 127);
    }

    public int getRollFromAccelerometer(float value) {
        float roll = value / 6;
        roll = (roll > 1) ? 1 : roll;
        roll = (roll < -1) ? -1 : roll;
        return (int) (roll * 127);
    }

}
