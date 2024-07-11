package com.shop.tacocloud.repositories;

import com.shop.tacocloud.models.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

    //NOTE: CrudRepository provides the following methods:
    //    Iterable<Ingredient> findAll();
    //    Optional<Ingredient> findById(String id);
    //    Ingredient save(Ingredient ingredient);

}
