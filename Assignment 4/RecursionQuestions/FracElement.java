// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 4
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;

    /**
     * DO NOT MODIFY!!
     * Used for marking.
     * Describes an individual element in the diagram
     * Has a draw method for drawing the element on the UI
     */

public class FracElement {
    public final double x, y, s;  // public fields; final to prevent changing them

    public FracElement(double u, double v, double sz){x=u;y=v;s=sz;}

    public void draw(){
        double rad = s/3;
        UI.fillOval(x-rad, y-rad, 2*rad, 2*rad);
        UI.drawLine(x, y, x-s, y);
        UI.drawLine(x, y, x+s, y);
        UI.drawLine(x, y, x,   y-s);
    }
        
}

