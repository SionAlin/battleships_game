package logic.player;

import model.*;
import java.awt.Point;

public class HumanPlayer extends Player{

    @Override
    public Point makeMove(Board opponentBoard){
        return new Point(2,3);
    }

}
