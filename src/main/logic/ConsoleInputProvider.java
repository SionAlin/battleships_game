package main.logic;

import model.*;
import java.util.Scanner;
import java.awt.Point;

public class ConsoleInputProvider implements PlacementInputProvider{
    
    private Scanner scanner = new Scanner(System.in);

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
}
