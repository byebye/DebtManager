package server;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

/**
 * Created by glapul on 30.05.15.
 */
public class TestMail {
   public static void main(String [] args) {
      SendGrid sg = new SendGrid("debtmanager", "debtmanager1");

      SendGrid.Email email = new SendGrid.Email();
      email.addTo("michal.glapa@gmail.com");
      email.addTo("uaaabbjjkl@gmail.com");
      email.setFrom("debtmanager@managedebtsfor.me");
      email.setSubject("Hello World");
      email.setText("My first email with SendGrid Java!  ");

      try {
         SendGrid.Response response = sg.send(email);
         System.out.println(response.getMessage());
      }
      catch (SendGridException e) {
         System.err.println(e);
      }
   }
}
