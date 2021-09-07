// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 3
 * Name:
 * Username:
 * ID:
 */

//DO NOT ADD ANY IMPORTS.

/**
 * The Offcut class describes individual offcuts from a factory that
 * processes sheets expensive metals and plastics.
 * For simplicity, we assume that all the offcuts are roughly rectangular.
 * Each Offcut has fields to specify
 *  - the ID number of the offcut
 *  - the material
 *  - the length of the offcut
 *  - the width of the offcut.
 * There are methods to return the values of the different fields.
 * 
 * YOU MUST NOT MODIFY THIS CLASS!
 */

public class Offcut {
    
    // Fields
    private long id; 
    private String material;  
    private double width;
    private double length;

    // Constructor
    public Offcut(long id, String m, double l, double w){
        this.id = id;
        material = m;
        length = l;
        width = w;
    }

    // Getters
    /** Return the material */
    public String getMaterial(){ return material;}

    /** Return the length */
    public double getLength(){ return length;}

    /** Return the width */
    public double getWidth(){ return width;}

    /** Return a string description suitable for printing out */
    public String toString(){
        return String.format("%d: %s (%.1fx%.1f)", id, material, length, width);
    }

    /**
     * Since the id field specifies the offcut uniquely,
     * equality is based solely on the id field
     */
    public boolean equals(Object other){
        if (other==null || ! (other instanceof Offcut)) { return false; }
        return (this.id==((Offcut) other).id);
    }

    /**
     * Since the id field specifies the offcut uniquely,
     * the hashCode is based solely on the id field
     */
    public int hashCode(){
        return (int)(id % Integer.MAX_VALUE);
    }

}

