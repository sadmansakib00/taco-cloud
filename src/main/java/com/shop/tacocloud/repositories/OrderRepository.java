package com.shop.tacocloud.repositories;

import com.shop.tacocloud.models.TacoOrder;

public interface OrderRepository {

    TacoOrder save(TacoOrder tacoOrder);

}
