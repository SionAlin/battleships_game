package main.logic;

import model.*;
import java.awt.Point;
import java.util.*;

public class ShipPlacer{
    
    private ArrayList<Point> IntervalsX, IntervalsY;

    public ShipPlacer(){
        IntervalsX = new ArrayList<>();
        IntervalsX.add(new Point(0,9));

        IntervalsY = new ArrayList<>();
        IntervalsY.add(new Point(0,9));
    }

    public void PlayerShipPlacer(Board board){
    }

    public void ComputerShipPlacer(Board board){
        
        Placement placement;
        int index;
        Point TopPoint, BottomPoint;

        for(ShipType ship: ShipType.getValue()){
            index = BiggestArea();
            placement = Place(IntervalsX.get(index), IntervalsY.get(index), ship.getLength());
            board.AddShip(ship, placement.getPoint(), placement.getRotation());
        
            TopPoint = new Point(IntervalsX.get(index).x, IntervalsY.get(index).x;
            BottomPoint = new Point(IntervalsX.get(index).y, IntervalsY.get(index).y;

            IntervalsGenerator(placement.getPoint(), placement.getRotation(), TopPoint, BottomPoint, ship.getLength());

            IntervalsX.remove(index);
            IntervalsY.remove(index);
        }
    }

    public int BiggestArea(){
        int maxArea = -1, maxI = -1;
        int area, width, height;
        for(int i = 0; i < IntervalsX.size(); i++){
            Point xInterval = IntervalsX.get(i);
            Point yInterval = IntervalsY.get(i);

            width = xInterval.y - xInterval.x + 1;
            height = yInterval.y - yInterval.x + 1;
            area = width * height;

            if(maxArea < area){
                maxArea = area;
                maxI = i;
            }
        }
        return maxI;
    }

    public Placement Place(Point xInterval, Point yInterval, int shipLength){
        int rotation;
        Random rand = new Random();
        
        if(xInterval.y - xInterval.x + 1 < shipLength)
            rotation = 0;
        else if(yInterval.y - yInterval.x + 1 < shipLength)
            rotation = 1;
        else 
            rotation = rand.nextBoolean() ? 0 : 1;

        int x = 0, y = 0;
        int xmax, ymax;

        if(rotation == 0){
        
            ymax = yInterval.y - shipLength + 1;
            x = rand.nextInt(xInterval.y - xInterval.x + 1) + xInterval.x;
            y = rand.nextInt(ymax - yInterval.x + 1) + yInterval.x;

        }else if(rotation == 1){

            xmax = xInterval.y - shipLength + 1;
            x = rand.nextInt(xmax - xInterval.x + 1) + xInterval.x;
            y = rand.nextInt(yInterval.y - yInterval.x + 1) + yInterval.x;

        }

        return new Placement(new Point(x, y), rotation);
    } 

    private void IntervalsGenerator(Point point, int rotation, Point TopPoint, Point BottomPoint, int ShipLength){
        Point XInterval, YInterval;

        XInterval = new Point(TopPoint.x, BottomPoint.x);
        YInterval = new Point(TopPoint.y, point.y - 1);

        IntervalsX.add(XInterval);
        IntervalsY.add(YInterval);

        if(rotation == 0){

            XInterval = new Point(TopPoint.x, point.x - 1);
            IntervalsX.add(XInterval);
            YInterval = new Point(point.y, (point.y - 1) + ShipLength);
            IntervalsY.add(YInterval);

            XInterval = new Point(point.x + 1, BottomPoint.x);
            IntervalsX.add(XInterval);
            YInterval = new Point(point.y, (point.y - 1) + ShipLength);
            IntervalsY.add(YInterval);
        }else{

            XInterval = new Point(TopPoint.x, point.x - 1);
            IntervalsX.add(XInterval);
            YInterval = new Point(point.y, point.y);
            IntervalsY.add(YInterval);

            XInterval = new Point(point.x + ShipLength, BottomPoint.x);
            IntervalsX.add(XInterval);
            YInterval = new Point(point.y, point.y);
            IntervalsY.add(YInterval);
        }

        XInterval = new Point(TopPoint.x, BottomPoint.x);
        YInterval = new Point(point.y + 1, BottomPoint.y);
    }
}

class Placement{
    private Point point;
    private int rotation;

    public Placement(Point point, int rotation){
        this.point = point;
        this.rotation = rotation;
    }

    public Point getPoint(){
        return point;
    }

    public int getRotation(){
        return rotation;
    }
}
