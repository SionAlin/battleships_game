package main.logic;

import model.*;
import java.awt.Point;
import java.util.*;

public class ShipPlacer{
    
    private ArrayList<Point> IntervalsX, IntervalsY;

    public ShipPlacer(){
        IntervalsX = new ArrayList<>();
        IntervalsY = new ArrayList<>();
    }

    public void PlayerShipPlacer(Board board){
    }

    public void ComputerShipPlacer(Board board){

    }

    

    private void IntervalsGenerator(Point point, int rotation, Point TopPoint, Point BottomPoint, int ShipLength){
        Point XInterval, YInterval;

        XInterval = new Point(TopPoint.getX(), BottomPoint.getX());
        YInterval = new Point(TopPoint.getY(), BottomPoint.getY());

        IntervalsX.add(XInterval);
        IntervalsY.add(YInterval);

        if(rotation == 0){

            XInterval = new Point(TopPoint.getX(), point.getX() - 1);
            IntervalsX.add(XInterval);
            YInterval = new Point(point.getY(), (point.getY() - 1) + ShipLength);
            IntervalsY.add(YInterval);

            XInterval = new Point(point.getX() + 1, BottomPoint.getX());
            IntervalsX.add(XInterval);
            YInterval = new Point(point.getY(), (point.getY() - 1) + ShipLength);
            IntervalsY.add(YInterval);
        }else{

            XInterval = new Point(TopPoint.getX(), point.getX() - 1);
            IntervalsX.add(XInterval);
            YInterval = new Point(point.getY(), point.getY());
            IntervalsY.add(YInterval);

            XInterval = new Point(point.getX() + ShipLength, BottomPoint.getX());
            IntervalsX.add(XInterval);
            YInterval = new Point(point.getY(), point.getY());
            IntervalsY.add(YInterval);
        }

        XInterval = new Point(TopPoint.getX(), BottomPoint.getX());
        YInterval = new Point(point.getY() + 1, BottomPoint.getY());
    }

}
