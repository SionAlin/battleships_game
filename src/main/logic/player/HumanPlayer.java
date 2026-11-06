package main.logic.player;

import main.model.*;
import main.logic.*;
import java.awt.Point;

public class HumanPlayer extends Player{

    public HumanPlayer(String PlayerName){
        super(PlayerName);
    }

    @Override
    public void makeMove(Board opponentBoard){
        PlacementInputProvider PIP = new ConsoleInputProvider();
        Point point = PIP.getMove();
        
        fire(opponentBoard, point.x, point.y); 
    }

}
