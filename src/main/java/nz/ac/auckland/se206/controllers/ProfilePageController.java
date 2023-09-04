package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.util.Duration;
import nz.ac.auckland.se206.Badge;
import nz.ac.auckland.se206.Badge.BadgeDifficulty;
import nz.ac.auckland.se206.Badge.BadgeType;
import nz.ac.auckland.se206.DifficultyBadge.DifficultyBadgeType;
import nz.ac.auckland.se206.Profile;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;
import nz.ac.auckland.se206.UniversalBadge.UniversalBadgeType;

public class ProfilePageController {

  private static int profileTracker = 0;
  private static int profileListSize;
  private static ArrayList<Profile> profileList = new ArrayList<Profile>();
  private static Timeline checkNumberProfilesTimeline;
  private static int numberOfButtons;
  private static int wins, loses, fast;
  private static ArrayList<String> fileNameList = new ArrayList<>();
  private static ArrayList<Badge> badgesEarnedArrayList;
  private static ArrayList<Badge> badgesNotEarnedArrayList;

  public static Timeline getTimeline() {
    return checkNumberProfilesTimeline;
  }

  protected static void addProfile(Profile profile) {
    profileList.add(profile);
  }

  /**
   * THe function is used to add the file into profile list already in the Guest file
   *
   * @throws IOException
   */
  public static void addExistFile() throws IOException {
    // TODO Auto-generated method stub
    File guestFile = new File("Guest");

    if (!guestFile.exists()) {
      guestFile.mkdir();
    }
    File fileresult[] = guestFile.listFiles();

    for (File file : fileresult) {
      // the file is direcotry means is the user created before
      if (file.isDirectory()) {
        fileNameList.add(file.getName());

        ///// final Earned Badges//////

        File earnFile = new File("Guest/" + file.getName() + "/" + "BadgesEarned.txt");

        badgesEarnedArrayList = badgeRead(earnFile);

        //// final Not Earned Badges////
        File notEarnFile = new File("Guest/" + file.getName() + "/" + "BadgesNotEarned.txt");
        badgesNotEarnedArrayList = badgeRead(notEarnFile);

        FileReader fileReader =
            new FileReader("Guest/" + file.getName() + "/" + file.getName() + ".txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        bufferedReader.readLine();
        int winTimes = Integer.valueOf(bufferedReader.readLine());
        wins = winTimes;

        bufferedReader.readLine();
        int loseTime = Integer.valueOf(bufferedReader.readLine());
        loses = loseTime;

        bufferedReader.readLine();
        int fastTime = Integer.valueOf(bufferedReader.readLine());
        fast = fastTime;

        profileList.add(
            new Profile(
                file.getName(),
                winTimes,
                loseTime,
                fastTime,
                0,
                true,
                badgesEarnedArrayList,
                badgesNotEarnedArrayList));
        fileReader.close();
        bufferedReader.close();
        // using word to make sure when reading the file would not skip line
        String word;
        FileReader wordReader =
            new FileReader("Guest/" + file.getName() + "/" + "Word Enounter.txt");
        BufferedReader wordBufferedReader = new BufferedReader(wordReader);
        Profile.setProfile(
            new Profile(
                file.getName(),
                wins,
                loses,
                fast,
                0,
                true,
                badgesEarnedArrayList,
                badgesNotEarnedArrayList));
        while ((word = wordBufferedReader.readLine()) != null) {
          // add to the arraylist used to generate random word that would not show again
          Profile.getProfile().addPreviousWordEncountered(word);
        }

        wordReader.close();
        wordBufferedReader.close();
      }
    }
  }

  /**
   * THe function is used to set the field type and difficult stores in the local file
   *
   * @param earnFile the input that stors the data
   * @return arraylist that have the badges based on data
   * @throws IOException the exception needed for file operation
   */
  private static ArrayList<Badge> badgeRead(File earnFile) throws IOException {
    String line;
    // using the line so that bufferedreader would read everyline
    ArrayList<Badge> badgesEarnedArrayList = new ArrayList<>();
    FileReader fileEarnReader = new FileReader(earnFile);
    BufferedReader bufferedEarnReader = new BufferedReader(fileEarnReader);
    while ((line = bufferedEarnReader.readLine()) != null) {
      String difficultLine = bufferedEarnReader.readLine();
      // when end to the file would break
      if (line == null || difficultLine == null) {
        break;
      }
      BadgeType type = null;
      BadgeDifficulty difficulty = null;

      switch (line) {
        case "UNDEFEATED":
          { // different type based on the file
            type = UniversalBadgeType.UNDEFEATED;
            break;
          }
        case "SEE_THE_IMPOSSIBLE":
          {
            type = UniversalBadgeType.SEE_THE_IMPOSSIBLE;
            break;
          }
        case "DO_THE_IMPOSSIBLE":
          {
            type = UniversalBadgeType.DO_THE_IMPOSSIBLE;
            break;
          }
        case "CLOSE_ONE":
          {
            type = UniversalBadgeType.CLOSE_ONE;
            break;
          }
        case "HELLO_WORLD":
          {
            type = UniversalBadgeType.HELLO_WORLD;
            break;
          }
        case "EXPERIENCED":
          {
            type = UniversalBadgeType.EXPERIENCED;
            break;
          }
        case "WINSTREAK_1":
          {
            type = UniversalBadgeType.WINSTREAK_1;
            break;
          }
        case "WINSTREAK_2":
          {
            type = UniversalBadgeType.WINSTREAK_2;
            break;
          }
        case "WINSTREAK_3":
          {
            type = UniversalBadgeType.WINSTREAK_3;
            break;
          }
        case "FAST":
          {
            type = DifficultyBadgeType.FAST;
            break;
          }
        case "LIGHTNING":
          {
            type = DifficultyBadgeType.LIGHTNING;
            break;
          }
        default:
          System.out.println("TYpe Wrong");
          break;
      }

      switch (difficultLine) {
        case "UNIVERSAL":
          {
            difficulty = BadgeDifficulty.UNIVERSAL;
            break;
          }
        case "AMATEUR":
          {
            difficulty = BadgeDifficulty.AMATEUR;
            break;
          }

        case "VETERAN":
          {
            difficulty = BadgeDifficulty.VETERAN;
            break;
          }

        case "MASTER":
          {
            difficulty = BadgeDifficulty.MASTER;
            break;
          }

        case "GRANDMASTER":
          {
            difficulty = BadgeDifficulty.GRANDMASTER;
            break;
          }

        default:
          System.out.println("Difficulty Wrong");
          break;
      }

      // get the badge and add in to the arraylist would return
      Badge badge = Badge.getBadge(type, difficulty);

      badgesEarnedArrayList.add(badge);
    }
    bufferedEarnReader.close();
    fileEarnReader.close();
    return badgesEarnedArrayList;
  }

  @FXML private Button leftProfileButton;

  @FXML private Button centerProfileButton;

  @FXML private Button rightProfileButton;

  @FXML private Button leftButton;

  @FXML private Button rightButton;

  @FXML private Button selectButton;

  @FXML private Button statisticsButton;

  public void initialize() throws IOException {
    addExistFile();
    // only do not have guest file would create one
    if (!fileNameList.contains("Guest")) {
      Profile newProfile =
          new Profile("Guest", 0, 0, -1, 0, true, new ArrayList<Badge>(), Badge.getAllBadges());
      ProfileCreationController.insertData(newProfile);
      profileList.add(newProfile);

      Profile.setProfile(newProfile);
    }

    profileListSize = profileList.size();

    checkNumberProfiles();

    checkNumberProfilesTimeline = new Timeline();

    checkNumberProfilesTimeline.setCycleCount(Timeline.INDEFINITE);

    EventHandler<ActionEvent> checkNumberProfilesHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            checkNumberProfiles();

            checkNumberProfilesTimeline.stop();
          }
        };
    KeyFrame checkNumberProfilesKeyFrame =
        new KeyFrame(Duration.millis(1), checkNumberProfilesHandler);

    checkNumberProfilesTimeline.getKeyFrames().add(checkNumberProfilesKeyFrame);
  }

  @FXML
  private void onLeftPush(ActionEvent event) {

    moveLeft();
  }

  @FXML
  private void onRightPush(ActionEvent event) {

    moveRight();
  }

  @FXML
  private void onLeftProfilePush(ActionEvent event) {
    moveLeft();

    enableButtons();
  }

  @FXML
  private void onCenterProfilePush(ActionEvent event) {
    enableButtons();
  }

  @FXML
  private void onRightProfilePush(ActionEvent event) {
    moveRight();
    enableButtons();
  }

  @FXML
  private void onSelectPush(ActionEvent event) {
    Profile.setProfile(profileList.get(profileTracker));

    returnToMainMenu(event);
  }

  @FXML
  private void onNewProfilePush(ActionEvent event) {
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.PROFILE_CREATION_PAGE));
  }

  @FXML
  private void onStatisticsPush(ActionEvent event) {
    Profile.setProfile(profileList.get(profileTracker));
    StatisticsController.getTimeline().playFromStart();

    Scene scene = ((Button) event.getSource()).getScene();

    scene.setRoot(SceneManager.getRoot(Page.STATISTICS));
  }

  /** Sets the text of the buttons that display the profile. */
  private void setButtonsText() {
    // The Buttons which are currently displayed have their text set to the name of
    // the current
    // profile tracked, as well as the profiles adjacent to that one depending on
    // how many buttons
    // are displayed
    if (numberOfButtons == 3) {

      leftProfileButton.setText(profileList.get(profileTracker - 1).getName());

      centerProfileButton.setText(profileList.get(profileTracker).getName());

      rightProfileButton.setText(profileList.get(profileTracker + 1).getName());
    } else if (numberOfButtons == 2) {
      centerProfileButton.setText(profileList.get(profileTracker).getName());

      rightProfileButton.setText(profileList.get(profileTracker + 1).getName());
    } else if (numberOfButtons == 0) {
      leftProfileButton.setText(profileList.get(profileTracker - 1).getName());

      centerProfileButton.setText(profileList.get(profileTracker).getName());
    } else if (numberOfButtons == 1) {
      centerProfileButton.setText(profileList.get(profileTracker).getName());
    }
  }

  private void moveLeft() {
    // make the trackedProfile look like goes to left
    if (profileTracker > 0) {

      profileTracker--;
      // If the left end of the profile list is reached, the left button is hidden as
      // there is no
      // profile to the left of the tracked profile
      if (profileTracker == 0) {
        leftProfileButton.setVisible(false);
        centerProfileButton.setVisible(true);
        rightProfileButton.setVisible(true);
        numberOfButtons = 2;
        // Otherwise, all three buttons can be displayed.
      } else {
        leftProfileButton.setVisible(true);
        centerProfileButton.setVisible(true);
        rightProfileButton.setVisible(true);
        numberOfButtons = 3;
      }
      // make the button to match the text
      setButtonsText();
    }
  }

  private void moveRight() {
    // make the tracked profile one to the right, and changes the display
    if (profileTracker < profileListSize - 1) {

      profileTracker++;

      // If the right end of the profile list is reached, the right button is hidden
      // as there is no
      // profile to the right
      if (profileTracker == profileListSize - 1) {
        leftProfileButton.setVisible(true);
        centerProfileButton.setVisible(true);
        rightProfileButton.setVisible(false);
        numberOfButtons = 0;
        // Otherwise, all three buttons are displayd
      } else {
        leftProfileButton.setVisible(true);
        centerProfileButton.setVisible(true);
        rightProfileButton.setVisible(true);
        numberOfButtons = 3;
      }
      // make the button to match the text
      setButtonsText();
    }
  }

  private void returnToMainMenu(ActionEvent event) {
    // The current profile is set to the one tracked
    Profile.setProfile(profileList.get(profileTracker));
    // The current profile is displayed on main menu
    MainMenuController.getTimeline().playFromStart();

    Button button = (Button) event.getSource();

    Scene scene = button.getScene();

    scene.setRoot(SceneManager.getRoot(Page.MAIN_MENU));
  }

  // make the button can click
  private void enableButtons() {

    selectButton.setDisable(false);

    statisticsButton.setDisable(false);
  }

  /** The number of profiles is checked to see how many buttons should be displayed */
  private void checkNumberProfiles() {
    profileListSize = profileList.size();
    // no file, nothing would show. Should never occur.
    if (profileListSize < 1) {
      leftProfileButton.setVisible(false);
      centerProfileButton.setVisible(false);
      rightProfileButton.setVisible(false);
      leftButton.setDisable(true);
      rightButton.setDisable(true);
      numberOfButtons = 0;
      // only one file the middle button would show and be enable
    } else if (profileListSize == 1) {
      leftProfileButton.setVisible(false);
      centerProfileButton.setVisible(true);
      // Middle buttons text is set to the name of the profile
      centerProfileButton.setText(profileList.get(0).getName());
      rightProfileButton.setVisible(false);
      leftButton.setDisable(true);
      rightButton.setDisable(true);
      // State of Buttons is recorded
      numberOfButtons = 1;
      // two files would make the middle and right button show/be enabled
    } else if (profileListSize == 2) {
      leftProfileButton.setVisible(false);
      centerProfileButton.setVisible(true);
      rightProfileButton.setVisible(true);
      // The buttons which are displayed have their text set
      centerProfileButton.setText(profileList.get(0).getName());
      rightProfileButton.setText(profileList.get(1).getName());
      leftButton.setDisable(false);
      rightButton.setDisable(false);
      // Button state recorded
      numberOfButtons = 2;
      // All Three buttons are visible and enabled otherwise
    } else if (profileListSize == 3) {
      leftProfileButton.setVisible(true);
      centerProfileButton.setVisible(true);
      rightProfileButton.setVisible(true);
      // Their text is all set
      leftProfileButton.setText(profileList.get(0).getName());
      centerProfileButton.setText(profileList.get(1).getName());
      rightProfileButton.setText(profileList.get(2).getName());
      leftButton.setDisable(false);
      rightButton.setDisable(false);
      // Their states all recorded
      numberOfButtons = 3;
    } else {
      leftProfileButton.setVisible(true);
      centerProfileButton.setVisible(true);
      rightProfileButton.setVisible(true);
      leftButton.setDisable(false);
      rightButton.setDisable(false);

      numberOfButtons = 3;
    }
  }
}
