package main.java;

import java.util.ArrayList;

public class RouteResult {
    public final ArrayList<Point> route;
    public final double distance;
    public final String algorithm;

    public RouteResult(ArrayList<Point> route, double distance, String algorithm) {
        this.route = route;
        this.distance = distance;
        this.algorithm = algorithm;
    }

    public ArrayList<Point> getRoute() {
        return route;
    }

    public double getDistance() {
        return distance;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String toString() {
        return "Algorithm: " + algorithm + "\nDistance: " + distance + "\nRoute: " + route; 
    }
}
