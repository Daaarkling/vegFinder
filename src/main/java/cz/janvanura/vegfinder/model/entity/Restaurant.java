package cz.janvanura.vegfinder.model.entity;

/**
 * Created by Jan on 25. 3. 2015.
 */
public class Restaurant {

    private int mId;
    private String mName, mLocality, mStreet;
    private float mLatitude, mLongitude;


    public Restaurant() {
    }

    public Restaurant(int id, String name, String locality, String street, float latitude, float longitude) {
        mId = id;
        mName = name;
        mLocality = locality;
        mStreet = street;
        mLatitude = latitude;
        mLongitude = longitude;
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLocality() {
        return mLocality;
    }

    public void setLocality(String locality) {
        mLocality = locality;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        mStreet = street;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    @Override
    public String toString() {
        return mName;
    }
}
