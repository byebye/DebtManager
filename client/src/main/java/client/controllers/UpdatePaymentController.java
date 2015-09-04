package client.controllers;

import common.data.Payment;
import common.data.User;
import client.view.Alerts;

import javafx.fxml.Initializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

public class UpdatePaymentController extends PaymentController implements Initializable {

  private Payment payment;

  public void setPayment(Payment payment) {
    this.payment = payment;
    fieldWhat.setText(payment.getWhat());
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
    return user.getId() == payment.getUserId();
  }

  @Override
  protected void initButtons() {
    super.initButtons();
    buttonSave.setText("Update");
    buttonLeft.setText("Remove");
    buttonLeft.setOnAction(event -> removePayment());
  }

  @Override
  protected void savePaymentInDatabase(User user, BigDecimal amount) throws RemoteException {
    dbHandler.updatePayment(payment.getId(), user.getId(), amount, fieldWhat.getText());
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
