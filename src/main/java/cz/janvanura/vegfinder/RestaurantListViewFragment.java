package cz.janvanura.vegfinder;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.repository.RestaurantRepository;

/**
 * Created by Jan on 25. 3. 2015.
 */
public class RestaurantListViewFragment extends ListFragment {

    private static final int LOADER_ALL = 0;
    private RestaurantRepository mRepository;
    private SimpleCursorAdapter mAdapter;
    private OnItemClick mOnItemClick;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof OnItemClick) {
            mOnItemClick = (OnItemClick) activity;
        } else {
            throw new ClassCastException("Class must implement OnItemClick interface!");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mRepository = new RestaurantRepository(getActivity());
        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.restaurant_list_item,
                mRepository.findAll(),
                new String[]{RestaurantDbSchema.C_NAME, RestaurantDbSchema.C_LOCALITY},
                new int[]{R.id.list_item_name, R.id.list_item_address},
                SimpleCursorAdapter.NO_SELECTION
        );

        setListAdapter(mAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mOnItemClick.itemClick((int)id);
    }



    public interface OnItemClick {

        public void itemClick(int rowId);
    }
}
