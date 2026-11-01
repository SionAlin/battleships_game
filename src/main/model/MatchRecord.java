package main.model;

import java.time.*;

public class MatchRecord{

    private PlayerProfile PlayerOne;
    private PlayerProfile PlayerTwo;
    private PlayerProfile Winner;
    private LocalDateTime StartTime, EndTime;

    public MatchRecord(PlayerProfile PlayerOne, PlayerProfile PlayerTwo){
        this.PlayerOne = PlayerOne;
        this.PlayerTwo = PlayerTwo;
        this.Winner = null;
        this.StartTime = LocalDateTime.now();
        this.EndTime = null;
    }

    public MatchRecord(PlayerProfile PlayerOne, PlayerProfile PlayerTwo, PlayerProfile Winner, LocalDateTime StartTime, LocalDateTime EndTime){
        this.PlayerOne = PlayerOne;
        this.PlayerTwo = PlayerTwo;
        this.Winner = Winner;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
    }

    public void setWinner(String WinnerName){
        if(PlayerOne.getPlayerName().equals(WinnerName)){
            Winner = PlayerOne;
        }else{
            Winner = PlayerTwo;
        }
        EndTime = LocalDateTime.now();
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
    
    public LocalDateTime getStartTime(){
        return StartTime;
    }

    public LocalDateTime getEndTime(){
        return EndTime;
    }
}
