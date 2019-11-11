package Controller;

import DB.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.Integer.parseInt;

public class MainFromControllerPlaceOrder {
    public AnchorPane placeOrderAnchorPane;
    public JFXTextField txtDescription_id;
    public JFXTextField txtQuntityOnHand_id;
    public JFXTextField txtUnitPrice_id;
    public JFXTextField txtQuntity_id;
    public JFXTextField txtTotal_id;
    public JFXButton btnAdd_Id;
    public JFXButton btnPlaceOrder_id;
    public TableView<OrderTM> tblList_Id;
    public JFXTextField txtOrderId_Id;
    public JFXTextField txtName_id;
    public ImageView picHome_id;
    public JFXTextField txtId_ID2;
    public JFXComboBox<String> cmbCode_id;
    public JFXComboBox cmbCustomerId_id;
    public Label lblDate_id;
    public JFXButton btnNewOrder_Id1;
    public Label lblQuantity;
    public Connection connection;
    public JFXButton btnBill;
    public Label home;
    public Label lblQ;


    int fullTotal = 0;
    int newTempTable;
    int count=1;


    public void initialize() {
        /*addConnection();*/
        connection = DBConnection.getInstance().getConnection();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        lblDate_id.setText(formatter.format(date));

        tblList_Id.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblList_Id.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblList_Id.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblList_Id.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblList_Id.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        tblList_Id.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("delete_Id"));

        idGenerate();
        cmbCustomerId_id.requestFocus();
        txtOrderId_Id.setDisable(true);
        lblDate_id.setDisable(true);

        try {
            PreparedStatement prepStateComboLoad = this.connection.prepareStatement("SELECT id FROM Customer");
            ResultSet resultSet = prepStateComboLoad.executeQuery();

            while (resultSet.next()){
                ObservableList<String> comboId = cmbCustomerId_id.getItems();
                comboId.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement prepStateComboLoad = this.connection.prepareStatement("SELECT code FROM Item");
            ResultSet resultSet = prepStateComboLoad.executeQuery();

            while (resultSet.next()){
                ObservableList<String> comboId = cmbCode_id.getItems();
                comboId.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblList_Id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderTM> observable, OrderTM oldValue, OrderTM newValue) {

                OrderTM selectedItem = tblList_Id.getSelectionModel().getSelectedItem();

                if (selectedItem == null) {
                    btnAdd_Id.setText("Add");
                    return;
                }

                btnAdd_Id.setText("Update");
                btnNewOrder_Id1.setDisable(true);
                txtName_id.setDisable(false);

                cmbCode_id.getSelectionModel().select(selectedItem.getCode());
                txtDescription_id.setText(selectedItem.getDescription());
                txtUnitPrice_id.setText(selectedItem.getUnitPrice());

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT quantityOnHand FROM Item WHERE code = ?");
                    preparedStatement.setString(1,selectedItem.getCode());

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        txtQuntityOnHand_id.setText(resultSet.getString(1));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


        cmbCustomerId_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Object selectedItem = cmbCustomerId_id.getSelectionModel().getSelectedItem();

                try {
                    PreparedStatement preparedStatement = MainFromControllerPlaceOrder.this.connection.prepareStatement("SELECT * FROM Customer");
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()){
                        if(resultSet.getString(1).equals(selectedItem)){
                            txtName_id.setText(resultSet.getString(2));

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        cmbCode_id.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Object selectedItem = cmbCode_id.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM Item");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                    if(resultSet.getString(1).equals(selectedItem)){
                        txtDescription_id.setText(resultSet.getString(2));
                        txtQuntityOnHand_id.setText(resultSet.getString(3));
                        txtUnitPrice_id.setText(resultSet.getString(4));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT table_name FROM information_schema.TABLES ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if(!resultSet.getString(1).equals("TempOrderTM")) {
                   setFieldDisable();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT table_name FROM information_schema.TABLES ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getString(1).equals("TempOrderTM")) {
                    System.out.println("Done");
                    btnNewOrder_Id1.setDisable(true);
                    txtOrderId_Id.setDisable(false);
                    lblDate_id.setDisable(false);
                    cmbCustomerId_id.setDisable(false);
                    txtName_id.setDisable(false);
                    cmbCode_id.setDisable(false);
                    txtDescription_id.setDisable(false);
                    txtQuntityOnHand_id.setDisable(false);
                    txtUnitPrice_id.setDisable(false);
                    txtQuntity_id.setDisable(false);
                    btnAdd_Id.setDisable(false);
                    tblList_Id.setDisable(false);
                    txtTotal_id.setDisable(false);
                    btnPlaceOrder_id.setDisable(false);

                    PreparedStatement prepStateCusCollect = this.connection.prepareStatement("SELECT t.id,c.name FROM TempOrderTM t INNER JOIN Customer c ON t.id=c.id GROUP BY t.id");
                    ResultSet resultSetCusCollect = prepStateCusCollect.executeQuery();
                    while (resultSetCusCollect.next()) {
                        cmbCustomerId_id.getSelectionModel().select(resultSetCusCollect.getString(1));
                    }
                    addToTableModel();

                    getTotal();
                    tblList_Id.refresh();
                    cmbCode_id.requestFocus();
                    System.out.println("in");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtQuntity_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String description = txtQuntity_id.getText();
                if(txtQuntity_id.getText().equals("")){
                    lblQ.setText("");
                }
                if(!description.matches("\\d+")){
                    txtQuntity_id.selectAll();
                    lblQ.setText("[Please enter only numbers.]");
                    txtQuntity_id.requestFocus();
                    btnAdd_Id.setDisable(true);
                }else {
                    btnAdd_Id.setDisable(false);
                    lblQ.setText("");
                }
                if(txtQuntity_id.getText().equals("")){
                    lblQ.setText("");
                }
            }
        });

        lblQ.setText("");
        btnNewOrder_Id1.setDisable(false);
    }


    public void txtId_OnAction(ActionEvent actionEvent) { }

    public void txtName_OnAction(ActionEvent actionEvent) { }

    public void picHome_OnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("../view/MainFrom.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.placeOrderAnchorPane.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) throws JRException, SQLException {

        System.out.println((String) cmbCustomerId_id.getSelectionModel().getSelectedItem());
        lblQ.setText("");

        try {
            PreparedStatement prepStateInsertOrder = connection.prepareStatement("INSERT INTO Orders VALUES (?,?,?)");
            prepStateInsertOrder.setString(1,txtOrderId_Id.getText());
            prepStateInsertOrder.setString(2,lblDate_id.getText());
            prepStateInsertOrder.setString(3, (String) cmbCustomerId_id.getSelectionModel().getSelectedItem());
            int i = prepStateInsertOrder.executeUpdate();

            if(i>0){
                PreparedStatement preparedStatementInsertToOrder = connection.prepareStatement("INSERT INTO OrderDetail SELECT orderId,itemId,quantity,unitPrice FROM TempOrderTM");
                preparedStatementInsertToOrder.executeUpdate();

                /*PreparedStatement prepStateUpdate = connection.prepareStatement("SELECT itemId,quantity FROM TempOrderTM");
                ResultSet resultSetUpdate = prepStateUpdate.executeQuery();*/

                PreparedStatement prepState = connection.prepareStatement("UPDATE Item i,TempOrderTM t SET i.quantityOnHand = (i.quantityOnHand-t.quantity) WHERE i.code = t.itemId" );
                if(prepState.executeUpdate()>0){
                    System.out.println("i'm Happy...");
                }

                PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE TempOrderTM");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

       /* JasperDesign mainReportDesign = JRXmlLoader.load(CustomerTM.class.getResourceAsStream("/Report/MainReport.jrxml"));
        JasperDesign subReportDesign = JRXmlLoader.load(CustomerTM.class.getResourceAsStream("/Report/subReportPosBill.jrxml"));*/
/*
        JasperReport mainJasperReport = JasperCompileManager.compileReport(mainReportDesign);
        JasperReport subJasperReport = JasperCompileManager.compileReport(subReportDesign);*/

        JasperReport mainJasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Report/MainReport.jasper"));
/*        JasperDesign subJasperReport = JRXmlLoader.load(this.getClass().getResourceAsStream("/Report/subReportPosBill.jrxml"));*/

        Map<String,Object> params = new HashMap<>();
        params.put("orderId",txtOrderId_Id.getText());
/*        params.put("SubReport",subJasperReport);*/
        JasperPrint jasperPrint = JasperFillManager.fillReport(mainJasperReport, params,connection);
        JasperViewer.viewReport(jasperPrint,false);

        reset();
        cmbCustomerId_id.requestFocus();
        setFieldDisable();
        btnNewOrder_Id1.setDisable(false);


    }
    private void reset(){
        // Initialize controls to their default states

        lblDate_id.setText(LocalDate.now() + "");

        btnNewOrder_Id1.setDisable(true);
        btnAdd_Id.setDisable(true);
        txtName_id.setEditable(false);
        btnAdd_Id.setDisable(false);
        txtName_id.clear();
        txtTotal_id.clear();
        txtQuntity_id.setDisable(false);
        cmbCustomerId_id.getSelectionModel().clearSelection();
        cmbCode_id.getSelectionModel().clearSelection();
        tblList_Id.getItems().clear();

        idGenerate();
    }

    public void setFieldDisable(){
        txtOrderId_Id.setDisable(true);
        lblDate_id.setDisable(true);
        cmbCustomerId_id.setDisable(true);
        txtName_id.setDisable(true);
        cmbCode_id.setDisable(true);
        txtDescription_id.setDisable(true);
        txtQuntityOnHand_id.setDisable(true);
        txtUnitPrice_id.setDisable(true);
        txtQuntity_id.setDisable(true);
        btnAdd_Id.setDisable(true);
        tblList_Id.setDisable(true);
        txtTotal_id.setDisable(true);
        btnPlaceOrder_id.setDisable(true);
    }


    public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {


        int QOH = 0;
        lblQ.setText("");
        int quantityInText = Integer.parseInt(txtQuntity_id.getText());
        String codeId = cmbCode_id.getSelectionModel().getSelectedItem();
        try {

            PreparedStatement prepStateGetItemDetail = connection.prepareStatement("SELECT quantityOnHand FROM Item WHERE Item.code = ?");
            prepStateGetItemDetail.setString(1,codeId);
            ResultSet resultSetQOH = prepStateGetItemDetail.executeQuery();
            while (resultSetQOH.next()){
                QOH = Integer.parseInt(resultSetQOH.getString(1));
            }
            if(QOH<quantityInText){
                infoBox("No adequate items to be supplied..","Massage","Warning..!");
                txtQuntity_id.requestFocus();
                txtQuntity_id.selectAll();
                return;
            }
            if(btnAdd_Id.getText().equals("Add")) {
                PreparedStatement prepStateQuantityFailure = connection.prepareStatement("SELECT t.itemId,t.quantity,t.unitPrice FROM TempOrderTM t");
                ResultSet resultSet = prepStateQuantityFailure.executeQuery();
                int totQty = 0;
                double unitPrice = 0;
                while (resultSet.next()) {
                    if (resultSet.getString(1).equals(codeId)) {

                        int quantity = Integer.parseInt(resultSet.getString(2));
                        unitPrice = Double.parseDouble(resultSet.getString(3));
                        totQty = quantity + quantityInText;

                        if (QOH < totQty) {

                            infoBox("No adequate items to be supplied..", "Massage", "Warning..!");
                            //Alert newAlert = new Alert(Alert.AlertType.WARNING,,ButtonType.CLOSE);
                            txtQuntity_id.requestFocus();
                            txtQuntity_id.selectAll();
                            return;
                        } else {
                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TempOrderTM SET quantity =?,rowTotal =? WHERE itemId =?");
                            preparedStatement.setString(1, String.valueOf(totQty));

                            double rowTotal = unitPrice * totQty;
                            preparedStatement.setString(2, String.valueOf(rowTotal));
                            preparedStatement.setString(3, codeId);

                            if (preparedStatement.executeUpdate() > 0) {
                                System.out.println("Same Id Update Successfully done.....");
                            }
                            addToTableModel();
                            getTotal();
                            return;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement123 = connection.prepareStatement("SELECT t.itemId,t.quantity,t.unitPrice FROM TempOrderTM t");
            ResultSet resultSet123 = preparedStatement123.executeQuery();
            while (resultSet123.next()){

                if(!resultSet123.getString(1).equals(codeId)) {

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

            if (btnAdd_Id.getText().equals("Add")) {

                addToTemp();
                addToTableModel();
                getTotal();

            } else {
                OrderTM selectedItem = tblList_Id.getSelectionModel().getSelectedItem();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TempOrderTM SET quantity =?,rowTotal =? WHERE itemId =?");
                    preparedStatement.setString(1, txtQuntity_id.getText());
                    double unitPrice = Double.parseDouble(selectedItem.getUnitPrice());
                    int qty = Integer.parseInt(txtQuntity_id.getText());

                    double rowTotal = unitPrice * qty;

                    preparedStatement.setString(2, String.valueOf(rowTotal));
                    preparedStatement.setString(3, selectedItem.getCode());
                    if (preparedStatement.executeUpdate() > 0) {
                        System.out.println("update is Successfully....");
                    }

                    btnAdd_Id.setText("Add");
                    addToTableModel();
                    getTotal();


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            txtQuntity_id.clear();
            txtDescription_id.clear();
            txtQuntityOnHand_id.clear();
            txtUnitPrice_id.clear();
            cmbCode_id.getSelectionModel().clearSelection();
            cmbCode_id.requestFocus();
        }


    public void addToTemp(){
        try {

            double UnitPrice = Double.parseDouble(txtUnitPrice_id.getText());
            int quantity = Integer.parseInt(txtQuntity_id.getText());
            double rowTot = UnitPrice*quantity;

            PreparedStatement preparedStatementIntoTable = connection.prepareStatement("INSERT INTO TempOrderTM VALUES (?,?,?,?,?,?,?)");
            preparedStatementIntoTable.setString(1,txtOrderId_Id.getText());
            preparedStatementIntoTable.setString(2, String.valueOf(cmbCustomerId_id.getSelectionModel().getSelectedItem()));
            preparedStatementIntoTable.setString(3,cmbCode_id.getSelectionModel().getSelectedItem());
            preparedStatementIntoTable.setString(4,txtDescription_id.getText());
            preparedStatementIntoTable.setString(5,txtQuntity_id.getText());
            preparedStatementIntoTable.setString(6,txtUnitPrice_id.getText());
            preparedStatementIntoTable.setString(7, String.valueOf(rowTot));

            int effectedRows = preparedStatementIntoTable.executeUpdate();
            if(effectedRows>0){
                System.out.println(effectedRows);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void addToTableModel(){

        try {

            ObservableList<OrderTM> items = tblList_Id.getItems();
            items.clear();

            PreparedStatement preparedStatementGetTempTable = connection.prepareStatement("SELECT * FROM TempOrderTM ");
            ResultSet resultSet = preparedStatementGetTempTable.executeQuery();

            while (resultSet.next()){
                String code = resultSet.getString(3);
                String  description = resultSet.getString(4);
                String quantityTable = resultSet.getString(5);
                String uPrice = resultSet.getString(6);
                String rowTot = resultSet.getString(7);

                JFXButton btnDelete = new JFXButton("Delete");
                btnDelete.setBackground(new Background(new BackgroundFill(Color.SKYBLUE,null,null)));
                btnDelete.setId(code);

                OrderTM detailTM = new OrderTM(code,description,quantityTable,uPrice,rowTot,btnDelete);
                items.add(detailTM);

                btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        try {

                            PreparedStatement pepStateDelete = connection.prepareStatement("DELETE FROM TempOrderTM WHERE itemId =?");
                            pepStateDelete.setString(1,btnDelete.getId());
                            pepStateDelete.executeUpdate();

                            addToTableModel();
                            getTotal();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void getTotal(){
        try {
            PreparedStatement prepStateGetTotal = connection.prepareStatement("SELECT SUM(rowTotal)FROM TempOrderTM ");
            ResultSet resultSet = prepStateGetTotal.executeQuery();
            while(resultSet.next()){
                String tot = resultSet.getString(1);
                txtTotal_id.setText(tot);
                System.out.println(tot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void txtTotal_OnAction(ActionEvent actionEvent) { }

    public void txtQuntity_OnAction(ActionEvent actionEvent) {
        String quantity = txtQuntity_id.getText();
        if(!quantity.matches("^[0-9]+$")){
            txtQuntity_id.selectAll();
            lblQuantity.setText("[ Place Enter Only Numbers ]");
        }else {
            lblQuantity.setText("");
            btnAdd_OnAction(actionEvent);
            cmbCode_id.requestFocus();
        }
    }

    public void txtUnitPrice_OnAction(ActionEvent actionEvent) {

    }

    public void txtQuntityOnHand_OnAction(ActionEvent actionEvent) { }

    public void txtDescription_OnAction(ActionEvent actionEvent) { }

    public void cmbCustomerId_OnAction(ActionEvent actionEvent) { }

    public void cmbCode_OnAction(ActionEvent actionEvent) {
        txtQuntity_id.requestFocus();
    }

    public void btnNewOrder_OnAction(ActionEvent actionEvent) {
        idGenerate();
        reset();
        count =1;
        System.out.println("count"+count);
        lblDate_id.setDisable(false);
        cmbCustomerId_id.setDisable(false);
        txtName_id.setDisable(false);
        cmbCode_id.setDisable(false);
        txtDescription_id.setDisable(false);
        txtQuntityOnHand_id.setDisable(false);
        txtUnitPrice_id.setDisable(false);
        txtQuntity_id.setDisable(false);
        btnAdd_Id.setDisable(false);
        tblList_Id.setDisable(false);
        txtTotal_id.setDisable(false);
        btnPlaceOrder_id.setDisable(false);
        cmbCustomerId_id.requestFocus();



        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT table_name FROM information_schema.TABLES ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getString(1).equals("TempOrderTM")){
                    PreparedStatement preparedStatementDrop = connection.prepareStatement("DROP TABLE TempOrderTM");
                    preparedStatementDrop.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE TempOrderTM(\n" +
                    "    orderId VARCHAR(10),\n" +
                    "    id VARCHAR(10),\n" +
                    "    itemId VARCHAR(10),\n" +
                    "    description VARCHAR(20),\n" +
                    "    quantity INT(10),\n" +
                    "    unitPrice DECIMAL(6,2),\n" +
                    "    rowTotal DECIMAL(10,2)\n" +
                    ");");
            newTempTable = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void idGenerate () {

        try {
            int maxId = 0;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT orderId  FROM Orders ORDER BY orderId DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String orderId = resultSet.getString(1);
                maxId = Integer.parseInt(orderId.replace("OI", ""));
            }

            maxId++;
            String id = "";
            if (maxId < 10) {
                id = "OI00" + maxId;
            } else if (maxId < 100) {
                id = "OI0" + maxId;
            } else {
                id = "OI" + maxId;
            }
            txtOrderId_Id.setText(id);
            tblList_Id.refresh();
            txtDescription_id.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

}
