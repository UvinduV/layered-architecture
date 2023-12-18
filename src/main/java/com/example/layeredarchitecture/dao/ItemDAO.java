package com.example.layeredarchitecture.dao;

import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.ItemDTO;

import java.sql.*;
import java.util.ArrayList;

public interface ItemDAO {
    ArrayList<ItemDTO> getAllItem() throws SQLException, ClassNotFoundException ;
    boolean deleteItem(String code) throws SQLException, ClassNotFoundException ;
    boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException ;
    boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException ;
    boolean existItem(String code) throws SQLException, ClassNotFoundException;
    String  generateItemId() throws SQLException, ClassNotFoundException;
    ItemDTO searchItem(String newItemCode) throws SQLException, ClassNotFoundException;
    ItemDTO findItem(String code) throws SQLException, ClassNotFoundException ;
    boolean updateItem(ItemDTO item, Connection connection) throws SQLException ;
}
