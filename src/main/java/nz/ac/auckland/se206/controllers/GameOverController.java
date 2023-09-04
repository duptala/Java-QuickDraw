package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.Profile;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;

public class GameOverController {

  private static Profile currentProfile;
  private static Timeline updatePlaceTimeline;

  /** Method to be called by other controllers to display the place */
  protected static void startTimeline() {
    updatePlaceTimeline.playFromStart();
  }

  @FXML private Button tryAgainButton;
  @FXML private Button mainMenuButton;
  @FXML private Button downloadButton;
  @FXML private Label yourPlaceLabel;

  public void initialize() {
    updatePlaceTimeline = new Timeline();
    updatePlaceTimeline.setCycleCount(Timeline.INDEFINITE);

    // The place the user achieved is retrieved and displayed
    EventHandler<ActionEvent> updatePlaceEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // If the gamemode is mysetery word, answer is displayed.
            if (ExtraGamesController.isChooseMystery()) {
              yourPlaceLabel.setText("Answer: " + ReadyController.getRandomWord());
            } else {
              // Otherwise, the place the user achieved is displayed
              yourPlaceLabel.setText("Your place: " + CanvasController.getPlace());
            }
            currentProfile = Profile.getProfile();
            try {
              // when jump to this secne would automatically update the record
              ProfileCreationController.insertData(currentProfile);
            } catch (IOException e) {
              e.printStackTrace();
            }

            // this is only done once(upon moving to this scene)
            updatePlaceTimeline.stop();
          }
        };

    // updating place happens once every time moving to this scene
    KeyFrame updatePlaceKeyFrame = new KeyFrame(Duration.millis(1), updatePlaceEventHandler);
    updatePlaceTimeline.getKeyFrames().add(updatePlaceKeyFrame);
  }

  /** Method to be called on TryAgain button push */
  @FXML
  private void onTryAgainPush(ActionEvent event) {
    // scene is switched to ready, and the word to be drawn is generated and
    // displayed
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();

    ReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.READY));
  }

  /** Method to be called on MainMenu push */
  @FXML
  private void onMainMenuPush(ActionEvent event) {
    // check whether in the mystery game mode
    if (ExtraGamesController.isChooseMystery()) {
      ExtraGamesController.setChooseMystery(false);
    }
    // scene is set to main menu
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();

    scene.setRoot(SceneManager.getRoot(Page.MAIN_MENU));
  }

  /** Method to be pushed on download. Calls CanvasController method to download the image */
  @FXML
  private void onDownloadPush(ActionEvent event) {
    CanvasController.getTimeLine().get(3).playFromStart();
  }
}
