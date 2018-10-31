package com.prs.dribbleapi.vo;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DribbleVO {
    private CompanyVO company;
    private LocationVO location;
    private JobVO job;

    public CompanyVO getCompany() {
        return company;
    }

    public void setCompany(final CompanyVO company) {
        this.company = company;
    }

    public LocationVO getLocation() {
        return location;
    }

    public void setLocation(final LocationVO location) {
        this.location = location;
    }

    public JobVO getJob() {
        return job;
    }

    public void setJob(final JobVO job) {
        this.job = job;
    }
}
