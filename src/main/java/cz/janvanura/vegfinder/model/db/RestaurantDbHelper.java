package cz.janvanura.vegfinder.model.db;

import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jan on 28. 3. 2015.
 */
public class RestaurantDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "restaurantdb.db";
    public static final int VERSION = 9;

    public static final String CREATE_TABLE_RESTAURANT =
            "CREATE TABLE " + RestaurantDbSchema.TABLE_NAME + " (" +
            RestaurantDbSchema._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RestaurantDbSchema.C_NAME + " TEXT, " +
            RestaurantDbSchema.C_IMAGE + " TEXT, " +
            RestaurantDbSchema.C_LATITUDE + " REAL, " +
            RestaurantDbSchema.C_LONGITUDE + " REAL, " +
            RestaurantDbSchema.C_STREET + " TEXT, " +
            RestaurantDbSchema.C_DESCRIPTION + " TEXT, " +
            RestaurantDbSchema.C_WEB + " TEXT, " +
            RestaurantDbSchema.C_TELEPHONE + " TEXT, " +
            RestaurantDbSchema.C_EMAIL + " TEXT, " +
            RestaurantDbSchema.C_MENU + " TEXT, " +
            RestaurantDbSchema.C_LOCALITY + " TEXT, " +
            RestaurantDbSchema.C_POSTAL_CODE + " TEXT, " +
            RestaurantDbSchema.C_COUNTRY + " TEXT, " +
            RestaurantDbSchema.C_FACEBOOK + " TEXT, " +
            RestaurantDbSchema.C_OPENING + " TEXT)";

    public static final String CREATE_TABLE_RESTAURANT_SEARCH =
            "CREATE VIRTUAL TABLE " + RestaurantDbSchema.Search.TABLE_NAME + " USING fts4(" +
            RestaurantDbSchema.Search._ID + ", " +
            RestaurantDbSchema.C_IMAGE + ", " +
            SearchManager.SUGGEST_COLUMN_TEXT_1 + ", " +
            SearchManager.SUGGEST_COLUMN_TEXT_2 + ", " +
            SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID + ")";


    public static final String DROP_TABLE_RESTAURANT = "DROP TABLE IF EXISTS " + RestaurantDbSchema.TABLE_NAME;
    public static final String DROP_TABLE_RESTAURANT_SEARCH = "DROP TABLE IF EXISTS " + RestaurantDbSchema.Search.TABLE_NAME;


    public RestaurantDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_RESTAURANT);
        db.execSQL(CREATE_TABLE_RESTAURANT_SEARCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE_RESTAURANT);
        db.execSQL(DROP_TABLE_RESTAURANT_SEARCH);
        db.execSQL(CREATE_TABLE_RESTAURANT);
        db.execSQL(CREATE_TABLE_RESTAURANT_SEARCH);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }
}
