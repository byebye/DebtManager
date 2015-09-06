package client;

import common.connection.DbHandler;
import common.data.BankTransfer;
import common.data.Budget;
import common.data.Payment;
import common.data.Settlement;
import common.data.User;
import client.controllers.BasicController;
import client.view.Alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.List;

public class BudgetExporter {

  private static DbHandler dbHandler = BasicController.dbHandler;

  private Budget budget;
  private List<User> participants;
  private List<Payment> settledPayments;
  private List<Payment> unsettledPayments;
  private List<Settlement> settleHistory;
  private Window ownerWindow;

  public BudgetExporter(Budget budget,
      List<User> participants,
      List<Payment> settledPayments,
      List<Payment> unsettledPayments,
      List<Settlement> settlementsHistory,
      Window ownerWindow
  ) {
    this.budget = budget;
    this.participants = participants;
    this.settledPayments = settledPayments;
    this.unsettledPayments = unsettledPayments;
    this.settleHistory = settlementsHistory;
    this.ownerWindow = ownerWindow;
  }

  public void export() {
    File exportFile = chooseExportFile();
    if (exportFile == null)
      return;
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile), "utf-8"))) {
      ObjectMapper mapper = new ObjectMapper();
      Object json = mapper.readValue(budgetToJSON(), Object.class);
      String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
      writer.write(formattedJson);
    }
    catch (Exception e) {
      e.printStackTrace();
      Alerts.exportToFileFailed();
    }
  }

  private File chooseExportFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select file to store exported budget");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Json files", "*.json")
    );
    return fileChooser.showOpenDialog(ownerWindow);
  }

  private String budgetToJSON() {
    JSONObject budgetJSON = new JSONObject();
    budgetJSON.put("owner", userToJSON(budget.getOwner()));
    budgetJSON.put("participants", budgetParticipantsToJSON());
    budgetJSON.put("settledPayments", paymentsToJSON(settledPayments));
    budgetJSON.put("unsettledPayments", paymentsToJSON(unsettledPayments));
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
    for (User user : participants) {
      participantsJSON.put(userToJSON(user));
    }
    return participantsJSON;
  }

  private JSONArray paymentsToJSON(List<Payment> payments) {
    JSONArray paymentsJSON = new JSONArray();
    for (Payment payment : payments) {
      paymentsJSON.put(paymentToJSON(payment));
    }
    return paymentsJSON;
  }

  private JSONObject paymentToJSON(Payment payment) {
    JSONObject paymentJSON = new JSONObject();
    paymentJSON.put("payer", payment.getPayer());
    paymentJSON.put("description", payment.getDescription());
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
    settlementJSON.put(settlement.getSettleDate().toString(), bankTransfersToJSON(settlement.getSettlementId()));
    return settlementJSON;
  }

  private JSONArray bankTransfersToJSON(int settlementId) {
    JSONArray transfersJSON = new JSONArray();
    try {
      List<BankTransfer> transfers = dbHandler.getBankTransfersBySettlementId(settlementId);
      for (BankTransfer transfer : transfers) {
        transfersJSON.put(bankTransferToJSON(transfer));
      }
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
    return transfersJSON;
  }

  private JSONObject bankTransferToJSON(BankTransfer transfer) {
    JSONObject transferJSON = new JSONObject();
    transferJSON.put("sender", transfer.getSender());
    transferJSON.put("recipient", transfer.getRecipient());
    transferJSON.put("bankAccount", transfer.getBankAccount());
    transferJSON.put("amount", transfer.getAmount());
    transferJSON.put("status", transfer.getStatus().toString());
    return transferJSON;
  }
}
