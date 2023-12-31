package lk.ijse.layeredarchitecture.bo.custom.impl;

import lk.ijse.layeredarchitecture.bo.custom.ItemBO;
import lk.ijse.layeredarchitecture.dao.DAOFactory;
import lk.ijse.layeredarchitecture.dao.custom.ItemDAO;
import lk.ijse.layeredarchitecture.dto.ItemDTO;
import lk.ijse.layeredarchitecture.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {
    ItemDAO itemDAO= (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    @Override
    public ArrayList<ItemDTO> getAllItem() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO>itemDTOS =new ArrayList<>();
        ArrayList<Item>items=itemDAO.getAll();
        for (Item item:items) {
            itemDTOS.add(new ItemDTO(item.getCode(),item.getDescription(),item.getUnitPrice(),item.getQtyOnHand()));
        }
        return itemDTOS;
    }
    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException{
        return itemDAO.delete(code);
    }
    @Override
    public boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return itemDAO.save(new Item(dto.getCode(),dto.getDescription(),dto.getUnitPrice(), dto.getQtyOnHand()));
    }
    @Override
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException{
        return itemDAO.update(new Item(dto.getCode(), dto.getDescription(),dto.getUnitPrice(), dto.getQtyOnHand()));
    }
    @Override
    public boolean existItem(String code) throws SQLException, ClassNotFoundException{
        return itemDAO.exist(code);
    }
    @Override
    public String  generateNewItemID() throws SQLException, ClassNotFoundException {
        return  itemDAO.generateNewID();
    }
}
