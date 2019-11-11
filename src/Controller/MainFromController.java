package Controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class MainFromController {

    public AnchorPane anpLoging;
    public Label lblDescription;
    public Label lblMenu;
    public Label lbl;
    public ImageView imgCustomer;
    public ImageView imgOrder;
    public ImageView imgItem;
    public ImageView imgViewOrders;
    private JFXPanel root;


    void show(String value) throws IOException {
        URL resource = this.getClass().getResource(value);
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
       /* scene.getStylesheets().add("/CSS/TableView.css");*/
        Stage primaryStage = (Stage)( this.anpLoging.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }

    public void manageCustomer_OnAction(MouseEvent mouseEvent) throws IOException {
        show("/view/MainFromManageCustomer.fxml");
    }

    public void picSearchOrder_OnClick(MouseEvent mouseEvent) throws IOException {
       show("/view/MainFromSearch.fxml");
    }

    public void picManageItems_OnClick(MouseEvent mouseEvent) throws IOException {
       show("/view/MainFromManageItems.fxml");

    }

    public void navigate(MouseEvent mouseEvent) throws IOException {
        show("/view/MainFromPaceOrder.fxml");
    }

    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();

            switch(icon.getId()){
                case "imgCustomer":
                    lblMenu.setText("Manage Customers");
                    lblDescription.setText("Click to add, edit, delete, search or view customers");
                    break;
                case "imgItem":
                    lblMenu.setText("Manage Items");
                    lblDescription.setText("Click to add, edit, delete, search or view items");
                    break;
                case "imgOrder":
                    lblMenu.setText("Place Orders");
                    lblDescription.setText("Click here if you want to place a new order");
                    break;
                case "imgViewOrders":
                    lblMenu.setText("Search Orders");
                    lblDescription.setText("Click if you want to search orders");
                    break;
            }

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
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }
}
