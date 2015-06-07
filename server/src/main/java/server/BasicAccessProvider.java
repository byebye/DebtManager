package server;

import common.*;

import javax.naming.AuthenticationException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by glapul on 16.05.15.
 */
public class BasicAccessProvider implements AccessProvider {

   //This should have been done differently! Storing dbController is unsafe
   DatabaseController dbController;

   BasicAccessProvider(DatabaseController dbController) {
      this.dbController = dbController;
   }

   @Override
   public Object getDBHandler(Email mail, String passwordHash) throws RemoteException, AuthenticationException {

      if (dbController.validateUserPassword(mail, passwordHash)) {
         return DatabaseController.getExportedInstance();
      }
      throw new AuthenticationException();
   }

   @Override
   public void signUp(Email mail, String name, BigInteger bankAccount, String passwordHash) throws RemoteException, AuthenticationException {

      if (!dbController.createUser(mail.getAddress(), name, bankAccount, passwordHash))
         throw new AuthenticationException();

   }

}
