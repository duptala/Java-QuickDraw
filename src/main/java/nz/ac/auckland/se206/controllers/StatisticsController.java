package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

public class StatisticsController {
  private static Timeline setTextTimeline;

  private static Profile currentProfile;

  public static Timeline getTimeline() {
    return setTextTimeline;
  }

  @FXML private Label winsLabel;
  @FXML private Label lossesLabel;
  @FXML private Label fastestTimeLabel;
  @FXML private Label profileNameLabel;
  @FXML private Button viewBadgesButton;
  @FXML private Label wordPlayed;
  private static String wordInsert = null;

  public void initialize() {
    setTextTimeline = new Timeline();
    setTextTimeline.setCycleCount(Timeline.INDEFINITE);

    EventHandler<ActionEvent> loadGameEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            currentProfile = Profile.getProfile();
            profileNameLabel.setText(currentProfile.getName());
            winsLabel.setText(String.valueOf(currentProfile.getWins()));
            lossesLabel.setText(String.valueOf(currentProfile.getLosses()));
            String wordString;
            FileReader wordReader = null;
            StringBuilder sb = new StringBuilder();
            try {
              wordReader =
                  new FileReader("Guest/" + currentProfile.getName() + "/" + "Word Enounter.txt");
            } catch (FileNotFoundException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
            BufferedReader wordBufferedReader = new BufferedReader(wordReader);

            try {

              while ((wordString = wordBufferedReader.readLine()) != null) {
                wordInsert = wordString + ",";

                sb.append(wordInsert);
              }
              String finalInsertString;
              if (sb.toString().length() != 0) {
                finalInsertString = sb.toString().substring(0, sb.toString().length() - 1);
              } else {
                finalInsertString = sb.toString().substring(0, sb.toString().length());
              }

              wordPlayed.setText(finalInsertString);
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }

            try {
              wordReader.close();
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
            try {
              wordBufferedReader.close();
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
            if (currentProfile.getFastestTime() == -1) {
              fastestTimeLabel.setText("N/A");
            } else {
              fastestTimeLabel.setText(
                  String.valueOf(currentProfile.getFastestTime() + " seconds"));
            }
            setTextTimeline.stop();
          }
        };
    KeyFrame setTextKeyFrame = new KeyFrame(Duration.millis(1), loadGameEventHandler);
    setTextTimeline.getKeyFrames().add(setTextKeyFrame);
  }

  public void selectProfile(Profile profile) {
    profileNameLabel.setText(profile.getName());
    winsLabel.setText(String.valueOf(profile.getWins()));
    lossesLabel.setText(String.valueOf(profile.getLosses()));
    fastestTimeLabel.setText(String.valueOf(profile.getFastestTime()));
  }

  @FXML
  private void onViewBadgesPush(ActionEvent event) {
    DisplayBadgesController.getTimeline().playFromStart();
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.DISPLAY_BADGES));
  }

  @FXML
  private void onReturnPush(ActionEvent event) {
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.PROFILE_PAGE));
  }
}
