package cz.janvanura.vegfinder.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import cz.janvanura.vegfinder.R;
import cz.janvanura.vegfinder.fragment.RestaurantDetailFragment;
import cz.janvanura.vegfinder.fragment.RestaurantListViewFragment;


public class RestaurantMainActivity extends BaseActivity implements RestaurantListViewFragment.OnItemClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_main_activity);


        Intent intent = getIntent();
        if(intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            RestaurantListViewFragment listViewFragment = (RestaurantListViewFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
            listViewFragment.doSearch(query);
        }
        else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            Uri detailUri = intent.getData();
            String id = detailUri.getLastPathSegment();

            showDetail(Integer.parseInt(id));
        }


    }




    @Override
    public void itemClick(int rowId) {

        showDetail(rowId);
    }


    private void showDetail(int rowId){

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
