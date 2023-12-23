package lk.ijse.layeredarchitecture.controller;

import javafx.scene.control.Alert;
import lk.ijse.layeredarchitecture.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.layeredarchitecture.dao.custom.impl.QueryDAOImpl;
import lk.ijse.layeredarchitecture.dto.CustOrderQueryDTO;
import lk.ijse.layeredarchitecture.dto.CustomerDTO;
import lk.ijse.layeredarchitecture.dto.OrderItemQueryDTO;
import lk.ijse.layeredarchitecture.entity.Customer;
import lk.ijse.layeredarchitecture.view.tdm.OrderItemTM;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    CustomerDAOImpl customerDAO= new CustomerDAOImpl();
    QueryDAOImpl jointQueryDAO=new QueryDAOImpl();

    public void initialize(){
        loadAllCustomerIds();
    }
    private void loadAllCustomerIds() {
        try {

            ArrayList<Customer> allCustomers = customerDAO.getAll();
            ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();

            for (Customer customer : allCustomers) {
                customerDTOS.add(new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress()));
            }

            for (CustomerDTO c : customerDTOS) {
                cmbCustomerId.getItems().add(c.getId());
            }


        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/layeredarchitecture/main-form.fxml");
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

            ArrayList<OrderItemQueryDTO>orderItems=jointQueryDAO.searchOrderItem(orderID);
            for (OrderItemQueryDTO oi: orderItems) {
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
