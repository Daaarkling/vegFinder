package cz.janvanura.vegfinder;


import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;

/**
 * Created by Jan on 1. 4. 2015.
 */
public class RestaurantDetailFragment extends Fragment {

    public static final String ROW_ID = "rowId";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.restaurant_detail_fragment, container, false);
        return v;
    }


    public void updateContent(int rowId) {

        Cursor cursor = getActivity().getContentResolver().query(ContentUris.withAppendedId(RestaurantDbSchema.CONTENT_URI, rowId), null, null, null, null);
        cursor.moveToFirst();

        TextView name = (TextView) getView().findViewById(R.id.detail_name);
        TextView address = (TextView) getView().findViewById(R.id.detail_address);

        name.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME)));
        address.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_LOCALITY)));
    }



}
