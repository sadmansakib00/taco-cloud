package com.shop.tacocloud.repositories;

import com.shop.tacocloud.models.TacoOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    //NOTE: CrudRepository provides the following methods:
//    TacoOrder save(TacoOrder tacoOrder);

}
