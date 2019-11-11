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

public class MAinFromManageCustomerController {

    public JFXTextField txtId_ID;
    public JFXTextField txtName_id;
    public JFXTextField txtAddress_Id;
    public TableView<CustomerTM> tblList_Id;
    public AnchorPane apManageCus;
    public ImageView picHome_id;
    public ImageView picNewCus_id;
    public JFXButton btnSave_Id;
    public JFXButton btnDelete_Id;
    public Label lblname;
    public Label lblAddress;
    public Connection connection;
    public JFXButton btnCustomerReport;
    public Label New;
    public Label home;


    public void initialize() {
        connection = DBConnection.getInstance().getConnection();

        tblList_Id.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblList_Id.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblList_Id.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        txtId_ID.setDisable(true);
        txtName_id.setDisable(true);
        txtAddress_Id.setDisable(true);
        btnSave_Id.setDisable(true);
        btnDelete_Id.setDisable(true);
        btnCustomerReport.setDisable(false);

        try {
            load();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblList_Id.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            CustomerTM selectedItems = tblList_Id.getSelectionModel().getSelectedItem();

            if (selectedItems == null) {
                btnSave_Id.setText("Save");
                btnDelete_Id.setDisable(true);
                return;
            }

            btnSave_Id.setText("Update");
            btnSave_Id.setDisable(false);
            btnDelete_Id.setDisable(false);
            btnCustomerReport.setDisable(true);
            txtName_id.setDisable(false);
            txtAddress_Id.setDisable(false);
            txtId_ID.setText(selectedItems.getId());
            txtName_id.setText(selectedItems.getName());
            txtAddress_Id.setText(selectedItems.getAddress());

        });

        txtName_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String name = txtName_id.getText();
                if(!name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")){
                    txtName_id.selectAll();
                    lblname.setText("[ Please enter,Correct format of the Name.. ]");
                    txtName_id.requestFocus();
                    btnSave_Id.setDisable(true);
                }else {
                    lblname.setText("");
                }
            }
        });

        txtAddress_Id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String address = txtAddress_Id.getText();
                if(!address.matches("\\d{1,3}.?\\d{0,3}\\s[a-zA-Z]{2,30}\\s[a-zA-Z]{2,15}")) {
                    txtAddress_Id.selectAll();
                    lblAddress.setText("[ Please enter,Correct format of the Address.. ]");
                }else {
                    btnSave_Id.setDisable(false);
                    lblAddress.setText("");
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

    public void picNewCus_OnClick(MouseEvent mouseEvent) {
//        picHome_id.setImage(new Image("icon/a.png"));

        txtName_id.clear();
        txtAddress_Id.clear();
        tblList_Id.getSelectionModel().clearSelection();
        txtName_id.setDisable(false);
        txtAddress_Id.setDisable(false);
        txtName_id.requestFocus();
        btnSave_Id.setDisable(false);
        lblAddress.setText("");
        lblname.setText("");

        //Generate a new id
        idGenerate();
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {

        if (btnSave_Id.getText().equals("Save")) {
            try {

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?)");
                preparedStatement.setString(1,txtId_ID.getText());
                preparedStatement.setString(2,txtName_id.getText());
                preparedStatement.setString(3,txtAddress_Id.getText());

                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows>0){
                    System.out.println("Insert Row Successfully..");
                }
                tblList_Id.getItems().clear();
                load();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            btnSave_Id.setDisable(true);
            txtAddress_Id.setDisable(true);
            txtName_id.setDisable(true);
            tblList_Id.refresh();

        } else {
            try {
                PreparedStatement prepsUpdate = connection.prepareStatement("UPDATE Customer SET name = ?, address= ? WHERE id= ?");
                prepsUpdate.setString(1, txtName_id.getText());
                prepsUpdate.setString(2, txtAddress_Id.getText());
                prepsUpdate.setString(3, txtId_ID.getText());

                if(prepsUpdate.executeUpdate()==0){
                    throw new RuntimeException("Something went wrong");
                }

                txtId_ID.clear();
                txtName_id.setDisable(true);
                txtAddress_Id.setDisable(true);
                txtAddress_Id.clear();
                txtName_id.clear();
                btnSave_Id.setDisable(true);
                btnDelete_Id.setDisable(true);
                lblAddress.setText("");
                lblname.setText("");

                tblList_Id.refresh();
                load();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
           /* try {
                connection.prepareStatement("SELECT id FROM Orders WHERE id ");
            } catch (SQLException e) {
                e.printStackTrace();
            }*/

            try {
                PreparedStatement prepSatate = connection.prepareStatement("DELETE FROM Customer WHERE id =? ");
                prepSatate.setString(1,txtId_ID.getText());
                if(prepSatate.executeUpdate()==0){
                    Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this customer?", ButtonType.CLOSE);
                }
                load();
                System.out.println("Done");
            } catch (SQLException e) {

                System.out.println("Done");
            }
        }
    }


    public void idGenerate(){
        try {

            int maxId =0;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id  FROM Customer ORDER BY  id DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String memberId = resultSet.getString(1);
                maxId = Integer.parseInt(memberId.replace("C", ""));
            }

            maxId++;
            String id = "";
            if (maxId < 10) {
                id = "C00" + maxId;
            } else if (maxId < 100) {
                id = "C0" + maxId;
            } else {
                id = "C" + maxId;
            }
            txtId_ID.setText(id);
            tblList_Id.refresh();
            txtName_id.requestFocus();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void load() throws SQLException {
        tblList_Id.getItems().clear();
        PreparedStatement pstForQuery = connection.prepareStatement("SELECT * FROM Customer ");

        ResultSet resultSet = pstForQuery.executeQuery();
        ObservableList<CustomerTM> members = tblList_Id.getItems();

        while(resultSet.next()){
            members.add(new CustomerTM(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
        }
    }

    public void btnCustomerReport_OnAction(ActionEvent actionEvent) throws JRException {

        ObservableList<CustomerTM> customers = tblList_Id.getItems();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Report/customerReport.jasper"));
        //JasperDesign jasperDesign = JRXmlLoader.load(CustomerTM.class.getResourceAsStream("/Report/customerReport.jrxml"));
       /* JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);*/
        Map<String,Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(customers));
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
        New.setText("New");
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
            New.setText("");
        }
    }
}
