package server;

import common.MessageBox;

/**
 * Created by glapul on 10.05.15.
 */
public class SimpleMessageBox implements common.MessageBox {

    private static String message = "INITIAL MESSAGE";

    @Override
    public synchronized String getMessage() {
        return message;
    }

    @Override
    public synchronized void updateMessage(String newMessage) {
        message = newMessage;
        System.out.println("New message : " + message);
    }
}
