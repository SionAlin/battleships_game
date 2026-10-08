package main.logic;

import main.model.*;
import java.util.Scanner;
import java.awt.Point;

public class ConsoleInputProvider implements PlacementInputProvider{
    
    private static final Scanner scanner = new Scanner(System.in);

    public String getName(){
        
        System.out.print("Set an player name: ");
        String playerName = scanner.nextLine();
        
        return playerName;
    }

    public Placement getPlacement(ShipType ship){

        System.out.println("Place the Ship: ");
        System.out.print("X axis: ");
        int x = scanner.nextInt();
        System.out.print("Y axis: ");
        int y = scanner.nextInt();
        System.out.print("Rotation: ");
        int rotation = scanner.nextInt();
        
        return new Placement(new Point(x, y), rotation);
    }

    public Point getMove(){
        
        System.out.println("Hit the target: ");
        System.out.print("X axis: ");
        int x = scanner.nextInt();
        System.out.print("Y axis: ");
        int y = scanner.nextInt();
        
        return new Point(x, y);
    }
}
