package main.model;

import java.awt.Point;

public class Board{
    public Cell[][] board;
   
    public Board(){
        this.board = new Cell[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                board[i][j] = new Cell(0);
            }
        }
    }
    
    public void AddShip(ShipType ShipToAdd, Point PlaceToAdd, int rotation){
        
        int ShipLength = ShipToAdd.getLength();
        int X = (int)PlaceToAdd.getX();
        int Y = (int)PlaceToAdd.getY();
        boolean ValidPosition = true;
        try{
            
            if(X < 0 || X > 9 || Y < 0 || Y > 9)
                throw new Exception("Error: Ship out of bounds!");

            if((rotation == 0 && Y + ShipLength >= 9) || (rotation == 1 && X + ShipLength >= 9))
                throw new Exception("Error: Ship out of bounds!");

            if(rotation == 0){
                for(int i = Y; i < Y + ShipLength; i++){
                    if(board[X][i].getCell() == 1){
                        ValidPosition = false;
                        throw new Exception("Error: Ships overlap!");
                    }
                }

                if(ValidPosition == true){
                    for(int i = Y; i < Y + ShipLength; i++){
                        board[X][i].setCell(1);
                    }
                }
            }

            if(rotation == 1){
                for(int i = X; i < X + ShipLength; i++){
                    if(board[i][Y].getCell() == 1){
                        ValidPosition = false;
                        throw new Exception("Error: Ships overlap!");
                    }
                }

                if(ValidPosition == true){
                    for(int i = X; i < X + ShipLength; i++){
                        board[i][Y].setCell(1);
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // true -> the game continues
    public boolean ShipsState(){
        boolean state = false;
        
        outerLoop:
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(board[i][j].getCell() == 1 && board[i][j].getEnemyHit() == 0){
                    state = true;
                    break outerLoop;
                }
            }
        }

        return state;
    }

    @Override
    public String toString(){
        String str = "";

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                str += "(" + board[i][j].getCell() + ", " + board[i][j].getEnemyHit() + ", " + board[i][j].getPlayerHit() + ") ";
            }
            str += "\n";
        }

        return str;
    }
}
