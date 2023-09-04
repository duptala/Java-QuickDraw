package nz.ac.auckland.se206;

import javafx.scene.control.ListCell;

public class BadgeCell extends ListCell<Badge> {
  public BadgeCell() {}
  ;

  @Override
  // Override method to display the text of the badge on the cell, and change the color depending on
  // rank
  protected void updateItem(Badge badge, boolean empty) {
    if (badge == null || empty) {
      setText(null);
    } else {
      setText(String.format("%1$-70s %2$-60s", badge.getName(), badge.getDescription()));
      switch (badge.getRank()) {
          // The color of the cell is dependent on the rank
        case IRON:
          // Iron is gray
          setStyle("-fx-control-inner-background: #ABB0B8");
          break;
        case BRONZE:
          // Bronze is Brown
          setStyle("-fx-control-inner-background: #deb887");
          break;
        case SILVER:
          // Silver is Gray
          setStyle("-fx-control-inner-background: #E6EEF7");
          break;
        case GOLD:
          // Gold is gold
          setStyle("-fx-control-inner-background: #FFD700");
          break;
        case PLATINUM:
          // Platinum is aquamarine
          setStyle("-fx-control-inner-background: #7FFFD4");
          break;
        case ONYX:
          // Onyx is black
          setStyle("-fx-control-inner-background: #000000");
          break;
      }
    }
  }
}