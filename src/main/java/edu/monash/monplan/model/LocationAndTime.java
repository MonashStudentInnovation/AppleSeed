package edu.monash.monplan.model;

import java.util.List;

public class LocationAndTime {
    private String location;
    private List<String> time;

    public LocationAndTime(){
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}
