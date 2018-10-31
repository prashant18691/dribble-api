package com.prs.dribbleapi.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prs.dribbleapi.dto.Availability;
import com.prs.dribbleapi.dto.ExperienceLevel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobVO {

    private String jobTitle;
    private String jobType;
    private Availability availability;
    private String charge;
    private String description;
    private ExperienceLevel expLevel;
    private String skills;
    private String postedOn;
    private String currency;

    public JobVO(final String jobTitle, final String jobType, final Availability availability, final String charge,
            final String description,
            final ExperienceLevel expLevel, final String skills, final String postedOn, final String currency) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.availability = availability;
        this.charge = charge;
        this.description = description;
        this.expLevel = expLevel;
        this.skills = skills;
        this.postedOn = postedOn;
        this.currency = currency;
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

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(final Availability availability) {
        this.availability = availability;
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

    public ExperienceLevel getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(final ExperienceLevel expLevel) {
        this.expLevel = expLevel;
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
}
