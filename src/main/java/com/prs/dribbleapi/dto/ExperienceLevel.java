package com.prs.dribbleapi.dto;

import java.util.HashMap;
import java.util.Map;


public enum ExperienceLevel {
    FRESHER(1),INTERMEDIATE(2),EXPERT(3);

    private final int value;

    public static Map<Integer,ExperienceLevel> experienceLevelMap = new HashMap<>();

    static {
        for (ExperienceLevel experienceLevel : ExperienceLevel.values())
            experienceLevelMap.put(experienceLevel.value, experienceLevel);
    }

    public int getValue(){
        return value;
    }

    private ExperienceLevel(int value){
        this.value = value;
    }

    public static ExperienceLevel fromValue(String value){
        return ExperienceLevel.valueOf(value);
    }
}
