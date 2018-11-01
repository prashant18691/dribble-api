package com.prs.dribbleapi.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "LOCATION_DETAILS")
public class Location implements Serializable{
    @Id
    private Integer locationId;
    @NotNull
    private String state;
    @NotNull
    private String province;
    @NotNull
    private String country;
    @NotNull
    private String description;
    @NotNull
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyid", nullable = false)
    private Company company;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "location")
    private Set<Job> jobs;

    public Location() {
    }

    public Location(@NotNull final String state, @NotNull final String province,
            @NotNull final String country, @NotNull final String description,
            @NotNull final String phoneNumber, Company company) {
        this.state = state;
        this.province = province;
        this.country = country;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.locationId = Objects.hash(state,province,company.getCompanyId());
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(final Integer locationId) {
        this.locationId = locationId;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(final String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(final Set<Job> jobs) {
        this.jobs = jobs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
    }

    @Override public int hashCode() {
        return Objects.hash(getState(),getProvince(),getCompany().getCompanyId());
    }

    @Override public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Location)) return false;
        Location that = (Location) obj;
        return Objects.equals(getState(),that.getState()) && Objects.equals(getProvince(),that.getProvince()) &&
                Objects.equals(getCompany().getCompanyId(),that.getCompany().getCompanyId());
    }
}
