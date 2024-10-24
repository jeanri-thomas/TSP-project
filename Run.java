package main.java;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Run {
    // File path for the Excel sheet
	private static final String FILE_PATH = "tsp_results.xlsx";
	
    public static void main(String[] args) {
        double[][] coordinates = {
            {1, 1}, {1.25, 1}, {1.5, 1}, {1.75, 1}, {2, 1}, {2.25, 1}, {2.5, 1}, {2.75, 1}, {3, 1}, {3.25, 1}, {3.5, 1}, {3.75, 1}, {4, 1}, {4.25, 1}, {4.5, 1}, {4.75, 1}, {5, 1}, {5.25, 1}, {5.5, 1}, {5.75, 1}, {6, 1},
            {1, 1.25}, {2, 1.25}, {3, 1.25}, {4, 1.25}, {5, 1.25}, {6, 1.25},
            {1, 1.5}, {2, 1.5}, {3, 1.5}, {4, 1.5}, {5, 1.5}, {6, 1.5},
            {1, 1.75}, {2, 1.75}, {3, 1.75}, {4, 1.75}, {5, 1.75}, {6, 1.75},
            {1, 2}, {1.25, 2}, {1.5, 2}, {1.75, 2}, {2, 2}, {2.25, 2}, {2.5, 2}, {2.75, 2}, {3, 2}, {3.25, 2}, {3.5, 2}, {3.75, 2}, {4, 2}, {4.25, 2}, {4.5, 2}, {4.75, 2}, {5, 2}, {5.25, 2}, {5.5, 2}, {5.75, 2}, {6, 2},
            {1, 2.25}, {2, 2.25}, {3, 2.25}, {4, 2.25}, {5, 2.25}, {6, 2.25},
            {1, 2.5}, {2, 2.5}, {3, 2.5}, {4, 2.5}, {5, 2.5}, {6, 2.5},
            {1, 2.75}, {2, 2.75}, {3, 2.75}, {4, 2.75}, {5, 2.75}, {6, 2.75},
            {1, 3}, {1.25, 3}, {1.5, 3}, {1.75, 3}, {2, 3}, {2.25, 3}, {2.5, 3}, {2.75, 3}, {3, 3}, {3.25, 3}, {3.5, 3}, {3.75, 3}, {4, 3}, {4.25, 3}, {4.5, 3}, {4.75, 3}, {5, 3}, {5.25, 3}, {5.5, 3}, {5.75, 3}, {6, 3},
            {1, 3.25}, {2, 3.25}, {3, 3.25}, {4, 3.25}, {5, 3.25}, {6, 3.25},
            {1, 3.5}, {2, 3.5}, {3, 3.5}, {4, 3.5}, {5, 3.5}, {6, 3.5},
            {1, 3.75}, {2, 3.75}, {3, 3.75}, {4, 3.75}, {5, 3.75}, {6, 3.75},
            {1, 4}, {1.25, 4}, {1.5, 4}, {1.75, 4}, {2, 4}, {2.25, 4}, {2.5, 4}, {2.75, 4}, {3, 4}, {3.25, 4}, {3.5, 4}, {3.75, 4}, {4, 4}, {4.25, 4}, {4.5, 4}, {4.75, 4}, {5, 4}, {5.25, 4}, {5.5, 4}, {5.75, 4}, {6, 4},
            {1, 4.25}, {2, 4.25}, {3, 4.25}, {4, 4.25}, {5, 4.25}, {6, 4.25},
            {1, 4.5}, {2, 4.5}, {3, 4.5}, {4, 4.5}, {5, 4.5}, {6, 4.5},
            {1, 4.75}, {2, 4.75}, {3, 4.75}, {4, 4.75}, {5, 4.75}, {6, 4.75},
            {1, 5}, {1.25, 5}, {1.5, 5}, {1.75, 5}, {2, 5}, {2.25, 5}, {2.5, 5}, {2.75, 5}, {3, 5}, {3.25, 5}, {3.5, 5}, {3.75, 5}, {4, 5}, {4.25, 5}, {4.5, 5}, {4.75, 5}, {5, 5}, {5.25, 5}, {5.5, 5}, {5.75, 5},
        };
        
        // Initialize Excel file
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("tsp_results");
            // Create the header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Route Number");
            headerRow.createCell(1).setCellValue("Selected Points");
            headerRow.createCell(2).setCellValue("Selected Points IDs");
            headerRow.createCell(3).setCellValue("Best Route");
            headerRow.createCell(4).setCellValue("Best Route IDs");
            headerRow.createCell(5).setCellValue("Best Distance");
            headerRow.createCell(6).setCellValue("Best Algorithm");
            headerRow.createCell(7).setCellValue("2nd Distance");
            headerRow.createCell(8).setCellValue("2nd Algorithm");
            headerRow.createCell(9).setCellValue("3rd Distance");
            headerRow.createCell(10).setCellValue("3rd Algorithm");
            headerRow.createCell(11).setCellValue("4th Distance");
            headerRow.createCell(12).setCellValue("4th Algorithm");
            headerRow.createCell(13).setCellValue("5th Distance");
            headerRow.createCell(14).setCellValue("5th Algorithm");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Do you want to randomly select visiting points or choose pre-defined visiting points?  \nType R for random or P for pre-defined: ");
            String input = scanner.nextLine().trim().toUpperCase();
            if ("R".equals(input)) {
                System.out.println("You selected random visiting points.");
                for (int i = 6; i < 10; i++) { // Run through visiting points - i may be >5 and <16
                    runRIterations(i, coordinates, workbook, sheet);
                }
                
            } else if ("P".equals(input)) {
                System.out.println("You selected pre-defined visiting points.");
             // Create a list of available sets (1 to 5)
                List<Integer> availableSets = new ArrayList<>();
                for (int j = 1; j <= 5; j++) {
                    availableSets.add(j);
                }
                Random random = new Random();
                for (int i = 6; i < 10; i++) { // i must be >5 and <10 for pre-defined routes 
                	if (availableSets.isEmpty()) {
                        System.out.println("All sets have been chosen.");
                        break;
                    }
                	int iteration = i-5;
                	int selectedIndex = random.nextInt(availableSets.size());
                    int selectedSet = availableSets.get(selectedIndex);
                    availableSets.remove(selectedIndex);
                    runPIterations(iteration, selectedSet, coordinates, workbook, sheet);
                }
            } else {
                System.out.println("Invalid input. Please type R or P.");
                
            }
            scanner.close(); 
            
         // Write workbook to file
            try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                workbook.write(fileOut);
                System.out.println("\nResults exported to Excel successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void runRIterations(int numberOfVisitingPoints, double[][] coordinates, Workbook workbook, Sheet sheet) {
        for (int i = 1; i < 6; i++) {
            System.out.print("\n---------------------------------------------------------------------------------");
            String routeNumberString = numberOfVisitingPoints + "." + i;
            double routeNumber = Double.parseDouble(routeNumberString);
            System.out.print("\nIteration: " +i +"\nNumber of Visiting Points: "+numberOfVisitingPoints);
            ArrayList<Point> selectedPoints = buildRSelectedPoints(numberOfVisitingPoints, coordinates);
            System.out.print("---------------------------------------------------------------------------------");
            ArrayList<Point> combinedPoints = buildCombinedPoints(selectedPoints);
            runRAlgorithms(coordinates, combinedPoints, selectedPoints, workbook, sheet, numberOfVisitingPoints, routeNumber);
        }
    }
    
    public static void runPIterations(int iteration, int selectedSet, double[][] coordinates, Workbook workbook, Sheet sheet) {
        for (int numberOfVisitingPoints = 6; numberOfVisitingPoints < 10; numberOfVisitingPoints++) {
            System.out.print("---------------------------------------------------------------------------------");
            String routeNumberString = numberOfVisitingPoints + "." + iteration;
            double routeNumber = Double.parseDouble(routeNumberString);
            System.out.print("\nIteration: "+iteration + "\nselectedSet: "+selectedSet +"\nNumber of Visiting Points: "+numberOfVisitingPoints);
            ArrayList<Point> selectedPoints = buildPSelectedPoints(numberOfVisitingPoints, selectedSet, coordinates);
            System.out.print("---------------------------------------------------------------------------------");
            ArrayList<Point> combinedPoints = buildCombinedPoints(selectedPoints);
            runPAlgorithms(coordinates, combinedPoints, selectedPoints, workbook, sheet, numberOfVisitingPoints, routeNumber);
        }
    }
    
    public static void runRAlgorithms(double[][] coordinates, ArrayList<Point> combinedPoints, ArrayList<Point> selectedPoints, Workbook workbook, Sheet sheet, int numberOfVisitingPoints, double routeNumber) {
        GA ga = new GA(combinedPoints, selectedPoints);
        RouteResult gaBestRoute = ga.runGA();
        SA sa = new SA(combinedPoints, selectedPoints);
        RouteResult saBestRoute = sa.runSA();
        ACO aco = new ACO(combinedPoints, selectedPoints);
        RouteResult acoBestRoute = aco.runACO();
        TS ts = new TS(combinedPoints, selectedPoints);
        RouteResult tsBestRoute = ts.runTS();
        PSO pso = new PSO(combinedPoints, selectedPoints);
        RouteResult psoBestRoute = pso.runPSO();
        List<RouteResult> allResults = new ArrayList<>();
        allResults.add(gaBestRoute);
        allResults.add(saBestRoute);
        allResults.add(acoBestRoute);
        allResults.add(tsBestRoute);
        allResults.add(psoBestRoute);
        // Sort by distance (assuming smaller distance is better)
        allResults.sort((r1, r2) -> Double.compare(r1.getDistance(), r2.getDistance()));
        // Get the best, 2nd, 3rd, 4th, and worst
        RouteResult bestRoute = allResults.get(0);   // Best
        RouteResult secondBestRoute = allResults.get(1);   // 2nd Best
        RouteResult thirdBestRoute = allResults.get(2);   // 3rd Best
        RouteResult fourthBestRoute = allResults.get(3);   // 4th Best
        RouteResult worstRoute = allResults.get(allResults.size() - 1);   // Worst
        ArrayList<Point> allIds = new ArrayList<>();
        int id = 1;
        for (double[] coordinate : coordinates) {
        allIds.add(new Point(id++, coordinate[0], coordinate[1]));
        }
     // Add the second, third, fourth, and worst algorithms
        ArrayList<Point> gaRoute = bestRoute.getRoute();
        addToExcel(allIds, workbook, sheet, numberOfVisitingPoints, selectedPoints, gaRoute, bestRoute.getDistance(), bestRoute.getAlgorithm(), secondBestRoute.getDistance(), secondBestRoute.getAlgorithm(), thirdBestRoute.getDistance(), thirdBestRoute.getAlgorithm(), fourthBestRoute.getDistance(), fourthBestRoute.getAlgorithm(), worstRoute.getDistance(), worstRoute.getAlgorithm());
    }
    
    public static void runPAlgorithms(double[][] coordinates, ArrayList<Point> combinedPoints, ArrayList<Point> selectedPoints, Workbook workbook, Sheet sheet, int numberOfVisitingPoints, double routeNumber) {
        GA ga = new GA(combinedPoints, selectedPoints);
        RouteResult gaBestRoute = ga.runGA();
        SA sa = new SA(combinedPoints, selectedPoints);
        RouteResult saBestRoute = sa.runSA();
        ACO aco = new ACO(combinedPoints, selectedPoints);
        RouteResult acoBestRoute = aco.runACO();
        TS ts = new TS(combinedPoints, selectedPoints);
        RouteResult tsBestRoute = ts.runTS();
        PSO pso = new PSO(combinedPoints, selectedPoints);
        RouteResult psoBestRoute = pso.runPSO();
        List<RouteResult> allResults = new ArrayList<>();
        allResults.add(gaBestRoute);
        allResults.add(saBestRoute);
        allResults.add(acoBestRoute);
        allResults.add(tsBestRoute);
        allResults.add(psoBestRoute);
        // Sort by distance (assuming smaller distance is better)
        allResults.sort((r1, r2) -> Double.compare(r1.getDistance(), r2.getDistance()));
        // Get the best, 2nd, 3rd, 4th, and worst
        RouteResult bestRoute = allResults.get(0);   // Best
        RouteResult secondBestRoute = allResults.get(1);   // 2nd Best
        RouteResult thirdBestRoute = allResults.get(2);   // 3rd Best
        RouteResult fourthBestRoute = allResults.get(3);   // 4th Best
        RouteResult worstRoute = allResults.get(allResults.size() - 1);   // Worst
        ArrayList<Point> allIds = new ArrayList<>();
        int id = 1;
        for (double[] coordinate : coordinates) {
        allIds.add(new Point(id++, coordinate[0], coordinate[1]));
        }
     // Add the second, third, fourth, and worst algorithms
        ArrayList<Point> gaRoute = bestRoute.getRoute();
        addToExcel(allIds, workbook, sheet, routeNumber, selectedPoints, gaRoute, bestRoute.getDistance(), bestRoute.getAlgorithm(), secondBestRoute.getDistance(), secondBestRoute.getAlgorithm(), thirdBestRoute.getDistance(), thirdBestRoute.getAlgorithm(), fourthBestRoute.getDistance(), fourthBestRoute.getAlgorithm(), worstRoute.getDistance(), worstRoute.getAlgorithm());
    }

    public static void addToExcel(ArrayList<Point> allIds, Workbook workbook, Sheet sheet, double routeNumber, ArrayList<Point> selectedPoints, ArrayList<Point> route, double distance, String algorithm, double distance2, String algorithm2, double distance3, String algorithm3, double distance4, String algorithm4, double distance5, String algorithm5) {
        int rowCount = sheet.getLastRowNum() + 1;  // Get the next row number
        Row row = sheet.createRow(rowCount);
        // Extract IDs from selectedPoints and route
        List<Integer> selectedPointIds = new ArrayList<>();
        for (Point p : selectedPoints) {
            selectedPointIds.add(p.getId());
        }
        String joinSelectedIds = selectedPointIds.stream()
            .map(String::valueOf)
            .collect(Collectors.joining("-"));

        List<Integer> routeIds = new ArrayList<>();
        for (Point routePoint : route) {
            for (Point allIdPoint : allIds) {
                if (routePoint.getX() == allIdPoint.getX() && routePoint.getY() == allIdPoint.getY()) {
                    routeIds.add(allIdPoint.getId());
                    break;
                }
            }
        }
        String joinRouteIds = routeIds.stream()
            .map(String::valueOf)  // Convert each Integer to String
            .collect(Collectors.joining("-"));
        row.createCell(0).setCellValue(routeNumber);
        row.createCell(1).setCellValue(selectedPoints.toString());
        row.createCell(2).setCellValue(joinSelectedIds);
        row.createCell(3).setCellValue(route.toString());
        row.createCell(4).setCellValue(joinRouteIds);
        row.createCell(5).setCellValue(distance);
        row.createCell(6).setCellValue(algorithm);
        row.createCell(7).setCellValue(distance2);
        row.createCell(8).setCellValue(algorithm2);
        row.createCell(9).setCellValue(distance3);
        row.createCell(10).setCellValue(algorithm3);
        row.createCell(11).setCellValue(distance4);
        row.createCell(12).setCellValue(algorithm4);
        row.createCell(13).setCellValue(distance5);
        row.createCell(14).setCellValue(algorithm5);
    }

    public static ArrayList<Point> buildRSelectedPoints(int numberOfVisitingPoints, double[][] coordinates) {
        ArrayList<Point> allPoints = new ArrayList<>();
        int id = 1;
        for (double[] coordinate : coordinates) {
        allPoints.add(new Point(id++, coordinate[0], coordinate[1]));
        }
        ArrayList<Point> selectedPoints = new ArrayList<>();
        List<Point> tempPoints = new ArrayList<>(allPoints);
        Collections.shuffle(tempPoints);
        for (int i = 0; i < numberOfVisitingPoints; i++) {
            selectedPoints.add(tempPoints.get(i));
        }
        System.out.println("\nVisiting Points: " + selectedPoints);
        return selectedPoints;
    }
    
    public static ArrayList<Point> buildPSelectedPoints(int numberOfVisitingPoints, int selectedSet, double[][] coordinates) {
    	ArrayList<Point> allPoints = new ArrayList<>();
        int id = 1;
        for (double[] coordinate : coordinates) {
        allPoints.add(new Point(id++, coordinate[0], coordinate[1]));
        }
    	
    	ArrayList<Point> selectedPoints = new ArrayList<>();
        double[][] n6_0 = {{2.0, 4.25}, {3.25, 2.0}, {5.0, 1.25}, {5.0, 2.25}, {2.0, 2.25}, {3.25, 4.0}};
        double[][] n7_0 = {{5.0, 1.5}, {3.0, 3.25}, {1.0, 3.5}, {4.75, 1.0}, {2.0, 1.75}, {1.0, 2.0}, {1.0, 1.5}};
        double[][] n8_0 = {{5.5, 2.0}, {2.0, 3.5}, {5.0, 3.75}, {5.5, 3.0}, {4.0, 2.0}, {3.0, 3.25}, {4.0, 3.0}, {1.0, 1.75}};
        double[][] n9_0 = {{4.5, 3.0}, {2.25, 1.0}, {5.5, 5.0}, {4.0, 1.25}, {3.0, 2.5}, {3.0, 4.5}, {3.0, 2.0}, {6.0, 3.25}, {1.0, 3.0}};
        double[][] n6_1 = {{3.75, 5.0}, {6.0, 2.75}, {5.25, 2.0}, {2.0, 1.5}, {5.0, 3.5}, {4.75, 1.0}};
        double[][] n7_1 = {{4.0, 3.5}, {2.0, 1.25}, {1.0, 1.75}, {1.5, 3.0}, {6.0, 3.0}, {3.75, 4.0}, {3.0, 5.0}};
        double[][] n8_1 = {{5.25, 4.0}, {2.25, 5.0}, {5.5, 2.0}, {3.75, 4.0}, {3.0, 5.0}, {5.25, 1.0}, {4.0, 4.75}, {6.0, 1.5}};
        double[][] n9_1 = {{3.0, 2.75}, {4.5, 3.0}, {6.0, 4.0}, {5.25, 5.0}, {3.0, 2.25}, {2.75, 1.0}, {1.75, 1.0}, {2.0, 3.5}, {3.25, 4.0}};
        double[][] n6_2 = {{1.0, 2.25}, {4.0, 5.0}, {6.0, 1.75}, {6.0, 2.25}, {1.75, 4.0}, {3.0, 1.75}};
        double[][] n7_2 = {{6.0, 1.5}, {1.75, 4.0}, {2.75, 3.0}, {2.75, 4.0}, {6.0, 1.0}, {6.0, 3.5}, {1.75, 2.0}};
        double[][] n9_2 = {{3.0, 2.75}, {4.5, 3.0}, {6.0, 4.0}, {5.25, 5.0}, {3.0, 2.25}, {2.75, 1.0}, {1.75, 1.0}, {2.0, 3.5}, {3.25, 4.0}};
        double[][] n8_2 = {{4.5, 3.0}, {4.0, 2.0}, {1.0, 2.0}, {5.0, 4.25}, {3.0, 3.75}, {3.0, 2.5}, {4.75, 4.0}, {3.25, 3.0}};
        double[][] n6_3 = {{1.0, 1.5}, {1.0, 2.75}, {5.0, 1.75}, {1.5, 5.0}, {1.25, 3.0}, {4.0, 3.25}};
        double[][] n7_3 = {{1.0, 3.25}, {4.0, 4.75}, {3.0, 5.0}, {4.25, 3.0}, {5.25, 2.0}, {5.0, 5.0}, {2.0, 2.25}};
        double[][] n8_3 = {{4.0, 4.5}, {1.75, 3.0}, {1.0, 1.25}, {1.0, 1.0}, {4.5, 4.0}, {1.0, 1.5}, {4.75, 4.0}, {4.25, 3.0}};
        double[][] n9_3 = {{3.75, 1.0}, {4.0, 4.75}, {4.5, 5.0}, {5.5, 1.0}, {2.25, 3.0}, {3.25, 1.0}, {5.5, 4.0}, {2.0, 1.75}, {3.0, 4.25}};
        double[][] n6_4 = {{2.0, 1.5}, {1.0, 1.75}, {5.25, 3.0}, {3.75, 1.0}, {2.0, 3.0}, {2.75, 2.0}};
        double[][] n7_4 = {{1.75, 4.0}, {4.25, 2.0}, {2.0, 5.0}, {4.0, 1.75}, {3.0, 3.25}, {2.5, 3.0}, {1.75, 5.0}};
        double[][] n8_4 = {{5.5, 4.0}, {5.75, 5.0}, {5.5, 1.0}, {4.75, 5.0}, {5.75, 2.0}, {3.0, 2.0}, {1.0, 3.25}, {1.0, 4.25}};
        double[][] n9_4 = {{5.5, 1.0}, {2.0, 1.75}, {4.25, 5.0}, {5.0, 5.0}, {1.25, 4.0}, {3.0, 1.75}, {4.25, 1.0}, {5.0, 3.25}, {5.5, 5.0}}; 
        
    	if (selectedSet == 1 && numberOfVisitingPoints == 6) {
            for (double[] pointCoords : n6_0) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 1 && numberOfVisitingPoints == 7) {
            for (double[] pointCoords : n7_0) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 1 && numberOfVisitingPoints == 8) {
            for (double[] pointCoords : n8_0) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 1 && numberOfVisitingPoints == 9) {
            for (double[] pointCoords : n9_0) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        }
        if (selectedSet == 2 && numberOfVisitingPoints == 6) {
            for (double[] pointCoords : n6_1) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 2 && numberOfVisitingPoints == 7) {
            for (double[] pointCoords : n7_1) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 2 && numberOfVisitingPoints == 8) {
            for (double[] pointCoords : n8_1) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 2 && numberOfVisitingPoints == 9) {
            for (double[] pointCoords : n9_1) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        }
        if (selectedSet == 3 && numberOfVisitingPoints == 6) {
            for (double[] pointCoords : n6_2) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 3 && numberOfVisitingPoints == 7) {
            for (double[] pointCoords : n7_2) {
            	selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 3 && numberOfVisitingPoints == 8) {
            for (double[] pointCoords : n8_2) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 3 && numberOfVisitingPoints == 9) {
            for (double[] pointCoords : n9_2) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        }
        if (selectedSet == 4 && numberOfVisitingPoints == 6) {
            for (double[] pointCoords : n6_3) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 4 && numberOfVisitingPoints == 7) {
            for (double[] pointCoords : n7_3) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 4 && numberOfVisitingPoints == 8) {
            for (double[] pointCoords : n8_3) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 4 && numberOfVisitingPoints == 9) {
            for (double[] pointCoords : n9_3) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        }
        if (selectedSet == 5 && numberOfVisitingPoints == 6) {
            for (double[] pointCoords : n6_4) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 5 && numberOfVisitingPoints == 7) {
            for (double[] pointCoords : n7_4) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 5 && numberOfVisitingPoints == 8) {
            for (double[] pointCoords : n8_4) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        } else if (selectedSet == 5 && numberOfVisitingPoints == 9) {
            for (double[] pointCoords : n9_4) {
                selectedPoints.add(getPointByCoordinates(allPoints, pointCoords[0], pointCoords[1]));
            }
        }
        System.out.println("\nVisiting Points: " + selectedPoints);
        return selectedPoints;
    }
    
    private static Point getPointByCoordinates(ArrayList<Point> points, double x, double y) {
        for (Point point : points) {
            if (point.getX() == x && point.getY() == y) {
                return point; // Return the point with matching coordinates
            }
        }
        return null; // Return null if no matching point is found
    }

    public static ArrayList<Point> buildCombinedPoints (ArrayList<Point> selectedPoints) {
        ArrayList<Point> adjacentPoints = new ArrayList<>();
        for (Point p : selectedPoints) {
            adjacentPoints.addAll(p.getAdjacentIntegerPoints());
        }
        Set<Point> combinedPointsSet = new HashSet<>(adjacentPoints);
        combinedPointsSet.addAll(selectedPoints);
        ArrayList<Point> combinedPoints = new ArrayList<>(combinedPointsSet);
        System.out.print("\nCombined Points: " +combinedPoints);
        return combinedPoints;
    }
    
    public static RouteResult getBestRoute(RouteResult acoBestRoute, RouteResult psoBestRoute, RouteResult gaBestRoute, RouteResult saBestRoute, RouteResult tsBestRoute) {
        double minDistance = tsBestRoute.distance;
        ArrayList<Point> bestRoute = tsBestRoute.route;
        String bestAlgorithm = "TS";
        if (acoBestRoute.distance < minDistance) {
            minDistance = acoBestRoute.distance;
            bestRoute = acoBestRoute.route;
            bestAlgorithm = "ACO";
        }
        if (psoBestRoute.distance < minDistance) {
            minDistance = psoBestRoute.distance;
            bestRoute = psoBestRoute.route;
            bestAlgorithm = "PSO";
        }
        if (saBestRoute.distance < minDistance) {
            minDistance = saBestRoute.distance;
            bestRoute = saBestRoute.route;
            bestAlgorithm = "SA";
        }
        if (gaBestRoute.distance < minDistance) {
            minDistance = gaBestRoute.distance;
            bestRoute = gaBestRoute.route;
            bestAlgorithm = "GA";
        }
        return new RouteResult(bestRoute, minDistance, bestAlgorithm);
    }
}
