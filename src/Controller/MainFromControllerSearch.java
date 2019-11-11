package Controller;

import DB.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class MainFromControllerSearch {
    public AnchorPane apManageCus;
    public JFXTextField txtSearch_id;
    public TableView <SearchTM>tblList;
    public ImageView picHome_id;

    public Connection connection;
    public JFXButton btnSearchReport;
    public Label home;

    public void initialize(){

        connection = DBConnection.getInstance().getConnection();

        tblList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblList.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        ObservableList<SearchTM> orders = tblList.getItems();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.orderId,o.date,o.id,c.name,SUM(od.quantity*od.unitPrice) AS total FROM OrderDetail od,Orders o,Customer c WHERE o.orderId=od.orderId AND o.id=c.id GROUP BY o.orderId");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String orderId = resultSet.getString(1);
                String date = resultSet.getString(2);
                String cusId = resultSet.getString(3);
                String cusName = resultSet.getString(4);
                double total = Double.parseDouble(resultSet.getString(5));

                SearchTM searchTm = new SearchTM(orderId,date,cusId,cusName,total);
                orders.add(searchTm);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<SearchTM> allOrders = FXCollections.observableArrayList(orders);

//       Listener for search Text

        txtSearch_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String searchText = txtSearch_id.getText();
                ObservableList<SearchTM> tempOrders = FXCollections.observableArrayList();

                for (SearchTM searchOrder: allOrders) {
                    if(searchOrder.getOrderId().contains(searchText)||
                            searchOrder.getDate().contains(searchText)||
                            searchOrder.getCustomerId().contains(searchText)||
                            searchOrder.getCustomerName().contains(searchText)){
                        tempOrders.add(searchOrder);
                    }
                }
                tblList.getItems().setAll(tempOrders);
            }
        });
    }

    public void picHome_OnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("../view/MainFrom.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.apManageCus.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }



//
    public void tblList_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount()==2) {
            URL resource = this.getClass().getResource("/view/MainFromPaceOrder.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent root = fxmlLoader.load();
            Scene placeOrderScene = new Scene(root);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(placeOrderScene);
            secondaryStage.centerOnScreen();
            secondaryStage.setTitle("View Order");
            secondaryStage.setResizable(false);

            MainFromControllerPlaceOrder ctrl = fxmlLoader.getController();
            SearchTM selectedItem = tblList.getSelectionModel().getSelectedItem();

            secondaryStage.showAndWait();

        }
    }
    // what will happen on the if you click on the Search Report Button

    public void btnSearchReport_OnAction(ActionEvent actionEvent) throws JRException {
        //JasperDesign jasperDesign = JRXmlLoader.load(CustomerTM.class.getResourceAsStream("/Report/searchReport.jrxml"));
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Report/searchReport.jasper"));
        //JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String,Object> params = new HashMap<>();
        params.put("searchText",txtSearch_id.getText());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,connection);
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
        ImageView icon = (ImageView) event.getSource();
        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
        scaleT.setToX(1.2);
        scaleT.setToY(1.2);
        scaleT.play();

        DropShadow glow = new DropShadow();
            //glow.setColor(Color.CORNFLOWERBLUE);
        glow.setWidth(20);
        glow.setHeight(20);
        glow.setRadius(20);
        icon.setEffect(glow);

        home.setText("Home");
    }

    @FXML
    private void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            home.setText("");
        }
    }

    public void lblhome(KeyEvent keyEvent) {

    }
}
