package cz.janvanura.vegfinder.model.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cz.janvanura.vegfinder.model.db.RestaurantDbHelper;
import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.entity.Restaurant;

/**
 * Created by Jan on 28. 3. 2015.
 */
public class RestaurantRepository {

    private SQLiteDatabase mDatabase;
    private RestaurantDbHelper mDbHelper;

    public RestaurantRepository(Context context) {
        mDbHelper = new RestaurantDbHelper(context);
    }


    public Restaurant findById(Integer id) {

        mDatabase = mDbHelper.getWritableDatabase();
        Cursor cursor = mDatabase.query(RestaurantDbSchema.TABLE_NAME, null, RestaurantDbSchema._ID + " = ?", new String[]{id.toString()}, null, null, null);
        cursor.moveToFirst();
        Restaurant restaurant = cursorToRestaurant(cursor);
        cursor.close();
        mDbHelper.close();
        return restaurant;
    }


    public Cursor findAll() {

        mDatabase = mDbHelper.getWritableDatabase();
        Cursor cursor = mDatabase.query(RestaurantDbSchema.TABLE_NAME, null, null, null, null, null, RestaurantDbSchema.C_NAME + " ASC");
        //cursor.close();
        //mDbHelper.close();
        return cursor;
    }


    private Restaurant cursorToRestaurant(Cursor cursor) {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(cursor.getInt(cursor.getColumnIndex(RestaurantDbSchema._ID)));
        restaurant.setName(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME)));
        restaurant.setLatitude(cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LATITUDE)));
        restaurant.setLongitude(cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LONGITUDE)));
        restaurant.setStreet(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_STREET)));
        restaurant.setLocality(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_LOCALITY)));
        return restaurant;
    }
}
