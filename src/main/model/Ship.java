package main.model;

public class Ship{
    
    private int length;
    private boolean sunk;
    private int damageTook;

    public Ship(int length){
        this.length = length;
        this.sunk = false;
        this.damageTook = 0;
    }
    
    public int getLength(){
        return length;
    }

    public boolean getSunk(){
        return sunk;
    }

    public int getDamageTook(){
        return damageTook;
    }

    public void setSunk(boolean sunk){
        this.sunk = sunk;
    }

    public void DamageShip(){
        if(damageTook < length)
            damageTook++;
        
        if(damageTook >= length)
            sunk = true;
    }
}
