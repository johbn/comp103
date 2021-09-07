// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 5
 * Name: Todd Wellwood
 * Username: wellwotodd
 * ID: 300529406
 */

import ecs100.UI;

import java.awt.*;
import java.util.*;

///button to make, slider for size, slider for speed, delay -> field.

/**
 * Search for a path to the goal in a maze.
 * The maze consists of a graph of MazeCells:
 * Each cell has a collection of neighbouring cells.
 * Each cell can be "visited" and it will remember that it has been visited
 * A MazeCell is Iterable, so that you can iterate through its neighbour cells with
 * for(MazeCell neighbour : cell){....
 * <p>
 * The maze has a goal cell (shown in green, two thirds towards the bottom right corner)
 * The maze.getGoal() method returns the goal cell of the maze.
 * The user can click on a cell, and the program will search for a path
 * from that cell to the goal.
 * <p>
 * Every cell that is looked at during the search is coloured  yellow, and then,
 * if the cell turns out to be on a dead end, it is coloured red.
 */

public class MazeSearch {

    private Maze maze;
    private String search = "first";   // "first", "all", or "shortest"
    private int pathCount = 0;
    private boolean stopNow = false;

    /**
     * CORE
     * Search for a path from a cell to the goal.
     * Return true if we got to the goal via this cell (and don't
     * search for any more paths).
     * Return false if there is not a path via this cell.
     * <p>
     * If the cell is the goal, then we have found a path - return true.
     * If the cell is already visited, then abandon this path - return false.
     * Otherwise,
     * Mark the cell as visited, and colour it yellow [and pause: UI.sleep(delay);]
     * Recursively try exploring from the cell's neighbouring cells, returning true
     * if a neighbour leads to the goal
     * If no neighbour leads to a goal,
     * colour the cell red (to signal failure)
     * abandon the path - return false.
     */
    public boolean exploreFromCell(MazeCell cell) {
        if(cell == maze.getGoal()){
            cell.draw(Color.blue);   // to indicate finding the goal
            return true;
        }
        else if(cell.isVisited()){ // return false if its visited (dead end)
            return false;
        }
        else{
            cell.visit(); // visit and draw yellow
            cell.draw(Color.YELLOW);
            UI.sleep(delay);
            for(MazeCell currentCell : cell){ // then recursion all other cells
                if(exploreFromCell(currentCell)){ // if that cell returned true then return true as well
                    return true;
                }
            }
            cell.draw(Color.RED); // else draw it red if nothing returned true
            return false; // and return false

        }
    }

    /**
     * COMPLETION
     * Search for all paths from a cell,
     * If we reach the goal, then we have found a complete path,
     * so pause for 1000 milliseconds
     * Otherwise,
     * visit the cell, and colour it yellow [and pause: UI.sleep(delay);]
     * Recursively explore from the cell's neighbours,
     * unvisit the cell and colour it white.
     */
    public void exploreFromCellAll(MazeCell cell) { // finds all possible routes to the cell
        if(stopNow){
            return;
        }    // exit if user clicked the stop now button
        if(cell == maze.getGoal()){
            pathCount++;
            UI.printMessage("Found Paths: "+pathCount);
            cell.draw(Color.blue);   // to indicate finding the goal
            UI.sleep(500);
            cell.draw(Color.green);   // to indicate new attempt at finding the goal
        }
        else{
            cell.visit(); // visit
            cell.draw(Color.YELLOW); // paint yellow
            UI.sleep(delay);
            for(MazeCell currentCell : cell){
                if(!currentCell.isVisited()){ // if not visited then visit it
                    exploreFromCellAll(currentCell);
                }
            }
            cell.unvisit(); // unvisit (used for finding all paths)
            cell.draw(Color.WHITE);    // paint whit
        }
    }

    /**
     * CHALLENGE
     * Search for shortest path from a cell,
     * Use Breadth first search.
     */
    public void exploreFromCellShortest(MazeCell start) {
        /*# YOUR CODE HERE */
        ArrayDeque<MazeCell> que = new ArrayDeque<MazeCell>();
        HashMap<MazeCell, MazeCell> route = new HashMap<MazeCell, MazeCell>(); // maps the route from cell to cell
        que.offer(start); // give the que the starting cell
        start.draw(Color.yellow);
        MazeCell currentCell = null;
        while(!que.isEmpty()){ // For breadth first
            currentCell = que.poll(); // get element
            if(currentCell == maze.getGoal()) break; // if it is the goal we are done
            currentCell.visit(); // else visit the cell
            if(currentCell.equals(start)){ // if its the very first cell
                route.put(currentCell, null); // map it to null
            }
            for(MazeCell neighbourCell : currentCell){ // then for all neighbours
                if(!neighbourCell.isVisited()){ // if it hasent been visited (Stops infinite loop)
                    que.offer(neighbourCell); // add it to the que
                    if(!currentCell.equals(start)){ // and as long as it isnt the start (hasnt been mapped yet)
                        route.put(neighbourCell, currentCell); // map form new to previous
                    }
                }
            }
        }
        // once shortest route has been found
        while(currentCell != null){ // go through every cell
            currentCell.draw(Color.yellow); // paint it yellow
            if(currentCell == maze.getGoal()){ // if its the final cell
                currentCell.draw(Color.blue);// instead paint it blue
            }
            currentCell = route.get(currentCell); // get the next cells previous cell using our map
        }
    }

    //=================================================

    // fields for gui.
    private int delay = 20;
    private int size = 10;

    /**
     * Set up the interface
     */
    public void setupGui() {
        UI.addButton("New Maze", this::makeMaze);
        UI.addSlider("Maze Size", 4, 40, 10, (double v) -> {
            size = (int) v;
        });
        UI.setMouseListener(this::doMouse);
        UI.addButton("First path", () -> {
            search = "first";
        });
        UI.addButton("All paths", () -> {
            search = "all";
        });
        UI.addButton("Shortest path", () -> {
            search = "shortest";
        });
        UI.addButton("Stop", () -> {
            stopNow = true;
        });
        UI.addSlider("Speed", 1, 101, 80, (double v) -> {
            delay = (int) (100 - v);
        });
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.);
    }

    /**
     * Creates a new maze and draws it .
     */
    public void makeMaze() {
        maze = new Maze(size);
        maze.draw();
    }

    /**
     * Clicking the mouse on a cell should make the program
     * search for a path from the clicked cell to the goal.
     */
    public void doMouse(String action, double x, double y) {
        if(action.equals("released")){
            maze.reset();
            maze.draw();
            pathCount = 0;
            MazeCell start = maze.getCellAt(x, y);
            if(search == "first"){
                exploreFromCell(start);
            }
            else if(search == "all"){
                stopNow = false;
                exploreFromCellAll(start);
            }
            else if(search == "shortest"){
                exploreFromCellShortest(start);
            }
        }
    }

    public static void main(String[] args) {
        MazeSearch ms = new MazeSearch();
        ms.setupGui();
        ms.makeMaze();
    }
}

