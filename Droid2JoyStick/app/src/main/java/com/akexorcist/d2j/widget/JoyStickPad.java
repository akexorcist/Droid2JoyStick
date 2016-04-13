package com.akexorcist.d2j.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.akexorcist.d2j.utility.GamepadUtility;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class JoyStickPad extends Joystick implements JoystickListener {
    private JoyStickActionListener joyStickActionListener;
    private long repeatTouchEventDelay = 0;
    private long lastTouchEventMillis = 0;

    public JoyStickPad(Context context) {
        super(context);
    }

    public JoyStickPad(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoyStickPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setJoyStickActionListener(JoyStickActionListener listener) {
        this.joyStickActionListener = listener;
        setJoystickListener(this);
    }

    public void setRepeatTouchEventDelay(long delay) {
        this.repeatTouchEventDelay = delay;
    }

    @Override
    public void onDown() {
    }

    @Override
    public void onDrag(float degrees, float offset) {
        if (joyStickActionListener != null && System.currentTimeMillis() - lastTouchEventMillis >= repeatTouchEventDelay) {
            int x = GamepadUtility.getInstance().getXAxisValue(degrees, offset);
            int y = GamepadUtility.getInstance().getYAxisValue(degrees, offset);
            joyStickActionListener.onStickMove(this, x, y);
            lastTouchEventMillis = System.currentTimeMillis();
        }
    }

    @Deprecated
    @Override
    public void setJoystickListener(JoystickListener listener) {
        super.setJoystickListener(listener);
    }

    @Override
    public void onUp() {
        if (joyStickActionListener != null) {
            joyStickActionListener.onStickUp(this);
        }
    }

    public interface JoyStickActionListener {
        void onStickUp(JoyStickPad v);

        void onStickMove(JoyStickPad v, int x, int y);
    }
}
