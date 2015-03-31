package cz.janvanura.vegfinder;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.entity.Restaurant;
import cz.janvanura.vegfinder.model.repository.RestaurantRepository;

/**
 * Created by Jan on 25. 3. 2015.
 */
public class RestaurantListViewFragment extends ListFragment {

    private static final int LOADER_ALL = 0;
    private RestaurantRepository mRepository;
    private SimpleCursorAdapter mAdapter;
    private ListView mListView;
    private Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mRepository = new RestaurantRepository(getActivity());
        mCursor = mRepository.findAll();
        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.restaurant_list_item,
                mCursor,
                new String[]{RestaurantDbSchema.C_NAME, RestaurantDbSchema.C_LOCALITY},
                new int[]{R.id.list_item_name, R.id.list_item_address},
                SimpleCursorAdapter.NO_SELECTION
            );

        setListAdapter(mAdapter);
    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Restaurant restaurant = mRepository.findById((int)id);
        Toast.makeText(getActivity(), restaurant.getName(), Toast.LENGTH_SHORT).show();
    }
}
