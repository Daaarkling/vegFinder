package cz.janvanura.vegfinder;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.janvanura.vegfinder.model.repository.DbRestaurantRepository;
import cz.janvanura.vegfinder.model.repository.FakeRestaurantRepository;
import cz.janvanura.vegfinder.model.repository.IRestaurantRepository;
import cz.janvanura.vegfinder.model.entity.Restaurant;

/**
 * Created by Jan on 25. 3. 2015.
 */
public class RestaurantListViewFragment extends ListFragment {

    private IRestaurantRepository mRestaurantRepository;
    private List<Restaurant> mRestaurants;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRestaurantRepository = new DbRestaurantRepository(getActivity());
        mRestaurants = mRestaurantRepository.findAll();

        setListAdapter(new RestaurantAdapter());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private class RestaurantAdapter extends ArrayAdapter<Restaurant> {

        private IRestaurantRepository mDataProvider;

        private RestaurantAdapter() {
            super(getActivity(), 0, mRestaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.restaurant_list_item, null, false);
            }

            Restaurant restaurant = mRestaurants.get(position);
            TextView textView = (TextView) convertView.findViewById(R.id.list_item_name);
            textView.setText(restaurant.getName());

            return convertView;
        }
    }
}
