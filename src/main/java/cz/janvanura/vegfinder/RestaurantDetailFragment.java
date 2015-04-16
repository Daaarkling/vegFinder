package cz.janvanura.vegfinder;


import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
        TextView opening = (TextView) getView().findViewById(R.id.detail_opening);
        ImageView imageView = (ImageView) getView().findViewById(R.id.detail_image);

        name.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME)));
        address.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_STREET)) + ", " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_LOCALITY)));
        opening.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_OPENING)));

        byte[] bytes = Base64.decode(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_IMAGE)), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }


}
