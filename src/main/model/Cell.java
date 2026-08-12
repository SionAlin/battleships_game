package main.model;

public class Cell{
    private int cell; //(0)water or (1)ship part 
    private int EnemyHit; //(0)not hit or (1)hit
    private int PlayerHit; //(0)not hit or (1)hit

    public Cell(int cell){
        this.cell = cell;
        this.EnemyHit = 0;
        this.PlayerHit = 0;
    }
    
    public int getCell(){
        return cell;
    }

    public int getEnemyHit(){
        return EnemyHit;
    }

    public int getPlayerHit(){
        return PlayerHit;
    }

    public void setCell(int cell){
        if(cell == 0 || cell == 1)
            this.cell = cell;
    }

    public void setEnemyHit(int EnemyHit){
        if(EnemyHit == 0 || EnemyHit == 1)
            this.EnemyHit = EnemyHit;
    }

    public void setPlayerHit(int PlayerHit){
        if(PlayerHit == 0 || PlayerHit == 1)
            this.PlayerHit = PlayerHit;
    }
        
    public void GetDamage(){
        EnemyHit = 1;
    }

    public void DoDamage(){
        PlayerHit = 1;
    }
    
}
