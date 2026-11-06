package main.model;

import java.awt.Point;

public class Board{
    
    private Cell[][] board;
    private int shipCellsLeft = 0;

    public Board(){
        this.board = new Cell[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                board[i][j] = new Cell(0);
            }
        }
    }
    
    public boolean CanPlace(ShipType type, int row, int col, int rotation){
        int len = type.getLength();
        if(row < 0 || col < 0 || row > 9 || col > 9) return false;
        if(rotation == 0 && col + len > 10) return false;
        if(rotation == 1 && row + len > 10) return false;

        for(int i = 0;  i < len; i++){
            int r = rotation == 1 ? row + i : row;
            int c = rotation == 0 ? col + i : col;
            if(board[r][c].getCell() == 1) return false;
        }
        return true;
    }

    public void AddShip(ShipType type, Point p, int rotation) throws Exception{
        if(!CanPlace(type, p.x, p.y, rotation))
            throw new Exception("Error: invalid ship position!");

        for(int i = 0; i < type.getLength(); i++){
            int r = rotation == 1 ? p.x + i : p.x;
            int c = rotation == 0 ? p.y + i : p.y;
            board[r][c].setCell(1);
            shipCellsLeft++;
        }
    }

    public ShotResult Shoot(int row, int col){
        if(board[row][col].getEnemyHit() != 0) return ShotResult.ALREADY_SHOT;
        board[row][col].setEnemyHit(1);

        if(board[row][col].getCell() == 1){
            shipCellsLeft--;
            return ShotResult.HIT;
        }
        return ShotResult.MISS;
    }

    public boolean HasShipsLeft(){
        return shipCellsLeft > 0;
    }
    
    public Cell[][] getBoard(){
        return board;
    }

    public void setBoard(Cell[][] board){
        this.board = new Cell[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                this.board[i][j] = board[i][j];
            }
        }
    }
}
