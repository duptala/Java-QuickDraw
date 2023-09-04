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
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;
import nz.ac.auckland.se206.words.CategorySelect.Difficulty;

public class SetDifficultyController {
  public enum DifficultyType {
    // DifficultyType enum will be responsible for recording what difficulty setting is being
    // changed by the page
    WORD,
    ACCURACY,
    TIME,
    CONFIDENCE,
    OVERALL
  }

  public static DifficultyType difficultyType;

  private static Timeline hideMasterTimeline;

  public static Timeline getTimeline() {
    return hideMasterTimeline;
  }

  public static void setDifficultyType(DifficultyType inputDifficultyType) {
    difficultyType = inputDifficultyType;
  }

  @FXML private Label difficultyTypeLabel;
  @FXML private Button easyButton;
  @FXML private Button mediumButton;
  @FXML private Button hardButton;
  @FXML private Button masterButton;
  @FXML private Button backButton;

  public void initialize() {
    // Timeline to hide the master button.
    // If accuracy difficulty is being edited, the master button should not appear as there is no
    // master difficulty for accuracy.
    hideMasterTimeline = new Timeline();
    hideMasterTimeline.setCycleCount(Timeline.INDEFINITE);
    EventHandler<ActionEvent> hideMasterEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // When the timeline is called, master button is invisible and will no longer take up
            // space
            masterButton.setManaged(false);
            masterButton.setVisible(false);
            hideMasterTimeline.stop();
          }
        };
    KeyFrame hideMasterKeyFrame = new KeyFrame(Duration.millis(1), hideMasterEventHandler);
    hideMasterTimeline.getKeyFrames().add(hideMasterKeyFrame);
  }



  /**
   * Changes the difficulty set by the DifficultyPageController to easy
   *
   * @param event ; the button push
   * @throws IOException
   */

  @FXML
  private void onEasyPush(ActionEvent event) throws IOException {
    switch (difficultyType) { // The Difficulty setting being changed has its difficulty Changed to
                              // easy
      case WORD:
        DifficultyPageController.setWordDifficulty(Difficulty.E);
        break;
      case ACCURACY:
        DifficultyPageController.setAccuracy(3);
        break;
      case TIME:
        DifficultyPageController.setTime(60);
        break;
      case CONFIDENCE:
        DifficultyPageController.setConfidence(1.00);
        break;
      case OVERALL:
        DifficultyPageController.setWordDifficulty(Difficulty.E);
        DifficultyPageController.setAccuracy(3);
        DifficultyPageController.setTime(60);
        DifficultyPageController.setConfidence(1.00);
    }
    returnToDifficultyPage(event);
  }

  /**
   * Changes the difficulty set by the DifficultyPageController to medium
   *
   * @param event ; the button push
   * @throws IOException
   */
  @FXML
  private void onMediumPush(ActionEvent event) throws IOException {
    // The Difficulty setting being changed has its difficulty Changed to medium
    switch (difficultyType) {
      case WORD:
        DifficultyPageController.setWordDifficulty(Difficulty.M);
        break;
      case ACCURACY:
        DifficultyPageController.setAccuracy(2);
        break;
      case TIME:
        DifficultyPageController.setTime(45);
        break;
      case CONFIDENCE:
        DifficultyPageController.setConfidence(10.00);
        break;
      case OVERALL:
        DifficultyPageController.setWordDifficulty(Difficulty.M);
        DifficultyPageController.setAccuracy(2);
        DifficultyPageController.setTime(45);
        DifficultyPageController.setConfidence(10.00);
        break;
    }
    returnToDifficultyPage(event);
  }

  /**
   * Changes the difficulty set by the DifficultyPageController to hard
   *
   * @param event ; the button push
   * @throws IOException
   */
  @FXML
  private void onHardPush(ActionEvent event) throws IOException {
    // The Difficulty setting being changed has its difficulty Changed to hard
    switch (difficultyType) {
      case WORD:
        DifficultyPageController.setWordDifficulty(Difficulty.H);
        break;
      case ACCURACY:
        DifficultyPageController.setAccuracy(1);
        break;
      case TIME:
        DifficultyPageController.setTime(30);
        break;
      case CONFIDENCE:
        DifficultyPageController.setConfidence(25.00);
        break;
      case OVERALL:
        DifficultyPageController.setWordDifficulty(Difficulty.H);
        DifficultyPageController.setAccuracy(1);
        DifficultyPageController.setTime(30);
        DifficultyPageController.setConfidence(25.00);
        break;
    }
    returnToDifficultyPage(event);
  }
  /**
   * Changes the difficulty set by the DifficultyPageController to master
   *
   * @param event ; the button push
   * @throws IOException
   */
  @FXML
  private void onMasterPush(ActionEvent event) throws IOException {
    // The Difficulty setting being changed has its difficulty Changed to master
    switch (difficultyType) {
      case WORD:
        DifficultyPageController.setWordDifficulty(Difficulty.Ma);
        break;
      case ACCURACY:
        break;
      case TIME:
        DifficultyPageController.setTime(15);
        break;
      case CONFIDENCE:
        DifficultyPageController.setConfidence(50.00);
        break;
      case OVERALL:
        DifficultyPageController.setWordDifficulty(Difficulty.Ma);
        DifficultyPageController.setTime(15);
        DifficultyPageController.setConfidence(50.00);
        DifficultyPageController.setAccuracy(1);
    }
    returnToDifficultyPage(event);
  }

  @FXML
  private void onBackPush(ActionEvent event) {
    returnToDifficultyPage(event);
  }

  private void returnToDifficultyPage(ActionEvent event) {
    // On returning to the root page, the master button is set to visible, and takes up space so it
    // will be displayed next time
    masterButton.setManaged(true);
    masterButton.setVisible(true);
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.DIFFICULTY_PAGE));
    DifficultyPageController.getTimeline().playFromStart();
  }
}