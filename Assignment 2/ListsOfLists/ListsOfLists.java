// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 2
 * Name:Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

//DO NOT CHANGE THESE IMPORTS OR ADD ANY FURTHER IMPORTS.
import ecs100.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/** ListsOfLists */
public class ListsOfLists{
    /**
     * Given a list of lists of Things, append all the lists into a single list and return it.
     * The method should not modify the lists.
     */
    public List<Thing> appendAll(List<List<Thing>> lists){
        List<Thing> allThings = new ArrayList <Thing>();
        for(List tempList : lists){ // for all lists
            allThings.addAll(tempList);// add all elements of that list to one list
        }

        return allThings;   // return all lists
    }

    /**
     * Given a list of lists of Things,
     * merge the lists into a single list, containing all the first elements of the lists,
     * followed by all the second elements of the lists, etc.
     * The lists do not need to be the same size
     * The method should not modify the lists.
     */
    public List<Thing> mergeZipped(List<List<Thing>> lists){      
        List <Thing> finalList = new ArrayList <Thing>();
        int longestList = 0;
        for(int i = 0; i < lists.size(); i++){ // find longest list
            if(lists.get(i).size() > longestList){
                longestList = lists.get(i).size();
            }
        }
        // Tyler green gave me a hint on how to impliment this
        for(int j = 0; j < longestList; j++){ // how many total times it will need to merge
            for(int x = 0; x < lists.size();x++){ // run for each list
                if(j <= lists.get(x).size()-1){ // if the element j exists inside list X
                    finalList.add(lists.get(x).get(j)); // then add it (Repeates untill merge is complete)
                }
            }

        }
        return finalList;  
    }

    /**
     * Given a list of _sorted_ lists of Things (ie, the Things in each list are in order)
     * Merge the lists into a single list, in sorted order.
     * None of the lists contain null.
     * At each step:
     *  Find the next item to add to the answer by looking at the front item of each list to find the smallest.
     *  Move that item from the list to the answer.
     * The lists may not be the same size
     * The method may not modify the lists
     */
    public List<Thing> mergeSorted(List<List<Thing>> lists){
           
        
        /**Makes Editable copy of the list***/
        List <List<Thing>> editableList = new ArrayList <List<Thing>>();
        List <Thing> pushingList = new ArrayList<Thing>();

        for(List<Thing> tempList : lists){ // for all lists
            for(Thing tempThing : tempList){  // for all things in those lists
                pushingList.add(tempThing); // add those things
            }
            editableList.add(pushingList); // add the pushing list to the editable list
            pushingList = new ArrayList<Thing>(); // reinitialize so it has no data
        }

        /** Makes final list for returning**/
        List<Thing> finalList = new ArrayList<Thing>(); // Final List for returning (Will have values addedto it one by one)

        /**Actually runs through**/
        while(!editableList.isEmpty()){ // Runs untill the editable list is empty once empty it means all values have been added to the finalList        
            int indexOfCurrentList = 0; // What list index has the first item be the best
            
            for(int i = 0; i < editableList.size(); i++){ // runs through every list     
                Thing newItem = editableList.get(i).get(0);  // gets the first item of each list each time it runs        
                Thing currentItem = editableList.get(indexOfCurrentList).get(0); // resets the current most alphabetical item
                if(currentItem.compareTo(newItem) > 0){  //Compares items to find if the next item is suppost to be ordered before the current best        
                    indexOfCurrentList = i;   // if so we need to update the index of that the best found list to be that list
                }
            }
            
            finalList.add(editableList.get(indexOfCurrentList).get(0)); // add the first item from the previously found list
            editableList.get(indexOfCurrentList).remove(0); // remove that thing from editable lists

            if(editableList.get(indexOfCurrentList).isEmpty()){ // if the list is now empty
                editableList.remove(indexOfCurrentList); // remove it from the editable list (So It cant be added again)
            }
        }
        return finalList; // Returns the final list
    }

    //--------- TESTING --------------------------------------------

    /**
     * Setup the GUI
     */
    public void setupGUI(){
        UI.addButton("AppendAll", this::testAppendAll);
        UI.addButton("MergeZipped", this::testMergeZipped);
        UI.addButton("MergeSorted", this::testMergeSorted);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    /** Test the appendAll method */
    public void testAppendAll(){
        List<List<Thing>> lists = new ArrayList<List<Thing>>();
        for (int i=0; i<4; i++){
            lists.add(Thing.makeList(UI.askString("Enter letters for list:")));
        }
        UI.println("appendAll -> ");
        UI.println(appendAll(lists));
    }

    /** Test the mergeZipped method */
    public void testMergeZipped(){
        List<List<Thing>> lists = new ArrayList<List<Thing>>();
        for (int i=0; i<4; i++){
            lists.add(Thing.makeList(UI.askString("Enter letters for list:")));
        }
        UI.println("mergeZipped -> ");
        UI.println(mergeZipped(lists));
    }

    /** Test the mergeSorted method */
    public void testMergeSorted(){
        List<List<Thing>> lists = new ArrayList<List<Thing>>();
        for (int i=0; i<4; i++){
            lists.add(Thing.makeSortedList(UI.askString("Enter letters for list:")));
        }
        UI.println("mergeSorted of ");
        for (List list : lists){
            UI.println(list);
        }
        UI.println("  -> ");
        UI.println(mergeSorted(lists));
    }

    public static void main(String[] arguments){
        new ListsOfLists().setupGUI();
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
        List<List<Thing>> lists = new ArrayList<List<Thing>>();
        List<Thing> list = appendAll(lists);
        list = mergeZipped(lists);
        list = mergeSorted(lists);
    }

}
