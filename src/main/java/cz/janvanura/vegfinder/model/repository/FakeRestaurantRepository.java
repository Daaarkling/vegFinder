package cz.janvanura.vegfinder.model.repository;

import java.util.ArrayList;
import java.util.List;

import cz.janvanura.vegfinder.model.entity.Restaurant;

/**
 * Created by Jan on 25. 3. 2015.
 */
public class FakeRestaurantRepository implements IRestaurantRepository {

    @Override
    public Restaurant findById(Integer id) {
        return new Restaurant(1, "Fake restaurace", "Kocourkov", "Neznámá 10", 50.075894f, 14.432354f);
    }

    @Override
    public List<Restaurant> findAll() {

        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant(1, "Fake restaurace", "Kocourkov", "Neznámá 10", 50.075894f, 14.432354f));
        restaurants.add(new Restaurant(1, "Fake restaurace", "Kocourkov", "Neznámá 10", 50.075894f, 14.432354f));
        restaurants.add(new Restaurant(1, "Fake restaurace", "Kocourkov", "Neznámá 10", 50.075894f, 14.432354f));
        restaurants.add(new Restaurant(1, "Fake restaurace", "Kocourkov", "Neznámá 10", 50.075894f, 14.432354f));

        return restaurants;
    }
}
