package nz.ac.auckland.se206;

import java.util.HashMap;

import javafx.scene.Parent;

// SceneManager for managing all the scenes
public class SceneManager {

	// All scenes saved are as a enum
	public enum Page {
		DIFFICULTY_PAGE, GAME_OVER, MAIN_MENU, READY, CANVAS, SUCCESS, PROFILE_PAGE, PROFILE_CREATION_PAGE, STATISTICS,
		EXTRA_GAMES, ZEN_MODE, READY_ZEN, DIFFICULTY_ZEN, DEFINITION, DISPLAY_BADGES, SET_DIFFICULTY_PAGE
	}

	// each enum has a corresponding page, which can be added or gotten
	private static HashMap<Page, Parent> sceneMap = new HashMap<Page, Parent>();

	/**
	 * adds a parent page to the SceneManager
	 *
	 * @param enum   associated with the page
	 * @param parent page to be saved
	 */
	public static void addPage(Page page, Parent pageRoot) {
		sceneMap.put(page, pageRoot);
	}

	/**
	 * gets the root/parent page of an enum
	 *
	 * @param enum of the page you want to retreive
	 * @return Parent page of the enum
	 */
	public static Parent getRoot(Page page) {
		return sceneMap.get(page);
	}
}