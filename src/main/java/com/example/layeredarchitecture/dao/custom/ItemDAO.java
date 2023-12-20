package com.example.layeredarchitecture.dao.custom;

import com.example.layeredarchitecture.dao.CrudDAO;
import com.example.layeredarchitecture.model.ItemDTO;

import java.sql.*;

public interface ItemDAO extends CrudDAO<ItemDTO> {

    ItemDTO findItem(String code) throws SQLException, ClassNotFoundException ;
    boolean updateItem(ItemDTO item, Connection connection) throws SQLException, ClassNotFoundException;
}
