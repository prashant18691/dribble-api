package com.prs.dribbleapi.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobVO {

    private String jobTitle;
    private String jobType;
    private String availabilityType;
    private String charge;
    private String description;
    private String experience;
    private String skills;
    private String postedOn;
    private String currency;

    private Integer availability;
    private Integer expLevel;

    public JobVO() {
    }

    public JobVO(final String jobTitle, final String jobType, final String availabilityType, final String charge,
            final String description,
            final String experience, final String skills, final String currency, final String postedOn) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.availabilityType = availabilityType;
        this.charge = charge;
        this.description = description;
        this.experience = experience;
        this.skills = skills;
        this.currency = currency;
        this.postedOn = postedOn;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(final String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(final String jobType) {
        this.jobType = jobType;
    }

    public String getAvailabilityType() {
        return availabilityType;
    }

    public void setAvailabilityType(final String availabilityType) {
        this.availabilityType = availabilityType;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(final String charge) {
        this.charge = charge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(final String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(final String skills) {
        this.skills = skills;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(final String postedOn) {
        this.postedOn = postedOn;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(final Integer availability) {
        this.availability = availability;
    }

    public Integer getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(final Integer expLevel) {
        this.expLevel = expLevel;
    }
}
