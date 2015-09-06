package server;

import common.data.Email;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

public class SingleRecipientEmailSender {

  private static final String sendGridUserName,
      sendGridPassword;

  static {
    sendGridUserName = System.getenv("SENDGRID_USERNAME");
    sendGridPassword = System.getenv("SENDGRID_PASSWORD");
    System.out.println(sendGridPassword + " " + sendGridUserName);
  }

  private SendGrid sg;

  public static class EmailNotSentException extends Exception {}

  public SingleRecipientEmailSender() {
    sg = new SendGrid(sendGridUserName, sendGridPassword);
  }

  public void send(Email from, Email to, String subject, String text) throws EmailNotSentException {
    SendGrid.Email email = new SendGrid.Email();

    email.addTo(to.toString());
    email.setFrom(from.toString());
    email.setSubject(subject);
    email.setText(text);

    try {
      SendGrid.Response response = sg.send(email);
      System.out.println(response.getMessage());
      if (!response.getMessage().equals("{\"message\":\"success\"}"))
        throw new EmailNotSentException();
    }
    catch (SendGridException e) {
      System.err.println(e);
      throw new EmailNotSentException();
    }
  }
}
