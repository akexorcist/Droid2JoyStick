package com.akexorcist.d2j.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.akexorcist.d2j.R;
import com.akexorcist.d2j.singleton.BusProvider;
import com.akexorcist.d2j.singleton.bluetooth.BluetoothManager;
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

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    private Toolbar tbMain;
    private Drawer ndMenu;

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
        if (item.equalsIgnoreCase("device_connection")) {
            replaceFragment(DeviceConnectionFragment.newInstance());
        } else if (item.equalsIgnoreCase("settings")) {
            Log.e("Check", "Settings");
        } else if (item.equalsIgnoreCase("airplane")) {
            Log.e("Check", "Airplane");
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
        PrimaryDrawerItem joyType1DrawerItem = new PrimaryDrawerItem().withName("Assault Horizon").withTag("Airplane").withLevel(2);
        PrimaryDrawerItem joyType2DrawerItem = new PrimaryDrawerItem().withName("Awesomenauts").withTag("side_scroll").withLevel(2);
        PrimaryDrawerItem joyType3DrawerItem = new PrimaryDrawerItem().withName("Dirt 3").withTag("Racing").withLevel(2);
        SectionDrawerItem sectionDrawerItem = new SectionDrawerItem().withName("Joystick Type").withTextColorRes(R.color.colorAccent);
        PrimaryDrawerItem deviceDrawerItem = new PrimaryDrawerItem().withName("Device Connection").withTag("device_connection").withIcon(GoogleMaterial.Icon.gmd_devices);
        PrimaryDrawerItem settingsDrawerItem = new PrimaryDrawerItem().withName("Settings").withTag("settings").withIcon(GoogleMaterial.Icon.gmd_settings);

        ndMenu = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(tbMain)
                .withCloseOnClick(true)
                .withFireOnInitialOnClick(true)
                .withSelectedItemByPosition(1)
                .withDelayDrawerClickEvent(300)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(false)
                .withOnDrawerItemClickListener(this)
                .addDrawerItems(section, deviceDrawerItem, settingsDrawerItem, sectionDrawerItem, joyType1DrawerItem, joyType2DrawerItem, joyType3DrawerItem)
                .build();
    }

    private void requestPermission(String[] permissions, AssentCallback callback) {
        int requestCode = new Random().nextInt(100);
        Assent.requestPermissions(callback, requestCode, permissions);
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_fragment_container, fragment)
                .addToBackStack(null)
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


}
