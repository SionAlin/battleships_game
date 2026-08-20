package main.logic;

import model.*;

public interface PlacementInputProvider{
   
    Placement getPlacement(ShipType ship);
}
