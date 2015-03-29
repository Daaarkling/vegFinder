package cz.janvanura.vegfinder.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jan on 28. 3. 2015.
 */
public class RestaurantDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "restaurantdb.db";
    public static final int VERSION = 1;

    public static final String CREATE_TABLE_RESTAURANT =
            "CREATE TABLE " + RestaurantDbSchema.TABLE_NAME + " (" +
            RestaurantDbSchema._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RestaurantDbSchema.C_NAME + " TEXT, " +
            RestaurantDbSchema.C_LATITUDE + " REAL, " +
            RestaurantDbSchema.C_LONGITUDE + " REAL, " +
            RestaurantDbSchema.C_STREET + " TEXT, " +
            RestaurantDbSchema.C_LOCALITY + " TEXT" +
            ")";

    public static final String DROP_TABLE_RESTAURANT = "DROP TABLE IF EXISTS " + RestaurantDbSchema.TABLE_NAME;


    public RestaurantDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESTAURANT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_RESTAURANT);
        db.execSQL(CREATE_TABLE_RESTAURANT);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
