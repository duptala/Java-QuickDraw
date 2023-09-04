package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.util.Duration;
import nz.ac.auckland.se206.Profile;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;

public class MainMenuController {

  private static Timeline setProfileTimeline;

  /**
   * Getter method for the timeline to set the text of the profile button, so that it can be
   * accessed by other classes.
   */
  protected static Timeline getTimeline() {
    return setProfileTimeline;
  }

  @FXML private Button startButton;
  @FXML private Button profileButton;
  @FXML private Button extraGamesButton;
  /*
   * Timeline to set the text of the profileButton to the name of the
   * currentProfile
   */

  public void initialize() throws IOException {
    // sets up setProfileTimeline
    setProfileTimeline = new Timeline();
    setProfileTimeline.setCycleCount(Timeline.INDEFINITE);
    /*
     * Upon call of the timeline, the text of the profileButton is set to the name
     * of the current profile
     */
    EventHandler<ActionEvent> setProfileEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            profileButton.setText(Profile.getProfile().getName());
            // This occurs once each call
            setProfileTimeline.stop();
          }
        };
    KeyFrame setProfileKeyFrame = new KeyFrame(Duration.millis(1), setProfileEventHandler);
    setProfileTimeline.getKeyFrames().add(setProfileKeyFrame);
  }

  /** Method to be called on start button press. Changes scene to difficulty page */
  @FXML
  private void onStartPush(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    DifficultyPageController.getTimeline().playFromStart();

    scene.setRoot(SceneManager.getRoot(Page.DIFFICULTY_PAGE));
  }

  /*
   * Method to be called on push of the profile button to take you to the profile
   * page
   */
  @FXML
  private void onProfilePush(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();

    scene.setRoot(SceneManager.getRoot(Page.PROFILE_PAGE));
  }

  @FXML
  private void onExtraGamesPush(ActionEvent event) {
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.EXTRA_GAMES));
  }
}
