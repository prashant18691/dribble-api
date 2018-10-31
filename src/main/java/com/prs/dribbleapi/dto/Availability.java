package com.prs.dribbleapi.dto;

import java.util.HashMap;
import java.util.Map;


public enum  Availability {
    HOURLY(1), PARTTIME(2), FULLTIME(3);

    private int value;

    public  int getValue(){
        return value;
    }
    public static Map<Integer,Availability> availabilityMap = new HashMap<>();
    static {
        for (Availability availability : Availability.values())
            availabilityMap.put(availability.value, availability);
    }

    private Availability(int value){
        this.value = value;
    }

    public static Availability fromValue(String value){
        return Availability.valueOf(value);
    }
}
