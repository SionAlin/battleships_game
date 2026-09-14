package main.ui;

import main.logic.*;
import main.model.*;
import java.awt.Point;

public class GuiInputProvider implements PlacementInputProvider{

    public String getName(){
        return "name";
    }
    
    public Placement getPlacement(ShipType ship){
        return new Placement(new Point(0,0), 0);
    }

    public Point getMove(){
        return new Point(0,0);
    }
}
