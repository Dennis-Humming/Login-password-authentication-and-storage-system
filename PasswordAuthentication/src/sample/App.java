package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("系统登录口令认证算法的验证实现");
        primaryStage.getIcons().add(new Image("file:images/555.PNG"));
        primaryStage.setScene(new Scene(root,680,400));
        primaryStage.show();
    }
}
