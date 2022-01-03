package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.net.URL;

import static algorithm.PasswordAlgorithm.Agent;
import static algorithm.PasswordAlgorithm.Arandom;

public class LoginController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile= new File("images/333.PNG");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile= new File("images/444.PNG");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void loginButtonOnAction() throws Exception {
        if(usernameTextField.getText().isBlank()==false&&enterPasswordField.getText().isBlank()==false){
            validateLogin();
        }else{
            loginMessageLabel.setText("Please enter username and password.");
        }
    }

    public void registerButtonOnAction() throws Exception {
        if(usernameTextField.getText().isBlank()==true&&enterPasswordField.getText().isBlank()==true){
            loginMessageLabel.setText("Please enter username and password.");
        }else{
            userRegister();
        }
    }

    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() throws Exception {
        //注册数据库驱动,与数据库建立连接
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String sqlVerifyLoginUsername = "SELECT count(1) FROM user_account WHERE username = '"
                + usernameTextField.getText()  + "'";
        try{
            Statement statement = connectDb.createStatement();
            ResultSet UsernameQueryResult = statement.executeQuery(sqlVerifyLoginUsername);
            boolean judgement = false;
            while(UsernameQueryResult.next()){
                if (UsernameQueryResult.getInt(1)==1){
                    judgement = true;

                }else{
                    judgement = false;
                    loginMessageLabel.setText("Invalid Login.Please try again!");
                }
            }
            if (judgement){
                PreparedStatement psSalt = connectDb.prepareStatement("SELECT salt FROM user_account WHERE username = '"
                        +usernameTextField.getText()+"'");
                ResultSet rs = psSalt.executeQuery();
                String salt = null;
                while (rs.next()) {
                    salt = rs.getString("salt");
                }
                String sqlVerifyLoginTemporaryPassword = "SELECT count(1) FROM user_account WHERE temporary_password = '"
                        + Agent(enterPasswordField.getText(),salt)  + "'";
                ResultSet passwordQueryResult = statement.executeQuery(sqlVerifyLoginTemporaryPassword);
                while(passwordQueryResult.next()) {
                    if (passwordQueryResult.getInt(1) == 1) {
                        loginMessageLabel.setText("Congratulations!");
                        PreparedStatement psTemporaryPassword= connectDb.prepareStatement("SELECT temporary_password FROM user_account WHERE username = '"
                                +usernameTextField.getText()+"'");
                        ResultSet rs2 = psTemporaryPassword.executeQuery();
                        String temporary_password = null;
                        while (rs2.next()) {
                            temporary_password = rs2.getString("temporary_password");
                        }
                        System.out.println(temporary_password);
                        System.out.println("口令比对成功！");
                    } else {
                        loginMessageLabel.setText("Invalid Login.Please try again!");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void userRegister() throws Exception {
        //注册数据库驱动,与数据库建立连接
        String Dsalt = Arandom();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        //执行SQL语句
        Statement statement = connectDb.createStatement();
        String sqlUserRegister = "INSERT INTO user_account (username,salt,temporary_password) values ('"
                +usernameTextField.getText()+"','"+Dsalt+"','"+Agent(enterPasswordField.getText(),Dsalt)+"')";
        statement.executeUpdate(sqlUserRegister);
        String sqlVerifyRegister = "SELECT count(1) FROM user_account WHERE username = '"
                + usernameTextField.getText() + "'";

        try{
            ResultSet queryResult = statement.executeQuery(sqlVerifyRegister);
            while(queryResult.next()){
                if (queryResult.getInt(1)==1){
                    loginMessageLabel.setText("Register succeeded!");
                }else{
                    loginMessageLabel.setText("Register failed!");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}
