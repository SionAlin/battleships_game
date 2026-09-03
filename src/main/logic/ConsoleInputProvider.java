package main.logic;

import model.*;
import java.util.Scanner;
import java.awt.Point;

public class ConsoleInputProvider implements PlacementInputProvider{
    
    private Scanner scanner;

    public String getName(){
        scanner = new Scanner(System.in);
        
        System.out.print("Set an player name: ");
        String playerName = scanner.nextLine();
        
        scanner.close();
        return playerName;
    }

    public Placement getPlacement(ShipType ship){
        scanner = new Scanner(System.in);

        System.out.println("Place the Ship: ");
        System.out.print("X axis: ");
        int x = scanner.nextInt();
        System.out.print("Y axis: ");
        int y = scanner.nextInt();
        System.out.print("Rotation: ");
        int rotation = scanner.nextInt();
        
        scanner.close();
        return new Placement(new Point(x, y), rotation);
    }

    public Point getMove(){
        scanner = new Scanner(System.in);
        
        System.out.println("Hit the target: ");
        System.out.print("X axis: ");
        int x = scanner.nextInt();
        System.out.print("Y axis: ");
        int y = scanner.nextInt();
        
        scanner.close();
        return new Point(x, y);
    }
}
