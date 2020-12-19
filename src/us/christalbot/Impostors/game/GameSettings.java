package us.christalbot.Impostors.game;

public class GameSettings {
    public boolean confirmEjects;
    public int numberEmergencyMeetings;
    public int cooldownEmergencyMeetings;
    public int discussionTime;
    public int votingTime;
    public int playerSpeed;
    public int cooldownKill;
    public boolean visualTasks;

    public GameSettings() {
        this.confirmEjects = false;
        this.numberEmergencyMeetings = 1;
        this.cooldownEmergencyMeetings = 20;
        this.discussionTime = 15;
        this.votingTime = 90;
        this.playerSpeed = 1;
        this.cooldownKill = 25;
        this.visualTasks = true;
    }
}
