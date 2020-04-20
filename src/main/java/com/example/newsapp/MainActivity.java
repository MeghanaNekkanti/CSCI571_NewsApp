package com.example.newsapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.newsapp.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements LocationListener {
    //    HomeFragment homeFragment = new HomeFragment();
    Boolean display = false;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_headlines, R.id.navigation_trending, R.id.navigation_bookmarks)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.d("TAG", "onRequestPermissionsResult: ");
//        // Forward results to EasyPermissions
//
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
//    public void requestLocationPermission() {
//        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            setDisplay(true);
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Log.d("TAG", "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
//
//            setLatitude(location.getLatitude());
//            setLongitude(location.getLongitude());
//            Log.d("TAG", "requestLocationPermission: 1");
//
//            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
//            FragmentManager manager = getSupportFragmentManager();
//            Fragment fragment = manager.findFragmentById(R.id.nav_host_fragment);
//            Log.d("TAG", "requestLocationPermission: "+fragment);
//            HomeFragment homeFragment = new HomeFragment();
//            homeFragment.setMyView(getLatitude(),getLongitude(), this,fragment.getView()
//            );
//        } else {
//            Log.d("TAG", "requestLocationPermission: 2");
//            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
//        }
//    }

//    public Boolean getDisplay() {
//        Log.d("TAG", "getDisplay: ");
//        return display;
//    }
//
//    public double getLatitude() {
//        Log.d("TAG", "getLatitude: ");
//        return latitude;
//    }
//
//    public double getLongitude() {
//        Log.d("TAG", "getLongitude: ");
//        return longitude;
//    }
//
//    public void setLatitude(double latitude) {
//        Log.d("TAG", "setLatitude: ");
//        this.latitude = latitude;
//    }
//
//    public void setLongitude(double longitude) {
//        Log.d("TAG", "setLongitude: ");
//        this.longitude = longitude;
//    }
//
//    public void setDisplay(Boolean display) {
//        Log.d("TAG", "setDisplay: ");
//        this.display = display;
//    }

    @Override
    public void onLocationChanged(Location location) {
//        Log.d("TAG", "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
//        setLatitude(location.getLatitude());
//        setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}



