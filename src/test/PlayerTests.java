package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.model.*;
import main.logic.*;
import java.awt.Point;

public class BoardTests{

    @Test
    public void ShipPlacer_Test(){

        Board testPlayerBoard = new Board();
        ShipPlacer testPlayerShipPlacer = new ShipPlacer();
        Board testComputerBoard = new Board();
        ShipPlacer testComputerShipPlacer = new ShipPlacer();

        testPlayerShipPlacer.PlayerShipPlacer(testPlayerBoard);
        System.out.println(testPlayerBoard.toString());

        testComputerShipPlacer.ComputerShipPlacer(testComputerBoard);
        System.out.println(testComputer
import main.model.*;
import main.logic.*;

public class PlayerTests{
    
    @Test
    public void AIPlayer_Test(){
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
