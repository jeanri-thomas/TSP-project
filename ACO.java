package main.java;

import java.util.ArrayList;
import java.util.Random;

public class ACO {
    public final ArrayList<Point> combinedPoints;
    public final ArrayList<Point> selectedPoints;
    private final Point startPoint = new Point(1.0, 1.0);
    private final int numAnts = 50;
    private final int iterations = 100;
    private final double evaporationRate = 0.5;
    private final double alpha = 1.0; // Influence of pheromone
    private final double beta = 2.0; // Influence of heuristic
    private final Random random = new Random();
    private double[][] pheromoneLevels;
    private double[][] distances;
    public GA gaInstance;
    public ArrayList<Point> overallBestRoute = null;
    public double overallBestDistance = Double.MAX_VALUE;
    public RouteResult ACORouteResult;

    public ACO(ArrayList<Point> combinedPoints, ArrayList<Point> selectedPoints) {
        this.combinedPoints = combinedPoints;
        this.selectedPoints = selectedPoints;
        this.gaInstance = new GA(combinedPoints, selectedPoints);
        combinedPoints.add(0,startPoint);
        initializePheromones();
        calculateDistances();
    }

    public RouteResult runACO() {
        for (int iteration = 0; iteration < iterations; iteration++) {
            ArrayList<ArrayList<Point>> antRoutes = new ArrayList<>();
            for (int ant = 0; ant < numAnts; ant++) {
                ArrayList<Point> route = buildAntRoute(startPoint);
                antRoutes.add(route);
                updatePheromones(route);
            }
            evaporatePheromones();
            ArrayList<Point> bestRouteInIteration = gaInstance.getFittest(antRoutes);
            double bestDistanceInIteration = gaInstance.getRouteDistance(bestRouteInIteration);
            if (bestDistanceInIteration < overallBestDistance) {
                overallBestDistance = bestDistanceInIteration;
                overallBestRoute = bestRouteInIteration;
            }
        }
        System.out.println("\nACO Distance: " + overallBestDistance);
        //overallBestRoute = gaInstance.getAllIntermediatePoints(overallBestRoute);
        System.out.println("ACO Route: " + overallBestRoute);
        ACORouteResult = new RouteResult(overallBestRoute, overallBestDistance,"ACO");
        return ACORouteResult;
    }

    private void initializePheromones() {
        int size = combinedPoints.size();
        pheromoneLevels = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pheromoneLevels[i][j] = 1.0; // Initialize pheromone levels
            }
        }
    }

    private void calculateDistances() {
        int size = combinedPoints.size();
        distances = new double[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Point pointA = combinedPoints.get(i);
                Point pointB = combinedPoints.get(j);
                double xA = pointA.getX();
                double yA = pointA.getY();
                double yB = pointB.getY();
                
                // Apply the movement rules based on the x coordinates of the points
                if (xA == 2.0 || xA == 3.0) {
                    // For x=2 and x=3, y can only move up (i.e., yB >= yA)
                    if (yB < yA) {
                        distances[i][j] = 1000; // Rule violation
                        continue; // Skip the distance calculation for this pair
                    }
                } else if (xA == 4.0 || xA == 5.0 || xA == 6.0) {
                    // For x=4, x=5, and x=6, y can only move down (i.e., yB <= yA)
                    if (yB > yA) {
                        distances[i][j] = 1000; // Rule violation
                        continue; // Skip the distance calculation for this pair
                    }
                }

                // If no rule is violated, calculate the segmented Manhattan distance
                distances[i][j] = GA.getSegmentedManhattanDistance(pointA, pointB);
            }
        }
    }

    private ArrayList<Point> buildAntRoute(Point startPoint) {
        ArrayList<Point> route = new ArrayList<>();
        route.add(startPoint);
        int startPointIndex = combinedPoints.indexOf(startPoint); // Get start point index
        boolean[] visited = new boolean[combinedPoints.size()];
        visited[startPointIndex] = true;
        while (route.size() < combinedPoints.size()+1) { // +1 for the return to startPoint
            Point currentPoint = route.get(route.size() - 1);
            Point nextPoint = selectNextPointExcludingStartAndEnd(currentPoint, visited);

            if (nextPoint == null) {
                break;
            }
            // Ensure nextPoint exists in combinedPoints
            int nextPointIndex = combinedPoints.indexOf(nextPoint);
            if (nextPointIndex == -1) {
                break;
            } else {
                visited[nextPointIndex] = true;
            }
            route.add(nextPoint);
        }
        route.add(startPoint); // Return to start point
        return route;
    }
    
    private void updatePheromones(ArrayList<Point> route) {
        double routeDistance = gaInstance.getRouteDistance(route);
        for (int i = 0; i < route.size() - 1; i++) {
            int fromIndex = combinedPoints.indexOf(route.get(i));
            int toIndex = combinedPoints.indexOf(route.get(i + 1));
            pheromoneLevels[fromIndex][toIndex] += 1.0 / routeDistance; // Deposit pheromone
        }
    }

    private void evaporatePheromones() {
        for (double[] pheromoneLevel : pheromoneLevels) {
            for (int j = 0; j < pheromoneLevel.length; j++) {
                pheromoneLevel[j] *= (1 - evaporationRate); // Evaporate pheromone
            }
        }
    }
    
    private Point selectNextPointExcludingStartAndEnd(Point currentPoint, boolean[] visited) {
        int currentIndex = combinedPoints.indexOf(currentPoint);
        double[] probabilities = new double[combinedPoints.size()];
        double sum = 0.0;
    
        // Calculate selection probabilities, but skip (1.0, 1.0) except for the last point
        for (int i = 0; i < combinedPoints.size(); i++) {
            Point nextPoint = combinedPoints.get(i);
            // Skip (1.0, 1.0) if it's not the last point
            if (!visited[i] && !(nextPoint.getX() == 1.0 && nextPoint.getY() == 1.0)) {
                double pheromoneLevel = pheromoneLevels[currentIndex][i];
                double heuristicValue = 1.0 / distances[currentIndex][i];
                probabilities[i] = Math.pow(pheromoneLevel, alpha) * Math.pow(heuristicValue, beta);
                sum += probabilities[i];
            }
        }
        
        if (sum == 0) {
            return null; // No valid next point found
        }
    
        // Select next point based on probabilities
        double randomValue = random.nextDouble() * sum;
        for (int i = 0; i < combinedPoints.size(); i++) {
            if (!visited[i] && !(combinedPoints.get(i).getX() == 1.0 && combinedPoints.get(i).getY() == 1.0)) {
                randomValue -= probabilities[i];
                if (randomValue <= 0) {
                    return combinedPoints.get(i);
                }
            }
        }
        return null; // No valid next point was selected
    }    
}
