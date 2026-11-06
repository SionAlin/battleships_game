package main.logic.player;

import main.model.*;
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

    public void recordHit(boolean isHit){
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

    public void applyShotResult(int row, int col, ShotResult r){
        if(r == ShotResult.ALREADY_SHOT) return;
        Cell[][] board = PlayerBoard.getBoard();
        board[row][col].setPlayerHit(r == ShotResult.HIT ? 2 : 1);
        recordHit(r == ShotResult.HIT);
    }

    public ShotResult fire(Board opponentBoard, int row, int col){
        Cell[][] board = PlayerBoard.getBoard();
        if(board[row][col].getPlayerHit() != 0) return ShotResult.ALREADY_SHOT;
        ShotResult r = opponentBoard.Shoot(row, col);
        applyShotResult(row, col, r);
        return r;
    }
}
