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
        
        if(PlayerBoard.board[point.x][point.y].getPlayerHit() == 0){
            PlayerBoard.board[point.x][point.y].setPlayerHit(1);
            opponentBoard.board[point.x][point.y].setEnemyHit(1);

            if(opponentBoard.board[point.x][point.y].getCell() == 1){
                RecordHit(true);
            }else{
                RecordHit(false);
            }
        }
    }

}
