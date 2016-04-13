package com.akexorcist.d2j.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.akexorcist.d2j.R;
import com.akexorcist.d2j.singleton.BusProvider;
import com.akexorcist.d2j.singleton.bluetooth.BluetoothManager;
import com.akexorcist.d2j.singleton.bluetooth.event.BluetoothConnectedEvent;
import com.akexorcist.d2j.singleton.gamepad.GamepadKey;
import com.akexorcist.d2j.utility.GamepadUtility;
import com.akexorcist.d2j.widget.GamepadButton;
import com.akexorcist.d2j.widget.JoyStickPad;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.squareup.otto.Subscribe;

/**
 * Created by Akexorcist on 4/13/2016 AD.
 */
public class DeviceConnectionFragment extends Fragment implements View.OnClickListener, GamepadButton.GamepadPressListener, JoyStickPad.JoyStickActionListener {
    private Button btnGetConnection;
    private GamepadButton btnPress1;
    private GamepadButton btnPress2;
    private GamepadButton btnPress3;
    private GamepadButton btnPress4;
    private GamepadButton btnPressUp;
    private GamepadButton btnPressDown;
    private GamepadButton btnPressLeft;
    private GamepadButton btnPressRight;
    private GamepadButton btnR1;
    private JoyStickPad jsAnalogLeft;
    private JoyStickPad jsAnalogRight;

    public static DeviceConnectionFragment newInstance() {
        return new DeviceConnectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_connection, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);
        setupView();
    }

    private void bindView(View view) {
        btnGetConnection = (Button) view.findViewById(R.id.btn_get_connection);
        btnPress1 = (GamepadButton) view.findViewById(R.id.btn_press_1);
        btnPress2 = (GamepadButton) view.findViewById(R.id.btn_press_2);
        btnPress3 = (GamepadButton) view.findViewById(R.id.btn_press_3);
        btnPress4 = (GamepadButton) view.findViewById(R.id.btn_press_4);
        btnPressUp = (GamepadButton) view.findViewById(R.id.btn_press_up);
        btnPressDown = (GamepadButton) view.findViewById(R.id.btn_press_down);
        btnPressLeft = (GamepadButton) view.findViewById(R.id.btn_press_left);
        btnPressRight = (GamepadButton) view.findViewById(R.id.btn_press_right);
        btnR1 = (GamepadButton) view.findViewById(R.id.btn_r1);
        jsAnalogLeft = (JoyStickPad) view.findViewById(R.id.js_analog_left);
        jsAnalogRight = (JoyStickPad) view.findViewById(R.id.js_analog_right);
    }

    private void setupView() {
        btnGetConnection.setOnClickListener(this);
        btnPress1.setGamepadPressListener(this);
        btnPress2.setGamepadPressListener(this);
        btnPress3.setGamepadPressListener(this);
        btnPress4.setGamepadPressListener(this);
        btnPressUp.setGamepadPressListener(this);
        btnPressDown.setGamepadPressListener(this);
        btnPressLeft.setGamepadPressListener(this);
        btnPressRight.setGamepadPressListener(this);
        btnR1.setGamepadPressListener(this);
        jsAnalogLeft.setJoyStickActionListener(this);
        jsAnalogRight.setJoyStickActionListener(this);
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
    public void onClick(View v) {
        if (v == btnGetConnection) {
            BluetoothManager.getInstance().openBluetoothConnection(getActivity());
        }
    }

    @Override
    public void onButtonAction(View v, boolean isPressed) {
        if (v == btnR1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.R1, isPressed);
        } else if (v == btnPress1) {
            BluetoothManager.getInstance().sendButton(GamepadKey.X, isPressed);
        } else if (v == btnPress2) {
            BluetoothManager.getInstance().sendButton(GamepadKey.A, isPressed);
        } else if (v == btnPress3) {
            BluetoothManager.getInstance().sendButton(GamepadKey.B, isPressed);
        } else if (v == btnPress4) {
            if (isPressed) {
                BluetoothManager.getInstance().sendLeftAxis(100, -127);
            } else {
                BluetoothManager.getInstance().sendLeftAxis(0, 0);
            }
        } else if (v == btnPressUp) {
            BluetoothManager.getInstance().sendHat(GamepadKey.UP, isPressed);
        } else if (v == btnPressDown) {
            BluetoothManager.getInstance().sendHat(GamepadKey.DOWN, isPressed);
        } else if (v == btnPressLeft) {
            BluetoothManager.getInstance().sendHat(GamepadKey.LEFT, isPressed);
        } else if (v == btnPressRight) {
            BluetoothManager.getInstance().sendHat(GamepadKey.RIGHT, isPressed);
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
}
