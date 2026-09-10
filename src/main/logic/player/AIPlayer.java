package logic.player;

import model.*;
import java.awt.Point;
import java.util.Arrays;
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
        int index;
        Point point;
        if(NextPossibleHits.size() == 0){

            index = random.nextInt(BoardCells.size());
            point = BoardCells.removes(index);

            opponentBoard[point.x][point.y].setEnemyHit(1);
            PlayerBoard[point.x][point.y].setPlayerHit(1);

            if(opponentBoard[point.x][point.y].getCell() == 1){
                if(point.y > 0)
                    NextPossibleHits.add(new Point(point.x, point.y-1));
                if(point.y < 9)
                    NextPossibleHits.add(new Point(point.x, point.y+1));
                if(point.x > 0)
                    NextPossibleHits.add(new Point(point.x-1, point.y));
                if(point.x < 9)
                    NextPossibleHits.add(new Point(point.x+1, point.y));
            }
        }else{
            index = random.nextInt(NextPossibleHits.size());
            point = NextPossibleHits.removes();

            opponentBoard[point.x][point.y].setEnemyHit(1);
            PlayerBoard[point.x][point.y].setPlayerHit(1);

            if(opponentBoard[point.x][point.y].getCell() == 1){

            }
        }
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
