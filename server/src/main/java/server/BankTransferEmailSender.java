package server;

import common.data.BankTransfer;
import common.data.Email;
import common.data.User;

import java.util.List;

public class BankTransferEmailSender implements Runnable {

  private static DatabaseController dbController;
  private String budgetName;
  private List<BankTransfer> transfers;
  private static Email debtmanagerEmail;

  static {
    debtmanagerEmail = new Email("debtmanager-noreply@managedebtsfor.me");
  }

  public BankTransferEmailSender(int budgetId, List<BankTransfer> transfers) {
    this.transfers = transfers;
    this.budgetName = Server.dbController.getBudgetName(budgetId);
  }

  @Override
  public void run() {
    SingleRecipientEmailSender sender = new SingleRecipientEmailSender();
    for (BankTransfer transfer : transfers) {
      User fromUser = Server.dbController.getUserById(transfer.getWhoId()),
          toUser = Server.dbController.getUserById(transfer.getWhomId());

      try {
        sender.send(debtmanagerEmail,
            new Email(fromUser.getEmail()),
            createSenderSubject(),
            createSenderText(transfer, toUser, fromUser));
        System.out.println("Email from " + fromUser.getEmail() + " to " + toUser.getEmail() + " sent");
      }
      catch (SingleRecipientEmailSender.EmailNotSentException e) {
        System.out.println("Email from " + fromUser.getEmail() + " to " + toUser.getEmail() + " not sent");
      }

      try {
        sender.send(debtmanagerEmail,
            new Email(toUser.getEmail()),
            createRecipientSubject(),
            createRecipientText(transfer, fromUser, toUser));
        System.out.println("Email from " + toUser.getEmail() + " to " + fromUser.getEmail() + " sent");
      }
      catch (SingleRecipientEmailSender.EmailNotSentException e) {
        System.out.println("Email from " + toUser.getEmail() + " to " + fromUser.getEmail() + " not sent");
      }
    }
  }

  private String createSenderSubject() {
    return "[ACTION REQUIRED] Settlement of budget " + budgetName;
  }

  private String createSenderText(BankTransfer transfer, User fromUser, User toUser) {

    return "Hi " + toUser.getName() + ",\n" +
           "\n" +
           "budget " + budgetName + " was settled and it turned out that you shall pay\n" +
           "\n" +
           transfer.getAmount() + " to user " + fromUser.getName() + " (" + fromUser.getEmail() + ").\n" +
           "" +
           "\n" +
           "His/Her bank account number is\n" +
           fromUser.getBankAccount() +
           "\n" +
           "He/She was sent an email about it, so he/she will be awaiting your payment.\n" +
           "After making the payment remember to confirm it in the app.\n" +
           "\n" +
           "Best wishes,\n" +
           "Debtmanager";
  }

  private String createRecipientSubject() {
    return "Settlement of budget " + budgetName;
  }

  private String createRecipientText(BankTransfer transfer, User fromUser, User toUser) {

    return "Hi " + toUser.getName() + ",\n" +
           "\n" +
           "budget " + budgetName + " was settled and it turned out that you shall BE PAID\n" +
           "\n" +
           transfer.getAmount() + " by " + fromUser.getName() + " (" + fromUser.getEmail() + ").\n" +
           "" +
           "\n" +
           "Await a transfer from account\n" +
           fromUser.getBankAccount() +
           "\n" +
           "Feel free to contact him/her if you will not receive the payment in a reasonable timeframe.\nAfter receiving the payment please confirm it in the app."
           +
           "\n" +
           "Best wishes,\n" +
           "Debtmanager";
  }
}
