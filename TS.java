package main.java;

import java.util.ArrayList;
import java.util.LinkedList;

public class TS {
    public ArrayList<Point> combinedPoints;
    public ArrayList<Point> selectedPoints;
    private final int maxIterations = 5000;
    private final int tabuListSize = 200;
    public GA gaInstance;
    public SA saInstance;
    public Point startPoint = new Point(1, 1);
    private final LinkedList<ArrayList<Point>> tabuList = new LinkedList<>();
    public ArrayList<Point> topSolution;
    public double topDistance;
    public RouteResult TSRouteResult;

    public TS(ArrayList<Point> combinedPoints, ArrayList<Point> selectedPoints) {
        this.combinedPoints = combinedPoints;
        this.selectedPoints = selectedPoints;
        this.gaInstance = new GA(combinedPoints, selectedPoints);
        this.saInstance = new SA(combinedPoints, selectedPoints);
    }

    public RouteResult runTS() {
        ArrayList<Point> initialRoute = gaInstance.buildNearestNeighborRoute(gaInstance.startPoint, combinedPoints);
        ArrayList<Point> currentSolution = new ArrayList<>(initialRoute);
        topSolution = new ArrayList<>(currentSolution);
        topDistance = gaInstance.getRouteDistance(topSolution);

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            //double currentDistance = gaInstance.getRouteDistance(currentSolution);
            ArrayList<Point> newSolution = new ArrayList<>(currentSolution);
            ArrayList<Point> route1 = saInstance.twoOptSwap(newSolution);
            ArrayList<Point> route2 = saInstance.swapTwoCities(newSolution);
            newSolution = gaInstance.sequentialConstructiveCrossover(startPoint, route1, route2);
            double newDistance = gaInstance.getRouteDistance(newSolution);
            // Check if the new solution is in the tabu list
            if (!isInTabuList(newSolution) || newDistance < topDistance) {
                currentSolution = new ArrayList<>(newSolution);
                //currentDistance = newDistance;
                addToTabuList(newSolution);
                if (newDistance < topDistance) {
                    topSolution = new ArrayList<>(newSolution);
                    topDistance = newDistance;
                }
            }
            if (iteration % 100 == 0) {
                //System.out.println("Iteration: " + iteration + " Current Distance: " + currentDistance);
            }
        }
        System.out.println("\nTS Distance: " + topDistance);
        //topSolution = gaInstance.getAllIntermediatePoints(topSolution);
        System.out.println("TS Route: " + topSolution);
        TSRouteResult = new RouteResult(topSolution,topDistance,"TS");
        return TSRouteResult;
    }

    // Add a new solution to the tabu list and remove old entries if the list exceeds the limit
    private void addToTabuList(ArrayList<Point> solution) {
        tabuList.add(new ArrayList<>(solution));
        if (tabuList.size() > tabuListSize) {
            tabuList.removeFirst();  // Remove the oldest solution
        }
    }

    // Check if the solution is in the tabu list
    private boolean isInTabuList(ArrayList<Point> solution) {
        for (ArrayList<Point> tabuSolution : tabuList) {
            if (tabuSolution.equals(solution)) {
                return true;
            }
        }
        return false;
    }
}
