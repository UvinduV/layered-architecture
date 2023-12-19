package com.example.layeredarchitecture.dao;

import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.ItemDTO;

import java.sql.*;
import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<ItemDTO>{

    ItemDTO findItem(String code) throws SQLException, ClassNotFoundException ;
    boolean updateItem(ItemDTO item, Connection connection) throws SQLException, ClassNotFoundException;
}
