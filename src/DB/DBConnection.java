package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection {

    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/Pos?createDatabaseIfNotExist=true&allowMultiQueries=true","root","password");

            PreparedStatement pstm = connection.prepareStatement("SHOW TABLES");
            ResultSet resultSet = pstm.executeQuery();
            if (!resultSet.next()){
                String sql = "create table Customer(\n" +
                        "\n" +
                        "    id VARCHAR(10),\n" +
                        "    name VARCHAR(20),\n" +
                        "    address VARCHAR(20),\n" +
                        "    constraint Customer_pk primary key (id)\n" +
                        ");\n" +
                        "\n" +
                        "create table Item(\n" +
                        "\n" +
                        "    code VARCHAR(10) PRIMARY KEY ,\n" +
                        "    description VARCHAR(20),\n" +
                        "    quantityOnHand INT(10),\n" +
                        "    unitPrice DECIMAL(10,2)\n" +
                        ");\n" +
                        "\n" +
                        "create table Orders(\n" +
                        "\n" +
                        "    orderId VARCHAR(10) PRIMARY KEY ,\n" +
                        "    date DATE,\n" +
                        "    id VARCHAR(10),\n" +
                        "    CONSTRAINT FOREIGN KEY(id) REFERENCES Customer(id)\n" +
                        "\n" +
                        ");\n" +
                        "\n" +
                        "create table OrderDetail(\n" +
                        "\n" +
                        "    orderId VARCHAR(10),\n" +
                        "    itemId VARCHAR(10),\n" +
                        "    quantity INT(10),\n" +
                        "    unitPrice DECIMAL(6,2),\n" +
                        "    CONSTRAINT FOREIGN KEY(orderId) REFERENCES Orders(orderId),\n" +
                        "    CONSTRAINT FOREIGN KEY(itemId) REFERENCES Item(code)\n" +
                        "\n" +
                        ");\n" +
                        "\n";
                pstm = connection.prepareStatement(sql);
                pstm.execute();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static DBConnection getInstance(){
        return dbConnection =((dbConnection ==null)? new DBConnection(): dbConnection);
    }
    public Connection getConnection(){
        return connection;
    }

}
