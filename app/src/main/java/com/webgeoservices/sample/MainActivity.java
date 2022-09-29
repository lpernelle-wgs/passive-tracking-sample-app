package com.webgeoservices.sample;

import android.location.Location;
import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.webgeoservices.woosmapgeofencing.Woosmap;
import com.webgeoservices.woosmapgeofencing.WoosmapSettings;

import com.webgeoservices.woosmapgeofencingcore.database.*;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private Woosmap woosmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instanciate woosmap object
        this.woosmap = Woosmap.getInstance().initializeWoosmap(this);

        // Set private Woosmap key API - It is mandatory for requesting Woosmap Search API and monitor Woosmap Asset
        WoosmapSettings.privateKeyWoosmapAPI = BuildConfig.WOOSMAP_KEY; //"WOOSMAP_KEY";

        this.woosmap.setRegionLogReadyListener( new WoosRegionLogReadyListener() );
        this.woosmap.setLocationReadyListener( new WoosLocationReadyListener() );

        // Passive tracking - Medium accuracy - low power battery usage - no permanent notification in background
        this.woosmap.startTracking( Woosmap.ConfigurationProfile.passiveTracking );

        // Live tracking - High accuracy - high power battery usage - permanent notification in background
        //this.woosmap.startTracking(Woosmap.ConfigurationProfile.liveTracking);

        // By enabling SearchAPIRequest and SearchAPICreationRegion parameters, SDK automatically retrieves assets from Woosmap Search API and automatically creates geofence around assets
        WoosmapSettings.searchAPIEnable = true;
        WoosmapSettings.searchAPICreationRegionEnable = true;

        // Define the user_properties subfield name that corresponds to radius value of the Geofence
        WoosmapSettings.poiRadiusNameFromResponse = "Radius";

    }

    public class WoosRegionLogReadyListener implements Woosmap.RegionLogReadyListener {

        // The RegionLogReadyCallback callback is triggered when the user enters (or exits) in a geofence
        public void RegionLogReadyCallback(RegionLog regionLog) {
            // use regionLog.didEnter value to know if the user enters (didEnter=true) or exits (didEnter=false) the geofence
            Log.d("WoosmapGeofencing", "regionLog: " + regionLog.idStore);
        }
    }

    public class WoosLocationReadyListener implements Woosmap.LocationReadyListener {

        public void LocationReadyCallback(Location location) {
            Log.d("LocationReadyListener", "lat: " + location.getLatitude() + ", lng: " + location.getLongitude());

        }
    }



    @Override
    public void onStart() { super.onStart(); }

    @Override
    public void onResume() { super.onResume(); woosmap.onResume(); }

    @Override
    public void onPause() { super.onPause(); woosmap.onPause();}

    @Override
    protected void onDestroy() { woosmap.onDestroy(); super.onDestroy(); };
}
