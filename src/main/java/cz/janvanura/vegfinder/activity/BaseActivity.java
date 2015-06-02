package cz.janvanura.vegfinder.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.janvanura.vegfinder.service.PopulateService;
import cz.janvanura.vegfinder.R;


/**
 * Created by Jan on 1. 4. 2015.
 */
public class BaseActivity extends ActionBarActivity {



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.update_restaurants) {
            Intent intent = new Intent(this, PopulateService.class);
            startService(intent);

            Toast.makeText(this, R.string.populate_start, Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.map_restaurants) {

            Intent intent = new Intent(this, RestaurantsMapActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.list_restaurants) {

            Intent intent = new Intent(this, RestaurantMainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
