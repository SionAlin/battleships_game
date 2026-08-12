package main.model;

public enum ShipType{
    CARRIER(5),
    BATTLESHIP(4),
    DESTROYER(4),
    SUBMARINE(3),
    PATROLBOAT(2);

    private final int length;
    private final Ship shipInstance;

    ShipType(int length){
        this.length = length;
        this.shipInstance = new Ship(length);
    }

    public Ship getShip(){
        return shipInstance;
    }

    public int getLength(){
        return length;
    }
}
