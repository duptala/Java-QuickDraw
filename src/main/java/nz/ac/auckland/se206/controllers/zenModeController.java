package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Page;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class zenModeController {

  private static Timeline counterTimeline;
  private static Timeline loadGameTimeline;
  private static Timeline downloadImageTimeline;
  private static String randomWord = "";
  private static int countdownTime;
  private static Double confidence;
  private static int accuracy;
  private static int place;
  private static int time;

  @FXML private Canvas canvas;
  @FXML private Label wordLabel;
  @FXML private Label timerLabel;
  @FXML private Label predictionsLabel;
  @FXML private Button finishButton;
  @FXML private Button eraserButton;
  @FXML private Label wordRankUpdateLbl;
  @FXML private Button downloadBtn;
  @FXML private ColorPicker colorPicker;

  private GraphicsContext graphic;
  private DoodlePrediction model;
  private ArrayList<String> predictions = null;
  private ArrayList<Double> probabilities = null;
  private TextToSpeech textToSpeech;
  private double currentX;
  private double currentY;

  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {
    graphic = canvas.getGraphicsContext2D();
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setFill(Color.BLACK);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });

    model = new DoodlePrediction();

    // Timelines are created for functionality of timing, or operating within the
    // CanvasController class from other scenes.
    // The cycle count is indefinite as the game can be played as many times as the
    // user wants
    // counterTimeline controls the timer, and detecting if the predicted place of
    // the word is in the top 3
    counterTimeline = new Timeline();
    counterTimeline.setCycleCount(Timeline.INDEFINITE);
    // loadGameTimeline clears the canvas, and display the new word every game
    loadGameTimeline = new Timeline();
    loadGameTimeline.setCycleCount(Timeline.INDEFINITE);
    // downloadImageTimeline downloads the imag.
    downloadImageTimeline = new Timeline();
    downloadImageTimeline.setCycleCount(Timeline.INDEFINITE);

    textToSpeech = new TextToSpeech();

    // Event handler to load the game
    EventHandler<ActionEvent> loadGameEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // The load game event is to clear the canvas, and displays the word
            clear();
            wordLabel.setText(randomWord);
            // this event only occurs once per call of the timeline
            loadGameTimeline.stop();
          }
        };

    // EventHandler for counter to update timer, and check predictions
    EventHandler<ActionEvent> counterEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // The time decreases by 1, going from 60-0
            time++;
            // timerLabel.setText("Time remaining: " + String.valueOf(countdownTime -
            // time));
            // The predictions and probabilites of the machine learning is updated
            try {
              predictions = getPredictions(model.getPredictions(getCurrentSnapshot(), 10));
              probabilities = getProbabilities(model.getPredictions(getCurrentSnapshot(), 10));
            } catch (TranslateException e) {
              e.printStackTrace();
            }
            // The predictions and probabilities of the machine learning is displayed
            predictionsLabel.setText(arrangePredictions(predictions, probabilities));
            // The place of the prediction is updated, and if it is between 1-3 inclusive,
            // the scene is switched to Success
            System.out.println("----------" + Integer.toString(time) + "-----------");
            place = getPredictionPlace(randomWord, predictions);
            System.out.println(confidence);
            if (place > 0) {
              System.out.println(probabilities.get(place + 1));
            }

            // if the word is not in top 10 list, then it informs the user if they are
            // getting closer or further away from top 10
            try {
              if (!getPredictions(model.getPredictions(getCurrentSnapshot(), 10))
                  .contains(randomWord)) {
                if (!getPredictions(model.getPredictions(getCurrentSnapshot(), 30))
                    .contains(randomWord)) {
                  wordRankUpdateLbl.setText("Guess further from top 10");
                } else {
                  wordRankUpdateLbl.setText("Guess closer to top 10");
                }
              } else { // if word is in top 10, it displays it
                wordRankUpdateLbl.setText("Your guess is in top 10");
              }
            } catch (TranslateException e) {
              e.printStackTrace();
            }
          }
        };

    // Eventhandler for downloading the image
    EventHandler<ActionEvent> downloadImageEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            // Image is downloaded
            try {
              saveCurrentSnapshotOnFile();
              System.out.println("file saved!");
            } catch (IOException e) {
              e.printStackTrace();
            }
            // Event only happens once per call
            downloadImageTimeline.stop();
          }
        };
    // counterTimline happens once per second, gameOver once per timer cycle(60s),
    // loadGame and downloadImage happens Immediately
    KeyFrame counterKeyFrame = new KeyFrame(Duration.seconds(1), counterEventHandler);
    KeyFrame loadGameKeyFrame = new KeyFrame(Duration.millis(1), loadGameEventHandler);
    KeyFrame downloadImageKeyFrame = new KeyFrame(Duration.millis(1), downloadImageEventHandler);
    counterTimeline.getKeyFrames().add(counterKeyFrame);
    loadGameTimeline.getKeyFrames().add(loadGameKeyFrame);
    downloadImageTimeline.getKeyFrames().add(downloadImageKeyFrame);
  }

  /**
   * Method thats returns the timelines of canvas so they can be accessed by other classes.
   *
   * @return Arraylist containing the timelines of CanvasController.
   */
  protected static ArrayList<Timeline> getTimeLine() {
    ArrayList<Timeline> timelines = new ArrayList<Timeline>();
    timelines.add(counterTimeline);
    timelines.add(loadGameTimeline);
    timelines.add(downloadImageTimeline);
    return timelines;
  }

  /** Method which stops all running timelines of CanvasController */
  private static void stopTimelines() {
    counterTimeline.stop();
    loadGameTimeline.stop();
    time = 0;
  }

  /**
   * Set the word to be drawn on canvas to the input word
   *
   * @param String to be set ast the input word
   */
  protected static void configureSettings(String wordInput) {
    randomWord = wordInput;
  }

  /**
   * converts the place of the word to a printable string
   *
   * @return printable string version of the place of the word to be drawn
   * @param place of the word to be drawn
   */
  private static String intToPlaceString(int place) {
    if (place == 1) {
      return "First Place";
    } else if (place == 2) {
      return "Second Place";
    } else if (place == 3) {
      return "Third Place";
    } else if (place == 4) {
      return "Fourth Place";
    } else if (place == 5) {
      return "Fifth Place";
    } else if (place == 6) {
      return "Sixth Place";
    } else if (place == 7) {
      return "Seventh Place";
    } else if (place == 8) {
      return "Eigth Place";
    } else if (place == 9) {
      return "Ninth Place";
    } else if (place == 10) {
      return "Tenth Place";
    } else {
      return "Unplaced";
    }
  }

  /**
   * gets the printable string for the place of the word to be written for CanvasController
   *
   * @return printable string of the place
   */
  protected static String getPlace() {
    return intToPlaceString(place);
  }

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException
   * @throws CsvException
   */

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClearPush() {
    clear();
  }

  /**
   * Method is called when "Erase" is pressed. Method changes the brush to "Eraser settings".
   * Remains unchanged if already at eraser settings
   */
  @FXML
  private void onEraserPush() {
    graphic = canvas.getGraphicsContext2D();

    canvas.setOnMouseDragged(
        e -> {
          // The size of the brush is increased significantly
          final double size = 30.0;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // The color of the brush is changed to white to erase
          graphic.setFill(Color.WHITE);
          graphic.fillOval(x, y, size, size);
        });
  }

  /**
   * Method is called when pen is pressed The brush is set to "Pen settings", remains the same if
   * settings are already at "Pen"
   */
  @FXML
  private void onPenPush() {
    graphic = canvas.getGraphicsContext2D();

    graphic.setStroke(Color.BLACK);
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setFill(Color.BLACK);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  protected void clear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /** Method is called when the finish button is pressed */
  @FXML
  private void onFinishPush(ActionEvent event) {
    // if the word to be drawn is within the top 3 predictions, the user wins the
    // game
    if (randomWord.equals(predictions.get(0))
        | randomWord.equals(predictions.get(1))
        | randomWord.equals(predictions.get(2))) {
      // operational timelines stopped
      stopTimelines();
      // scene is changed to success
      Button button = (Button) event.getSource();
      Scene scene = button.getScene();
      scene.setRoot(SceneManager.getRoot(Page.SUCCESS));
    }
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }

  /**
   * Save the current snapshot on a bitmap file.
   *
   * @return The file of the saved image.
   * @throws IOException If the image cannot be saved.
   */
  private File saveCurrentSnapshotOnFile() throws IOException {
    // You can change the location as you see fit.
    final File tmpFolder = new File("tmp");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }

    // We save the image to a file in the tmp folder.
    final File imageToClassify =
        new File(tmpFolder.getName() + "/snapshot" + System.currentTimeMillis() + ".bmp");

    // Save the image to a file.
    ImageIO.write(getCurrentSnapshot(), "bmp", imageToClassify);

    return imageToClassify;
  }

  /**
   * takes the list of predictions, and puts the names of the predictions into an arraylist to be
   * returned
   *
   * @return Arraylist of strings containing the strings of all the predictions.
   * @param Arraylist of Classifications containing the predictions
   */
  private ArrayList<String> getPredictions(final List<Classifications.Classification> predictions) {
    ArrayList<String> predictionStrings = new ArrayList<String>();

    for (final Classifications.Classification classification : predictions) {
      predictionStrings.add(classification.getClassName().replace("_", " "));
    }

    return predictionStrings;
  }

  /**
   * takes the list of predictions, and puts the probabilities of the predictions into an arraylist
   * to be returned
   *
   * @return Arraylist of Doubles containing the probabilities of all the predictions.
   * @param Arraylist of Classifications containing the predictions
   */
  private ArrayList<Double> getProbabilities(
      final List<Classifications.Classification> predictions) {
    ArrayList<Double> predictionStrings = new ArrayList<Double>();

    for (final Classifications.Classification classification : predictions) {
      predictionStrings.add(100 * classification.getProbability());
    }

    return predictionStrings;
  }

  /**
   * Takes predictions names and probabilities, and concatenates them to a presentable string so it
   * can be later printed
   *
   * @return String concatenated predictions and probabilities in a visually appealing form
   * @param Arraylist of Strings containing predictions names
   * @param Arraylist of doubles containing predictions probabilities
   */
  private String arrangePredictions(ArrayList<String> predictions, ArrayList<Double> probabilites) {
    final StringBuilder sb = new StringBuilder();
    sb.append("TOP PREDICTIONS: ").append(System.lineSeparator());

    for (int j = 0; j < predictions.size(); j++) {
      sb.append(j + 1)
          .append(") ")
          .append(predictions.get(j))
          .append(" : ")
          .append(String.format("%.2f%%", probabilites.get(j)))
          .append(System.lineSeparator());
    }

    return sb.toString();
  }

  /**
   * gets the place the machine learning model thinks the word to be drawn is likely
   *
   * @return int 1-10 of the place the machine learning thinks the word to be drawn is. 0 if it isnt
   *     in the top 10
   * @param String word to be drawn
   * @param ArrayList of Strings containing the predictions
   */
  private int getPredictionPlace(String word, ArrayList<String> predictions) {
    for (int i = 0; i < 9; i++) {
      if (word.equals(predictions.get(i))) {
        return i + 1;
      }
    }
    return 0;
  }

  @FXML
  private void onExitPush(ActionEvent event) {
    // timelines stop and it exits back to the extra games tab
    stopTimelines();
    Button button = (Button) event.getSource();
    Scene scene = button.getScene();
    scene.setRoot(SceneManager.getRoot(Page.EXTRA_GAMES));
  }

  // this
  @FXML
  private void onColorPush(ActionEvent event) {
    // grabs the updated colour and changes the brush to that colour
    Color brushColor = colorPicker.getValue();
    graphic.setStroke(brushColor);
  }

  // this allows the user to save their image in any location they desire, can
  // save multiple times too
  @FXML
  private void onDownloadPush(ActionEvent event) {
    try { // creating the file and capturing the current snapshot
      File f1 = saveCurrentSnapshotOnFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // saving the image through file chooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Image");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

    /**
     * @author fabian <https://stackoverflow.com/users/2991525/fabian>
     * @copyright 2017 fabian
     * @license CC BY-SA 3.0
     * @see {@link https://stackoverflow.com/a/42120246/1248177|How to save Image in a user selected
     *     format using FileChooser in javaFx?}
     */
    File f1 = fileChooser.showSaveDialog(null);
    if (f1 != null) {
      String name = f1.getName();
      String extension = name.substring(1 + name.lastIndexOf(".")).toLowerCase();
      try {
        ImageIO.write(getCurrentSnapshot(), extension, f1);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
