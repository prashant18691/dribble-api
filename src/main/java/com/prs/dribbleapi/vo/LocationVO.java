package com.prs.dribbleapi.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationVO {
    private String state;
    private String province;
    private String country;
    private String description;
    private String phoneNumber;
    private List<JobVO> jobs;

    public LocationVO() {
    }

    public LocationVO(final String state, final String province, final String country, final String description,
            final String phoneNumber) {
        this.state = state;
        this.province = province;
        this.country = country;
        this.description = description;
        this.phoneNumber = phoneNumber;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<JobVO> getJobs() {
        return jobs;
    }

    public void setJobs(final List<JobVO> jobs) {
        this.jobs = jobs;
    }
}
