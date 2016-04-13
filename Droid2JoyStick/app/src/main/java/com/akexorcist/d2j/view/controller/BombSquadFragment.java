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
public class BombSquadFragment extends Fragment implements GamepadImageButton.GamepadPressListener, JoyStickPad.JoyStickActionListener, View.OnClickListener {
    private ImageButton btnFullscreen;
    private GamepadImageButton btnJump;
    private GamepadImageButton btnBomb;
    private GamepadImageButton btnPunch;
    private GamepadImageButton btnRun;
    private GamepadImageButton btnMenu;
    private JoyStickPad jsAnalogLeft;

    public static BombSquadFragment newInstance() {
        return new BombSquadFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bombsquard, container, false);
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
        btnJump = (GamepadImageButton) view.findViewById(R.id.btn_jump);
        btnBomb = (GamepadImageButton) view.findViewById(R.id.btn_bomb);
        btnRun = (GamepadImageButton) view.findViewById(R.id.btn_run);
        btnPunch = (GamepadImageButton) view.findViewById(R.id.btn_punch);
        btnMenu = (GamepadImageButton) view.findViewById(R.id.btn_menu);
        jsAnalogLeft = (JoyStickPad) view.findViewById(R.id.js_analog_left);
    }

    private void setupView() {
        btnJump.setGamepadPressListener(this);
        btnBomb.setGamepadPressListener(this);
        btnRun.setGamepadPressListener(this);
        btnPunch.setGamepadPressListener(this);
        btnMenu.setGamepadPressListener(this);
        btnFullscreen.setOnClickListener(this);
        jsAnalogLeft.setJoyStickActionListener(this);
        jsAnalogLeft.setRepeatTouchEventDelay(50);
        ((ActivityListener) getActivity()).setActivityTitle("BombSquad");
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
        if (v == btnJump) {
            BluetoothManager.getInstance().sendButton(GamepadKey.X, isPressed);
        } else if (v == btnBomb) {
            BluetoothManager.getInstance().sendButton(GamepadKey.B, isPressed);
        } else if (v == btnRun) {
            BluetoothManager.getInstance().sendButton(GamepadKey.Y, isPressed);
        } else if (v == btnPunch) {
            BluetoothManager.getInstance().sendButton(GamepadKey.A, isPressed);
        } else if (v == btnMenu) {
            BluetoothManager.getInstance().sendButton(GamepadKey.L1, isPressed);
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
