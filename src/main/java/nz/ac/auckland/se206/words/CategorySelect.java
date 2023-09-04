package nz.ac.auckland.se206.words;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import nz.ac.auckland.se206.Profile;

public class CategorySelect {
  public enum Difficulty {
    E,
    M,
    H,
    Ma
  }

  private Map<Difficulty, List<String>> difficultyMap;

  /**
   * Constructor creates a hashmap of difficulty to arrayLists
   *
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  public CategorySelect() throws IOException, CsvException, URISyntaxException {
    difficultyMap = new HashMap<>();
    for (Difficulty difficulty : Difficulty.values()) {
      difficultyMap.put(difficulty, new ArrayList<>());
    }

    // In every line, the word is stored in its difficulty's arraylist
    for (String[] line : getLines()) {
      difficultyMap.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  /**
   * Returns a random word which is of the difficulty input @Return random word of the difficulty
   *
   * @param difficulty that you want the random word to be
   */
  public String getRandomCategory(Difficulty difficulty) {
    for (int i = 0; i < difficultyMap.get(difficulty).size(); i += 1) {
      int randomNumber = new Random().nextInt(difficultyMap.get(difficulty).size());
      if (Profile.getProfile()
                  .getWordsEncountered()
                  .contains(difficultyMap.get(difficulty).get(randomNumber))
              == false
          && Profile.getProfile()
                  .getPreviousWordsEncountered()
                  .contains(difficultyMap.get(difficulty).get(randomNumber))
              == false) {
        return difficultyMap.get(difficulty).get(randomNumber);
      }
    }
    return difficultyMap
        .get(difficulty)
        .get(new Random().nextInt(difficultyMap.get(difficulty).size()));
  }

  /** get the lines from the file, and puts them into a list of Strings. */
  protected List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File fileName = new File(CategorySelect.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fr = new FileReader(fileName, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }
}
