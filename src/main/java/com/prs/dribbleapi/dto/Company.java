package com.prs.dribbleapi.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


@Entity
public class Company implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_COMPANY", sequenceName="SEQ_GEN_COMPANY", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_COMPANY")
    private Integer companyId;
    @NotNull
    @Column(unique = true)
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
}
