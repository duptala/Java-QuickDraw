package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;
import nz.ac.auckland.se206.words.CategorySelect.Difficulty;

public class zenDifficultyController {

  private static Difficulty wordDifficulty = Difficulty.E;

  public static Difficulty getWordDifficulty() {
    return wordDifficulty;
  }

  @FXML private Button wordEasyButton;

  @FXML private Button wordMediumButton;

  @FXML private Button wordHardButton;

  @FXML private Button wordMasterButton;

  @FXML private Button backBtn;

  /** Method to be called when Easy Button is pushed */
  @FXML
  private void onWordEasyPush(ActionEvent event) {
    // difficulty selected is stored. Currently all easy for alpha
    wordDifficulty = Difficulty.E;
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    zenReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.READY_ZEN));
  }

  /** Method to be called when Medium Button is pushed */
  @FXML
  private void onWordMediumPush(ActionEvent event) {
    wordDifficulty = Difficulty.M; // the difficulty is depend on user select
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    zenReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.READY_ZEN));
  }

  /** Method to be called when Hard Button is pushed */
  @FXML
  private void onWordHardPush(ActionEvent event) {
    wordDifficulty = Difficulty.H;
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    zenReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.READY_ZEN));
  }

  /** Method to be called when Master Button is pushed */
  @FXML
  private void onWordMasterPush(ActionEvent event) {
    wordDifficulty = Difficulty.Ma;
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    zenReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.READY_ZEN));
  }

  /** Method to be called when Back Button is pushed */
  @FXML
  private void onBackPush(ActionEvent event) {
    // difficulty selected is stored. Currently all easy for alpha
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    zenReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.EXTRA_GAMES));
  }
}
