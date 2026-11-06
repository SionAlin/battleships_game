package main.logic.player;

import main.model.*;
import main.logic.ShipPlacer;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player{
    
    private Random random;
    private ArrayList<Point> BoardCells;
    private ArrayList<Point> NextPossibleHits;
    
    public AIPlayer(String PlayerName){
        super(PlayerName);
        
        this.random = new Random();
        this.BoardCells = this.ConvenientBoardCells();
        this.NextPossibleHits = new ArrayList<Point>();
    }

    @Override
    public void makeMove(Board opponentBoard){
        if(NextPossibleHits.isEmpty() && BoardCells.isEmpty())
            BoardCells = RemainingCells();
        
        boolean hunting = NextPossibleHits.isEmpty();
        ArrayList<Point> source = hunting ? BoardCells : NextPossibleHits;
        Point p = source.remove(random.nextInt(source.size()));

        ShotResult result = fire(opponentBoard, p.x, p.y);
        if(result == ShotResult.ALREADY_SHOT){
            makeMove(opponentBoard);
            return;
        }
        if(result != ShotResult.HIT) return;

        if(hunting){
            if(p.y > 0) NextPossibleHits.add(new Point(p.x, p.y - 1));
            if(p.y < 9) NextPossibleHits.add(new Point(p.x, p.y + 1));
            if(p.x > 0) NextPossibleHits.add(new Point(p.x - 1, p.y));
            if(p.x < 9) NextPossibleHits.add(new Point(p.x + 1, p.y));
        }else if(p.x > 0 && p.x < 9 && p.y > 0 && p.y < 9){
            if(myHit(p.x - 1, p.y) && unshot(p.x + 1, p.y)) NextPossibleHits.add(new Point(p.x + 1, p.y));
            if(myHit(p.x + 1, p.y) && unshot(p.x - 1, p.y)) NextPossibleHits.add(new Point(p.x - 1, p.y));
            if(myHit(p.x, p.y - 1) && unshot(p.x, p.y + 1)) NextPossibleHits.add(new Point(p.x, p.y + 1));
            if(myHit(p.x, p.y + 1) && unshot(p.x, p.y - 1)) NextPossibleHits.add(new Point(p.x, p.y - 1));
        }
    }
    
    public void PlaceShips() throws Exception{
        new ShipPlacer().ComputerShipPlacer(PlayerBoard);
    }

    private boolean myHit(int x, int y){ 
        Cell[][] board = PlayerBoard.getBoard();
        return board[x][y].getPlayerHit() == 2;
    }

    private boolean unshot(int x, int y){
        Cell[][] board = PlayerBoard.getBoard();
        return board[x][y].getPlayerHit() == 0;
    }

    private ArrayList<Point> RemainingCells(){
        ArrayList<Point> list = new ArrayList<Point>();
        Cell[][] board = PlayerBoard.getBoard();

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(board[i][j].getPlayerHit() == 0)
                    list.add(new Point(i, j));
            }
        }

        return list;
    }

    private ArrayList<Point> ConvenientBoardCells(){
        ArrayList<Point> list = new ArrayList<Point>();

        for(int i = 0; i < 9; i += 2){
            for(int j = 0; j < 9; j += 2){
                list.add(new Point(i,j));
                list.add(new Point(i+1, j+1));
            }
        }

        return list;
    }

}
