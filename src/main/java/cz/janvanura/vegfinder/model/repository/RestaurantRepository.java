package cz.janvanura.vegfinder.model.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.janvanura.vegfinder.model.db.RestaurantDbHelper;
import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.entity.Restaurant;

/**
 * Created by Jan on 28. 3. 2015.
 */
public class RestaurantRepository {

    public static final String J_NAME = "name";
    public static final String J_LAT = "latitude";
    public static final String J_LON = "longitude";
    public static final String J_STREET = "street";
    public static final String J_LOCALITY = "locality";

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





    public void populate(JSONArray jsonArray){

        JSONObject jsonObject;
        mDatabase = mDbHelper.getWritableDatabase();
        mDatabase.beginTransaction();

        String sqlDelete = "DELETE FROM " + RestaurantDbSchema.TABLE_NAME;
        mDatabase.execSQL(sqlDelete);

        String sql = "INSERT INTO " + RestaurantDbSchema.TABLE_NAME + " (" +
                        RestaurantDbSchema.C_NAME + ", " +
                        RestaurantDbSchema.C_LATITUDE + ", " +
                        RestaurantDbSchema.C_LONGITUDE +  ", " +
                        RestaurantDbSchema.C_LOCALITY + ", " +
                        RestaurantDbSchema.C_STREET + ") " +
                        "VALUES (?, ?, ?, ?, ?)";

        SQLiteStatement statement = mDatabase.compileStatement(sql);

        for(int i = 0; i <= jsonArray.length(); i++) {

            try {
                jsonObject = jsonArray.getJSONObject(i);

                statement.bindString(1, jsonObject.getString(J_NAME));
                statement.bindDouble(2, jsonObject.getDouble(J_LAT));
                statement.bindDouble(3, jsonObject.getDouble(J_LON));
                statement.bindString(4, jsonObject.getString(J_LOCALITY));
                statement.bindString(5, jsonObject.getString(J_STREET));
                statement.execute();
                statement.clearBindings();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

        mDatabase.close();
        statement.close();
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
