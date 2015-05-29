package cz.janvanura.vegfinder;

import android.content.ContentUris;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;

public class RestaurantsMapActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_MAP = 1;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_map);
        setUpMapIfNeeded();
        getSupportLoaderManager().initLoader(LOADER_MAP, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case LOADER_MAP:
                return new CursorLoader(
                        this,
                        RestaurantDbSchema.CONTENT_URI,
                        new String[]{RestaurantDbSchema.C_LATITUDE, RestaurantDbSchema.C_LONGITUDE, RestaurantDbSchema._ID, RestaurantDbSchema.C_NAME},
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
            setUpMap(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (mMap != null) {
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(Cursor cursor) {

        float lat;
        float lon;
        String name;
        while (cursor.moveToNext()) {

            lat = cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LATITUDE));
            lon = cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LONGITUDE));
            name = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME));

            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(name));
        }
    }
}
