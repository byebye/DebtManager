package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetWindow extends Stage {

   public BudgetWindow() throws IOException {
      Parent root = FXMLLoader.load(getClass().getResource("/fxml/BudgetWindow.fxml"));
      setTitle("DeptManager - [Budget Name]");
      setScene(new Scene(root));
   }
}

