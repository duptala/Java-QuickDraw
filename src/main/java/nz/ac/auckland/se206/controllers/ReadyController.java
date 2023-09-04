package nz.ac.auckland.se206.controllers;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileOutputStream;
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

public class ReadyController {

  private static Timeline getWordTimeline;
  private static Profile currentProfile;
  private static String randomWord;



  protected static Timeline getTimeline() {
    return getWordTimeline;
  }


  public static String getRandomWord() {
    return randomWord;
  }

  @FXML private Button readyButton;
  @FXML private ButtonBar buttonBar;
  @FXML private Button noButton;
  @FXML private Label readyLabel;
  @FXML private Label wordLabel;

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
                categorySelector.getRandomCategory(DifficultyPageController.getWordDifficulty());
            // the word is displayed, and this is done once per timeline

            if (ExtraGamesController.isChooseMystery()) {
              wordLabel.setText("Target word: " + "? ? ? ?");
            } else {
              wordLabel.setText("Target word: " + randomWord);
            }
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

    if (ExtraGamesController.isChooseMystery()) { // do not choose
      scene.setRoot(SceneManager.getRoot(Page.EXTRA_GAMES));
      ExtraGamesController.setChooseMystery(false);

    } else {
      scene.setRoot(SceneManager.getRoot(Page.DIFFICULTY_PAGE));
    }
  }

  /** Method to be run on ready button push */
  @FXML
  private void onReadyPush(ActionEvent event) throws IOException, CsvException, URISyntaxException {
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();

    // CanvasController's timelines functionalities are started, and the wrod is
    // displayed
    (CanvasController.getTimeLine().get(0)).playFromStart();
    (CanvasController.getTimeLine().get(1)).playFromStart();

    if (ExtraGamesController.isChooseMystery()) {
      CanvasController.configureSettings(randomWord, 60, 3, 1.00);
    } else {
      CanvasController.configureSettings(
          randomWord,
          DifficultyPageController.getTime(),
          DifficultyPageController.getAccuracy(),
          DifficultyPageController.getConfidence());
    }

    Profile.getProfile().addWordEncountered(randomWord);
    currentProfile = Profile.getProfile();
    String currentProfileName = currentProfile.getName();
    File userNameFile = ProfileCreationController.createFile(currentProfileName);
    // create a new txt file that would store all the word even in the previous game
    File wordTxtFile = new File(userNameFile, "Word Enounter.txt");
    FileOutputStream outputWord = new FileOutputStream(wordTxtFile, true);
    // add \n is to create a new line
    String wordOutput = randomWord + "\n";
    byte[] word = wordOutput.getBytes();
    outputWord.write(word);
    outputWord.flush();
    outputWord.close();
    // Scene is changed to CANVAS
    scene.setRoot(SceneManager.getRoot(Page.CANVAS));
  }
}
