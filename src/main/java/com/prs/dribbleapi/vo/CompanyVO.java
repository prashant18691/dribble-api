package com.prs.dribbleapi.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyVO {
    private String companyName;
    private String description;
    private String mainPhoneNumber;
    private List<LocationVO> locations;

    public CompanyVO() {
    }

    public CompanyVO(final String companyName, final String description, final String mainPhoneNumber) {
        this.companyName = companyName;
        this.description = description;
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public void setMainPhoneNumber(final String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public List<LocationVO> getLocations() {
        return locations;
    }

    public void setLocations(final List<LocationVO> locations) {
        this.locations = locations;
    }
}
