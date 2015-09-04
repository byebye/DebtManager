package client.utils;

import common.data.BankTransfer.Status;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {

  private static Map<String, Image> imageCache = new HashMap<>();

  public static Image getStatusImage(Status status) {
    return loadImage("transfer_status/" + status.name() + ".png");
  }

  public static Image loadImage(String imageName) {
    if (!imageCache.containsKey(imageName)) {
      InputStream imageStream = ImageUtils.class.getResourceAsStream("/graphics/" + imageName);
      imageCache.put(imageName, new Image(imageStream));
    }
    return imageCache.get(imageName);
  }

  public static ImageView loadImageView(String imageName) {
    Image image = loadImage(imageName);
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(15);
    return imageView;
  }

  public static Button loadImageButton(String imageName) {
    Button button = new Button();
    button.setGraphic(ImageUtils.loadImageView(imageName));
    button.setPrefSize(25, 25);
    button.setPadding(new Insets(5, 5, 5, 5));
    return button;
  }
}
