// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 4
 * Name: Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

import java.util.*;
import ecs100.UI;

/**
 * Simple simulation of unloading passengers from a plane on the airport 
 * tarmac (not at a gate), and taking the passengers to the terminal by
 * a series of shuttles.
 *
 * There are four shuttles (numbered 0 to 3) going back and forward to the terminal. 
 * Each shuttle has a maximum capacity (ranging from 3 to 8).
 * 
 * For each shuttle, there is a small area on the tarmac 
 * where up to 6 passengers can line up between the barriers.
 * Shuttles will take their passengers off to the terminal whenever
 * they are full, or when there are no passengers left in their queue.
 * 
 * Passengers on the plane are let off in priority order, with
 *  the first-class passengers first,
 *  the business-class passengers next, and then
 *  the economy-class passengers. 
 * Within each class, the priority is governed by their seat number.
 * 
 * For safety reasons, passengers can only be let off the plane when
 * there is space for them in a shuttle waiting area.
 * Until there is space, they have to wait on the plane.
 * Passengers always go to the lowest number queue that has space.

 * The simulation should start by initialising all the queues, and
 *  putting all the passengers onto the plane.
 * Then, each time tick the simulation will:
 *   - Check each shuttle
 *       if it is waiting and full: then start the trip
 *       if it is waiting and not empty, but its queue is empty: then start the trip
 *       if it is waiting and not full and there is a passenger in its queue:
 *              put the passenger on the shuttle
 *       if it is not waiting (ie going to the terminal): advance the trip by one tick.
 *   - Let one passenger off the plane (in priority order), into the
 *     first shuttle queue that has space (if any of them have space)
 * The process repeats until all the passengers have gone to the terminal.
 * 
 * The simulation must create and return a list of every event in the simulation
 * There are three kinds of events to record:
 *    new Event("deplane", p, n)       [passenger p got off the plane to the n'th shuttle queue]
 *    new Event("onShuttle", p, n)     [passenger p got on shuttle n]
 *    new Event("toTerminal", n)       [shuttle n left for the terminal]
 * Your code should add all the Events to the record list.
 */

public class TarmacShuttle {
    public static final int SHUTTLE_QUEUE_MAX = 6;  // max number of passengers in a shuttle queue

    private boolean running = false;
    private int time = 0; // The simulated time - the current "tick"
    private int numPassengers = 100;
    private int delay = 500; // milliseconds of real time for each tick

    /**
     * Run the simulation.
     * The first argument is a set of passengers that start off on the plane.
     */
    public List<Event> run(Set<Passenger> passengers) {
        // The record of all passenger movements
        List<Event> record = new ArrayList<Event>();

        // the four shuttles, with their number and their capacity
        Set<Shuttle> shuttles = Set.of(
                new Shuttle(0, 3),
                new Shuttle(1, 4),
                new Shuttle(2, 6),
                new Shuttle(3, 8)
            );

        // Initialise the Queues for the plane and the four shuttle queues.
        // and fill the plane with the passengers (from the Set of passengers).
        PriorityQueue <Passenger> plane = new PriorityQueue <Passenger> (passengers);
        List <Queue <Passenger>> shuttleQueues =  List.of(
                new ArrayDeque <Passenger>(),
                new ArrayDeque <Passenger>(),
                new ArrayDeque <Passenger>(),
                new ArrayDeque <Passenger>()                
            );

        
        // loop
        while(running) {
            // Process each shuttle and its queue. See details above.
            // Record the passenger movements
            /**IF FULL AND WAITING**/
            for(Shuttle currentShuttle :shuttles){
                if(currentShuttle.isWaiting() && currentShuttle.isFull()){
                    currentShuttle.startTrip();
                    record.add(new Event("toTerminal", currentShuttle.getNum()));
                }
            }

            /**IF WATING, NOT EMPTY, BUT QUE EMPTY**/
            for(Shuttle currentShuttle :shuttles){
                if(currentShuttle.isWaiting() &&  !currentShuttle.isEmpty() && shuttleQueues.get(currentShuttle.getNum()).isEmpty()){
                    currentShuttle.startTrip();
                    record.add(new Event("toTerminal", currentShuttle.getNum()));
                }
            }

            /** IF WAITING AND NOT FULL AND PASSENGER IN QUE**/
            for(Shuttle currentShuttle :shuttles){
                if(currentShuttle.isWaiting() &&  !currentShuttle.isFull() && !shuttleQueues.get(currentShuttle.getNum()).isEmpty()){
                    Passenger tempPassenger = shuttleQueues.get(currentShuttle.getNum()).poll(); // get from the shuttles que
                    currentShuttle.addPassenger(tempPassenger);
                    record.add(new Event("onShuttle", tempPassenger, currentShuttle.getNum())); // index of to find which que it was
                }
            }

            
            /**IF IN TRANSIT**/
            for(Shuttle currentShuttle :shuttles){
                if(!currentShuttle.isWaiting()){ // if not waiting means its moving
                    currentShuttle.advanceTrip();                    
                }
            }

            // Move a passenger from the plane onto the first shuttle queue with
            // space, if there is one.
            // Record the passenger movement
            /**PLANE TO QUE**/
            for(Queue <Passenger> currentQueue : shuttleQueues){ 
                if(currentQueue.size() < SHUTTLE_QUEUE_MAX && !plane.isEmpty()){ // if room on shuttle and plane isnt empty
                    Passenger tempPassenger = plane.poll(); // polled seperatly so can be added in two places
                    currentQueue.add(tempPassenger); // add to the current que
                    record.add(new Event("deplane", tempPassenger, shuttleQueues.indexOf(currentQueue))); // index of to find which que it was
                    break; // break once one passenger has been moved,
                }
            }

            redraw(plane, shuttleQueues, shuttles);
            // Break if simulation is finished -
            // all the queues are empty.
            
            running = false; // set to false to stop simulation
            for(Shuttle currentShuttle :shuttles){
                if(!currentShuttle.isEmpty() || !shuttleQueues.get(currentShuttle.getNum()).isEmpty()){
                    running = true; // if there exists such a que that is not empty or a que that is not empty then simulation is still running
                }           
            }

            UI.sleep(delay);
        }
        return record;    // Do not remove:  Essential for the marking
    }

    /**
     * Construct a new object, setting up the GUI, and resetting
     */
    public static void main(String[] arguments) {
        TarmacShuttle q= new TarmacShuttle();
        q.setupGUI();
    }

    /**
     * Set up the GUI: buttons to control simulation and sliders for setting parameters
     */
    public void setupGUI() {
        UI.addButton("Test run", this::testRun);
        UI.addSlider("Num Passengers", 5, 500, 100, (double v) -> {numPassengers = (int)v;});
        UI.addSlider("Delay", 1, 1000, 500, (double v) -> {delay = (int)v;});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000, 600);
        UI.setDivider(0);
    }

    public void testRun(){
        if (! running) {
            running = true;
            List<Event> events =  run(Passenger.makePassengers(numPassengers));
            running = false;
            UI.clearText();
            for(Event ev : events){ UI.println(ev); }
        }
    }

    /** redraw the state of the simulation */
    public void redraw(Queue<Passenger> plane, List<Queue<Passenger>> shuttleQueues, Set<Shuttle> shuttles) {
        UI.clearGraphics();
        UI.setFontSize(14);

        // The Plane

        double y = 50;
        double x = 10;
        UI.drawString("Plane Passengers", x, y-35);
        for(Passenger p: plane) {
            p.draw(x, y);
            x += 10;
        }
        UI.drawLine(0, y+2, 600, y+2);

        // Shuttle queues
        y = 100;
        for(int n=0; n<shuttleQueues.size(); n++){
            Queue<Passenger> sq = shuttleQueues.get(n);
            UI.setFontSize(14);
            x = 10+n*100;
            UI.drawString("Queue "+n, x, y-35);
            UI.drawRect(x-5, y-30, SHUTTLE_QUEUE_MAX*10, 30); 
            for (Passenger p : sq) {
                p.draw(x, y);
                x += 10;
            }
        }
        UI.drawLine(0, y + 2, 450, y + 2);

        // Shuttles
        y = 150;
        for(Shuttle shuttle : shuttles){
            shuttle.draw(10+shuttle.getNum()*100, y);
        }
        UI.drawLine(0, y + 2, 450, y + 2);
        y = 350;
        UI.drawLine(0, y + 2, 450, y + 2);
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
        List<Event> events =  run(new HashSet<Passenger>());
    }

}
