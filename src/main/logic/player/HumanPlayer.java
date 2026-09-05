package logic.player;

import model.*;
import logic.*;
import java.awt.Point;

public class HumanPlayer extends Player{

    @Override
    public void makeMove(Board opponentBoard){
        PlacementInputProvider PIP = new ConsoleInputProvider();
        Point point = PIP.getMove();
        
        if(PlayerBoard[point.x][point.y].getPlayerHit() == 0){
            PlayerBoard[point.x][point.y].setPlayerHit(1);
            opponentBoard[point.x][point.y].setEnemyHit(1);

            if(opponentBoard[point.x][point.y].getCell() == 1){
                RecordHit(true);
            }else{
                RecordHit(false);
            }
        }
    }

}
