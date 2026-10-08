package main.logic.player;

import main.model.*;
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
        int index;
        Point point;
    
        if(NextPossibleHits.isEmpty() && BoardCells.isEmpty())
            BoardCells = RemainingCells();

        if(NextPossibleHits.size() == 0){

            index = random.nextInt(BoardCells.size());
            point = BoardCells.remove(index);

            if(PlayerBoard.board[point.x][point.y].getPlayerHit() == 1){
                makeMove(opponentBoard);
                return;
            }

            opponentBoard.board[point.x][point.y].setEnemyHit(1);
            PlayerBoard.board[point.x][point.y].setPlayerHit(1);

            if(opponentBoard.board[point.x][point.y].getCell() == 1){
                recordHit(true);

                if(point.y > 0)
                    NextPossibleHits.add(new Point(point.x, point.y-1));
                if(point.y < 9)
                    NextPossibleHits.add(new Point(point.x, point.y+1));
                if(point.x > 0)
                    NextPossibleHits.add(new Point(point.x-1, point.y));
                if(point.x < 9)
                    NextPossibleHits.add(new Point(point.x+1, point.y));  
            }else{
                recordHit(false);
            }
        }else{
            index = random.nextInt(NextPossibleHits.size());
            point = NextPossibleHits.remove(index);

            if(PlayerBoard.board[point.x][point.y].getPlayerHit() == 1){
                makeMove(opponentBoard);
                return;
            }

            opponentBoard.board[point.x][point.y].setEnemyHit(1);
            PlayerBoard.board[point.x][point.y].setPlayerHit(1);

            if(opponentBoard.board[point.x][point.y].getCell() == 1){
                recordHit(true);
                
                if(!(point.x == 0 || point.y == 0 || point.x == 9 || point.y == 9)){
                    if(PlayerBoard.board[point.x-1][point.y].getPlayerHit() == 1 && opponentBoard.board[point.x-1][point.y].getCell() == 1 && PlayerBoard.board[point.x+1][point.y].getPlayerHit() == 0)
                        NextPossibleHits.add(new Point(point.x+1, point.y));
                    if(PlayerBoard.board[point.x+1][point.y].getPlayerHit() == 1 && opponentBoard.board[point.x+1][point.y].getCell() == 1 && PlayerBoard.board[point.x-1][point.y].getPlayerHit() == 0)
                        NextPossibleHits.add(new Point(point.x-1, point.y));
                    if(PlayerBoard.board[point.x][point.y-1].getPlayerHit() == 1 && opponentBoard.board[point.x][point.y-1].getCell() == 1 && PlayerBoard.board[point.x][point.y+1].getPlayerHit() == 0)
                        NextPossibleHits.add(new Point(point.x, point.y+1));
                    if(PlayerBoard.board[point.x][point.y+1].getPlayerHit() == 1 && opponentBoard.board[point.x][point.y+1].getCell() == 1 && PlayerBoard.board[point.x][point.y-1].getPlayerHit() == 0)
                        NextPossibleHits.add(new Point(point.x, point.y-1));
                }

            }else{
                recordHit(false);
            }
        }
    }

    private ArrayList<Point> RemainingCells(){
        ArrayList<Point> list = new ArrayList<Point>();

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(PlayerBoard.board[i][j].getPlayerHit() == 0)
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
