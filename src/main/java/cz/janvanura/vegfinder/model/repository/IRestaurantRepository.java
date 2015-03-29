package cz.janvanura.vegfinder.model.repository;

import java.util.List;

import cz.janvanura.vegfinder.model.entity.Restaurant;

/**
 * Created by Jan on 25. 3. 2015.
 */
public interface IRestaurantRepository {

    public Restaurant findById(Integer id);

    public List<Restaurant> findAll();
}
