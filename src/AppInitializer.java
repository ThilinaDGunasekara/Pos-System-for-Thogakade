import DB.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
        Connection connection = DBConnection.getInstance().getConnection();


        // Unfortunately if i close the system, this will be drop the table name call TempOrderTm...........
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT table_name FROM information_schema.TABLES ");
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


        //Close Connection...........
        try {
            DBConnection.getInstance().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL resource = this.getClass().getResource("/view/MainFrom.fxml");
        Parent root;
        root = FXMLLoader.load(resource);
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("@@@ Male Suduyakada @@@");
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
