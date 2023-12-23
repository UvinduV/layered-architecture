package lk.ijse.layeredarchitecture.dao.custom;

import lk.ijse.layeredarchitecture.dao.CrudDAO;
import lk.ijse.layeredarchitecture.dto.ItemDTO;
import lk.ijse.layeredarchitecture.entity.Item;

import java.sql.*;

public interface ItemDAO extends CrudDAO<Item> {

    Item findItem(String code) throws SQLException, ClassNotFoundException ;
    boolean updateItem(Item entity,Connection connection) throws SQLException, ClassNotFoundException;
}
