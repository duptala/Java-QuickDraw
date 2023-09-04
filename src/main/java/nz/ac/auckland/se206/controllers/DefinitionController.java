package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;
import nz.ac.auckland.se206.dict.DictionaryLookup;
import nz.ac.auckland.se206.dict.WordInfo;
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.panes.WordPane;

public class DefinitionController {
  @FXML private Button backCanvas;

  @FXML private Button searchForDefinitionsButton;
  @FXML private ProgressBar progressBar;
  @FXML private Accordion resultsAccordion;

  @FXML private VBox backgroundPane;

  /**
   * THe function is to switch the scene to canvas page
   *
   * @param event the information store in the button
   */
  @FXML
  private void onSwitchCanvas(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    scene.setRoot(SceneManager.getRoot(Page.CANVAS));
  }

  /** the function is to create a task to do the most time consuming thing- look up definition. */
  public void searchWords() {
    progressBar.progressProperty().unbind();

    resultsAccordion.getPanes().clear();
    progressBar.setProgress(0);

    String[] queryWords = ReadyController.getRandomWord().split("\\s+");

    int numberOfWords = queryWords.length;

    if (numberOfWords == 0) {
      Alert alert = new Alert(AlertType.ERROR, "You need to provide at least one word.");
      alert.showAndWait();
      return;
    }

    long startTime = System.currentTimeMillis();

    searchForDefinitionsButton.setDisable(true);
    backCanvas.setDisable(true);

    Task<Void> backgroundTask =
        new Task<Void>() {

          // make our searching progress as a background task
          protected Void call() throws Exception {

            updateProgress(0, 1);
            for (int s = 0; s < numberOfWords; s++) {
              String query = queryWords[s];
              try {
                WordInfo wordResult = DictionaryLookup.searchWordInfo(query);
                // the pane that show the word definition
                TitledPane pane = WordPane.generateWordPane(query, wordResult);

                Platform.runLater(
                    () -> {
                      resultsAccordion.getPanes().add(pane);
                    });

              } catch (IOException e) {
                e.printStackTrace();
              } catch (WordNotFoundException e) {

                TitledPane pane = WordPane.generateErrorPane(e);

                Platform.runLater(
                    () -> {
                      resultsAccordion.getPanes().add(pane);
                    });
              }

              // upadte the progress show it is not freeze
              updateProgress(s + 1.0, numberOfWords);
            }

            return null;
          }
        };

    progressBar.progressProperty().bind(backgroundTask.progressProperty());

    // after searching word successfully, the button would be used again
    backgroundTask.setOnSucceeded(
        event -> {
          searchForDefinitionsButton.setDisable(false);
          backCanvas.setDisable(false);
        });

    Thread backgroundThread = new Thread(backgroundTask);
    backgroundThread.start();
  }
}
