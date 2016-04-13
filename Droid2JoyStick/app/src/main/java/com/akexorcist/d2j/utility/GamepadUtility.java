package com.akexorcist.d2j.utility;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class GamepadUtility {
    private static GamepadUtility gamepadUtility;

    public static GamepadUtility getInstance() {
        if (gamepadUtility == null) {
            gamepadUtility = new GamepadUtility();
        }
        return gamepadUtility;
    }

    public int getXAxisValue(float degree, float offset) {
        float xRatio = 0;
        if (degree >= 0 && degree <= 90) {
            xRatio = (degree < 45) ? 1 : (90 - Math.abs(degree)) / 45;
        } else if (degree > 90 && degree <= 180) {
            xRatio = -((degree >= 135) ? 1 : (Math.abs(degree) - 90) / 45);
        } else if (degree < 0 && degree >= -90) {
            xRatio = (degree > -45) ? 1 : (90 - Math.abs(degree)) / 45;
        } else if (degree < -90 && degree >= -180) {
            xRatio = -((degree <= -135) ? 1 : (Math.abs(degree) - 90) / 45);
        }
        return Math.round((xRatio * 127f) * offset);
    }

    public int getYAxisValue(float degree, float offset) {
        float yRatio = 0;
        if (degree >= 0 && degree <= 90) {
            yRatio = -((degree > 45) ? 1 : Math.abs(degree) / 45);
        } else if (degree > 90 && degree <= 180) {
            yRatio = -((degree <= 135) ? 1 : (180 - Math.abs(degree)) / 45);
        } else if (degree < 0 && degree >= -90) {
            yRatio = (degree < -45) ? 1 : Math.abs(degree) / 45;
        } else if (degree < -90 && degree >= -180) {
            yRatio = (degree >= -135) ? 1 : (180 - Math.abs(degree)) / 45;
        }
        return Math.round((yRatio * 127f) * offset);
    }
}
