// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 2
 * Name:
 * Username:
 * ID:
 */

import java.util.*;

/** A Thing is an object that has a name.
 * Things can be printed, compared with equals, and compared according to alphabetical order.
 */

public class Thing implements Comparable<Thing> {
    private String name;

    public Thing(String s){
        if(s==null||s.length()==0){
            name = "@";
        }
        else{
            name = s;
        }
    }

    public boolean equals(Object other){
        if (other==null){return false;}
        if (! (other instanceof Thing)) {return false;}
        return (this.name.equals(((Thing)other).name));
    }

    public int compareTo(Thing other){
        return this.name.compareTo(other.name);
    }

    /** toString returns a random value */
    public String toString(){
        return name; //""+Math.random();
    }

    /** Returns an unmodifiable list of Things for testing */
    public static List<Thing> makeList(String str){
        List<Thing> ans = new ArrayList<Thing>();
        for (int i=0; i<str.length(); i++){
            String nm = str.substring(i, i+1);
            if (!nm.equals(" ")) {ans.add(new Thing(nm));}
        }
        return Collections.unmodifiableList(ans);
    }

    /** Returns an unmodifiable, sorted list of Things for testing */
    public static List<Thing> makeSortedList(String str){
        List<Thing> ans = new ArrayList<Thing>();
        for (int i=0; i<str.length(); i++){
            String nm = str.substring(i, i+1);
            if (!nm.equals(" ")) {ans.add(new Thing(nm));}
        }
        Collections.sort(ans);
        return Collections.unmodifiableList(ans);
    }

}
