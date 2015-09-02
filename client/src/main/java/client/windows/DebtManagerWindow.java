package client.windows;

import client.view.Alerts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DebtManagerWindow extends Stage {

  protected FXMLLoader fxmlLoader;

  public DebtManagerWindow(String fxmlWindowPath, String title, Modality modality) {
    this(fxmlWindowPath, title, modality, null);
  }

  public DebtManagerWindow(String fxmlWindowPath, String title, Modality modality,
                           Callback<Class<?>, Object> controllerFactory) {
    try {
      fxmlLoader = new FXMLLoader(getClass().getResource(fxmlWindowPath));
      if (controllerFactory != null)
        fxmlLoader.setControllerFactory(controllerFactory);
      Parent root = fxmlLoader.load();
      setScene(new Scene(root));
      setTitle(title);
      initModality(modality);
    }
    catch (Exception e) {
      e.printStackTrace();
      Alerts.internalError();
      System.exit(1);
    }
  }
}
