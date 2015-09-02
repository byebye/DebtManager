package client;

import client.controllers.LoginController;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.connection.DbHandler;
import common.data.BankTransfer;
import common.data.Budget;
import common.data.Payment;
import common.data.Settlement;
import common.data.User;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.rmi.RemoteException;
import java.util.List;

public class BudgetExporter {
   private static DbHandler dbController = LoginController.dbHandler;

   private Budget budget;
   private List<User> participants;
   private List<Payment> accountedPayments;
   private List<Payment> unaccountedPayments;
   private List<Settlement> settleHistory;
   private Window ownerWindow;

   public BudgetExporter(
           Budget budget,
           List<User> participants,
           List<Payment> accountedPayments,
           List<Payment> unaccountedPayments,
           List<Settlement> settleHistory,
           Window ownerWindow
   ) {
      this.budget = budget;
      this.participants = participants;
      this.accountedPayments = accountedPayments;
      this.unaccountedPayments = unaccountedPayments;
      this.settleHistory = settleHistory;
      this.ownerWindow = ownerWindow;
   }

   public void export() {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select file to store exported budget");
      fileChooser.getExtensionFilters().addAll(
                                                      new FileChooser.ExtensionFilter("Json files", "*.json")
      );

      File exportFile = fileChooser.showOpenDialog(ownerWindow);
      if (exportFile != null) {
         try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile), "utf-8"))) {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(budgetToJSON(), Object.class);
            String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            writer.write(formattedJson);
         }
         catch (FileNotFoundException e) {
            e.printStackTrace();
         }
         catch (UnsupportedEncodingException e) {
            e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   private String budgetToJSON() {
      JSONObject budgetJSON = new JSONObject();
      budgetJSON.put("owner", userToJSON(budget.getOwner()));
      budgetJSON.put("participants", budgetParticipantsToJSON());
      budgetJSON.put("accountedPayments", paymentsToJSON(accountedPayments));
      budgetJSON.put("unaccountedPayments", paymentsToJSON(unaccountedPayments));
      budgetJSON.put("settlementsHistory", settlementsHistoryToJSON());
      return budgetJSON.toString();
   }

   private JSONObject userToJSON(User user) {
      JSONObject userJSON = new JSONObject();
      userJSON.put("name", user.getName());
      userJSON.put("email", user.getEmail());
      userJSON.put("bankAccount", user.getBankAccount());
      return userJSON;
   }

   private JSONArray budgetParticipantsToJSON() {
      JSONArray participantsJSON = new JSONArray();
      for (User user : participants)
         participantsJSON.put(userToJSON(user));
      return participantsJSON;
   }

   private JSONArray paymentsToJSON(List<Payment> payments) {
      JSONArray paymentsJSON = new JSONArray();
      for (Payment payment : payments)
         paymentsJSON.put(paymentToJSON(payment));
      return paymentsJSON;
   }

   private JSONObject paymentToJSON(Payment payment) {
      JSONObject paymentJSON = new JSONObject();
      paymentJSON.put("date", payment.getDate().toString());
      paymentJSON.put("who", payment.getWho());
      paymentJSON.put("what", payment.getWhat());
      paymentJSON.put("amount", payment.getAmount());
      return paymentJSON;
   }

   private JSONArray settlementsHistoryToJSON() {
      JSONArray historyJSON = new JSONArray();
      for (Settlement settlement : settleHistory) {
         historyJSON.put(settlementToJSON(settlement));
      }
      return historyJSON;
   }

   private JSONObject settlementToJSON(Settlement settlement) {
      JSONObject settlementJSON = new JSONObject();
      settlementJSON.put(settlement.getDate(), bankTransfersToJSON(settlement.getSettlementId()));
      return settlementJSON;
   }

   private JSONArray bankTransfersToJSON(int settlementId) {
      JSONArray transfersJSON = new JSONArray();
      try {
         List<BankTransfer> transfers = dbController.getBankTransfersBySettlementId(settlementId);
         for (BankTransfer transfer : transfers)
            transfersJSON.put(bankTransferToJSON(transfer));
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
      return transfersJSON;
   }

   private JSONObject bankTransferToJSON(BankTransfer transfer) {
      JSONObject transferJSON = new JSONObject();
      transferJSON.put("sender", transfer.getWho());
      transferJSON.put("recipient", transfer.getWhom());
      transferJSON.put("bankAccount", transfer.getBankAccount());
      transferJSON.put("amount", transfer.getAmount());
      transferJSON.put("status", transfer.getStatus().toString());
      return transferJSON;
   }
}
