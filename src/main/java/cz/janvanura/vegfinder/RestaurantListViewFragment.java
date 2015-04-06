package cz.janvanura.vegfinder;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.db.RestaurantProvider;

/**
 * Created by Jan on 25. 3. 2015.
 */
public class RestaurantListViewFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ALL = 0;
    private static final String SEARCH_KEY = "search";

    private SimpleCursorAdapter mAdapter;
    private OnItemClick mOnItemClick;
    private String mSelectionArg;


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

        getLoaderManager().initLoader(LOADER_ALL, null, this);


        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.restaurant_list_item,
                null,
                new String[]{RestaurantDbSchema.C_NAME, RestaurantDbSchema.C_LOCALITY},
                new int[]{R.id.list_item_name, R.id.list_item_address},
                0
        );

        setListAdapter(mAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mOnItemClick.itemClick((int)id);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        switch (i) {
            case LOADER_ALL:

                String selection = null;
                String[] selectionArg = null;
                if(bundle != null) {
                    String query = bundle.getString(SEARCH_KEY);
                    selection = RestaurantDbSchema.C_NAME + " LIKE ? OR " + RestaurantDbSchema.C_LOCALITY + " LIKE ?";
                    selectionArg = new String[] {query, query};
                }

                return new CursorLoader(
                        getActivity(),
                        RestaurantDbSchema.CONTENT_URI,
                        null,
                        selection,
                        selectionArg,
                        RestaurantDbSchema.C_NAME
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        mAdapter.changeCursor(null);
    }



    public void doSearch(String s) {

        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_KEY, s);
        getLoaderManager().restartLoader(LOADER_ALL, bundle, this);
    }


    public interface OnItemClick {

        public void itemClick(int rowId);
    }
}
