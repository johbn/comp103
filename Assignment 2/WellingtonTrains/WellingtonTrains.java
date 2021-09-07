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
import java.util.Map.Entry;
import java.io.*;
import java.nio.file.*;

/**
 * WellingtonTrains
 * A program to answer queries about Wellington train lines and timetables for
 *  the train services on those train lines.
 *
 * See the assignment page for a description of the program and what you have to do.
 */

public class WellingtonTrains{
    //Fields to store the collections of Stations and Lines
    HashMap <String,Station> stationSet = new HashMap<>();
    HashMap <String,TrainLine> trainLineSet = new HashMap <>();
    HashMap <String,TrainService> trainServiceSet = new HashMap <>();

    // Fields for the suggested GUI.
    private String stationName;        // station to get info about, or to start journey from
    private String lineName;           // train line to get info about.
    private String destinationName;
    private int startTime = 0;         // time for enquiring about

    /**
     * main method:  load the data and set up the user interface
     */
    public static void main(String[] args){
        WellingtonTrains wel = new WellingtonTrains();
        wel.loadData();   // load all the data
        wel.setupGUI();   // set up the interface
    }

    /**
     * Load data files
     */
    public void loadData(){
        loadStationData();
        UI.println("Loaded Stations");
        loadTrainLineData();
        UI.println("Loaded Train Lines");
        UI.println("Loaded Train Services");
    }

    /**
     * User interface has buttons for the queries and text fields to enter stations and train line
     * You will need to implement the methods here.
     */
    public void setupGUI(){
        UI.addButton("All Stations",        this::listAllStations);
        UI.addButton("Stations by name",    this::listStationsByName);
        UI.addButton("All Lines",           this::listAllTrainLines);
        UI.addTextField("Station",          (String name) -> {this.stationName=name;});
        UI.addTextField("Train Line",       (String name) -> {this.lineName=name;});
        UI.addTextField("Destination",      (String name) -> {this.destinationName=name;});
        UI.addTextField("Time (24hr)",      (String time) ->
            {try{this.startTime=Integer.parseInt(time);}catch(Exception e){UI.println("Enter four digits");}});
        UI.addButton("Lines of Station",    () -> {listLinesOfStation(this.stationName);});
        UI.addButton("Stations on Line",    () -> {listStationsOnLine(this.lineName);});
        UI.addButton("Stations connected?", () -> {checkConnected(this.stationName, this.destinationName);});
        UI.addButton("Next Services",       () -> {findNextServices(this.stationName, this.startTime);});
        UI.addButton("Find Trip",           () -> {findTrip(this.stationName, this.destinationName, this.startTime);});

        UI.addButton("Quit", UI::quit);
        UI.setMouseListener(this::doMouse);

        UI.setWindowSize(900, 400);
        UI.setDivider(0.2);

        UI.drawImage("data/geographic-map.png", 0, 0);
        UI.drawString("Click to list closest stations", 2, 12);

    }

    public void doMouse(String action, double x, double y){
        if (action.equals("released")){
            UI.clearText();
            TreeMap <Double,String> tempStationsDistanceTreemap = new TreeMap<>(); // Tree map so it sorts on dist
            Station copyOfClosestStation = stationSet.get(0);
            double currentClosestStationDist = 1000000;
            Collection<Station> values = stationSet.values();
            for(Station tempStation :values){ // for all stations (Finds closest station to mouse click)           
                // easy math
                double distanceFromMouseToStation = Math.sqrt((x - tempStation.getXCoord())*(x - tempStation.getXCoord()) + (y - tempStation.getYCoord())*(y - tempStation.getYCoord()));
                if(distanceFromMouseToStation <  currentClosestStationDist){
                    currentClosestStationDist = distanceFromMouseToStation; // update closestt distance
                    copyOfClosestStation = tempStation; // if closer become new closest station
                }
            }
            UI.println("Printing Ten Closest Stations To " + copyOfClosestStation.getName()); // prints the closesst station
            UI.println("--------------------------------------------");
            for(Station stationI : values){ // makes treemap (sorted by distance) of all the values 
                if(!stationI.equals(copyOfClosestStation)){ // as long as it isnt comparing to itself
                    tempStationsDistanceTreemap.put(Math.sqrt((stationI.getXCoord() - copyOfClosestStation.getXCoord())*(stationI.getXCoord() - copyOfClosestStation.getXCoord()) + (stationI.getYCoord() - copyOfClosestStation.getYCoord())*(stationI.getYCoord() - copyOfClosestStation.getYCoord())),stationI.getName());
                    // gets distance from station to the current station
                }
            }   
            int breakAfterX = 0;
            for(double currentDistance : tempStationsDistanceTreemap.keySet()){ // for all the keys (Distance)               
                UI.println("Distance from " + tempStationsDistanceTreemap.get(currentDistance) + " to " + copyOfClosestStation.getName() + ": " +  Math.round(currentDistance));
                if(breakAfterX >= 9){ // lists only 10
                    break;
                }
                breakAfterX++; // incremenet count
            }
        }
    }

    public int getNextTime(TrainLine tempTrainLine,Station stationName,int startTime){
        int stationIndex = tempTrainLine.getStations().indexOf(stationName);     
        for(TrainService tempTrainService : tempTrainLine.getTrainServices()){
            if(startTime < tempTrainService.getTimes().get(stationIndex)){               
                return tempTrainService.getTimes().get(stationIndex);
            }
        }
        return -1;
    }

    // Methods for loading data and answering queries
    void loadStationData(){
        Path filePath = Path.of("data/stations.data");
        try{
            List <String> allLines = Files.readAllLines(filePath);
            for(String currentLine : allLines){
                Scanner scan = new Scanner(currentLine);
                String tempStationName = scan.next();
                int tempZoneID = scan.nextInt();
                double xPos = scan.nextDouble();
                double yPos = scan.nextDouble();       
                Station tempStation = new Station(tempStationName,tempZoneID,xPos,yPos);
                stationSet.put(tempStationName.toUpperCase(),tempStation);
            }
        } catch(IOException e){ //print error if file couldn't be loaded
            UI.println("Error Invalid File");
        }
    }

    void loadTrainLineData(){
        Path filePath = Path.of("data/train-lines.data");
        try{
            List <String> allLines = Files.readAllLines(filePath);
            for(String currentLine : allLines){ // for all lines of Trainline file
                Scanner scan = new Scanner(currentLine);
                String tempLineName = scan.next(); // Get line Name
                trainLineSet.put(tempLineName.toUpperCase(),(new TrainLine(tempLineName))); // add line as new trainline                       
                loadTrainLineStations(tempLineName.toUpperCase());
                loadTrainServicesData(tempLineName.toUpperCase());
            }
        } catch(IOException e){ //print error if file couldn't be loaded
            UI.println("Error Invalid File");
        }
    }

    void loadTrainServicesData(String tempLineName){
        try{
            Path filePathOfTrainLineServices = Path.of("data/"+tempLineName+"-services.data");  
            List <String> allServicesOfLine = Files.readAllLines(filePathOfTrainLineServices); // read all lines of that path

            for(String currentTrainLineServiceTimeLine : allServicesOfLine){
                Scanner scanningInServices = new Scanner(currentTrainLineServiceTimeLine);
                TrainService tempTrainService = new TrainService (trainLineSet.get(tempLineName));
                trainServiceSet.put(tempLineName,tempTrainService); // put the service in the list of services
                while(scanningInServices.hasNextInt()){ // for all services on that line
                    int currentScannedTime = scanningInServices.nextInt();
                    trainServiceSet.get(tempLineName).addTime(currentScannedTime);
                }
                trainLineSet.get(tempLineName).addTrainService(trainServiceSet.get(tempLineName)); // add train time to that Service
            }

        }catch(IOException e){ //print error if file couldn't be loaded
            UI.println("Error Invalid File");
        }
    }

    void loadTrainLineStations(String tempLineName){
        // got rid of a to upper case templine here
        try{
            Path filePathOfTrainLineStations = Path.of("data/"+tempLineName+"-stations.data"); // get path of that TrainLines Stations     
            List <String> allLinesOfStations = Files.readAllLines(filePathOfTrainLineStations); // read all lines of that path
            for(String currentTrainLineStationName : allLinesOfStations){ // for all Stations
                Scanner scanningInStations = new Scanner(currentTrainLineStationName); //create new scanner
                String currentScannedStation = scanningInStations.next(); // scan the station name
                trainLineSet.get(tempLineName).addStation(stationSet.get(currentScannedStation.toUpperCase()));  // add Station to that stationLine
                stationSet.get(currentScannedStation.toUpperCase()).addTrainLine(trainLineSet.get(tempLineName)); // add trainline to that station
            }
        } catch(IOException e){ //print error if file couldn't be loaded
            UI.println("Error Invalid File");
        }
    }

    /***********************QUEIRIES***************/
    void listAllStations(){ //List all Stations
        UI.clearText();
        Collection<Station> values = stationSet.values(); // gets all map values
        HashSet <Station> stationHashSet = new HashSet <Station>(values); // converts map values to hashset for displaying
        for(Station TempStation: stationHashSet){
            UI.println(TempStation.toString());
        }

    }

    void listStationsByName(){// List all Stations Alphabetically
        UI.clearText();
        Collection<Station> values = stationSet.values();  // gets all values of hashmap
        ArrayList <Station> alphabeticSet = new ArrayList <Station>(values); // converts values to array list
        Collections.sort(alphabeticSet);  // sorts arraylist
        for(Station TempStation: alphabeticSet){
            UI.println(TempStation.toString());
        }
    }

    void listAllTrainLines(){// List all Stations Alphabetically
        UI.clearText();
        Collection<TrainLine> values = trainLineSet.values(); // gets all map values
        HashSet <TrainLine> trainLineHashSet = new HashSet <TrainLine>(values);
        for(TrainLine tempTrainLine: trainLineHashSet){ // for each lines
            UI.println(tempTrainLine.toString()); // println
        }
    }

    void listLinesOfStation(String stationName){// Lists all train lines that go through given station
        UI.clearText();
        if(stationName != null){
            stationName = stationName.toUpperCase();
            boolean noneFound = true; 
            if(stationSet.containsKey(stationName)){
                noneFound = false; // one was found
                Set <TrainLine> tempTrainLinesList = stationSet.get(stationName).getTrainLines();          
                for(TrainLine tempTrainLine : tempTrainLinesList){
                    UI.println(tempTrainLine.getName()); // that means the station in that line
                }
            }
            if(noneFound){
                UI.println("No lines found, check spelling of Station Name");
            }
        }
        else{
            UI.println("Please enter a station name");
        }
    }

    void listStationsOnLine(String lineName){// Lists all train stations that go through given line
        UI.clearText();
        if(lineName != null){           
            boolean noneFound = true;
            lineName = lineName.toUpperCase();
            if(trainLineSet.containsKey(lineName)){ // if the linename is an actual key
                noneFound = false;
                List <Station> tempStationsList = trainLineSet.get(lineName).getStations(); // get list of all stations on line    
                for(Station tempStation : tempStationsList){ // for all the stations; print them
                    UI.println(tempStation.getName()); // println
                }
            }
            if(noneFound){
                UI.println("No Stations found, check spelling of Line Name");
            }
        }
        else{
            UI.println("Please enter a Line name");
        }
    }

    void checkConnected(String startingStation, String DestinationStation){ // checks if stations are connected
        UI.clearText();
        if(startingStation != null && DestinationStation != null){
            boolean routeNotFound = true;
            Collection <TrainLine> values = trainLineSet.values(); // gets all map values
            HashSet <TrainLine> trainLineHashSet = new HashSet <TrainLine>(values); // converts to a hashset
            for(TrainLine tempTrainLine: trainLineHashSet){ // for all trainlines
                List <Station> listOfStationsOnTrainLine = tempTrainLine.getStations(); // get all stations
                List <String> stationNames = new ArrayList <String>();
                for(Station tempStation : listOfStationsOnTrainLine){ // store all names of stations in an arraylist but capitalized
                    stationNames.add(tempStation.getName().toUpperCase());
                }
                /*
                 * These two if loops check first if the list of names for that line contains both the starting and end station
                 */
                if((stationNames.contains(startingStation.toUpperCase())) && (stationNames.contains(DestinationStation.toUpperCase()))){
                    if(stationNames.indexOf(startingStation.toUpperCase()) < stationNames.indexOf(DestinationStation.toUpperCase())){            
                        UI.println(tempTrainLine);
                        routeNotFound = false;
                    }
                }
                stationNames = new ArrayList <String>();//clears all names of stations
            }
            if(routeNotFound){
                UI.println("Route not found,If a route does exist please check spelling");
            }
        }
        else{
            UI.println("Please enter both a Starting and Destination station");
        }
    }

    void findNextServices(String stationName, int startTime){
        UI.clearText();
        boolean noneFound = true; 
        if(stationName != null){
            stationName = stationName.toUpperCase();
            if(stationSet.containsKey(stationName)){ // if exsits within set
                Set <TrainLine> tempTrainLinesList = stationSet.get(stationName).getTrainLines();     
                for(TrainLine tempTrainLine : tempTrainLinesList){

                    int time = getNextTime(tempTrainLine,stationSet.get(stationName),startTime); // gets next time
                    if(time != -1){ // if time was found
                        UI.println("Next Service For: " + tempTrainLine.getName() + " Is at :" + time); 
                        noneFound = false;  // so println doesnt run                  
                    }
                }                  
            }
            if(noneFound){
                UI.println("No Services found, check spelling of Station Name / Or Service may not exist");
            }
        }
        else{
            UI.println("Please enter a station name");
        }
    }

    void findTrip(String startingStation, String DestinationStation, int startTime){
        UI.clearText();
        if(startingStation != null && DestinationStation != null){ //if not empty
            startingStation = startingStation.toUpperCase();
            boolean noneFoundTime = true; // set to none found for println
            if(stationSet.containsKey(startingStation)){ // if the station exists
                Set <TrainLine> tempTrainLinesList = stationSet.get(startingStation).getTrainLines();          
                for(TrainLine tempTrainLine : tempTrainLinesList){
                    List <Integer> listOfTimesPerStation = trainServiceSet.get(tempTrainLine.getName().toUpperCase()).getTimes();
                    List <Station> listOfStationsOnTrainLine = tempTrainLine.getStations(); // get all stations
                    List <String> stationNames = new ArrayList <String>();
                    for(Station tempStation : listOfStationsOnTrainLine){ // store all names of stations in an arraylist but capitalized
                        stationNames.add(tempStation.getName().toUpperCase());
                    }
                    //if the stationNames contains both starting and destination Station
                    // AND if the destination comes after the starting station
                    if((stationNames.contains(startingStation.toUpperCase())) && (stationNames.contains(DestinationStation.toUpperCase()))){
                        if(stationNames.indexOf(startingStation.toUpperCase()) < stationNames.indexOf(DestinationStation.toUpperCase())){      
                            int time = getNextTime(tempTrainLine,stationSet.get(startingStation),startTime); // runs get time method
                            if(time != -1){
                                UI.println("Next Service For: " + tempTrainLine.getName() + " Departs from " +  startingStation + " (Zone:" + stationSet.get(startingStation).getZone() + ") at :" + time);
                                UI.println("Arrives at: " + DestinationStation.toUpperCase() + " (Zone:" + stationSet.get(DestinationStation.toUpperCase()).getZone()+")"); //
                                noneFoundTime = false;
                            }
                        }
                    }            
                }
            }
            if(noneFoundTime){
                UI.println("No Services found, check spelling of Station Name");
            }
        }
        else{
            UI.println("Please enter a station name");
        }

    }

}