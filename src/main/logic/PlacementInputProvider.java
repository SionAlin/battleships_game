package main.logic;

import model.*;
import java.awt.Point;

public interface PlacementInputProvider{
   
    String getName();
    Placement getPlacement(ShipType ship);
    Point getMove();
}
