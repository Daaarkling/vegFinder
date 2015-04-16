package cz.janvanura.vegfinder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.db.RestaurantProvider;
import cz.janvanura.vegfinder.model.json.JSONLoader;

/**
 * Created by Jan on 16. 4. 2015.
 */
public class PopulateService extends IntentService {

    public static final String NAME = "PopulateService";
    public static final int POPULATE_SERVICE_NOTIFICATION = 1;

    private NotificationManager mNotificationManager;
    private Notification.Builder mBuilder;



    public PopulateService() {
        super(NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle(getResources().getString(R.string.populate_progress_title))
                .setContentText(getResources().getString(R.string.populate_progress))
                .setSmallIcon(R.drawable.ic_launcher)
                .setProgress(0, 0, true)
                .setOngoing(true);

        mNotificationManager.notify(POPULATE_SERVICE_NOTIFICATION, mBuilder.build());

        mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle(getResources().getString(R.string.populate_progress_title))
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);

        if(populate()) {
            mBuilder.setContentText(getResources().getString(R.string.populate_success));
        } else {
            mBuilder.setContentText(getResources().getString(R.string.populate_error));
        }

        mNotificationManager.notify(POPULATE_SERVICE_NOTIFICATION, mBuilder.build());
    }


    private Boolean populate() {

        try {

            ArrayList<ContentProviderOperation> batchOps = new ArrayList<>();
            ContentValues values;
            JSONArray jsonArray = JSONLoader.getJSONFromUrl(RestaurantDbSchema.DATA_URL);
            JSONObject jsonObject;

            // delete old values
            batchOps.add(ContentProviderOperation.newDelete(RestaurantDbSchema.CONTENT_URI).build());
            batchOps.add(ContentProviderOperation.newDelete(RestaurantDbSchema.Search.CONTENT_URI).build());

            for(int i = 0; i <= jsonArray.length()-1; i++) {

                jsonObject = jsonArray.getJSONObject(i);

                values = new ContentValues();
                values.put(RestaurantDbSchema._ID, jsonObject.getLong(RestaurantDbSchema.J_ID));
                values.put(RestaurantDbSchema.C_NAME, jsonObject.getString(RestaurantDbSchema.J_NAME));
                values.put(RestaurantDbSchema.C_IMAGE, jsonObject.getString(RestaurantDbSchema.J_IMAGE));
                values.put(RestaurantDbSchema.C_LATITUDE, jsonObject.getDouble(RestaurantDbSchema.J_LAT));
                values.put(RestaurantDbSchema.C_LONGITUDE, jsonObject.getDouble(RestaurantDbSchema.J_LON));
                values.put(RestaurantDbSchema.C_LOCALITY, jsonObject.getString(RestaurantDbSchema.J_LOCALITY));
                values.put(RestaurantDbSchema.C_STREET, jsonObject.getString(RestaurantDbSchema.J_STREET));
                values.put(RestaurantDbSchema.C_OPENING, jsonObject.getString(RestaurantDbSchema.J_OPENING));

                batchOps.add(ContentProviderOperation.newInsert(RestaurantDbSchema.CONTENT_URI).withValues(values).build());
                values.clear();

                values.put(RestaurantDbSchema.Search._ID, jsonObject.getLong(RestaurantDbSchema.J_ID));
                values.put(SearchManager.SUGGEST_COLUMN_TEXT_1, jsonObject.getString(RestaurantDbSchema.J_NAME));
                values.put(SearchManager.SUGGEST_COLUMN_TEXT_2, jsonObject.getString(RestaurantDbSchema.J_LOCALITY));
                values.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, jsonObject.getLong(RestaurantDbSchema.J_ID));

                batchOps.add(ContentProviderOperation.newInsert(RestaurantDbSchema.Search.CONTENT_URI).withValues(values).build());
                values.clear();
            }

            getContentResolver().applyBatch(RestaurantProvider.AUTHORITY, batchOps);
            return true;

        } catch (IOException e) {
            Log.e("error1", e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e("error2", e.getMessage());
            return false;
        } catch (RemoteException e) {
            Log.e("error3", e.getMessage());
            return false;
        } catch (OperationApplicationException e) {
            Log.e("error4", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
