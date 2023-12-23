package lk.ijse.layeredarchitecture.dao.custom.impl;

import lk.ijse.layeredarchitecture.dao.SqlUtil;
import lk.ijse.layeredarchitecture.dao.custom.QueryDAO;
import lk.ijse.layeredarchitecture.dto.CustOrderQueryDTO;
import lk.ijse.layeredarchitecture.dto.OrderItemQueryDTO;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public ArrayList<CustOrderQueryDTO> search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.test("select o.oid,c.name from Customer c join Orders o on c.id = o.customerID where o.customerID = ?",id);

        ArrayList<CustOrderQueryDTO> getcustOrders = new ArrayList<>();

        while (rst.next()) {
            CustOrderQueryDTO custOrderQueryDTO = new CustOrderQueryDTO(rst.getString(1), rst.getString(2));
            getcustOrders.add(custOrderQueryDTO);
        }

        return getcustOrders;
    }


   @Override
   public ArrayList<OrderItemQueryDTO> searchOrderItem(String oid) throws SQLException, ClassNotFoundException {
        ResultSet rst=SqlUtil.test("select o.date,od.itemCode,i.description,i.qtyOnHand,i.unitPrice from Orders o\n" +
                "    join OrderDetails od on o.oid = od.oid\n" +
                "    join Item i on od.itemCode = i.code\n" +
                "    where o.oid=?",oid);
        ArrayList<OrderItemQueryDTO> getOrderItem = new ArrayList<>();
        while (rst.next()) {
            OrderItemQueryDTO orderItemDTO = new OrderItemQueryDTO(rst.getString(1),rst.getString(2),rst.getString(3), rst.getString(4),rst.getString(5) );
            getOrderItem.add(orderItemDTO);
        }
        return getOrderItem;

    }
}
