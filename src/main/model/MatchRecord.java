package main.model;

import java.time.*;

public class MatchRecord{

    private String GameMode;
    private PlayerProfile PlayerOne;
    private PlayerProfile PlayerTwo;
    private PlayerProfile Winner;
    private LocalDateTime MatchDate;
    private LocalTime StartTime, EndTime;

    public MatchRecord(PlayerProfile PlayerOne, PlayerProfile PlayerTwo){
        if(PlayerTwo.getPlayerName().equals("Com")){
            this.GameMode = "Player vs. Computer";
        }else{
            this.GameMode = "Player vs. Player";
        }
        this.PlayerOne = PlayerOne;
        this.PlayerTwo = PlayerTwo;
        this.Winner = null;
        this.MatchDate = LocalDateTime.now();
        this.StartTime = LocalTime.now();
        this.EndTime = null;
    }

    public void SetWinner(String WinnerName){
        if(PlayerOne.getPlayerName().equals(WinnerName)){
            Winner = PlayerOne;
            PlayerOne.MatchWon();
            PlayerTwo.MatchLost();
        }else{
            Winner = PlayerTwo;
            PlayerOne.MatchLost();
            PlayerTwo.MatchWon();
        }
        EndTime = LocalTime.now();
    }
    
    public String getGameMode(){
        return GameMode;
    }

    public PlayerProfile getPlayerOne(){
        return PlayerOne;
    }

    public PlayerProfile getPlayerTwo(){
        return PlayerTwo;
    }

    public PlayerProfile getWinner(){
        return Winner;
    }
    
    public LocalDateTime getMatchDate(){
        return MatchDate;
    }

    public LocalTime getStartTime(){
        return StartTime;
    }

    public LocalTime getEndTime(){
        return EndTime;
    }
}
