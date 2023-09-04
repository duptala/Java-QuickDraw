package nz.ac.auckland.se206;

import java.util.ArrayList;

public abstract class Badge {
  // Each badge has a badge type denoting what the achievement condition, name and
  // description is.
  public interface BadgeType {}
  ;

  // Has a difficulty; the game needs to be played on a specific difficulty to be
  // earned
  public enum BadgeDifficulty {
    AMATEUR,
    VETERAN,
    MASTER,
    GRANDMASTER,
    UNIVERSAL;
  }

  // Has a rank denoting its prestige
  public enum BadgeRank {
    ONYX("Onyx"),
    PLATINUM("Platinum"),
    GOLD("Gold"),
    SILVER("Silver"),
    BRONZE("Bronze"),
    IRON("Iron");

    private String rankName;

    private BadgeRank(String rankName) {
      this.rankName = rankName;
    }

    public String getRankName() {
      return rankName;
    }

    /**
     * THe function is to set the badge in to different rank based on the number
     *
     * @param number the character that used to point different rank
     * @return the different type of the badge
     */
    protected static BadgeRank getRank(int number) {
      switch (number) {
        case 0:
          return IRON;
        case 1:
          return BRONZE;
        case 2:
          return SILVER;
        case 3:
          return GOLD;
        case 4:
          return PLATINUM;
        case 5:
          return ONYX;
        default:
          return IRON;
      }
    }
  }

  protected BadgeType badgeType;
  protected String badgeDescription;
  protected String badgeName;
  protected BadgeRank badgeRank;
  protected BadgeDifficulty badgeDifficulty;

  public abstract boolean isBadgeEarned(int timeLeft);

  public Badge(
      BadgeType badgeType, String badgeName, String badgeDescription, BadgeRank badgeRank) {
    this.badgeType = badgeType;
    this.badgeName = badgeName;
    this.badgeDescription = badgeDescription;
    this.badgeRank = badgeRank;
  }

  public String getDescription() {
    return badgeDescription;
  }

  public String getName() {
    return badgeName;
  }

  public BadgeType getType() {
    return badgeType;
  }

  public BadgeRank getRank() {
    return badgeRank;
  }

  public BadgeDifficulty getBadgeDifficulty() {
    return badgeDifficulty;
  }

  /**
   * Gets all badges. Used to initialize badges of new profiles
   *
   * @return an arraylist that have all the badges
   */
  public static ArrayList<Badge> getAllBadges() {
    ArrayList<Badge> allBadges = new ArrayList<Badge>();
    // Gets all the badges from both category of badges; universal and difficulty
    allBadges.addAll(UniversalBadge.getAllBadges());
    allBadges.addAll(DifficultyBadge.getAllBadges());
    return allBadges;
  }

  /**
   * Gets a badge from its type and difficulty. Used for retrieving instances of badges from saved
   * information
   *
   * @param badgeType
   * @param badgeDifficulty
   * @return
   */
  public static Badge getBadge(BadgeType badgeType, BadgeDifficulty badgeDifficulty) {
    // The category of the badge is determined, and that class' method to get badge
    // is used to get
    // the badge.
    if (badgeDifficulty == BadgeDifficulty.UNIVERSAL) {
      return UniversalBadge.getUniversalBadge(badgeType);
    } else {
      return DifficultyBadge.getDifficultyBadge(badgeType, badgeDifficulty);
    }
  }
}