package com.prs.dribbleapi.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;


@Entity
public class Company implements Serializable{
    @Id
    private Integer companyId;
    @NotNull
    private String companyName;
    @NotNull
    private String description;
    @NotNull
    private String mainPhoneNumber;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "company")
    private Set<Location> locations;

    public Company(@NotNull final String companyName, @NotNull final String description,
            @NotNull final String mainPhoneNumber) {
        this.companyName = companyName;
        this.description = description;
        this.mainPhoneNumber = mainPhoneNumber;
        this.companyId = Objects.hash(companyName);
    }

    public Company() {
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final Integer companyId) {
        this.companyId = companyId;
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

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(final Set<Location> locations) {
        this.locations = locations;
    }

    @Override public int hashCode() {
        return Objects.hash(getCompanyName());
    }

    @Override public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Company)) return false;
        Company that = (Company) obj;
        return Objects.equals(getCompanyName(),that.getCompanyName());
    }
}
