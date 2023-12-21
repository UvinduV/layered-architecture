package com.example.layeredarchitecture.controller;

import com.example.layeredarchitecture.dao.impl.CustomerDAOImpl;
import com.example.layeredarchitecture.dao.impl.JointQueryDAOImpl;
import com.example.layeredarchitecture.model.CustOrderQueryDTO;
import com.example.layeredarchitecture.model.CustomerDTO;
import com.example.layeredarchitecture.model.OrderItemDTO;
import com.example.layeredarchitecture.view.tdm.OrderItemTM;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchOrderFormController {
    @FXML
    private AnchorPane root;
    @FXML
    private JFXComboBox<String> CmbOrderID;
    @FXML
    private JFXComboBox<String> cmbCustomerId;
    @FXML
    private TableView<OrderItemTM> tblOrderDetails;

    @FXML
    private TextField txtOrderDate;

    @FXML
    private TextField txtCustomerName;

    public void initialize(){
        loadAllCustomerIds();
    }
    private void loadAllCustomerIds() {
        try {
            CustomerDAOImpl customerDAO= new CustomerDAOImpl();
            ArrayList<CustomerDTO> allCustomers= customerDAO.getAll();
            for (CustomerDTO c:allCustomers) {
                cmbCustomerId.getItems().add(c.getId());
            }


        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/example/layeredarchitecture/main-form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    public void CustomerIDOnAction(ActionEvent event) {
        String id =cmbCustomerId.getValue();

        try {
            JointQueryDAOImpl jointQueryDAO=new JointQueryDAOImpl();
            ArrayList<CustOrderQueryDTO> dtolist = jointQueryDAO.search(id);

            for (CustOrderQueryDTO c : dtolist) {
                CmbOrderID.getItems().add(c.getOid());
                txtCustomerName.setText(c.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void OrderIDOnAction(ActionEvent event) {
        String orderID= CmbOrderID.getValue();

        try {
            JointQueryDAOImpl jointQueryDAO=new JointQueryDAOImpl();
            ArrayList<OrderItemDTO>orderItems=jointQueryDAO.searchOrderItem(orderID);
            for (OrderItemDTO oi: orderItems) {
                System.out.println(oi.getDate());
                txtOrderDate.setText(oi.getDate());

                double totalPrice= Double.parseDouble(oi.getQty())*Double.parseDouble(oi.getUnitprice());

                tblOrderDetails.getItems().add(new OrderItemTM(oi.getItemcode(), oi.getDescription(), oi.getQty(), oi.getUnitprice(), totalPrice));
            }
            tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
            tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
            tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
            tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
