This repository contains the implementation of various algorithms to solve the Traveling Salesman Problem (TSP), specifically tailored for an Open Day demonstration. The primary goal is to find the shortest possible route that visits all selected points on a TSP board arranged in a grid, using multiple metaheuristic algorithms.

Algorithms Implemented:
 -  Genetic Algorithm (GA)
 -  Ant Colony Optimization (ACO)
 -  Simulated Annealing (SA)
 -  Tabu Search (TS)
 -  Particle Swarm Optimization (PSO)

Each algorithm is implemented in Java, utilizing a modular structure for ease of testing and extension. The repository is organized into folders for each algorithm, as well as additional utilities for running, comparing, and exporting results to Excel.

Project Structure:
 -  Point.java: Defines the key class for representing coordinates on the TSP grid, used across all algorithms.
 -  Run.java: The main execution hub that runs the algorithms, compares the results, and exports the best-performing routes to Excel for further analysis.
 -  Algorithm Folders: Each folder contains the Java implementation of one of the five algorithms listed above, with methods for constructing routes, optimizing them, and returning the shortest path discovered.

Key Features:
 -  Manhattan Distance Calculation: Routes are evaluated based on Manhattan distances between points, as the board is laid out on a grid.
 -  Export to Excel: Results are exported to Excel to facilitate the Open Day demonstration.
 -  Heuristic Methods: In the Genetic Algorithm, routes are initially generated using the Nearest Neighbor, Nearest Insertion, and Farthest Insertion methods, enhancing the population diversity.
 -  Comparative Analysis: Each algorithm’s performance is evaluated, and the best route and corresponding distance are returned.

How to Run:
1) Clone the repository: git clone https://github.com/jeanri-thomas/TSP-project.git
2) Import the project into your Java IDE (e.g., IntelliJ, Eclipse).
3) Run Run.java to execute all algorithms and compare the results.
4) View the exported results in the output Excel file.
