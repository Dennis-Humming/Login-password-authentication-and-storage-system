package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;


    public Connection getConnection() {
        String databaseName = "demo_db";
        String databaseUser = "root";
        String databasePassword = "123456";
        String url = "jdbc:mysql://localhost:3306/demo_db?serverTimezone=GMT";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        boolean blank = databaseName.isBlank();

        return databaseLink;
    }
}
