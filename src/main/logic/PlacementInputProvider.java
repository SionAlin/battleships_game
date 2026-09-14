package main.logic;

import main.model.*;
import java.awt.Point;

public interface PlacementInputProvider{
   
    String getName();
    Placement getPlacement(ShipType ship);
    Point getMove();
}
