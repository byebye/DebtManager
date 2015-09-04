package client.windows;

import client.controllers.LoginController;
import client.controllers.SignUpController;

import javafx.stage.Modality;

public class SignUpWindow extends DebtManagerWindow {

  public SignUpWindow(LoginController loginController) {
    super("/fxml/SignUpWindow.fxml", "DebtManager - Sign up", Modality.APPLICATION_MODAL);

    SignUpController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setLoginController(loginController);
  }
}