package lk.ijse.layeredarchitecture.dao.custom;

import lk.ijse.layeredarchitecture.dao.CrudDAO;
import lk.ijse.layeredarchitecture.dto.OrderDTO;
import lk.ijse.layeredarchitecture.entity.Order;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO extends CrudDAO<Order> {
    //String generateOrderId() throws SQLException, ClassNotFoundException ;
    //boolean existOrder(String orderId,Connection connection) throws SQLException, ClassNotFoundException;
    boolean saveOrder(String orderId, LocalDate orderDate, String customerId) throws SQLException, ClassNotFoundException;
}
