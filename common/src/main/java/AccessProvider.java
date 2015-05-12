import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccessProvider extends Remote {

    public Object getAccess(String login, String passwordHash)
            throws RemoteException, AuthenticationException;
}