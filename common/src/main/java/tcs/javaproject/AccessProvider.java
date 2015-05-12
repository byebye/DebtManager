package tcs.javaproject;

import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccessProvider extends Remote {

    public Object getAccess(Email mail, String passwordHash)
            throws RemoteException, AuthenticationException;

//    public Object signUp(tcs.javaproject.Email mail, String passwordHash, BankAccountNumber ban)
//            throws RemoteException, AuthenticationException;
}