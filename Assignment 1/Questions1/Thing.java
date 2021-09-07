// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 1
 * Name:  Todd Wellwood
 * Username:  wellwotodd
 * ID:300529406
 */

/** A Thing is an object that has a name.
 * Things can be printed, compared with equals, and compared according to alphabetical order.
*/

public class Thing{
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

    /** toString returns the name */
    public String toString(){
        return name;
    }


}
