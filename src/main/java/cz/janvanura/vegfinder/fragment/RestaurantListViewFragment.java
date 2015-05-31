package cz.janvanura.vegfinder.fragment;


import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import cz.janvanura.vegfinder.R;
import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;
import cz.janvanura.vegfinder.model.db.RestaurantProvider;

/**
 * Created by Jan on 25. 3. 2015.
 */
public class RestaurantListViewFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ALL = 0;
    private static final int LOADER_SEARCH = 1;
    private static final String SEARCH_KEY = "search";

    private ResourceCursorAdapter mAdapterAll;
    private ResourceCursorAdapter mAdapterSearch;
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

        getLoaderManager().initLoader(LOADER_ALL, null, this);

        mAdapterAll = new ResourceCursorAdapter(getActivity(), R.layout.restaurant_list_item, null, 0) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                if(cursor != null) {
                    TextView name = (TextView) view.findViewById(R.id.list_item_name);
                    TextView address = (TextView) view.findViewById(R.id.list_item_address);
                    ImageView imageView = (ImageView) view.findViewById(R.id.list_item_image);

                    byte[] bytes = Base64.decode(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_IMAGE)), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);

                    name.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME)));
                    address.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_STREET)) + ", " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_LOCALITY)));
                }
            }
        };

        setListAdapter(mAdapterAll);
    }


    public void doSearch(String s) {

        mAdapterSearch = new ResourceCursorAdapter(getActivity(), R.layout.restaurant_list_item, null, 0) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                if(cursor != null) {
                    TextView name = (TextView) view.findViewById(R.id.list_item_name);
                    TextView address = (TextView) view.findViewById(R.id.list_item_address);
                    ImageView imageView = (ImageView) view.findViewById(R.id.list_item_image);

                    byte[] bytes = Base64.decode(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_IMAGE)), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);

                    name.setText(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));
                    address.setText(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)));
                }
            }
        };

        setListAdapter(mAdapterSearch);

        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_KEY, s);

        getLoaderManager().destroyLoader(LOADER_ALL);
        getLoaderManager().initLoader(LOADER_SEARCH, bundle, this);
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

                return new CursorLoader(
                        getActivity(),
                        RestaurantDbSchema.CONTENT_URI,
                        new String[]{RestaurantDbSchema._ID, RestaurantDbSchema.C_NAME, RestaurantDbSchema.C_STREET, RestaurantDbSchema.C_LOCALITY, RestaurantDbSchema.C_IMAGE},
                        null,
                        null,
                        RestaurantDbSchema.C_NAME
                );

            case LOADER_SEARCH:

                String query = null;
                if(bundle != null) {
                    query = bundle.getString(SEARCH_KEY);
                }

                Uri uri = Uri.withAppendedPath(RestaurantDbSchema.Search.CONTENT_URI, query);
                return new CursorLoader(
                        getActivity(),
                        uri,
                        null,
                        null,
                        null,
                        null
                );

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        switch (cursorLoader.getId()) {
            case LOADER_ALL:
                mAdapterAll.changeCursor(cursor);
                break;
            case LOADER_SEARCH:
                mAdapterSearch.changeCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        switch (cursorLoader.getId()) {
            case LOADER_ALL:
                mAdapterAll.changeCursor(null);
                break;
            case LOADER_SEARCH:
                mAdapterSearch.changeCursor(null);
                break;
        }
    }



    public interface OnItemClick {

        public void itemClick(int rowId);
    }
}
