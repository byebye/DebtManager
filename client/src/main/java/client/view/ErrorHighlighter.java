package client.view;

import javafx.scene.control.TextInputControl;

public class ErrorHighlighter {

  private static final String defaultColor = "transparent";
  private static final String errorColor = "#d60f0f";

  public static void highlightInvalidFields(TextInputControl mainField, TextInputControl... fields) {
    mainField.setStyle("-fx-border-color: " + errorColor + ";");
    for (TextInputControl field : fields)
      field.setStyle("-fx-border-color: " + errorColor + ";");
    mainField.requestFocus();
  }

  public static void unhighlightFields(TextInputControl mainField, TextInputControl... fields) {
    mainField.setStyle("-fx-border-color: " + defaultColor + ";");
    for (TextInputControl field : fields)
      field.setStyle("-fx-border-color: " + defaultColor + ";");
  }
}
