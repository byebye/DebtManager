package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Users;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;

public class SignUpWindow extends Stage {

   public SignUpWindow() throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpWindow.fxml"));
      Parent root = fxmlLoader.load();
      SignUpController controller = fxmlLoader.<SignUpController>getController();
      setTitle("DeptManager - sign up");
      setScene(new Scene(root));
   }
}