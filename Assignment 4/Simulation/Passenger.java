// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 4
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/**
 * Represents a Passenger at an Airport
 * Each Passenger has
 *    a field with their initials,
 *    their cabin class
 *    a seat number
 *
 * Constructor:
 *     Passenger(cabin, seat, initials)

 * Methods:
 *   compareTo(other) -> int
 *   toString()       -> String
 *   draw(x, y)
 *   getSeat()        -> int
 *
 */
public class Passenger implements Comparable<Passenger> {

    private String initials;
    private int cabin; // 1 is the first class, 3 is economy class (lowest priority)
    private int seat;        

    /**
     * Construct a new Passenger object
     *
     */
    public Passenger(int c, int s, String init) {
        cabin = c;
        seat = s;
        initials = init;
    }
        
    public int getSeat(){ return seat; }

    /**
     * Compare this Passenger with another Passenger to determine who should
     * board first.
     * A Passenger should be earlier in the ordering if they should board first.
     * The ordering depends on passenger's flight class type and the arrival time.
     */
    public int compareTo(Passenger other) {
        if (cabin<other.cabin){ return -1; }
        if (cabin>other.cabin){ return 1; }
        if (cabin==1) { return (other.seat-seat); }  // first class come out in reverse order of seat
        return (seat-other.seat);
    }
        
        
    /**
     * toString: Descriptive string of the information in the Passenger 
     */
    public String toString() {
        return initials+((cabin==1)?" (1st@":(cabin==2)?" (bus@":" (econ@")+seat+")";
    }

    /**
     * Draw the Passenger: 6 units wide, 28 units high x,y specifies center of the
     * base
     */
    public void draw(double x, double y) {
        UI.setColor((cabin==1)?Color.red:(cabin==2)?Color.orange:Color.green);
        UI.fillOval(x-3, y-28, 6, 8);
        UI.fillRect(x-3, y-20, 6, 20);
        UI.setColor(Color.black);
        UI.drawOval(x-3, y-28, 6, 8);
        UI.drawRect(x-3, y-20, 6, 20);
        UI.setFontSize(10);
        UI.drawString("" + initials.charAt(0), x-3, y-10);
        UI.drawString("" + initials.charAt(1), x-3, y-1);
    }


    /**
     * Construct a list of count passengers, with
     * 1 in 30 of the passengers being first class,
     * 2 in 30 being business class, and
     * the rest being economy class.
    */
    public static Set<Passenger> makePassengers (int count) {
        Set<Passenger> ans = new HashSet<Passenger>();
        int numFirst = count/10;
        int numBus = count/5;
        char init1 = 'A';
        char init2 = 'M';
        for (int seat=1; seat<=count; seat++){
            String inits = ""+init1 + init2;
            init1 = (char)((init1-'A'+5)%26 +'A');
            init2 = (char)((init2-'A'+11)%26 +'A');
            int cab = (seat<=numFirst)?1:(seat<=(numFirst+numBus))?2:3;
            ans.add(new Passenger(cab, seat, inits));
        }
        return  ans;
    }
            
            
    public static void main(String[] args){
        for(Passenger p : makePassengers(400)){
            System.out.println(p);
        }
    }
    




}
