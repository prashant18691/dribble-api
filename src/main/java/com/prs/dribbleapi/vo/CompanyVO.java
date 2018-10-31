package com.prs.dribbleapi.vo;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyVO {
    private String companyName;
    private String description;
    private String mainPhoneNumber;

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
}
