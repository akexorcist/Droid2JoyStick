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
import com.akexorcist.d2j.widget.GamepadButton;
import com.akexorcist.d2j.widget.JoyStickPad;
import com.squareup.otto.Subscribe;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class BasicGamepad1Fragment extends Fragment implements GamepadButton.GamepadPressListener, JoyStickPad.JoyStickActionListener, View.OnClickListener {
    private ImageButton btnFullscreen;
    private GamepadButton btnA;
    private GamepadButton btnB;
    private GamepadButton btnX;
    private GamepadButton btnY;
    private GamepadButton btnR1;
    private GamepadButton btnL1;
    private GamepadButton btnR2;
    private GamepadButton btnL2;
    private GamepadButton btnSelect;
    private GamepadButton btnStart;
    private JoyStickPad jsAnalogLeft;

    public static BasicGamepad1Fragment newInstance() {
        return new BasicGamepad1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic_gamepad_1, container, false);
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
        btnA = (GamepadButton) view.findViewById(R.id.btn_a);
        btnB = (GamepadButton) view.findViewById(R.id.btn_b);
        btnX = (GamepadButton) view.findViewById(R.id.btn_x);
        btnY = (GamepadButton) view.findViewById(R.id.btn_y);
        btnL1 = (GamepadButton) view.findViewById(R.id.btn_l1);
        btnR1 = (GamepadButton) view.findViewById(R.id.btn_r1);
        btnL2 = (GamepadButton) view.findViewById(R.id.btn_l2);
        btnR2 = (GamepadButton) view.findViewById(R.id.btn_r2);
        btnSelect = (GamepadButton) view.findViewById(R.id.btn_select);
        btnStart = (GamepadButton) view.findViewById(R.id.btn_start);
        jsAnalogLeft = (JoyStickPad) view.findViewById(R.id.js_analog_left);
    }

    private void setupView() {
        btnA.setGamepadPressListener(this);
        btnB.setGamepadPressListener(this);
        btnX.setGamepadPressListener(this);
        btnY.setGamepadPressListener(this);
        btnL1.setGamepadPressListener(this);
        btnR1.setGamepadPressListener(this);
        btnL2.setGamepadPressListener(this);
        btnR2.setGamepadPressListener(this);
        btnSelect.setGamepadPressListener(this);
        btnStart.setGamepadPressListener(this);
        btnFullscreen.setOnClickListener(this);
        jsAnalogLeft.setJoyStickActionListener(this);
        jsAnalogLeft.setRepeatTouchEventDelay(50);
        ((ActivityListener) getActivity()).setActivityTitle("Simple Gamepad");
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
        } else if (v == btnR1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.R1, isPressed);
        } else if (v == btnL2) {
            BluetoothManager.getInstance().sendButton(GamepadKey.L2, isPressed);
        } else if (v == btnR2) {
            BluetoothManager.getInstance().sendButton(GamepadKey.R2, isPressed);
        } else if (v == btnA) {
            BluetoothManager.getInstance().sendButton(GamepadKey.B, isPressed);
        } else if (v == btnB) {
            BluetoothManager.getInstance().sendButton(GamepadKey.X, isPressed);
        } else if (v == btnX) {
            BluetoothManager.getInstance().sendButton(GamepadKey.A, isPressed);
        } else if (v == btnY) {
            BluetoothManager.getInstance().sendButton(GamepadKey.Y, isPressed);
        }
    }

    @Override
    public void onStickUp(JoyStickPad v) {
        if (v == jsAnalogLeft) {
            BluetoothManager.getInstance().sendLeftAxis(0, 0);
        }
    }

    @Override
    public void onStickMove(JoyStickPad v, int x, int y) {
        if (v == jsAnalogLeft) {
            BluetoothManager.getInstance().sendLeftAxis(x, y);
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
