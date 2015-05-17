package common;

import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Inteface that is used to 'log in' to the server.
 */
public interface AccessProvider extends Remote {

    /**
     * Method used to 'login' to the server
     * @param mail user email
     * @param passwordHash password hash generated with SHA1Hasher
     * @return reference to a user-specific DB Handler
     * @throws RemoteException
     * @throws AuthenticationException if (mail, passwordHash) is not valid
     */
    public Object getDBHandler(Email mail, String passwordHash)
            throws RemoteException, AuthenticationException;

    /** Method used to create an account on the server
     * @param mail user email
     * @param passwordHash SHA1Hasher generated hash of the password picked by an user
     * @param iban user's InternationalBankAccountNumber
     * @throws RemoteException
     * @throws AuthenticationException if a user with such email OR IBAN already exists
     */
    public void signUp(Email mail, String passwordHash, IBAN iban)
            throws RemoteException, AuthenticationException;
}