// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 3
 * Name:Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

import ecs100.*;
import java.awt.Color;
import javax.swing.JButton;

/**
 *  Simple 'Minesweeper' program.
 *  There is a grid of squares, some of which contain a mine.
 *  The user can click on a square to either expose it or to
 *  mark/unmark it.
 *  
 *  If the user exposes a square with a mine, they lose.
 *  Otherwise, it is uncovered, and shows a number which represents the
 *  number of mines in the eight squares surrounding that one.
 *  If there are no mines adjacent to it, then all the unexposed squares
 *  immediately adjacent to it are exposed (and and so on)
 *
 *  If the user marks a square, then they cannot expose the square,
 *  (unless they unmark it first)
 *  When all squares with mines are marked, and all the squares without
 *  mines are exposed, the user has won.
 */
public class MineSweeper {

    public static final int ROWS = 15;
    public static final int COLS = 15;

    public static final double LEFT = 10; 
    public static final double TOP = 10;
    public static final double SQUARE_SIZE = 20;

    // Fields
    private boolean marking;

    private Square[][] squares;

    private JButton mrkButton;
    private JButton expButton;
    Color defaultColor;

    /** 
     * Construct a new MineSweeper object
     * and set up the GUI
     */
    public static void main(String[] arguments){
        MineSweeper ms = new MineSweeper();
        ms.setupGUI();
        ms.setMarking(false);
        ms.makeGrid();
    }        

    /** Set up the GUI: buttons and mouse to play the game */
    public void setupGUI(){
        UI.setMouseListener(this::doMouse);
        UI.addButton("New Game", this::makeGrid);
        UI.addButton("CHALLANGE", this::hint); // adds challange button
        this.expButton = UI.addButton("Expose", ()->setMarking(false));
        this.mrkButton = UI.addButton("Mark", ()->setMarking(true));

        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);
    }

    /** Respond to mouse events */
    public void doMouse(String action, double x, double y) {
        if (action.equals("released")){
            int row = (int)((y-TOP)/SQUARE_SIZE);
            int col = (int)((x-LEFT)/SQUARE_SIZE);
            if (row>=0 && row < ROWS && col >= 0 && col < COLS){
                if (marking) { mark(row, col);}
                else         { tryExpose(row, col); }
            }
        }
    }

    /**
     * Remember whether it is "Mark" or "Expose"
     * Change the colour of the "Mark", "Expose" buttons
     */
    public void setMarking(boolean v){
        marking=v;
        if (marking) {
            mrkButton.setBackground(Color.red);
            expButton.setBackground(null);
        }
        else {
            expButton.setBackground(Color.red);
            mrkButton.setBackground(null);
        }
    }

    // Other Methods

    /** 
     * The player has clicked on a square to expose it
     * - if it is already exposed or marked, do nothing.
     * - if it's a mine: lose (call drawLose()) 
     * - otherwise expose it (call exposeSquareAt)
     * then check to see if the player has won and call drawWon() if they have.
     * (This method is not recursive)
     */
    public void tryExpose(int row, int col){
        if(!squares[row][col].isExposed() && !squares[row][col].isMarked()){ // if not exposed and is not marked
            if (squares[row][col].hasMine()){ // if mine
                drawLose(); // loose
            }
            else{
                exposeSquareAt(row,col); // expose
                if(hasWon()){ // check if win
                    drawWin(); // if win draw win
                }
            }
        }
    }

    /** 
     *  Expose a square, and spread to its neighbours if safe to do so.
     *  It is guaranteed that this square is safe to expose (ie, does not have a mine).
     *  If it is already exposed, we are done.
     *  Otherwise expose it, and redraw it.
     *  If the number of adjacent mines of this square is 0, then
     *     expose all its neighbours (which are safe to expose)
     *     (and if they have no adjacent mine, expose their neighbours, and ....)
     */
    public void exposeSquareAt(int row, int col){
        if(row < ROWS && col < COLS && row >= 0 && col >= 0){ // if within boundary
            if(!squares[row][col].isExposed()){ // if not exposed
                double x = LEFT + col * SQUARE_SIZE;
                double y = TOP  + row * SQUARE_SIZE;
                squares[row][col].setExposed();  //set exposed
                squares[row][col].draw(x,y,SQUARE_SIZE);          // redraw
                if(squares[row][col].getAdjacentMines() == 0){ // if no mines in neighbours spread
                    exposeSquareAt(row-1,col-1); // Top Left
                    exposeSquareAt(row-1,col); // Top 
                    exposeSquareAt(row-1,col+1); // Top right

                    exposeSquareAt(row,col-1); // Left
                    exposeSquareAt(row,col+1); // Right 

                    exposeSquareAt(row+1,col); // Bottom 
                    exposeSquareAt(row+1,col-1); // Bottom Left
                    exposeSquareAt(row+1,col+1); // Bottom Right

                }
            }
        }

    }

    /**
     * Mark/unmark the square.
     * If the square is exposed, don't do anything,
     * If it is marked, unmark it.
     * otherwise mark it and redraw.
     * (Marking cannot make the player win or lose)
     */
    public void mark(int row, int col){ 
        if(squares[row][col].isExposed()){return;}  // if exposed return
        double x = LEFT + col * SQUARE_SIZE;
        double y = TOP  + row * SQUARE_SIZE;
        squares[row][col].toggleMark();  // toggle the mark
        squares[row][col].draw(x,y,SQUARE_SIZE); // redraw
    }
    
    public void markIfNotMarked(int row, int col){ // used for challange Only runs if not marked
        if(squares[row][col].isMarked() || squares[row][col].isExposed()){return;} 

        double x = LEFT + col * SQUARE_SIZE;
        double y = TOP  + row * SQUARE_SIZE;
        squares[row][col].toggleMark(); 
        squares[row][col].draw(x,y,SQUARE_SIZE);
    }


    /** 
     * Returns true if the player has won:
     * If all the squares without a mine have been exposed, then the player has won.
     */
    public boolean hasWon(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){ 
                if(!squares[i][j].isExposed() && !squares[i][j].hasMine()){ // if any square is not exposed and does not has a mine then game is not finished
                    return false;
                }
            }
        }        
        return true; // if reached game is finished
    }

    /**
     * Construct a grid with random mines.
     */
    public void makeGrid(){
        UI.clearGraphics();
        this.squares = new Square[ROWS][COLS];
        for (int row=0; row < ROWS; row++){
            double y = TOP+row*SQUARE_SIZE;
            for (int col=0; col<COLS; col++){
                double x =LEFT+col*SQUARE_SIZE;
                boolean isMine = Math.random()<0.1;     // approx 1 in 10 squares is a mine 
                this.squares[row][col] = new Square(isMine);
                this.squares[row][col].draw(x, y, SQUARE_SIZE);
            }
        }
        // now compute the number of adjacent mines for each square
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                int count = 0;
                //look at each square in the neighbourhood.
                for (int r=Math.max(row-1,0); r<Math.min(row+2, ROWS); r++){
                    for (int c=Math.max(col-1,0); c<Math.min(col+2, COLS); c++){
                        if (squares[r][c].hasMine())
                            count++;
                    }
                }
                if (this.squares[row][col].hasMine())
                    count--;  // we weren't suppose to count this square, just the adjacent ones.

                this.squares[row][col].setAdjacentMines(count);
            }
        }
    }

    /** Draw a message telling the player they have won */
    public void drawWin(){
        UI.setFontSize(28);
        UI.drawString("You Win!", LEFT + COLS*SQUARE_SIZE + 20, TOP + ROWS*SQUARE_SIZE/2);
        UI.setFontSize(12);
    }

    /**
     * Draw a message telling the player they have lost
     * and expose all the squares and redraw them
     */
    public void drawLose(){
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                squares[row][col].setExposed();
                squares[row][col].draw(LEFT+col*SQUARE_SIZE, TOP+row*SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        UI.setFontSize(28);
        UI.drawString("You Lose!", LEFT + COLS*SQUARE_SIZE+20, TOP + ROWS*SQUARE_SIZE/2);
        UI.setFontSize(12);
    }

    
    /** CHALLANGE
     *  Automatically flags all safe squares that the user can see.
     *  DOES NOT VISION CHEAT
     */
    public void hint(){       
            for (int row=0; row<ROWS; row++){
                for (int col=0; col<COLS; col++){
                    // if ammount of unexqposed squares is equal to amount of mines around that square
                    // and the square is exposed then autoflag
                    if(squares[row][col].isExposed() && squares[row][col].getAdjacentMines() == 8 -  getUnexposed(row,col)){ 
                        markIfNotMarked(row-1,col-1); // Top Left
                        markIfNotMarked(row-1,col); // Top 
                        markIfNotMarked(row-1,col+1); // Top right

                        markIfNotMarked(row,col-1); // Left
                        markIfNotMarked(row,col+1); // Right 

                        markIfNotMarked(row+1,col); // Bottom 
                        markIfNotMarked(row+1,col-1); // Bottom Left
                        markIfNotMarked(row+1,col+1); // Bottom Right
                    }                                    
                }
            }       
    }

    public int getUnexposed(int row,int col){ // gets unexposed squares around a square.
                int count = 0;
                //look at each square in the neighbourhood.
                for (int r=Math.max(row-1,0); r<Math.min(row+2, ROWS); r++){
                    for (int c=Math.max(col-1,0); c<Math.min(col+2, COLS); c++){
                        if (squares[r][c].isExposed())
                            count++;
                    }
                }
                if (this.squares[row][col].isExposed())
                    count--;  // we weren't suppose to count this square, just the adjacent ones.
                return(count);                   
    }
}
