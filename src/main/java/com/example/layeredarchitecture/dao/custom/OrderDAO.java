package com.example.layeredarchitecture.dao.custom;

import com.example.layeredarchitecture.dao.CrudDAO;
import com.example.layeredarchitecture.model.OrderDTO;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO extends CrudDAO<OrderDTO> {
    //String generateOrderId() throws SQLException, ClassNotFoundException ;
    //boolean existOrder(String orderId,Connection connection) throws SQLException, ClassNotFoundException;
    boolean saveOrder(String orderId, LocalDate orderDate, String customerId, Connection connection) throws SQLException, ClassNotFoundException;
}
