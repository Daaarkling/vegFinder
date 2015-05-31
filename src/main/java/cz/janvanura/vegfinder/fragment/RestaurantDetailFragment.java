package cz.janvanura.vegfinder.fragment;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cz.janvanura.vegfinder.R;
import cz.janvanura.vegfinder.activity.RestaurantsMapActivity;
import cz.janvanura.vegfinder.model.db.RestaurantDbSchema;

/**
 * Created by Jan on 1. 4. 2015.
 */
public class RestaurantDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ROW_ID = "rowId";
    public static final String MAP_POS_LAT = "lat";
    public static final String MAP_POS_LON = "lon";
    public static final int LOADER_DETAIL = 1;

    private Button mBtnNav;
    private Button mBtnMap;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.restaurant_detail_fragment, container, false);

        mBtnNav = (Button) v.findViewById(R.id.detail_btn_nav);
        mBtnMap = (Button) v.findViewById(R.id.detail_btn_map);

        return v;
    }


    public void updateContent(int rowId) {

        Bundle bundle = new Bundle();
        bundle.putInt(ROW_ID, rowId);
        getLoaderManager().restartLoader(LOADER_DETAIL, bundle, this);
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

        String nameString = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_NAME));
        name.setText(nameString);
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


        String addressString = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_STREET)) + ", " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_POSTAL_CODE)) + ", " + cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_LOCALITY));
        address.setText(addressString);

        String openingString = cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_OPENING));
        openingString = openingString.replace(" || ", "<br>");
        opening.setText(Html.fromHtml(openingString));


        byte[] bytes = Base64.decode(cursor.getString(cursor.getColumnIndex(RestaurantDbSchema.C_IMAGE)), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);


        float lat = cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LATITUDE));
        float lon = cursor.getFloat(cursor.getColumnIndex(RestaurantDbSchema.C_LONGITUDE));

        setUpBtn(lat, lon);
    }


    private void setUpBtn(final float lat, final float lon){

        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RestaurantsMapActivity.class);
                intent.putExtra(MAP_POS_LAT, lat);
                intent.putExtra(MAP_POS_LON, lon);
                startActivity(intent);
            }
        });


        mBtnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "," + lon));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
