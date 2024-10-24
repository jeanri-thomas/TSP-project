package main.java;

import java.util.ArrayList;
import java.util.Random;

public class PSO {
    private final ArrayList<Point> combinedPoints;
    private final Point startPoint = new Point(1.0, 1.0);
    private final int numBirds = 10;
    private final int iterations = 100;
    private final Random random = new Random();
    private ArrayList<Point>[] birdPositions;
    public ArrayList<Point> globalBestRoute;
    public double globalBestDistance = Double.MAX_VALUE;
    private final double inertiaWeight = 0.5; // Controls exploration
    private final double cognitiveComponent = 1.5; // Weight for personal best
    private final double socialComponent = 1.5; // Weight for global best
    private final GA gaInstance;
    public RouteResult PSORouteResult;

    public PSO(ArrayList<Point> combinedPoints, ArrayList<Point> selectedPoints) { 
        this.combinedPoints = combinedPoints;
        this.gaInstance = new GA(combinedPoints, selectedPoints);
        combinedPoints.add(0, startPoint);
        initializeBirdPositions();
    }

    public RouteResult runPSO() {
        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int bird = 0; bird < numBirds; bird++) {
                ArrayList<Point> currentRoute = birdPositions[bird];
                ArrayList<Point> newRoute = updateBirdPosition(currentRoute);
                double currentDistance = gaInstance.getRouteDistance(currentRoute);
                double newDistance = gaInstance.getRouteDistance(newRoute);
                // Update bird's personal best and check if it's also the global best
                if (newDistance < currentDistance) {
                    birdPositions[bird] = newRoute;
                }
                if (newDistance < globalBestDistance) {
                    globalBestRoute = newRoute;
                    globalBestDistance = newDistance;
                }
            }
            if (iteration % 100 == 0) {
                //System.out.println("Iteration: " + iteration + " Global Best Distance: " + globalBestDistance);
            }
        }
        System.out.println("\nPSO Distance: " + globalBestDistance);
        //globalBestRoute = gaInstance.getAllIntermediatePoints(globalBestRoute);
        System.out.println("PSO Route: " + globalBestRoute);
        PSORouteResult = new RouteResult(globalBestRoute, globalBestDistance, "PSO");
        return  PSORouteResult;
    }

    @SuppressWarnings("unchecked")
    private void initializeBirdPositions() {
        birdPositions = new ArrayList[numBirds];
        ArrayList<Point> NNRoute = gaInstance.buildNearestNeighborRoute(startPoint, combinedPoints);
        ArrayList<Point> NIRoute = gaInstance.buildNearestInsertionRoute(startPoint, combinedPoints);
        ArrayList<Point> FIRoute = gaInstance.buildFarthestInsertionRoute(startPoint, combinedPoints);
        ArrayList<Point> SCCRoute = gaInstance.sequentialConstructiveCrossover(startPoint, FIRoute, NNRoute);
        for (int i = 0; i < numBirds / 4; i++) {
            birdPositions[i] = new ArrayList<>(NNRoute);
        }
        for (int i = numBirds / 4; i < numBirds; i++) {
            birdPositions[i] = new ArrayList<>(NIRoute);
        }
        for (int i = numBirds / 4; i < numBirds; i++) {
            birdPositions[i] = new ArrayList<>(FIRoute);
        }
        for (int i = numBirds / 4; i < numBirds; i++) {
            birdPositions[i] = new ArrayList<>(SCCRoute);
        }
        globalBestRoute = birdPositions[0];
        globalBestDistance = gaInstance.getRouteDistance(globalBestRoute);
    }

    private ArrayList<Point> updateBirdPosition(ArrayList<Point> currentRoute) {
        ArrayList<Point> newRoute = new ArrayList<>(currentRoute);

        // Cognitive component: bird's personal best route
        for (int i = 1; i < newRoute.size() - 1; i++) { // Skip start and end points
            if (random.nextDouble() < cognitiveComponent) {
                int swapIndex = random.nextInt(newRoute.size() - 2) + 1; // Random swap
                java.util.Collections.swap(newRoute, i, swapIndex);
            }
        }

        // Social component: influenced by global best
        if (random.nextDouble() < socialComponent) {
            int globalInfluenceIndex = random.nextInt(globalBestRoute.size() - 2) + 1;
            Point globalInfluencePoint = globalBestRoute.get(globalInfluenceIndex);
            int currentInfluenceIndex = newRoute.indexOf(globalInfluencePoint);
            if (currentInfluenceIndex != -1 && random.nextDouble() < inertiaWeight) {
                int swapIndex = random.nextInt(newRoute.size() - 2) + 1;
                java.util.Collections.swap(newRoute, currentInfluenceIndex, swapIndex);
            }
        }
        newRoute = applyTwoOpt(newRoute);

        return newRoute;
    }

    private ArrayList<Point> applyTwoOpt(ArrayList<Point> route) {
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 1; i < route.size() - 2; i++) {
                for (int j = i + 1; j < route.size() - 1; j++) {
                    double oldDistance = gaInstance.getRouteDistance(route);
                    ArrayList<Point> newRoute = twoOptSwap(route, i, j);
                    double newDistance = gaInstance.getRouteDistance(newRoute);
                    if (newDistance < oldDistance) {
                        route = newRoute;
                        improved = true;
                    }
                }
            }
        }
        return route;
    }

    private ArrayList<Point> twoOptSwap(ArrayList<Point> route, int i, int j) {
        if (i == 0 || j == route.size() - 1) {
            return route; // Do nothing if the swap involves the start or end point
        }
        ArrayList<Point> newRoute = new ArrayList<>(route.subList(0, i));
        ArrayList<Point> reversed = new ArrayList<>(route.subList(i, j + 1));
        java.util.Collections.reverse(reversed);
        newRoute.addAll(reversed);
        newRoute.addAll(route.subList(j + 1, route.size()));
        return newRoute;
    }
}
