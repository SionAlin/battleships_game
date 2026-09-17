package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.model.*;
import main.logic.*;
import main.logic.player.*;
import java.awt.Point;

public class PlayerTests{
    
    @Test
    public void AIPlayer_Test() throws Exception{
        Board enemyBoard = new Board();
        enemyBoard.AddShip(ShipType.CARRIER, new Point(3,3), 0);
        
        Player aiPlayer = new AIPlayer("AIPlayer");
        aiPlayer.makeMove(enemyBoard);
        System.out.println(enemyBoard.toString());
    
        aiPlayer.makeMove(enemyBoard);
        System.out.println(enemyBoard.toString());
        
        aiPlayer.makeMove(enemyBoard);
        System.out.println(enemyBoard.toString());
        
        aiPlayer.makeMove(enemyBoard);
        System.out.println(enemyBoard.toString());
        
        aiPlayer.makeMove(enemyBoard);
        System.out.println(enemyBoard.toString());
        
        aiPlayer.makeMove(enemyBoard);
        System.out.println(enemyBoard.toString());
        
        aiPlayer.makeMove(enemyBoard);
        System.out.println(enemyBoard.toString());
    }
    
}
