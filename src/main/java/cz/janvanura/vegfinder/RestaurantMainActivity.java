package cz.janvanura.vegfinder;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class RestaurantMainActivity extends BaseActivity implements RestaurantListViewFragment.OnItemClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_main_activity);
    }


    @Override
    public void itemClick(int rowId) {

        RestaurantDetailFragment detailFragment = (RestaurantDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if(detailFragment == null) {

            Intent intent = new Intent(this, RestaurantDetailActivity.class);
            intent.putExtra(RestaurantDetailFragment.ROW_ID, rowId);
            startActivity(intent);
        } else {
            detailFragment.updateContent(rowId);
        }
    }


}
