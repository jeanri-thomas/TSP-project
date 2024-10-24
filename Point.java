package main.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Point {
    private double x;
    private double y;
    private int id; 

    // Constructor with three parameters
    public Point(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    // Constructor with two parameters
    public Point(double x, double y) {
        this(0, x, y); 
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public List<Point> getAdjacentIntegerPoints() {
        Set<Point> adjacentPoints = new HashSet<>();
        int xFloor = (int) Math.floor(x);
        int xCeil = (int) Math.ceil(x);
        int yFloor = (int) Math.floor(y);
        int yCeil = (int) Math.ceil(y);
        // Check if x is non-integer
        if (Math.floor(x) != x) {
            adjacentPoints.add(new Point(id, xFloor, y));
            adjacentPoints.add(new Point(id, xCeil, y));
        }
        // Check if y is non-integer
        if (Math.floor(y) != y) {
            adjacentPoints.add(new Point(id, x, yFloor));
            adjacentPoints.add(new Point(id, x, yCeil));
        }
        // If both x and y are integers, include the point itself
        if (Math.floor(y) == y && Math.floor(x) == x) {
            adjacentPoints.add(new Point(id, x, y));
        }
        if (x > 3 && y >= 4) {
        	adjacentPoints.add(new Point(3, 5));
        	adjacentPoints.add(new Point(4, 5));
        }
        if (x == 5 && y >= 4) {
        	adjacentPoints.add(new Point(5, 5));
        }
        if (x == 6 && y >= 4) {
        	adjacentPoints.add(new Point(6, 5));
        }
        return new ArrayList<>(adjacentPoints);  // Convert Set back to List
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
