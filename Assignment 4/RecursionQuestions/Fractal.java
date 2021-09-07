// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 4
 * Name: Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

import ecs100.*;
import java.util.*;

/** Fractal   */
public class Fractal{
    /**
     * Construct a Fractal Diagram made up of a triangular pattern of
     * "elements". Each element is a circle with three "spikes".
     * See the assignment description for the pattern of how the
     *  elements are put together.
     * x and y are the center of the central element of the pattern, and
     * size is the length of the three spikes from that element.
     * Each element will have three smaller elements placed at the ends of its spikes
     * You should stop if the size of the element is 3 or less.
     *
     * The diagram is a List of all the elements.
     *  Your method MUST use addElement(....) to add an element to the diagram.
     *  Do NOT try to draw the element directly on the graphics pane.
     */
    public void makeFractal(double x, double y, double size){
        if(size <= 3){ // if size less
            return; // return out
        }
        makeFractal(x+size,y,size/2); // Right fractal
        makeFractal(x,y-size,size/2); // Top Fractal (y - size) because further down is bigger Y
        makeFractal(x-size,y,size/2); // Left fractal
        addElement(x,y,size); // draw at current pos

    }

    /**
     * Adds one fractal element to the diagram.
     * The element is a circle and three spikes.
     * The method prints out the element in the text pane to
     * help your debugging.
     */
    public void addElement(double x, double y, double size){
        FracElement elem = new FracElement(x, y, size); // make the element
        diagram.add(elem);                              // add it to the diagram
        UI.printf("%.0f@(%.0f,%.0f)\n", size, x, y);    // print to help with debugging
    }

    // ===============================
    // CODE FOR TESTING  (and Marking)
    // ===============================

    /**
     * Initialise the interface
     */
    public void setupGUI(){
        UI.setMouseListener(this::doMouse);
        UI.addButton("Clear", UI::clearPanes);
        UI.addButton("Quit", UI::quit);
        UI.drawString("Drag mouse to test your fractal", 3, 20);
    }

    private double fX,fY;  // for the mouse actions

    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){fX=x; fY=y;}
        else if (action.equals("released")){
            for (FracElement el : testFractal(fX,fY,Math.hypot(x-fX,y-fY))){
                el.draw();
            }
        }
    }

    //================================================
    // DO NOT CHANGE ANYTHING BELOW THIS LINE!
    // IF YOU CHANGE THIS IN ANYWAY,
    // THE AUTOMATED MARKING WILL FAIL AND GIVE YOU ZERO

    /**
     * Used for marking. DO NOT MODIFY.
     * This is where the elements in the diagram are kept.
     */
    private List<FracElement> diagram = new ArrayList<FracElement>();

    /**
     * Used for marking. DO NOT MODIFY!
     * Resets the diagram list, and then calls the makeFractal method
     * Returns the diagram.
     */
    public List<FracElement> testFractal(double centerX, double centerY, double size){
        diagram = new ArrayList<FracElement>(); // initialise the list for the diagram
        makeFractal(centerX, centerY, size);    // make the diagram
        return diagram;                         // return the diagram
    }

    public static void main(String[] arguments){
        new Fractal().setupGUI();;
    }        

    /**
     * Does nothing, but compiling with this method ensures that the method
     *  headers have not been changed.
     */
    public void checkCompile(){
        List<FracElement> res = testFractal(0.,0.,0.);
    }

}
