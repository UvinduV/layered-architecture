package com.example.layeredarchitecture.dao;

import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.CustomerDTO;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO{
    @Override
    public ArrayList<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM Customer");*/
        ResultSet rst=SqlUtil.test("SELECT * FROM Customer");

        ArrayList<CustomerDTO>getAllCustomer = new ArrayList<>();
        while (rst.next()) {
            CustomerDTO customerDTO=new CustomerDTO(rst.getString("id"), rst.getString("name"), rst.getString("address"));
            getAllCustomer.add(customerDTO);
        }
        return getAllCustomer;
    }
    @Override
    public boolean saveCustomer(String id,String name,String address) throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer (id,name, address) VALUES (?,?,?)");
        pstm.setString(1, id);
        pstm.setString(2, name);
        pstm.setString(3, address);*/

       boolean isSaved =SqlUtil.test("INSERT INTO Customer (id,name, address) VALUES (?,?,?)",id,name,address);
        //boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;

    }
    @Override
    public boolean updateCustomer(String id,String name,String address) throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET name=?, address=? WHERE id=?");
        pstm.setString(1, name);
        pstm.setString(2, address);
        pstm.setString(3, id);
        boolean isUpdate=pstm.executeUpdate()>0;*/
        boolean isUpdate=SqlUtil.test("UPDATE Customer SET name=?, address=? WHERE id=?",name,address,id);

        return isUpdate;

    }
    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE id=?");
        pstm.setString(1, id);
        return pstm.executeUpdate() >0;*/
        return SqlUtil.test("DELETE FROM Customer WHERE id=?",id);
    }
    @Override
    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
       /* Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT id FROM Customer WHERE id=?");
        pstm.setString(1, id);*/
       // return pstm.executeQuery().next();
        ResultSet rst=SqlUtil.test("SELECT id FROM Customer WHERE id=?",id);
        return rst.next();
    }
    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
       /* Connection connection = DBConnection.getDbConnection().getConnection();
        ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM Customer ORDER BY id DESC LIMIT 1;");*/
       ResultSet rst=SqlUtil.test("SELECT id FROM Customer ORDER BY id DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("id");
            int newCustomerId = Integer.parseInt(id.replace("C00-", "")) + 1;
            return String.format("C00-%03d", newCustomerId);
        } else {
            return "C00-001";
        }
    }
    @Override
    public CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer WHERE id=?");
        pstm.setString(1, newValue + "");
        ResultSet rst = pstm.executeQuery();*/
        ResultSet rst=SqlUtil.test("SELECT * FROM Customer WHERE id=?",newValue);
        rst.next();
        CustomerDTO customerDTO = new CustomerDTO(newValue + "", rst.getString("name"), rst.getString("address"));

        return customerDTO;
    }


}
