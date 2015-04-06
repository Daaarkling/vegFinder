package cz.janvanura.vegfinder;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.db.RestaurantProvider;
import cz.janvanura.vegfinder.model.json.JSONLoader;

/**
 * Created by Jan on 1. 4. 2015.
 */
public class BaseActivity extends ActionBarActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.update_restaurants) {


            ProgressTask progressTask = new ProgressTask(this);
            progressTask.execute(new String[]{RestaurantDbSchema.DATA_URL});

        }

        return super.onOptionsItemSelected(item);
    }


    protected class ProgressTask extends AsyncTask<String, Void, Boolean>{

        private ProgressDialog mProgressDialog;
        private Context mContext;


        public ProgressTask(Context context) {
            mProgressDialog = new ProgressDialog(context);
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog.setTitle(R.string.populate_progress_title);
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.populate_progress));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {

                ArrayList<ContentProviderOperation> batchOps = new ArrayList<>();
                ContentValues values = new ContentValues();
                JSONArray jsonArray = JSONLoader.getJSONFromUrl(params[0]);
                JSONObject jsonObject;

                // delete old values
                batchOps.add(ContentProviderOperation.newDelete(RestaurantDbSchema.CONTENT_URI).build());

                for(int i = 0; i <= jsonArray.length()-1; i++) {

                    jsonObject = jsonArray.getJSONObject(i);

                    values = new ContentValues();
                    values.put(RestaurantDbSchema._ID, jsonObject.getLong(RestaurantDbSchema.J_ID));
                    values.put(RestaurantDbSchema.C_NAME, jsonObject.getString(RestaurantDbSchema.J_NAME));
                    values.put(RestaurantDbSchema.C_LATITUDE, jsonObject.getDouble(RestaurantDbSchema.J_LAT));
                    values.put(RestaurantDbSchema.C_LONGITUDE, jsonObject.getDouble(RestaurantDbSchema.J_LON));
                    values.put(RestaurantDbSchema.C_LOCALITY, jsonObject.getString(RestaurantDbSchema.J_LOCALITY));
                    values.put(RestaurantDbSchema.C_STREET, jsonObject.getString(RestaurantDbSchema.J_STREET));

                    batchOps.add(ContentProviderOperation.newInsert(RestaurantDbSchema.CONTENT_URI)
                                   .withValues(values).build());

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

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            final boolean b = aBoolean;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();

                    if(b) {
                        Toast.makeText(mContext, R.string.populate_success, Toast.LENGTH_SHORT);
                    } else  {
                        Toast.makeText(mContext, R.string.populate_error, Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }
}
