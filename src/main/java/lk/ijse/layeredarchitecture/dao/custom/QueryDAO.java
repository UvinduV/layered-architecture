package lk.ijse.layeredarchitecture.dao.custom;

import lk.ijse.layeredarchitecture.dao.SuperDAO;
import lk.ijse.layeredarchitecture.dto.CustOrderQueryDTO;
import lk.ijse.layeredarchitecture.dto.OrderItemQueryDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<CustOrderQueryDTO> search(String id) throws SQLException, ClassNotFoundException ;

    ArrayList<OrderItemQueryDTO> searchOrderItem(String oid) throws SQLException, ClassNotFoundException ;

}
