package main.logic;

import java.awt.Point;

public class Placement{
    private Point point;
    private int rotation;

    public Placement(Point point, int rotation){
        this.point = point;
        this.rotation = rotation;
    }

    public Point getPoint(){
        return point;
    }

    public int getRotation(){
        return rotation;
    }
}
