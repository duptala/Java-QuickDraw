package nz.ac.auckland.se206;

import java.util.ArrayList;
import nz.ac.auckland.se206.controllers.DifficultyPageController;
import nz.ac.auckland.se206.words.CategorySelect.Difficulty;

public class DifficultyBadge extends Badge {
  // Difficulty Badges are badges which have different versions for each
  // difficulty, and can only be
  // earned at the corresponding difficulty
  public enum DifficultyBadgeType implements BadgeType {
    FAST,
    LIGHTNING,
  }

  /**
   * gets all the badges of of Difficulty badge category. Used in conjunction with the getBadge() of
   * Badges to initialize badges of new profiles
   *
   * @return Arraylist<Badge> containing all badges of the category Difficulty Badge;
   */
  public static ArrayList<Badge> getAllBadges() {
    ArrayList<Badge> badges = new ArrayList<Badge>();
    // For all difficulty levels, a badge of each type is added
    addAllDifficulty(
        badges, DifficultyBadgeType.FAST, "Fast!", "Finish a game within 30 seconds", 0);
    addAllDifficulty(
        badges,
        DifficultyBadgeType.LIGHTNING,
        "Lightning Fast!",
        "Finish a game within 10 seconds",
        2);
    return badges;
  }

  /**
   * Adds all difficulties of a badge to an array
   *
   * @param badges Array to add badges to
   * @param badgeType type of added badge
   * @param badgeName name of the added badge
   * @param badgeDescription description of the added badge
   * @param badgeRank number corresponding to the rank of the lowest difficulty rank
   */
  private static void addAllDifficulty(
      ArrayList<Badge> badges,
      BadgeType badgeType,
      String badgeName,
      String badgeDescription,
      int badgeRank) {
    // For all badges of difficulty that are not universal, badges are created.
    for (BadgeDifficulty badgeDifficulty : BadgeDifficulty.values()) {
      if (badgeDifficulty != BadgeDifficulty.UNIVERSAL) {
        badges.add(
            new DifficultyBadge(
                badgeType,
                badgeName,
                badgeDescription,
                BadgeRank.getRank(badgeRank),
                badgeDifficulty));
        badgeRank++;
      }
    }
  }

  /**
   * Get the Difficulty Badge given its difficulty and type. Used in getBadges() in Badges to
   * retrieve an instance of badge from saved data
   *
   * @param badgeType
   * @param badgeDifficulty
   * @return
   */
  public static Badge getDifficultyBadge(BadgeType badgeType, BadgeDifficulty badgeDifficulty) {
    switch ((DifficultyBadgeType) badgeType) {
        // Depending on the Type, its name, description and rank are created
      case FAST:
        return new DifficultyBadge(
            DifficultyBadgeType.FAST,
            "Fast!",
            "Finish a game within 30 seconds",
            // Its rank is calculated from a combination of its type and its difficulty
            convertBadgeRank(badgeType, badgeDifficulty),
            badgeDifficulty);
      case LIGHTNING:
        return new DifficultyBadge(
            DifficultyBadgeType.LIGHTNING,
            "Lightning Fast!",
            "Finish a game within 10 seconds",
            convertBadgeRank(badgeType, badgeDifficulty),
            badgeDifficulty);
      default:
        return new DifficultyBadge(
            DifficultyBadgeType.FAST,
            "Fast!",
            "Finish a game within 30 seconds",
            convertBadgeRank(badgeType, badgeDifficulty),
            badgeDifficulty);
    }
  }

  /**
   * Calculates The badge rank of the badge from the badge type and difficulty. Used to work out the
   * rank of the badge when retrieving badges frm saved information
   *
   * @param badgeType
   * @param badgeDifficulty
   * @return
   */
  private static BadgeRank convertBadgeRank(BadgeType badgeType, BadgeDifficulty badgeDifficulty) {
    int baseDifficulty;
    switch ((DifficultyBadgeType) badgeType) {
        // FAST has a difficulty of 0, meaning at the lowest difficulty, its badge rank
        // is iron
      case FAST:
        baseDifficulty = 0;
        break;
      case LIGHTNING:
        // LIGHTNING has a badge rank of silver at the lowest difficulty
        baseDifficulty = 2;
        break;
      default:
        baseDifficulty = 0;
        break;
    }
    switch (badgeDifficulty) {
        // For each increase in difficulty level, the rank of the badges increase by one
        // from the
        // base
      case VETERAN:
        baseDifficulty++;
        break;
      case MASTER:
        baseDifficulty += 2;
        break;
      case GRANDMASTER:
        baseDifficulty += 3;
        break;
      default:
        break;
    }
    switch (baseDifficulty) {
        // The badge rank is returned.
      case 0:
        return BadgeRank.IRON;
      case 1:
        return BadgeRank.BRONZE;
      case 2:
        return BadgeRank.SILVER;
      case 3:
        return BadgeRank.GOLD;
      case 4:
        return BadgeRank.PLATINUM;
      case 5:
        return BadgeRank.ONYX;
      default:
        return BadgeRank.BRONZE;
    }
  }

  private DifficultyBadge(
      BadgeType badgeType,
      String badgeName,
      String badgeDescription,
      BadgeRank rankOfBadge,
      BadgeDifficulty badgeDifficulty) {
    super(badgeType, badgeName, badgeDescription, rankOfBadge);
    this.badgeDifficulty = badgeDifficulty;
  }

  public BadgeDifficulty getDifficulty() {
    return badgeDifficulty;
  }

  @Override
  public String getName() {
    // The name of Difficulty Badges have their ranks appended to the end.
    final StringBuilder sb = new StringBuilder();
    sb.append(badgeName);
    switch (badgeDifficulty) {
      case AMATEUR:
        sb.append(" (Amateur)");
        break;
      case VETERAN:
        sb.append(" (Veteran)");
        break;
      case MASTER:
        sb.append(" (Master)");
        break;
      case GRANDMASTER:
        sb.append(" (Grandmaster)");
        break;
    }
    return sb.toString();
  }

  @Override
  /**
   * Checks if the Badge has been earned depending on the time taken and the difficulty of the last
   * game played
   *
   * @param timeTaken timeTaken for the user to play the last game
   * @return boolean denoting if the badge has been earned
   */
  public boolean isBadgeEarned(int timeTaken) {
    switch ((DifficultyBadgeType) badgeType) {
        // There are unique achievement conditions depending on the type of the badge
      case FAST:
        // The last game played needs to be of the same difficulty as the badge's
        // difficulty for the
        // badge to be earned
        if (timeTaken < 30
            & convertToDifficulty() == DifficultyPageController.getOverallDifficulty()) {
          return true;
        }
        break;
      case LIGHTNING:
        if (timeTaken < 10
            & convertToDifficulty() == DifficultyPageController.getOverallDifficulty()) {
          return true;
        }
        break;
    }
    return false;
  }

  /**
   * Calculates game difficulty according to the badge difficulty, used to check whether the badge
   * difficulty is the same as the game difficulty
   *
   * @return Difficulty game difficulty corresponding to the BadgeDifficulty
   */
  private Difficulty convertToDifficulty() {
    // Badge difficulty is converted to correspondinggame difficulty
    if (badgeDifficulty == BadgeDifficulty.AMATEUR) {
      return Difficulty.E;
    } else if (badgeDifficulty == BadgeDifficulty.VETERAN) {
      return Difficulty.M;
    } else if (badgeDifficulty == BadgeDifficulty.MASTER) {
      return Difficulty.H;
    } else {
      return Difficulty.Ma;
    }
  }
}