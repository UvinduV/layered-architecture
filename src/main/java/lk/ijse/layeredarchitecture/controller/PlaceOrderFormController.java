package lk.ijse.layeredarchitecture.controller;

import lk.ijse.layeredarchitecture.bo.BOFactory;
import lk.ijse.layeredarchitecture.bo.custom.PlaceOrderBo;
import lk.ijse.layeredarchitecture.bo.custom.impl.PlaceOrderBOImpl;
import lk.ijse.layeredarchitecture.dto.CustomerDTO;
import lk.ijse.layeredarchitecture.dto.ItemDTO;
import lk.ijse.layeredarchitecture.dto.OrderDetailDTO;
import lk.ijse.layeredarchitecture.view.tdm.OrderDetailTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public class PlaceOrderFormController {

    public AnchorPane root;
    public JFXButton btnPlaceOrder;
    public TextField txtCustomerName;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public JFXButton btnSave;
    public TableView<OrderDetailTM> tblOrderDetails;
    public TextField txtUnitPrice;
    public JFXComboBox<String> cmbCustomerId;
    public JFXComboBox<String> cmbItemCode;
    public TextField txtQty;
    public Label lblId;
    public Label lblDate;
    public Label lblTotal;
    private String orderId;

    //CustomerDAO customerDAO =new CustomerDAOImpl();
    //ItemDAO itemDAO=new ItemDAOImpl();
    //OrderDAO orderDAO= new OrderDAOImpl();
    PlaceOrderBo placeOrderBO= (PlaceOrderBo) BOFactory.getBoFactory().getBo(BOFactory.BOTypes.PLACE_ORDER);
    //OrderDetailDAO orderDetailDAO=new OrderDetailDAOImpl();

    public void initialize() throws SQLException, ClassNotFoundException {

        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        TableColumn<OrderDetailTM, Button> lastCol = (TableColumn<OrderDetailTM, Button>) tblOrderDetails.getColumns().get(5);

        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");

            btnDelete.setOnAction(event -> {
                tblOrderDetails.getItems().remove(param.getValue());
                tblOrderDetails.getSelectionModel().clearSelection();
                calculateTotal();
                enableOrDisablePlaceOrderButton();
            });

            return new ReadOnlyObjectWrapper<>(btnDelete);
        });

        orderId = generateNewOrderId();
        lblId.setText("Order ID: " + orderId);
        lblDate.setText(LocalDate.now().toString());
        btnPlaceOrder.setDisable(true);
        txtCustomerName.setFocusTraversable(false);
        txtCustomerName.setEditable(false);
        txtDescription.setFocusTraversable(false);
        txtDescription.setEditable(false);
        txtUnitPrice.setFocusTraversable(false);
        txtUnitPrice.setEditable(false);
        txtQtyOnHand.setFocusTraversable(false);
        txtQtyOnHand.setEditable(false);
        txtQty.setOnAction(event -> btnSave.fire());
        txtQty.setEditable(false);
        btnSave.setDisable(true);

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            enableOrDisablePlaceOrderButton();

            if (newValue != null) {
                try {
                    /*Search Customer*/
                    //Connection connection = DBConnection.getDbConnection().getConnection();
                    try {
                        if (!existCustomer(newValue + "")) {
//                            "There is no such customer associated with the id " + id
                            new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + newValue + "").show();
                        }

                        ////CustomerDTO customerDTO=customerDAO.search(newValue);
                        PlaceOrderBo placeOrderBO =new PlaceOrderBOImpl();
                        CustomerDTO customerDTO=placeOrderBO.searchCustomer(newValue);

                        /*PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer WHERE id=?");
                        pstm.setString(1, newValue + "");
                        ResultSet rst = pstm.executeQuery();
                        rst.next();
                        CustomerDTO customerDTO = new CustomerDTO(newValue + "", rst.getString("name"), rst.getString("address"));*/

                        txtCustomerName.setText(customerDTO.getName());
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the customer " + newValue + "" + e).show();
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                txtCustomerName.clear();
            }
        });


        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newItemCode) -> {
            txtQty.setEditable(newItemCode != null);
            btnSave.setDisable(newItemCode == null);

            if (newItemCode != null) {

                /*Find Item*/
                try {
                    if (!existItem(newItemCode + "")) {
//                        throw new NotFoundException("There is no such item associated with the id " + code);
                    }
                    PlaceOrderBo placeOrderBO=new PlaceOrderBOImpl();
                    ItemDTO item=placeOrderBO.searchItem(newItemCode);
                    /*Connection connection = DBConnection.getDbConnection().getConnection();
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
                    pstm.setString(1, newItemCode + "");
                    ResultSet rst = pstm.executeQuery();
                    rst.next();
                    ItemDTO item = new ItemDTO(newItemCode + "", rst.getString("description"), rst.getBigDecimal("unitPrice"), rst.getInt("qtyOnHand"));*/

                    txtDescription.setText(item.getDescription());
                    txtUnitPrice.setText(item.getUnitPrice().setScale(2).toString());

//                    txtQtyOnHand.setText(tblOrderDetails.getItems().stream().filter(detail-> detail.getCode().equals(item.getCode())).<Integer>map(detail-> item.getQtyOnHand() - detail.getQty()).findFirst().orElse(item.getQtyOnHand()) + "");
                    Optional<OrderDetailTM> optOrderDetail = tblOrderDetails.getItems().stream().filter(detail -> detail.getCode().equals(newItemCode)).findFirst();
                    txtQtyOnHand.setText((optOrderDetail.isPresent() ? item.getQtyOnHand() - optOrderDetail.get().getQty() : item.getQtyOnHand()) + "");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                txtDescription.clear();
                txtQty.clear();
                txtQtyOnHand.clear();
                txtUnitPrice.clear();
            }
        });

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrderDetail) -> {

            if (selectedOrderDetail != null) {
                cmbItemCode.setDisable(true);
                cmbItemCode.setValue(selectedOrderDetail.getCode());
                btnSave.setText("Update");
                txtQtyOnHand.setText(Integer.parseInt(txtQtyOnHand.getText()) + selectedOrderDetail.getQty() + "");
                txtQty.setText(selectedOrderDetail.getQty() + "");
            } else {
                btnSave.setText("Add");
                cmbItemCode.setDisable(false);
                cmbItemCode.getSelectionModel().clearSelection();
                txtQty.clear();
            }

        });

        loadAllCustomerIds();
        loadAllItemCodes();
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {

        boolean isExistItem=placeOrderBO.existItem(code);
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT code FROM Item WHERE code=?");
        pstm.setString(1, code);
        return pstm.executeQuery().next();*/
        return isExistItem;
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {

        boolean isExistCustomer=placeOrderBO.existCustomer(id);
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT id FROM Customer WHERE id=?");
        pstm.setString(1, id);
        return pstm.executeQuery().next();*/
        return isExistCustomer;
    }

    public String generateNewOrderId() {
        try {

            return placeOrderBO.generateNewOrderID();
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT oid FROM `Orders` ORDER BY oid DESC LIMIT 1;");

            return rst.next() ? String.format("OID-%03d", (Integer.parseInt(rst.getString("oid").replace("OID-", "")) + 1)) : "OID-001";*/
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new order id").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "OID-001";
    }

    private void loadAllCustomerIds() {
        try {

            ArrayList<CustomerDTO> allCustomers= placeOrderBO.getAllCustomer();
            for (CustomerDTO c:allCustomers) {
                cmbCustomerId.getItems().add(c.getId());
            }
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");*/

            /*while (rst.next()) {
                cmbCustomerId.getItems().add(rst.getString("id"));
            }*/

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllItemCodes() {
        try {
            /*Get all items*/

            ArrayList<ItemDTO>allItems=placeOrderBO.getAllItem();
            for (ItemDTO i: allItems) {
                cmbItemCode.getItems().add(i.getCode());
            }
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Item");*/
            /*while (rst.next()) {
                cmbItemCode.getItems().add(rst.getString("code"));
            }*/
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/layeredarchitecture/main-form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
        if (!txtQty.getText().matches("\\d+") || Integer.parseInt(txtQty.getText()) <= 0 ||
                Integer.parseInt(txtQty.getText()) > Integer.parseInt(txtQtyOnHand.getText())) {
            new Alert(Alert.AlertType.ERROR, "Invalid qty").show();
            txtQty.requestFocus();
            txtQty.selectAll();
            return;
        }

        String itemCode = cmbItemCode.getSelectionModel().getSelectedItem();
        String description = txtDescription.getText();
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        int qty = Integer.parseInt(txtQty.getText());
        BigDecimal total = unitPrice.multiply(new BigDecimal(qty)).setScale(2);

        boolean exists = tblOrderDetails.getItems().stream().anyMatch(detail -> detail.getCode().equals(itemCode));

        if (exists) {
            OrderDetailTM orderDetailTM = tblOrderDetails.getItems().stream().filter(detail -> detail.getCode().equals(itemCode)).findFirst().get();

            if (btnSave.getText().equalsIgnoreCase("Update")) {
                orderDetailTM.setQty(qty);
                orderDetailTM.setTotal(total);
                tblOrderDetails.getSelectionModel().clearSelection();
            } else {
                orderDetailTM.setQty(orderDetailTM.getQty() + qty);
                total = new BigDecimal(orderDetailTM.getQty()).multiply(unitPrice).setScale(2);
                orderDetailTM.setTotal(total);
            }
            tblOrderDetails.refresh();
        } else {
            tblOrderDetails.getItems().add(new OrderDetailTM(itemCode, description, qty, unitPrice, total));
        }
        cmbItemCode.getSelectionModel().clearSelection();
        cmbItemCode.requestFocus();
        calculateTotal();
        enableOrDisablePlaceOrderButton();
    }

    private void calculateTotal() {
        BigDecimal total = new BigDecimal(0);

        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            total = total.add(detail.getTotal());
        }
        lblTotal.setText("Total: " +total);
    }

    private void enableOrDisablePlaceOrderButton() {
        btnPlaceOrder.setDisable(!(cmbCustomerId.getSelectionModel().getSelectedItem() != null && !tblOrderDetails.getItems().isEmpty()));
    }

    public void txtQty_OnAction(ActionEvent actionEvent) {
    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) throws SQLException {
        boolean b = saveOrder(orderId, LocalDate.now(), cmbCustomerId.getValue(),
                tblOrderDetails.getItems().stream().map(tm -> new OrderDetailDTO(tm.getCode(), tm.getQty(), tm.getUnitPrice())).collect(Collectors.toList()));

        if (b) {
            new Alert(Alert.AlertType.INFORMATION, "Order has been placed successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order has not been placed successfully").show();
        }

        orderId = generateNewOrderId();
        lblId.setText("Order Id: " + orderId);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblOrderDetails.getItems().clear();
        txtQty.clear();
        calculateTotal();//
    }

    public boolean saveOrder(String orderId, LocalDate orderDate, String customerId, List<OrderDetailDTO> orderDetails){

        PlaceOrderBo placeOrderBO=new PlaceOrderBOImpl();
        try {
            return placeOrderBO.placeOrder(orderId,orderDate,customerId,orderDetails);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*Transaction*/
        /*Connection connection = null;
        try {
            connection = DBConnection.getDbConnection().getConnection();
            *//*PreparedStatement stm = connection.prepareStatement("SELECT oid FROM `Orders` WHERE oid=?");
            stm.setString(1, orderId);*//*
            *//*if order id already exist*//*

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
            *//*stm = connection.prepareStatement("INSERT INTO `Orders` (oid, date, customerID) VALUES (?,?,?)");
            stm.setString(1, orderId);
            stm.setDate(2, Date.valueOf(orderDate));
            stm.setString(3, customerId);*//*

            *//*if (stm.executeUpdate() != 1) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }*//*


            boolean isSaveOrderDetail=orderDetailDAO.saveOrderDetail(orderId,orderDetails,connection);
            if (!isSaveOrderDetail) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
            *//* stm = connection.prepareStatement("INSERT INTO OrderDetails (oid, itemCode, unitPrice, qty) VALUES (?,?,?,?)");

            for (OrderDetailDTO detail : orderDetails) {
                stm.setString(1, orderId);
                stm.setString(2, detail.getItemCode());
                stm.setBigDecimal(3, detail.getUnitPrice());
                stm.setInt(4, detail.getQty());

                if (stm.executeUpdate() != 1) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }*//*
            for (OrderDetailDTO detail : orderDetails) { //
//                //Search & Update Item
                ItemDTO item = findItem(detail.getItemCode());
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());


                boolean itemQTY=itemDAO.updateItem(item,connection);
                *//*PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?");
                pstm.setString(1, item.getDescription());
                pstm.setBigDecimal(2, item.getUnitPrice());
                pstm.setInt(3, item.getQtyOnHand());
                pstm.setString(4, item.getCode());

                if (!(pstm.executeUpdate() > 0)) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }*//*
                if(!itemQTY){
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;*/
    }



    /////find item method fully pass to the palce order bo/////
    /*public ItemDTO findItem(String code) {
        try {

            return itemDAO.findItem(code);
            *//*Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
            pstm.setString(1, code);
            ResultSet rst = pstm.executeQuery();
            rst.next();
            return new ItemDTO(code, rst.getString("description"), rst.getBigDecimal("unitPrice"), rst.getInt("qtyOnHand"));*//*
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Item " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/


}