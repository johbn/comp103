// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 2
 * Name:Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JColorChooser;

/** Pencil   */
public class Pencil{
    private double lastX;
    private double lastY;
    private ArrayList <ArrayList> drawingArray = new ArrayList <ArrayList>(); // Stores all the brushstrokes for undoing
    private ArrayList <ArrayList> redoDrawingArray = new ArrayList<ArrayList>(); // Stores all the brushstrokes for redoing
    private ArrayList <BrushStroke> brushStrokeArray = new ArrayList<BrushStroke>();// stores the individual points that make up a brushstroke 
    Color pencilColour = new Color(0,0,0);
    double strokeSize = 5;
    JButton button;

    /**
     * Setup the GUI
     */ 
    public void setupGUI(){
        UI.setMouseMotionListener(this::doMouse);
        button = UI.addButton("ColourPicker", this::colourPicker);
        UI.addButton("Undo", this::undo);
        UI.addButton("Redo", this::redo);
        UI.addButton("Quit", UI::quit);
        UI.addSlider("StrokeSize", 1, 30, this::setSliderValue);
        UI.setLineWidth(3);
        UI.setDivider(0.0);
    }

    /**
     * Respond to mouse events
     */
    public void doMouse(String action, double x, double y) {
        if(action.equals("pressed")){
            lastX = x;
            lastY = y;
        }
        else if(action.equals("dragged")){
            /**Sets colour and stroke size**/
            UI.setColor(pencilColour);
            UI.setLineWidth(strokeSize);
            /**Clears the array because user drawed something**/
            redoDrawingArray = new ArrayList<ArrayList>(); // reset redo as the user has drawn something new
            /**Create and push every point for undoing**/
            BrushStroke tempBrushStroke = new BrushStroke(x,y,lastX,lastY,pencilColour,strokeSize); // create new brushstroke
            brushStrokeArray.add(tempBrushStroke); // push that brushstroke to the stack     
            /**Draw line**/
            UI.drawLine(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        }
        else if(action.equals("released")){            
            UI.setColor(pencilColour);
            UI.setLineWidth(strokeSize);
            UI.drawLine(lastX, lastY, x, y);
            /****Needs to push Otherwise will have a floating point at the end***/
            BrushStroke tempBrushStroke = new BrushStroke(x,y,lastX,lastY,pencilColour,strokeSize);
            brushStrokeArray.add(tempBrushStroke); //add that point to the array of points
            /*******log for undoing*********/
            drawingArray.add(0,brushStrokeArray);              // add that line to array for undoing
            brushStrokeArray =  new ArrayList <BrushStroke>(); // needs to be reset to avoid point by reference.
        }
    }

    public void redraw(){
        UI.clearGraphics(); // clears the graphics pane
        for(int i = drawingArray.size(); i > 0; i--){ //for all the lines
            ArrayList <BrushStroke> tempBrushStrokeArray  = drawingArray.get(i-1); //get that line
            for(int x = 0; x < tempBrushStrokeArray.size(); x++){                  // redraw all points on that line
                BrushStroke tempBrushStroke = tempBrushStrokeArray.get(x);         //get info about specific point                          
                UI.setColor(tempBrushStroke.getColour());                          //set colour to the colour that point was drawn in
                UI.setLineWidth(tempBrushStroke.getStrokeSize());                  //set strokesize to the size that point was drawn in                             
                UI.drawLine(tempBrushStroke.getLastX(),tempBrushStroke.getLastY(),tempBrushStroke.getX(),tempBrushStroke.getY()); // draw that specfic point  
            }
        }
        /****************CHALLANGE***********/
        if(!drawingArray.isEmpty()){                                              //if there is still a brushstroke
            ArrayList <BrushStroke> tempArrayOfBrushStroke = drawingArray.get(0); // get that brushstroke
            BrushStroke tempPoint = tempArrayOfBrushStroke.get(0);                // get an indivdual point in that brushstroke
            pencilColour = tempPoint.getColour();                                 // set the pencil colour to be the colour of that point
            strokeSize = tempPoint.getStrokeSize();                               // set the strokeSize to be the size of that point
            button.setForeground(pencilColour);                                   //set button colour
        }
    }

    public void undo(){
        if(!drawingArray.isEmpty()){                  
            redoDrawingArray.add(0,drawingArray.get(0)); // add for redoing latter      
            drawingArray.remove(0);                      // remove from the array    
            redraw();                                    // redraw the screen
        }
    }

    public void redo(){
        if(!redoDrawingArray.isEmpty()){                 // if there is a line to redo
            drawingArray.add(0,redoDrawingArray.get(0)); // add the line back to the array to draw
            redoDrawingArray.remove(0);                  // remove from the redo array
            redraw();                                    // redraw the screen
        }
    }

    public static void main(String[] arguments){
        new Pencil().setupGUI();
    }

    public void colourPicker(){ // handles choosing colour
        pencilColour = JColorChooser.showDialog(null, "Choose Colour",pencilColour);
        button.setForeground(pencilColour);
    }

    public void setSliderValue(double test){ // handles the slider
        strokeSize = test;
    }
}
