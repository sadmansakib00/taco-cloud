package com.shop.tacocloud.repositories;

import com.shop.tacocloud.models.TacoOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    //NOTE: CrudRepository provides the following methods:
    //    TacoOrder save(TacoOrder tacoOrder);

    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> findByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate,Date endDate);

//    @Query("Order o where o.deliveryCity='Seattle'")
//    List<TacoOrder> findByDeliveredInSeattle();

}
