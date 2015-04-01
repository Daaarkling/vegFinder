package cz.janvanura.vegfinder;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.janvanura.vegfinder.model.entity.Restaurant;
import cz.janvanura.vegfinder.model.repository.RestaurantRepository;

/**
 * Created by Jan on 1. 4. 2015.
 */
public class RestaurantDetailFragment extends Fragment {

    public static final String ROW_ID = "rowId";
    private RestaurantRepository mRepository;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.restaurant_detail_fragment, container, false);

        mRepository = new RestaurantRepository(getActivity());

        return v;
    }


    public void updateContent(int rowId) {

        Restaurant restaurant = mRepository.findById(rowId);

        TextView name = (TextView) getView().findViewById(R.id.detail_name);
        TextView address = (TextView) getView().findViewById(R.id.detail_address);

        name.setText(restaurant.getName());
        address.setText(restaurant.getStreet() + ", " + restaurant.getLocality());
    }
}
