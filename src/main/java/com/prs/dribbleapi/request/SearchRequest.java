package com.prs.dribbleapi.request;

import java.util.List;


public class SearchRequest {

    private List<String> skills;
    private List<String> availabilities;
    private String jobType;
    private String jobTitle;
    private String showJobsWithoutPayRate = "YES";
    private int payRatefrom;
    private int payRateto;
    private String experienceLevel;
    private String location;
    private String availabilityCriteria;
    private String experienceCriteria;
    private String skillsCriteria;

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(final List<String> skills) {
        this.skills = skills;
    }

    public List<String> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(final List<String> availabilities) {
        this.availabilities = availabilities;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(final String jobType) {
        this.jobType = jobType;
    }

    public String getShowJobsWithoutPayRate() {
        return showJobsWithoutPayRate;
    }

    public void setShowJobsWithoutPayRate(final String showJobsWithoutPayRate) {
        this.showJobsWithoutPayRate = showJobsWithoutPayRate;
    }

    public int getPayRatefrom() {
        return payRatefrom;
    }

    public void setPayRatefrom(final int payRatefrom) {
        this.payRatefrom = payRatefrom;
    }

    public int getPayRateto() {
        return payRateto;
    }

    public void setPayRateto(final int payRateto) {
        this.payRateto = payRateto;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(final String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getAvailabilityCriteria() {
        return availabilityCriteria;
    }

    public void setAvailabilityCriteria(final String availabilityCriteria) {
        this.availabilityCriteria = availabilityCriteria;
    }

    public String getExperienceCriteria() {
        return experienceCriteria;
    }

    public String getSkillsCriteria() {
        return skillsCriteria;
    }

    public void setSkillsCriteria(final String skillsCriteria) {
        this.skillsCriteria = skillsCriteria;
    }

    public void setExperienceCriteria(final String experienceCriteria) {
        this.experienceCriteria = experienceCriteria;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(final String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
