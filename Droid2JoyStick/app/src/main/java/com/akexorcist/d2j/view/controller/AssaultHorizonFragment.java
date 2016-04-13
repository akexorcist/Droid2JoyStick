package com.akexorcist.d2j.view.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.akexorcist.d2j.R;
import com.akexorcist.d2j.singleton.BusProvider;
import com.akexorcist.d2j.singleton.bluetooth.BluetoothManager;
import com.akexorcist.d2j.singleton.bluetooth.event.BluetoothConnectedEvent;
import com.akexorcist.d2j.singleton.gamepad.GamepadKey;
import com.akexorcist.d2j.utility.AssaultHorizonUtility;
import com.akexorcist.d2j.view.ActivityListener;
import com.akexorcist.d2j.view.FullscreenListener;
import com.akexorcist.d2j.widget.GamepadImageButton;
import com.akexorcist.d2j.widget.JoyStickPad;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.squareup.otto.Subscribe;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class AssaultHorizonFragment extends Fragment implements GamepadImageButton.GamepadPressListener, JoyStickPad.JoyStickActionListener, SeekBar.OnSeekBarChangeListener, SensorEventListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final long REPEAT_SENSOR_EVENT_DELAY = 100;

    private CheckBox cbAccelerometer;
    private VerticalSeekBar sbSpeed;
    private SeekBar sbYaw;
    private ImageButton btnFullscreen;
    private GamepadImageButton btn1;
    private GamepadImageButton btn2;
    private GamepadImageButton btn3;
    private GamepadImageButton btn4;
    private GamepadImageButton btnR1;
    private GamepadImageButton btnL1;
    private GamepadImageButton btnL2;
    private JoyStickPad jsAnalogLeft;
    private SensorManager sensorManager;
    private long lastSensorEventMillis = 0;

    public static AssaultHorizonFragment newInstance() {
        return new AssaultHorizonFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assault_horizon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);
        setupView();
        setupComponent();
    }

    private void bindView(View view) {
        cbAccelerometer = (CheckBox) view.findViewById(R.id.cb_accelerometer);
        sbSpeed = (VerticalSeekBar) view.findViewById(R.id.sb_speed);
        sbYaw = (SeekBar) view.findViewById(R.id.sb_yaw);
        btnFullscreen = (ImageButton) view.findViewById(R.id.btn_fullscreen);
        btn1 = (GamepadImageButton) view.findViewById(R.id.btn_1);
        btn2 = (GamepadImageButton) view.findViewById(R.id.btn_2);
        btn3 = (GamepadImageButton) view.findViewById(R.id.btn_3);
        btn4 = (GamepadImageButton) view.findViewById(R.id.btn_4);
        btnR1 = (GamepadImageButton) view.findViewById(R.id.btn_r1);
        btnL1 = (GamepadImageButton) view.findViewById(R.id.btn_l1);
        btnL2 = (GamepadImageButton) view.findViewById(R.id.btn_l2);
        jsAnalogLeft = (JoyStickPad) view.findViewById(R.id.js_analog_left);
    }

    private void setupView() {
        cbAccelerometer.setOnCheckedChangeListener(this);
        sbSpeed.setOnSeekBarChangeListener(this);
        sbYaw.setOnSeekBarChangeListener(this);
        btn1.setGamepadPressListener(this);
        btn2.setGamepadPressListener(this);
        btn3.setGamepadPressListener(this);
        btn4.setGamepadPressListener(this);
        btnR1.setGamepadPressListener(this);
        btnL1.setGamepadPressListener(this);
        btnL2.setGamepadPressListener(this);
        btnFullscreen.setOnClickListener(this);
        jsAnalogLeft.setJoyStickActionListener(this);
        jsAnalogLeft.setRepeatTouchEventDelay(50);
        ((ActivityListener) getActivity()).setActivityTitle("Assault Horizon");
    }

    private void setupComponent() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
    }

    private void setupAccelerometer() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void clearAccelerometer() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().getProvider().register(this);
        if (cbAccelerometer.isChecked()) {
            setupAccelerometer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().getProvider().unregister(this);
        if (cbAccelerometer.isChecked()) {
            clearAccelerometer();
        }
    }

    @Override
    public void onButtonAction(View v, boolean isPressed) {
        if (v == btnR1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.R1, isPressed);
        } else if (v == btnL1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.L1, isPressed);
        } else if (v == btnL2) {
            BluetoothManager.getInstance().sendButton(GamepadKey.L2, isPressed);
        } else if (v == btn1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.X, isPressed);
        } else if (v == btn2) {
            BluetoothManager.getInstance().sendButton(GamepadKey.A, isPressed);
        } else if (v == btn3) {
            BluetoothManager.getInstance().sendButton(GamepadKey.B, isPressed);
        } else if (v == btn4) {
            if (isPressed) {
                BluetoothManager.getInstance().sendLeftAxis(100, -127);
            } else {
                BluetoothManager.getInstance().sendLeftAxis(0, 0);
            }
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbYaw || seekBar == sbSpeed) {
            int x = (255 - sbSpeed.getProgress()) - 127;
            int y = sbYaw.getProgress() - 127;
            BluetoothManager.getInstance().sendRightAxis(x, y);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - lastSensorEventMillis >= REPEAT_SENSOR_EVENT_DELAY &&
                event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int y = AssaultHorizonUtility.getInstance().getPitchFromAccelerometer(event.values[0]);
            int x = AssaultHorizonUtility.getInstance().getRollFromAccelerometer(event.values[1]);
            BluetoothManager.getInstance().sendLeftAxis(x, y);
            lastSensorEventMillis = System.currentTimeMillis();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == cbAccelerometer) {
            if (isChecked) {
                setupAccelerometer();
            } else {
                clearAccelerometer();
            }
        }
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
