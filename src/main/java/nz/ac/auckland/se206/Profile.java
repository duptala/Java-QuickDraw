package nz.ac.auckland.se206;

import java.util.ArrayList;

public class Profile {
  // the Class has a static variable which is used to store the current profile.
  private static Profile currentProfile;

  public static void setProfile(Profile profile) {
    currentProfile = profile;
  }

  public static Profile getProfile() {
    return currentProfile;
  }

  // Profile class stores data regarding each profile
  private String profileName;
  private int wins;
  private int losses;
  private ArrayList<String> wordsEncountered = new ArrayList<String>();
  private ArrayList<String> previousWordsEncountered = new ArrayList<String>();
  private int fastestTime;
  private int streakLength;
  private boolean isStreakWin;
  private ArrayList<Badge> badgesEarned;
  private ArrayList<Badge> badgesNotEarned;

  /*
   * On initialisation of the app, the profiles are read from files and passed
   * into the constructor to so they can be accessed at runtime
   *
   */
  public Profile(String ProfileName, int wins, int losses, int fastestTime, int streakLength, boolean isStreakWin, ArrayList<Badge> badgesEarned, ArrayList<Badge> badgesNotEarned){
    this.profileName = ProfileName;
    this.wins = wins;
    this.losses = losses;
    this.fastestTime = fastestTime;
    this.streakLength = streakLength;
    this.isStreakWin = isStreakWin;
    this.badgesEarned = badgesEarned;
    this.badgesNotEarned = badgesNotEarned;
  }

  // Getter methods, and methods which add data exist for each statistic.
  public String getName() {
    return profileName;
  }

  public int getWins() {
    return wins;
  }

  public int getLosses() {
    return losses;
  }

  public void addWin() {
    wins++;
    calculateStreak(true);
  }

  public void addLosses() {
    losses++;
    calculateStreak(false);
  }

  public ArrayList<String> getWordsEncountered() {
    return wordsEncountered;
  }

  public void addWordEncountered(String string) {
    wordsEncountered.add(string);
  }

  public ArrayList<String> getPreviousWordsEncountered() {
    return previousWordsEncountered;
  }

  public void addPreviousWordEncountered(String string) {
    previousWordsEncountered.add(string);
  }

  public int getFastestTime() {
    return fastestTime;
  }
  
  public boolean getIsStreakWin() {
	  return isStreakWin;
  }
  public int getStreakLength() {
	  return streakLength;
  }
  public ArrayList<Badge> getBadgesEarned(){
	  return badgesEarned;
  }
  public ArrayList<Badge> getBadgesNotEarned(){
	  return badgesNotEarned;
  }
  public void addBadgeEarned(Badge badge){
	  badgesEarned.add(badge);
  }
  public void removeBadgeNotEarned(Badge badge) {
	  badgesNotEarned.remove(badge);
  }

  public void compareSetFastestTime(int time) {
    /*
     * The time taken to draw for that particular game is passed into this method.
     * If the time is less than the fastest time, or if there is no previous fastest
     * time, this time is set to be the new fastest time.
     */
    if ((time < fastestTime) | (fastestTime == -1)) {
      this.fastestTime = time;
    }
  }
  private void calculateStreak(boolean isGameWon) {
	  if(isStreakWin == isGameWon) {
	    	streakLength++;
	    }else {
	    	isStreakWin = isGameWon;
	    	streakLength = 1;
	    }
  }
}