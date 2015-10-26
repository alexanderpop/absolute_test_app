package software.absolute.test_app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import software.absolute.test_app.R;
import software.absolute.test_app.helpers.DatabaseHelper;
import software.absolute.test_app.helpers.GPSTracker;
import software.absolute.test_app.models.MarkerModel;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, CompoundButton.OnCheckedChangeListener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private static final float INITIAL_ZOOM_LEVEL = 11.0f;
    private GPSTracker gpsTracker;
    private RelativeLayout filter_category;
    private CheckBox red_box, green_box, blue_box;
    private ToggleButton day_filter, week_filter, month_filter;
    private Button searchButton;

    private void initialize() {
        ActiveAndroid.initialize(this);
        gpsTracker = new GPSTracker(this);
        buildGoogleApiClient();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        day_filter = (ToggleButton) findViewById(R.id.today_filter_button);
        searchButton = (Button) findViewById(R.id.searchButton);
        week_filter = (ToggleButton) findViewById(R.id.week_filter_button);
        month_filter = (ToggleButton) findViewById(R.id.month_filter_button);
        filter_category = (RelativeLayout) findViewById(R.id.filter_category);
        Button createCheckin = (Button) findViewById(R.id.create_checkin);
        ToggleButton toggle_button = (ToggleButton) findViewById(R.id.toggle_button);
        red_box = (CheckBox) findViewById(R.id.red_box);
        blue_box = (CheckBox) findViewById(R.id.blue_box);
        green_box = (CheckBox) findViewById(R.id.green_box);

        mapFragment.getMapAsync(this);

        createCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, CheckinActivity.class);
                intent.putExtra("latitude", gpsTracker.getLatitude());
                intent.putExtra("longitude", gpsTracker.getLongitude());
                startActivity(intent);
            }
        });

        toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    filter_category.setVisibility(View.VISIBLE);
                else
                    filter_category.setVisibility(View.GONE);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapActivity.this, SearchActivity.class));
            }
        });

        boolean red_bool = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("red", true);
        boolean green_bool = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("green", true);
        boolean blue_bool = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("blue", true);

        boolean day_bool = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("day_filter", false);
        boolean week_bool = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("week_filter", true);
        boolean month_bool = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("month_filter", false);

        day_filter.setChecked(day_bool);
        week_filter.setChecked(week_bool);
        month_filter.setChecked(month_bool);

        red_box.setChecked(red_bool);
        green_box.setChecked(green_bool);
        blue_box.setChecked(blue_bool);

        day_filter.setOnCheckedChangeListener(this);
        week_filter.setOnCheckedChangeListener(this);
        month_filter.setOnCheckedChangeListener(this);

        red_box.setOnCheckedChangeListener(this);
        blue_box.setOnCheckedChangeListener(this);
        green_box.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initialize();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (gpsTracker.canGetLocation()) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), INITIAL_ZOOM_LEVEL));
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
            dialog.setMessage(MapActivity.this.getResources().getString(R.string.location_services));
            dialog.setPositiveButton(MapActivity.this.getResources().getString(R.string.turn_on_location_services), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.show();
        }
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    @Override
    public void onConnectionSuspended(int i) {
        switch (i) {
            case 1: {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.google_play_disconnected), Toast.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.connection_google_lost), Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(this);
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                filter_category.setVisibility(View.GONE);
            }
        });
        getCheckins();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        filter_category.setVisibility(View.VISIBLE);
        startActivity(new Intent(MapActivity.this, MarkerActivity.class).putExtra("marker_title", marker.getTitle()));
        return true;
    }

    private void getCheckins() {
        mMap.clear();
        List<MarkerModel> markerModels = new ArrayList<>();
        if (day_filter.isChecked()) {
            markerModels = DatabaseHelper.get().getMarkers("day_filter");
        } else if (week_filter.isChecked()) {
            markerModels = DatabaseHelper.get().getMarkers("week_filter");
        } else if (month_filter.isChecked()) {
            markerModels = DatabaseHelper.get().getMarkers("month_filter");
        }
        if (markerModels != null && markerModels.size() > 0) {
            for (MarkerModel item : markerModels) {
                if (item.getColor().equals("red") && red_box.isChecked()) {
                    double latitude = Double.parseDouble(item.getLatitude());
                    double longitude = Double.parseDouble(item.getLongitude());
                    LatLng position = new LatLng(latitude, longitude);
                    getmMap().addMarker(new MarkerOptions()
                            .position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true).title(item.getTitle()));
                }
                if (item.getColor().equals("blue") && blue_box.isChecked()) {
                    double latitude = Double.parseDouble(item.getLatitude());
                    double longitude = Double.parseDouble(item.getLongitude());
                    LatLng position = new LatLng(latitude, longitude);
                    getmMap().addMarker(new MarkerOptions()
                            .position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).visible(true).title(item.getTitle()));
                }
                if (item.getColor().equals("green") && green_box.isChecked()) {
                    double latitude = Double.parseDouble(item.getLatitude());
                    double longitude = Double.parseDouble(item.getLongitude());
                    LatLng position = new LatLng(latitude, longitude);
                    getmMap().addMarker(new MarkerOptions()
                            .position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).visible(true).title(item.getTitle()));
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsTracker = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.red_box: {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("red", b).apply();
                getCheckins();
                break;
            }
            case R.id.blue_box: {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("blue", b).apply();
                getCheckins();
                break;
            }
            case R.id.green_box: {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("green", b).apply();
                getCheckins();
                break;
            }

            case R.id.today_filter_button: {
                removeListener(week_filter, month_filter);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("day_filter", b).apply();
                getCheckins();
                break;
            }

            case R.id.week_filter_button: {
                removeListener(day_filter, month_filter);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("week_filter", b).apply();
                getCheckins();
                break;
            }

            case R.id.month_filter_button: {
                removeListener(day_filter, week_filter);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("month_filter", b).apply();
                getCheckins();
                break;
            }
        }
    }

    private void removeListener(ToggleButton tb1, ToggleButton tb2) {
        tb1.setOnCheckedChangeListener(null);
        tb2.setOnCheckedChangeListener(null);
        tb1.setChecked(false);
        tb2.setChecked(false);
        tb1.setOnCheckedChangeListener(this);
        tb2.setOnCheckedChangeListener(this);
    }
}
