package cz.janvanura.vegfinder.model.db;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jan on 6. 4. 2015.
 */
public class RestaurantSuggestionProvider extends ContentProvider {

    private RestaurantDbHelper mRestaurantDbHelper;
    private Map<String, String> projectionMap = new HashMap<>();

    @Override
    public boolean onCreate() {

        mRestaurantDbHelper = new RestaurantDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mRestaurantDbHelper.getReadableDatabase();
        Cursor c = null;

        if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0) {

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(RestaurantDbSchema.TABLE_NAME);

            projectionMap.put(RestaurantDbSchema.C_NAME, RestaurantDbSchema.C_NAME + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
            projectionMap.put(RestaurantDbSchema.C_LOCALITY, RestaurantDbSchema.C_LOCALITY + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_2);
            projectionMap.put(RestaurantDbSchema._ID, RestaurantDbSchema._ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);

            qb.setProjectionMap(projectionMap);

            c = qb.query(db, null, selection, selectionArgs, null, null, null);
            c.moveToFirst();
        } else {
            return null;
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
