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


    }

}
