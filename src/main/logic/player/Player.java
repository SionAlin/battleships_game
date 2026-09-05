package logic.player;

import model.*;
import java.awt.Point;

public abstract class Player{
    
    protected String PlayerName;
    protected Board PlayerBoard;
    protected int CorrectHits;
    protected int WrongHits;

    public Player(String PlayerName){
        this.PlayerName = PlayerName;
        this.PlayerBoard = new Board();
        this.CorrectHits = 0;
        this.WrongHits = 0;
    }

    public abstract void makeMove(Board opponentBoard);

    public void RecordHit(boolean isHit){
        if(isHit == true)
            CorrectHits++;
        else
            WrongHits++;
    }

    public String getPlayerName(){ return PlayerName; }
    public Board getPlayerBoard(){ return PlayerBoard; }
    public int getCorrectHits(){ return CorrectHits; }
    public int getWrongHits(){ return WrongHits; }

    public double HitRatio(){
        int total = CorrectHits + WrongHits;

        if(total == 0) return 0.0;
        else return (double) CorrectHits / total;
    }

}
