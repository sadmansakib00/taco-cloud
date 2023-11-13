package com.shop.tacocloud.repositories;

import com.shop.tacocloud.models.Ingredient;
import com.shop.tacocloud.models.IngredientRef;
import com.shop.tacocloud.models.Taco;
import com.shop.tacocloud.models.TacoOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Author: sakib
 * Date: 30/10/2023
 * Description:
 */

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {
        PreparedStatementCreatorFactory pscf =
            new PreparedStatementCreatorFactory(
                "insert into Taco_Order "
                        + "(delivery_name, delivery_street, delivery_city, "
                        + "delivery_state, delivery_zip, cc_number, "
                        + "cc_expiration, cc_cvv, placed_at) "
                        + "values (?,?,?,?,?,?,?,?,?)",
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
            );
        pscf.setReturnGeneratedKeys(true);

        tacoOrder.setPlacedAt(new Date());
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                tacoOrder.getDeliveryName(),
                                tacoOrder.getDeliveryStreet(),
                                tacoOrder.getDeliveryCity(),
                                tacoOrder.getDeliveryState(),
                                tacoOrder.getDeliveryZip(),
                                tacoOrder.getCcNumber(),
                                tacoOrder.getCcExpiration(),
                                tacoOrder.getCcCVV(),
                                tacoOrder.getPlacedAt()
                        )
                );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        tacoOrder.setId(orderId);
        List<Taco> tacos = tacoOrder.getTacos();
        int i=0;
        for(Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }

        return tacoOrder;
    }

    private long saveTaco(Long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Taco "
                        + "(name, created_at, taco_order, taco_order_key) "
                        + "values (?, ?, ?, ?)",
                        Types.VARCHAR, Types.TIMESTAMP, Types.LONGVARCHAR, Types.INTEGER
                );
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                taco.getCreatedAt(),
                                orderId,
                                orderKey
                        )
                );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());
        return tacoId;
    }

    private void saveIngredientRefs(
            long tacoId, @NotNull @Size(min = 1, message = "You must choose at least 1 ingredient") List<Ingredient> ingredients) {
        int key = 0;
        for(Ingredient ingredient : ingredients) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key) "
                    + "values (?, ?, ?)",
                    ingredient.getId(), tacoId, key++
            );
        }
    }

}
