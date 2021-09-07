// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 4
 * Name: Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * The board game involves moving a piece on a row of squares.
 * Each square on the board has a positive integer on it.
 * The piece can move to the left or right.
 * When moving to the left, the piece must move the number of
 *  steps specified in the cell immediately to its left.
 * When moving to the right, the piece must move the number of
 *  steps specified in the cell immediately to its right.
 * For example:
 *  If the piece is currently at the cell marked P on the following board:
 *       +---+---+---+---+---+---+---+
 *       | 3 | 4 | 2 | P | 3 | 1 | 5 |
 *       +---+---+---+---+---+---+---+
 *  then the piece could move 2 steps to the left, or 3 steps to the right.
 *  If the piece is placed in the cell marked P on the following board:
 *       +---+---+---+---+---+---+---+
 *       | 3 | 4 | 2 | 4 | 3 | P | 5 |
 *       +---+---+---+---+---+---+---+
 *  then the piece can't move to the right (because 5 steps to the right would
 *  take it off the board) but it can move 3 steps to the left.
 *
 * Depending on the numbers on the board, the piece may be able to get to lots
 * of different square, or to very few squares.
 * For example (if the squares are counted from 0)
 *  In the board above, starting at square number 4, the piece could get to
 *  squares 3, 1, 6, 5, 2, but couldn't get to squares 0 or 4.

 *  In this board, starting at square number 3 (underlined)
 *       +---+---+---+---+---+---+---+---+---+
 *       | 3 | 2 | 1 | 5 | 3 | 3 | 1 | 4 | 1 |
 *       +---+---+---+===+---+---+---+---+---+
 *  the piece could get to squares 3, 2, 0, 7, 8, and 6, but not 1, 4, or 5.
 *
 * You have to complete the findPlaces method to find all the squares that the piece
 * could get to from a given starting position.
 * The findPlaces method must construct and return a Set of all the indexes of the
 * squares the piece can get to (including the starting point).
 * It should not re-visit any square that it has
 * already been to because that would lead to an infinite loop!
 */

public class BoardGame{
    /**
     * Recursive method to find all the places on the board that
     *   the piece could get to from pos
     * places is the Set of places that it has been to.
     * The method should add new positions it finds to places.
     */

    public void findPlaces(List<Integer> board, int pos, Set<Integer> places){  
        places.add(pos); // add to visited
        if(pos + 1 != board.size()){ // if not on the last one check if can move right
            if(pos + board.get(pos+1) < board.size()  && !places.contains(pos + board.get(pos+1))){ // check if still within bounds
                findPlaces(board,pos + board.get(pos+1),places); // recursive it
            }
        }
        if(pos != 0){ // if not on the first one check if can move less
            if(pos - board.get(pos-1) >= 0 && !places.contains(pos - board.get(pos-1))){ // check if still within bounds
                findPlaces(board,pos - board.get(pos-1),places); // move to that position (recursively)
            }
        }        
    }

    /**
     * Top level method to find all the places on the board that the
     * piece could get to if from the given starting position.
     */
    public Set<Integer> findPlaces(List<Integer> board, int startPos){
        Set<Integer> places = new HashSet<Integer>();
        findPlaces(board, startPos, places);
        return places;
    }

    // Testing code ===============================================

    public static final double LEFT = 10;
    public static final double TOP = 10;
    public static final double SIZE = 40;

    private List<Integer> board = new ArrayList<Integer>();
    /**
     * Setup the GUI
     */
    public void setupGUI(){
        UI.addSlider("Random Board: choose size", 10, 20, (double v)->{makeBoard((int)v);});
        UI.addTextField("Specify board: list numbers", (String s)->{makeBoard(s);});
        UI.addButton("Test findPlaces", this::testFindPlaces);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);
        UI.setFontSize(20);
    }

    /**
     * Calls the findPlaces method and draws all the places
     * it finds on the current board
     */
    public void testFindPlaces(){
        if (board.isEmpty()){UI.println("No Board"); return;}
        int start = board.size()/2;
        Set<Integer> places = findPlaces(board, start);
        drawBoard(board, start, places);
    }

    public static void main(String[] arguments){
        new BoardGame().setupGUI();
    }

    /** Make a board of the given size with random numbers */
    public void makeBoard(int size){
        List<Integer> ans = new ArrayList<Integer>();
        for(int i = 0; i<size; i++){
            ans.add((int)(Math.random()*size*.8)+1);
        }
        board = Collections.unmodifiableList(ans);
        drawBoard(board);
    }

    /** Make a board with the numbers in the string */
    public void makeBoard(String nums){
        List<Integer> ans = new ArrayList<Integer>();
        Scanner sc = new Scanner(nums);
        while(sc.hasNext()){
            if (sc.hasNextInt()) {
                ans.add(sc.nextInt());
            }
            else {
                sc.next();
            }
        }
        board = Collections.unmodifiableList(ans);
        drawBoard(board);
    }

    /** Draw the board */
    public void drawBoard(List<Integer> board){
        UI.clearGraphics();
        UI.setColor(Color.black);
        UI.setLineWidth(2);
        for (int i=0; i<board.size(); i++){
            UI.drawRect(LEFT+i*SIZE, TOP, SIZE, SIZE);
            UI.drawString(""+board.get(i), LEFT+(i+0.5)*SIZE-8, TOP+SIZE/2+10);
        }
    }

    /** Draw the board with the starting position and the places it can get to */
    public void drawBoard(List<Integer> board, int start, Set<Integer> places){
        drawBoard(board);
        // draw set of found places.
        UI.setColor(Color.green);
        double rad = SIZE/2-3;
        for (int i : places){
            UI.drawRect(LEFT+(i+.5)*SIZE-rad, TOP+SIZE/2-rad, 2*rad, 2*rad);
        }
        // draw start
        UI.setColor(Color.blue);
        UI.drawString(""+board.get(start), LEFT+(start+0.5)*SIZE-8, TOP+SIZE/2+10);

        UI.setColor(Color.black);

        UI.drawString("Blue number is the starting point", LEFT, TOP+SIZE*2);
        UI.drawString("Green are the places it can get to", LEFT, TOP+SIZE*3);
    }

    //================================================
    // DO NOT CHANGE ANYTHING BELOW THIS LINE!
    // IF YOU CHANGE THIS TO MAKE YOUR CODE COMPILE,
    // THE AUTOMATED MARKING WILL FAIL AND GIVE YOU ZERO

    /**
     * Does nothing, but compiling with this method ensures that the method
     *  headers have not been changed.
     */
    public void checkCompile(){
        Set<Integer> places = findPlaces(board, 0);
    }

}
