package client.view;

import client.utils.ImageUtils;
import common.data.BankTransfer;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;

public class StatusImageCell extends TableCell<BankTransfer, ImageView> {

  private final ImageView imageView = new ImageView();

  public StatusImageCell() {
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(20);
  }

  @Override
  protected void updateItem(ImageView item, boolean empty) {
    super.updateItem(item, empty);
    if (!empty) {
      BankTransfer transfer = (BankTransfer) StatusImageCell.this.getTableRow().getItem();
      if (transfer != null) {
        imageView.setImage(ImageUtils.getStatusImage(transfer.getStatus()));
        setGraphic(imageView);
      }
    }
    else {
      setGraphic(null);
    }
  }
}
