package logic.player;

import model.*;
import java.awt.Point;
import java.util.Arrays;

public class AIPlayer extends Player{
    
    private ArrayList<Point> BoardCells;
    private ArrayList<Point> NextPossibleHits;
    
    public AIPlayer(String PlayerName){
        super(PlayerName);
        this.BoardCells = this.ConvenientBoardCells();
        this.NextPossibleHits = new ArrayList<Point>();
    }
    
    @Override
    public void makeMove(Board opponentBoard){
        
    }

    private ArrayList<Point> ConvenientBoardCells(){
        ArrayList<Point> list = new ArrayList<Point>();

        for(int i = 0; i < 9; i += 2){
            for(int j = 0; j < 9; j += 2){
                list.add(Point(i,j));
                list.add(Point(i+1, j+1));
            }
        }

        return list;
    }

}
