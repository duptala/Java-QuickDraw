package nz.ac.auckland.se206.controllers;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.Profile;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;
import nz.ac.auckland.se206.words.CategorySelect;

public class zenReadyController {

  private static Timeline getWordTimeline;
  private static Profile currentProfile;

  protected static Timeline getTimeline() {
    return getWordTimeline;
  }

  @FXML private Button readyButton;
  @FXML private ButtonBar buttonBar;
  @FXML private Button noButton;
  @FXML private Label readyLabel;
  @FXML private Label wordLabel;

  private String randomWord;

  public void initialize() {
    // getWordTimeline set up. This is run every time the player goes to READY
    getWordTimeline = new Timeline();
    getWordTimeline.setCycleCount(Timeline.INDEFINITE);

    EventHandler<ActionEvent> getWordEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // getWordTimeline will generate a new word of difficulty from the getPage's
            // difficulty
            CategorySelect categorySelector = null;
            try {
              categorySelector = new CategorySelect();
            } catch (IOException | CsvException | URISyntaxException e) {
              e.printStackTrace();
            }
            randomWord =
                categorySelector.getRandomCategory(zenDifficultyController.getWordDifficulty());
            // the word is displayed, and this is done once per timeline
            wordLabel.setText("Target word: " + randomWord);
            getWordTimeline.stop();
          }
        };

    KeyFrame getWordKeyFrame = new KeyFrame(Duration.millis(1), getWordEventHandler);
    getWordTimeline.getKeyFrames().add(getWordKeyFrame);
  }

  /** Method to be run on No button push. Returns user to MAIN_MENU */
  @FXML
  private void onNoPush(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();

    scene.setRoot(SceneManager.getRoot(Page.DIFFICULTY_ZEN));
  }

  /** Method to be run on ready button push */
  @FXML
  private void onReadyPush(ActionEvent event) throws IOException, CsvException, URISyntaxException {
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();

    // CanvasController's timelines functionalities are started, and the wrod is
    // displayed
    // (CanvasController.getTimeLine().get(0)).playFromStart();
    (zenModeController.getTimeLine().get(0)).playFromStart();
    (zenModeController.getTimeLine().get(1)).playFromStart();
    zenModeController.configureSettings(randomWord);

    // Profile.getProfile().addWordEncountered(randomWord);
    // currentProfile = Profile.getProfile();
    // String currentProfileName = currentProfile.getName();
    // File userNameFile = ProfileCreationController.createFile(currentProfileName);
    // // create a new txt file that would store all the word even in the previous
    // game
    // File wordTxtFile = new File(userNameFile, "Word Enounter.txt");
    // FileOutputStream outputWord = new FileOutputStream(wordTxtFile, true);
    // // add \n is to create a new line
    // String wordOutput = randomWord + "\n";
    // byte[] word = wordOutput.getBytes();
    // outputWord.write(word);
    // outputWord.flush();
    // outputWord.close();
    // System.out.println("---------------");
    // Scene is changed to CANVAS
    scene.setRoot(SceneManager.getRoot(Page.ZEN_MODE));
  }
}
