package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.Duration;
import nz.ac.auckland.se206.Badge;
import nz.ac.auckland.se206.BadgeCell;
import nz.ac.auckland.se206.Profile;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;

public class DisplayBadgesController {
  private static Timeline loadBadgesTimeline;

  public static Timeline getTimeline() {
    return loadBadgesTimeline;
  }

  @FXML private ListView<Badge> earnedBadgesList = new ListView<Badge>();

  @FXML private ListView<Badge> notEarnedBadgesList = new ListView<Badge>();

  @FXML private Button backButton;

  @SuppressWarnings("unchecked")
  public void initialize() {
    // Timeline is used to load the badges
    loadBadgesTimeline = new Timeline();
    loadBadgesTimeline.setCycleCount(Timeline.INDEFINITE);
    EventHandler<ActionEvent> loadBadgesEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // All Earned Badges will be added to the listview containing earned badges;
            ArrayList<Badge> badges = sortBadges(Profile.getProfile().getBadgesEarned());
            for (Badge badge : badges) {
              earnedBadgesList.getItems().add(badge);
            }
            // All unearned Badges will be added to the listview containing unearned badges;
            badges = sortBadges(Profile.getProfile().getBadgesNotEarned());
            for (Badge badge : badges) {
              notEarnedBadgesList.getItems().add(badge);
            }
            // This only occurs once
            loadBadgesTimeline.stop();
          }
        };
    // This occurs with a small delay for visual effects.
    KeyFrame loadBadgesKeyFrame = new KeyFrame(Duration.millis(10), loadBadgesEventHandler);
    loadBadgesTimeline.getKeyFrames().add(loadBadgesKeyFrame);
    // The cells are of BadgeCell, meaning their color is dependent on their rank
    // and their text is
    // dependent on the name and description of the badge.
    earnedBadgesList.setCellFactory(
        new Callback<ListView<Badge>, ListCell<Badge>>() {
          @Override
          public ListCell<Badge> call(ListView<Badge> list) {
            return new BadgeCell();
          }
        });
    notEarnedBadgesList.setCellFactory(
        new Callback<ListView<Badge>, ListCell<Badge>>() {
          @Override
          public ListCell<Badge> call(ListView<Badge> list) {
            return new BadgeCell();
          }
        });
  }

  @FXML
  private void onBackPush(ActionEvent event) {
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.PROFILE_PAGE));
    // The list is cleared so that the badges don't duplicate upon multiple visits
    // to the page.
    earnedBadgesList.getItems().clear();
    earnedBadgesList.getItems().clear();
  }

  /**
   * Sorts the badges according to their rank
   *
   * @param badges ArrayList<Badge> containing badges to be sorted
   * @return sorted ArrayList<Badge> of the input ArrayList
   */
  private ArrayList<Badge> sortBadges(ArrayList<Badge> badges) {
    // lists are created to contain the badges of each difficulty seperately.
    ArrayList<Badge> iron = new ArrayList<Badge>();
    ArrayList<Badge> bronze = new ArrayList<Badge>();
    ArrayList<Badge> silver = new ArrayList<Badge>();
    ArrayList<Badge> gold = new ArrayList<Badge>();
    ArrayList<Badge> platinum = new ArrayList<Badge>();
    ArrayList<Badge> onyx = new ArrayList<Badge>();
    // The badges are added to their respective arraylists;
    for (Badge badge : badges) {
      switch (badge.getRank()) {
        case IRON:
          iron.add(badge);
          break;
        case BRONZE:
          bronze.add(badge);
          break;
        case SILVER:
          silver.add(badge);
          break;
        case GOLD:
          gold.add(badge);
          break;
        case PLATINUM:
          platinum.add(badge);
          break;
        case ONYX:
          onyx.add(badge);
          break;
      }
    }
    // All the sublists are added together in the Order they want to be displayed
    // in: Iron -> Bronze
    // -> Silver -> Gold -> Platinum -> Onyx
    addAllBadges(iron, bronze);
    addAllBadges(iron, silver);
    addAllBadges(iron, gold);
    addAllBadges(iron, platinum);
    addAllBadges(iron, onyx);
    // This sorts them in order
    return iron;
  }

  private void addAllBadges(ArrayList<Badge> original, ArrayList<Badge> added) {
    for (int i = 0; i < added.size(); i++) {
      original.add(added.get(i));
    }
  }
}
