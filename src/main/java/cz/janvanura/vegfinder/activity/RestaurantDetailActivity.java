package cz.janvanura.vegfinder.activity;

import android.os.Bundle;

import cz.janvanura.vegfinder.R;
import cz.janvanura.vegfinder.fragment.RestaurantDetailFragment;


public class RestaurantDetailActivity extends BaseActivity {


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
