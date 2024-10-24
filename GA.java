package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GA {
    public final ArrayList<Point> combinedPoints;
    public final ArrayList<Point> selectedPoints;
    public final Point startPoint = new Point(1, 1.0, 1.0);
    private final int populationSize = 50; //50
    private final int generations = 100; //100
    private final Random random = new Random();
    public RouteResult GARouteResult;

    public GA(ArrayList<Point> combinedPoints, ArrayList<Point> selectedPoints) { // Constructor accepting: selectedPoints, combinedPoints
        this.combinedPoints = combinedPoints;
        this.selectedPoints = selectedPoints;
    }

    public RouteResult runGA() {
        ArrayList<Point> NNRoute = buildNearestNeighborRoute(startPoint, combinedPoints);
        //double NNDistance = getRouteDistance(NNRoute);

        ArrayList<Point> NIRoute = buildNearestInsertionRoute(startPoint, combinedPoints);
        //double NIDistance = getRouteDistance(NIRoute);

        ArrayList<Point> FIRoute = buildFarthestInsertionRoute(startPoint, combinedPoints);
        //double FIDistance = getRouteDistance(FIRoute);
        
        ArrayList<ArrayList<Point>> population = initializePopulation(NNRoute, NIRoute, FIRoute);
        
        for (int generation = 0; generation < generations; generation++) { // Run the GA
            ArrayList<ArrayList<Point>> newPopulation = new ArrayList<>();
            while (newPopulation.size() < populationSize) {
                ArrayList<Point> parent1 = tournamentSelection(population);
                ArrayList<Point> parent2 = tournamentSelection(population);
                ArrayList<Point> child = sequentialConstructiveCrossover(startPoint, parent1, parent2);
                ArrayList<Point> mutatedChild = mutate(child);
                newPopulation.add(mutatedChild);
            }
            population = newPopulation;
            //System.out.print("population: "+population);
        }
        //ArrayList<Point> gaRoute = getAllIntermediatePoints(getFittest(population));
        ArrayList<Point> gaRoute = getFittest(population);
        double gaDistance = getRouteDistance(gaRoute);
        
        System.out.println("\n\nGA Distance: " + gaDistance);
        System.out.println("GA Route: " + gaRoute + "\n");
                
        GARouteResult = new RouteResult(gaRoute, gaDistance, "GA");
        return GARouteResult;
    }

    public ArrayList<Point> buildNearestNeighborRoute(Point startPoint, ArrayList<Point> points) {
        ArrayList<Point> route = new ArrayList<>();
        Set<Point> unvisited = new HashSet<>(points);
        Point currentPoint = startPoint;
        route.add(currentPoint);
        unvisited.remove(currentPoint);
        while (!unvisited.isEmpty()) {
            Point nearestPoint = null;
            double minDistance = Double.MAX_VALUE;
            for (Point point : unvisited) {
                double distance = getSegmentedManhattanDistance(currentPoint, point);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPoint = point;
                }
            }
            if (nearestPoint != null) {
                route.add(nearestPoint);
                currentPoint = nearestPoint;
                unvisited.remove(nearestPoint);
            }
        }
        route.add(startPoint);
        return route;
    }

    public ArrayList<Point> buildNearestInsertionRoute(Point startPoint, ArrayList<Point> points) {
        ArrayList<Point> route = new ArrayList<>();
        Set<Point> unvisited = new HashSet<>(points);
        // Initialize route with start point and nearest point to it
        Point currentPoint = startPoint;
        route.add(currentPoint);
        unvisited.remove(currentPoint);
        // Find the nearest point to the start point
        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (Point point : unvisited) {
            double distance = getSegmentedManhattanDistance(currentPoint, point);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = point;
            }
        }
        // Add the nearest point to the route and remove it from unvisited
        if (nearestPoint != null) {
            route.add(nearestPoint);
            unvisited.remove(nearestPoint);
        }
        // Insert the remaining points
        while (!unvisited.isEmpty()) {
            Point nextPoint = null;
            minDistance = Double.MAX_VALUE;
            // Find the nearest unvisited point to any point in the current route
            for (Point point : unvisited) {
                for (int i = 0; i < route.size(); i++) {
                    double distance = getSegmentedManhattanDistance(route.get(i), point);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nextPoint = point;
                    }
                }
            }
            // Insert the nearest unvisited point at the position where it increases the route length the least
            if (nextPoint != null) {
                int bestInsertPos = 0;
                double bestInsertCost = Double.MAX_VALUE;
                for (int i = 0; i < route.size() - 1; i++) {
                    double costBefore = getSegmentedManhattanDistance(route.get(i), route.get(i + 1));
                    double costAfter = getSegmentedManhattanDistance(route.get(i), nextPoint) + getSegmentedManhattanDistance(nextPoint, route.get(i + 1));
                    double insertCost = costAfter - costBefore;
                    if (insertCost < bestInsertCost) {
                        bestInsertCost = insertCost;
                        bestInsertPos = i + 1;
                    }
                }
                // Insert the nextPoint at the best position
                route.add(bestInsertPos, nextPoint);
                unvisited.remove(nextPoint);
            }
        }
        route.add(startPoint);
        return route;
    }
    
    public ArrayList<Point> buildFarthestInsertionRoute(Point startPoint, ArrayList<Point> points) {
        ArrayList<Point> route = new ArrayList<>();
        Set<Point> unvisited = new HashSet<>(points);
        unvisited.remove(startPoint);
        // Step 1: Select an initial route with two far-apart points
        Point farthestPoint = null;
        double maxDistance = Double.MIN_VALUE;
        // Find the farthest point from the startPoint
        for (Point point : unvisited) {
            double distance = getSegmentedManhattanDistance(startPoint, point);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestPoint = point;
            }
        }
        // Initialize the route with startPoint and farthestPoint
        if (farthestPoint != null) {
            route.add(startPoint);
            route.add(farthestPoint);
            route.add(startPoint);  // Complete the loop
            unvisited.remove(farthestPoint);
        }
        // Step 2: Insert the farthest unvisited point at the best position in the route
        while (!unvisited.isEmpty()) {
            Point farthestUnvisitedPoint = null;
            maxDistance = Double.MIN_VALUE;
            // Find the farthest unvisited point from any point in the current route
            for (Point unvisitedPoint : unvisited) {
                for (Point routePoint : route) {
                    double distance = getSegmentedManhattanDistance(unvisitedPoint, routePoint);
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        farthestUnvisitedPoint = unvisitedPoint;
                    }
                }
            }
            // Insert the farthest point into the route at the position where it increases the distance the least
            if (farthestUnvisitedPoint != null) {
                int bestInsertionIndex = -1;
                double minInsertionCost = Double.MAX_VALUE;
                for (int i = 0; i < route.size() - 1; i++) {
                    Point current = route.get(i);
                    Point next = route.get(i + 1);
                    double currentDistance = getSegmentedManhattanDistance(current, next);
                    double newDistance = getSegmentedManhattanDistance(current, farthestUnvisitedPoint) + getSegmentedManhattanDistance(farthestUnvisitedPoint, next);
                    double insertionCost = newDistance - currentDistance;
                    if (insertionCost < minInsertionCost) {
                        minInsertionCost = insertionCost;
                        bestInsertionIndex = i;
                    }
                }
                
                if (bestInsertionIndex != -1) {
                    route.add(bestInsertionIndex + 1, farthestUnvisitedPoint);
                }
                unvisited.remove(farthestUnvisitedPoint);
            }
        }
        return route;
    }

    public ArrayList<ArrayList<Point>> initializePopulation(ArrayList<Point> NNRoute, ArrayList<Point> NIRoute, ArrayList<Point> FIRoute) {
        ArrayList<ArrayList<Point>> population = new ArrayList<>();
        population.add(NNRoute);
        population.add(NIRoute);
        population.add(FIRoute);
        while (population.size() < populationSize) {
            ArrayList<Point> mutateNN = mutate(new ArrayList<>(NNRoute));
            ArrayList<Point> mutateNI = mutate(new ArrayList<>(NIRoute));
            ArrayList<Point> mutateFI = mutate(new ArrayList<>(FIRoute));
            population.add(mutateNN);
            if (population.size() < populationSize) population.add(mutateNI);
            if (population.size() < populationSize) population.add(mutateFI);
        }
        return population;
    }

    public ArrayList<Point> tournamentSelection(ArrayList<ArrayList<Point>> population) {
        int tournamentSize = 5;
        ArrayList<ArrayList<Point>> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        ArrayList<Point> fittest = getFittest(tournament);
        return fittest;
    }

    public ArrayList<Point> sequentialConstructiveCrossover(Point startPoint, ArrayList<Point> parent1, ArrayList<Point> parent2) {
        // Check for null or empty parent routes
        if (parent1 == null || parent2 == null || parent1.isEmpty() || parent2.isEmpty()) {
            throw new IllegalArgumentException("Parent routes cannot be null or empty.");
        }
        // Ensure both parents have the same size
        if (parent1.size() != parent2.size()) {
            throw new IllegalArgumentException("Parent routes must have the same size.");
        }
        ArrayList<Point> offspring = new ArrayList<>();
        Set<Point> visited = new HashSet<>();
        // Start from the startPoint
        offspring.add(startPoint);
        visited.add(startPoint);
        Point current = startPoint;
        // Iterate until the offspring route is complete
        while (offspring.size() < parent1.size()) {
            Point next1 = getNextUnvisitedPoint(parent1, current, visited);
            Point next2 = getNextUnvisitedPoint(parent2, current, visited);
            // Choose the next point based on the greedy heuristic (shorter distance)
            Point next;
            if (next1 == null && next2 == null) {
                //System.out.println("Both next1 and next2 are null, selecting a random unvisited point.");
                next = getRandomUnvisitedPoint(parent1, visited);  // Fallback to random unvisited point
            } else if (next1 == null) {
                next = next2;
            } else if (next2 == null) {
                next = next1;
            } else {
                // Both next1 and next2 are non-null, compare the distances
                if (getSegmentedManhattanDistance(current, next1) < getSegmentedManhattanDistance(current, next2)) {
                    next = next1;
                } else {
                    next = next2;
                }
            }
            // Add the chosen point to the offspring route if it's valid
            if (next != null) {
                offspring.add(next);
                visited.add(next);
                current = next;
            } else {
                //System.out.println("Error: No valid next point found, breaking the loop.");
                break;
            }
        }
        // Ensure the route ends at the starting point (1, 1)
        if (!offspring.isEmpty() && !offspring.get(offspring.size() - 1).equals(startPoint)) {
            offspring.add(startPoint);
        }
        return offspring;
    }
    
    public Point getNextUnvisitedPoint(ArrayList<Point> parent, Point current, Set<Point> visited) {
        int currentIndex = parent.indexOf(current);
        // Check if current point exists in parent list
        if (currentIndex == -1) {
            //System.out.println("Error: Current point " + current + " not found in parent route.");
            return null;
        }
        // Find the next unvisited point in the parent's list
        for (int i = currentIndex + 1; i < parent.size(); i++) {
            Point candidate = parent.get(i);
            if (!visited.contains(candidate)) {
                return candidate;
            }
        }
        // If no further unvisited point found, loop back to the start
        for (int i = 0; i < currentIndex; i++) {
            Point candidate = parent.get(i);
            if (!visited.contains(candidate)) {
                return candidate;
            }
        }
        //System.out.println("Warning: No unvisited points found after current " + current);
        return null;  // Return null if no unvisited point is found
    }
    
    public Point getRandomUnvisitedPoint(ArrayList<Point> parent, Set<Point> visited) {
        for (Point candidate : parent) {
            if (!visited.contains(candidate)) {
                return candidate;
            }
        }
        //System.out.println("Error: No random unvisited points available.");
        return null;  // Return null if no unvisited point is found
    }
    
    public  ArrayList<Point> mutate(ArrayList<Point> individual) {
        int index1 = random.nextInt(individual.size()-2)+1;
        int index2 = random.nextInt(individual.size()-2)+1;
        Point temp = individual.get(index1);
        individual.set(index1, individual.get(index2));
        individual.set(index2, temp);
        return individual;
    }

    public ArrayList<Point> getFittest(ArrayList<ArrayList<Point>> population) {
        ArrayList<Point> fittest = population.get(0);
        double minDistance = getRouteDistance(fittest);
        for (ArrayList<Point> individual : population) {
            double currentDistance = getRouteDistance(individual);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                fittest = individual;
            }
        }
        return fittest;
    }

    public double getRouteDistance(ArrayList<Point> fittestSolution) {
            double distance = 0.0;
            for (int i = 0; i < fittestSolution.size() - 1; i++) {
                distance += getSegmentedManhattanDistance(fittestSolution.get(i), fittestSolution.get(i + 1));
            }
            return distance;
    }

    public static double getSegmentedManhattanDistance(Point p1, Point p2) {
        double xA = p1.getX();
        double yA = p1.getY();
        double xB = p2.getX();
        double yB = p2.getY();

        // Apply the movement rules based on the x coordinates of the points
        if (xA == 2.0 || xA == 3.0) {
            // For x=2 and x=3, y can only move up (i.e., yB >= yA)
            if (yB < yA) {
                return 1000; // Rule violation
            }
        } else if (xA == 4.0 || xA == 5.0 || xA == 6.0) {
            // For x=4, x=5, and x=6, y can only move down (i.e., yB <= yA)
            if (yB > yA) {
                return 1000; // Rule violation
            }
        }

        // Default Manhattan distance calculation
        return Math.abs(xA - xB) + Math.abs(yA - yB);
    }


    public RouteResult getBestRoute(ArrayList<Point> NNRoute, double NNDistance, ArrayList<Point> NIRoute, double NIDistance, 
                                            ArrayList<Point> FIRoute, double FIDistance, ArrayList<Point> gaRoute, double gaDistance) {
        double minDistance = NNDistance;
        ArrayList<Point> bestRoute = NNRoute;
        String bestAlgorithm = "NN";
        if (NIDistance < minDistance) {
            minDistance = NIDistance;
            bestRoute = NIRoute;
            bestAlgorithm = "NI";
        }
        if (FIDistance < minDistance) {
            minDistance = FIDistance;
            bestRoute = FIRoute;
            bestAlgorithm = "FI";
        }
        if (gaDistance < minDistance) {
            minDistance = gaDistance;
            bestRoute = gaRoute;
            bestAlgorithm = "GA";
        }
        bestRoute = getAllIntermediatePoints(bestRoute);
        return new RouteResult(bestRoute, minDistance, bestAlgorithm);
    }
    
    public ArrayList<Point> getAllIntermediatePoints(ArrayList<Point> route) {
        ArrayList<Point> allIntermediatePoints = new ArrayList<>();
        for (int i = 0; i < route.size() - 1; i++) {
            Point p1 = route.get(i);
            Point p2 = route.get(i + 1);
            List<Point> intermediatePoints = getIntermediatePoints(p1, p2);
            allIntermediatePoints.addAll(intermediatePoints);
        }
        allIntermediatePoints.add(0, new Point(1.0, 1.0)); // Ensure (1.0, 1.0) format
        return allIntermediatePoints;
    }

    public static ArrayList<Point> getIntermediatePoints(Point p1, Point p2) {
        ArrayList<Point> intermediatePoints = new ArrayList<>();
        double xDistance = getXdistance(p1, p2);
        double yDistance = getYdistance(p1, p2);
        if (xDistance == 0.0 && yDistance == 0.0) return intermediatePoints; 
        int yNum = (int) (yDistance / 0.25);
        int xNum = (int) (xDistance / 0.25); 
        double xStep = 0.25;
        double yStep = 0.25;
        for (int i = 1; i <= Math.abs(xNum); i++) {
            double newX = p1.getX() + i * xStep * Math.abs(xNum) / xNum;
            double newY = p1.getY(); // Keep p1's y to ensure one coordinate has .0
            if (isValidPoint(newX, newY)) {
                intermediatePoints.add(new Point(newX, newY));
            }
        }
        for (int i = 1; i <= Math.abs(yNum); i++) {
            double newX = p2.getX(); // Keep p2's x to ensure one coordinate has .0
            double newY = p1.getY() + i * yStep * Math.abs(yNum) / yNum;
            if (isValidPoint(newX, newY)) {
                intermediatePoints.add(new Point(newX, newY));
            }
        }
        return intermediatePoints;
    }

    private static boolean isValidPoint(double x, double y) {
        // Ensure that only one of x or y is a decimal value (not both)
        boolean xIsInt = (x % 1.0 == 0.0); // x is an integer or presented as one (like 2.0)
        boolean yIsInt = (y % 1.0 == 0.0); // y is an integer or presented as one (like 2.0)
        return (xIsInt && !yIsInt) || (!xIsInt && yIsInt) || (xIsInt && yIsInt ==true);
    }
    public static double getXdistance(Point p1, Point p2) {
        return (p2.getX() - p1.getX());
    }
    public static double getYdistance(Point p1, Point p2) {
        return (p2.getY() - p1.getY());
    }
}