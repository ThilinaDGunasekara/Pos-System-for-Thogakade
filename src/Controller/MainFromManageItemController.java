package Controller;

import DB.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainFromManageItemController {
    public AnchorPane apManageCus;
    public JFXTextField txtItemCode_id;
    public JFXTextField txtQuantityOH_id;
    public ImageView picHome_id;
    public ImageView picNewItem_id;
    public JFXButton btnSave_Id;
    public JFXButton btnDelete_Id;
    public JFXTextField txtDescription_id;
    public JFXTextField txtUnitPrice_id;
    public TableView<ItemTm> tblItemList;
    public Label lblDescription;
    public Label lblQOH;
    public Label lblUnitPrice;
    public Connection connection;
    public JFXButton btnItemReport;
    public Label home;
    public Label cart;

    public void initialize() throws IOException {

        connection = DBConnection.getInstance().getConnection();

        tblItemList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItemList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));
        tblItemList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        try {
            load();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtItemCode_id.setDisable(true);
        txtDescription_id.setDisable(true);
        txtQuantityOH_id.setDisable(true);
        txtUnitPrice_id.setDisable(true);
        btnSave_Id.setDisable(true);
        btnDelete_Id.setDisable(true);

        tblItemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTm>() {
            @Override
            public void changed(ObservableValue<? extends ItemTm> observable, ItemTm oldValue, ItemTm newValue) {

                ItemTm selectedItems = tblItemList.getSelectionModel().getSelectedItem();

                if (selectedItems == null) {
                    btnSave_Id.setText("Save");
                    btnDelete_Id.setDisable(true);
                    return;
                }

                btnSave_Id.setText("Update");
                btnItemReport.setDisable(true);
                btnSave_Id.setDisable(false);
                btnDelete_Id.setDisable(false);
                txtDescription_id.setDisable(false);
                txtQuantityOH_id.setDisable(false);
                txtUnitPrice_id.setDisable(false);


                txtItemCode_id.setText(selectedItems.getCode());
                txtDescription_id.setText(selectedItems.getDescription());
                txtQuantityOH_id.setText(String.valueOf(selectedItems.getQuantityOnHand()));
                txtUnitPrice_id.setText(String.valueOf(selectedItems.getUnitPrice()));

            }
        });

        txtDescription_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String description = txtDescription_id.getText();
                if(!description.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")){
                    txtDescription_id.selectAll();
                    lblDescription.setText("[Please enter,Correct format of the Description.]");
                    txtDescription_id.requestFocus();
                    btnSave_Id.setDisable(true);
                }else {
                    lblDescription.setText("");
                }
            }
        });
        txtQuantityOH_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String description = txtQuantityOH_id.getText();
                if(!description.matches("\\d+")){
                    txtQuantityOH_id.selectAll();
                    lblQOH.setText("[Please enter only numbers.]");
                    txtQuantityOH_id.requestFocus();
                    btnSave_Id.setDisable(true);
                }else {
                    lblQOH.setText("");
                }
            }
        });
        txtUnitPrice_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String description = txtUnitPrice_id.getText();
                if(!description.matches("^\\d+(,\\d{3})*(\\.\\d{1,2})?$")){
                    txtUnitPrice_id.selectAll();
                    lblUnitPrice.setText("[Please enter,Correct format of the Unit Price.]");
                    txtUnitPrice_id.requestFocus();
                    btnSave_Id.setDisable(true);
                }else {
                    lblUnitPrice.setText("");
                    btnSave_Id.setDisable(false);
                }
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

    public void picNewItem_OnClick(MouseEvent mouseEvent) {

        idGenerate();

        txtUnitPrice_id.clear();
        txtQuantityOH_id.clear();
        txtDescription_id.clear();
        txtDescription_id.setDisable(false);
        txtQuantityOH_id.setDisable(false);
        txtUnitPrice_id.setDisable(false);
        btnSave_Id.setText("Save");
        btnSave_Id.setDisable(false);
        txtDescription_id.requestFocus();
        btnItemReport.setDisable(true);

    }
    public void btnSave_OnAction(ActionEvent actionEvent) {

        if (btnSave_Id.getText().equals("Save")) {
            try {

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
                preparedStatement.setString(1, txtItemCode_id.getText());
                preparedStatement.setString(2, txtDescription_id.getText());
                preparedStatement.setString(3, txtQuantityOH_id.getText());
                preparedStatement.setString(4, txtUnitPrice_id.getText());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Insert Row Successfully..");
                }
                load();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            btnSave_Id.setDisable(true);
            btnItemReport.setDisable(false);
            txtItemCode_id.setDisable(true);
            txtDescription_id.setDisable(true);
            txtQuantityOH_id.setDisable(true);
            txtUnitPrice_id.setDisable(true);
            tblItemList.refresh();

        } else {
            try {
                PreparedStatement prepsUpdate = connection.prepareStatement("UPDATE Item SET description= ?,quantityOnHand=? ,unitPrice=? WHERE code= ?");
                prepsUpdate.setString(1, txtDescription_id.getText());
                prepsUpdate.setString(2, txtQuantityOH_id.getText());
                prepsUpdate.setString(3, txtUnitPrice_id.getText());
                prepsUpdate.setString(4, txtItemCode_id.getText());

                if(prepsUpdate.executeUpdate()==0){
                    throw new RuntimeException("Something went wrong");
                }


                txtItemCode_id.setDisable(true);
                txtDescription_id.setDisable(true);
                txtQuantityOH_id.setDisable(true);
                txtUnitPrice_id.setDisable(true);

                //txtItemCode_id.clear();
                txtUnitPrice_id.clear();
                txtQuantityOH_id.clear();
                txtDescription_id.clear();

                btnSave_Id.setDisable(true);
                btnDelete_Id.setDisable(true);

                lblUnitPrice.setText("");
                lblDescription.setText("");
                lblQOH.setText("");

                tblItemList.getItems().clear();
                tblItemList.refresh();
                load();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void btnDelete_OnAction (ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this Item?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            try {
                PreparedStatement prpDelete = connection.prepareStatement("DELETE FROM Item WHERE code = ?");
                prpDelete.setString(1,txtItemCode_id.getText());
                if(prpDelete.executeUpdate()==0){
                    throw new RuntimeException("Something went wrong..");
                }
                load();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        txtItemCode_id.setDisable(true);
        txtDescription_id.setDisable(true);
        txtQuantityOH_id.setDisable(true);
        txtUnitPrice_id.setDisable(true);

        //txtItemCode_id.clear();
        txtUnitPrice_id.clear();
        txtQuantityOH_id.clear();
        txtDescription_id.clear();

        btnSave_Id.setDisable(true);
        btnDelete_Id.setDisable(true);

        lblUnitPrice.setText("");
        lblDescription.setText("");
        lblQOH.setText("");

        tblItemList.getItems().clear();
        tblItemList.refresh();
    }

    public void txtDescription_OnAction (ActionEvent actionEvent){

    }

    public void txtQuantityOH_OnAction (ActionEvent actionEvent){

    }

    public void txtUnitPrice_OnAction (ActionEvent actionEvent){

    }

    public void txtItemCode_OnAction (ActionEvent actionEvent){ }

    public void idGenerate () {
        try {

            int maxId = 0;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT code  FROM Item ORDER BY  code DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String memberId = resultSet.getString(1);
                maxId = Integer.parseInt(memberId.replace("I", ""));
            }

            maxId++;
            String id = "";
            if (maxId < 10) {
                id = "I00" + maxId;
            } else if (maxId < 100) {
                id = "I0" + maxId;
            } else {
                id = "I" + maxId;
            }
            txtItemCode_id.setText(id);
            tblItemList.refresh();
            txtDescription_id.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void load () throws SQLException {
        tblItemList.getItems().clear();
        PreparedStatement pstForQuery = connection.prepareStatement("SELECT * FROM Item ");

        ResultSet resultSet = pstForQuery.executeQuery();
        ObservableList<ItemTm> members = tblItemList.getItems();

        while (resultSet.next()) {
            members.add(new ItemTm(resultSet.getString(1), resultSet.getString(2),Integer.parseInt(resultSet.getString(3)),Double.parseDouble(resultSet.getString(4))));
        }
    }

    public void btnItemReport_OnAction(ActionEvent actionEvent) throws JRException {
        ObservableList<ItemTm> getItems = tblItemList.getItems();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Report/ItemReport.jasper"));
        //JasperDesign jasperDesign = JRXmlLoader.load(CustomerTM.class.getResourceAsStream("/Report/ItemReport.jrxml"));
        //JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String,Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(getItems));
        JasperViewer.viewReport(jasperPrint,false);
    }


    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
       try{
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
           cart.setText("New");
       }catch (ClassCastException e){
           System.out.println(" ");
       }

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
            cart.setText("");
        }
    }
}

