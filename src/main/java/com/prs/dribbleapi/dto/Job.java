package com.prs.dribbleapi.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "JOB_DETAILS")
public class Job implements Serializable{
    @Id
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

    public Job() {
    }

    public Job(@NotNull final String jobTitle, @NotNull final String jobType,
            @NotNull final String availability, @NotNull final String charge,
            @NotNull final String currency, @NotNull final String description,
            @NotNull final String expLevel, @NotNull final Date postedOn, @NotNull final String skills, Location location) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.availability = availability;
        this.charge = charge;
        this.currency = currency;
        this.description = description;
        this.expLevel = expLevel;
        this.postedOn = postedOn;
        this.skills = skills;
        this.location = location;
        this.jobId = Objects.hash(jobTitle,jobType,availability,expLevel,location.getLocationId());
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

    @Override public int hashCode() {
        return Objects.hash(getJobTitle(),getJobType(),getAvailability(),getExpLevel(),getLocation().getLocationId());
    }

    @Override public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Job)) return false;
        Job that = (Job) obj;
        return Objects.equals(getJobTitle(),that.getJobTitle()) && Objects.equals(getJobType(),that.getJobType()) &&
                Objects.equals(getAvailability(),that.getAvailability()) && Objects.equals(getExpLevel(),that
                .getExpLevel()) && Objects.equals(getLocation().getLocationId(),that.getLocation().getLocationId());
    }
}
