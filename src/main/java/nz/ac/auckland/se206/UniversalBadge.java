package nz.ac.auckland.se206;

import java.util.ArrayList;
import nz.ac.auckland.se206.controllers.DifficultyPageController;
import nz.ac.auckland.se206.words.CategorySelect.Difficulty;

public class UniversalBadge extends Badge {
  // Universal Badges are badges which can be earned no matter the difficulty
  private UniversalBadge(
      BadgeType badgeType, String badgeName, String badgeDescription, BadgeRank rankOfBadge) {
    super(badgeType, badgeName, badgeDescription, rankOfBadge);
    this.badgeDifficulty = BadgeDifficulty.UNIVERSAL;
  }

  public enum UniversalBadgeType implements BadgeType {
    UNDEFEATED,
    SEE_THE_IMPOSSIBLE,
    DO_THE_IMPOSSIBLE,
    CLOSE_ONE,
    HELLO_WORLD,
    EXPERIENCED,
    WINSTREAK_1,
    WINSTREAK_2,
    WINSTREAK_3,
  }

  /**
   * Gets all badges of the universal category. Used in in Badges() to get all badges for new
   * profiles
   *
   * @return ArrayList<Badge> All badges of the universal category
   */
  public static ArrayList<Badge> getAllBadges() {
    ArrayList<Badge> badges = new ArrayList<Badge>();
    // A Badge of each type, along with their corresponding name, rank, and description is added.
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.UNDEFEATED,
            "Undefeated",
            "Win every game for 10 games",
            BadgeRank.GOLD));
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.HELLO_WORLD, "Hello World!", "Play a game", BadgeRank.BRONZE));
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.SEE_THE_IMPOSSIBLE,
            "I've seen it...",
            "Play a game on the hardest difficulty",
            BadgeRank.SILVER));
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.DO_THE_IMPOSSIBLE,
            "I've done it!",
            "Win a game on the hardest difficulty",
            BadgeRank.ONYX));
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.CLOSE_ONE,
            "Not Even Close!",
            "Win a game on the last possible second",
            BadgeRank.PLATINUM));
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.WINSTREAK_1,
            "Winstreak I",
            "Win two games in a row",
            BadgeRank.IRON));
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.WINSTREAK_2,
            "WinStreak II",
            "Win five games in a row",
            BadgeRank.SILVER));
    badges.add(
        new UniversalBadge(
            UniversalBadgeType.WINSTREAK_3,
            "Winstreak III",
            "Win 10 games in a row",
            BadgeRank.GOLD));
    return badges;
  }
  /**
   * Gets a universal badge given its type. Used in getBadge() in Badge to create an instance of a
   * Badge from data saved in a file.
   *
   * @param badgeType
   * @return Badge corresponding to the saved badge type.
   */
  public static Badge getUniversalBadge(BadgeType badgeType) {
    // For each badge type, the badge is created and returned
    switch ((UniversalBadgeType) badgeType) {
      case UNDEFEATED:
        return new UniversalBadge(
            UniversalBadgeType.UNDEFEATED,
            "Undefeated",
            "Win every game for 10 games",
            BadgeRank.GOLD);
      case HELLO_WORLD:
        return new UniversalBadge(
            UniversalBadgeType.HELLO_WORLD, "Hello World!", "Play a game", BadgeRank.BRONZE);
      case SEE_THE_IMPOSSIBLE:
        return new UniversalBadge(
            UniversalBadgeType.SEE_THE_IMPOSSIBLE,
            "I've seen it...",
            "Play a game on the hardest difficulty",
            BadgeRank.SILVER);
      case DO_THE_IMPOSSIBLE:
        return new UniversalBadge(
            UniversalBadgeType.DO_THE_IMPOSSIBLE,
            "I've done it!",
            "Win a game on the hardest difficulty",
            BadgeRank.ONYX);
      case CLOSE_ONE:
        return new UniversalBadge(
            UniversalBadgeType.CLOSE_ONE,
            "Not Even Close!",
            "Win a game on the last possible second",
            BadgeRank.PLATINUM);
      case WINSTREAK_1:
        return new UniversalBadge(
            UniversalBadgeType.WINSTREAK_1,
            "Winstreak I",
            "Win two games in a row",
            BadgeRank.IRON);
      case WINSTREAK_2:
        return new UniversalBadge(
            UniversalBadgeType.WINSTREAK_2,
            "WinStreak II",
            "Win five games in a row",
            BadgeRank.SILVER);
      case WINSTREAK_3:
        return new UniversalBadge(
            UniversalBadgeType.WINSTREAK_3,
            "Winstreak III",
            "Win 10 games in a row",
            BadgeRank.GOLD);
      default:
        return new UniversalBadge(
            UniversalBadgeType.HELLO_WORLD, "Hello World!", "Play a game", BadgeRank.BRONZE);
    }
  }

  @Override
  /**
   * Checks whether a badge has been earned in the last Round
   *
   * @param timeTaken time taken in the last round
   * @return boolean denoting whether the badge has been earned.
   */
  public boolean isBadgeEarned(int timeTaken) {
    // Information about the difficulty of the last round is obtained from DifficultyPageController
    int time = DifficultyPageController.getTime();
    int accuracy = DifficultyPageController.getAccuracy();
    double confidence = DifficultyPageController.getConfidence();
    Difficulty difficulty = DifficultyPageController.getWordDifficulty();
    // Information about the profile, such as win streak is obtained from Profile.
    Profile profile = Profile.getProfile();
    boolean isStreakWin = profile.getIsStreakWin();
    // For each Badge type, the unique achievement condition is checked. If the condition is
    // fulfilled, the badge is earned and true is returned
    switch ((UniversalBadgeType) badgeType) {
      case UNDEFEATED:
        if ((profile.getLosses() == 0) & (profile.getWins() > 9)) {
          return true;
        }
        break;
      case SEE_THE_IMPOSSIBLE:
        if (accuracy == 1
            & (50.00 - confidence) < 0.01
            & time == 15
            & difficulty == Difficulty.Ma) {
          return true;
        }
        break;
      case DO_THE_IMPOSSIBLE:
        if (accuracy == 1
            & (50.00 - confidence) < 0.01
            & time == 15
            & difficulty == Difficulty.Ma
            & isStreakWin) {
          return true;
        }
        break;
      case CLOSE_ONE:
        if ((timeTaken <= 1) & isStreakWin) {
          return true;
        }
        break;
      case HELLO_WORLD:
        return true;
      case EXPERIENCED:
        if (profile.getLosses() + profile.getWins() > 99) {
          return true;
        }
        break;
      case WINSTREAK_1:
        return checkWinStreak(profile, 1);
      case WINSTREAK_2:
        return checkWinStreak(profile, 4);
      case WINSTREAK_3:
        return checkWinStreak(profile, 9);
    }
    return false;
  }

  private boolean checkWinStreak(Profile profile, int streakLength) {
    if (profile.getIsStreakWin() == true & profile.getStreakLength() > streakLength) {
      return true;
    }
    return false;
  }
}