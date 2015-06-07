package client.view;

import javafx.scene.control.TextField;

public class ErrorHighlighter {

   private static String defaultColor = "transparent";
   private static String errorColor = "#d60f0f";

   public static void highlightInvalidFields(TextField... fields) {
      for (TextField field : fields)
         field.setStyle("-fx-border-color: " + errorColor + ";");
      fields[0].requestFocus();
   }

   public static void unhighlitghtFields(TextField... fields) {
      for (TextField field : fields)
         field.setStyle("-fx-border-color: " + defaultColor + ";");
   }
}
