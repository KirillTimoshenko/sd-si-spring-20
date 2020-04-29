package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.services.db.impl.NcAttributeService;
import com.netcracker.ec.services.db.impl.NcObjectService;
import com.netcracker.ec.services.db.impl.NcObjectTypeService;
import com.netcracker.ec.services.db.impl.NcParamsService;
import com.netcracker.ec.services.util.IdGenerator;

import java.sql.SQLException;
import java.util.*;

public class CreateOrderOperation implements Operation {
    private final NcObjectTypeService ncObjectTypeService;
    private final NcAttributeService ncAttributeService;
    private final NcObjectService ncObjectService;
    private final NcParamsService ncParamsService;

    private final Console console = Console.getInstance();

    public CreateOrderOperation() {
        this.ncObjectTypeService = new NcObjectTypeService();
        this.ncAttributeService = new NcAttributeService();
        this.ncObjectService = new NcObjectService();
        this.ncParamsService = new NcParamsService();
    }

    @Override
    public void execute() {
        System.out.println("Please Select Object Type.");

        Map<Integer, String> orderObjectTypeMap = ncObjectTypeService.getOrdersObjectTypeNameMap();
        console.printAvailableOperations(orderObjectTypeMap);

        Integer objectTypeId = console.nextAvailableOperation(orderObjectTypeMap.keySet());

        List<NcAttribute> attributeList = ncAttributeService.getAttributesByOrderTypeId(objectTypeId);

        try {
            Order order = new Order(ncObjectTypeService.getObjectTypeById(objectTypeId));
            order.setId(IdGenerator.generateId());
            order.setName(generateOrderName(order.getObjectType()));
            attributeList.forEach(attr -> order.getParameters()
                    .put(attr, console.getAttributeValue(attr)));

            if (console.getSaveDialogueAnswer()) {
                addOrder(order);
                addOrderParams(order);
                console.printOrderInfo(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addOrder(Order order) {
        ncObjectService.insertOrder(order);
    }

    private void addOrderParams(Order order) {
        ncParamsService.insertParams(order.getParameters(), order.getId());
    }

    private String generateOrderName(NcObjectType objectType) {
        Scanner scanner = new Scanner(objectType.getName());
        return String.join(" ", scanner.next(), scanner.next())
                + " #" + (ncObjectService.getOrderCountByOrderType(objectType.getId())+1);
    }
}
