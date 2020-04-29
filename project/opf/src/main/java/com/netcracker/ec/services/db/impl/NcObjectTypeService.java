package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NcObjectTypeService {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public NcObjectTypeService() {
    }

    public List<NcObjectType> getOrdersObjectTypes() {
        List<NcObjectType> objectTypes = new ArrayList<>();

        try {
            String sqlQuery = "select * from nc_object_types where parent_id = 2;";
            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            while (resultSet.next()) {
                objectTypes.add(
                        new NcObjectType(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getInt(3)
                  )
                );
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objectTypes;
    }

    public Map<Integer, String> getOrdersObjectTypeNameMap() {
        List<NcObjectType> objectTypesList = getOrdersObjectTypes();
        Map<Integer, String> objectTypesMap = new HashMap<>();
        objectTypesList.forEach(objectType -> objectTypesMap
                .put(objectType.getId(), objectType.getName()));
        return objectTypesMap;
    }

    public NcObjectType getObjectTypeById(Integer id) {
        NcObjectType objectType = new NcObjectType();

        try {
            String sqlQuery = String.format("select * from nc_object_types where object_type_id = %d;", id);
            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            resultSet.next();

            objectType.setId(resultSet.getInt(1));
            objectType.setName(resultSet.getString(2));
            objectType.setParentId(resultSet.getInt(3));

            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objectType;
    }
}
