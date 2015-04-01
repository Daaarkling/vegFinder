package cz.janvanura.vegfinder;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import cz.janvanura.vegfinder.model.json.JSONLoader;
import cz.janvanura.vegfinder.model.repository.RestaurantRepository;

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


            Thread thread = new Thread(){

                @Override
                public void run() {
                    RestaurantRepository repository = new RestaurantRepository(BaseActivity.this);
                    repository.populate(JSONLoader.getJSONFromUrl("http://www.veganstvi.net/vegfinder"));
                }
            };
            thread.start();


        }

        return super.onOptionsItemSelected(item);
    }
}
