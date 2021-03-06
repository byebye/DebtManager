package client.controllers;

import common.data.User;

import javafx.fxml.Initializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public class AddPaymentController extends PaymentController implements Initializable {

  @Override
  protected User getPaymentOwner(List<User> participants) {
    return currentUser;
  }

  @Override
  protected void initButtons() {
    super.initButtons();
    buttonSave.setText("Create");
    buttonLeft.setText("Cancel");
    buttonLeft.setOnAction(event -> currentStage.close());
  }

  @Override
  protected void checkOwingUsers() {
    boxOwingUsers.getCheckModel().checkAll();
    boxOwingUsers.getCheckModel().clearCheck(currentUser);
  }

  @Override
  protected void savePaymentInDatabase(User user, BigDecimal amount,
      Collection<Integer> owingUserIds) throws RemoteException {
    dbHandler.addPayment(budget, user.getId(), amount, fieldDescription.getText(), owingUserIds);
  }
}
