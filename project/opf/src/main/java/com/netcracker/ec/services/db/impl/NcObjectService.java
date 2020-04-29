package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NcObjectService {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public NcObjectService() {
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();

        try {
            String sqlQuery = "select o.object_id, o.name, o.object_type_id, t.parent_id " +
                    "from nc_objects o " +
                    "inner join nc_object_types t " +
                    "on o.object_type_id = t.object_type_id " +
                    "where t.parent_id = 2;";
            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            while (resultSet.next()) {
                orders.add(
                        new Order(
                                resultSet.getInt("object_id"),
                                resultSet.getString("name"),
                                new NcObjectType(
                                        resultSet.getInt("object_type_id"),
                                        null,
                                        null
                                )
                        )
                );
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersByObjectTypeId(Integer id) {
        List<Order> orders = new ArrayList<>();

        try {
            String sqlQuery = String.format("select o.object_id, o.name, o.object_type_id, t.parent_id " +
                            "from nc_objects o " +
                            "inner join nc_object_types t " +
                            "on o.object_type_id = t.object_type_id " +
                            "where t.parent_id = 2 " +
                            "and o.object_type_id = %d;", id);

            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            while (resultSet.next()) {
                orders.add(
                        new Order(
                                resultSet.getInt("object_id"),
                                resultSet.getString("name"),
                                new NcObjectType(
                                        resultSet.getInt("object_type_id"),
                                        null,
                                        null
                                )
                        )
                );
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public int getOrderCountByOrderType(int orderTypeId) {
        int count = 0;
        try {
            String sqlQuery = String.format(
                    "select count(*) " +
                            "from nc_objects " +
                            "where object_type_id = %d;",
                    orderTypeId);

            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            resultSet.next();
            count = resultSet.getInt(1);
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public Order insertOrder(Order order) {

        try {
            String sqlQuery = String.format("insert into nc_objects values(%d, '%s', %d, null);",
                    order.getId(),
                    order.getName(),
                    order.getObjectType().getId());
            dbWorker.executeInsert(sqlQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    private Integer getLastId() {
        Integer id = null;
        try {
            String sqlQuery = "select last_insert_id();";
            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            resultSet.next();
            id = resultSet.getInt(1);
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
