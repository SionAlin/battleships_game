package main.logic;

import main.model.*;
import main.logic.player.*;

public class GameEngine{
   
    public static void main(String[] args){
        new GameEngine();
    }

    public GameEngine(){
        
        String gameMode = "PlayerVsComputer";

        start(gameMode);
    }

    public void start(String gameMode){
        try{
            if(gameMode.equals("PlayerVsPlayer")){
                PlayerVsPlayer();
            }else if(gameMode.equals("PlayerVsComputer")){
                PlayerVsComputer();
            }else{
                throw new Exception("Error: This mode dosen't exists!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void PlayerVsPlayer() throws Exception{
        PlacementInputProvider inputProvider = new ConsoleInputProvider();

        System.out.println("Player 1");
        Player player1 = new HumanPlayer(inputProvider.getName());
        System.out.println("Player 2");
        Player player2 = new HumanPlayer(inputProvider.getName());

        PlacementHelp();
        PrintBoards(player1, player2);
        System.out.println("\n" + player1.getPlayerName() + ", place your ships: ");
        new ShipPlacer(inputProvider).PlayerShipPlacer(player1.getPlayerBoard());
        ClearScreen();

        PrintBoards(player2, player1);
        System.out.println(player2.getPlayerName() + ", place your ships: ");
        new ShipPlacer(inputProvider).PlayerShipPlacer(player2.getPlayerBoard());
        ClearScreen();

        PlayGame(player1, player2, true);

    }

    private void PlayerVsComputer() throws Exception{
        PlacementInputProvider inputProvider = new ConsoleInputProvider();

        Player player = new HumanPlayer(inputProvider.getName());
        Player computer = new AIPlayer("Com");

        new ShipPlacer().ComputerShipPlacer(computer.getPlayerBoard());

        PlacementHelp();
        PrintBoards(player, computer);
        new ShipPlacer(inputProvider).PlayerShipPlacer(player.getPlayerBoard());

        PlayGame(player, computer, false);
    }

    private void PlayGame(Player player1, Player player2, boolean hotSeat){
        Player current = player1;
        Player opponent = player2;
        boolean gameRunning = true;

        while(gameRunning){
            System.out.println("\n=== " + current.getPlayerName() + "'s turn ===");

            if(current instanceof HumanPlayer)
                PrintBoards(current, opponent);

            int shotsBefore = current.getCorrectHits() + current.getWrongHits();
            int hitsBefore = current.getCorrectHits();

            current.makeMove(opponent.getPlayerBoard());

            if(current.getCorrectHits() + current.getWrongHits() == shotsBefore){
                System.out.println("You already shot there! Choose another cell.");
                continue;
            }

            System.out.println(current.getCorrectHits() > hitsBefore ? "Hit!" : "Miss.");

            if(!opponent.getPlayerBoard().ShipsState()){
                gameRunning = false;
                System.out.println("\nWinner " + current.getPlayerName() + "!");

                if(current instanceof HumanPlayer)
                    PrintBoards(current, opponent);

                System.out.println("\n" + current.getPlayerName() + "WON!");
                PrintStats(player1);
                PrintStats(player2);
            }else{
                Player temp = current;
                current = opponent;
                opponent = temp;

                if(hotSeat)
                    ClearScreen();
            }
        }
    }

    private void PrintBoards(Player me, Player enemy){
        Board mine = me.getPlayerBoard();
        Board theirs = enemy.getPlayerBoard();
        String header = "  0 1 2 3 4 5 6 7 8 9";

        System.out.println("\n   YOUR FLEET          YOUR SHOTS");
        System.out.println(header + "     " + header);

        for(int y = 0; y < 10; y++){
            StringBuilder row = new StringBuilder(y + " ");

            for(int x = 0; x < 10; x++){
                Cell c = mine.board[x][y];
                char ch;
                if(c.getCell() == 1)
                    ch = (c.getEnemyHit() == 1) ? 'X' : 'S';
                else
                    ch = (c.getEnemyHit() == 1) ? 'o' : '¬';

                row.append(ch);
                if(x < 9) row.append(' ');
            }

            row.append("     ").append(y).append(" ");

            for(int x = 0; x < 10; x++){
                char ch;
                if(mine.board[x][y].getPlayerHit() == 0)
                    ch = '.';
                else
                    ch = (theirs.board[x][y].getCell() == 1) ? 'X' : 'o';
                
                row.append(ch);
                if(x < 9) row.append(' ');
            }
            
            System.out.println(row);

        }

        System.out.println("S ship, X hit, o miss, ¬ water, .not shot yet");

    }

    private void PrintStats(Player player){
        System.out.println(player.getPlayerName() + ": " + player.getCorrectHits() + " hits, " + player.getWrongHits() + " misses (" + Math.round(player.HitRatio() * 100) + "% accuracy)");
    }

    private void PlacementHelp(){
        System.out.println("\nX axis = column (0-9), Y axis = row (0-9).");
        System.out.println("Rotation: 0 = ship goes down (along Y), 1 = ship goes right (along X).");
    }

    private void ClearScreen(){
        for(int i = 0; i < 40; i++)
            System.out.println();
    }

}
