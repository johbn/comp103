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
import java.nio.file.*;


/** Event, for recording the events in the simulation */

public class Event{

    final String type;
    final Passenger passenger;
    final int shuttleNum;

    public static final Set<String> types = Set.of("deplane","onShuttle","toTerminal");

    
    public Event(String t, Passenger p, int num){
        if (!types.contains(t)){throw new RuntimeException("Invalid Event type: "+t);}
        type = t;
        passenger = p;
        shuttleNum = num;
    }

    public Event(String t, int num){
        if (!t.equals("toTerminal")){throw new RuntimeException("Invalid Event type: "+t);}
        type = "toTerminal";
        passenger = null;
        shuttleNum = num;
    }


    public String toString(){
        if (type.equals("deplane")){
            return (passenger+" deplane to shuttle queue "+shuttleNum);
        }
        if (type.equals("onShuttle")){
            return (passenger+" got on shuttle "+shuttleNum);
        }
        if (type.equals("toTerminal")){
            return ("shuttle "+shuttleNum+" left for terminal");
        }
        return "Unknown movement with "+ passenger +" and "+shuttleNum;
    }

}
