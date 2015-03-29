package cz.janvanura.vegfinder.model.db;

import android.provider.BaseColumns;

/**
 * Created by Jan on 28. 3. 2015.
 */
public abstract class RestaurantDbSchema implements BaseColumns {

    public static final String TABLE_NAME = "restaurants";
    public static final String C_NAME = "name";
    public static final String C_LATITUDE = "latitude";
    public static final String C_LONGITUDE = "longitude";
    public static final String C_STREET = "street";
    public static final String C_LOCALITY = "locality";
}
