package cz.janvanura.vegfinder.model.db;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Jan on 5. 4. 2015.
 */
public class RestaurantProvider extends ContentProvider {

    private RestaurantDbHelper mRestaurantDbHelper;
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>();

    private static final int RESTAURANTS = 1;
    private static final int RESTAURANT_ITEM = 2;
    private static final int RESTAURANT_SEARCH = 3;
    private static final int RESTAURANT_SEARCH_NO_PARAM = 4;
    private static final int RESTAURANT_SEARCH_ITEM = 5;

    public static final String AUTHORITY = "cz.janvanura.vegfinder.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, "restaurants", RESTAURANTS);
        sURIMatcher.addURI(AUTHORITY, "restaurants/#", RESTAURANT_ITEM);
        sURIMatcher.addURI(AUTHORITY, "search_suggest_query/*", RESTAURANT_SEARCH);
        sURIMatcher.addURI(AUTHORITY, "search_suggest_query", RESTAURANT_SEARCH_NO_PARAM);
        sURIMatcher.addURI(AUTHORITY, "restaurants_search/#", RESTAURANT_SEARCH_ITEM);
    }




    @Override
    public boolean onCreate() {

        mRestaurantDbHelper = new RestaurantDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sURIMatcher.match(uri)) {
            case RESTAURANTS:
                queryBuilder.setTables(RestaurantDbSchema.TABLE_NAME);
                break;

            case RESTAURANT_SEARCH_ITEM:
            case RESTAURANT_ITEM:

                queryBuilder.setTables(RestaurantDbSchema.TABLE_NAME);
                queryBuilder.appendWhere(RestaurantDbSchema._ID + " = " + uri.getLastPathSegment());
                break;

            case RESTAURANT_SEARCH:

                queryBuilder.setTables(RestaurantDbSchema.Search.TABLE_NAME);
                queryBuilder.appendWhere(
                        SearchManager.SUGGEST_COLUMN_TEXT_1 + " MATCH '*" + uri.getLastPathSegment() + "*'");
                break;

            case RESTAURANT_SEARCH_NO_PARAM:
                return null;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = RestaurantDbSchema._ID;
        }

        SQLiteDatabase database = mRestaurantDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (sURIMatcher.match(uri)){
            case RESTAURANT_SEARCH:
            case RESTAURANTS:
                return RestaurantDbSchema.CONTENT_TYPE;
            case RESTAURANT_SEARCH_ITEM:
            case RESTAURANT_ITEM:
                return RestaurantDbSchema.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase database = mRestaurantDbHelper.getWritableDatabase();
        long id = -1;

        switch (sURIMatcher.match(uri)) {
            case RESTAURANTS:
                id = database.insert(RestaurantDbSchema.TABLE_NAME, null, values);
                break;
            case RESTAURANT_SEARCH_NO_PARAM:
                id = database.insert(RestaurantDbSchema.Search.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);

        }

        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (!isInBatchMode()) {
                getContext().getContentResolver().notifyChange(itemUri, null);
            }
            return itemUri;
        }

        // s.th. went wrong:
        try {
            throw new SQLException("Problem while inserting into uri: " + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }






    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mRestaurantDbHelper.getWritableDatabase();
        int delCount = 0;

        switch (sURIMatcher.match(uri)) {
            case RESTAURANT_SEARCH_NO_PARAM:
                delCount = database.delete(RestaurantDbSchema.Search.TABLE_NAME, selection, selectionArgs);
                break;
            case RESTAURANTS:
                delCount = database.delete(RestaurantDbSchema.TABLE_NAME, selection, selectionArgs);
                break;
            case RESTAURANT_ITEM:
                String idStr = uri.getLastPathSegment();
                String where = RestaurantDbSchema._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                delCount = database.delete(RestaurantDbSchema.TABLE_NAME, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }
        if(delCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;
    }


    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {

        SQLiteDatabase db = mRestaurantDbHelper.getWritableDatabase();
        mIsInBatchMode.set(true);
        db.beginTransaction();
        try {
            final ContentProviderResult[] retResult = super.applyBatch(operations);
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(RestaurantDbSchema.CONTENT_URI, null);
            return retResult;
        }
        finally {
            mIsInBatchMode.remove();
            db.endTransaction();
        }
    }


    private boolean isInBatchMode() {

        return mIsInBatchMode.get() != null && mIsInBatchMode.get();
    }
}
