package tcs.javaproject.guitest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tcs.javaproject.database.DatabaseController;

import java.io.IOException;

public class LoginWindow extends Application {
   public static DatabaseController dbController = new DatabaseController();

   @Override
   public void start(Stage primaryStage) throws Exception {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
      Parent root = fxmlLoader.load();
      LoginController controller = fxmlLoader.<LoginController>getController();

      primaryStage.setTitle("DeptManager - Sign in");
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
   }

}
