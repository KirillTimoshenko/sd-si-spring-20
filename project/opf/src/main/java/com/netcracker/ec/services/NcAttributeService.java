package com.netcracker.ec.services;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcAttributeTypeDef;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NcAttributeService {
    private static final DbWorker dbWorker = DbWorker.getInstance();
    private Connection connection;

    public NcAttributeService () {
        this.connection = dbWorker.getConnection();
    }

    public List<NcAttribute> getAttributesByOrderType(NcObjectType objectType) {
        List<NcAttribute> attributes = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "select a.attr_id, ao.object_type_id," +
                            " a.name, a.attr_type_def_id, d.type " +
                            "from nc_attr_object_types ao " +
                            "inner join nc_attributes a " +
                            "on ao.attr_id = a.attr_id " +
                            "inner join nc_attr_type_defs d " +
                            "on a.attr_type_def_id = d.attr_type_def_id " +
                            "where ao.object_type_id = 2 or ao.object_type_id = ?;");

            ps.setInt(1, objectType.getId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                attributes.add(
                        new NcAttribute(
                                resultSet.getInt("attr_id"),
                                resultSet.getString("name"),
                                new NcAttributeTypeDef(
                                        resultSet.getInt("attr_type_def_id"),
                                        resultSet.getInt("type"),
                                        null
                                )
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attributes;
    }
}