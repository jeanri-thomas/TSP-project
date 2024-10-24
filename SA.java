package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SA {
    private static final Random random = new Random();
    public ArrayList<Point> combinedPoints;
    public ArrayList<Point> selectedPoints;
    public double initialTemperature = 100000;
    public double coolingRate = 0.995;
    private final int maxIterations = 5000;
    private final GA gaInstance;
    public Point startPoint = new Point(1, 1);
    public ArrayList<Point> initialRoute;
    private ArrayList<Point> currentSolution;
    public ArrayList<Point> bestSolution;
    private double currentDistance;
    public double bestDistance;
    public RouteResult SARouteResult;

    public SA(ArrayList<Point> combinedPoints, ArrayList<Point> selectedPoints) {
        this.combinedPoints = combinedPoints;
        this.selectedPoints = selectedPoints;
        this.gaInstance = new GA(combinedPoints, selectedPoints);

        // Initialize routes and distances here after gaInstance is ready
        initialRoute = gaInstance.buildNearestNeighborRoute(startPoint, combinedPoints);
        currentSolution = new ArrayList<>(initialRoute);
        bestSolution = new ArrayList<>(currentSolution);
        currentDistance = gaInstance.getRouteDistance(currentSolution);
        bestDistance = currentDistance;
    }

    public RouteResult runSA() {
        double temperature = initialTemperature;

        for (int i = 0; i < maxIterations; i++) {
            ArrayList<Point> newSolution = new ArrayList<>(currentSolution);
            ArrayList<Point> route1 = twoOptSwap(newSolution);
            ArrayList<Point> route2 = swapTwoCities(newSolution);
            newSolution = gaInstance.sequentialConstructiveCrossover(startPoint, route1, route2);
            double newDistance = gaInstance.getRouteDistance(newSolution);

            if (acceptanceProbability(currentDistance, newDistance, temperature) > random.nextDouble()) {
                currentSolution = new ArrayList<>(newSolution);
                currentDistance = newDistance;
            }
            if (currentDistance < bestDistance) {
                bestSolution = new ArrayList<>(currentSolution);
                bestDistance = currentDistance;
            }
            temperature *= coolingRate;
        }
        System.out.println("SA Distance: " + bestDistance);
        //bestSolution = gaInstance.getAllIntermediatePoints(bestSolution);
        System.out.println("SA Route: " + bestSolution);
        SARouteResult = new RouteResult(bestSolution, bestDistance, "SA");
        return SARouteResult;
    }

    public ArrayList<Point> swapTwoCities(ArrayList<Point> tour) {
        if (tour.size() < 2) {
            return tour;
        }
        int index1 = random.nextInt(tour.size());
        int index2 = random.nextInt(tour.size());
        while (index1 == index2) {
            index2 = random.nextInt(tour.size());
        }
        Collections.swap(tour, index1, index2);
        return tour;
    }

    public ArrayList<Point> twoOptSwap(ArrayList<Point> route) {
        int size = route.size();
        int i = random.nextInt(size);
        int j = random.nextInt(size);
        while (i == j) {
            j = random.nextInt(size);
        }
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        ArrayList<Point> newRoute = new ArrayList<>(route.subList(0, i));
        ArrayList<Point> reversedSegment = new ArrayList<>(route.subList(i, j + 1));
        Collections.reverse(reversedSegment);
        newRoute.addAll(reversedSegment);
        newRoute.addAll(route.subList(j + 1, size));
        return newRoute;
    }

    private static double acceptanceProbability(double currentDistance, double newDistance, double temperature) {
        if (newDistance < currentDistance) {
            return 1.0;
        }
        return Math.exp((currentDistance - newDistance) / temperature);
    }
}
