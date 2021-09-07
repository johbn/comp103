// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 1
 * Name:  Todd Wellwood
 * Username:  wellwotodd
 * ID:300529406
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * DeShredder allows a user to sort fragments of a shredded document ("shreds") into strips, and
 * then sort the strips into the original document.
 * The program shows
 *   - a list of all the shreds along the top of the window, 
 *   - the working strip (which the user is constructing) just below it.
 *   - the list of completed strips below the working strip.
 * The "rotate" button moves the first shred on the list to the end of the list to let the
 *  user see the shreds that have disappeared over the edge of the window.
 * The "shuffle" button reorders the shreds in the list randomly
 * The user can use the mouse to drag shreds between the list at the top and the working strip,
 *  and move shreds around in the working strip to get them in order.
 * When the user has the working strip complete, they can move
 *  the working strip down into the list of completed strips, and reorder the completed strips
 *
 */
public class DeShredder {

    // Fields to store the lists of Shreds and strips.  These should never be null.
    private List<Shred> allShreds = new ArrayList<Shred>();    //  List of all shreds
    private List<Shred> workingStrip = new ArrayList<Shred>(); // Current strip of shreds
    private List<List<Shred>> completedStrips = new ArrayList<List<Shred>>();

    // Constants for the display and the mouse
    public static final double LEFT = 20;       // left side of the display
    public static final double TOP_ALL = 20;    // top of list of all shreds 
    public static final double GAP = 5;         // gap between strips
    public static final double SIZE = Shred.SIZE; // size of the shreds

    public static final double TOP_WORKING = TOP_ALL+SIZE+GAP;
    public static final double TOP_STRIPS = TOP_WORKING+(SIZE+GAP);

    //Fields for recording where the mouse was pressed  (which list/strip and position in list)
    // note, the position may be past the end of the list!
    private List<Shred> fromStrip;   // The strip (List of Shreds) that the user pressed on
    private int fromPosition = -1;   // index of shred in the strip

    /**
     * Initialises the UI window, and sets up the buttons. 
     */
    public void setupGUI() {
        UI.addButton("Load library",   this::loadLibrary);
        UI.addButton("Rotate",         this::rotateList);
        UI.addButton("Shuffle",        this::shuffleList);
        UI.addButton("Complete Strip", this::completeStrip);
        UI.addButton("Quit",           UI::quit);
        UI.addButton("Save completed strip",    this::saveCompletedStrip);
        UI.setMouseListener(this::doMouse);
        UI.setWindowSize(1000,800);
        UI.setDivider(0);
    }

    /**
     * Asks user for a library of shreds, loads it, and redisplays.
     * Uses UIFileChooser to let user select library
     * and finds out how many images are in the library
     * Calls load(...) to construct the List of all the Shreds
     */
    public void loadLibrary(){
        Path filePath = Path.of(UIFileChooser.open("Choose first shred in directory"));
        Path directory = filePath.getParent(); //subPath(0, filePath.getNameCount()-1);
        int count=1;
        while(Files.exists(directory.resolve(count+".png"))){ count++; }
        //loop stops when count.png doesn't exist
        count = count-1;
        load(directory, count);   // YOU HAVE TO COMPLETE THE load METHOD
        display();
    }

    /**
     * Empties out all the current lists (the list of all shreds,
     *  the working strip, and the completed strips).
     * Loads the library of shreds into the allShreds list.
     * Parameters are the directory containing the shred images and the number of shreds.
     * Each new Shred needs the directory and the number/id of the shred.
     */
    public void load(Path dir, int count) {
        allShreds.clear(); // clears arrays
        workingStrip.clear(); 
        completedStrips.clear();  
        while(count > 0){ // while we still need to load in shreadsheight*.9
            Shred shredStorage = new Shred(dir,count); // create new shred storage
            allShreds.add(shredStorage); // push to Shred
            count--;
        }
    }

    /**
     * Rotate the list of all shreds by one step to the left
     * and redisplay;
     * Should not have an error if the list is empty
     * (Called by the "Rotate" button)
     */
    public void rotateList(){
        Shred shredStorage = allShreds.get(0); // get first one
        allShreds.remove(0); // remove first (array shifts)
        allShreds.add(shredStorage); // add back to the end (rotating the array)
        display();

    }

    /**
     * Shuffle the list of all shreds into a random order
     * and redisplay;
     */
    public void shuffleList(){
        Collections.shuffle(allShreds); // collections libary shuffle
        display();
    }

    /**
     * Move the current working strip to the end of the list of completed strips.
     * (Called by the "Complete Strip" button)
     */
    public void completeStrip(){
        completedStrips.add(workingStrip);  // add the working strip to completeed strip
        workingStrip = new ArrayList<Shred>(); // reset array (Cleaering the working strip) (.clear() wont work due to reference by passing)
        display();
    }

    /**
     * Simple Mouse actions to move shreds and strips
     *  User can
     *  - move a Shred from allShreds to a position in the working strip
     *  - move a Shred from the working strip back into allShreds
     *  - move a Shred around within the working strip.
     *  - move a completed Strip around within the list of completed strips
     *  - move a completed Strip back to become the working strip
     *    (but only if the working strip is currently empty)
     * Moving a shred to a position past the end of a List should put it at the end.
     * You should create additional methods to do the different actions - do not attempt
     *  to put all the code inside the doMouse method - you will lose style points for this.
     * Attempting an invalid action should have no effect.
     * Note: doMouse uses getStrip and getColumn, which are written for you (at the end).
     * You should not change them.
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){
            fromStrip = getStrip(y);      // the List of shreds to move from (possibly null)
            fromPosition = getColumn(x);  // the index of the shred to move (may be off the end)
        }
        if (action.equals("released")){
            List<Shred> toStrip = getStrip(y); // the List of shreds to move to (possibly null)
            int toPosition = getColumn(x);     // the index to move the shred to (may be off the end)
            // perform the correct action, depending on the from/to strips/positions
            if(toPosition >= 0){
                if(fromStrip == workingStrip && toStrip == workingStrip){ // if moving from working to working
                    moveAroundWorkingStrip(toPosition);
                }

                else if(fromStrip == allShreds && toStrip == workingStrip){ // if moving from all to working
                    moveFromAllToWorking(toPosition);
                }

                else if(fromStrip == workingStrip && toStrip == allShreds){ // handles moving from working to all
                    moveFromWorkingToAll(toPosition);
                }

                else if (fromStrip != allShreds && fromStrip != workingStrip) {  // handles moving from completed to completed
                    if(fromStrip != null){   // if it is not empty
                        if(toStrip == workingStrip){ // if we are trying to move to working
                            if(workingStrip.isEmpty()){ // first check that the working strip is empty (And only run if it is)
                                completedToWorking(toPosition);  // move if it is empty
                            }
                        }
                        else if(toStrip != allShreds) { // if they are not trying to move to working or all
                            int row = (int) ((y-TOP_ALL)/(SIZE+GAP)); // used for passing which row the user is tryna move completed too
                            completedStripFunction(row); // pass row to completed function
                            display();
                        }
                    }
                }
            }
            display();
        }
    }

    // Additional methods to perform the different actions, called by doMouse
    public void moveAroundWorkingStrip(int toPositon){ // move around inside of working
        if(toPositon < workingStrip.size()){ // if within bounds
            Shred shredStorage = workingStrip.get(fromPosition); // get it from where user clicked
            workingStrip.remove(fromPosition); // remove the one clicked on
            workingStrip.add(toPositon,shredStorage); // add it back where they released mouse
        }
    }

    public void moveFromAllToWorking(int toPositon){ // all to working
        if(fromPosition < allShreds.size()){
            Shred shredStorage = allShreds.get(fromPosition); // get strip where they selected
            allShreds.remove(fromPosition);  // remove it
            if(toPositon < workingStrip.size()){ // if mouse is released on a positon inside array
                workingStrip.add(toPositon,shredStorage);  // add to position they released on
            } 
            else{ // if not released on position inside array add on the end
                workingStrip.add(shredStorage); // add back to the end
            }
        }
    }

    public void moveFromWorkingToAll(int toPositon){ // handles moving from working to all
        if(fromPosition <= workingStrip.size() && !workingStrip.isEmpty()){
            Shred shredStorage = workingStrip.get(fromPosition);
            workingStrip.remove(fromPosition);
            if(toPositon < allShreds.size()){ // if it will fit
                allShreds.add(toPositon,shredStorage); 
            } 
            else{ // if not fit add on the end
                allShreds.add(shredStorage);
            }
        }
    }

    public void completedStripFunction(int row){ // move completed around completed
        if(row-2 < completedStrips.size()){ // if within bounds
            List<Shred> stripStorage = fromStrip;
            completedStrips.remove(fromStrip);
            completedStrips.add(row-2,stripStorage); // row -2 to account for the working strip and completed strips           
        }
    }

    public void completedToWorking(int toPositon){ // move a completed to be the working
        List<Shred> stripStorage = fromStrip; // store
        completedStrips.remove(fromStrip); // delete
        workingStrip.addAll(stripStorage); // replace
    }

    //=============================================================================
    // Completed for you. Do not change.
    // loadImage and saveImage may be useful for the challenge.

    /**
     * Displays the remaining Shreds, the working strip, and all completed strips
     */
    public void display(){
        UI.clearGraphics();

        // list of all the remaining shreds that haven't been added to a strip
        double x=LEFT;
        for (Shred shred : allShreds){
            shred.drawWithBorder(x, TOP_ALL);
            x+=SIZE;
        }

        //working strip (the one the user is workingly working on)
        x=LEFT;
        for (Shred shred : workingStrip){
            shred.draw(x, TOP_WORKING);
            x+=SIZE;
        }
        UI.setColor(Color.red);
        UI.drawRect(LEFT-1, TOP_WORKING-1, SIZE*workingStrip.size()+2, SIZE+2);
        UI.setColor(Color.black);

        //completed strips
        double y = TOP_STRIPS;
        for (List<Shred> strip : completedStrips){
            x = LEFT;
            for (Shred shred : strip){
                shred.draw(x, y);
                x+=SIZE;
            }
            UI.drawRect(LEFT-1, y-1, SIZE*strip.size()+2, SIZE+2);
            y+=SIZE+GAP;
        }
    }

    /**
     * Returns which column the mouse position is on.
     * This will be the index in the list of the shred that the mouse is on, 
     * (or the index of the shred that the mouse would be on if the list were long enough)
     */
    public int getColumn(double x){
        return (int) ((x-LEFT)/(SIZE)); 
    }

    /**
     * Returns the strip that the mouse position is on.
     * This may be the list of all remaining shreds, the working strip, or
     *  one of the completed strips.
     * If it is not on any strip, then it returns null.
     */
    public List<Shred> getStrip(double y){
        int row = (int) ((y-TOP_ALL)/(SIZE+GAP));
        if (row<=0){
            return allShreds;
        }
        else if (row==1){
            return workingStrip;
        }
        else if (row-2<completedStrips.size()){
            return completedStrips.get(row-2);
        }
        else {
            return null;
        }
    }

    public static void main(String[] args) {
        DeShredder ds =new DeShredder();
        ds.setupGUI();

    }

    /**
     * Load an image from a file and return as a two-dimensional array of Color.
     * From COMP 102 assignment 8&9.
     * Maybe useful for the challenge. Not required for the core or completion.
     */
    public Color[][] loadImage(String imageFileName) {
        if (imageFileName==null || !Files.exists(Path.of(imageFileName))){
            return null;
        }
        try {
            BufferedImage img = ImageIO.read(Files.newInputStream(Path.of(imageFileName)));
            int rows = img.getHeight();
            int cols = img.getWidth();
            Color[][] ans = new Color[rows][cols];
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){                 
                    Color c = new Color(img.getRGB(col, row));
                    ans[row][col] = c;
                }
            }
            return ans;
        } catch(IOException e){UI.println("Reading Image from "+imageFileName+" failed: "+e);}
        return null;
    }

    /**
     * Save a 2D array of Color as an image file
     * From COMP 102 assignment 8&9.
     * Maybe useful for the challenge. Not required for the core or completion.
     */
    public  void saveImage(Color[][] imageArray, String imageFileName) {
        int rows = imageArray.length;
        int cols = imageArray[0].length;
        BufferedImage img = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Color c =imageArray[row][col];
                img.setRGB(col, row, c.getRGB());
            }
        }
        try {
            if (imageFileName==null) { return;}
            ImageIO.write(img, "png", Files.newOutputStream(Path.of(imageFileName)));
        } catch(IOException e){UI.println("Image reading failed: "+e);}

    }

    /**
     ** Handles saving the entire completed strips as an image
     **/

    public void saveCompletedStrip(){
        if(!completedStrips.isEmpty()){ // protection for trying to save an empty strip
            Color[][] tempStrip; // new temp strip
            int longestStrip = 0; // longest strip starts at 0;
            // finds longest strip
            for(int i = 0; i < completedStrips.size(); i++){    // for all strips
                if((completedStrips.get(i)).size() > longestStrip){ // if the completedstrip's total amount of shreads is longer than the current longest strip
                    longestStrip = (completedStrips.get(i)).size(); // then set the longest to be the size of the strip
                }
            }

            int row = completedStrips.size() * (int) SIZE; // row is the ammount of strips * size
            int col = longestStrip * (int) SIZE; // col is the longest strip previously found * size
            BufferedImage finalImage = new BufferedImage(col, row, BufferedImage.TYPE_INT_RGB); // create the final image

            // sets all colours in the image array to white
            for (int i = 0; i < row; i++) { // rows
                for (int j = 0; j < col; j++) { // cols    
                    Color myWhite = new Color(255, 255, 255); // Color white
                    int rgb = myWhite.getRGB();//get it as an int
                    finalImage.setRGB(j,i,rgb); // set colour white
                }
            }

            // paints the image
            for(int i = 0; i < completedStrips.size(); i++){         // for the ammount of strips (not row or col so that we dont try paint where we dont have a shred)
                for(int j = 0; j < completedStrips.get(i).size(); j++){ // then for each strip inside of the completedStrips 
                    Shred currentShred = (completedStrips.get(i)).get(j);// gets the shred
                    String currentShredName = currentShred.getFileName(); // get the name of the file
                    tempStrip = loadImage(currentShredName);  // load the image for scanning
                    for(int x = 0; x < SIZE; x++){// within the image x
                        for(int y = 0; y < SIZE; y++){ // within the image y
                            Color c = tempStrip[x][y]; // get pixel at xy
                            finalImage.setRGB(y+(int) (j*SIZE),x+(int) (i*SIZE), c.getRGB()); // paint at the correct position on the final image
                        }
                    }
                }
            }
            try{
                ImageIO.write(finalImage, "png", Files.newOutputStream(Path.of(UIFileChooser.save()))); // write the image to drive.
            }
            catch(IOException e){UI.println("ERROR SAVING");} // incase of a cancel

        }
    }
}