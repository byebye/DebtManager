package client.utils;

import javafx.beans.value.ChangeListener;

import java.util.function.Consumer;

public class DataFormatListeners {

  public static ChangeListener<String> restrictBankAccountInput(Consumer<String> formatConsumer) {
    final int ACCOUNT_NUMBER_LENGTH = 22;
    return (observable, oldText, newText) -> {
      StringBuilder accountFormatBuilder = new StringBuilder(newText.replaceAll("[^0-9]", ""));
      accountFormatBuilder.setLength(Math.min(accountFormatBuilder.length(), ACCOUNT_NUMBER_LENGTH));
      for (int i = 2; i < accountFormatBuilder.length(); i += 5) {
        accountFormatBuilder.insert(i, ' ');
      }
      formatConsumer.accept(accountFormatBuilder.toString());
    };
  }

  public static ChangeListener<String> restrictTextLength(Consumer<String> formatConsumer, int maxLength) {
    return (observable, oldText, newText) -> {
      if (newText.length() > maxLength) {
        if (oldText.length() == maxLength)
          formatConsumer.accept(oldText);
        else
          formatConsumer.accept(newText.substring(0, maxLength));
      }
    };
  }

  public static ChangeListener<String> restrictTextFormat(Consumer<String> formatConsumer, String formatRegex) {
    return (observable, oldText, newText) -> {
      if (!newText.matches(formatRegex)) {
        // this condition is for security - sometimes listener is too slow and text may change multiple times
        if (!oldText.matches(formatRegex))
          formatConsumer.accept("");
        else
          formatConsumer.accept(oldText);
      }
    };
  }
}
