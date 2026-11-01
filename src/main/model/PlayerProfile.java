package main.model;

public class PlayerProfile{
        
    private String PlayerName;
    private int correctHits;
    private int wrongHits;
    private int wins;
    private int losses;
    private double hitRatio;

    public PlayerProfile(String PlayerName, int correctHits, int wrongHits){
        this.PlayerName = PlayerName;
        this.correctHits = correctHits;
        this.wrongHits = wrongHits;
        this.wins = 0;
        this.losses = 0;
        this.HitRatio();
    }

    public PlayerProfile(String PlayerProfile){
        this(PlayerProfile, 0, 0);
    }

    public String getPlayerName(){
        return PlayerName;
    }

    public int getCorrectHits(){
        return correctHits;
    }

    public int getWrongHits(){
        return wrongHits;
    }

    public double getHitRatio(){
        return hitRatio;
    }

    public int[] getRecord(){
        return new int[]{wins, losses};
    }

    public void updateHits(int correctHits, int wrongHits){
        this.correctHits += correctHits;
        this.wrongHits += wrongHits;
    }

    public void updateRecord(int[] record){
        if(record[0] >= 0 && record[1] >= 0){
            wins = record[0];
            losses = record[1];
        }
    }

    public void HitRatio(){
        if(correctHits + wrongHits != 0){
            hitRatio = (double)correctHits / (correctHits + wrongHits);
            hitRatio *= 100;
        }else{
            hitRatio = 0;
        }
    }

}
