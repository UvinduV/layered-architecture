package lk.ijse.layeredarchitecture.dto;

public class OrderItemQueryDTO {
    private String date;
    private String itemcode;
    private String description;
    private String qty;
    private String unitprice;

    public OrderItemQueryDTO() {
    }

    public OrderItemQueryDTO(String date, String itemcode, String description, String qty, String unitprice) {

        this.date = date;
        this.itemcode = itemcode;
        this.description = description;
        this.qty = qty;
        this.unitprice = unitprice;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                " date='" + date + '\'' +
                ", itemcode='" + itemcode + '\'' +
                ", description='" + description + '\'' +
                ", qty='" + qty + '\'' +
                ", unitprice='" + unitprice + '\'' +
                '}';
    }
}
