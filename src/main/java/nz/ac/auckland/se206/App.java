package nz.ac.auckland.se206;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.Page;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * This is the entry point of the JavaFX application, while you can change this
 * class, it should remain as the class that runs the JavaFX application.
 */
public class App extends Application {
	private Scene scene;
	TextToSpeech speak = new TextToSpeech();

	public static void main(final String[] args) {
		launch();
	}

	/**
	 * Returns the node associated to the input file. The method expects that the
	 * file is located in "src/main/resources/fxml".
	 *
	 * @param fxml The name of the FXML file (without extension).
	 * @return The node of the input file.wordLabelwordLabelwordLabelwordLabel
	 * @throws IOException If the file is not found.
	 */
	private static Parent loadFxml(final String fxml) throws IOException {
		return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
	}

	/**
	 * This method is invoked when the application starts. It loads and shows the
	 * "Canvas" scene.
	 *
	 * @param stage The primary stage of the application.
	 * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
	 */
	@Override
	public void start(final Stage stage) throws IOException {
		// All the scenes within the app are setup for scenemanager
		SceneManager.addPage(Page.CANVAS, loadFxml("canvas"));
		SceneManager.addPage(Page.READY, loadFxml("ready"));
		SceneManager.addPage(Page.MAIN_MENU, loadFxml("mainmenu"));
		SceneManager.addPage(Page.GAME_OVER, loadFxml("gameover"));
		SceneManager.addPage(Page.DIFFICULTY_PAGE, loadFxml("difficultypage"));
		SceneManager.addPage(Page.SUCCESS, loadFxml("success"));
		SceneManager.addPage(Page.PROFILE_PAGE, loadFxml("profilepage"));
		SceneManager.addPage(Page.PROFILE_CREATION_PAGE, loadFxml("profileCreationPage"));
		SceneManager.addPage(Page.STATISTICS, loadFxml("statistics"));
		SceneManager.addPage(Page.EXTRA_GAMES, loadFxml("extragamemodes"));
		SceneManager.addPage(Page.ZEN_MODE, loadFxml("zenMode"));
		SceneManager.addPage(Page.READY_ZEN, loadFxml("zenReady"));
		SceneManager.addPage(Page.DIFFICULTY_ZEN, loadFxml("zenDifficulty"));
    SceneManager.addPage(Page.PROFILE_CREATION_PAGE.DEFINITION, loadFxml("definition"));
    SceneManager.addPage(Page.DISPLAY_BADGES, loadFxml("displaybadges"));
    SceneManager.addPage(Page.SET_DIFFICULTY_PAGE, loadFxml("setdifficultypage"));

		scene = new Scene(SceneManager.getRoot(Page.MAIN_MENU), 640, 480);
		// when closing the app
		stage.setOnCloseRequest(event -> {
			event.consume(); // if click cancel the app would continue.
			logOut(stage);
		});
    
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * The function is used when click the close the window. An alert would come up
	 * to mention.
	 */
	public void logOut(Stage stage) {
		// Popup confirming if the user wants to exit occurs upon user trying to exit
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("LogOut Window");
		alert.setHeaderText("Do you want to finish the game?");
		// if user click which means the user wants to ternimate the app
		if (alert.showAndWait().get() == ButtonType.OK) {
			stage.close();
			Platform.exit();
			speak.terminate();
		}
	}
}