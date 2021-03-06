package common.connection;

import common.data.BankAccount;
import common.data.Email;

import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Inteface that is used to 'log in' to the server.
 */
public interface AccessProvider extends Remote {

  /**
   * Method used to 'login' to the server
   *
   * @param mail         user email
   * @param passwordHash password hash generated with SHA1Hasher
   * @return reference to a user-specific DB Handler
   * @throws RemoteException
   * @throws AuthenticationException if (mail, passwordHash) is not valid
   */
  Object getDbHandler(Email mail, String passwordHash) throws RemoteException, AuthenticationException;

  /**
   * Method used to create an account on the server
   *
   * @param mail         user email
   * @param name         user name
   * @param passwordHash hash generated by SHA1Hasher from the password picked by an user
   * @param bankAccount  valid bank account
   * @throws RemoteException
   * @throws AuthenticationException
   */
  void signUp(Email mail, String name, String passwordHash, BankAccount bankAccount)
      throws RemoteException, AuthenticationException;
}