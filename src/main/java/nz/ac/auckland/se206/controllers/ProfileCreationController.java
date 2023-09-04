package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.Badge;
import nz.ac.auckland.se206.Profile;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;

public class ProfileCreationController {

  private static boolean fileExist = false;
private static Profile currentprofile;
  /**
   * THe function is used to create the file.
   *
   * @param playerName the user want the file to name
   * @return folder that named by user
   * @throws IOException the exception needed for file operation
   */
  public static File createFile(String playerName) throws IOException {
    // initialise the file tha create in to the guest file
    File initialFile = new File("Guest");
    File playerNameFile = new File(initialFile, playerName);
    File playerTxtFile = new File(playerNameFile, playerName + ".txt");
    File WordTxtFile = new File(playerNameFile, "Word Enounter.txt");
    File lastDifficult = new File(playerNameFile, "Difficulty.txt");
    File difficultyTimeFile = new File(playerNameFile, "Difficulty Time.txt");
    File difficultyAccuracyFile = new File(playerNameFile, "Difficulty Accuracy.txt");
    File difficultyConfidenceFile = new File(playerNameFile, "Difficulty Confidence.txt");
    File badgesEarnedTxtFile = new File(playerNameFile, "BadgesEarned.txt");
    File badgesNotEarnedFile = new File(playerNameFile, "BadgesNotEarned.txt");
    if (!initialFile.exists()) {
      initialFile.mkdir();
    }
    // The userNameFile would under the Guest File
    if (!playerNameFile.exists()) {

      playerNameFile.mkdir();
      fileExist = false;

    } else {
      fileExist = true;
    }
    // create the txt file
    if (!playerTxtFile.exists()) {
      playerTxtFile.createNewFile();
    }

    if (!WordTxtFile.exists()) {
      WordTxtFile.createNewFile();
    }

    if (!difficultyTimeFile.exists()) {
      difficultyTimeFile.createNewFile();
    }
    // read the first line to check whether it is null
    FileReader timeFileReader = new FileReader(difficultyTimeFile);
    BufferedReader timeReader = new BufferedReader(timeFileReader);
    String timeString = timeReader.readLine();
    timeReader.close();
    timeFileReader.close();
    if (timeString == null) {
      FileWriter timeWrite = new FileWriter(difficultyTimeFile);
      BufferedWriter timeBufferedWriter = new BufferedWriter(timeWrite);
      timeBufferedWriter.write("Time 60"); // initialise the easy mode
      timeBufferedWriter.newLine();
      timeBufferedWriter.close();
      timeWrite.close();
    }

    if (!lastDifficult.exists()) {
      lastDifficult.createNewFile(); // create the file if do not exist
    }
    FileReader wordFileReader = new FileReader(lastDifficult);
    BufferedReader wordReader = new BufferedReader(wordFileReader);
    String wordString = wordReader.readLine();
    wordReader.close(); // / read the first line to check whether need to initialise
    wordFileReader.close();

    if (wordString == null) {
      FileWriter wordFileWriter = new FileWriter(lastDifficult);
      BufferedWriter wordBufferedWriter = new BufferedWriter(wordFileWriter);
      wordBufferedWriter.write("Word Easy"); // write the word easy message into the file
      wordBufferedWriter.newLine();
      wordBufferedWriter.close();
      wordFileWriter.close();
    }

    if (!difficultyAccuracyFile.exists()) {
      difficultyAccuracyFile.createNewFile();
    }
    // read the first line to check whether need to initialise
    FileReader accuracyFileReader = new FileReader(difficultyAccuracyFile);
    BufferedReader accuracyReader = new BufferedReader(accuracyFileReader);
    String accuracyString = accuracyReader.readLine();
    accuracyReader.close();
    accuracyFileReader.close();
    if (accuracyString == null) {
      FileWriter accuracyFileWriter = new FileWriter(difficultyAccuracyFile);
      BufferedWriter accuracyBufferedWriter = new BufferedWriter(accuracyFileWriter);
      accuracyBufferedWriter.write("Accuracy 3"); // initialise the accuracy
      accuracyBufferedWriter.newLine();
      accuracyBufferedWriter.close();
      accuracyFileWriter.close();
    }

    if (!difficultyConfidenceFile.exists()) {
      difficultyConfidenceFile.createNewFile();
    }
    // read the first line to check whether need to initialise
    FileReader confidenceFileReader = new FileReader(difficultyConfidenceFile);
    BufferedReader confidenceReader = new BufferedReader(confidenceFileReader);
    String confidenceString = confidenceReader.readLine();
    confidenceReader.close();
    confidenceFileReader.close();
    // check whether the file is empty
    if (confidenceString == null) {
      FileWriter confidenceFileWriter = new FileWriter(difficultyConfidenceFile, true);
      BufferedWriter confidenceBufferedWriter = new BufferedWriter(confidenceFileWriter);
      confidenceBufferedWriter.write("Confidence Easy");
      confidenceBufferedWriter.newLine();
      confidenceBufferedWriter.close();
      confidenceFileWriter.close();

      if (!badgesEarnedTxtFile.exists()) {
        badgesEarnedTxtFile.createNewFile();
      }
      if (!badgesNotEarnedFile.exists()) {
        badgesEarnedTxtFile.createNewFile();
      }
    }
    return playerNameFile;
  }

  /**
   * The method is to wrtie the data in to the profile
   *
   * @param currentProfile the profile that current working on
   * @throws IOException the exception needed for file operation
   */
  public static void insertData(Profile currentProfile) throws IOException {

    ArrayList<String> wordsEncountered = currentProfile.getWordsEncountered();
    String currentProfileName = currentProfile.getName();
    File usernameFile = createFile(currentProfileName);
    File playerNameTxtFile = new File(usernameFile, currentProfileName + ".txt");
    File badgesEarnedTxtFile = new File(usernameFile, "BadgesEarned.txt");
    File badgesNotEarnedFile = new File(usernameFile, "BadgesNotEarned.txt");
    if (!usernameFile.exists()) {

      usernameFile.mkdir();
    }
    // check whether the file is exist to make one
    if (!playerNameTxtFile.exists()) {
      playerNameTxtFile.createNewFile();
    }
    if (!badgesEarnedTxtFile.exists()) {
      badgesEarnedTxtFile.exists();
    }
    if (!badgesNotEarnedFile.exists()) {
      badgesNotEarnedFile.createNewFile();
    }
    // get what the badges user win in the game and write it into the file
    FileWriter fileBadgeEarnedWriter = new FileWriter(badgesEarnedTxtFile);
    BufferedWriter fileBadgeEarnedBufferedWriter = new BufferedWriter(fileBadgeEarnedWriter);
    ArrayList<Badge> badgesEarnedArrayList = currentProfile.getBadgesEarned();

    for (Badge badgeEarned : badgesEarnedArrayList) {
      fileBadgeEarnedBufferedWriter.write(badgeEarned.getType().toString());
      fileBadgeEarnedBufferedWriter.newLine();
      fileBadgeEarnedBufferedWriter.write(badgeEarned.getBadgeDifficulty().toString());
      fileBadgeEarnedBufferedWriter.newLine();
    }
    fileBadgeEarnedBufferedWriter.close();
    fileBadgeEarnedWriter.close();

    // write the badges that the users do not get in the game
    FileWriter fileBadgeNotEarnedWriter = new FileWriter(badgesNotEarnedFile);
    BufferedWriter fileBadgeNotEarnedBufferedWriter = new BufferedWriter(fileBadgeNotEarnedWriter);
    ArrayList<Badge> badgesNotEarnedArrayList = currentProfile.getBadgesNotEarned();
    for (Badge badgeNotEarned : badgesNotEarnedArrayList) {
      fileBadgeNotEarnedBufferedWriter.write(badgeNotEarned.getType().toString());
      fileBadgeNotEarnedBufferedWriter.newLine();
      fileBadgeNotEarnedBufferedWriter.write(badgeNotEarned.getBadgeDifficulty().toString());
      fileBadgeNotEarnedBufferedWriter.newLine();
    }
    fileBadgeNotEarnedBufferedWriter.close();
    fileBadgeNotEarnedWriter.close();

    // using file wrtier would automaticaly overlap the last data
    FileWriter fileWriter = new FileWriter(playerNameTxtFile);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    // write the data in to the txt file
    bufferedWriter.write("Wins : ");
    bufferedWriter.newLine();
    bufferedWriter.write(String.valueOf(currentProfile.getWins()));
    bufferedWriter.newLine();

    bufferedWriter.write("Loses : ");
    bufferedWriter.newLine();
    bufferedWriter.write(String.valueOf(currentProfile.getLosses()));
    bufferedWriter.newLine();

    bufferedWriter.write("Fast Time : ");
    bufferedWriter.newLine();
    bufferedWriter.write(String.valueOf(currentProfile.getFastestTime()));
    bufferedWriter.newLine();

    bufferedWriter.write("Word Last Encounter : ");
    bufferedWriter.newLine();
    // write toe word that encounter in the current game
    for (String word : wordsEncountered) {
      bufferedWriter.write(word);
      bufferedWriter.newLine();
    }

    bufferedWriter.close();
    fileWriter.close();
  }

  @FXML private Button backButton;
  @FXML private Button confirmButton;
  @FXML private TextField profileNameTextField;

  /**
   * the function is to change to the profile scene
   *
   * @param event the information needed that store in the button
   */
  @FXML
  private void onBackPush(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();

    scene.setRoot(SceneManager.getRoot(Page.PROFILE_PAGE));
  }

  /**
   * The function is to confirm that the user want to create a new file
   *
   * @param event the information needed that store in the button
   * @throws IOException the exception for the file operations
   */
  @FXML
  private void onConfirmPush(ActionEvent event) throws IOException {
    String playerName = profileNameTextField.getText();
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    if (playerName.length() > 0) {
      createFile(playerName);
      if (fileExist) { // file already exist in the folder
        Alert fileNameRepeatAlert = new Alert(AlertType.INFORMATION);
        fileNameRepeatAlert.setTitle("File Exist"); // set the sentence on the window
        fileNameRepeatAlert.setHeaderText("Change a name please");
        fileNameRepeatAlert.showAndWait();
      } else {
        // create the foler and two txt files
        Profile newProfile =
            new Profile(
                playerName, 0, 0, -1, 0, true, new ArrayList<Badge>(), Badge.getAllBadges());
        ProfilePageController.addProfile(newProfile);
        insertData(newProfile);
        scene.setRoot(SceneManager.getRoot(Page.PROFILE_PAGE));
      }

    } else { // user type nothing would pop up an alert
      Alert noInputAlert = new Alert(AlertType.INFORMATION);
      noInputAlert.setTitle("File Name empty");
      noInputAlert.setHeaderText("Type a name for your file please");
      noInputAlert.showAndWait();
    }
    ProfilePageController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.PROFILE_PAGE));
  }
}
