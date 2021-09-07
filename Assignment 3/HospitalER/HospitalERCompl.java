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
import java.io.*;
import java.util.Map.Entry;
import java.awt.Color;

/**
 * Simulation of a Hospital ER
 * 
 * The hospital has a collection of Departments, including the ER department, each of which has
 *  and a treatment room.
 * 
 * When patients arrive at the hospital, they are immediately assessed by the
 *  triage team who determine the priority of the patient and (unrealistically) a sequence of treatments 
 *  that the patient will need.
 *
 * The simulation should move patients through the departments for each of the required treatments,
 * finally discharging patients when they have completed their final treatment.
 *
 *  READ THE ASSIGNMENT PAGE!
 */

public class HospitalERCompl{

    // Fields for recording the patients waiting in the waiting room and being treated in the treatment room
    private HashMap <String,Department> departmentsMap = new HashMap <String,Department>();
    private static final int MAX_PATIENTS = 5;   // max number of patients currently being treated

    // fields for the statistics
    public int numPatientsTreated = 0;
    public int numPriOneTreated = 0;
    public int totalWaitTime = 0;
    public int totalWaitTimePriOne = 0;
    boolean advancedStats = false;

    // Fields for the simulation
    private boolean running = false;
    private int time = 0; // The simulated time - the current "tick"
    private int delay = 300;  // milliseconds of real time for each tick

    // fields controlling the probabilities.
    private int arrivalInterval = 5;   // new patient every 5 ticks, on average
    private double probPri1 = 0.1; // 10% priority 1 patients
    private double probPri2 = 0.2; // 20% priority 2 patients
    private Random random = new Random();  // The random number generator.

    /**
     * Construct a new HospitalERCore object, setting up the GUI, and resetting
     */
    public static void main(String[] arguments){
        HospitalERCompl hospital = new HospitalERCompl();
        hospital.reset(false);   // initialise with an ordinary queue.
        hospital.setupGUI();

    }       

    public void createDepartments(boolean usePriorityQueue){ // creates the departments
        departmentsMap = new HashMap <String,Department>();
        //ER, MRI, Surgery, X-ray Ultrasound
        Department erDepartmnet = new Department("ER",MAX_PATIENTS,usePriorityQueue);
        Department mriDepartmnet = new Department("MRI",MAX_PATIENTS,usePriorityQueue);
        Department surgeryDepartmnet = new Department("Surgery",MAX_PATIENTS,usePriorityQueue);
        Department XrayDepartmnet = new Department("X-Ray",MAX_PATIENTS,usePriorityQueue);
        Department ultraSoundDepartmnet = new Department("Ultrasound",MAX_PATIENTS,usePriorityQueue);        

        departmentsMap.put("ER", erDepartmnet);
        departmentsMap.put("MRI", mriDepartmnet);
        departmentsMap.put("Surgery", surgeryDepartmnet);
        departmentsMap.put("X-ray", XrayDepartmnet);
        departmentsMap.put("Ultrasound", ultraSoundDepartmnet);

    }

    /**
     * Set up the GUI: buttons to control simulation and sliders for setting parameters
     */
    public void setupGUI(){
        UI.addButton("Toggle Advanced Statistics (CHALLANGE)", () -> {advancedStats = !advancedStats;});
        UI.addButton("Reset (Queue)", () -> {this.reset(false); });
        UI.addButton("Reset (Pri Queue)", () -> {this.reset(true);});
        UI.addButton("Start", ()->{if (!running){ run(); }});   //don't start if already running!advancedStats
        UI.addButton("Pause & Report", ()->{running=false;});
        UI.addSlider("Speed", 1, 400, (401-delay),
            (double val)-> {delay = (int)(401-val);});
        UI.addSlider("Av arrival interval", 1, 50, arrivalInterval,
            (double val)-> {arrivalInterval = (int)val;});
        UI.addSlider("Prob of Pri 1", 1, 100, probPri1*100,
            (double val)-> {probPri1 = val/100;});
        UI.addSlider("Prob of Pri 2", 1, 100, probPri2*100,
            (double val)-> {probPri2 = Math.min(val/100,1-probPri1);});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
    }

    /**
     * Reset the simulation:
     *  stop any running simulation,
     *  reset the waiting and treatment rooms
     *  reset the statistics.
     */
    public void reset(boolean usePriorityQueue){
        running=false;
        UI.sleep(2*delay);  // to make sure that any running simulation has stopped
        time = 0;           // set the "tick" to zero.

        // reset the waiting room, the treatment room, and the statistics.
        createDepartments(usePriorityQueue);
        //treatmentRoom = new HashSet<Patient>();

        /*Statistics*/ 
        numPatientsTreated = 0;
        numPriOneTreated = 0;
        totalWaitTime = 0;
        totalWaitTimePriOne = 0;

        UI.clearGraphics();
        UI.clearText();
    }

    /**
     * Main loop of the simulation
     */
    public void run(){
        if (running) { return; } // don't start simulation if already running one!
        running = true;
        while (running){         // each time step, check whether the simulation should pause.

            // Hint: if you are stepping through a set, you can't remove
            //   items from the set inside the loop!
            //   If you need to remove items, you can add the items to a
            //   temporary list, and after the loop is done, remove all 
            //   the items on the temporary list from the set.

            /**move time along**/
            time += 1;
            Collection <Department> values = departmentsMap.values();       // get all departments  

            for(Department currentDepartment : values){ // for all departments 
                Set <Patient> finishedPatients = currentDepartment.checkForFinishedTreatment();  // get all patients who have finsihed
                for(Patient tempPatient : finishedPatients){
                    departmentsMap.get(tempPatient.getCurrentTreatment()).addPatientToQue(tempPatient);     // reasign all patients who ahve finished            
                }
            }

            for(Department currentDepartment : values){ // for all departments
                currentDepartment.incrementTicks();                //increment each patient
            }           

            for(Department currentDepartment : values){// 
                currentDepartment.checkForMorePatients();   //check waiting rooms for patients          
            }

            // Gets any new patient that has arrived and adds them to the waiting room
            if (time==1 || Math.random()<1.0/arrivalInterval){
                Patient newPatient = new Patient(time, randomPriority());
                UI.println(time+ ": Arrived: "+newPatient);
                departmentsMap.get(newPatient.getCurrentTreatment()).addPatientToQue(newPatient); // adds to correct que
            }

            int y = 1;                
            for(Department currentDepartment : values){
                currentDepartment.redraw(y*60);             
                y++;
            }

            UI.sleep(delay);
        }
        // paused, so report current statistics
        reportStatistics();
    }

    // Additional methods used by run() (You can define more of your own)

    /**
     * Report summary statistics about all the patients that have been discharged.
     * (Doesn't include information about the patients currently waiting or being treated)
     * The run method should have been recording various statistics during the simulation.
     */
    public void reportStatistics(){
        UI.println("/***************************************/");

        numPatientsTreated = getTotalTreated();
        totalWaitTime = getTotalWaitTime();
        numPriOneTreated = getPrioOneTreated();
        totalWaitTimePriOne = getPrioOneWaitTime();

       
        if(numPatientsTreated > 0){ // avoid / 0 error
            UI.println("Total Patients Processed: " + numPatientsTreated);
            UI.println("Average Waiting Time: " + totalWaitTime/numPatientsTreated); // total time / all patientce = average
        }                
        else{
            UI.println("No patients proccessed yet");
        }
        if(numPriOneTreated > 0){ // Avoid  / 0 error
            UI.println("Total Priority one patients Processed: " + numPriOneTreated);
            UI.println("Average Waiting Time for priority one: " + totalWaitTimePriOne/numPriOneTreated); // total time / all patientce = average
        }
        else{
            UI.println("No priority one patients proccessed yet");
        }

        if(advancedStats){
            UI.clearGraphics();
            
            Collection <Department> values = departmentsMap.values();        
            int y = 1;                
            for(Department currentDepartment : values){ // redraw after clear so graphs look clean and dont have leftover text
                currentDepartment.redraw(y*60);             
                y++;
            }
            
            for(Department currentDepartment : values){ // prints out per department statistics
                UI.println("/*********************************************/");
                UI.println("Department: "+currentDepartment.getName());
                UI.println("Ammount in Que: "+currentDepartment.getAmmountInQue());
                UI.println("Ammount in Treatment room: "+currentDepartment.howManyCurrentlyTreated());
                UI.println("Treatment room Size: "+currentDepartment.getMaxPatients());
            }
            // Graphs:
            UI.drawLine(10, 320, 10, 500); // vertical
            UI.drawLine(10, 500, 1000, 500);// horizontal
            UI.setColor(Color.BLUE);
            UI.setFontSize(10);
            if(numPatientsTreated < 200){
                UI.drawString("Total Patients Treated = " + numPatientsTreated, 50, 500-numPatientsTreated - 20);      
                UI.fillRect(10,500- numPatientsTreated, 20,  numPatientsTreated); // Total num
            }
            else{
                UI.drawString("Total Patients Treated > 200", 50, 320);      
                UI.fillRect(10,300, 20,200); // Total num
            }
            UI.setColor(Color.RED);
            if(numPriOneTreated < 200){
                UI.drawString("Total PrioOne Patients Treated = " + numPriOneTreated, 290, 500-numPriOneTreated - 20);      
                UI.fillRect(250,500- numPriOneTreated, 20,  numPriOneTreated); // Total num
            }
            else{
                UI.drawString("Total PrioOne Treated > 200", 290, 320);      
                UI.fillRect(250,300, 20,200); // Total num
            }
            UI.setColor(Color.BLUE);
            if(numPatientsTreated > 0){
                if(totalWaitTime/numPatientsTreated < 200){
                    UI.drawString("Average WaitTime Patients (Seconds)  = " + totalWaitTime/numPatientsTreated, 520, 500-totalWaitTime/numPatientsTreated - 20);      
                    UI.fillRect(500,500- totalWaitTime/numPatientsTreated, 20,  totalWaitTime/numPatientsTreated); // Total num
                }
                else{
                    UI.drawString("Average WaitTime Patients (Seconds) > 200", 520, 320);      
                    UI.fillRect(500,300, 20,200); // Total num
                }
            }

            UI.setColor(Color.RED);
            if(numPriOneTreated > 0){
                if(totalWaitTimePriOne/numPriOneTreated < 200){
                    UI.drawString("Average WaitTime PrioOne Patients (Seconds)  = " + totalWaitTimePriOne/numPriOneTreated, 770, 500-totalWaitTimePriOne/numPriOneTreated - 20);      
                    UI.fillRect(750,500- totalWaitTimePriOne/numPriOneTreated, 20,  totalWaitTimePriOne/numPriOneTreated); // Total num
                }
                else{
                    UI.drawString("Average WaitTime PrioOne Patients (Seconds)  > 200", 770, 320);      
                    UI.fillRect(750,300, 20,200); // Total num
                }
            }
        }

    }

    int getTotalTreated(){ // gets total treatemnet
        int totalPatients = 0;
        Collection <Department> values = departmentsMap.values();  
        for(Department currentDepartment : values){
            totalPatients += currentDepartment.returnAllFinishedPatients().size();     // add size of patients finished in each departmnet for total         
        }

        return totalPatients;
    }

    int getPrioOneTreated(){
        int totalPrioOnePatients = 0;
        Set <Patient> tempSet = new HashSet <Patient>();
        Collection <Department> values = departmentsMap.values();  
        for(Department currentDepartment : values){            
            tempSet.addAll(currentDepartment.returnAllFinishedPatients());             
        }
        for(Patient tempPatient : tempSet){
            if(tempPatient.getPriority() == 1){ // add only if patient was priority one
                totalPrioOnePatients++;
            }

        }
        return totalPrioOnePatients;
    }

    int getTotalWaitTime(){
        int totalWaitTime = 0;
        Collection <Department> values = departmentsMap.values();  
        for(Department currentDepartment : values){
            for(Patient tempPatient : currentDepartment.returnAllFinishedPatients()){             
                totalWaitTime += tempPatient.getWaitingTime(); // increase waiting time by waiting time ofthat patient
            }
        }

        return totalWaitTime;

    }

    int getPrioOneWaitTime(){
        int totalPrioOneWaitTime = 0;
        Collection <Department> values = departmentsMap.values();  
        for(Department currentDepartment : values){
            for(Patient tempPatient : currentDepartment.returnAllFinishedPatients()){  
                if(tempPatient.getPriority() == 1){
                    totalPrioOneWaitTime += tempPatient.getWaitingTime(); // increase waiting time by waiting time of that patient only if they are prio one
                }
            }
        }

        return totalPrioOneWaitTime;

    }

    /** 
     * Returns a random priority 1 - 3
     * Probability of a priority 1 patient should be probPri1
     * Probability of a priority 2 patient should be probPri2
     * Probability of a priority 3 patient should be (1-probPri1-probPri2)
     */
    private int randomPriority(){
        double rnd = random.nextDouble();
        if (rnd < probPri1) {return 1;}
        if (rnd < (probPri1 + probPri2) ) {return 2;}
        return 3;
    }

}
