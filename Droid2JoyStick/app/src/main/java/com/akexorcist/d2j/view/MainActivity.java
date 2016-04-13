package com.akexorcist.d2j.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.akexorcist.d2j.R;
import com.akexorcist.d2j.singleton.BusProvider;
import com.akexorcist.d2j.singleton.bluetooth.BluetoothManager;
import com.akexorcist.d2j.view.controller.AssaultHorizonFragment;
import com.akexorcist.d2j.view.controller.AwesomenautsFragment;
import com.akexorcist.d2j.view.controller.BasicGamepad1Fragment;
import com.akexorcist.d2j.view.controller.BombSquadFragment;
import com.akexorcist.d2j.view.controller.FinalFantasyXIIIFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Random;

import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener, FullscreenListener, ActivityListener {
    private static final String KEY_DEVICE_CONNECTION = "device_connection";
    private static final String KEY_BASIC_GAMEPAD_1 = "basic_gamepad_1";
    private static final String KEY_ASSAULT_HORIZON = "assault_horizon";
    private static final String KEY_AWESOMENAUTS = "awesomenauts";
    private static final String KEY_BOMBSQUAD = "bombsquad";
    private static final String KEY_FINAL_FANTASY_XIII = "final_fantasy_xiii";

    private Toolbar tbMain;
    private Drawer ndMenu;
    private boolean isFullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        initView();
        setupComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Assent.setActivity(this, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().getProvider().register(this);
        initialBluetoothService();
    }

    private void initialBluetoothService() {
        if (BluetoothManager.getInstance().isBluetoothAvailable() &&
                !BluetoothManager.getInstance().isServiceAvailable()) {
            BluetoothManager.getInstance().setupService();
            BluetoothManager.getInstance().startService();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().getProvider().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothManager.getInstance().stopService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing())
            Assent.setActivity(this, null);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Assent.handleResult(permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                BluetoothManager.getInstance().connect(data);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                BluetoothManager.getInstance().setupService();
                BluetoothManager.getInstance().startService();
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        String item = drawerItem.getTag().toString();
        if (item.equalsIgnoreCase(KEY_DEVICE_CONNECTION)) {
            BluetoothManager.getInstance().openBluetoothConnection(this);
        } else if (item.equalsIgnoreCase("settings")) {
            Log.e("Check", "Settings");
        } else if (item.equalsIgnoreCase(KEY_ASSAULT_HORIZON)) {
            openAssaultHorizonFragment();
        } else if (item.equalsIgnoreCase(KEY_FINAL_FANTASY_XIII)) {
            openFinalFantasyXIIIFragment();
        } else if (item.equalsIgnoreCase(KEY_AWESOMENAUTS)) {
            openAwesomenautsFragment();
        } else if (item.equalsIgnoreCase(KEY_BOMBSQUAD)) {
            openBombSquadFragment();
        } else if (item.equalsIgnoreCase(KEY_BASIC_GAMEPAD_1)) {
            openBasicGamepadFragment();
        }
        return true;
    }

    private void bindView() {
        tbMain = (Toolbar) findViewById(R.id.tb_main);
    }

    private void initView() {
        setupToolbar();
        setupNavigationDrawer();
        checkBluetoothAvailability();
    }

    private void setupToolbar() {
        setSupportActionBar(tbMain);
        setTitle("Droid 2 Joy");
    }

    private void setupComponent() {
        Assent.setActivity(this, this);
        requestLocationPermission();
    }

    private void requestLocationPermission() {
        requestPermission(new String[]{Assent.ACCESS_COARSE_LOCATION, Assent.ACCESS_FINE_LOCATION}, new AssentCallback() {
            @Override
            public void onPermissionResult(PermissionResultSet permissionResultSet) {
                if (!permissionResultSet.allPermissionsGranted()) {
                    finish();
                    Toast.makeText(MainActivity.this, "You must grant location permission.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupNavigationDrawer() {
        ProfileDrawerItem section = new ProfileDrawerItem()
                .withSetSelected(false)
                .withSetSelected(false)
                .withEnabled(false);
        PrimaryDrawerItem joyType0DrawerItem = new PrimaryDrawerItem().withName("Basic Gamepad").withTag(KEY_BASIC_GAMEPAD_1).withLevel(2);
        PrimaryDrawerItem joyType1DrawerItem = new PrimaryDrawerItem().withName("Assault Horizon").withTag(KEY_ASSAULT_HORIZON).withLevel(2);
        PrimaryDrawerItem joyType2DrawerItem = new PrimaryDrawerItem().withName("Awesomenauts").withTag(KEY_AWESOMENAUTS).withLevel(2);
        PrimaryDrawerItem joyType3DrawerItem = new PrimaryDrawerItem().withName("BombSquad").withTag(KEY_BOMBSQUAD).withLevel(2);
        PrimaryDrawerItem joyType4DrawerItem = new PrimaryDrawerItem().withName("Final Fantasy XIII").withTag(KEY_FINAL_FANTASY_XIII).withLevel(2);
        SectionDrawerItem sectionDrawerItem = new SectionDrawerItem().withName("Joystick Type").withTextColorRes(R.color.colorAccent);
        PrimaryDrawerItem deviceDrawerItem = new PrimaryDrawerItem().withName("Device Connection").withTag(KEY_DEVICE_CONNECTION).withIcon(GoogleMaterial.Icon.gmd_devices);
        PrimaryDrawerItem settingsDrawerItem = new PrimaryDrawerItem().withName("Settings").withTag("settings").withIcon(GoogleMaterial.Icon.gmd_settings);

        ndMenu = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(tbMain)
                .withCloseOnClick(true)
                .withFireOnInitialOnClick(true)
                .withSelectedItemByPosition(6)
                .withDelayDrawerClickEvent(300)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(false)
                .withOnDrawerItemClickListener(this)
                .addDrawerItems(section, deviceDrawerItem, settingsDrawerItem, sectionDrawerItem, joyType0DrawerItem, joyType1DrawerItem, joyType2DrawerItem, joyType3DrawerItem, joyType4DrawerItem)
                .build();
    }

    @Override
    public void enterFullscreen() {
        isFullscreen = true;
        int uiOption = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOption);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ndMenu.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void exitFullscreen() {
        isFullscreen = false;
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
        ndMenu.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void toggleFullscreen() {
        if (isFullscreen) {
            exitFullscreen();
        } else {
            enterFullscreen();
        }
    }

    @Override
    public void setActivityTitle(int titleResId) {
        setTitle(titleResId);
    }

    @Override
    public void setActivityTitle(String title) {
        setTitle(title);
    }

    public void replaceFragment(Fragment fragment, String fragmentName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_fragment_container, fragment, fragmentName)
                .addToBackStack(fragmentName)
                .commit();
    }

    public void checkBluetoothAvailability() {
        if (BluetoothManager.getInstance().isBluetoothAvailable()) {
            BluetoothManager.getInstance().enable();
        } else {
            finish();
            Toast.makeText(this, "Bluetooth unavailable on your device", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission(String[] permissions, AssentCallback callback) {
        int requestCode = new Random().nextInt(100);
        Assent.requestPermissions(callback, requestCode, permissions);
    }

    private void openBasicGamepadFragment() {
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(KEY_BASIC_GAMEPAD_1, 0);
        if (!fragmentPopped) {
            replaceFragment(BasicGamepad1Fragment.newInstance(), KEY_BASIC_GAMEPAD_1);
        }
    }

    private void openAssaultHorizonFragment() {
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(KEY_ASSAULT_HORIZON, 0);
        if (!fragmentPopped) {
            replaceFragment(AssaultHorizonFragment.newInstance(), KEY_ASSAULT_HORIZON);
        }
    }

    private void openFinalFantasyXIIIFragment() {
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(KEY_FINAL_FANTASY_XIII, 0);
        if (!fragmentPopped) {
            replaceFragment(FinalFantasyXIIIFragment.newInstance(), KEY_FINAL_FANTASY_XIII);
        }
    }

    private void openAwesomenautsFragment() {
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(KEY_AWESOMENAUTS, 0);
        if (!fragmentPopped) {
            replaceFragment(AwesomenautsFragment.newInstance(), KEY_AWESOMENAUTS);
        }
    }

    private void openBombSquadFragment() {
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(KEY_BOMBSQUAD, 0);
        if (!fragmentPopped) {
            replaceFragment(BombSquadFragment.newInstance(), KEY_BOMBSQUAD);
        }
    }
}
