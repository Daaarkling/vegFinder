package cz.janvanura.vegfinder.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import cz.janvanura.vegfinder.R;
import cz.janvanura.vegfinder.fragment.RestaurantDetailFragment;
import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;

public class RestaurantsMapActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {

    public static final int LOADER_MAP = 1;

    private Map<Marker, Integer> mMarkerMap = new HashMap<>();
    private GoogleMap mMap;
    private float lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            lat = bundle.getFloat(RestaurantDetailFragment.MAP_POS_LAT);
            lon = bundle.getFloat(RestaurantDetailFragment.MAP_POS_LON);
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case LOADER_MAP:
                return new CursorLoader(
                        this,
                        RestaurantDbSchema.CONTENT_URI,
                        new String[]{RestaurantDbSchema.C_LATITUDE, RestaurantDbSchema.C_LONGITUDE, RestaurantDbSchema._ID, RestaurantDbSchema.C_NAME, RestaurantDbSchema.C_STREET, RestaurantDbSchema.C_POSTAL_CODE, RestaurantDbSchema.C_COUNTRY, RestaurantDbSchema.C_LOCALITY},
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (mMap != null) {
            setUpMarkers(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        getSupportLoaderManager().initLoader(LOADER_MAP, null, this);

        LatLng pos;
        int zoom;
        if(lat != 0 && lon != 0) {
            pos = new LatLng(lat, lon);
            zoom = 15;
        } else {
            pos = new LatLng(49.8037633, 15.4749126);
            zoom = 7;
        }


        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                int id = mMarkerMap.get(marker);
                Intent intent = new Intent(RestaurantsMapActivity.this, RestaurantDetailActivity.class);
                intent.putExtra(RestaurantDetailFragment.ROW_ID, id);
                startActivity(intent);
            }
        });
    }


    private void setUpMarkers(Cursor cursor) {

        float lat;
        float lon;
        String name;
        Marker marker;
        while (cursor.moveToNext()) {

            lat = cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LATITUDE));
            lon = cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LONGITUDE));
            name = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME));

            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(name)
                    .snippet(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_STREET)) + ", " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_POSTAL_CODE)) + ", " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_LOCALITY))
                    ));

            mMarkerMap.put(marker, cursor.getInt(cursor.getColumnIndex(RestaurantDbSchema._ID)));

            if(this.lat == lat && this.lon == lon) {
                marker.showInfoWindow();
            }
        }
    }
}
