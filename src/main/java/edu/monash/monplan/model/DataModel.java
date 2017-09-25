package edu.monash.monplan.model;

public abstract class DataModel {

    /***
     * IMPORTANT: Declare the following fields in all subclasses
     * public static String nameField = "";
     * public static String codeField = "";
    ***/

    public abstract void setId(String id);

    public abstract String getId();


    public abstract String fetchCode();

    public abstract String fetchName();

    /**
     * Prevents accidentally re-initialising an object that is retrieved from the data layer.
     */
    public abstract void init();

}
