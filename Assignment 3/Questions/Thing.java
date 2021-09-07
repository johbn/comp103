// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 3
 * Name:
 * Username:
 * ID:
 */

/** A Thing is an object that has a name.
 * Things can be printed, compared, and used in hashed collections.
*/

public class Thing implements Comparable<Thing>{
    private String name;

    public Thing(String s){
        if(s==null||s.length()==0){
            name = "@";
        }
        else{
            name = s.toUpperCase();
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

    public int hashCode(){
        return name.hashCode();
    }

    /** toString returns name */
    public String toString(){
        return name;
    }


}
