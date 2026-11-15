package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.model.*;
import main.logic.*;
import java.awt.Point;

public class BoardTests{

    @Test
    public void ShipPlacer_Test() throws Exception{

        Board testPlayerBoard = new Board();
        ShipPlacer testPlayerShipPlacer = new ShipPlacer();
        Board testComputerBoard = new Board();
        ShipPlacer testComputerShipPlacer = new ShipPlacer();

        testComputerShipPlacer.ComputerShipPlacer(testComputerBoard);
        System.out.println(testComputerBoard.toString());
    }

    @Test
    public void Board_Test() throws Exception{

        Board testBoard = new Board();
        Cell[][] b = testBoard.getBoard();
        ShipType testShipFirst = ShipType.CARRIER;
        ShipType testShipSecond = ShipType.BATTLESHIP;
        
        Point centralPoint = new Point(3,3);
        Point OFBPoint = new Point(9,9);

        // Test 1
        testBoard.AddShip(testShipFirst, centralPoint, 0);

        boolean check = true;
        for(int i = 3; i < 3 + testShipFirst.getLength(); i++)
            if(b[3][i].getCell() == 0)
                check = false;
        
        assertEquals(true, check, "Board test 1 : Failed");
        System.out.println("Board test 1 : Passed");

        // Test 2
        assertThrows(Exception.class, () -> {
            testBoard.AddShip(testShipFirst, OFBPoint, 1);
        });
        
        assertEquals(0, b[9][9].getCell(), "Board test 2 : Failed");
        System.out.println("Board test 2 : Passed");
        
        // Test 3
        assertThrows(Exception.class, () -> {
            testBoard.AddShip(testShipSecond, centralPoint, 1);
        });
        assertEquals(0, b[4][3].getCell(), "Board test 3 : Failed");
        System.out.println("Board test 3 : Passed");

        // Test 4
        assertEquals(true, testBoard.HasShipsLeft(), "Board test 4 : Failed");
        System.out.println("Board test 4 : Passed");
    
        // Test 5
        assertThrows(IllegalArgumentException.class, () -> testBoard.StringToBoard("110011..."));
        assertThrows(IllegalArgumentException.class, () -> testBoard.StringToBoard("2".repeat(100)));
        assertThrows(IllegalArgumentException.class, () -> testBoard.StringToBoard("0".repeat(100)));
        assertThrows(IllegalArgumentException.class, () -> testBoard.StringToBoard("1".repeat(100)));
        assertThrows(IllegalArgumentException.class, () -> testBoard.BoardToString());
        System.out.println("Board test 5 : Passed");
    
        // Test 6
        Board testOriginalBoard = new Board();
        ShipPlacer shipPlacer = new ShipPlacer();
        shipPlacer.ComputerShipPlacer(testOriginalBoard);

        String testBoardToString = testOriginalBoard.BoardToString();
        Board testStringToBoard = new Board();
        testStringToBoard.StringToBoard(testBoardToString);

        Cell[][] originalBoard = testOriginalBoard.getBoard();
        Cell[][] stringBoard = testStringToBoard.getBoard();

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                assertEquals(originalBoard[i][j].getCell(), stringBoard[i][j].getCell(), "Board test 6 : Failed");
            }
        }
        assertTrue(testStringToBoard.HasShipsLeft(), "Board test 6 : HasShipsLeft after StringToBoard");
        System.out.println("Board test 6 : Passed");
    }
    
}
