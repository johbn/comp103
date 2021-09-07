// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 4
 * Name:
 * Username:
 * ID:
 */

import java.util.*;
import ecs100.UI;

/**
 * A Shuttle takes passengers to the terminal.
 *
 * Each shuttle has a an identifying number (0 to 3), which specifies the queue it goes to.
 *    relevant methods:  getNum()

 * It has a maximum number of passengers it can hold.
 *    relevant methods:  isEmpty()   isFull() 

 * It can be waiting or it can be on a trip to the terminal and back.
 *    relevant methods:  isWaiting()
 * 
 * If it is waiting, and not full, then you can add a passenger to it.
 *    relevant methods:  addPassenger(p)

 * You can make the shuttle start its trip at any time. 
 *    relevant methods:  startTrip()  advanceTrip()  
 */ 

public class Shuttle {
        
    public static final int MIN_TRIPTIME = 15;

    private final int number;
    private final int capacity;
    private Set<Passenger> passengers = new HashSet<Passenger>();
    private boolean waiting = true;
    private int tripCounter;
    private int tripTime = MIN_TRIPTIME;
        
    public Shuttle(int num, int cap){
            number = num;
        capacity = cap;
    }
    
    /**
     * Return the number of the shuttle
     */
    public int getNum() {
            return number;
    }
    
    /**
     * The shuttle is full
     */
    public boolean isFull(){
        return passengers.size()==capacity;
    }

/**
     * The shuttle is empty
     */
    public boolean isEmpty(){
        return passengers.isEmpty();
    }

    /**
     * The shuttle is waiting for passengers to get on
     */
    public boolean isWaiting() {
            return waiting;
    }

    /**
     * Add a passenger to the shuttle.
     */
    public void addPassenger(Passenger p){
        if (passengers.size() >= capacity){
            throw new RuntimeException("Shuttle "+number+" is full!");
        }
        passengers.add(p);
        tripTime += (1 + p.getSeat()%3);
    }

    /**
     * Make shuttle depart for the terminal
     * Trip time will depend partly on the passengers
     */
    public void startTrip(){
        waiting = false;
        tripCounter = 0;
    }

    /**
     * Advance trip by one time tick (if it is traveling)
     * If trip is now complete, reset the counters and start waiting for more passengers
     */
    public void advanceTrip(){
        if (waiting){throw new RuntimeException("Shuttle "+number+" is not travelling");}
        tripCounter++;
        if (tripCounter==tripTime){
            //reset
            waiting = true;
            tripCounter=0;
            tripTime = MIN_TRIPTIME;
            passengers.clear();
        }
    }

    
    /**
     * Draws the Shuttle:
     *   When in a trip, the y position is increased with the tripCounter,
     *   up to 200 at the halfway point, after which the passengers disappear,
     *   and the y position decreases again.
     */
    public void draw(double x, double y){
        UI.setFontSize(14);
        UI.drawString("Shuttle"+number, x, y-35);
        if (!waiting){
            double step = 200.0*2/tripTime;
            y += step*((tripCounter<= tripTime/2)?tripCounter:(tripTime-tripCounter));
        }
        UI.drawRect(x-5, y-30, capacity*10, 30);  // box to show total number of passengers
        if (waiting || (tripCounter<= tripTime/2)){
            for (Passenger p : passengers) {
                p.draw(x, y);
                x += 10;
            }
        }
    }

    

}
