package server;

import common.connection.AccessProvider;
import common.data.BankAccount;
import common.data.Email;

import javax.naming.AuthenticationException;
import java.rmi.RemoteException;

public class BasicAccessProvider implements AccessProvider {

  @Override
  public Object getDbHandler(Email mail, String passwordHash) throws RemoteException, AuthenticationException {
    if (!DatabaseController.getInstance().validateUserPassword(mail, passwordHash)) {
      throw new AuthenticationException();
    }
    return DatabaseController.getExportedInstance();
  }

  @Override
  public void signUp(Email mail, String name, String passwordHash, BankAccount bankAccount)
      throws RemoteException, AuthenticationException {
    if (!DatabaseController.getInstance()
        .createUser(mail.toString(), name, passwordHash, bankAccount.toString())) {
      throw new AuthenticationException();
    }
  }
}
