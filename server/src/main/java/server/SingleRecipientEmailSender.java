package server;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import common.Email;

/**
 * Created by glapul on 30.05.15.
 */
public class SingleRecipientEmailSender {

   private static final String sendGridUserName,
                               sendGridPassword;

   static {
      sendGridUserName = System.getenv("SENDGRID_USERNAME");
      sendGridPassword = System.getenv("SENDGRID_PASSWORD");
      System.out.println(sendGridPassword + " "  + sendGridUserName);
   }

   private SendGrid sg;

   public static class EmailNotSentException extends Exception {}

   public SingleRecipientEmailSender() {
      sg = new SendGrid(sendGridUserName, sendGridPassword);
   }

   public void send(Email from, Email to, String subject, String text) throws EmailNotSentException{
      SendGrid.Email email = new SendGrid.Email();

      email.addTo(to.getAddress());
      email.setFrom(from.getAddress());
      email.setSubject(subject);
      email.setText(text);

      try {
         SendGrid.Response response = sg.send(email);
         System.out.println(response.getMessage());
         if(response.getMessage() != "success")
            throw new EmailNotSentException();
      }
      catch (SendGridException e) {
         System.err.println(e);
         throw new EmailNotSentException();
      }
   }

}
