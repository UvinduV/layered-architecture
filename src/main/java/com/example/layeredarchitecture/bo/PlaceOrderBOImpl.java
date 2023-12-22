package com.example.layeredarchitecture.bo;

import com.example.layeredarchitecture.dao.SqlUtil;
import com.example.layeredarchitecture.dao.custom.CustomerDAO;
import com.example.layeredarchitecture.dao.custom.ItemDAO;
import com.example.layeredarchitecture.dao.custom.OrderDAO;
import com.example.layeredarchitecture.dao.custom.OrderDetailDAO;
import com.example.layeredarchitecture.dao.impl.CustomerDAOImpl;
import com.example.layeredarchitecture.dao.impl.ItemDAOImpl;
import com.example.layeredarchitecture.dao.impl.OrderDAOImpl;
import com.example.layeredarchitecture.dao.impl.OrderDetailDAOImpl;
import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.CustomerDTO;
import com.example.layeredarchitecture.model.ItemDTO;
import com.example.layeredarchitecture.model.OrderDetailDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBo {
    CustomerDAO customerDAO =new CustomerDAOImpl();
    ItemDAO itemDAO=new ItemDAOImpl();
    OrderDAO orderDAO= new OrderDAOImpl();
    OrderDetailDAO orderDetailDAO=new OrderDetailDAOImpl();
    @Override
    public boolean placeOrder(String orderId, LocalDate orderDate, String customerId, List<OrderDetailDTO> orderDetails) throws SQLException, ClassNotFoundException {
        /*Transaction*/
        Connection connection = null;

            connection = DBConnection.getDbConnection().getConnection();


            boolean isexistOrder=orderDAO.exist(orderId);
            if (isexistOrder) {

            }
            connection.setAutoCommit(false);

            boolean isSaved=orderDAO.saveOrder(orderId,orderDate,customerId,connection);
            if (!isSaved) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }


            boolean isSaveOrderDetail=orderDetailDAO.saveOrderDetail(orderId,orderDetails,connection);
            if (!isSaveOrderDetail) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            for (OrderDetailDTO detail : orderDetails) { //
//                //Search & Update Item
                ItemDTO item = findItem(detail.getItemCode());
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());


                boolean itemQTY=itemDAO.updateItem(item,connection);

                if(!itemQTY){
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;



    }
    @Override
    public ItemDTO findItem(String code) {
        try {

            return itemDAO.findItem(code);
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
            pstm.setString(1, code);
            ResultSet rst = pstm.executeQuery();
            rst.next();
            return new ItemDTO(code, rst.getString("description"), rst.getBigDecimal("unitPrice"), rst.getInt("qtyOnHand"));*/
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Item " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException {
        return customerDAO.search(newValue);
    }
    @Override
    public ItemDTO searchItem(String newItemCode) throws SQLException, ClassNotFoundException {
        return itemDAO.search(newItemCode);
    }
    @Override
    public boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(code);
    }
    @Override
    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }
    @Override
    public String generateNewOrderID() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewID();
    }
    @Override
    public ArrayList<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {
        return customerDAO.getAll();
    }
    @Override
    public ArrayList<ItemDTO> getAllItem() throws SQLException, ClassNotFoundException {
        return itemDAO.getAll();
    }


}
