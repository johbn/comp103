// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 3
 * Name:Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

import ecs100.*;
import java.util.*;
//import java.io.*;

/**
 * A treatment Department (Surgery, X-ray room,  ER, Ultrasound, etc)
 * Each department will needp
 * - A name,
 * - A maximum number of patients that can be treated at the same time
 * - A Set of Patients that are currently being treated
 * - A Queue of Patients waiting to be treated.
 *    (ordinary queue, or priority queue, depending on argument to constructor)
 */

public class Department{

    private String name;
    private int maxPatients;
    private Queue<Patient> waitingRoom;   
    private Set <Patient> treatmentRoom;

    private Set <Patient> allFinishedPatients = new HashSet <Patient>();

    public Department(String departmentName,int MAXPATIENTS,boolean priority){      
        this.name = departmentName;
        this.maxPatients = MAXPATIENTS;   
        if(priority){ // if priority que 
            this.waitingRoom = new PriorityQueue<Patient>();
        } 
        else{
            this.waitingRoom = new ArrayDeque<Patient>();
        }    

        this.treatmentRoom = new HashSet<Patient>();   
        this.allFinishedPatients = new HashSet <Patient>(); // all patients who finished in that department (used for stats)
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getAmmountInQue (){    
        return this.waitingRoom.size();
    }
    
        public int getMaxPatients (){    
        return this.maxPatients;
    }
    
    public int howManyCurrentlyTreated (){    
        return this.treatmentRoom.size();
    }

    public void incrementTicks(){       //increment all patients
        for(Patient currentPatient : this.treatmentRoom){
            currentPatient.advanceTreatmentByTick(); 
        }

        for(Patient currentPatient : this.waitingRoom){
            currentPatient.waitForATick(); 
        }
    }

    public Set checkForFinishedTreatment(){
        Set <Patient> tempSet = new HashSet <Patient>();     
        Set <Patient> finishedCurrentTreatmentPatients = new HashSet <Patient>();     
        for(Patient currentPatient : this.treatmentRoom){      // for every patient in the treatment            
            if(currentPatient.completedCurrentTreatment()){ // if this patient has finished current 
                if(currentPatient.noMoreTreatments()){ // and if no more
                    tempSet.add(currentPatient); // remove them 
                    this.allFinishedPatients.add(currentPatient); // add them for getting data latter
                }
                else{
                    currentPatient.incrementTreatmentNumber(); // increment number
                    if(!currentPatient.noMoreTreatments()){ // if no more 
                        finishedCurrentTreatmentPatients.add(currentPatient); //add them for returning
                        this.allFinishedPatients.add(currentPatient); // add them for data collection
                    }
                    tempSet.add(currentPatient); // add for removal
                }
            }
        }        
        this.treatmentRoom.removeAll(tempSet); // remove all finished patients  

        return finishedCurrentTreatmentPatients; // return patients who need more treatments
    }

    public Set <Patient> returnAllFinishedPatients(){ // returns all finsihed patients
        return this.allFinishedPatients;
    }

    public void checkForMorePatients (){
        if(this.treatmentRoom.size() < this.maxPatients && this.waitingRoom.size() > 0){ // if patient to go and treatment room isnt full
            this.treatmentRoom.add(this.waitingRoom.poll()); // grab that person
        }
    }

    public void addPatientToQue(Patient patient){ // adds person to waiting room
        this.waitingRoom.offer(patient);
    }    

    /**
     * Draw the department: the patients being treated and the patients waiting
     * You may need to change the names if your fields had different names
     */
    public void redraw(double y){

        UI.setFontSize(14);
        UI.drawString("Treating Patients", 5, 10);
        UI.drawString("Waiting Queues", 200, 10);
        UI.drawLine(0,30*(y/60),400, 30*(y/60));
        UI.drawLine(0,30*(y/60)*2,400, 30*(y/60)*2);

        UI.drawString(this.name, 0, y-35);
        double x = 10;
        UI.drawRect(x-5, y-30, this.maxPatients * 10, 30);  // box to show max number of patients
        for(Patient p : this.treatmentRoom){
            p.redraw(x, y);
            x += 10;
        }
        x = 200;
        for(Patient p : this.waitingRoom){
            p.redraw(x, y);
            x += 10;
        }
    }

}
