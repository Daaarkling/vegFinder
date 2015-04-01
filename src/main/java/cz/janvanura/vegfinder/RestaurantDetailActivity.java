package cz.janvanura.vegfinder;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import cz.janvanura.vegfinder.model.repository.RestaurantRepository;


public class RestaurantDetailActivity extends BaseActivity {

    private RestaurantRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail_activity);

        RestaurantDetailFragment detailFragment = (RestaurantDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        Bundle bundle = getIntent().getExtras();
        int rowId = bundle.getInt(RestaurantDetailFragment.ROW_ID);
        detailFragment.updateContent(rowId);
    }

}
