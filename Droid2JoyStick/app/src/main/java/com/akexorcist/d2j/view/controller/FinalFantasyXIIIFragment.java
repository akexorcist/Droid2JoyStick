package com.akexorcist.d2j.view.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.akexorcist.d2j.R;
import com.akexorcist.d2j.singleton.BusProvider;
import com.akexorcist.d2j.singleton.bluetooth.BluetoothManager;
import com.akexorcist.d2j.singleton.bluetooth.event.BluetoothConnectedEvent;
import com.akexorcist.d2j.singleton.gamepad.GamepadKey;
import com.akexorcist.d2j.view.ActivityListener;
import com.akexorcist.d2j.view.FullscreenListener;
import com.akexorcist.d2j.widget.GamepadImageButton;
import com.akexorcist.d2j.widget.JoyStickPad;
import com.squareup.otto.Subscribe;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class FinalFantasyXIIIFragment extends Fragment implements GamepadImageButton.GamepadPressListener, JoyStickPad.JoyStickActionListener, View.OnClickListener {
    private ImageButton btnFullscreen;
    private GamepadImageButton btnSquare;
    private GamepadImageButton btnCross;
    private GamepadImageButton btnCircle;
    private GamepadImageButton btnTriangle;
    private GamepadImageButton btnR1;
    private GamepadImageButton btnR2;
    private GamepadImageButton btnR3;
    private GamepadImageButton btnL1;
    private GamepadImageButton btnL2;
    private GamepadImageButton btnL3;
    private GamepadImageButton btnStart;
    private GamepadImageButton btnSelect;
    private JoyStickPad jsAnalogLeft;
    private JoyStickPad jsAnalogRight;

    public static FinalFantasyXIIIFragment newInstance() {
        return new FinalFantasyXIIIFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_final_fantasy_xiii, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);
        setupView();
        setupComponent();
    }

    private void bindView(View view) {
        btnFullscreen = (ImageButton) view.findViewById(R.id.btn_fullscreen);
        btnSquare = (GamepadImageButton) view.findViewById(R.id.btn_square);
        btnCross = (GamepadImageButton) view.findViewById(R.id.btn_cross);
        btnCircle = (GamepadImageButton) view.findViewById(R.id.btn_circle);
        btnTriangle = (GamepadImageButton) view.findViewById(R.id.btn_triangle);
        btnL1 = (GamepadImageButton) view.findViewById(R.id.btn_l1);
        btnR1 = (GamepadImageButton) view.findViewById(R.id.btn_r1);
        btnL2 = (GamepadImageButton) view.findViewById(R.id.btn_l2);
        btnR2 = (GamepadImageButton) view.findViewById(R.id.btn_r2);
        btnL3 = (GamepadImageButton) view.findViewById(R.id.btn_l3);
        btnR3 = (GamepadImageButton) view.findViewById(R.id.btn_r3);
        btnSelect = (GamepadImageButton) view.findViewById(R.id.btn_select);
        btnStart = (GamepadImageButton) view.findViewById(R.id.btn_start);
        jsAnalogLeft = (JoyStickPad) view.findViewById(R.id.js_analog_left);
        jsAnalogRight = (JoyStickPad) view.findViewById(R.id.js_analog_right);
    }

    private void setupView() {
        btnSquare.setGamepadPressListener(this);
        btnCross.setGamepadPressListener(this);
        btnCircle.setGamepadPressListener(this);
        btnTriangle.setGamepadPressListener(this);
        btnL1.setGamepadPressListener(this);
        btnR1.setGamepadPressListener(this);
        btnL2.setGamepadPressListener(this);
        btnR2.setGamepadPressListener(this);
        btnL3.setGamepadPressListener(this);
        btnR3.setGamepadPressListener(this);
        btnSelect.setGamepadPressListener(this);
        btnStart.setGamepadPressListener(this);
        btnFullscreen.setOnClickListener(this);
        jsAnalogLeft.setJoyStickActionListener(this);
        jsAnalogRight.setJoyStickActionListener(this);
        jsAnalogLeft.setRepeatTouchEventDelay(50);
        jsAnalogRight.setRepeatTouchEventDelay(50);
        ((ActivityListener) getActivity()).setActivityTitle("Final Fantasy XIII");
    }

    private void setupComponent() {
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().getProvider().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().getProvider().unregister(this);
    }

    @Override
    public void onButtonAction(View v, boolean isPressed) {
        if (v == btnL1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.L1, isPressed);
        } else if (v == btnL2) {
            BluetoothManager.getInstance().sendButton(GamepadKey.L2, isPressed);
        } else if (v == btnL3) {
            BluetoothManager.getInstance().sendButton(GamepadKey.L3, isPressed);
        } else if (v == btnR1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.R1, isPressed);
        } else if (v == btnR2) {
            BluetoothManager.getInstance().sendButton(GamepadKey.R2, isPressed);
        } else if (v == btnR3) {
            BluetoothManager.getInstance().sendButton(GamepadKey.R3, isPressed);
        } else if (v == btnSelect) {
            BluetoothManager.getInstance().sendButton(GamepadKey.SELECT, isPressed);
        } else if (v == btnStart) {
            BluetoothManager.getInstance().sendButton(GamepadKey.START, isPressed);
        } else if (v == btnSquare) {
            BluetoothManager.getInstance().sendButton(GamepadKey.B, isPressed);
        } else if (v == btnCross) {
            BluetoothManager.getInstance().sendButton(GamepadKey.X, isPressed);
        } else if (v == btnCircle) {
            BluetoothManager.getInstance().sendButton(GamepadKey.A, isPressed);
        } else if (v == btnTriangle) {
            BluetoothManager.getInstance().sendButton(GamepadKey.Y, isPressed);
        }
    }

    @Override
    public void onStickUp(JoyStickPad v) {
        if (v == jsAnalogLeft) {
            BluetoothManager.getInstance().sendLeftAxis(0, 0);
        } else if (v == jsAnalogRight) {
            BluetoothManager.getInstance().sendRightAxis(0, 0);
        }
    }

    @Override
    public void onStickMove(JoyStickPad v, int x, int y) {
        if (v == jsAnalogLeft) {
            BluetoothManager.getInstance().sendLeftAxis(x, y);
        } else if (v == jsAnalogRight) {
            BluetoothManager.getInstance().sendRightAxis(x, y);
        }
    }

    @Subscribe
    public void onBluetoothDeviceConnected(BluetoothConnectedEvent event) {
        Log.e("Check", "Connected");
    }

    @Override
    public void onClick(View v) {
        if (v == btnFullscreen) {
            toggleFullscreen();
        }
    }

    private void toggleFullscreen() {
        ((FullscreenListener) getActivity()).toggleFullscreen();
    }
}
