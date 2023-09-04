package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;

public class ExtraGamesController {
  private static int time = 60;

  private static boolean chooseMystery = false;


  public static boolean isChooseMystery() {
    return chooseMystery;
  }

  public static void setChooseMystery(boolean chooseMystery) {
    ExtraGamesController.chooseMystery = chooseMystery;
  }


  @FXML private Button backButton;
  @FXML private Button zenButton;
  @FXML private Button mysteryWordButton;

  /**
   * change to the zen game mode page
   *
   * @param event the information needed get from button
   */
  @FXML
  public void onZenPush(ActionEvent event) {
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.DIFFICULTY_ZEN));
  }

  /**
   * the function is to change to the mystery game mode.
   *
   * @param even the information needed get from button
   */
  @FXML
  public void onMysteryWordPush(ActionEvent event) {
    // If the mystery word mode is selected
    // Time is set to 60 seconds
    time = 60;
    // Page remembers its in mystery word mode
    chooseMystery = true;
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    ReadyController.getTimeline().playFromStart();
    scene.setRoot(SceneManager.getRoot(Page.READY));
  }

  /**
   * the function is to change to the main menu page.
   *
   * @param event the information needed get from button
   */
  @FXML
  private void onBackPush(ActionEvent event) {
    Scene scene = ((Button) event.getSource()).getScene();
    scene.setRoot(SceneManager.getRoot(Page.MAIN_MENU));
  }
}
