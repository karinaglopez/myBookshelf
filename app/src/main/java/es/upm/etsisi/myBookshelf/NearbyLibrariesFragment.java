package es.upm.etsisi.myBookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

import es.upm.etsisi.myBookshelf.REST.BibliotecasMadrid.BibliotecasMadridResponse;
import es.upm.etsisi.myBookshelf.REST.BibliotecasMadrid.Library;
import es.upm.etsisi.myBookshelf.REST.BibliotecasMadridAdapter;
import es.upm.etsisi.myBookshelf.databinding.ActivityNearbyLibrariesBinding;
import retrofit2.Call;


import android.Manifest;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.upm.etsisi.myBookshelf.utils.PermissionUtils;

public class NearbyLibrariesFragment extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap map;
    private ActivityNearbyLibrariesBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng userLocation;


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNearbyLibrariesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();
        //viewNearbyLibraries();
    }

    public void viewNearbyLibraries() {
        //Location loc = new Location(40.46558, -3.689354);
        //Toast.makeText(this, userLocation.getLongitude() + " " +userLocation.getLongitude(), Toast.LENGTH_LONG).show();

        //LatLng userLoc = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        //Call<BibliotecasMadridResponse> call = BibliotecasMadridAdapter.getApiService().getBibliotecasDataWithLocation(40.46558, -3.689354, 2000);
        Call<BibliotecasMadridResponse> call = BibliotecasMadridAdapter.getApiService().getBibliotecasDataWithLocation(userLocation.latitude, userLocation.longitude, 2000);

        call.enqueue(new retrofit2.Callback<BibliotecasMadridResponse>() {
            @Override
            public void onResponse(Call<BibliotecasMadridResponse> call, retrofit2.Response<BibliotecasMadridResponse> response) {
                if (response.isSuccessful() && Objects.nonNull(response.body().getNearbyLibraries())) {
                        for (Library library: response.body().getNearbyLibraries()) {
                            LatLng libraryLocation = new LatLng(library.getLocation().getLatitude(), library.getLocation().getLongitude());
                            map.addMarker(new MarkerOptions().position(libraryLocation).title(library.getTitle()));
                        }
                }
            }

            @Override
            public void onFailure(Call<BibliotecasMadridResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f));
                                CharSequence loc = userLocation.toString();
                                Toast.makeText(getApplicationContext(), loc, Toast.LENGTH_LONG).show();
                                viewNearbyLibraries();
                            }
                        }
                    });
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // ...
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }



}