package com.example.gpstracking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.gpstracking.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.security.acl.Group;
import java.util.List;

import Fragments.BusFragment;
import Fragments.EventFragment;
import Fragments.MoreFragment;
import Fragments.RouteFragment;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public ActivityMapsBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    GestureDetector gestureDetector;

    LocationManager locationManager;
    LocationListener locationListener;

    LatLng userLatLong;

    private BottomNavigationView bottomr;
    private Fragment selectorFragment;
    private Group group;
    private SearchView searchView;
    private  BottomSheetBehavior bottomsheet;


    public MapsActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       LinearLayout linearLayout = findViewById(R.id.design_bottom_sheet);
       bottomsheet = BottomSheetBehavior.from(linearLayout);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        bottomr = findViewById(R.id.bottom_navigation);
        searchView = findViewById(R.id.Sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {

                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));

                        }
                    });



                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });





        mapFragment.getMapAsync(this);


        bottomr.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch(item.getItemId()){
                   case R.id.bus_fragment:
                       selectorFragment = new BusFragment();
                       break;
                   case R.id.road:
                       selectorFragment = new RouteFragment();
                       break;
                   case R.id.mores:
                       selectorFragment = new MoreFragment();
                       break;
                   case R.id.activity:
                       selectorFragment = new EventFragment();

               }
               if(selectorFragment != null){
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();
               }
               return true;
           }

       });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {


            }
        } catch (Resources.NotFoundException e) {

        }
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLatLong = new LatLng(location.getLatitude(), location.getLongitude());



              Circle circle = mMap.addCircle(new CircleOptions().center(userLatLong).radius(10000).strokeColor(Color.WHITE).fillColor(Color.RED).strokeWidth(15));
              // mMap.addMarker(new MarkerOptions().position(userLatLong).title("locstion"));
               mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));

            }
        };
        askLocation();


    }

    private void askLocation() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.clear();
                Circle circle = mMap.addCircle(new CircleOptions().center(userLatLong).radius(10000).strokeColor(Color.WHITE).fillColor(Color.RED).strokeWidth(15));
                // mMap.addMarker(new MarkerOptions().position(userLatLong).title("locstion"));



            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}