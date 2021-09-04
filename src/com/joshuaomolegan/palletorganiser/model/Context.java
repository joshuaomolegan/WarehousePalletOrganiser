package com.joshuaomolegan.palletorganiser.model;

/** Class to provide a shared model for all controllers*/
public class Context {
    private final static Context instance = new Context();

    /** Getter method for instance
     * @return static instance of Context class*/
    public static Context getInstance() {
        return instance;
    }

    private Organiser organiser = new Organiser();

    /** Function to return organiser object
     * @return organiser object to be shared between controllers*/
    public Organiser currentOrganiser(){
        return organiser;
    }
}
