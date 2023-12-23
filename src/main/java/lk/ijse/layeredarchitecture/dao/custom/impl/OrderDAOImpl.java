package lk.ijse.layeredarchitecture.dao.custom.impl;

import lk.ijse.layeredarchitecture.dao.SqlUtil;
import lk.ijse.layeredarchitecture.dao.custom.OrderDAO;
import lk.ijse.layeredarchitecture.dto.OrderDTO;
import lk.ijse.layeredarchitecture.entity.Customer;
import lk.ijse.layeredarchitecture.entity.Order;


import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT oid FROM `Orders` ORDER BY oid DESC LIMIT 1;");*/
        ResultSet rst= SqlUtil.test("SELECT oid FROM `Orders` ORDER BY oid DESC LIMIT 1;");

        return rst.next() ? String.format("OID-%03d", (Integer.parseInt(rst.getString("oid").replace("OID-", "")) + 1)) : "OID-001";
    }

    @Override
    public Order search(String newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<Order> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Order entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Order entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException {
        /*PreparedStatement stm = connection.prepareStatement("SELECT oid FROM `Orders` WHERE oid=?");
        stm.setString(1, orderId);*/
        ResultSet rst=SqlUtil.test("SELECT oid FROM `Orders` WHERE oid=?",orderId);
        return rst.next();
        /*if (stm.executeQuery().next()) {
           // connection.setAutoCommit(false);
            return true;
        }else {
            //connection.setAutoCommit(false);
            return false;
        }*/
    }
    @Override
    public boolean saveOrder(String orderId, LocalDate orderDate,String customerId) throws SQLException, ClassNotFoundException {
        /*PreparedStatement stm = connection.prepareStatement("INSERT INTO `Orders` (oid, date, customerID) VALUES (?,?,?)");
        stm.setString(1, orderId);
        stm.setDate(2, Date.valueOf(orderDate));
        stm.setString(3, customerId);*/
         return SqlUtil.test("INSERT INTO `Orders` (oid, date, customerID) VALUES (?,?,?)",orderId,Date.valueOf(orderDate),customerId);
       /* if (stm.executeUpdate() != 1) {
           // connection.rollback();
           // connection.setAutoCommit(true);
            return false;
        }else {
            return true;
        }*/

    }
}
