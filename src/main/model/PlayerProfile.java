package main.model;

public class PlayerProfile{
        
    private String PlayerName;
    private int Wins;
    private int Losses;
    private double WinRatio;

    public PlayerProfile(String PlayerName, int Wins, int Losses, double WinRatio){
        this.PlayerName = PlayerName;
        this.Wins = Wins;
        this.Losses = Losses;
        this.WinRatio = WinRatio;
    }

    public PlayerProfile(String PlayerProfile){
        this.PlayerProfile(PlayerProfile, 0, 0, 0.0);
    }
    
    public String getPlayerName(){
        return PlayerName;
    }

    public int getWins(){
        return Wins;
    }

    public int getLosses(){
        return Losses;
    }

    public double getWinRatio(){
        return WinRatio;
    }

    public void MatchWon(){
        Wins += 1;
    }

    public void MatchLost(){
        Losses += 1;
    }

    public void WinRatio(){
        try{
            WinRatio = Wins / (Wins + Losses);
            WinRatio *= 100;
        }catch(ArithmeticException e){
            e.printStackTrace();
        }
    }

}
