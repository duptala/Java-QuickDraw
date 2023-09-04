package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import nz.ac.auckland.se206.controllers.SetDifficultyController.DifficultyType;
import nz.ac.auckland.se206.words.CategorySelect.Difficulty;

public class DifficultyPageController {

  private static Profile currentProfile;
  private static Difficulty wordDifficulty = Difficulty.E;
  private static int accuracy = 3;
  private static int time = 60;
  private static Double confidence = 1.00;
  private static Difficulty overallDifficulty;
  private static String insertFile;
  public static Timeline setTextTimeline;

  public static Difficulty getWordDifficulty() {
    return wordDifficulty;
  }

  public static String getInsertFile() {
    return insertFile;
  }

  public static Timeline getTimeline() {
    return setTextTimeline;
  }

  public static int getAccuracy() {
    return accuracy;
  }

  public static Double getConfidence() {
    return confidence;
  }

  public static int getTime() {
    return time;
  }

  /** the function is to calculate the overall difficulty based on the four difficulties. */
  private static void calculateOverallDifficulty() {
    if (wordDifficulty == Difficulty.E | time == 60 | (confidence - 2.00) < 0 | accuracy == 3) {
      overallDifficulty = Difficulty.E;
    } else if (wordDifficulty == Difficulty.M
        | time == 45
        | (confidence - 11.00) < 0
        | accuracy == 2) {
      overallDifficulty = Difficulty.M;
    } else if (wordDifficulty == Difficulty.H | time == 30 | (confidence - 26.00) < 0) {
      overallDifficulty = Difficulty.H;
    } else {
      overallDifficulty = Difficulty.Ma;
    }
  }

  public static Difficulty getOverallDifficulty() {
    return overallDifficulty;
  }

  /**
   * The function is to set the confidence and save it in to the file.
   *
   * @param inputConfidence the number that want to change
   * @throws IOException the exception that needed in case
   */
  public static void setConfidence(double inputConfidence) throws IOException {
    confidence = inputConfidence;
    StringBuilder sb = new StringBuilder();
    sb.append("Confidence ");
    if ((inputConfidence - 49.00) > 0.00) {
      sb.append("Master");
    } else if ((inputConfidence - 24.00) > 0.00) {
      sb.append("Hard");
    } else if ((inputConfidence - 9.00) > 0.00) {
      sb.append("Medium");
    } else {
      sb.append("Easy");
    }

    currentProfile = Profile.getProfile();
    String currentProfileName = currentProfile.getName();
    // The file under the guest
    File userNameFile = ProfileCreationController.createFile(currentProfileName);
    // each difficulty save in this file
    File difficultyFile = new File(userNameFile, "Difficulty Confidence.txt");
    if (!difficultyFile.exists()) {
      difficultyFile.createNewFile();
    }
    saveDifficulty(difficultyFile, sb.toString());
  }

  /**
   * The function is to set the Accuracy and save it in to the file.
   *
   * @param inputAccuracy the number that want to change
   * @throws IOException the exception that needed in case
   */
  public static void setAccuracy(int inputAccuracy) throws IOException {
    accuracy = inputAccuracy;
    StringBuilder sb = new StringBuilder();
    sb.append("Accuracy ");
    sb.append(String.valueOf(inputAccuracy));

    currentProfile = Profile.getProfile();
    String currentProfileName = currentProfile.getName();
    // The file under the guest
    File userNameFile = ProfileCreationController.createFile(currentProfileName);
    // each difficulty save in this file
    File difficultyFile = new File(userNameFile, "Difficulty Accuracy.txt");
    if (!difficultyFile.exists()) {
      difficultyFile.createNewFile();
    }
    saveDifficulty(difficultyFile, sb.toString());
  }

  /**
   * The function is to set the Time and save it in to the file.
   *
   * @param inputTime the number that want to change
   * @throws IOException the exception that needed in case
   */
  public static void setTime(int inputTime) throws IOException {
    time = inputTime;
    StringBuilder sb = new StringBuilder();
    sb.append("Time ");
    sb.append(String.valueOf(inputTime));

    currentProfile = Profile.getProfile();
    String currentProfileName = currentProfile.getName();
    // The file under the guest
    File userNameFile = ProfileCreationController.createFile(currentProfileName);
    // each difficulty save in this file
    File difficultyFile = new File(userNameFile, "Difficulty Time.txt");
    if (!difficultyFile.exists()) {
      difficultyFile.createNewFile();
    }
    saveDifficulty(difficultyFile, sb.toString());
  }

  /**
   * The function is to set the Word and save it in to the file.
   *
   * @param inputDifficulty the number that want to change
   * @throws IOException the exception that needed in case
   */
  public static void setWordDifficulty(Difficulty inputDifficulty) throws IOException {
    wordDifficulty = inputDifficulty;
    StringBuilder sb = new StringBuilder();
    sb.append("Word ");
    switch (wordDifficulty) {
      case E:
        sb.append("Easy");
        break;
      case M:
        sb.append("Medium");
        break;

      case H:
        sb.append("Hard");
        break;
      case Ma:
        sb.append("Master");
        break;
    }
    currentProfile = Profile.getProfile();
    String currentProfileName = currentProfile.getName();
    // The file under the guest
    File userNameFile = ProfileCreationController.createFile(currentProfileName);
    // each difficulty save in this file
    File difficultyFile = new File(userNameFile, "Difficulty.txt");
    if (!difficultyFile.exists()) {
      difficultyFile.createNewFile();
    }
    System.out.println("台灯 Word SB" + sb.toString());
    saveDifficulty(difficultyFile, sb.toString());
  }

  /**
   * THis function is to save the difficulty in to the word txt file.
   *
   * @param targetFile the file that would insert data
   * @param difficulty the string that would store in it
   * @throws IOException the file exception needed to prevent mistake
   */
  public static void saveDifficulty(File difficultyFile, String difficulty) throws IOException {
    // TODO Auto-generated method stub

    FileWriter fileWriter = new FileWriter(difficultyFile);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    bufferedWriter.write(difficulty);
    bufferedWriter.newLine();

    bufferedWriter.close();
    fileWriter.close();
  }

  @FXML private Label difficultyLabel;
  @FXML private Button wordButton;
  @FXML private Button accuracyButton;
  @FXML private Button timeButton;
  @FXML private Button confidenceButton;
  @FXML private Button overallDifficultyButton;
  @FXML private Button confirmButton;
  @FXML private Button lastDifficultyButton;

  public void initialize() {
    // Timeline to set the text and colors of the buttons to match their difficulty
    setTextTimeline = new Timeline();
    // Timeline can occur any amount of times
    setTextTimeline.setCycleCount(Timeline.INDEFINITE);
    EventHandler<ActionEvent> setTextEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // The text of the wordDifficulty button is set to its difficulty, and its color
            // is set
            switch (wordDifficulty) {
              case E: // based on the different difficulty set different text and style
                wordButton.setText("Easy");
                setStyle(wordButton, Difficulty.E);
                break;
              case M:
                wordButton.setText("Medium");
                setStyle(wordButton, Difficulty.M);
                break;
              case H:
                wordButton.setText("Hard");
                setStyle(wordButton, Difficulty.H);
                break;
              case Ma:
                wordButton.setText("Master");
                setStyle(wordButton, Difficulty.Ma);
                break;
            }
            // The text and color of the time Button is set
            switch (time) {
              case 60:
                timeButton.setText("Easy");
                setStyle(timeButton, Difficulty.E);
                break;
              case 45:
                timeButton.setText("Medium");
                setStyle(timeButton, Difficulty.M);
                break;
              case 30:
                timeButton.setText("Hard");
                setStyle(timeButton, Difficulty.H);
                break;
              default:
                timeButton.setText("Master");
                setStyle(timeButton, Difficulty.Ma);
                break;
            }
            // The text and color of the accuracy button is set
            switch (accuracy) {
              case 3:
                accuracyButton.setText("Easy");
                setStyle(accuracyButton, Difficulty.E);
                break;
              case 2:
                accuracyButton.setText("Medium");
                setStyle(accuracyButton, Difficulty.M);
                break;
              case 1:
                accuracyButton.setText("Hard");
                setStyle(accuracyButton, Difficulty.H);
                break;
            }
            // The text and color of the confidence button is set
            if ((confidence - 2.00) < 0.00) {
              confidenceButton.setText("Easy");
              setStyle(confidenceButton, Difficulty.E);
            } else if ((confidence - 11.00) < 0.00) {
              confidenceButton.setText("Medium");
              setStyle(confidenceButton, Difficulty.M);
            } else if ((confidence) - 26.00 < 0.00) {
              confidenceButton.setText("Hard");
              setStyle(confidenceButton, Difficulty.H);
            } else {
              confidenceButton.setText("Master");
              setStyle(confidenceButton, Difficulty.Ma);
            }
            // The overallDifficulty is calculated, then it's text and color is set
            calculateOverallDifficulty();
            setStyle(overallDifficultyButton, overallDifficulty);
            switch (overallDifficulty) {
              case E:
                overallDifficultyButton.setText("Easy");
                break;
              case M:
                overallDifficultyButton.setText("Medium");
                break;
              case H:
                overallDifficultyButton.setText("Hard");
                break;
              case Ma:
                overallDifficultyButton.setText("Master");
                break;
            }
            setTextTimeline.stop();
          }
        };
    // This happens with a small delay so users can see the change
    KeyFrame setTextKeyFrame = new KeyFrame(Duration.millis(5), setTextEventHandler);
    setTextTimeline.getKeyFrames().add(setTextKeyFrame);
  }

  /**
   * set the data to the field when click the button
   *
   * @param event the information that needed stored in the button
   */
  @FXML
  private void onWordPush(ActionEvent event) {
    SetDifficultyController.setDifficultyType(DifficultyType.WORD);
    switchToSetDifficulty(event);
  }

  /**
   * set the data to the field when click the button
   *
   * @param event the information that needed stored in the button
   */
  @FXML
  private void onTimePush(ActionEvent event) {
    SetDifficultyController.setDifficultyType(DifficultyType.TIME);
    switchToSetDifficulty(event);
  }

  /**
   * set the data to the field when click the button
   *
   * @param event the information that needed stored in the button
   */
  @FXML
  private void onAccuracyPush(ActionEvent event) {
    SetDifficultyController.getTimeline().playFromStart();
    SetDifficultyController.setDifficultyType(DifficultyType.ACCURACY);
    switchToSetDifficulty(event);
  }

  /**
   * set the data to the field when click the button
   *
   * @param event the information that needed stored in the button
   */
  @FXML
  private void onConfidencePush(ActionEvent event) {
    SetDifficultyController.setDifficultyType(DifficultyType.CONFIDENCE);
    switchToSetDifficulty(event);
  }

  /**
   * set the data to the field when click the button
   *
   * @param event the information that needed stored in the button
   */
  @FXML
  private void onOverallDifficultyPush(ActionEvent event) {
    SetDifficultyController.setDifficultyType(DifficultyType.OVERALL);
    switchToSetDifficulty(event);
  }

  /**
   * the fucntion is to confirm that user choose the difficult they want
   *
   * @param event the information that needed stored in the button
   * @throws IOException the exception that needed for IO
   */
  @FXML
  public void onConfirmPush(ActionEvent event) throws IOException {

    // Overall Difficulty is calculated and displayed.
    calculateOverallDifficulty();
    // Scene is set to READY, and its timeline to get word to be written is played
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    ReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.READY)); // change scene
  }

  /**
   * Sets the color of the button according to its difficulty
   *
   * @param button button to change color
   * @param difficulty to determine the color
   */
  private void setStyle(Button button, Difficulty difficulty) {
    // Each difficulty has an unique color to change the button to
    switch (difficulty) {
      case E:
        // Easy changes the color to Green
        button.setStyle("-fx-background-color: #00FF00; ");
        break;
      case M:
        // Medium changes the color to Orange
        button.setStyle("-fx-background-color: #FFA500; ");
        break;
      case H:
        // Hard changes the color to Salmon
        button.setStyle("-fx-background-color: #E9967A; ");
        break;
      case Ma:
        // Master Changes the color to red
        button.setStyle("-fx-background-color: #ff0000; ");
        break;
    }
  }

  /**
   * The function is to read from the file and change the difficult
   *
   * @param event the information store in the button
   * @throws IOException the exception needs for the file operations
   */
  @FXML
  private void setDifficulty(ActionEvent event) throws IOException {
    currentProfile = Profile.getProfile();
    String currentProfileName = currentProfile.getName();
    FileReader fileReader =
        new FileReader("Guest/" + currentProfileName + "/" + "Difficulty" + ".txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String difficulty = bufferedReader.readLine();
    readFile(difficulty); // the file would only have one line that keep the record.
    bufferedReader.close();
    fileReader.close();

    FileReader fileReaderConfidence =
        new FileReader("Guest/" + currentProfileName + "/" + "Difficulty Confidence" + ".txt");
    BufferedReader bufferedReaderConfidence = new BufferedReader(fileReaderConfidence);
    String confidence = bufferedReaderConfidence.readLine();
    readFile(confidence); // the file would only have one line that keep the record.
    bufferedReaderConfidence.close();
    fileReaderConfidence.close();

    FileReader fileReaderAccuracy =
        new FileReader("Guest/" + currentProfileName + "/" + "Difficulty Accuracy" + ".txt");
    BufferedReader bufferedReaderAccuracy = new BufferedReader(fileReaderAccuracy);
    String Accuracy = bufferedReaderAccuracy.readLine();
    readFile(Accuracy); // the file would only have one line that keep the record.
    bufferedReaderAccuracy.close();
    fileReaderAccuracy.close();

    FileReader fileReaderTime =
        new FileReader("Guest/" + currentProfileName + "/" + "Difficulty Time" + ".txt");
    BufferedReader bufferedReaderTime = new BufferedReader(fileReaderTime);
    String Time = bufferedReaderTime.readLine();
    readFile(Time); // the file would only have one line that keep the record.
    bufferedReaderTime.close();
    fileReaderTime.close();

    setTextTimeline.playFromStart();
  }

  /**
   * the function is toset the difficult based on what read form the file
   *
   * @param difficulty the string that read from the file
   * @throws IOException the exception needs for the file operations
   */
  private void readFile(String difficulty) throws IOException {
    switch (difficulty) {
      case "Word Easy":
        {
          setWordDifficulty(Difficulty.E);
          break;
        }
      case "Word Medium":
        {
          setWordDifficulty(Difficulty.M);
          break;
        }
      case "Word Hard":
        {
          setWordDifficulty(Difficulty.H);
          break;
        }
      case "Word Master":
        {
          setWordDifficulty(Difficulty.Ma);
          break;
        }
      case "Time 60":
        {
          setTime(60);
          break;
        }
      case "Time 45":
        {
          setTime(45);
          break;
        }
      case "Time 30":
        {
          setTime(30);
          break;
        }

      case "Time 15":
        {
          setTime(15);
          break;
        }
      case "Accuracy 3":
        {
          setAccuracy(3);
          break;
        }
      case "Accuracy 2":
        {
          setAccuracy(2);
          break;
        }
      case "Accuracy 1":
        {
          setAccuracy(1);
          break;
        }
      case "Confidence Easy":
        {
          setConfidence(1.00);
          break;
        }
      case "Confidence Medium":
        {
          setConfidence(10.00);
          break;
        }
      case "Confidence Hard":
        {
          setConfidence(25.00);
          break;
        }
      case "Confidence Master":
        {
          setConfidence(50.00);
          break;
        }

      default:
        break;
    }
  }

  private void switchToSetDifficulty(ActionEvent event) {
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.SET_DIFFICULTY_PAGE));
  }
}