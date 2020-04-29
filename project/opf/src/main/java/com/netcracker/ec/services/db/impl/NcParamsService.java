package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcAttributeTypeDef;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class NcParamsService {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public NcParamsService() {
    }

    public void insertParams(Map<NcAttribute, String> attributesMap, Integer objectId) {
        attributesMap.forEach((attr, value) -> insertParam(attr, value, objectId));
    }

    private void insertParam(NcAttribute attr, String value, Integer objectId) {

        try {
            String sqlQuery = String.format("insert into nc_params values(%d, %d, null, '%s');",
                    attr.getId(),
                    objectId,
                    value);
            dbWorker.executeInsert(sqlQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<NcAttribute, String> getParamsByObjectId(Integer id) {
        Map<NcAttribute, String> params = new HashMap<>();

        try {
            String sqlQuery = String.format("select a.name, p.attr_id, p.object_id, p.list_value_id, p.value " +
                    "from nc_params p " +
                    "inner join nc_attributes a " +
                    "on a.attr_id = p.attr_id " +
                    "where object_id = %d;",
                    id);
            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            while (resultSet.next()) {
                params.put(
                        new NcAttribute(
                                resultSet.getInt("attr_id"),
                                resultSet.getString("name"),
                                new NcAttributeTypeDef()),
                        resultSet.getString("value")
                );
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return params;
    }
}
