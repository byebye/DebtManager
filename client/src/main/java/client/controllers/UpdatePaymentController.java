package client.controllers;

import common.data.Payment;
import common.data.User;
import client.view.Alerts;

import javafx.fxml.Initializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public class UpdatePaymentController extends PaymentController implements Initializable {

  private Payment payment;

  public void setPayment(Payment payment) {
    this.payment = payment;
    fieldDescription.setText(payment.getDescription());
    fieldAmount.setText(Double.toString(payment.getAmount()));
  }

  @Override
  protected User getPaymentOwner(List<User> participants) {
    return participants.stream()
        .filter(this::isPaymentOwner)
        .findFirst()
        .get();
  }

  private boolean isPaymentOwner(User user) {
    return user.getId() == payment.getPayerId();
  }

  @Override
  protected void initButtons() {
    super.initButtons();
    buttonSave.setText("Update");
    buttonLeft.setText("Remove");
    buttonLeft.setOnAction(event -> removePayment());
  }

  @Override
  protected void checkOwingUsers() {
    participants.stream()
        .filter(user -> payment.isUserOwing(user.getId()))
        .forEach(user -> boxOwingUsers.getCheckModel().check(user));
  }

  @Override
  protected void savePaymentInDatabase(User user, BigDecimal amount,
      Collection<Integer> owingUserIds) throws RemoteException {
    dbHandler.updatePayment(payment.getId(), user.getId(), amount, fieldDescription.getText(), owingUserIds);
  }

  private void removePayment() {
    try {
      dbHandler.deletePayment(payment.getId());
      currentStage.close();
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }
}
