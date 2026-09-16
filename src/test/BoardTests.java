package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.model.*;
import main.logic.*;
import java.awt.Point;

public class BoardTests{

    @Test
    public void ShipPlacer_Test(){}

    @Test
    public void Board_Test(){

        Board testBoard = new Board();
        ShipType testShipFirst = ShipType.CARRIER;
        ShipType testShipSecond = ShipType.BATTLESHIP;
        
        Point centralPoint = new Point(3,3);
        Point OFBPoint = new Point(9,9);

        // Test 1
        testBoard.AddShip(testShipFirst, centralPoint, 0);

        boolean check = true;
        for(int i = 3; i < 3 + testShipFirst.getLength(); i++)
            if(testBoard.board[3][i].getCell() == 0)
                check = false;
        
        assertEquals(true, check, "Board test 1 : Failed");
        System.out.println("Board test 1 : Passed");

        // Test 2
        testBoard.AddShip(testShipFirst, OFBPoint, 1);
        
        assertEquals(0, testBoard.board[9][9].getCell(), "Board test 2 : Failed");
        System.out.println("Board test 2 : Passed");
        
        // Test 3
        testBoard.AddShip(testShipSecond, centralPoint, 1);
        assertEquals(0, testBoard.board[4][3].getCell(), "Board test 3 : Failed");
        System.out.println("Board test 3 : Passed");
    }
    
    @Test
    public void Ship_Test(){}

    @Test
    public void ShipType_Test(){}
}
