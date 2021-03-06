package client.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public abstract class Alerts {

  public static void internalError() {
    showAlert(new Alert(AlertType.ERROR),
        "Internal error",
        "Internal application error!",
        "Application will be closed.");
  }

  public static void serverConnectionError() {
    showAlert(new Alert(AlertType.ERROR),
        "Server connection error",
        "Server connection error!",
        "Check your connection and try again.");
  }

  public static void userCreated() {
    showAlert(new Alert(AlertType.INFORMATION),
        "New account created",
        "Account created successfully!",
        "Now you can log in to view your budgets.");
  }

  public static Optional<ButtonType> deleteBudgetConfirmation() {
    return showAlert(new Alert(AlertType.CONFIRMATION),
        "Confirm deletion",
        "Are you sure you want to delete this budget?",
        "This operation cannot be undone.");
  }

  public static void participantCannotBeRemoved(String reason) {
    showAlert(new Alert(AlertType.ERROR),
        "Participant cannot be removed",
        "Participant cannot be removed!",
        reason);
  }

  public static void noPaymentsToSettle() {
    showAlert(new Alert(AlertType.INFORMATION),
        "No payments to settle",
        "No payments to settle!",
        "Add some payments and try again.");
  }

  public static void exportToFileFailed() {
    showAlert(new Alert(AlertType.ERROR),
        "Export to file failed",
        "Failed to export your budgets to file!",
        "Try to choose another file and try again.");
  }

  private static Optional<ButtonType> showAlert(Alert alert, String title, String header, String content) {
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    return alert.showAndWait();
  }
}
