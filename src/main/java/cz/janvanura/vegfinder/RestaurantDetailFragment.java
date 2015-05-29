package cz.janvanura.vegfinder;


import android.app.SearchManager;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;

/**
 * Created by Jan on 1. 4. 2015.
 */
public class RestaurantDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ROW_ID = "rowId";
    public static final int LOADER_DETAIL = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.restaurant_detail_fragment, container, false);
        return v;
    }


    public void updateContent(int rowId) {

        Bundle bundle = new Bundle();
        bundle.putInt(ROW_ID, rowId);
        getLoaderManager().initLoader(LOADER_DETAIL, bundle, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case LOADER_DETAIL:
                return new CursorLoader(
                        getActivity(),
                        ContentUris.withAppendedId(RestaurantDbSchema.CONTENT_URI, args.getInt(ROW_ID)),
                        null,
                        null,
                        null,
                        RestaurantDbSchema.C_NAME
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        switch (loader.getId()) {
            case LOADER_DETAIL:
                updateDetail(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    private void updateDetail(Cursor cursor) {

        cursor.moveToFirst();

        TextView name = (TextView) getView().findViewById(R.id.detail_name);
        TextView desc = (TextView) getView().findViewById(R.id.detail_desc);
        TextView web  = (TextView) getView().findViewById(R.id.detail_web);
        TextView email = (TextView) getView().findViewById(R.id.detail_email);
        TextView menu  = (TextView) getView().findViewById(R.id.detail_menu);
        TextView address = (TextView) getView().findViewById(R.id.detail_address);
        TextView opening = (TextView) getView().findViewById(R.id.detail_opening);
        ImageView imageView = (ImageView) getView().findViewById(R.id.detail_image);

        name.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME)));
        desc.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_DESCRIPTION)));


        String webString = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_WEB));
        String webFinal = "---";
        if (!webString.equals("")) {
            webFinal = "<a href='" + webString + "'>" + webString.replace("http://", "").trim() + "</a>";
        }
        web.setMovementMethod(LinkMovementMethod.getInstance());
        web.setText(Html.fromHtml(webFinal));

        String emailString = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_EMAIL));
        String emailFinal = "---";
        if (!emailString.equals("")) {
            emailFinal = emailString;
        }
        email.setText(emailFinal);

        String menuString = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_MENU));
        String menuFinal = "---";
        if (!menuString.equals("")) {
            menuFinal = "<a href='" + menuString + "'>" + getResources().getString(R.string.link) + "</a>";
        }
        menu.setMovementMethod(LinkMovementMethod.getInstance());
        menu.setText(Html.fromHtml(menuFinal));


        address.setText(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_STREET)) + " " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_POSTAL_CODE)) + ", " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_LOCALITY)));

        String openingString = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_OPENING));
        openingString = openingString.replace(" || ", "<br>");
        opening.setText(Html.fromHtml(openingString));


        byte[] bytes = Base64.decode(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_IMAGE)), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }

}
