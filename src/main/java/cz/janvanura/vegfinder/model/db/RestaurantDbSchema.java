package cz.janvanura.vegfinder.model.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jan on 28. 3. 2015.
 */
public abstract class RestaurantDbSchema implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(RestaurantProvider.CONTENT_URI, "restaurants");
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.cz.janvanura.vegfinder_restaurants";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.cz.janvanura.vegfinder_restaurants";
    public static final String TABLE_NAME = "restaurants";
    public static final String C_NAME = "name";
    public static final String C_LATITUDE = "latitude";
    public static final String C_LONGITUDE = "longitude";
    public static final String C_STREET = "street";
    public static final String C_LOCALITY = "locality";

    public static final String J_ID = "id";
    public static final String J_NAME = "name";
    public static final String J_LAT = "latitude";
    public static final String J_LON = "longitude";
    public static final String J_STREET = "street";
    public static final String J_LOCALITY = "locality";

    public static final String DATA_URL = "http://www.veganstvi.net/vegfinder";
}
