package com.prs.dribbleapi.vo;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DribbleVO {
    private CompanyVO company;

    public CompanyVO getCompany() {
        return company;
    }

    public void setCompany(final CompanyVO company) {
        this.company = company;
    }
}
