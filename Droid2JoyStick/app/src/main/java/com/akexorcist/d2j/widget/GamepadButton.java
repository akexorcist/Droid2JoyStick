package com.akexorcist.d2j.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class GamepadButton extends Button {
    private GamepadPressListener gamepadPressListener;

    public GamepadButton(Context context) {
        super(context);
    }

    public GamepadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GamepadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            setPressed(true);
            if (gamepadPressListener != null) {
                gamepadPressListener.onButtonAction(this, true);
            }
        } else if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_CANCEL) {
            setPressed(false);
            if (gamepadPressListener != null) {
                gamepadPressListener.onButtonAction(this, false);
            }
        }
        return true;
    }

    @Deprecated
    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    @Deprecated
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    public void setGamepadPressListener(GamepadPressListener listener) {
        this.gamepadPressListener = listener;
    }

    public interface GamepadPressListener {
        void onButtonAction(View v, boolean isPressed);
    }
}
