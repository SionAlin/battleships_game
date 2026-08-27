package main.logic;

import model.*;

public interface PlacementInputProvider{
   
    String getName();
    Placement getPlacement(ShipType ship);
}
