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

public class BrushStroke {
    double x;
    double y;
    double lastX;
    double lastY;
    double strokeSize;
    Color colour;
    public BrushStroke(double x,double y, double lastX, double lastY, Color c, double strokeSize){ // constructor
        this.x = x;
        this.y = y;
        this.lastX = lastX;
        this.lastY = lastY;
        this.strokeSize = strokeSize;
        this.colour = c;
    }
    
    /*********RETURN_METHODS********/

    public double getX(){
        return this.x;
    }

    public double getLastX(){
        return this.lastX;
    }

    public double getY(){
        return this.y;
    }

    public double getLastY(){
        return this.lastY;
    }
    
    public Color getColour(){
        return this.colour;
    }
    
    public double getStrokeSize(){
        return this.strokeSize;
    }
}