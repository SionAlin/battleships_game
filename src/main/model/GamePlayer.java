package main.model;

public class GamePlayer{
    
    private String PlayerName;
    public Board PlayerBoard;
    private int CorrectHits;
    private int WrongHits;
    private double HitRatio;

    public GamePlayer(String PlayerName){
        
        this.PlayerName = PlayerName;
        this.PlayerBoard = new Board();
        this.CorrectHits = 0;
        this.WrongHits = 0;
        this.HitRatio = 0.0;
    }

    public String getPlayerName(){
        return PlayerName;
    }

    public int getCorrectHits(){
        return CorrectHits;
    }

    public int getWrongHits(){
        return WrongHits;
    }

    public double getHitRatio(){
        return HitRatio;
    }
    
    public void HitRatio(){
        try{
            HitRatio = CorrectHits / (CorrectHits + WrongHits);
            HitRatio *= 100;
        }catch(ArithmeticException e){
            e.printStackTrace();
        }
    }

    public void CorrectHit(){
        CorrectHit += 1;
    }

    public void WrongHit(){
        WrongHits += 1;
    }
}
