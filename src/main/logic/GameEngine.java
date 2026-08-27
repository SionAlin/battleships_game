package main.logic;

import model.*;

public class GameEngine{
   
    public static void main(String[] args){

    }

    public GameEngine(){
        
    }

    public void start(String GameMode){
        try{
            if(GameMode.equals("PlayerVsPlayer")){
                PlayerVsPlayer();
            }else if(GameMode.equals("PlayerVsComputer")){
                PlayerVsComputer();
            }else{
                throw new Exception("Error: This mode dosen't exists!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void PlayerVsPlayer(){
        
    }

    private void PlayerVsComputer(){
        PlacementInputProvider PIP = new ConsoleInputProvider();
        ShipPlacer shipPlacer = new ShipPlacer();

        GamePlayer Player = new GamePlayer(PIP.getName());
        GamePlayer Computer = new GamePlayer("Computer");
 
        shipPlacer.ComputerShipPlacer(Computer.PlayerBoard);
        shipPlacer.PlayerShipPlacer(Player.PlayerBoard);

        boolean run = true;
        while(run){
            
            

            if(Player.PlayerBoard.ShipState() == true && Computer.PlayerBoard.ShipState() == false){
                System.out.println(Player.getPlayerName() + "WON ");
                run = false;
            }else if(Player.PlayerBoard.ShipState() == false && Computer.PlayerBoard.ShipState() == true){
                System.out.println(Computer.getPlayerName() + "WON");
                run = false;
            }
        }
    }

}
