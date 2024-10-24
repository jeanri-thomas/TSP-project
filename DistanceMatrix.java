//package main.java;
//
//public class DistanceMatrix {
//    static double[][] coordinates = {
//        {1, 1}, {1.25, 1}, {1.5, 1}, {1.75, 1}, {2, 1}, {2.25, 1}, {2.5, 1}, {2.75, 1}, {3, 1}, {3.25, 1}, {3.5, 1}, {3.75, 1}, {4, 1}, {4.25, 1}, {4.5, 1}, {4.75, 1}, {5, 1}, {5.25, 1}, {5.5, 1}, {5.75, 1}, {6, 1},
//        {1, 1.25}, {2, 1.25}, {3, 1.25}, {4, 1.25}, {5, 1.25}, {6, 1.25},
//        {1, 1.5}, {2, 1.5}, {3, 1.5}, {4, 1.5}, {5, 1.5}, {6, 1.5},
//        {1, 1.75}, {2, 1.75}, {3, 1.75}, {4, 1.75}, {5, 1.75}, {6, 1.75},
//        {1, 2}, {1.25, 2}, {1.5, 2}, {1.75, 2}, {2, 2}, {2.25, 2}, {2.5, 2}, {2.75, 2}, {3, 2}, {3.25, 2}, {3.5, 2}, {3.75, 2}, {4, 2}, {4.25, 2}, {4.5, 2}, {4.75, 2}, {5, 2}, {5.25, 2}, {5.5, 2}, {5.75, 2}, {6, 2},
//        {1, 2.25}, {2, 2.25}, {3, 2.25}, {4, 2.25}, {5, 2.25}, {6, 2.25},
//        {1, 2.5}, {2, 2.5}, {3, 2.5}, {4, 2.5}, {5, 2.5}, {6, 2.5},
//        {1, 2.75}, {2, 2.75}, {3, 2.75}, {4, 2.75}, {5, 2.75}, {6, 2.75},
//        {1, 3}, {1.25, 3}, {1.5, 3}, {1.75, 3}, {2, 3}, {2.25, 3}, {2.5, 3}, {2.75, 3}, {3, 3}, {3.25, 3}, {3.5, 3}, {3.75, 3}, {4, 3}, {4.25, 3}, {4.5, 3}, {4.75, 3}, {5, 3}, {5.25, 3}, {5.5, 3}, {5.75, 3}, {6, 3},
//        {1, 3.25}, {2, 3.25}, {3, 3.25}, {4, 3.25}, {5, 3.25}, {6, 3.25},
//        {1, 3.5}, {2, 3.5}, {3, 3.5}, {4, 3.5}, {5, 3.5}, {6, 3.5},
//        {1, 3.75}, {2, 3.75}, {3, 3.75}, {4, 3.75}, {5, 3.75}, {6, 3.75},
//        {1, 4}, {1.25, 4}, {1.5, 4}, {1.75, 4}, {2, 4}, {2.25, 4}, {2.5, 4}, {2.75, 4}, {3, 4}, {3.25, 4}, {3.5, 4}, {3.75, 4}, {4, 4}, {4.25, 4}, {4.5, 4}, {4.75, 4}, {5, 4}, {5.25, 4}, {5.5, 4}, {5.75, 4}, {6, 4},
//        {1, 4.25}, {2, 4.25}, {3, 4.25}, {4, 4.25}, {5, 4.25}, {6, 4.25},
//        {1, 4.5}, {2, 4.5}, {3, 4.5}, {4, 4.5}, {5, 4.5}, {6, 4.5},
//        {1, 4.75}, {2, 4.75}, {3, 4.75}, {4, 4.75}, {5, 4.75}, {6, 4.75},
//        {1, 5}, {1.25, 5}, {1.5, 5}, {1.75, 5}, {2, 5}, {2.25, 5}, {2.5, 5}, {2.75, 5}, {3, 5}, {3.25, 5}, {3.5, 5}, {3.75, 5}, {4, 5}, {4.25, 5}, {4.5, 5}, {4.75, 5}, {5, 5}, {5.25, 5}, {5.5, 5}, {5.75, 5},
//        {0.75, 1}
//    };
//    
//    public static double[][] distanceMatrix;
//
//    static {
//        calculateDistanceMatrix();
//    }
//
//    public static void calculateDistanceMatrix() {
//    	distanceMatrix = new double[coordinates.length][coordinates.length];
//        for (int i = 0; i < coordinates.length; i++) {
//            for (int j = 0; j < coordinates.length; j++) {
//                if (i == j) {
//                    distanceMatrix[i][j] = 0;
//                } else {
//                    distanceMatrix[i][j] = calculateManhattanDistance(coordinates[i], coordinates[j]);
//
//                    // Apply penalties
//                    if (applyPenalty(coordinates[i], coordinates[j])) {
//                        distanceMatrix[i][j] += 1000;
//                    }
//                }
//            }
//        }
//        // Print the distance matrix
//        for (int i = 0; i < distanceMatrix.length; i++) {
//            for (int j = 0; j < distanceMatrix[i].length; j++) {
//                System.out.printf("%.2f ", distanceMatrix[i][j]);
//            }
//            System.out.println();
//        }
//    }
//
//    private static double calculateManhattanDistance(double[] point1, double[] point2) {
//        return Math.abs(point1[0] - point2[0]) + Math.abs(point1[1] - point2[1]);
//    }
//
//    private static boolean applyPenalty(double[] point1, double[] point2) {
//        // Define the x and y coordinates of the points
//        double x1 = point1[0], y1 = point1[1];
//        double x2 = point2[0], y2 = point2[1];
//
//        // Define the penalty conditions for consecutive pairs
//        if ((x1 == 1.25 && x2 == 1.25 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 1.5 && x2 == 1.5 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 1.75 && x2 == 1.75 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 2.25 && x2 == 2.25 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 2.5 && x2 == 2.5 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 2.75 && x2 == 2.75 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 3.25 && x2 == 3.25 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 3.5 && x2 == 3.5 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 3.75 && x2 == 3.75 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 4.25 && x2 == 4.25 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 4.5 && x2 == 4.5 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 4.75 && x2 == 4.75 && Math.abs(y1 - y2) == 1 && y1 >= 1 && y1 <= 5) ||
//            (x1 == 1 && x2 == 1 && Math.abs(y1 - y2) == 1 && y2 == 6) ||
//            (x1 == 1.25 && x2 == 1.25 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 1.5 && x2 == 1.5 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 1.75 && x2 == 1.75 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 2.25 && x2 == 2.25 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 2.5 && x2 == 2.5 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 2.75 && x2 == 2.75 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 3.25 && x2 == 3.25 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 3.5 && x2 == 3.5 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 3.75 && x2 == 3.75 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 4.25 && x2 == 4.25 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 4.5 && x2 == 4.5 && y1 == 6 && y2 >= 1 && y2 <= 5) ||
//            (x1 == 4.75 && x2 == 4.75 && y1 == 6 && y2 >= 1 && y2 <= 5)) {
//            return true; // Apply penalty for these pairs
//        }
//
//        // Check for penalties based on x-axis conditions
//        if ((point1[0] == 3 && point1[1] < point2[1]) || (point2[0] == 3 && point2[1] < point1[1])) {
//            return true; // Down movement from x=3
//        }
//        if ((point1[0] == 2 && point1[1] > point2[1]) || (point2[0] == 2 && point2[1] > point1[1])) {
//            return true; // Up movement from x=2
//        }
//        if ((point1[0] == 4 && point1[1] < point2[1]) || (point2[0] == 4 && point2[1] < point1[1])) {
//            return true; // Up movement from x=4
//        }
//        if ((point1[0] == 5 && point1[1] < point2[1]) || (point2[0] == 5 && point2[1] < point1[1])) {
//            return true; // Up movement from x=5
//        }
//        if ((point1[0] == 6 && point1[1] < point2[1]) || (point2[0] == 6 && point2[1] < point1[1])) {
//            return true; // Up movement from x=6
//        }
//
//        return false;
//    }
//}
