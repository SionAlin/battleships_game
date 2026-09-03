package logic.player;

import model.*;
import java.awt.Point;

public class AIPlayer extends Player{

    @Override
    public Point makeMove(Board opponentBoard){
        return new Point(3,5);
    }

}
