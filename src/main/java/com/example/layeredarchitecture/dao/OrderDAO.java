package com.example.layeredarchitecture.dao;

import com.example.layeredarchitecture.db.DBConnection;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO {
    String generateOrderId() throws SQLException, ClassNotFoundException ;
    boolean existOrder(String orderId,Connection connection) throws SQLException ;
    boolean saveOrder(String orderId, LocalDate orderDate, String customerId, Connection connection) throws SQLException ;
}
