package com.example.layeredarchitecture.dao.custom;

import com.example.layeredarchitecture.dao.SqlUtil;
import com.example.layeredarchitecture.dao.SuperDAO;
import com.example.layeredarchitecture.model.CustOrderQueryDTO;
import com.example.layeredarchitecture.model.OrderItemDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface JointQueryDAO extends SuperDAO {
    ArrayList<CustOrderQueryDTO> search(String id) throws SQLException, ClassNotFoundException ;

    ArrayList<OrderItemDTO> searchOrderItem(String oid) throws SQLException, ClassNotFoundException ;

}
