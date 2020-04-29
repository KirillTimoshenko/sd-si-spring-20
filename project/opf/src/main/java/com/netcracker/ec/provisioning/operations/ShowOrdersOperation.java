package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.services.db.impl.NcObjectService;
import com.netcracker.ec.services.db.impl.NcObjectTypeService;
import com.netcracker.ec.services.db.impl.NcParamsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowOrdersOperation implements Operation {
    private final NcObjectTypeService ncObjectTypeService;
    private final NcObjectService ncObjectService;
    private final NcParamsService ncParamsService;

    private final Console console = Console.getInstance();

    public ShowOrdersOperation() {
        this.ncObjectTypeService = new NcObjectTypeService();
        this.ncObjectService = new NcObjectService();
        this.ncParamsService = new NcParamsService();
    }

    @Override
    public void execute() {
        System.out.println("Please Select Operation.");

        Map<Integer, String> operationModifications = getOperationModificationsMap();
        console.printAvailableOperations(operationModifications);

        Integer operationModification = console.nextAvailableOperation(operationModifications.keySet());
        switch (operationModification) {
            case 1:
                showAllOrders();
                break;
            case 2:
                showOrderOfASpecificObjectType();
                break;
            default:
                break;
        }
    }

    private void showAllOrders() {
        List<Order> orders = ncObjectService.getOrders();

        initOrdersParameters(orders);

        printOrders(orders);
    }

    private void showOrderOfASpecificObjectType() {
        Map<Integer, String> orderObjectTypeMap = ncObjectTypeService.getOrdersObjectTypeNameMap();
        console.printAvailableOperations(orderObjectTypeMap);

        Integer objectTypeId = console.nextAvailableOperation(orderObjectTypeMap.keySet());

        List<Order> orders = ncObjectService.getOrdersByObjectTypeId(objectTypeId);

        initOrdersParameters(orders);

        printOrders(orders);
    }

    private void initOrdersParameters(List<Order> orders) {
        orders.forEach(order -> order.setParameters(
                ncParamsService.getParamsByObjectId(order.getId())));
    }

    private void printOrders(List<Order> orders) {
        orders.forEach(order -> console.printOrderInfo(order));
    }

    private Map<Integer, String> getOperationModificationsMap() {
        Map<Integer, String> operationModifications = new HashMap<>();
        operationModifications.put(1, "Show All Orders");
        operationModifications.put(2, "Show Orders Of A Specific Object Type");
        return operationModifications;
    }
}
