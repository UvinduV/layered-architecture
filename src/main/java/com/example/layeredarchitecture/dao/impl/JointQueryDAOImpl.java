package com.example.layeredarchitecture.dao.impl;

import com.example.layeredarchitecture.dao.SqlUtil;
import com.example.layeredarchitecture.model.CustOrderQueryDTO;
import com.example.layeredarchitecture.model.OrderItemDTO;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JointQueryDAOImpl {
    public ArrayList<CustOrderQueryDTO> search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.test("select o.oid,c.name from Customer c join Orders o on c.id = o.customerID where o.customerID = ?",id);

        ArrayList<CustOrderQueryDTO> getcustOrders = new ArrayList<>();

        while (rst.next()) {
            CustOrderQueryDTO custOrderQueryDTO = new CustOrderQueryDTO(rst.getString(1), rst.getString(2));
            getcustOrders.add(custOrderQueryDTO);
        }

        return getcustOrders;
    }


    public ArrayList<OrderItemDTO> searchOrderItem(String oid) throws SQLException, ClassNotFoundException {
        ResultSet rst=SqlUtil.test("select o.date,od.itemCode,i.description,i.qtyOnHand,i.unitPrice from Orders o\n" +
                "    join OrderDetails od on o.oid = od.oid\n" +
                "    join Item i on od.itemCode = i.code\n" +
                "    where o.oid=?",oid);
        ArrayList<OrderItemDTO> getOrderItem = new ArrayList<>();
        while (rst.next()) {
            OrderItemDTO orderItemDTO = new OrderItemDTO(rst.getString(1),rst.getString(2),rst.getString(3), rst.getString(4),rst.getString(5) );
            getOrderItem.add(orderItemDTO);
        }
        return getOrderItem;

    }
}
