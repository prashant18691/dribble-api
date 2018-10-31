package com.prs.dribbleapi.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "JOB_DETAILS")
public class Job implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_JOB", sequenceName="SEQ_GEN_JOB", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_JOB")
    private Integer jobId;
    @NotNull
    @Column(unique = true)
    private String jobTitle;
    @NotNull
    private String jobType;
    @Column(name = "AVAILABILITY_TYPE")
    @NotNull
    private String availability;
    @NotNull
    private String charge;
    @NotNull
    private String currency;
    @NotNull
    private String description;
    @NotNull
    private String expLevel;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date postedOn;
    @NotNull
    private String skills;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid", nullable = false)
    private Location location;

    @Transient
    private String availabilityType;

    @Transient
    private String experience;

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

    public String getCharge() {
        return charge;
    }

    public void setCharge(final String charge) {
        this.charge = charge;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(final Date postedOn) {
        this.postedOn = postedOn;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(final String skills) {
        this.skills = skills;
    }

    public String getAvailabilityType() {
        return availabilityType;
    }

    public void setAvailabilityType(final String availabilityType) {
        this.availabilityType = availabilityType;
    }

    public String getExperience() {
        return experience;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(final Integer jobId) {
        this.jobId = jobId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public void setExperience(final String experience) {
        this.experience = experience;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(final String availability) {
        this.availability = availability;
    }

    public String getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(final String expLevel) {
        this.expLevel = expLevel;
    }
}
